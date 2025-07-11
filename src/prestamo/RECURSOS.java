package prestamo;

import CONEXION.CONEXION;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

public class RECURSOS extends JPanel {
   
    private DefaultTableModel model;
    private String[] columnNames;
    private Object oldValue; // Para almacenar el valor antes de editar
    
    public RECURSOS() {
        initComponents();
        personalizarBotones();
        
        configurarTabla();
        cargarDatos();
        
        crear_recurso.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        CREAR_RECURSOS crearUsuariosVentana = new CREAR_RECURSOS();
        crearUsuariosVentana.setVisible(true);
    }
});

        buscador.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        buscar.doClick(); // Simula clic en el botón
    }
});
    buscar.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String textoBusqueda = buscador.getText(); // suponiendo que buscador es un JTextField
        buscarUsuarios(textoBusqueda);
    }
});
    buscador.setText("Buscar");
buscador.setForeground(Color.GRAY);

buscador.addFocusListener(new FocusListener() {
    @Override
    public void focusGained(FocusEvent e) {
        if (buscador.getText().equals("Buscar")) {
            buscador.setText("");
            buscador.setForeground(Color.BLACK);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (buscador.getText().isEmpty()) {
            buscador.setText("Buscar");
            buscador.setForeground(Color.GRAY);
        }
    }
});

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
    
private void personalizarBotones() {
    // Configuración común para todos los botones
    Font buttonFont = new Font("Segoe UI", Font.BOLD, 12);
    Color textColor = Color.WHITE;
    
    // Botón BUSCAR (solo icono)
    buscar.setText(""); // Eliminar texto para que solo muestre el icono
    buscar.setBackground(new Color(51, 102, 255));
    buscar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    buscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // Botón ELIMINAR
    Eliminar.setBackground(new Color(255, 80, 80));
    Eliminar.setForeground(textColor);
    Eliminar.setFont(buttonFont);
    Eliminar.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 60, 60)), 
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    Eliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // Botón CREAR USUARIO
    crear_recurso.setBackground(new Color(60, 180, 60));
    crear_recurso.setForeground(textColor);
    crear_recurso.setFont(buttonFont);
    crear_recurso.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(40, 140, 40)), 
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    crear_recurso.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // Botón ACTIVAR/INACTIVO
    Inhabilitar.setBackground(new Color(255, 165, 0));
    Inhabilitar.setForeground(textColor);
    Inhabilitar.setFont(buttonFont);
    Inhabilitar.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(205, 125, 0)), 
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    Inhabilitar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // Añadir efectos hover a todos los botones
    agregarEfectoHover(buscar, new Color(51, 102, 255), new Color(0, 75, 255));
    agregarEfectoHover(Eliminar, new Color(255, 80, 80), new Color(255, 60, 60));
    agregarEfectoHover(crear_recurso, new Color(60, 180, 60), new Color(40, 160, 40));
    agregarEfectoHover(Inhabilitar, new Color(255, 165, 0), new Color(255, 140, 0));
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
        RECURSOS.setModel(model);
        
        // Editor personalizado para manejar la confirmación
        RECURSOS.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
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
                    String columnName = columnNames[RECURSOS.getSelectedColumn()];
                    String nombreUsuario = model.getValueAt(RECURSOS.getSelectedRow(), 1).toString(); // Asume columna 1 es nombre
                    
                    int confirm = JOptionPane.showConfirmDialog(RECURSOS.this,
                        "¿Confirmar cambio para " + nombreUsuario + "?\n" +
                        "Campo: " + columnName + "\n" +
                        "De: " + oldValue + "\n" +
                        "A: " + newValue,
                        "Confirmar Edición",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean result = super.stopCellEditing();
                        if (result) {
                            actualizarEnBD(RECURSOS.getSelectedRow(), RECURSOS.getSelectedColumn(), newValue);
                            
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
    texto = texto.toLowerCase();
    try (Connection conn = CONEXION.conectar();
         PreparedStatement pstmt = conn.prepareStatement(
             "SELECT COLOR, MARCA, INVENTARIO, ESTADO, DESCRIPCION " +
             "FROM EQUIPO WHERE " +
             "LOWER(COLOR) LIKE ? OR " +
             "LOWER(MARCA) LIKE ? OR " +
             "INVENTARIO LIKE ? OR " +
             "LOWER(ESTADO) LIKE ? OR " +
             "LOWER(DESCRIPCION) LIKE ?")) {

        for (int i = 1; i <= 5; i++) {
            pstmt.setString(i, "%" + texto + "%");
        }

        ResultSet rs = pstmt.executeQuery();
        
        // Usamos el mismo orden de columnas que en cargarDatos()
        String[] columnasMostradas = {"INVENTARIO", "MARCA", "COLOR", "DESCRIPCION", "ESTADO"};
        columnNames = columnasMostradas;

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getObject("INVENTARIO"));
            row.add(rs.getObject("MARCA"));
            row.add(rs.getObject("COLOR"));
            row.add(rs.getObject("DESCRIPCION"));
            row.add(rs.getObject("ESTADO"));
            data.add(row);
        }

        model.setDataVector(data, new Vector<String>() {{
            for (String name : columnNames) add(name);
        }});
        ajustarAnchoColumnas();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this,
            "Error al buscar recursos: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

    
    public void cargarDatos() {
    try (Connection conn = CONEXION.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT COLOR, MARCA, INVENTARIO, ESTADO, ID_RECURSO, DESCRIPCION, TIPO_RECURSO FROM EQUIPO ORDER BY INVENTARIO")) {
        
        ResultSetMetaData metaData = rs.getMetaData();
        
        // Definir manualmente el orden de las columnas que queremos mostrar
        String[] columnasMostradas = {"TIPO_RECURSO","INVENTARIO", "MARCA", "COLOR", "DESCRIPCION", "ESTADO"};
        columnNames = columnasMostradas;
        
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            // Orden correspondiente al SELECT: COLOR, MARCA, INVENTARIO, ESTADO, ID_RECURSO, DESCRIPCION
            // Pero lo reordenamos según columnasMostradas
            row.add(rs.getObject("TIPO_RECURSO"));         // ESTADO
            row.add(rs.getObject("INVENTARIO"));    // INVENTARIO
            row.add(rs.getObject("MARCA"));         // MARCA
            row.add(rs.getObject("COLOR"));         // COLOR
            row.add(rs.getObject("DESCRIPCION"));    // DESCRIPCION
            
            row.add(rs.getObject("ESTADO"));         // ESTADO
            
            // No agregamos ID_RECURSO a los datos mostrados
            
            data.add(row);
        }
        
        model.setDataVector(data, new Vector<String>() {{
            for (String name : columnNames) add(name);
        }});
        
        ajustarAnchoColumnas();
        
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error al cargar recursos: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}
    
    // Reemplaza TODO el método actualizarEnBD con este código
private void actualizarEnBD(int row, int col, Object newValue) {
    String columnName = columnNames[col];
    Object numeroInventario = model.getValueAt(row, 1); // Columna INVENTARIO (2da columna)

    // Si estás intentando actualizar la columna INVENTARIO
    if (col == 1) {
        try {
            // Asegurarse de que el nuevo valor sea un número válido
            int nuevoInventario = Integer.parseInt(newValue.toString());

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try (Connection conn = CONEXION.conectar();
                         PreparedStatement pstmt = conn.prepareStatement(
                             "UPDATE EQUIPO SET INVENTARIO = ? WHERE INVENTARIO = ?")) {

                        // Establecer el nuevo y el antiguo valor
                        pstmt.setInt(1, nuevoInventario);
                        pstmt.setInt(2, Integer.parseInt(numeroInventario.toString()));

                        int affectedRows = pstmt.executeUpdate();

                        SwingUtilities.invokeLater(() -> {
                            if (affectedRows > 0) {
                                JOptionPane.showMessageDialog(RECURSOS.this,
                                    "¡Recurso actualizado correctamente!",
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                model.setValueAt(oldValue, row, col);
                                JOptionPane.showMessageDialog(RECURSOS.this,
                                    "No se pudo actualizar el recurso",
                                    "Error", JOptionPane.WARNING_MESSAGE);
                            }
                        });
                    } catch (SQLException ex) {
                        SwingUtilities.invokeLater(() -> {
                            model.setValueAt(oldValue, row, col);
                            JOptionPane.showMessageDialog(RECURSOS.this,
                                "Error al actualizar: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        });
                        ex.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(RECURSOS.this,
                "El valor para INVENTARIO debe ser un número entero.",
                "Error de Formato", JOptionPane.ERROR_MESSAGE);
            model.setValueAt(oldValue, row, col);
        }
        return;
    }

    // Manejar otros campos normalmente
    new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            try (Connection conn = CONEXION.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE EQUIPO SET " + columnName + " = ? WHERE INVENTARIO = ?")) {

                if (newValue instanceof Number) {
                    pstmt.setObject(1, newValue);
                } else {
                    pstmt.setString(1, newValue.toString());
                }

                pstmt.setInt(2, Integer.parseInt(numeroInventario.toString()));

                int affectedRows = pstmt.executeUpdate();

                SwingUtilities.invokeLater(() -> {
                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(RECURSOS.this,
                            "¡Recurso actualizado correctamente!",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        model.setValueAt(oldValue, row, col);
                        JOptionPane.showMessageDialog(RECURSOS.this,
                            "No se pudo actualizar el recurso",
                            "Error", JOptionPane.WARNING_MESSAGE);
                    }
                });
            } catch (SQLException ex) {
                SwingUtilities.invokeLater(() -> {
                    model.setValueAt(oldValue, row, col);
                    JOptionPane.showMessageDialog(RECURSOS.this,
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
    if (RECURSOS == null || RECURSOS.getColumnModel() == null) return;
    
    // Desactivar autoajuste temporalmente
    RECURSOS.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    
    TableColumnModel columnModel = RECURSOS.getColumnModel();
    
    for (int column = 0; column < RECURSOS.getColumnCount(); column++) {
        TableColumn col = columnModel.getColumn(column);
        int width = 15; // Margen mínimo
        
        // 1. Obtener ancho del encabezado
        TableCellRenderer headerRenderer = col.getHeaderRenderer();
        if (headerRenderer == null) {
            headerRenderer = RECURSOS.getTableHeader().getDefaultRenderer();
        }
        Component headerComp = headerRenderer.getTableCellRendererComponent(RECURSOS, col.getHeaderValue(), false, false, -1, column);
        width = Math.max(width, headerComp.getPreferredSize().width);
        
        // 2. Obtener ancho máximo del contenido
        for (int row = 0; row < RECURSOS.getRowCount(); row++) {
            TableCellRenderer renderer = RECURSOS.getCellRenderer(row, column);
            Component comp = RECURSOS.prepareRenderer(renderer, row, column);
            width = Math.max(width, comp.getPreferredSize().width + 10); // +10 de margen
        }
        
        // 3. Establecer el ancho con límites (mín 50px, máx 300px)
        col.setPreferredWidth(Math.min(Math.max(width, 50), 300));
    }
    
      // Restaurar el autoajuste
    RECURSOS.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
   private void eliminarRecursoSeleccionado() {
    int filaSeleccionada = RECURSOS.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, 
            "Por favor seleccione un recurso de la tabla", 
            "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Obtener el número de inventario (primera columna visible)
    Object numeroInventario = model.getValueAt(filaSeleccionada, 1);
    Object marca = model.getValueAt(filaSeleccionada, 2);
    
    int confirmacion = JOptionPane.showConfirmDialog(this,
        "¿Está seguro que desea eliminar el recurso?\n" +
        "Inventario: " + numeroInventario + "\n" +
        "Marca: " + marca,
        "Confirmar Eliminación",
        JOptionPane.YES_NO_OPTION);
    
    if (confirmacion == JOptionPane.YES_OPTION) {
        eliminarPorInventario(numeroInventario, filaSeleccionada);
    }
}

private void eliminarPorInventario(Object numeroInventario, int filaTabla) {
    new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            try (Connection conn = CONEXION.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(
                     "DELETE FROM EQUIPO WHERE INVENTARIO = ?")) {
                
                pstmt.setBigDecimal(1, new BigDecimal(numeroInventario.toString()));
                
                int affectedRows = pstmt.executeUpdate();
                
                SwingUtilities.invokeLater(() -> {
                    if (affectedRows > 0) {
                        model.removeRow(filaTabla);
                        JOptionPane.showMessageDialog(RECURSOS.this,
                            "Recurso eliminado correctamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(RECURSOS.this,
                            "No se pudo eliminar el recurso",
                            "Error", JOptionPane.WARNING_MESSAGE);
                    }
                });
            } catch (SQLException ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(RECURSOS.this,
                        "Error al eliminar: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                });
                ex.printStackTrace();
            }
            return null;
        }
    }.execute();
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
        RECURSOS = new javax.swing.JTable();
        buscador = new javax.swing.JTextField();
        buscar = new javax.swing.JButton();
        Eliminar = new javax.swing.JButton();
        crear_recurso = new javax.swing.JButton();
        Inhabilitar = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(980, 720));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel8.setText("Administracion de Recursos");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 0, 430, 40));

        RECURSOS.setModel(new javax.swing.table.DefaultTableModel(
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
        RECURSOS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RECURSOSMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(RECURSOS);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 960, 560));
        jPanel1.add(buscador, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 60, 170, 30));

        buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/buscar.png"))); // NOI18N
        jPanel1.add(buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 60, 40, 30));

        Eliminar.setText("ELIMINAR");
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });
        jPanel1.add(Eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 130, 40));

        crear_recurso.setText("CREAR RECURSO");
        crear_recurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crear_recursoActionPerformed(evt);
            }
        });
        jPanel1.add(crear_recurso, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 40));

        Inhabilitar.setText("ACTIVAR/INACTIVO");
        Inhabilitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InhabilitarActionPerformed(evt);
            }
        });
        jPanel1.add(Inhabilitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 140, 40));

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

    private void RECURSOSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RECURSOSMouseClicked
     if (evt.getClickCount() == 2) { // Doble clic
            int row = RECURSOS.getSelectedRow();
            if (row >= 0) {
                // Puedes implementar edición específica aquí si lo deseas
            }
        }          // TODO add your handling code here:
    }//GEN-LAST:event_RECURSOSMouseClicked

    private void InhabilitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InhabilitarActionPerformed
    int fila = RECURSOS.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona un recurso para cambiar su estado.");
        return;
    }

    // Obtener el número de inventario y el estado actual
    Object inventario = RECURSOS.getValueAt(fila, 1); // Columna 0 es INVENTARIO
    Object estadoActual = RECURSOS.getValueAt(fila, 5); // Columna 4 es ESTADO

    // Validar que el inventario no esté vacío
    if (inventario == null || inventario.toString().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El número de inventario no puede estar vacío.");
        return;
    }

    String nuevoEstado = estadoActual.toString().equalsIgnoreCase("ACTIVO") ? "INACTIVO" : "ACTIVO";

    try (Connection conn = CONEXION.conectar();
         PreparedStatement pstmt = conn.prepareStatement("UPDATE EQUIPO SET ESTADO = ? WHERE INVENTARIO = ?")) {

        pstmt.setString(1, nuevoEstado);
        pstmt.setString(2, inventario.toString().trim());
        int filasAfectadas = pstmt.executeUpdate();

        if (filasAfectadas > 0) {
            // Actualizar el estado en la tabla
            RECURSOS.setValueAt(nuevoEstado, fila, 5);
            JOptionPane.showMessageDialog(this, "Estado actualizado a: " + nuevoEstado);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado. Verifica que el recurso exista.");
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error actualizando estado: " + ex.getMessage());
    }
    // TODO add your handling code here:
    }//GEN-LAST:event_InhabilitarActionPerformed

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed
                           
    eliminarRecursoSeleccionado();

        // TODO add your handling code here:
    }//GEN-LAST:event_EliminarActionPerformed

    private void crear_recursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crear_recursoActionPerformed
                // TODO add your handling code here:
    }//GEN-LAST:event_crear_recursoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Eliminar;
    private javax.swing.JButton Inhabilitar;
    private javax.swing.JTable RECURSOS;
    private javax.swing.JTextField buscador;
    private javax.swing.JButton buscar;
    private javax.swing.JButton crear_recurso;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}

