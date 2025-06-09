package prestamo;

import CONEXION.CONEXION;
import java.awt.event.ItemEvent;
import java.sql.*;
import java.util.HashSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class SALAS extends javax.swing.JPanel {

    private String cedulaUsuario;

    public SALAS(String cedulaUsuario) {
    initComponents();
    this.cedulaUsuario = cedulaUsuario;
    cargarComboEdificio();
    cargarComboProgramas();

    edificio.addItemListener(e -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            buscarAulas();
        }
    });

    programas.addItemListener(e -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            buscarAulas();
        }
    });
}



private void cargarComboEdificio() {
    String sql = "SELECT DISTINCT nombre FROM EDIFICIO ORDER BY nombre";

    try (Connection conn = CONEXION.conectar();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        DefaultComboBoxModel<String> modelEdificio = new DefaultComboBoxModel<>();
        modelEdificio.addElement("-- Seleccione --"); // Opción inicial

        while (rs.next()) {
            String nombre = rs.getString("nombre");
            modelEdificio.addElement(nombre);
        }

        edificio.setModel(modelEdificio);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar edificios: " + e.getMessage());
    }
}



    private void cargarComboProgramas() {
    String sql = "SELECT descripcion FROM INFO_SALAS"; // Cambia "SALAS" si el nombre de la tabla es otro

    try (Connection conn = CONEXION.conectar();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        HashSet<String> programasSet = new HashSet<>();

        while (rs.next()) {
            String descripcion = rs.getString("descripcion");
            if (descripcion != null && !descripcion.trim().isEmpty()) {
                String[] programas = descripcion.split(",");
                for (String prog : programas) {
                    String limpio = prog.trim().toLowerCase();
                    if (!limpio.isEmpty()) {
                        programasSet.add(limpio);
                    }
                }
            }
        }

        DefaultComboBoxModel<String> modelProgramas = new DefaultComboBoxModel<>();
        modelProgramas.addElement("-- Seleccione --"); // Opción inicial

        java.util.List<String> listaProgramas = new java.util.ArrayList<>(programasSet);
        java.util.Collections.sort(listaProgramas);

        for (String p : listaProgramas) {
            modelProgramas.addElement(p);
        }

        programas.setModel(modelProgramas);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar programas: " + e.getMessage());
    }
}
private void buscarAulas() {
    String edificioSeleccionado = (String) edificio.getSelectedItem();
    String programaSeleccionado = (String) programas.getSelectedItem();

    DefaultComboBoxModel<String> modelAula = new DefaultComboBoxModel<>();
    aula.setModel(modelAula); // Limpiar primero

    if (edificioSeleccionado == null || edificioSeleccionado.equals("-- Seleccione --")) {
        JOptionPane.showMessageDialog(null, "Seleccione un edificio.");
        return;
    }

    String sql;
    boolean filtrarPrograma = programaSeleccionado != null && !programaSeleccionado.equals("-- Seleccione --");

    if (filtrarPrograma) {
        sql = "SELECT NOMBRE FROM INFO_SALAS WHERE EDIFICIO = ? AND LOWER(descripcion) LIKE ?";
    } else {
        sql = "SELECT NOMBRE FROM INFO_SALAS WHERE EDIFICIO = ?";
    }

    try (Connection conn = CONEXION.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, edificioSeleccionado);

        if (filtrarPrograma) {
            ps.setString(2, "%" + programaSeleccionado.toLowerCase() + "%");
        }

        ResultSet rs = ps.executeQuery();
        boolean hayResultados = false;

        modelAula.addElement("-- Seleccione --"); // Opción inicial

        while (rs.next()) {
            String aulaNombre = rs.getString("nombre");
            modelAula.addElement(aulaNombre);
            hayResultados = true;
        }

        if (!hayResultados) {
            JOptionPane.showMessageDialog(null, "No hay aulas disponibles con ese programa.");
            modelAula.removeAllElements();
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al buscar aulas: " + e.getMessage());
    }
}


private void reservarSala() {
    String edificioSeleccionado = (String) edificio.getSelectedItem();
    String aulaSeleccionada = (String) aula.getSelectedItem();
    String descripcionTexto = descripcion.getText().trim();

java.util.Date fechaIni = fecha_inicio.getDate();
java.util.Date fechaFin = fecha_fin.getDate();
String horaIni = (String) hora_inicio.getSelectedItem();
String horaFin = (String) hora_fin.getSelectedItem();

    // o .getText()

    // Validar campos
    if (edificioSeleccionado == null || aulaSeleccionada == null ||
    fechaIni == null || fechaFin == null ||
    horaIni == null || horaFin == null || descripcionTexto.isEmpty()) {

    JOptionPane.showMessageDialog(null, "Por favor completa todos los campos.");
    return;
}


    // Convertir fechas a java.sql.Date
    java.sql.Date sqlFechaInicio = new java.sql.Date(fechaIni.getTime());
    java.sql.Date sqlFechaFin = new java.sql.Date(fechaFin.getTime());

    String sql = "INSERT INTO PRESTAMO2025.RESERVA_SALAS (ID, EDIFICIO, AULA, DESCRIPCION, " 
           + "FECHA_INICIO, HORA_INICIO, FECHA_FIN, HORA_FIN, CEDULA) "
           + "VALUES ((SELECT NVL(MAX(ID),0)+1 FROM PRESTAMO2025.RESERVA_SALAS), "
           + "?, ?, ?, ?, ?, ?, ?, ?)";


    try (Connection conn = CONEXION.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, edificioSeleccionado);
        ps.setString(2, aulaSeleccionada);
        ps.setString(3, descripcionTexto);
ps.setDate(4, sqlFechaInicio);
ps.setString(5, horaIni);
ps.setDate(6, sqlFechaFin);
ps.setString(7, horaFin);
ps.setString(8, cedulaUsuario); // Aquí se guarda la cédula del usuario logueado


        int filas = ps.executeUpdate();

        if (filas > 0) {
            JOptionPane.showMessageDialog(null, "¡Reserva realizada exitosamente!");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo realizar la reserva.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al reservar: " + e.getMessage());
    }
}





    // End of variables declaration                   


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        reservar = new javax.swing.JButton();
        edificio = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        hora_fin = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        fecha_fin = new com.toedter.calendar.JDateChooser();
        fecha_inicio = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        descripcion = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        aula = new javax.swing.JComboBox<>();
        hora_inicio = new javax.swing.JComboBox<>();
        programas = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(980, 720));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        reservar.setBackground(new java.awt.Color(0, 0, 0));
        reservar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        reservar.setForeground(new java.awt.Color(255, 255, 255));
        reservar.setText("RESERVAR");
        reservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reservarActionPerformed(evt);
            }
        });
        jPanel1.add(reservar, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 570, 160, 40));

        edificio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        edificio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "JORGE LUIS BORGES", "DAVID CONSUEGRA", "CARLOS LLERAS", " " }));
        jPanel1.add(edificio, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 260, 240, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Fecha fin:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 130, 140, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Edificio");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 230, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel8.setText("RESERVA DE SALAS");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, 320, 40));

        hora_fin.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        hora_fin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "8:00 AM" }));
        hora_fin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hora_finActionPerformed(evt);
            }
        });
        jPanel1.add(hora_fin, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, 240, 40));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Aula");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, 50, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Descripcion");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 430, 160, -1));
        jPanel1.add(fecha_fin, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 160, 240, 40));
        jPanel1.add(fecha_inicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 240, 40));

        descripcion.setColumns(20);
        descripcion.setRows(5);
        jScrollPane1.setViewportView(descripcion);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 460, 810, 70));

        jLabel12.setText("Horarios disponibles: 6:00-12:00 y 13:30-21:30");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 200, 250, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Fecha inicio:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 160, -1));

        jLabel15.setText("Horarios disponibles: 6:00-12:00 y 13:30-21:30");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 250, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("Hora inicio:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 130, 140, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setText("Hora fin:");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 230, 140, -1));

        aula.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        aula.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "BUCARAMANGA", "SAN GIL", "BARRANCABERMEJA", "BOGOTA" }));
        jPanel1.add(aula, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 350, 240, 40));

        hora_inicio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        hora_inicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "8:00 AM" }));
        jPanel1.add(hora_inicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 160, 240, 40));

        programas.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        programas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "BUCARAMANGA", "SAN GIL", "BARRANCABERMEJA", "BOGOTA" }));
        jPanel1.add(programas, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 260, 240, 40));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setText("Programas:");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 230, 150, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void reservarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reservarActionPerformed
        reservarSala();
    
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_reservarActionPerformed

    private void hora_finActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hora_finActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hora_finActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> aula;
    private javax.swing.JTextArea descripcion;
    private javax.swing.JComboBox<String> edificio;
    private com.toedter.calendar.JDateChooser fecha_fin;
    private com.toedter.calendar.JDateChooser fecha_inicio;
    private javax.swing.JComboBox<String> hora_fin;
    private javax.swing.JComboBox<String> hora_inicio;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> programas;
    private javax.swing.JButton reservar;
    // End of variables declaration//GEN-END:variables
}
