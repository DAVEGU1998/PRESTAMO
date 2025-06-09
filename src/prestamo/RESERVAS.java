package prestamo;

import CONEXION.CONEXION;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class RESERVAS extends JPanel {
   
    private DefaultTableModel model;
    private String[] columnNames;
    private Object oldValue; // Para almacenar el valor antes de editar
    private String cedulaUsuario;
    private String tipoUsuario;
    private JTable tablaReservas;

    
    public RESERVAS(String cedulaUsuario, String tipoUsuario) {
    this.cedulaUsuario = cedulaUsuario;
    
    // Obtener el tipo de usuario directamente desde la conexión actual
    this.tipoUsuario = obtenerTipoUsuarioDesdeBD(cedulaUsuario);
    
    System.out.println("Tipo de usuario obtenido desde BD: " + this.tipoUsuario);
    
    initComponents();
    personalizarBotones();
    configurarTabla();
    verificarConfiguracionTabla();
    boolean esADMINISTRADOR = "ADMINISTRADOR".equalsIgnoreCase(this.tipoUsuario);
     System.out.println("Tipo de usuario recibido: " + tipoUsuario);
    System.out.println("Tipo de usuario normalizado: " + this.tipoUsuario);
    System.out.println("Es administrador?: " + esADMINISTRADOR);

SALAS.addActionListener(e -> {
    System.out.println("Click en SALAS detectado");
    cargarReservas("SALAS", cedulaUsuario, esADMINISTRADOR);
});
EQUIPOS.addActionListener(e -> {
    System.out.println("Click en EQUIPOS detectado");
    cargarReservas("EQUIPOS", cedulaUsuario, esADMINISTRADOR);
});


    FILTRAR.addActionListener(e -> aplicarFiltro());
    CANCELAR.addActionListener(e -> cancelarFiltro());
}

    private String obtenerTipoUsuarioDesdeBD(String cedulaUsuario) {
    String tipo = "NORMAL"; // Valor por defecto
    
    try (Connection conn = CONEXION.conectar();
         PreparedStatement pst = conn.prepareStatement(
             "SELECT TIPO_USUARIO FROM USUARIOS WHERE CEDULA = ?")) {
        
        pst.setString(1, cedulaUsuario);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            tipo = rs.getString("TIPO_USUARIO");
        }
        
    } catch (SQLException e) {
        System.err.println("Error al obtener tipo de usuario: " + e.getMessage());
    }
    
    return tipo != null ? tipo.trim().toUpperCase() : "NORMAL";
}
    // Agrega este método auxiliar dentro de tu clase USUARIOS
    private int obtenerIndiceColumna(String nombreColumna) {
    for (int i = 0; i < columnNames.length; i++) {
        if (columnNames[i].equalsIgnoreCase(nombreColumna)) {
            return i;
        }
    }
    return -1; // Si no se encuentra
}
    private void cargarReservas(String tipo, String cedulaUsuario, boolean esADMINISTRADOR) {
    System.out.println("=== Iniciando carga de reservas ===");
    System.out.println("Tipo: " + tipo);
    System.out.println("Cédula: " + cedulaUsuario);
    System.out.println("Es administrador: " + esADMINISTRADOR);

    String query;
    boolean esSala = tipo.equalsIgnoreCase("SALAS");
    
    // Configurar nombres de columnas
    String[] columnas = esSala ? 
        new String[]{"ID", "EDIFICIO", "AULA", "DESCRIPCION", "FECHA_INICIO", "HORA_INICIO", "FECHA_FIN", "HORA_FIN", "CEDULA"} :
        new String[]{"ID", "TIPO_RECURSO", "RECURSO", "EDIFICIO", "AULA", "DESCRIPCION", "FECHA_INICIO", "HORA_INICIO", "FECHA_FIN", "HORA_FIN", "CEDULA"};
    
    model.setColumnIdentifiers(columnas);

    // Construir consulta SQL
    if (esADMINISTRADOR) {
        query = esSala
            ? "SELECT ID, EDIFICIO, AULA, DESCRIPCION, FECHA_INICIO, HORA_INICIO, FECHA_FIN, HORA_FIN, CEDULA FROM PRESTAMO2025.RESERVA_SALAS ORDER BY FECHA_INICIO DESC, HORA_INICIO"
            : "SELECT ID, TIPO_RECURSO, RECURSO, EDIFICIO, AULA, DESCRIPCION, FECHA_INICIO, HORA_INICIO, FECHA_FIN, HORA_FIN, CEDULA FROM PRESTAMO2025.RESERVA_EQUIPOS ORDER BY FECHA_INICIO DESC, HORA_INICIO";
    } else {
        query = esSala
            ? "SELECT ID, EDIFICIO, AULA, DESCRIPCION, FECHA_INICIO, HORA_INICIO, FECHA_FIN, HORA_FIN, CEDULA FROM PRESTAMO2025.RESERVA_SALAS WHERE CEDULA = ? ORDER BY FECHA_INICIO DESC, HORA_INICIO"
            : "SELECT ID, TIPO_RECURSO, RECURSO, EDIFICIO, AULA, DESCRIPCION, FECHA_INICIO, HORA_INICIO, FECHA_FIN, HORA_FIN, CEDULA FROM PRESTAMO2025.RESERVA_EQUIPOS WHERE CEDULA = ? ORDER BY FECHA_INICIO DESC, HORA_INICIO";
    }

    System.out.println("Consulta SQL: " + query);

    try (Connection conn = CONEXION.conectar();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        // Solo establecer parámetro si NO es administrador
        if (!esADMINISTRADOR) {
            stmt.setInt(1, Integer.parseInt(cedulaUsuario));
            System.out.println("Parámetro cédula establecido: " + cedulaUsuario);
        }

        ResultSet rs = stmt.executeQuery();
        model.setRowCount(0);

        int contador = 0;
        while (rs.next()) {
            contador++;
            Object[] fila = esSala
                ? new Object[]{
                    rs.getInt("ID"),
                    rs.getString("EDIFICIO"),
                    rs.getString("AULA"),
                    rs.getString("DESCRIPCION"),
                    rs.getDate("FECHA_INICIO"),
                    rs.getString("HORA_INICIO"),
                    rs.getDate("FECHA_FIN"),
                    rs.getString("HORA_FIN"),
                    rs.getInt("CEDULA")
                }
                : new Object[]{
                    rs.getInt("ID"),
                    rs.getString("TIPO_RECURSO"),
                    rs.getString("RECURSO"),
                    rs.getString("EDIFICIO"),
                    rs.getString("AULA"),
                    rs.getString("DESCRIPCION"),
                    rs.getDate("FECHA_INICIO"),
                    rs.getString("HORA_INICIO"),
                    rs.getDate("FECHA_FIN"),
                    rs.getString("HORA_FIN"),
                    rs.getInt("CEDULA")
                };
            model.addRow(fila);
        }

        System.out.println("Registros encontrados: " + contador);
        
        if (contador == 0) {
            JOptionPane.showMessageDialog(this, 
                "No se encontraron reservas de " + tipo, 
                "Información", JOptionPane.INFORMATION_MESSAGE);
        }
        
        ajustarAnchoColumnas();

    } catch (SQLException e) {
        System.err.println("Error SQL: " + e.getMessage());
        JOptionPane.showMessageDialog(this, 
            "Error al cargar reservas: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } catch (NumberFormatException e) {
        System.err.println("Error en formato de cédula: " + e.getMessage());
        JOptionPane.showMessageDialog(this, 
            "Error: Cédula en formato incorrecto",
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private void aplicarFiltro() {
    String filtro = JOptionPane.showInputDialog(this, "Ingrese texto para filtrar:");

    if (filtro == null || filtro.trim().isEmpty()) {
        return; // No hacer nada si no ingresó texto
    }
    
    filtro = filtro.toLowerCase();

    TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
    RESERVAS.setRowSorter(sorter);

    // Busca el filtro en todas las columnas, usando regex (?i) para ignore case
    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + filtro));
}

private void cancelarFiltro() {
    RESERVAS.setRowSorter(null);
}



private void cargarDatosDesdeConsulta(String query, String cedulaUsuario) {
    try (Connection conn = CONEXION.conectar();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        if (cedulaUsuario != null) {
            int cedulaNum = Integer.parseInt(cedulaUsuario);
            stmt.setInt(1, cedulaNum);
        }

        ResultSet rs = stmt.executeQuery();

        DefaultTableModel modelo = (DefaultTableModel) RESERVAS.getModel();
        modelo.setRowCount(0);

        while (rs.next()) {
            Object[] fila = {
                rs.getInt("ID"),
                rs.getString("TIPO_RECURSO"),   // En equipos
                rs.getString("RECURSO"),
                rs.getString("EDIFICIO"),
                rs.getString("AULA"),
                rs.getString("DESCRIPCION"),
                rs.getDate("FECHA_INICIO"),
                rs.getString("HORA_INICIO"),
                rs.getDate("FECHA_FIN"),
                rs.getString("HORA_FIN"),
                rs.getInt("CEDULA")
            };
            modelo.addRow(fila);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al cargar reservas: " + e.getMessage());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Cédula inválida");
    }
}


private void personalizarBotones() {
    // Configuración común para todos los botones
    Font buttonFont = new Font("Segoe UI", Font.BOLD, 12);
    Color textColor = Color.WHITE;
    
    
    // Botón ELIMINAR
    CANCELAR.setBackground(new Color(255, 80, 80));
    CANCELAR.setForeground(textColor);
    CANCELAR.setFont(buttonFont);
    CANCELAR.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 60, 60)), 
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    CANCELAR.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // Botón CREAR USUARIO
    EQUIPOS.setBackground(new Color(60, 180, 60));
    EQUIPOS.setForeground(textColor);
    EQUIPOS.setFont(buttonFont);
    EQUIPOS.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(40, 140, 40)), 
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    EQUIPOS.setCursor(new Cursor(Cursor.HAND_CURSOR));
    SALAS.setBackground(new Color(60, 180, 60));
    SALAS.setForeground(textColor);
    SALAS.setFont(buttonFont);
    SALAS.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(40, 140, 40)), 
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    SALAS.setCursor(new Cursor(Cursor.HAND_CURSOR));
    FILTRAR.setBackground(new Color(60, 180, 60));
    FILTRAR.setForeground(textColor);
    FILTRAR.setFont(buttonFont);
    FILTRAR.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(40, 140, 40)), 
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    FILTRAR.setCursor(new Cursor(Cursor.HAND_CURSOR));
    // Botón ACTIVAR/INACTIVO
   
    
    // Añadir efectos hover a todos los botones

}
private void agregarEfectoHover(JButton button, Color normalColor, Color hoverColor) {
    button.setFocusPainted(false); // Eliminar borde de enfoque
    button.setContentAreaFilled(false); // Necesario para el hover personalizado
    button.setOpaque(true); // Permitir que se vea el color de fondo
    
    button.setBackground(normalColor);
    
    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(hoverColor);
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(normalColor);
        }
    });
}
    private void configurarTabla() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // Todas las celdas editables
            }
        };
        RESERVAS.setModel(model);
        
        // Editor personalizado para manejar la confirmación
        RESERVAS.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, 
                    boolean isSelected, int row, int column) {
                oldValue = value; // Guardar valor original
                return super.getTableCellEditorComponent(table, value, isSelected, row, column);
            }
            
            @Override
            public boolean stopCellEditing() {
                Object newValue = getCellEditorValue();
                
                // Si el valor cambió, pedir confirmación
                if (!newValue.equals(oldValue)) {
                    String columnName = columnNames[RESERVAS.getSelectedColumn()];
                    String nombreUsuario = model.getValueAt(RESERVAS.getSelectedRow(), 1).toString(); // Asume columna 1 es nombre
                    
                    int confirm = JOptionPane.showConfirmDialog(RESERVAS.this,
                        "¿Confirmar cambio para " + nombreUsuario + "?\n" +
                        "Campo: " + columnName + "\n" +
                        "De: " + oldValue + "\n" +
                        "A: " + newValue,
                        "Confirmar Edición",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean result = super.stopCellEditing();
                        if (result) {
                            actualizarEnBD(RESERVAS.getSelectedRow(), RESERVAS.getSelectedColumn(), newValue);
                        }
                        return result;
                    } else {
                        // Cancelar edición
                        cancelCellEditing();
                        return false;
                    }
                }
                return super.stopCellEditing();
            }
        });
    }
    private void buscarUsuarios(String texto) {
        System.out.println("Buscando: " + texto);
    texto = texto.toLowerCase(); // Ignorar mayúsculas/minúsculas
    try (Connection conn = CONEXION.conectar();
         PreparedStatement pstmt = conn.prepareStatement(
             "SELECT * FROM RESERVAS WHERE " +
             "LOWER(NOMBRE) LIKE ? OR " +
             "LOWER(APELLIDO) LIKE ? OR " +
             "LOWER(CORREO) LIKE ? OR " +
             "LOWER(TELEFONO) LIKE ? OR " +
             "LOWER(TIPO_USUARIO) LIKE ? OR " +
             "LOWER(CAMPUS) LIKE ? OR " +
             "LOWER(CONTRASEÑA) LIKE ? OR " +
             "LOWER(FECHA_NAC) LIKE ? OR " +
             "LOWER(FECHA_EXP) LIKE ? OR " +
             "LOWER(CEDULA) LIKE ? OR " +
             "LOWER(ESTADO) LIKE ?")) {

        for (int i = 1; i <= 11; i++) {
            pstmt.setString(i, "%" + texto + "%");
        }

        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = metaData.getColumnName(i + 1);
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }

        model.setDataVector(data, new Vector<String>() {{
            for (String name : columnNames) add(name);
        }});
        ajustarAnchoColumnas();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this,
            "Error al buscar usuarios: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

    
    public void cargarDatos() {
        try (Connection conn = CONEXION.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM USUARIOS ORDER BY NOMBRE")) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnName(i + 1);
            }
            
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }
            
            model.setDataVector(data, new Vector<String>() {{
                for (String name : columnNames) add(name);
            }});
            
            ajustarAnchoColumnas();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar usuarios: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    // Reemplaza TODO el método actualizarEnBD con este código
private void actualizarEnBD(int row, int col, Object newValue) {
    String columnName = columnNames[col];
    int cedulaIndex = obtenerIndiceColumna("CEDULA");
    if (cedulaIndex == -1) {
        JOptionPane.showMessageDialog(this, "No se encontró la columna 'CEDULA'", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    Object cedulaValue = model.getValueAt(row, cedulaIndex); // Usar CEDULA como identificador

    new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            try (Connection conn = CONEXION.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE USUARIOS SET " + columnName + " = ? WHERE CEDULA = ?")) {

                pstmt.setObject(1, newValue);
                pstmt.setObject(2, cedulaValue);

                int affectedRows = pstmt.executeUpdate();

                SwingUtilities.invokeLater(() -> {
                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(RESERVAS.this,
                            "¡Usuario actualizado correctamente!",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        model.setValueAt(oldValue, row, col);
                        JOptionPane.showMessageDialog(RESERVAS.this,
                            "No se pudo actualizar el usuario",
                            "Error", JOptionPane.WARNING_MESSAGE);
                    }
                });
            } catch (SQLException ex) {
                SwingUtilities.invokeLater(() -> {
                    model.setValueAt(oldValue, row, col);
                    JOptionPane.showMessageDialog(RESERVAS.this,
                        "Error al actualizar: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                });
                ex.printStackTrace();
            }
            return null;
        }
    }.execute();
}

    
    private void ajustarAnchoColumnas() {
    // Asegurarse que la tabla y el modelo de columnas existen
    if (RESERVAS == null || RESERVAS.getColumnModel() == null) return;
    
    // Desactivar autoajuste temporalmente
    RESERVAS.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    
    TableColumnModel columnModel = RESERVAS.getColumnModel();
    
    for (int column = 0; column < RESERVAS.getColumnCount(); column++) {
        TableColumn col = columnModel.getColumn(column);
        int width = 15; // Margen mínimo
        
        // 1. Obtener ancho del encabezado
        TableCellRenderer headerRenderer = col.getHeaderRenderer();
        if (headerRenderer == null) {
            headerRenderer = RESERVAS.getTableHeader().getDefaultRenderer();
        }
        Component headerComp = headerRenderer.getTableCellRendererComponent(RESERVAS, col.getHeaderValue(), false, false, -1, column);
        width = Math.max(width, headerComp.getPreferredSize().width);
        
        // 2. Obtener ancho máximo del contenido
        for (int row = 0; row < RESERVAS.getRowCount(); row++) {
            TableCellRenderer renderer = RESERVAS.getCellRenderer(row, column);
            Component comp = RESERVAS.prepareRenderer(renderer, row, column);
            width = Math.max(width, comp.getPreferredSize().width + 10); // +10 de margen
        }
        
        // 3. Establecer el ancho con límites (mín 50px, máx 300px)
        col.setPreferredWidth(Math.min(Math.max(width, 50), 300));
    }
    
    // Restaurar el autoajuste
    RESERVAS.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    
}
private String determinarTipoReservaActual() {
   TableModel modelo = RESERVAS.getModel();
    
    // Buscar columnas específicas de EQUIPOS
    for (int i = 0; i < modelo.getColumnCount(); i++) {
        if ("TIPO_RECURSO".equalsIgnoreCase(modelo.getColumnName(i))) {
            return "EQUIPOS";
        }
    }
    
    // Si no es EQUIPOS, asumimos que es SALAS
    for (int i = 0; i < modelo.getColumnCount(); i++) {
        if ("AULA".equalsIgnoreCase(modelo.getColumnName(i))) {
            return "SALAS";
        }
    }
    
    return null;
}

private void verificarConfiguracionTabla() {
    System.out.println("=== Configuración de Tabla ===");
    System.out.println("Modelo de selección: " + RESERVAS.getSelectionModel().getClass().getName());
    
    System.out.println("Filas seleccionables: " + RESERVAS.getRowSelectionAllowed());
    System.out.println("Columnas seleccionables: " + RESERVAS.getColumnSelectionAllowed());
    System.out.println("Celda seleccionable: " + RESERVAS.getCellSelectionEnabled());
    
    // Verificar listener de selección
    RESERVAS.getSelectionModel().addListSelectionListener(e -> {
        System.out.println("Evento de selección - Fila: " + RESERVAS.getSelectedRow());
    });
}

    
    // Variables declaration
    
 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        RESERVAS = new javax.swing.JTable();
        CANCELAR = new javax.swing.JButton();
        EQUIPOS = new javax.swing.JButton();
        FILTRAR = new javax.swing.JButton();
        SALAS = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(980, 720));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel8.setText("Administracion de Reservas");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 0, 430, 40));

        RESERVAS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        RESERVAS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RESERVASMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(RESERVAS);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 960, 560));

        CANCELAR.setText("CANCELAR");
        CANCELAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CANCELARActionPerformed(evt);
            }
        });
        jPanel1.add(CANCELAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 130, 40));

        EQUIPOS.setText("EQUIPOS");
        EQUIPOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EQUIPOSActionPerformed(evt);
            }
        });
        jPanel1.add(EQUIPOS, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 130, 40));

        FILTRAR.setText("FILTRAR");
        jPanel1.add(FILTRAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 130, 40));

        SALAS.setText("SALAS");
        SALAS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SALASActionPerformed(evt);
            }
        });
        jPanel1.add(SALAS, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 40));

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

    private void RESERVASMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RESERVASMouseClicked
     if (evt.getClickCount() == 2) { // Doble clic
            int row = RESERVAS.getSelectedRow();
            if (row >= 0) {
                // Puedes implementar edición específica aquí si lo deseas
            }
        }          // TODO add your handling code here:
    }//GEN-LAST:event_RESERVASMouseClicked

    private void CANCELARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CANCELARActionPerformed
    // Verificar si hay una fila seleccionada
    int filaSeleccionada = RESERVAS.getSelectedRow();
    
    // Depuración
    System.out.println("[DEBUG] Índice de fila seleccionada: " + filaSeleccionada);
    System.out.println("[DEBUG] Número total de filas: " + RESERVAS.getRowCount());
    
    if (filaSeleccionada < 0 || filaSeleccionada >= RESERVAS.getRowCount()) {
        JOptionPane.showMessageDialog(this, 
            "Por favor selecciona una reserva haciendo clic en una fila de la tabla", 
            "Ninguna reserva seleccionada", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Obtener el ID de la reserva (asumiendo que está en la columna 0)
    Object idObj = RESERVAS.getValueAt(filaSeleccionada, 0);
    if (idObj == null) {
        JOptionPane.showMessageDialog(this, 
            "No se pudo obtener el ID de la reserva seleccionada", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    int idReserva;
    try {
        idReserva = Integer.parseInt(idObj.toString());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, 
            "El ID de reserva no tiene un formato válido", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Determinar si es reserva de SALAS o EQUIPOS
    String tipoReserva = determinarTipoReservaActual();
    if (tipoReserva == null) {
        JOptionPane.showMessageDialog(this, 
            "No se pudo determinar el tipo de reserva", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Mostrar confirmación
    int confirmacion = JOptionPane.showConfirmDialog(this,
        "¿Estás seguro que deseas cancelar esta reserva?\nID: " + idReserva + "\nTipo: " + tipoReserva,
        "Confirmar cancelación",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (confirmacion != JOptionPane.YES_OPTION) {
        return;
    }

    // Ejecutar la eliminación en la base de datos
    try (Connection conn = CONEXION.conectar()) {
        String tabla = tipoReserva.equals("SALAS") ? "RESERVA_SALAS" : "RESERVA_EQUIPOS";
        String sql = "DELETE FROM PRESTAMO2025." + tabla + " WHERE ID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idReserva);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Reserva cancelada exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Actualizar la tabla
                boolean esAdmin = "ADMINISTRADOR".equalsIgnoreCase(tipoUsuario);
                cargarReservas(tipoReserva, cedulaUsuario, esAdmin);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se encontró la reserva en la base de datos", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error al conectar con la base de datos: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }





// Método auxiliar para determinar el tipo de reserva actual


        // TODO add your handling code here:
    }//GEN-LAST:event_CANCELARActionPerformed

    private void SALASActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SALASActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SALASActionPerformed

    private void EQUIPOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EQUIPOSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EQUIPOSActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CANCELAR;
    private javax.swing.JButton EQUIPOS;
    private javax.swing.JButton FILTRAR;
    private javax.swing.JTable RESERVAS;
    private javax.swing.JButton SALAS;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}

