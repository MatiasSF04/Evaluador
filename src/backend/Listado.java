/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import frontend.Principal;
import backend.Lista;

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
    Map<String, Integer> mapaAsignatura = Map.of(
        "1. Matemáticas", 1,
        "2. Programación", 2,
        "3. Bases de Datos", 3,
        "4. Sistemas Operativos y Redes", 4
    );
    Map<String, String> mapaTipo = Map.of(
        "Seleccion Multiple", "selMultiple",
        "Verdadero o Falso", "verOfal"
    );
    Map<String, Integer> mapaBloom = Map.of(
        "1. Recordar", 1,
        "2. Comprender", 2,
        "3. Aplicar", 3,
        "4. Analizar", 4,
        "5. Evaluar", 5,
        "6. Crear", 6
    );

    
    public void MostrarLista(JTable tbLista){
        Conexion con = new Conexion();
        Connection cn = con.EnlaceSQL();
        
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("enunciado");
        modelo.addColumn("tipo");
        modelo.addColumn("nivel");
        modelo.addColumn("tiempo");
        modelo.addColumn("asignatura");
        
        try {
            //Obtenemos las selecciones del usuario
            String selecAsignatura = Main.cbAsignatura.getSelectedItem().toString();
            String selecTipo = Main.cbTipo.getSelectedItem().toString();
            String selecBloom = Main.cbBloom.getSelectedItem().toString();
            String selecCantidad = Main.cbCantidad.getValue().toString();

            //Las homologamos con los valores de nuestra base
            int idAsignatura = mapaAsignatura.get(selecAsignatura);
            String tipoSQL = mapaTipo.get(selecTipo);
            int nivelBloom = mapaBloom.get(selecBloom);

            //String Consulta = "SELECT * FROM Bloom.preguntas WHERE Tipo = 'selMultiple' && Nivel = '2' ORDER BY RAND() LIMIT 5;";
            //Generamos la consulta
            String Consulta = "SELECT * FROM Bloom.preguntas " +
                              "WHERE Tipo = '" + tipoSQL + "' " +
                              "AND Nivel = " + nivelBloom + " " +
                              "AND Asignatura = " + idAsignatura + " " +
                              "ORDER BY RAND() LIMIT " + selecCantidad;
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
