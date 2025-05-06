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
    
    public RESERVAS() {
        initComponents();
        personalizarBotones();
        configurarTabla();
        cargarDatos();
        


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
    FILTRAR.setBackground(new Color(60, 180, 60));
    FILTRAR.setForeground(textColor);
    FILTRAR.setFont(buttonFont);
    FILTRAR.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(40, 140, 40)), 
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    FILTRAR.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // Botón ACTIVAR/INACTIVO
    MODIFICAR.setBackground(new Color(255, 165, 0));
    MODIFICAR.setForeground(textColor);
    MODIFICAR.setFont(buttonFont);
    MODIFICAR.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(205, 125, 0)), 
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    MODIFICAR.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
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
        USUARIOS.setModel(model);
        
        // Editor personalizado para manejar la confirmación
        USUARIOS.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
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
                    String columnName = columnNames[USUARIOS.getSelectedColumn()];
                    String nombreUsuario = model.getValueAt(USUARIOS.getSelectedRow(), 1).toString(); // Asume columna 1 es nombre
                    
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
                            actualizarEnBD(USUARIOS.getSelectedRow(), USUARIOS.getSelectedColumn(), newValue);
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
             "SELECT * FROM USUARIOS WHERE " +
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
    if (USUARIOS == null || USUARIOS.getColumnModel() == null) return;
    
    // Desactivar autoajuste temporalmente
    USUARIOS.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    
    TableColumnModel columnModel = USUARIOS.getColumnModel();
    
    for (int column = 0; column < USUARIOS.getColumnCount(); column++) {
        TableColumn col = columnModel.getColumn(column);
        int width = 15; // Margen mínimo
        
        // 1. Obtener ancho del encabezado
        TableCellRenderer headerRenderer = col.getHeaderRenderer();
        if (headerRenderer == null) {
            headerRenderer = USUARIOS.getTableHeader().getDefaultRenderer();
        }
        Component headerComp = headerRenderer.getTableCellRendererComponent(
            USUARIOS, col.getHeaderValue(), false, false, -1, column);
        width = Math.max(width, headerComp.getPreferredSize().width);
        
        // 2. Obtener ancho máximo del contenido
        for (int row = 0; row < USUARIOS.getRowCount(); row++) {
            TableCellRenderer renderer = USUARIOS.getCellRenderer(row, column);
            Component comp = USUARIOS.prepareRenderer(renderer, row, column);
            width = Math.max(width, comp.getPreferredSize().width + 10); // +10 de margen
        }
        
        // 3. Establecer el ancho con límites (mín 50px, máx 300px)
        col.setPreferredWidth(Math.min(Math.max(width, 50), 300));
    }
    
    // Restaurar el autoajuste
    USUARIOS.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
        USUARIOS = new javax.swing.JTable();
        CANCELAR = new javax.swing.JButton();
        FILTRAR = new javax.swing.JButton();
        MODIFICAR = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(980, 720));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel8.setText("Administracion de Reservas");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 0, 430, 40));

        USUARIOS.setModel(new javax.swing.table.DefaultTableModel(
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
        USUARIOS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                USUARIOSMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(USUARIOS);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 960, 560));

        CANCELAR.setText("CANCELAR");
        CANCELAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CANCELARActionPerformed(evt);
            }
        });
        jPanel1.add(CANCELAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 130, 40));

        FILTRAR.setText("FILTRAR");
        jPanel1.add(FILTRAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 40));

        MODIFICAR.setText("MODIFICAR");
        MODIFICAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MODIFICARActionPerformed(evt);
            }
        });
        jPanel1.add(MODIFICAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 140, 40));

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

    private void USUARIOSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_USUARIOSMouseClicked
     if (evt.getClickCount() == 2) { // Doble clic
            int row = USUARIOS.getSelectedRow();
            if (row >= 0) {
                // Puedes implementar edición específica aquí si lo deseas
            }
        }          // TODO add your handling code here:
    }//GEN-LAST:event_USUARIOSMouseClicked

    private void MODIFICARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MODIFICARActionPerformed
        int fila = USUARIOS.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona un usuario para cambiar su estado.");
        return;
    }

    String cedula = USUARIOS.getValueAt(fila, 9).toString(); // CEDULA debe estar en la columna 9
    String estadoActual = USUARIOS.getValueAt(fila, 10).toString(); // ESTADO debe estar en la columna 10
    String nuevoEstado = estadoActual.equalsIgnoreCase("ACTIVO") ? "INACTIVO" : "ACTIVO";

    try (Connection conn = CONEXION.conectar();
         PreparedStatement pstmt = conn.prepareStatement("UPDATE USUARIOS SET ESTADO = ? WHERE CEDULA = ?")) {

        pstmt.setString(1, nuevoEstado);
        pstmt.setString(2, cedula);
        pstmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "Estado actualizado a: " + nuevoEstado);
        cargarDatos(); // Refresca la tabla

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error actualizando estado: " + ex.getMessage());
    }
    // TODO add your handling code here:
    }//GEN-LAST:event_MODIFICARActionPerformed

    private void CANCELARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CANCELARActionPerformed
                           
    int fila = USUARIOS.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona un usuario para eliminar.");
        return;
    }

    String cedula = USUARIOS.getValueAt(fila, 9).toString(); // Ajusta si la columna es distinta

    int confirmar = JOptionPane.showConfirmDialog(this,
        "¿Estás seguro de que quieres eliminar este usuario?\nEsta acción no se puede deshacer.",
        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

    if (confirmar == JOptionPane.YES_OPTION) {
        try (Connection conn = CONEXION.conectar();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM USUARIOS WHERE CEDULA = ?")) {

            pstmt.setString(1, cedula);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                cargarDatos(); // Refresca tabla
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el usuario.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }

        // TODO add your handling code here:
    }//GEN-LAST:event_CANCELARActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CANCELAR;
    private javax.swing.JButton FILTRAR;
    private javax.swing.JButton MODIFICAR;
    private javax.swing.JTable USUARIOS;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}

