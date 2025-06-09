/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
    Connection cn;
    
    private String user;
    private String password;
    private String url;
    private final String driver = "com.mysql.cj.jdbc.Driver";
    
    public Conexion() {
        try (InputStream input = getClass().getResourceAsStream("/backend/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            this.user = prop.getProperty("db.user");
            this.password = prop.getProperty("db.password");
            this.url = prop.getProperty("db.url");
        } catch (Exception e) {
            System.out.println("Error cargando configuración de base de datos: " + e.getMessage());
        }
    }
    
    public Connection EnlaceSQL() {
        try {
            Class.forName(driver);
            cn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión Establecida!");
            return cn;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver MySQL no encontrado.\n" + e);
            return null;
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: \n" + e.getMessage());
            return null;
        }
    }

    public void CerrarConexion() {
        try {
            if (cn != null && !cn.isClosed()) {
                cn.close();
                System.out.println("Conexión Cerrada!");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}