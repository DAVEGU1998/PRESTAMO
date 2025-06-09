package prestamo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import prestamo.EQUIPOS;

public class MENU_STANDAR extends javax.swing.JFrame {
     // Asegurar que está declarado como variable de clase
    private String correoUsuario;
    private String nombreUsuario;
    private String cedulaUsuario;
    public MENU_STANDAR() {
        initComponents();
        setTitle("MENU PRINCIPAL");
        setIconImage(new ImageIcon(getClass().getResource("/IMAGENES/administracion-de-empresas64.png")).getImage());
        setSize(1164, 779);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // Configuración inicial del contenedor
    contenedor.setLayout(new BorderLayout());
    
    // Configuración de botones
    configureButtons();
    
    // Configurar el label de cerrar sesión
    configurarCerrarSesion();
    iniciarDetectorInactividad();// <-- Añade esta línea
}
    private Timer inactivityTimer;
    private final int TIMEOUT = 5 * 60 * 1000; // 5 minutos en milisegundos



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
    JButton[] buttons = {perfil, salas, equipos, reservas};
    
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
    reservas.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Asumiendo que tienes la variable tipoUsuario (agrega un atributo si no lo tienes)
        String tipoUsuario = "normal"; // O inicialízalo en el login y guarda en la clase

        // Crear el panel RESERVAS pasando cedulaUsuario y tipoUsuario
        RESERVAS panelReservas = new RESERVAS(cedulaUsuario, tipoUsuario);
        mostrarPanel(panelReservas);
    }
});
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
    salas.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            SALAS equiposPanel = new SALAS(cedulaUsuario);
            mostrarPanel(equiposPanel);
        }
    });
    equipos.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EQUIPOS equiposPanel = new EQUIPOS(cedulaUsuario);
            mostrarPanel(equiposPanel);
        }
    });
}

    public void setDatosUsuario(String nombreUsuario, String correoUsuario, String cedula) {
        this.nombreUsuario = nombreUsuario;
        this.correoUsuario = correoUsuario;
        this.cedulaUsuario = cedula;
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
    private void iniciarDetectorInactividad() {
    // Listener global para cualquier evento de entrada del usuario (mouse, teclado, etc.)
    Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
        public void eventDispatched(AWTEvent event) {
            reiniciarTemporizadorInactividad();
        }
    }, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);

    // Temporizador de inactividad
    inactivityTimer = new Timer(TIMEOUT, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            cerrarSesionPorInactividad();
        }
    });
    inactivityTimer.setRepeats(false); // Solo una vez
    inactivityTimer.start(); // Iniciar temporizador
}

private void reiniciarTemporizadorInactividad() {
    if (inactivityTimer != null) {
        inactivityTimer.restart();
    }
}

private void cerrarSesionPorInactividad() {
    JOptionPane.showMessageDialog(this, 
        "Sesión cerrada por inactividad.", 
        "Inactividad", 
        JOptionPane.WARNING_MESSAGE);
    this.dispose(); // Cierra la ventana actual
    new LOGIN().setVisible(true); // Vuelve a la ventana de login
    // Inicializa el detector de inactividad
    

}


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cerrar = new javax.swing.JLabel();
        nombre = new javax.swing.JLabel();
        bienvenido = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        reservas = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
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
        jPanel1.add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 260, 30));

        bienvenido.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        bienvenido.setForeground(new java.awt.Color(255, 255, 255));
        bienvenido.setText("BIENVENIDO:");
        jPanel1.add(bienvenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 130, 30));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/calendario.png"))); // NOI18N
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 248, -1, 40));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("SISTEMA DE PRESTAMO ");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 0, 300, 70));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1160, 70));

        reservas.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        reservas.setText("RESERVAS");
        reservas.setBorder(null);
        reservas.setBorderPainted(false);
        reservas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reservasActionPerformed(evt);
            }
        });
        jPanel1.add(reservas, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 140, 50));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/perfil32.png"))); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 164, -1, 40));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/salas 32.png"))); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 320, 40, 40));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/EQUIPOS 32.png"))); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 400, 40, 50));

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
        jPanel1.add(equipos, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 140, 50));

        salas.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        salas.setText("SALAS");
        salas.setBorder(null);
        salas.setBorderPainted(false);
        salas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salasActionPerformed(evt);
            }
        });
        jPanel1.add(salas, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 140, 50));

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

    private void reservasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reservasActionPerformed
     
    }//GEN-LAST:event_reservasActionPerformed

    private void salasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salasActionPerformed
        
    }//GEN-LAST:event_salasActionPerformed

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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nombre;
    private javax.swing.JButton perfil;
    private javax.swing.JButton reservas;
    private javax.swing.JButton salas;
    // End of variables declaration//GEN-END:variables

   
}
