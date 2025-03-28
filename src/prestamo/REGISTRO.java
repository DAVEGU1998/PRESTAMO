/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package prestamo;

import java.net.PasswordAuthentication;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import oracle.jdbc.driver.Message;
import prestamo.LOGIN;
import java.util.Properties;
import javax.swing.*;
import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author ADMIN
 */
public class REGISTRO extends javax.swing.JFrame {
    // ... (tus componentes existentes)
    
    public REGISTRO() {
        initComponents();
        setTitle("Solicitud de Registro");
        setLocationRelativeTo(null);
        
        // Configurar botones
        solicitar.addActionListener(e -> enviarSolicitud());
        cancelar.addActionListener(e -> volverALogin());
        
        // Configurar JComboBox
        tipo_usuario.setModel(new DefaultComboBoxModel<>(new String[] {
            "Seleccionar", "Estudiante", "Docente", "Administrativo"
        }));
        
        campus.setModel(new DefaultComboBoxModel<>(new String[] {
            "Seleccionar", "BUCARAMANGA", "SAN GIL", "BARRANCABERMEJA", "BOGOTA"
        }));
    }
    
    private void enviarSolicitud() {
        // Validar campos
        if (!validarCampos()) {
            return;
        }
        
        // Obtener datos del formulario
        String datosUsuario = obtenerDatosFormulario();
        
        // Enviar correo
        if (enviarCorreo(datosUsuario)) {
            JOptionPane.showMessageDialog(this, 
                "Solicitud enviada correctamente. Revisa tu correo en un lapso de 24 horas para recibir tu usuario y contraseña.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volverALogin();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error al enviar la solicitud. Por favor intente nuevamente.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarCampos() {
        if (nombre.getText().trim().isEmpty() ||
            apellido.getText().trim().isEmpty() ||
            cedula.getText().trim().isEmpty() ||
            correo.getText().trim().isEmpty() ||
            tipo_usuario.getSelectedIndex() == 0 ||
            campus.getSelectedIndex() == 0) {
            
            JOptionPane.showMessageDialog(this, 
                "Todos los campos son obligatorios", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validar formato de correo
        if (!correo.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, 
                "Ingrese un correo electrónico válido", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private String obtenerDatosFormulario() {
        return "Nueva solicitud de registro para creacion de usuario:\n\n" +
               "Nombre: " + nombre.getText() + "\n" +
               "Apellido: " + apellido.getText() + "\n" +
               "Cédula: " + cedula.getText() + "\n" +
               "Correo: " + correo.getText() + "\n" +
               "Tipo de usuario: " + tipo_usuario.getSelectedItem() + "\n" +
               "Campus: " + campus.getSelectedItem();
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
    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

    try {
        // SOLUCIÓN CLAVE: Usar referencia completa para Authenticator y PasswordAuthentication
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(usuario, contraseña);
            }
        });

        javax.mail.internet.MimeMessage message = new javax.mail.internet.MimeMessage(session);
        message.setFrom(new javax.mail.internet.InternetAddress(usuario));
        message.addRecipient(javax.mail.Message.RecipientType.TO, 
                          new javax.mail.internet.InternetAddress(destinatario));
        message.setSubject("Nueva solicitud de registro - Sistema de Préstamos");
        message.setText(mensaje);

        javax.mail.Transport.send(message);
        return true;

    } catch (javax.mail.MessagingException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al enviar correo: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return false;
    }
}
    private void volverALogin() {
        this.dispose(); // Cierra la ventana actual
        new LOGIN().setVisible(true); // Abre la ventana de login
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
        jLabel4.setText("Realiza la solicitud para utilizar el sistema de prestamo de salas y equipos de computo, despues de ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 560, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("solicitar el usuario te llegara un correo en un lapso de 24 horas confirmando el usuario y una ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 540, 30));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel8.setText("Informacion Personal");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 320, 40));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Nombre");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Apellido");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 290, -1, -1));

        nombre.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 210, 40));

        apellido.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(apellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 320, 210, 40));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Tipo de Usuario");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 470, -1, -1));

        tipo_usuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tipo_usuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "DOCENTE", "ADMINISTRATIVO" }));
        jPanel1.add(tipo_usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 500, 210, 40));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Campus");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 470, -1, -1));

        cancelar.setBackground(new java.awt.Color(0, 0, 0));
        cancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cancelar.setForeground(new java.awt.Color(255, 255, 255));
        cancelar.setText("CANCELAR");
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });
        jPanel1.add(cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 600, 160, 50));

        solicitar.setBackground(new java.awt.Color(0, 0, 0));
        solicitar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        solicitar.setForeground(new java.awt.Color(255, 255, 255));
        solicitar.setText("SOLICITAR ");
        solicitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solicitarActionPerformed(evt);
            }
        });
        jPanel1.add(solicitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 600, 160, 50));

        cedula.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(cedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, 210, 40));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setText("Cedula");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 380, -1, -1));

        correo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 410, 210, 40));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Correo");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 380, -1, -1));

        campus.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "BUCARAMANGA", "SAN GIL", "BARRANCABERMEJA", "BOGOTA" }));
        jPanel1.add(campus, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 500, 210, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed

        

        // TODO add your handling code here:
    }//GEN-LAST:event_cancelarActionPerformed

    private void solicitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solicitarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_solicitarActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField apellido;
    private javax.swing.JComboBox<String> campus;
    private javax.swing.JButton cancelar;
    private javax.swing.JTextField cedula;
    private javax.swing.JTextField correo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JComboBox<String> tipo_usuario;
    // End of variables declaration//GEN-END:variables
}
