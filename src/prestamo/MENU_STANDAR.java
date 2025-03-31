package prestamo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MENU_STANDAR extends javax.swing.JFrame {
     // Asegurar que está declarado como variable de clase
    private String correoUsuario;
    private String nombreUsuario;
    public MENU_STANDAR() {
        initComponents();
        setTitle("MENU PRINCIPAL");
        setSize(1164, 779);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // Configuración inicial del contenedor
    contenedor.setLayout(new BorderLayout());
    
    // Configuración de botones
    configureButtons();
    
    // Configurar el label de cerrar sesión
    configurarCerrarSesion(); // <-- Añade esta línea
}
    // En tu clase MENU_STANDAR, añade este método
private void configurarCerrarSesion() {
    // Configurar el JLabel cerrar para que actúe como un botón
    cerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cambia el cursor
    cerrar.setForeground(Color.WHITE); // Asegurar que sea visible
    
    // Añadir el MouseListener
    cerrar.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            cerrarSesion();
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            cerrar.setForeground(Color.RED); // Cambia color al pasar el mouse
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            cerrar.setForeground(Color.WHITE); // Vuelve al color original
        }
    });
}

private void cerrarSesion() {
    int confirmacion = JOptionPane.showConfirmDialog(
        this, 
        "¿Estás seguro que deseas cerrar sesión?", 
        "Cerrar sesión", 
        JOptionPane.YES_NO_OPTION
    );
    
    if (confirmacion == JOptionPane.YES_OPTION) {
        this.dispose(); // Cierra la ventana actual
        new LOGIN().setVisible(true); // Abre la ventana de login
    }
}
    public void setNombreUsuario(String nombreUsuario) {
    nombre.setText(nombreUsuario);
    nombre.setForeground(java.awt.Color.WHITE); // Asegura que el texto sea negro
    nombre.setEnabled(true);  // Asegura que no esté deshabilitado
    nombre.repaint();  // Vuelve a pintar el JLabel para aplicar los cambios
}

   private void configureButtons() {
    JButton[] buttons = {perfil, salas, equipos, pqrs};
    
    // Hacer botones transparentes
    for (JButton button : buttons) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
    }
    
    // Efecto hover
    Color hoverColor = new Color(0, 0, 128);
    Color normalColor = jLabel3.getBackground();
    
    for (JButton button : buttons) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setOpaque(true);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
                button.setOpaque(false);
            }
            
        });
    }

    // ✅ Agregar evento al botón "perfil" para cambiar de panel
    perfil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PERFIL perfilPanel = new PERFIL();
                // Pasar ambos datos al panel PERFIL
                perfilPanel.cargarDatosUsuario(nombreUsuario, correoUsuario);
                mostrarPanel(perfilPanel);
            }
        });
    }
    public void setDatosUsuario(String nombreUsuario, String correoUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.correoUsuario = correoUsuario;
        nombre.setText(nombreUsuario);
        nombre.setForeground(Color.WHITE);
        nombre.repaint();
    }
    private void mostrarPanel(JPanel panel) {
    contenedor.removeAll();
    contenedor.add(panel, BorderLayout.CENTER);
    contenedor.revalidate();
    contenedor.repaint();
}

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cerrar = new javax.swing.JLabel();
        nombre = new javax.swing.JLabel();
        bienvenido = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        pqrs = new javax.swing.JButton();
        perfil = new javax.swing.JButton();
        equipos = new javax.swing.JButton();
        salas = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        contenedor = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/cerrar-sesion54 .png"))); // NOI18N
        jPanel1.add(cerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 4, -1, -1));

        nombre.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jPanel1.add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 20, 270, 30));

        bienvenido.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        bienvenido.setForeground(new java.awt.Color(255, 255, 255));
        bienvenido.setText("BIENVENIDO:");
        jPanel1.add(bienvenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 130, 30));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("SISTEMA DE PRESTAMO ");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 0, 300, 70));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/PQR32.png"))); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 400, 40, 50));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1160, 70));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/perfil32.png"))); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 164, -1, 40));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/salas 32.png"))); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 243, 40, 40));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/EQUIPOS 32.png"))); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 320, 40, 50));

        pqrs.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        pqrs.setText("PQRS");
        pqrs.setBorder(null);
        pqrs.setBorderPainted(false);
        pqrs.setFocusable(false);
        jPanel1.add(pqrs, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 140, 50));

        perfil.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        perfil.setText("PERFIL");
        perfil.setBorder(null);
        perfil.setBorderPainted(false);
        perfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                perfilActionPerformed(evt);
            }
        });
        jPanel1.add(perfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 140, 50));

        equipos.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        equipos.setText("EQUIPOS");
        equipos.setBorder(null);
        equipos.setBorderPainted(false);
        jPanel1.add(equipos, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 140, 50));

        salas.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        salas.setText("SALAS");
        salas.setBorder(null);
        salas.setBorderPainted(false);
        jPanel1.add(salas, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 140, 50));

        jLabel3.setBackground(new java.awt.Color(102, 153, 255));
        jLabel3.setOpaque(true);
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 180, 710));

        contenedor.setBackground(new java.awt.Color(255, 255, 255));
        contenedor.setMaximumSize(new java.awt.Dimension(32767, 3000));

        javax.swing.GroupLayout contenedorLayout = new javax.swing.GroupLayout(contenedor);
        contenedor.setLayout(contenedorLayout);
        contenedorLayout.setHorizontalGroup(
            contenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 980, Short.MAX_VALUE)
        );
        contenedorLayout.setVerticalGroup(
            contenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 710, Short.MAX_VALUE)
        );

        jPanel1.add(contenedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 980, 710));

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
    private javax.swing.JLabel bienvenido;
    private javax.swing.JLabel cerrar;
    private javax.swing.JPanel contenedor;
    private javax.swing.JButton equipos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nombre;
    private javax.swing.JButton perfil;
    private javax.swing.JButton pqrs;
    private javax.swing.JButton salas;
    // End of variables declaration//GEN-END:variables

   
}
