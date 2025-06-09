package CONEXION;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class CONEXION {
    private static final String URL = "jdbc:oracle:thin:@192.168.254.215:1521:orcl"; 

    // Por defecto es el administrador
    private static String USER = "PRESTAMO2025";
    private static String PASSWORD = "DAVEGU";

    // Este método se puede usar para cambiar el usuario actual después del login
    public static void setCredenciales(String usuario, String contraseña) {
        USER = usuario;
        PASSWORD = contraseña;
    }

    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conexion;
    }
   

    

    public static void mostrarTablas() {
        Connection conexion = conectar();
        if (conexion != null) {
            try {
                Statement stmt = conexion.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT TABLE_NAME FROM USER_TABLES");

                System.out.println("📋 Listado de tablas en el esquema actual:");
                while (rs.next()) {
                    System.out.println("🗂 " + rs.getString("TABLE_NAME"));
                }

                rs.close();
                stmt.close();
                conexion.close();
            } catch (SQLException e) {
                System.out.println("❌ Error al obtener la lista de tablas.");
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠ No se pudo establecer conexión con la base de datos.");
        }
    }

    public static void main(String[] args) {
        mostrarTablas();
    }
}




