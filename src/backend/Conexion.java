/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    Connection cn;
    
    private final String user = "root";
    private final String password = "12348765";
    private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String url;

    public Conexion() {
        this.url = "jdbc:mysql://localhost:3306/Bloom";
    }
    
    public Connection EnlaceSQL(){
        try{
            Class.forName(driver);
            cn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión Establecida!");
            return cn;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver MySQL no encontrado..\n" + e);
            return null;
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: \n" + e.getMessage());
            return null;
        }
    }
    
    public void CerrarConexion(){
        try {
            if (cn != null && !cn.isClosed()) {
                cn.close();
                System.out.println("Conexión Cerrada!");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: "+e.getMessage());
        }
    }
}
