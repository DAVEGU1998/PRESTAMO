package CONEXION;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class CONEXION {
    
    private static final String URL = "jdbc:oracle:thin:@192.168.254.215:1521:orcl"; 
    private static final String USER = "PRESTAMO2025";
    private static final String PASSWORD = "DAVEGU";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            System.out.println("📢 Cargando el driver de Oracle...");
            Class.forName("oracle.jdbc.driver.OracleDriver"); 

            System.out.println("📢 Intentando conectar a la base de datos...");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión exitosa a Oracle Database");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Error: No se encontró el driver JDBC de Oracle.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión a la base de datos.");
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




