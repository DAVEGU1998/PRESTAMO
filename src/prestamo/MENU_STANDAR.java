import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MENU_STANDAR extends javax.swing.JFrame {

    public MENU_STANDAR() {
        initComponents();
        makeButtonsTransparent();
        addButtonHoverEffect();
    }

    private void makeButtonsTransparent() {
        JButton[] buttons = {perfil, salas, equipos, pqrs};
        for (JButton button : buttons) {
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setForeground(Color.WHITE); // Color del texto
        }
    }

    private void addButtonHoverEffect() {
        JButton[] buttons = {perfil, salas, equipos, pqrs};
        for (JButton button : buttons) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(0, 0, 128)); // Azul oscuro
                    button.setOpaque(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(jLabel3.getBackground()); // Fondo del jLabel3
                    button.setOpaque(false);
                }
            });
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pqrs = new javax.swing.JButton();
        perfil = new javax.swing.JButton();
        equipos = new javax.swing.JButton();
        salas = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("SISTEMA DE PRESTAMO ");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 0, 300, 70));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1160, 70));

        pqrs.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        pqrs.setText("PQRS");
        pqrs.setBorder(null);
        pqrs.setBorderPainted(false);
        pqrs.setFocusable(false);
        jPanel1.add(pqrs, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, 180, 50));

        perfil.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        perfil.setText("PERFIL");
        perfil.setBorder(null);
        perfil.setBorderPainted(false);
        perfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                perfilActionPerformed(evt);
            }
        });
        jPanel1.add(perfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 180, 50));

        equipos.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        equipos.setText("EQUIPOS");
        equipos.setBorder(null);
        equipos.setBorderPainted(false);
        jPanel1.add(equipos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 180, 50));

        salas.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        salas.setText("SALAS");
        salas.setBorder(null);
        salas.setBorderPainted(false);
        jPanel1.add(salas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 180, 50));

        jLabel3.setBackground(new java.awt.Color(102, 153, 255));
        jLabel3.setOpaque(true);
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 180, 710));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1164, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void perfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_perfilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_perfilActionPerformed

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
            java.util.logging.Logger.getLogger(MENU_STANDAR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MENU_STANDAR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MENU_STANDAR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MENU_STANDAR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MENU_STANDAR().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton equipos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton perfil;
    private javax.swing.JButton pqrs;
    private javax.swing.JButton salas;
    // End of variables declaration//GEN-END:variables
}
