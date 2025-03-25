package prestamo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

public class LOGIN extends javax.swing.JFrame {

    public LOGIN() {
        initComponents();
        setPlaceholderText(USUARIO, "Usuario");
        setPlaceholderText(CONTRASEÑA, "Contraseña");
        
        // Evitar que el foco se coloque automáticamente en USUARIO o CONTRASEÑA
        // Haciendo que el foco inicial se ponga en el panel vacío
        jPanel1.requestFocusInWindow();  // Poner el foco en el panel vacío
        
        // Deshabilitar la maximización de la ventana
        setResizable(false); // Evita que la ventana sea redimensionable
    }

    // Método para agregar texto de sugerencia (placeholder) con movimiento y cambio de tamaño
    private void setPlaceholderText(JTextField textField, String placeholder) {
        // Crear un JLabel que funcionará como el texto de sugerencia
        JLabel label = new JLabel(placeholder);
        label.setForeground(Color.GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBounds(10, 0, 200, 40);  // Ajustar posición y tamaño

        // Añadir el JLabel al panel, no al JTextField directamente
        textField.setLayout(null);  // Establecer el layout a null para que puedas colocar componentes libremente
        textField.add(label);

        // Cuando el campo recibe el foco, mueve el JLabel hacia arriba y reduce el tamaño
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    // Mover el texto hacia arriba y reducir el tamaño
                    label.setBounds(10, -20, 200, 20);
                    label.setFont(new Font("Arial", Font.PLAIN, 12));  // Reducir el tamaño del texto
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    // Si el campo está vacío, regresar el JLabel a su posición original
                    label.setBounds(10, 0, 200, 40);
                    label.setFont(new Font("Arial", Font.PLAIN, 16));  // Volver al tamaño original
                }
            }
        });
    }
    
    // Método para agregar los enlaces debajo del campo de contraseña
   

       

    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        USUARIO = new javax.swing.JTextField();
        CONTRASEÑA = new javax.swing.JTextField();
        OLVIDE = new javax.swing.JLabel();
        OLVIDE1 = new javax.swing.JLabel();
        INGRESAR = new javax.swing.JToggleButton();
        ICON = new javax.swing.JLabel();
        FONDO2 = new javax.swing.JLabel();
        FONDO = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 255, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel1.add(CONTRASEÑA, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 420, 260, 40));

        OLVIDE.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        OLVIDE.setForeground(new java.awt.Color(0, 51, 255));
        OLVIDE.setText("Registrarse");
        OLVIDE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OLVIDEMouseClicked(evt);
            }
        });
        jPanel1.add(OLVIDE, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 550, -1, 20));

        OLVIDE1.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        OLVIDE1.setForeground(new java.awt.Color(0, 51, 255));
        OLVIDE1.setText("Olvide la contraseña.");
        OLVIDE1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OLVIDE1MouseClicked(evt);
            }
        });
        jPanel1.add(OLVIDE1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 460, -1, 20));

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

    private void OLVIDEMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OLVIDEMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_OLVIDEMouseClicked

    private void OLVIDE1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OLVIDE1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_OLVIDE1MouseClicked

    private void USUARIOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_USUARIOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_USUARIOActionPerformed

    private void USUARIOKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_USUARIOKeyTyped
    char c = evt.getKeyChar();
    // Aceptar solo letras mayúsculas y espacios
        if ((c < 'A' || c > 'Z') && c != KeyEvent.VK_SPACE) {
            evt.consume(); // Ignorar el evento
    } 
    if (USUARIO.getText().length ()>=15) 
            evt.consume();     // TODO add your handling code here:
    }//GEN-LAST:event_USUARIOKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LOGIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LOGIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LOGIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LOGIN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LOGIN().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CONTRASEÑA;
    private javax.swing.JLabel FONDO;
    private javax.swing.JLabel FONDO2;
    private javax.swing.JLabel ICON;
    private javax.swing.JToggleButton INGRESAR;
    private javax.swing.JLabel OLVIDE;
    private javax.swing.JLabel OLVIDE1;
    private javax.swing.JTextField USUARIO;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
