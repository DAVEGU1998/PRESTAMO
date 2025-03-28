package prestamo;
import CONEXION.CONEXION;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class LOGIN extends javax.swing.JFrame {
    // Declaración de componentes
    
    
    public LOGIN() {
        initComponents();
         setTitle("LOGIN");
        setPlaceholderText(USUARIO, "Correo");
        setPasswordFieldPlaceholder(CONTRASEÑA, "Contraseña");
        setResizable(false);
        setLocationRelativeTo(null); // Centrar ventana
        CONTRASEÑA.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            validarUsuario(); // Llama la función que usa el botón
        }
    }
});
    
        INGRESAR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarUsuario();
            }
        });
    configurarLabelRegistro();
    }
    
    private void validarUsuario() {
    String correo = USUARIO.getText().trim();
    String contraseña = new String(CONTRASEÑA.getPassword()).trim();

    if (correo.isEmpty() || contraseña.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    Connection conexion = CONEXION.conectar();
    if (conexion == null) {
        JOptionPane.showMessageDialog(this, "Error de conexión.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Consulta modificada para obtener nombre y correo
    String sql = "SELECT NOMBRE, CORREO FROM USUARIOS WHERE CORREO = ? AND CONTRASEÑA = ?";
    try {
        PreparedStatement pst = conexion.prepareStatement(sql);
        pst.setString(1, correo);
        pst.setString(2, contraseña);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            String nombreUsuario = rs.getString("NOMBRE");
            String correoUsuario = rs.getString("CORREO"); // Obtenemos el correo

            this.dispose();

            MENU_STANDAR menu = new MENU_STANDAR();
            menu.setDatosUsuario(nombreUsuario, correoUsuario); // Pasamos ambos datos
            menu.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
        }

        rs.close();
        pst.close();
        conexion.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al validar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    private void setPlaceholderText(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                }
            }
        });
    }

    private void setPasswordFieldPlaceholder(JPasswordField field, String placeholder) {
        field.setEchoChar((char)0);
        field.setText(placeholder);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('*');
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (String.valueOf(field.getPassword()).isEmpty()) {
                    field.setEchoChar((char)0);
                    field.setText(placeholder);
                }
            }
        });
    }
         private void configurarLabelRegistro() {
        // Configurar el JLabel registrarse para que actúe como un botón
        registrarse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registrarse.setForeground(Color.BLUE);
        
        // Añadir el MouseListener
        registrarse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirVentanaRegistro();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                registrarse.setForeground(new Color(0, 0, 200)); // Azul más oscuro
                registrarse.setText("<html><u>Registrarse</u></html>"); // Subrayado
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                registrarse.setForeground(Color.BLUE);
                registrarse.setText("Registrarse"); // Quita el subrayado
            }
        });
    }

    private void abrirVentanaRegistro() {
        this.dispose(); // Cierra completamente LOGIN
        REGISTRO registro = new REGISTRO();
        registro.setVisible(true);
        registro.setLocationRelativeTo(null); // Centrar la ventana
        
        // Opcional: Al cerrar REGISTRO, abrir nuevo LOGIN
        registro.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                new LOGIN().setVisible(true);
            }
        });

    }
    

    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        USUARIO = new javax.swing.JTextField();
        CONTRASEÑA = new javax.swing.JPasswordField();
        registrarse = new javax.swing.JLabel();
        OLVIDE1 = new javax.swing.JLabel();
        INGRESAR = new javax.swing.JToggleButton();
        ICON = new javax.swing.JLabel();
        FONDO2 = new javax.swing.JLabel();
        FONDO = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 255, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        USUARIO.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        USUARIO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                USUARIOActionPerformed(evt);
            }
        });
        USUARIO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                USUARIOKeyTyped(evt);
            }
        });
        jPanel1.add(USUARIO, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 360, 260, 40));

        CONTRASEÑA.setText("jPasswordField1");
        jPanel1.add(CONTRASEÑA, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 420, 260, 40));

        registrarse.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        registrarse.setForeground(new java.awt.Color(0, 51, 255));
        registrarse.setText("Registrarse");
        registrarse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                registrarseMouseClicked(evt);
            }
        });
        jPanel1.add(registrarse, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 560, 80, 20));

        OLVIDE1.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        OLVIDE1.setForeground(new java.awt.Color(0, 51, 255));
        OLVIDE1.setText("Olvide la contraseña.");
        OLVIDE1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OLVIDE1MouseClicked(evt);
            }
        });
        jPanel1.add(OLVIDE1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 460, 130, 20));

        INGRESAR.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        INGRESAR.setText("INGRESAR");
        jPanel1.add(INGRESAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 510, 130, 40));

        ICON.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/usuario 512px.png"))); // NOI18N
        jPanel1.add(ICON, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 170, 130, 160));

        FONDO2.setBackground(new java.awt.Color(255, 255, 255));
        FONDO2.setForeground(new java.awt.Color(255, 255, 255));
        FONDO2.setOpaque(true);
        jPanel1.add(FONDO2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 100, 640, 530));

        FONDO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/images (1).jpg"))); // NOI18N
        FONDO.setPreferredSize(new java.awt.Dimension(1169, 779));
        jPanel1.add(FONDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1170, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1164, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void registrarseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registrarseMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_registrarseMouseClicked

    private void OLVIDE1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OLVIDE1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_OLVIDE1MouseClicked

    private void USUARIOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_USUARIOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_USUARIOActionPerformed

    private void USUARIOKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_USUARIOKeyTyped
    char c = evt.getKeyChar();
    // Aceptar solo letras mayúsculas y espacios
    if (USUARIO.getText().length ()>=20) 
            evt.consume();     // TODO add your handling code here:
    }//GEN-LAST:event_USUARIOKeyTyped

    /**
     * @param args the command line arguments
     */
     public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new LOGIN().setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField CONTRASEÑA;
    private javax.swing.JLabel FONDO;
    private javax.swing.JLabel FONDO2;
    private javax.swing.JLabel ICON;
    private javax.swing.JToggleButton INGRESAR;
    private javax.swing.JLabel OLVIDE1;
    private javax.swing.JTextField USUARIO;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel registrarse;
    // End of variables declaration//GEN-END:variables
}


