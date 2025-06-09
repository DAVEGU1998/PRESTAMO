package prestamo;

import CONEXION.CONEXION;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class EQUIPOS extends javax.swing.JPanel {
    private String cedulaUsuario;
    public EQUIPOS(String cedulaUsuario) {
    try {
        Integer.parseInt(cedulaUsuario); // Validar que sea numérico
        this.cedulaUsuario = cedulaUsuario;
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("La cédula debe contener solo números");
    }
        initComponents();
        this.cedulaUsuario = cedulaUsuario;
        setBackground(Color.WHITE);
        personalizarComponentes();
        aplicarBordesDelgados();
        cargarDatosCombobox();
        configurarDateChoosers();
        estiloBotonDelgado();
        configurarBotonInfo();
        cargarEdificios();
        
        edificio.addActionListener(e -> {
    String edificioSeleccionado = (String) edificio.getSelectedItem();
    cargarAulasPorEdificio(edificioSeleccionado);
    });

    }
    private void guardarReserva() {
    try {
        String sql = "INSERT INTO PRESTAMO2025.RESERVA_EQUIPOS (ID, TIPO_RECURSO, RECURSO, EDIFICIO, AULA, " +
             "DESCRIPCION, FECHA_INICIO, HORA_INICIO, FECHA_FIN, HORA_FIN, CEDULA) " +
             "VALUES ((SELECT NVL(MAX(ID),0)+1 FROM PRESTAMO2025.RESERVA_EQUIPOS), " +
             "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
                     // Asumiendo que tienes una secuencia llamada RESERVA_EQUIPOS_SEQ

        CONEXION conexion = new CONEXION();
        var conn = conexion.conectar();
        var pstmt = conn.prepareStatement(sql);

        // Validación de campos
        if (edificio.getSelectedItem() == null || edificio.getSelectedItem().equals("SELECCIONE") ||
            aula.getSelectedItem() == null || aula.getSelectedItem().equals("SELECCIONE") ||
            tipo.getSelectedItem() == null || tipo.getSelectedItem().equals("SELECCIONE") ||
            recurso.getSelectedItem() == null || recurso.getSelectedItem().equals("SELECCIONE") ||
            fecha_inicio.getDate() == null || fecha_fin.getDate() == null ||
            hora_inicio.getSelectedItem() == null || hora_inicio.getSelectedItem().equals("SELECCIONAR") ||
            hora_fin.getSelectedItem() == null || hora_fin.getSelectedItem().equals("SELECCIONAR")) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos correctamente", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pstmt.setString(1, (String) tipo.getSelectedItem());
        pstmt.setString(2, (String) recurso.getSelectedItem());
        pstmt.setString(3, (String) edificio.getSelectedItem());
        pstmt.setString(4, (String) aula.getSelectedItem());
        pstmt.setString(5, descripcion.getText());
        pstmt.setDate(6, new java.sql.Date(fecha_inicio.getDate().getTime()));
        pstmt.setString(7, (String) hora_inicio.getSelectedItem());
        pstmt.setDate(8, new java.sql.Date(fecha_fin.getDate().getTime()));
        pstmt.setString(9, (String) hora_fin.getSelectedItem());
        pstmt.setInt(10, Integer.parseInt(cedulaUsuario)); // Convertir a número

        int resultado = pstmt.executeUpdate();

        if (resultado > 0) {
            JOptionPane.showMessageDialog(this, "Reserva guardada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar la reserva", "Error", JOptionPane.ERROR_MESSAGE);
        }

        pstmt.close();
        conn.close();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "La cédula debe contener solo números", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private boolean verificarDisponibilidadEquipo() {
    try {
        String sql = "SELECT COUNT(*) FROM RESERVA_EQUIPOS " +
                     "WHERE RECURSO = ? " +
                     "AND FECHA_INICIO <= ? " +
                     "AND FECHA_FIN >= ? " +
                     "AND HORA_INICIO <= ? " +
                     "AND HORA_FIN >= ?";
        
        CONEXION conexion = new CONEXION();
        var conn = conexion.conectar();
        var pstmt = conn.prepareStatement(sql);
        
        pstmt.setString(1, (String) recurso.getSelectedItem());
        pstmt.setDate(2, new java.sql.Date(fecha_fin.getDate().getTime()));
        pstmt.setDate(3, new java.sql.Date(fecha_inicio.getDate().getTime()));
        pstmt.setString(4, (String) hora_fin.getSelectedItem());
        pstmt.setString(5, (String) hora_inicio.getSelectedItem());
        
        var rs = pstmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            JOptionPane.showMessageDialog(this, "El equipo no está disponible en el horario seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
private void limpiarCampos() {
    edificio.setSelectedIndex(0);
    aula.setModel(new DefaultComboBoxModel<>(new String[]{"SELECCIONE"}));
    tipo.setSelectedIndex(0);
    recurso.setModel(new DefaultComboBoxModel<>(new String[]{"SELECCIONE"}));
    descripcion.setText("");
    fecha_inicio.setDate(null);
    fecha_fin.setDate(null);
    hora_inicio.setSelectedIndex(0);
    hora_fin.setSelectedIndex(0);
}

    private void configurarBotonInfo() {
        // Estilo básico del botón
        botonInfo.setContentAreaFilled(false);
        botonInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        botonInfo.setFocusPainted(false);
        
        // Icono de información (opcional)
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/imagenes/info.png"));
            Image img = icono.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            botonInfo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            // Si no hay icono, se muestra texto
            botonInfo.setText("?");
        }
        
        // Comportamiento al pasar el mouse
        botonInfo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botonInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
                botonInfo.setBackground(new Color(230, 230, 230));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                botonInfo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                botonInfo.setBackground(null);
            }
        });
        
        // Acción al hacer clic
        botonInfo.addActionListener(e -> mostrarDescripcionCompleta());
    }

    private void mostrarDescripcionCompleta() {
        String descripcion = obtenerDescripcionActual();
        
        if (descripcion == null || descripcion.isEmpty()) {
            descripcion = "No hay descripción disponible para este recurso";
        }
        
        // Crear un área de texto con scroll
        JTextArea textArea = new JTextArea(descripcion);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(new Color(245, 245, 245));
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 150));
        
        // Mostrar en un diálogo
        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Descripción Detallada",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
private void cargarEdificios() {
    DefaultComboBoxModel<String> modeloEdificios = new DefaultComboBoxModel<>();
    modeloEdificios.addElement("SELECCIONE"); // Opción por defecto

    try {
        CONEXION conexion = new CONEXION();
        String sql = "SELECT DISTINCT NOMBRE FROM EDIFICIO ORDER BY NOMBRE";
        var conn = conexion.conectar();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String nombreEdificio = rs.getString("NOMBRE");
            modeloEdificios.addElement(nombreEdificio);
        }

        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    edificio.setModel(modeloEdificios); // Asegúrate que tu JComboBox se llame 'edificio'
}
    private void cargarAulasPorEdificio(String edificioSeleccionado) {
    DefaultComboBoxModel<String> modeloAulas = new DefaultComboBoxModel<>();
    modeloAulas.addElement("SELECCIONE");

    if (edificioSeleccionado == null || edificioSeleccionado.equals("SELECCIONE")) {
        aula.setModel(modeloAulas);
        return;
    }

    try {
        CONEXION conexion = new CONEXION();
        String sql = "SELECT NOMBRE FROM INFO_SALAS WHERE EDIFICIO = ? ORDER BY NOMBRE";
        var conn = conexion.conectar();
        var stmt = conn.prepareStatement(sql);
        stmt.setString(1, edificioSeleccionado);
        var rs = stmt.executeQuery();

        while (rs.next()) {
            String nombreSala = rs.getString("NOMBRE");
            modeloAulas.addElement(nombreSala);
        }

        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    aula.setModel(modeloAulas);
}

    private String obtenerDescripcionActual() {
        // Implementación según tu conexión a BD
        try {
            if (recurso.getSelectedItem() != null && !recurso.getSelectedItem().equals("SELECCIONE")) {
                CONEXION conexion = new CONEXION();
                String sql = "SELECT DESCRIPCION FROM EQUIPO WHERE MARCA = ?";
                var conn = conexion.conectar();
                var stmt = conn.prepareStatement(sql);
                stmt.setString(1, (String) recurso.getSelectedItem());
                var rs = stmt.executeQuery();
                
                if (rs.next()) {
                    return rs.getString("DESCRIPCION");
                }
                
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void personalizarComponentes() {
        // Configuración para todos los JComboBox
        for (Component comp : jPanel1.getComponents()) {
            if (comp instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) comp;
                combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                combo.setBackground(Color.WHITE);
                combo.setForeground(new Color(60, 60, 60));
                
                // Configurar el renderer para los items
                combo.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value,
                            int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        
                        if (isSelected) {
                            setBackground(new Color(70, 130, 220));
                            setForeground(Color.WHITE);
                        } else {
                            setBackground(index % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                            setForeground(Color.DARK_GRAY);
                        }
                        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                        return this;
                    }
                });
            }
        }
        
        // Configuración específica para los combobox de horas
        personalizarComboboxHoras(hora_inicio);
        personalizarComboboxHoras(hora_fin);
        
        // Configurar el texto del área de descripción
        descripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void aplicarBordesDelgados() {
        // Grosor del borde en píxeles (1px)
        int grosorBorde = 1;
        Color colorBorde = new Color(200, 200, 200); // Gris claro
        
        // Aplicar a todos los JComboBox
        for (Component comp : jPanel1.getComponents()) {
            if (comp instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) comp;
                combo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(colorBorde, grosorBorde),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
                ));
            }
        }
        
        // Bordes para JTextArea
        descripcion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorBorde, grosorBorde),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    private void personalizarComboboxHoras(JComboBox<String> comboHora) {
        // Crear modelo con horas en formato moderno
        DefaultComboBoxModel<String> modeloHoras = new DefaultComboBoxModel<>();
        modeloHoras.addElement("SELECCIONAR");
        for (int i = 6; i <= 12; i++) {
            modeloHoras.addElement(String.format("%02d:00", i));
        }
        for (int i = 13; i <= 21; i++) {
            modeloHoras.addElement(String.format("%02d:30", i));
        }
        comboHora.setModel(modeloHoras);
    }

    private void cargarDatosCombobox() {
    // Cargar tipos de recursos únicos desde la base de datos
    DefaultComboBoxModel<String> modeloTipos = new DefaultComboBoxModel<>();
    modeloTipos.addElement("SELECCIONE"); // Agregar la opción inicial
    
    try {
        CONEXION conexion = new CONEXION();
        String sql = "SELECT DISTINCT TIPO_RECURSO FROM EQUIPO ORDER BY TIPO_RECURSO";
        var conn = conexion.conectar();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            String tipoRecurso = rs.getString("TIPO_RECURSO");
            modeloTipos.addElement(tipoRecurso);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    // Asignar el modelo al JComboBox tipo
    tipo.setModel(modeloTipos);
    
    // Agregar listener para cargar recursos relacionados
    tipo.addActionListener(e -> cargarRecursosPorTipo());
}

private void cargarRecursosPorTipo() {
    String tipoSeleccionado = (String) tipo.getSelectedItem();
    DefaultComboBoxModel<String> modeloRecursos = new DefaultComboBoxModel<>();
    modeloRecursos.addElement("SELECCIONE"); // Opción inicial

    // Evitar cargar datos si no se selecciona un tipo válido
    if (tipoSeleccionado == null || tipoSeleccionado.equals("SELECCIONE")) {
        recurso.setModel(modeloRecursos);
        return;
    }

    try {
        CONEXION conexion = new CONEXION();
        String sql = "SELECT DISTINCT MARCA FROM EQUIPO WHERE TIPO_RECURSO = ? ORDER BY MARCA";
        var conn = conexion.conectar();
        var stmt = conn.prepareStatement(sql);
        stmt.setString(1, tipoSeleccionado);
        var rs = stmt.executeQuery();
        
        while (rs.next()) {
            String marca = rs.getString("MARCA");
            modeloRecursos.addElement(marca);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    // Asignar el modelo al JComboBox recurso
    recurso.setModel(modeloRecursos);
}


    private void configurarDateChoosers() {
        Color colorBorde = new Color(200, 200, 200);
        int grosorBorde = 1;
        
        // Configurar el editor del JDateChooser
        fecha_inicio.getDateEditor().getUiComponent().setBorder(
            BorderFactory.createLineBorder(colorBorde, grosorBorde)
        );
        fecha_inicio.getDateEditor().getUiComponent().setBackground(Color.WHITE);
        fecha_inicio.getDateEditor().getUiComponent().setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        fecha_fin.getDateEditor().getUiComponent().setBorder(
            BorderFactory.createLineBorder(colorBorde, grosorBorde)
        );
        fecha_fin.getDateEditor().getUiComponent().setBackground(Color.WHITE);
        fecha_fin.getDateEditor().getUiComponent().setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void estiloBotonDelgado() {
        Border border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 220)), // Borde delgado azul
            BorderFactory.createEmptyBorder(8, 20, 8, 20) // Padding interno
        );
        
        reservar.setBorder(border);
        reservar.setContentAreaFilled(false);
        reservar.setOpaque(true);
        reservar.setBackground(new Color(70, 130, 220));
        reservar.setForeground(Color.WHITE);
        reservar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Efecto hover
        reservar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                reservar.setBackground(new Color(90, 150, 240));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                reservar.setBackground(new Color(70, 130, 220));
            }
        });
        
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
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        hora_fin = new javax.swing.JComboBox<>();
        tipo = new javax.swing.JComboBox<>();
        recurso = new javax.swing.JComboBox<>();
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
        botonInfo = new javax.swing.JButton();

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
        edificio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "BUCARAMANGA", "SAN GIL", "BARRANCABERMEJA", "BOGOTA" }));
        edificio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edificioActionPerformed(evt);
            }
        });
        jPanel1.add(edificio, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 160, 240, 40));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Recurso");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 130, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Fecha fin:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 230, 140, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Edificio");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 130, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Tipo de recurso");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel8.setText("RESERVA DE EQUIPOS");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 60, 320, 40));

        hora_fin.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        hora_fin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "BUCARAMANGA", "SAN GIL", "BARRANCABERMEJA", "BOGOTA" }));
        hora_fin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hora_finActionPerformed(evt);
            }
        });
        jPanel1.add(hora_fin, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 360, 240, 40));

        tipo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "BUCARAMANGA", "SAN GIL", "BARRANCABERMEJA", "BOGOTA" }));
        tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoActionPerformed(evt);
            }
        });
        jPanel1.add(tipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 240, 40));

        recurso.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        recurso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONE", " " }));
        jPanel1.add(recurso, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 160, 240, 40));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Aula");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 50, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Descripcion");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 430, 160, -1));
        jPanel1.add(fecha_fin, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 260, 240, 40));
        jPanel1.add(fecha_inicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 260, 240, 40));

        descripcion.setColumns(20);
        descripcion.setRows(5);
        jScrollPane1.setViewportView(descripcion);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 460, 810, 70));

        jLabel12.setText("Horarios disponibles: 6:00-12:00 y 13:30-21:30");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 400, 250, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Fecha inicio:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 160, -1));

        jLabel15.setText("Horarios disponibles: 6:00-12:00 y 13:30-21:30");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 400, 250, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("Hora inicio:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 330, 140, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setText("Hora fin:");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 330, 140, -1));

        aula.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        aula.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "BUCARAMANGA", "SAN GIL", "BARRANCABERMEJA", "BOGOTA" }));
        jPanel1.add(aula, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, 240, 40));

        hora_inicio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        hora_inicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "BUCARAMANGA", "SAN GIL", "BARRANCABERMEJA", "BOGOTA" }));
        jPanel1.add(hora_inicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 360, 240, 40));

        botonInfo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        botonInfo.setText("?");
        botonInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonInfoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonInfoMouseExited(evt);
            }
        });
        botonInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInfoActionPerformed(evt);
            }
        });
        jPanel1.add(botonInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 160, 30, 40));

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
        guardarReserva();
    
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_reservarActionPerformed

    private void hora_finActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hora_finActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hora_finActionPerformed

    private void botonInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInfoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonInfoActionPerformed

    private void botonInfoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonInfoMouseExited
        botonInfo.setToolTipText(null);// TODO add your handling code here:
    }//GEN-LAST:event_botonInfoMouseExited

    private void botonInfoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonInfoMouseEntered
        String tipoSeleccionado = (String) tipo.getSelectedItem();
        String recursoSeleccionado = (String) recurso.getSelectedItem();

        // Validar que no sean "SELECCIONE"
        if (tipoSeleccionado != null && recursoSeleccionado != null &&
            !tipoSeleccionado.equals("SELECCIONE") && !recursoSeleccionado.equals("SELECCIONE")) {

            try {
                CONEXION conexion = new CONEXION();
                String sql = "SELECT DESCRIPCION FROM EQUIPO WHERE TIPO_RECURSO = ? AND MARCA = ?";
                var conn = conexion.conectar();
                var stmt = conn.prepareStatement(sql);
                stmt.setString(1, tipoSeleccionado);
                stmt.setString(2, recursoSeleccionado);
                var rs = stmt.executeQuery();

                if (rs.next()) {
                    String descripcionTexto = rs.getString("DESCRIPCION");

                    // Mostrar la descripción como tooltip
                    botonInfo.setToolTipText("<html><p style='width:200px;'>" + descripcionTexto + "</p></html>");
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }// TODO add your handling code here:
    }//GEN-LAST:event_botonInfoMouseEntered

    private void tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoActionPerformed

    private void edificioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edificioActionPerformed
            // TODO add your handling code here:
    }//GEN-LAST:event_edificioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> aula;
    private javax.swing.JButton botonInfo;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> recurso;
    private javax.swing.JButton reservar;
    private javax.swing.JComboBox<String> tipo;
    // End of variables declaration//GEN-END:variables
}
