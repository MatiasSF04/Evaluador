/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

//Esta clase contiene la declaracion de la clase examen con sus atributos y sus getters
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Examen {
    Conexion con = new Conexion();
    Connection cn = con.EnlaceSQL();
      
    /*
    public void agregarPregunta(Lista l){
        String Consulta;
        Consulta = "INSERT INTO Bloom.examen(enunciado, tipo, nivel, tiempo, asignatura) VALUES (?,?,?,?, ?)";
        try{
            PreparedStatement ps = cn.prepareStatement(Consulta);
            ps.setString(1,l.getEnunciado());
            ps.setString(2,l.getTipo());
            ps.setString(3,l.getNivel());
            ps.setInt(4,l.getTiempo());
            ps.setString(5, l.getAsignatura());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Registro Guardado!");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: "+e);
        }
    }
    */
    
    // 1. Obtener preguntas filtradas desde la base
    public List<Pregunta> obtenerPreguntasFiltradas(String tipo, String nivel, int cantidad) {
        ConsultaPreguntas cp = new ConsultaPreguntas();
        return cp.obtenerPreguntasFiltradas(tipo, nivel, cantidad);
    }

    // 2. Insertar las preguntas a la tabla examen
    public void insertarPreguntasEnExamen(List<Pregunta> preguntas, String asignatura) {
        for (Pregunta p : preguntas) {
            try {
                String insert = "INSERT INTO Bloom.examen(Id_Pregunta, Enunciado, RespuestaA, RespuestaB, RespuestaC, RespuestaD, Correcta, Tipo, Nivel, Tiempo, Asignatura) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = cn.prepareStatement(insert);
                ps.setInt(1, p.getId());
                ps.setString(2, p.getEnunciado());
                ps.setString(3, p.getRespuestas(0));
                ps.setString(4, p.getRespuestas(1));
                ps.setString(5, p.getRespuestas(2));
                ps.setString(6, p.getRespuestas(3));
                ps.setInt(7, p.getCorrecta());
                ps.setString(8, p.getTipo());
                ps.setString(9, p.getNivelBloom());
                ps.setInt(10, p.getTiempoEstimado());
                ps.setString(11, asignatura);
                ps.executeUpdate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al insertar: " + ex.getMessage());
            }
        }
    }

    // 3. Mostrar preguntas en la tabla tbLista
    public void mostrarPreguntasEnTabla(List<Pregunta> preguntas, JTable tbLista, String asignatura) {
        DefaultTableModel model = (DefaultTableModel) tbLista.getModel();
        model.setRowCount(0); // Limpia la tabla

        for (Pregunta p : preguntas) {
            model.addRow(new Object[]{
                p.getEnunciado(),
                p.getTipo(),
                p.getNivelBloom(),
                p.getTiempoEstimado(),
                asignatura
            });
        }
    }

    // 4. Método principal que hace todo (opcional)
    public void agregarPreguntasAExamen(String tipo, String nivel, int cantidad, String asignatura, JTable tbLista) {
        List<Pregunta> preguntas = obtenerPreguntasFiltradas(tipo, nivel, cantidad);
        insertarPreguntasEnExamen(preguntas, asignatura);
        mostrarPreguntasEnTabla(preguntas, tbLista, asignatura);
    }
    
    public void iniciarExamen(){
        
    }
    
    public void salir(){
        
    }
}
