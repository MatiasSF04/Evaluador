/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import frontend.Principal;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Listado {
    public Listado(){}
    Principal Main;
    
    
    
    Map<String, int> asignaturas = Map.of(
        Main.cbAsignatura)
    Map<String, String> tipos = new HashMap<>();
    Map<String, int> niveles = new HashMap<>();
    
    
   
    public void MostrarLista(JTable tbLista){
        Conexion con = new Conexion();
        Connection cn = con.EnlaceSQL();
        
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("enunciado");
        modelo.addColumn("tipo");
        modelo.addColumn("nivel");
        modelo.addColumn("tiempo");
        modelo.addColumn("asignatura");
        
        String Consulta = "SELECT * FROM Bloom.preguntas WHERE Tipo = 'selMultiple' && Nivel = '2' ORDER BY RAND() LIMIT 5;";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(Consulta);
            while(rs.next()) {
                Object [] Lista = {
                    rs.getString(3),
                    rs.getInt(9),
                    rs.getInt(10),
                    rs.getInt(11),
                    rs.getInt(12)
                };
                modelo.addRow(Lista);
            }
            tbLista.setModel(modelo);
            cn.close();
        } catch (Exception e) {
            System.out.println("Error: " + String.valueOf(e));
        }
    }
}
