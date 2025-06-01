/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import frontend.Principal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ConsultaPreguntas {
    Conexion con = new Conexion();
    Connection cn = con.EnlaceSQL();
    
    Principal Main;
    
    private void cargarPreguntasDesdeBD() {
        if (cn == null) {
            JOptionPane.showMessageDialog(null, "No nos conectamos con la BD :(");
            return;
        }
            
        try {
            Statement st = cn.createStatement();
            //ResultSet rs = st.executeQuery("SELECT * FROM preguntas");
            ResultSet rs = st.executeQuery("SELECT * FROM Bloom.preguntas WHERE Tipo = 'selMultiple' && Nivel = '2' ORDER BY RAND() LIMIT 5");

            while (rs.next()) {
                Pregunta p = new Pregunta(
                    rs.getInt("Id_Pregunta"),
                    rs.getString("Enunciado"),
                    rs.getString("Respuesta_1"),
                    rs.getString("Respuesta_2"),
                    rs.getString("Respuesta_3"),
                    rs.getString("Respuesta_4"),
                    rs.getInt("Respuesta_Correcta"),
                    rs.getString("Tipo"),
                    rs.getString("Nivel"),
                    rs.getInt("Tiempo")
                );
                //preguntas.add(p);
            }
            con.CerrarConexion();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar preguntas: " + ex.getMessage());
        }
    } 
}
