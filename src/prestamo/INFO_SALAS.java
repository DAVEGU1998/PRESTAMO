package prestamo;

import CONEXION.CONEXION;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class INFO_SALAS extends JPanel {
    
    private DefaultTableModel model;
    
    
    private JButton crearSala, eliminar, inhabilitar;

    public INFO_SALAS() {
    initComponents();
    model = (DefaultTableModel) RECURSOS.getModel(); // ✅ Asegúrate de que esto esté aquí
    configurarTabla();
    configurarEventos();
    personalizarBotones();
    cargarDatos();  // Para cargar los datos al inicio
    ajustarAnchoColumnas();
    buscar.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String textoBusqueda = buscador.getText(); // suponiendo que buscador es un JTextField
        buscarSalas(textoBusqueda);
    }
});
    
   
   crear_sala.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        CREAR_SALAS crearUsuariosVentana = new CREAR_SALAS();
        crearUsuariosVentana.setVisible(true);
    }
});
}



    

    private void configurarTabla() {
    // Configurar modelo de columnas
    model = new DefaultTableModel();
    model.setColumnIdentifiers(new String[]{ "ID","Nombre", "Capacidad", "Descripción", "ESTADO","EDIFICIO"});
    RECURSOS.setModel(model);
    RECURSOS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    RECURSOS.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    RECURSOS.setRowHeight(25);

    // Ajustar automáticamente el ancho de las columnas
    ajustarAnchoColumnas();
}

// Método para ajustar el ancho de las columnas según el contenido más largo
// Método para ajustar automáticamente el ancho de las columnas
// Método para ajustar automáticamente el ancho de las columnas
private void ajustarAnchoColumnas() {
    // Recorre todas las columnas de la tabla
    for (int col = 0; col < RECURSOS.getColumnCount(); col++) {
        TableColumn column = RECURSOS.getColumnModel().getColumn(col);
        int maxWidth = 100; // Ancho mínimo por defecto

        // Ajusta el ancho al texto del encabezado
        TableCellRenderer headerRenderer = RECURSOS.getTableHeader().getDefaultRenderer();
        Component headerComp = headerRenderer.getTableCellRendererComponent(RECURSOS, column.getHeaderValue(), false, false, 0, col);
        maxWidth = Math.max(headerComp.getPreferredSize().width, maxWidth);

        // Ajusta el ancho al contenido de las celdas
        for (int row = 0; row < RECURSOS.getRowCount(); row++) {
            TableCellRenderer cellRenderer = RECURSOS.getCellRenderer(row, col);
            Component cellComp = RECURSOS.prepareRenderer(cellRenderer, row, col);
            
            // Para la columna de descripción, permite más espacio
            if (col == 2) { // Asumiendo que la descripción es la tercera columna
                maxWidth = Math.max(cellComp.getPreferredSize().width + 100, maxWidth);
            } else {
                maxWidth = Math.max(cellComp.getPreferredSize().width + 20, maxWidth);
            }
        }

        // Establece el ancho final de la columna
        column.setPreferredWidth(maxWidth);
    }
}






    private void personalizarBotones() {
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
    crear_sala.setBackground(new Color(60, 180, 60));
    crear_sala.setForeground(textColor);
    crear_sala.setFont(buttonFont);
    crear_sala.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(40, 140, 40)), 
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    crear_sala.setCursor(new Cursor(Cursor.HAND_CURSOR));
    // Botón CREAR USUARIO
    crear_edificio.setBackground(new Color(60, 180, 60));
    crear_edificio.setForeground(textColor);
    crear_edificio.setFont(buttonFont);
    crear_edificio.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(40, 140, 40)), 
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    crear_edificio.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
    agregarEfectoHover(crear_sala, new Color(60, 180, 60), new Color(40, 160, 40));
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
    private void configurarBoton(JButton boton, Color color) {
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(color.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(color);
            }
        });
    }

    private void configurarEventos() {
    // Evento para crear edificio
    crear_edificio.addActionListener(e -> {
        String nombreEdificio = JOptionPane.showInputDialog(this, 
                "¿Cuál es el nombre del nuevo edificio?", 
                "Nuevo Edificio", 
                JOptionPane.QUESTION_MESSAGE);
        
        if (nombreEdificio != null && !nombreEdificio.trim().isEmpty()) {
            try (Connection conn = CONEXION.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO EDIFICIO (NOMBRE) VALUES (?)")) {
                
                pstmt.setString(1, nombreEdificio.trim());
                pstmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, 
                        "Edificio '" + nombreEdificio.trim() + "' creado exitosamente.", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // ✅ Actualiza los datos después de crear el edificio
                cargarDatos();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, 
                        "Error al crear edificio: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                    "El nombre del edificio no puede estar vacío.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    });
}



    

    private void cargarDatos() {
    try (Connection conn = CONEXION.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(
             "SELECT ID, NOMBRE, CAPACIDAD, ESTADO, DESCRIPCION,EDIFICIO FROM INFO_SALAS ORDER BY ID")) {
        
        // ✅ Limpiar filas, no cambiar el diseño de la tabla
        model.setRowCount(0);
        
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("ID"),              // Ahora agregamos el ID
                rs.getString("NOMBRE"),
                rs.getInt("CAPACIDAD"),
                rs.getString("DESCRIPCION"),
                rs.getString("ESTADO"),
                rs.getString("EDIFICIO")
            });
        }
        
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error al cargar salas: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}





    private void buscarSalas(String texto) {
    if (texto == null || texto.trim().equalsIgnoreCase("Buscar")) {
        texto = "";
    }
    
    try (Connection conn = CONEXION.conectar();
         PreparedStatement pstmt = conn.prepareStatement(
             "SELECT s.ID, s.NOMBRE, e.NOMBRE AS EDIFICIO, s.CAPACIDAD, s.ESTADO " +
             "FROM INFO_SALAS s " +
             "JOIN EDIFICIO e ON s.ID_EDIFICIO = e.ID " +
             "WHERE LOWER(s.NOMBRE) LIKE ? OR LOWER(e.NOMBRE) LIKE ? OR " +
             "TO_CHAR(s.CAPACIDAD) LIKE ? OR LOWER(s.ESTADO) LIKE ?")) {
        
        // Preparar el parámetro de búsqueda
        String parametro = "%" + texto.trim().toLowerCase() + "%";
        for (int i = 1; i <= 4; i++) {
            pstmt.setString(i, parametro);
        }
        
        // Ejecutar la consulta
        ResultSet rs = pstmt.executeQuery();
        model.setRowCount(0);
        
        // Cargar los resultados en la tabla
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("ID"),
                rs.getString("NOMBRE"),
                rs.getString("EDIFICIO"),
                rs.getInt("CAPACIDAD"),
                rs.getString("ESTADO")
            });
        }
        
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this,
            "Error al buscar salas: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void eliminarSala() {
    int fila = RECURSOS.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione una sala para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Asegúrate de tratar el ID como Integer y no como String
    Integer id = (Integer) model.getValueAt(fila, 0);  // Asegúrate de que la columna 0 sea el ID y sea un Integer
    String nombre = (String) model.getValueAt(fila, 1);  // El nombre sigue siendo un String
    
    int confirm = JOptionPane.showConfirmDialog(this,
        "¿Eliminar la sala '" + nombre + "'?",
        "Confirmar eliminación",
        JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        try (Connection conn = CONEXION.conectar();
             PreparedStatement pstmt = conn.prepareStatement(
                 "DELETE FROM INFO_SALAS WHERE ID = ?")) {
            
            pstmt.setInt(1, id);  // El ID se pasa como Integer, que es lo correcto para números enteros
            pstmt.executeUpdate();
            cargarDatos();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al eliminar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



private void cambiarEstadoSala() {
    int fila = RECURSOS.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione una sala para cambiar su estado.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int id = (Integer) model.getValueAt(fila, 0);
    String nombre = (String) model.getValueAt(fila, 1);
    
    String nuevoEstado = JOptionPane.showInputDialog(this, 
            "Cambiar estado de '" + nombre + "'. Escriba el nuevo estado (DISPONIBLE/INHABILITADO):", 
            "Cambiar Estado", 
            JOptionPane.QUESTION_MESSAGE);
    
    if (nuevoEstado != null && (nuevoEstado.equalsIgnoreCase("DISPONIBLE") || nuevoEstado.equalsIgnoreCase("INHABILITADO"))) {
        try (Connection conn = CONEXION.conectar();
             PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE INFO_SALAS SET ESTADO = ? WHERE ID = ?")) {
            
            pstmt.setString(1, nuevoEstado.toUpperCase());
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            cargarDatos();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, 
                "El estado debe ser 'DISPONIBLE' o 'INHABILITADO'.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
    }
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
        Inhabilitar = new javax.swing.JButton();
        crear_sala = new javax.swing.JButton();
        crear_edificio = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(980, 720));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel8.setText("Administracion de Salas");
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

        buscador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscadorActionPerformed(evt);
            }
        });
        jPanel1.add(buscador, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 60, 170, 30));

        buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/buscar.png"))); // NOI18N
        jPanel1.add(buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 60, 40, 30));

        Eliminar.setText("ELIMINAR");
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });
        jPanel1.add(Eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 50, 130, 40));

        Inhabilitar.setText("ACTIVAR/INACTIVO");
        Inhabilitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InhabilitarActionPerformed(evt);
            }
        });
        jPanel1.add(Inhabilitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 140, 40));

        crear_sala.setText("CREAR SALA");
        jPanel1.add(crear_sala, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 40));

        crear_edificio.setText("CREAR EDIFICIO");
        crear_edificio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crear_edificioActionPerformed(evt);
            }
        });
        jPanel1.add(crear_edificio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 130, 40));

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
    int fila = RECURSOS.getSelectedRow();  // Obtener la fila seleccionada

    // Verificar si se ha seleccionado una fila
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona un recurso para cambiar su estado.");
        return;
    }

    // Obtener el NOMBRE (columna 0) y el ESTADO (columna 3) de la fila seleccionada
    String nombre = (String) RECURSOS.getValueAt(fila, 1);  // Columna NOMBRE
    String estadoActual = (String) RECURSOS.getValueAt(fila, 4);  // Columna ESTADO

    // Validar que el nombre no esté vacío
    if (nombre == null || nombre.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El nombre del recurso no puede estar vacío.");
        return;
    }

    // Determinar el nuevo estado
    String nuevoEstado = estadoActual.equalsIgnoreCase("ACTIVO") ? "INACTIVO" : "ACTIVO";

    // Conectar a la base de datos para actualizar el estado
    try (Connection conn = CONEXION.conectar();
         PreparedStatement pstmt = conn.prepareStatement("UPDATE INFO_SALAS SET ESTADO = ? WHERE NOMBRE = ?")) {
        
        pstmt.setString(1, nuevoEstado);  // Nuevo estado
        pstmt.setString(2, nombre);  // Nombre del recurso a actualizar

        int filasAfectadas = pstmt.executeUpdate();  // Ejecutar actualización

        if (filasAfectadas > 0) {
            // Si la actualización fue exitosa, actualizar el estado en la tabla
            RECURSOS.setValueAt(nuevoEstado, fila, 4);  // Columna 3 es ESTADO
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
        eliminarSala();

        // TODO add your handling code here:
    }//GEN-LAST:event_EliminarActionPerformed

    private void buscadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buscadorActionPerformed

    private void crear_edificioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crear_edificioActionPerformed
              // TODO add your handling code here:
    }//GEN-LAST:event_crear_edificioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Eliminar;
    private javax.swing.JButton Inhabilitar;
    private javax.swing.JTable RECURSOS;
    private javax.swing.JTextField buscador;
    private javax.swing.JButton buscar;
    private javax.swing.JButton crear_edificio;
    private javax.swing.JButton crear_sala;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}

