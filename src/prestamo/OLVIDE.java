package prestamo;

import CONEXION.CONEXION;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class OLVIDE extends javax.swing.JFrame {
    // Componentes de interfaz
    
    
    // Mapa para almacenar intentos (cedula -> [intentos, timestamp])
    private static final Map<String, Pair<Integer, Long>> intentosPorCedula = new HashMap<>();
    private static final int MAX_INTENTOS = 3;
    private static final long TIEMPO_BLOQUEO_MINUTOS = 60; // 1 hora en minutos
    
    public OLVIDE() {
        initComponents();
        setResizable(false);
        configurarComponentes();
    }
    
    
    
    private void configurarComponentes() {
        // Configurar formatos de fecha
        fecha_nac.setDateFormatString("dd/MM/yyyy");
        fecha_exp.setDateFormatString("dd/MM/yyyy");
        
        // Deshabilitar campos inicialmente
        con_nueva.setEnabled(false);
        con_confirmar.setEnabled(false);
        guardar.setEnabled(false);
        
        // Listeners
        validar.addActionListener(e -> validarDatos());
        guardar.addActionListener(e -> cambiarContraseña());
        cancelar.addActionListener(e -> volverALogin());
    }
    
    private boolean validarDatos() {
    String cedulaInput = cedula.getText().trim();
    String correoInput = correo.getText().trim().toLowerCase();

    // Verificar campos vacíos
    if (cedulaInput.isEmpty() || correoInput.isEmpty() || 
        fecha_nac.getDate() == null || fecha_exp.getDate() == null) {
        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
        return false;
    }

    try (Connection con = CONEXION.conectar()) {
        // **Nuevo SQL para verificar estado y datos del usuario**
        String sql = "SELECT estado, COUNT(*) AS coincidencias FROM usuarios WHERE cedula = ? " +
                     "AND correo = LOWER(?) " +
                     "AND fecha_nac = ? " +
                     "AND fecha_exp = ? GROUP BY estado";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedulaInput);
            ps.setString(2, correoInput);
            ps.setDate(3, new java.sql.Date(fecha_nac.getDate().getTime()));
            ps.setDate(4, new java.sql.Date(fecha_exp.getDate().getTime()));
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String estado = rs.getString("estado");
                int coincidencias = rs.getInt("coincidencias");
                
                if ("INACTIVO".equalsIgnoreCase(estado)) {
                    JOptionPane.showMessageDialog(this, 
                        "El usuario está bloqueado. Contacte al administrador para desbloquearlo.",
                        "Cuenta Bloqueada", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                
                if (coincidencias > 0) {
                    // **Reiniciar intentos y habilitar campos de contraseña**
                    reiniciarIntentos(cedulaInput, con);
                    habilitarCamposContraseña(true);
                    return true;
                } else {
                    // **Incrementar intentos y bloquear si es necesario**
                    incrementarIntentos(cedulaInput, con);
                    return false;
                }
            } else {
                // **Sin coincidencias (datos incorrectos)**
                incrementarIntentos(cedulaInput, con);
                return false;
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, 
            "Error de conexión a la base de datos: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return false;
    }
}
    private boolean estaBloqueada(String cedula) {
        Pair<Integer, Long> intentos = intentosPorCedula.get(cedula);
        if (intentos == null) return false;
        
        long minutosTranscurridos = (System.currentTimeMillis() - intentos.getRight()) / (60 * 1000);
        
        // Si pasó el tiempo de bloqueo, reiniciar contador
        if (minutosTranscurridos > TIEMPO_BLOQUEO_MINUTOS) {
            intentosPorCedula.remove(cedula);
            return false;
        }
        
        return intentos.getLeft() >= MAX_INTENTOS;
    }
    
    private void incrementarIntentos(String cedula, Connection con) throws SQLException {
    String sql = "SELECT intentos FROM usuarios WHERE cedula = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, cedula);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            int intentos = rs.getInt("intentos") + 1;
            
            // **Actualizar intentos**
            sql = "UPDATE usuarios SET intentos = ? WHERE cedula = ?";
            try (PreparedStatement psUpdate = con.prepareStatement(sql)) {
                psUpdate.setInt(1, intentos);
                psUpdate.setString(2, cedula);
                psUpdate.executeUpdate();
            }
            
            if (intentos >= 3) {
                // **Bloquear usuario**
                sql = "UPDATE usuarios SET estado = 'INACTIVO' WHERE cedula = ?";
                try (PreparedStatement psBloqueo = con.prepareStatement(sql)) {
                    psBloqueo.setString(1, cedula);
                    psBloqueo.executeUpdate();
                }
                
                JOptionPane.showMessageDialog(this,
                    "El usuario ha sido bloqueado después de 3 intentos fallidos.",
                    "Cuenta Bloqueada", JOptionPane.WARNING_MESSAGE);
            } else {
                int intentosRestantes = 3 - intentos;
                JOptionPane.showMessageDialog(this,
                    "Datos incorrectos. Intentos restantes: " + intentosRestantes,
                    "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}

    
    private void reiniciarIntentos(String cedula, Connection con) throws SQLException {
    String sql = "UPDATE usuarios SET intentos = 0 WHERE cedula = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, cedula);
        ps.executeUpdate();
    }
}
    
    private void mostrarMensajeIntentoFallido(String cedula) {
        int intentos = intentosPorCedula.get(cedula).getLeft();
        int restantes = MAX_INTENTOS - intentos;
        
        if (restantes > 0) {
            JOptionPane.showMessageDialog(this,
                "Datos incorrectos. Intentos restantes: " + restantes,
                "Error", JOptionPane.WARNING_MESSAGE);
        } else {
            mostrarMensajeBloqueo();
        }
    }
    
    private void mostrarMensajeBloqueo() {
        String mensaje = "<html><div style='width:300px;'>"
            + "<h3 style='color:red;text-align:center;'>CUENTA BLOQUEADA</h3>"
            + "<p>Has excedido el número máximo de intentos permitidos.</p>"
            + "<p>Por razones de seguridad, tu cuenta ha sido bloqueada temporalmente.</p>"
            + "<p><b>Contacta al área de soporte:</b></p>"
            + "<ul>"
            + "<li>Teléfono: (7) 635-1234</li>"
            + "<li>Email: soporte@udi.edu.co</li>"
            + "<li>Oficina: Bloque Administrativo, Piso 2</li>"
            + "</ul>"
            + "<p>Horario: Lunes a Viernes 8:00 AM - 5:00 PM</p>"
            + "</div></html>";
        
        JLabel label = new JLabel(mensaje);
        JOptionPane.showMessageDialog(this, label, "Cuenta Bloqueada", JOptionPane.ERROR_MESSAGE);
    }
    
    private void habilitarCamposContraseña(boolean habilitar) {
        con_nueva.setEnabled(habilitar);
        con_confirmar.setEnabled(habilitar);
        guardar.setEnabled(habilitar);
        
        if (!habilitar) {
            con_nueva.setText("");
            con_confirmar.setText("");
        }
    }
    
    private boolean cambiarContraseña() {
    String nueva = new String(con_nueva.getPassword()).trim();
    String confirmacion = new String(con_confirmar.getPassword()).trim();
    
    // Validaciones previas (se mantienen igual)
    if (nueva.isEmpty() || confirmacion.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ambos campos son obligatorios");
        return false;
    }
    
    if (!nueva.equals(confirmacion)) {
        JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
        return false;
    }
    
   
    try (Connection con = CONEXION.conectar()) {
        // SOLUCIÓN CLAVE: Usar comillas dobles para el nombre de columna
        String sql = "UPDATE usuarios SET \"CONTRASEÑA\" = ? WHERE cedula = ?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nueva);
            ps.setInt(2, Integer.parseInt(cedula.getText().trim()));
            
            int filasActualizadas = ps.executeUpdate();
            
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Contraseña actualizada exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new LOGIN().setVisible(true);
                return true;
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se encontró el usuario para actualizar",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Error al actualizar contraseña: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return false;
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "La cédula debe contener solo números",
            "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
    
    // Clase auxiliar Pair para manejar los intentos y timestamp
    private static class Pair<L, R> {
        private final L left;
        private final R right;
        
        public Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }
        
        public L getLeft() { return left; }
        public R getRight() { return right; }
        
        public static <L, R> Pair<L, R> of(L left, R right) {
            return new Pair<>(left, right);
        }
    }

    private void volverALogin() {
        this.dispose();
        new LOGIN().setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cedula = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        correo = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        fecha_nac = new com.toedter.calendar.JDateChooser();
        fecha_exp = new com.toedter.calendar.JDateChooser();
        validar = new javax.swing.JButton();
        guardar = new javax.swing.JButton();
        con_confirmar = new javax.swing.JPasswordField();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        con_nueva = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        cancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("RECUPERAR CONTRASEÑA");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 490, 40));

        jLabel1.setBackground(new java.awt.Color(0, 0, 153));
        jLabel1.setToolTipText("");
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 90));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 0, 0));
        jLabel3.setText("no ingresaste correctamente tus datos se bloqueara la cuenta.");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 540, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 0, 0));
        jLabel4.setText("Para poder recuperar el usuario debes ingresar los datos personales con el fin de verificar que ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 560, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("seas tu, despues de validar la informacion te permitira cambiar la contraseña, si a los 3 intentos ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 560, 30));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel8.setText("Informacion Personal");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 320, 40));

        cedula.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cedulaKeyTyped(evt);
            }
        });
        jPanel1.add(cedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 210, 40));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setText("Cedula");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 280, -1, -1));

        correo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        correo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                correoKeyTyped(evt);
            }
        });
        jPanel1.add(correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 310, 210, 40));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Correo");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 280, -1, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("Fecha de expedicion");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 380, -1, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setText("Fecha de nacimiento");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, -1, -1));
        jPanel1.add(fecha_nac, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, 210, 40));
        jPanel1.add(fecha_exp, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 410, 210, 40));

        validar.setBackground(new java.awt.Color(0, 0, 0));
        validar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        validar.setForeground(new java.awt.Color(255, 255, 255));
        validar.setText("VALIDAR");
        validar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validarActionPerformed(evt);
            }
        });
        jPanel1.add(validar, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 480, 160, 50));

        guardar.setBackground(new java.awt.Color(0, 0, 0));
        guardar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        guardar.setForeground(new java.awt.Color(255, 255, 255));
        guardar.setText("CAMBIAR");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });
        jPanel1.add(guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 720, 160, 50));

        con_confirmar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(con_confirmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 650, 210, 40));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Confirmar Contraseña");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 610, -1, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Nueva Constraseña");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 610, -1, -1));

        con_nueva.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(con_nueva, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 650, 210, 40));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel6.setText("Cambiar Contraseña");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 540, 290, 40));

        cancelar.setBackground(new java.awt.Color(0, 0, 0));
        cancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cancelar.setForeground(new java.awt.Color(255, 255, 255));
        cancelar.setText("CANCELAR");
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });
        jPanel1.add(cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 720, 160, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cedulaKeyTyped
        char c = evt.getKeyChar();
        // Aceptar solo letras mayúsculas y espacios
        if (cedula.getText().length ()>=10) 
                evt.consume();  
                if (c<'0' || c>'9') evt.consume();// TODO add your handling code here:
    }//GEN-LAST:event_cedulaKeyTyped

    private void correoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_correoKeyTyped
    char c = evt.getKeyChar();
        // Aceptar solo letras mayúsculas y espacios
        if (correo.getText().length ()>=30) 
                evt.consume();        // TODO add your handling code here:
    }//GEN-LAST:event_correoKeyTyped

    private void validarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_validarActionPerformed

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed

       
        // TODO add your handling code here:
    }//GEN-LAST:event_guardarActionPerformed

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_cancelarActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelar;
    private javax.swing.JTextField cedula;
    private javax.swing.JPasswordField con_confirmar;
    private javax.swing.JPasswordField con_nueva;
    private javax.swing.JTextField correo;
    private com.toedter.calendar.JDateChooser fecha_exp;
    private com.toedter.calendar.JDateChooser fecha_nac;
    private javax.swing.JButton guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton validar;
    // End of variables declaration//GEN-END:variables
}
