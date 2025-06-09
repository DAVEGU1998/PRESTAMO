package prestamo;

import CONEXION.CONEXION;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.mail.*;
import javax.mail.internet.*;

public class REGISTRO extends javax.swing.JFrame {
    // Componentes del formulario (nombres EXACTOS como en NetBeans)
     // Nombre exacto del diseñador

    public REGISTRO() {
        initComponents();
        setTitle("Solicitud de Registro");
        setResizable(false);
        setLocationRelativeTo(null);
        configurarComponentes();
    }

    private void configurarComponentes() {
        // Configurar JComboBox
        tipo_usuario.setModel(new DefaultComboBoxModel<>(new String[] {
            "Seleccionar", "DOCENTE", "ADMINISTRATIVO","ADMINISTRADOR"
        }));
        
        campus.setModel(new DefaultComboBoxModel<>(new String[] {
            "Seleccionar", "BUCARAMANGA", "SAN GIL", "BARRANCABERMEJA", "BOGOTA"
        }));

        // Configurar botones
        solicitar.addActionListener(e -> enviarSolicitud());
        cancelar.addActionListener(e -> volverALogin());
    }

    private void enviarSolicitud() {
    if (!validarCampos()) {
        return;
    }

    String correoIngresado = correo.getText().trim();

    if (correoYaRegistrado(correoIngresado)) {
        JOptionPane.showMessageDialog(this, 
            "Este correo ya está registrado. Si quieres restablecer la contraseña ingrea a olvide mi contraseña", 
            "Usuario Existente", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String datosUsuario = obtenerDatosFormulario();

    if (enviarCorreo(datosUsuario)) {
        JOptionPane.showMessageDialog(this, 
            "Solicitud enviada. Revisa tu correo en 24 horas.", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
        volverALogin();
    } else {
        JOptionPane.showMessageDialog(this, 
            "Error al enviar la solicitud.", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();
        
        if (nombre.getText().trim().isEmpty()) errores.append("- Nombre\n");
        if (apellido.getText().trim().isEmpty()) errores.append("- Apellido\n");
        if (cedula.getText().trim().isEmpty()) errores.append("- Cédula\n");
        if (correo.getText().trim().isEmpty()) errores.append("- Correo\n");
        if (tipo_usuario.getSelectedIndex() == 0) errores.append("- Tipo de usuario\n");
        if (campus.getSelectedIndex() == 0) errores.append("- Campus\n");
        if (fecha_nac.getDate() == null) errores.append("- Fecha de nacimiento\n");
        if (fecha_exp.getDate() == null) errores.append("- Fecha de expedición\n");
        if (telefono.getText().trim().isEmpty()) errores.append("- Teléfono\n");

        // Validación adicional de fechas
        if (fecha_nac.getDate() != null && fecha_exp.getDate() != null && 
            fecha_exp.getDate().before(fecha_nac.getDate())) {
            errores.append("- La fecha de expedición no puede ser anterior a la de nacimiento\n");
        }

        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Complete todos los campos:\n" + errores.toString(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private String obtenerDatosFormulario() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    String fechaNac = sdf.format(fecha_nac.getDate());
    String fechaExp = sdf.format(fecha_exp.getDate());

    return "SOLICITUD DE REGISTRO\n\n" +
           "Nombre: " + nombre.getText() + "\n" +
           "Apellido: " + apellido.getText() + "\n" +
           "Cédula: " + cedula.getText() + "\n" +
           "Correo: " + correo.getText() + "\n" +
           "Teléfono: " + telefono.getText() + "\n" + // 👈 LÍNEA NUEVA
           "Fecha Nacimiento: " + fechaNac + "\n" +
           "Fecha Expedición: " + fechaExp + "\n" +
           "Tipo Usuario: " + tipo_usuario.getSelectedItem() + "\n" +
           "Campus: " + campus.getSelectedItem();
}
    private boolean correoYaRegistrado(String correo) {
    String consulta = "SELECT COUNT(*) FROM USUARIOS WHERE CORREO = ?";
    try (Connection conn = CONEXION.conectar();
        PreparedStatement stmt = conn.prepareStatement(consulta)) {
        stmt.setString(1, correo);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0; // Si hay al menos 1 registro, ya existe
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error al verificar el correo: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    return false;
}


    private boolean enviarCorreo(String mensaje) {
        final String host = "smtp.gmail.com";
        final String usuario = "dvesga6@udi.edu.co";
        final String contraseña = "avmk weex jckz aatl";
        final String destinatario = "dvesga6@udi.edu.co";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(usuario, contraseña);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject("Nueva Solicitud de Registro");
            message.setText(mensaje);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void volverALogin() {
        this.dispose();
        new LOGIN().setVisible(true);
    }

    

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new REGISTRO().setVisible(true);
        });
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
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        apellido = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tipo_usuario = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        cancelar = new javax.swing.JButton();
        solicitar = new javax.swing.JButton();
        cedula = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        correo = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        campus = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        fecha_nac = new com.toedter.calendar.JDateChooser();
        fecha_exp = new com.toedter.calendar.JDateChooser();
        telefono = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("REGISTRATE");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 240, 40));

        jLabel1.setBackground(new java.awt.Color(0, 0, 153));
        jLabel1.setToolTipText("");
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 90));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 0, 0));
        jLabel3.setText("contraseña generica que debes cambiar lo mas pronto prosible.");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 540, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 0, 0));
        jLabel4.setText("Realiza la solicitud para utilizar el sistema de prestamo de salas y equipos de computo, despues  ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 560, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("de solicitar el usuario te llegara un correo en un lapso de 24 horas confirmando el usuario y una ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 560, 30));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel8.setText("Informacion Personal");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 320, 40));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Nombres:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Apellidos:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 250, -1, -1));

        nombre.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nombreKeyTyped(evt);
            }
        });
        jPanel1.add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 210, 40));

        apellido.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                apellidoKeyTyped(evt);
            }
        });
        jPanel1.add(apellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 280, 210, 40));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Tipo de Usuario:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 530, -1, -1));

        tipo_usuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tipo_usuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "DOCENTE", "ADMINISTRATIVO" }));
        tipo_usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipo_usuarioActionPerformed(evt);
            }
        });
        jPanel1.add(tipo_usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 560, 210, 40));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Campus:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 530, -1, -1));

        cancelar.setBackground(new java.awt.Color(0, 0, 0));
        cancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cancelar.setForeground(new java.awt.Color(255, 255, 255));
        cancelar.setText("CANCELAR");
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });
        jPanel1.add(cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 700, 160, 50));

        solicitar.setBackground(new java.awt.Color(0, 0, 0));
        solicitar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        solicitar.setForeground(new java.awt.Color(255, 255, 255));
        solicitar.setText("SOLICITAR ");
        solicitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solicitarActionPerformed(evt);
            }
        });
        jPanel1.add(solicitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 700, 160, 50));

        cedula.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cedulaKeyTyped(evt);
            }
        });
        jPanel1.add(cedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 370, 210, 40));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setText("Cedula:");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 340, -1, -1));

        correo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        correo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                correoKeyTyped(evt);
            }
        });
        jPanel1.add(correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 470, 210, 40));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Correo:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 440, -1, -1));

        campus.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "BUCARAMANGA", "SAN GIL", "BARRANCABERMEJA", "BOGOTA" }));
        jPanel1.add(campus, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 560, 210, 40));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("Fecha de expedicion:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 340, -1, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setText("Fecha de nacimiento:");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 440, -1, -1));
        jPanel1.add(fecha_nac, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 470, 210, 40));
        jPanel1.add(fecha_exp, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 370, 210, 40));

        telefono.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                telefonoKeyTyped(evt);
            }
        });
        jPanel1.add(telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 640, 210, 40));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setText("Telefono:");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 610, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed

        

        // TODO add your handling code here:
    }//GEN-LAST:event_cancelarActionPerformed

    private void solicitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solicitarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_solicitarActionPerformed

    private void nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreKeyTyped
        char c = evt.getKeyChar();
        // Aceptar solo letras mayúsculas y espacios
        if (nombre.getText().length ()>=30) 
                evt.consume();        // TODO add your handling code here:
    }//GEN-LAST:event_nombreKeyTyped

    private void apellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_apellidoKeyTyped
        char c = evt.getKeyChar();
        // Aceptar solo letras mayúsculas y espacios
        if (apellido.getText().length ()>=30) 
                evt.consume();        // TODO add your handling code here:
    }//GEN-LAST:event_apellidoKeyTyped

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

    private void telefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telefonoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_telefonoKeyTyped

    private void tipo_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipo_usuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipo_usuarioActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField apellido;
    private javax.swing.JComboBox<String> campus;
    private javax.swing.JButton cancelar;
    private javax.swing.JTextField cedula;
    private javax.swing.JTextField correo;
    private com.toedter.calendar.JDateChooser fecha_exp;
    private com.toedter.calendar.JDateChooser fecha_nac;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nombre;
    private javax.swing.JButton solicitar;
    private javax.swing.JTextField telefono;
    private javax.swing.JComboBox<String> tipo_usuario;
    // End of variables declaration//GEN-END:variables
}
