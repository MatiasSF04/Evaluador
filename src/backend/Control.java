/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import frontend.Evaluacion;
import frontend.Principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Control implements ActionListener{
    Principal main;
    Evaluacion vista;
    List<Pregunta> preguntas;
    Map<Integer, Integer> respuestasUsuario;
    int indice = 0;
    int codigoAsignatura;
    String codigoTipo;
    int codigoNivel;
    int codigoCantidad;
    
    
    public Map<String, Integer> asignaturas = Map.of(
        "1. Matemáticas", 1,
        "2. Programación", 2,
        "3. Bases de Datos", 3,
        "4. Sistemas Operativos y Redes", 4
    );
    
    public Map<String, String> tipos = Map.of(
        "Selección Múltiple", "selMultiple",
        "Verdadero o Falso", "verOfal"
    );
    
    public Map<String, Integer> niveles = Map.of(
        "1. Recordar", 1,
        "2. Comprender", 2,
        "3. Aplicar", 3,
        "4. Analizar", 4,
        "5. Evaluar", 5,
        "6. Crear", 6
    );
    
    Conexion con = new Conexion();
    Connection cn = con.EnlaceSQL();
    
    public Control(Evaluacion vista, Principal main) {
        this.vista = vista;
        this.main = main;
        this.preguntas = new ArrayList<>();
        this.respuestasUsuario = new HashMap<>();
        this.indice = 0;
        
        this.vista.btnIniciar.addActionListener(this);
        this.vista.btnSiguiente.addActionListener(this);
        this.vista.btnAnterior.addActionListener(this);
        this.vista.btnReiniciar.addActionListener(this);
        
        this.main.btnAddPreguntas.addActionListener(this);
        this.main.btnAbrirEvaluacion.addActionListener(this);
        this.main.btnSalir.addActionListener(this);
        
        this.vista.btnAnterior.setEnabled(false);
        this.vista.btnReiniciar.setEnabled(false);
        this.vista.btnSiguiente.setEnabled(false);
        
        //cargarPreguntasDesdeBD();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        //Pantalla Crear
        if (source == main.btnAddPreguntas) {
            String asignatura = main.cbAsignatura.getSelectedItem().toString();
            codigoAsignatura = obtenerCodigoAsignatura(asignatura);
            System.out.println(codigoAsignatura);
            
            String tipo = main.cbTipo.getSelectedItem().toString();
            codigoTipo = obtenerCodigoTipo(tipo);
            System.out.println(codigoTipo);
            
            String nivel = main.cbBloom.getSelectedItem().toString();
            codigoNivel = obtenerCodigoNivel(nivel);
            System.out.println(codigoNivel);
            
            codigoCantidad = (Integer) main.cbCantidad.getValue();
            System.out.println(codigoCantidad);
            
            cargarPreguntasDesdeBD();

            LimpiarPrincipal();
        } else if (source == main.btnAbrirEvaluacion) {
            vista.setVisible(true);
            main.setVisible(false);
        } else if (source == main.btnSalir) {
            System.out.println("Botón salir presionado.");
            System.exit(0);
        }
        
        //Pantalla Preguntas
        if (source == vista.btnIniciar && vista.btnIniciar.getText().equals("Iniciar")) {
            iniciarEvaluacion();
        } else if (source == vista.btnIniciar && vista.btnIniciar.getText().equals("Salir")) {
            vista.setVisible(false);
            main.setVisible(true);
        } else if (source == vista.btnSiguiente) {
            vista.rdBtnOpcion1.setEnabled(true);
            vista.rdBtnOpcion2.setEnabled(true);
            vista.rdBtnOpcion3.setEnabled(true);
            vista.rdBtnOpcion4.setEnabled(true);
            guardarRespuestaSeleccionada();
            if (indice < preguntas.size() - 1) {
                indice++;
                mostrarPregunta(indice);
            } else {
                //evaluarRespuestas();
                vista.btnSiguiente.setEnabled(false);
                vista.btnReiniciar.setEnabled(true);
            }
        } else if (source == vista.btnAnterior) {
            guardarRespuestaSeleccionada();
            if (indice > 0) {
                indice--;
                mostrarPregunta(indice);
            }
        } else if (source == vista.btnSiguiente && vista.btnSiguiente.getText().equals("Evaluar")) {
            guardarRespuestaSeleccionada();
            //evaluarRespuestas();
        } else if (source == vista.btnReiniciar) {
            reiniciarEvaluacion();
            mostrarPregunta(indice);
        }
    }
    
    public int obtenerCodigoAsignatura(String nombre) {
        return asignaturas.getOrDefault(nombre, -1); // Devuelve -1 si el nombre de la asignatura no existe
    }
    public String obtenerCodigoTipo(String tipoUI) {
        return tipos.getOrDefault(tipoUI, "null");
    }
    public int obtenerCodigoNivel(String nivelBloomUI) {
        return niveles.getOrDefault(nivelBloomUI, -1);
    }
    
    private void LimpiarPrincipal() {
        main.cbAsignatura.setSelectedIndex(0);
        main.cbBloom.setSelectedIndex(0);
        main.cbTipo.setSelectedIndex(0);
        main.cbCantidad.setValue(0);
    }
    
    private void mostrarPregunta(int index) {
        Pregunta p = preguntas.get(index);

        vista.AreaPregunta.setText(p.getEnunciado());
        
        vista.rdBtnOpcion1.setText("<html><div style='width:200px;'>" + p.getRespuestas(0) + "</div></html>");
        vista.rdBtnOpcion2.setText("<html><div style='width:200px;'>" + p.getRespuestas(1) + "</div></html>");
        vista.rdBtnOpcion3.setText("<html><div style='width:200px;'>" + p.getRespuestas(2) + "</div></html>");
        vista.rdBtnOpcion4.setText("<html><div style='width:200px;'>" + p.getRespuestas(3) + "</div></html>");

        mostrarOpcionesVisibles(p);
        vista.grupoRespuestas.clearSelection();

        if (respuestasUsuario.containsKey(p.getId())) {
            int seleccion = respuestasUsuario.get(p.getId());
            switch (seleccion) {
                case 0: vista.rdBtnOpcion1.setSelected(true); break;
                case 1: vista.rdBtnOpcion2.setSelected(true); break;
                case 2: vista.rdBtnOpcion3.setSelected(true); break;
                case 3: vista.rdBtnOpcion4.setSelected(true); break;
            }
        }

        vista.btnAnterior.setEnabled(index > 0);
        if (index == preguntas.size() - 1) {
            vista.btnSiguiente.setText("Evaluar");
            vista.btnIniciar.setText("Salir");
            vista.btnIniciar.setEnabled(true);
        } else {
            vista.btnSiguiente.setText("Pregunta Siguiente");
            vista.btnIniciar.setText("Iniciar");
        }
    }

    private void cargarPreguntasDesdeBD() {
        try {
            String sql = "SELECT * FROM Bloom.preguntas WHERE Asignatura = ? && Tipo = ? && Nivel = ? ORDER BY RAND() LIMIT ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            
            ps.setInt(1, codigoAsignatura);
            ps.setString(2, codigoTipo);
            ps.setInt(3, codigoNivel);
            ps.setInt(4, codigoCantidad);
            ResultSet rs = ps.executeQuery();
            
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Enunciado");
            modelo.addColumn("Tipo");
            modelo.addColumn("Nivel");
            modelo.addColumn("Tiempo");
            modelo.addColumn("Asignatura");
            
            int contador = 0;
            while (rs.next()) {
                contador++;
                String enunciado = rs.getString("Enunciado");
                String tipo = rs.getString("Tipo");
                String nivel = rs.getString("Nivel");
                int tiempo = rs.getInt("Tiempo");
                int asignaturaCodigo = rs.getInt("Asignatura");

                String asignaturaNombre = obtenerNombreAsignatura(asignaturaCodigo);

                modelo.addRow(new Object[]{enunciado, tipo, nivel, tiempo, asignaturaNombre});
            }
            System.out.println("Preguntas encontradas: "+contador);
            
            main.tbLista.setModel(modelo);
            con.CerrarConexion();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar preguntas: " + ex.getMessage());
        }
    } 
    
    public String obtenerNombreAsignatura(int codigo) {
    for (Map.Entry<String, Integer> entry : asignaturas.entrySet()) {
        if (entry.getValue() == codigo) {
            return entry.getKey();
        }
    }
    return "Desconocida";
    }
   
    private void mostrarOpcionesVisibles(Pregunta p) {
        vista.rdBtnOpcion1.setVisible(p.getRespuestas(0) != null);
        vista.rdBtnOpcion2.setVisible(p.getRespuestas(1) != null);
        vista.rdBtnOpcion3.setVisible(p.getRespuestas(2) != null);
        vista.rdBtnOpcion4.setVisible(p.getRespuestas(3) != null);
    }
    
    private void guardarRespuestaSeleccionada() {
        int seleccion = -1;
        if (vista.rdBtnOpcion1.isSelected()) seleccion = 0;
        else if (vista.rdBtnOpcion2.isSelected()) seleccion = 1;
        else if (vista.rdBtnOpcion3.isSelected()) seleccion = 2;
        else if (vista.rdBtnOpcion4.isSelected()) seleccion = 3;

        if (seleccion >= 0) {
            respuestasUsuario.put(preguntas.get(indice).getId(), seleccion);
        }
    }
    
    //POR ARREGLAR
    private void evaluarRespuestas() {
        int total = preguntas.size();
        int correctas = 0;

        // Mapas para contar correctas y totales por categoría
        Map<String, Integer> correctasPorNivel = new HashMap<>();
        Map<String, Integer> totalPorNivel = new HashMap<>();

        Map<String, Integer> correctasPorTipo = new HashMap<>();
        Map<String, Integer> totalPorTipo = new HashMap<>();

        for (int i = 0; i < preguntas.size(); i++) {
            Pregunta p = preguntas.get(i);
            Integer seleccion = respuestasUsuario.get(i);

            // Conteo general
            if (seleccion != null && p.getCorrecta() == seleccion) {
                correctas++;
                correctasPorNivel.put(p.getNivelBloom(), correctasPorNivel.getOrDefault(p.getNivelBloom(), 0) + 1);
                correctasPorTipo.put(p.getTipo(), correctasPorTipo.getOrDefault(p.getTipo(), 0) + 1);
            }

            // Conteo total por categoría
            totalPorNivel.put(p.getNivelBloom(), totalPorNivel.getOrDefault(p.getNivelBloom(), 0) + 1);
            totalPorTipo.put(p.getTipo(), totalPorTipo.getOrDefault(p.getTipo(), 0) + 1);
        }

        // Construir mensaje final
        StringBuilder resultado = new StringBuilder();
        resultado.append("Respuestas Correctas:\n");
        if (total == 0) {
            System.out.println("No hay preguntas para evaluar.");
            return;
        }
        resultado.append("Total = ").append((correctas * 100 / total)).append("%\n\n");

        resultado.append("- Según Taxonomía:\n");
        for (String nivel : totalPorNivel.keySet()) {
            int totalNivel = totalPorNivel.get(nivel);
            int correctNivel = correctasPorNivel.getOrDefault(nivel, 0);
            resultado.append("  ").append(nivel).append(" = ").append((correctNivel * 100 / totalNivel)).append("%\n");
        }

        resultado.append("\n- Según Tipo:\n");
        for (String tipo : totalPorTipo.keySet()) {
            int totalTipo = totalPorTipo.get(tipo);
            int correctTipo = correctasPorTipo.getOrDefault(tipo, 0);
            resultado.append("  ").append(tipo).append(" = ").append((correctTipo * 100 / totalTipo)).append("%\n");
        }

        vista.mostrarMensaje(resultado.toString());

        // Habilitar botón Reiniciar, deshabilitar otros
        vista.btnReiniciar.setEnabled(true);
        vista.btnAnterior.setEnabled(false);
        vista.btnReiniciar.setEnabled(true);
    }
    
    private void reiniciarEvaluacion() {
        vista.grupoRespuestas.clearSelection();
        iniciarEvaluacion();
    }
    
    private void iniciarEvaluacion() {
        this.respuestasUsuario.clear();
        this.indice = -1;
        vista.AreaPregunta.setText("");
        vista.rdBtnOpcion1.setText("Opcion 1");
        vista.rdBtnOpcion2.setText("Opcion 2");
        vista.rdBtnOpcion3.setText("Opcion 3");
        vista.rdBtnOpcion4.setText("Opcion 4");
        //vista.txtInfo.setText("");
        mostrarInfo();
        vista.btnIniciar.setEnabled(false);
        vista.btnSiguiente.setEnabled(true);
        vista.btnAnterior.setEnabled(false);
        vista.btnReiniciar.setEnabled(false);
        vista.btnSiguiente.setText("Pregunta Siguiente");
    }
    
    private void mostrarInfo() {
        int totalPreguntas = preguntas.size();
        int tiempoEstimado = preguntas.stream().mapToInt(Pregunta::getTiempoEstimado).sum();
        vista.txtInfo.setText("==================\n  INFORMACIÓN DEL EXAMEN\n==================\n\nCantidad de Preguntas: " + totalPreguntas +
                "\nTiempo Estimado: " + tiempoEstimado + " mins");
    }
}