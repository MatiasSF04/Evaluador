/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import frontend.Evaluacion;
import frontend.Principal;
import java.awt.Color;

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
    boolean revision = false;
    
    
    public static Map<String, Integer> asignaturas = Map.of(
        "1. Matemáticas", 1,
        "2. Programación", 2,
        "3. Bases de Datos", 3,
        "4. Sistemas Operativos y Redes", 4
    );
    
    public static String obtenerNombreAsignatura(int codigo) {
    for (Map.Entry<String, Integer> entry : asignaturas.entrySet()) {
        if (entry.getValue() == codigo) {
            return entry.getKey();
        }
    }
    return "Desconocida";
    }
    
    public static int obtenerCodigoAsignatura(String nombre) {
        return asignaturas.getOrDefault(nombre, -1);
    }
    
    public Map<String, String> tipos = Map.of(
        "Selección Múltiple", "selMultiple",
        "Verdadero o Falso", "verOfal"
    );
    
    public String obtenerCodigoTipo(String tipoUI) {
        return tipos.getOrDefault(tipoUI, "null");
    }
    
    public Map<String, Integer> niveles = Map.of(
        "1. Recordar", 1,
        "2. Comprender", 2,
        "3. Aplicar", 3,
        "4. Analizar", 4,
        "5. Evaluar", 5,
        "6. Crear", 6
    );
    
    public int getNivel(String nivelBloomUI) {
        return niveles.getOrDefault(nivelBloomUI, -1);
    }
    
    public String obtenerNombreNivel(int codigo) {
        for (Map.Entry<String, Integer> entry : niveles.entrySet()) {
            if (entry.getValue() == codigo) {
                return entry.getKey();
            }
        }
        return "Desconocido";
    }
    
    
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
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Random rd = new Random();
        
        //Pantalla Crear
        if (source == main.btnAddPreguntas) {
            String asignatura = main.cbAsignatura.getSelectedItem().toString();
            if ("Null".equals(asignatura)) {
                List<String> clavesAsig = new ArrayList<>(asignaturas.keySet());
                String claveAleatoria = clavesAsig.get(rd.nextInt(clavesAsig.size()));
                codigoAsignatura = asignaturas.get(claveAleatoria);
            } else {
                codigoAsignatura = asignaturas.get(asignatura);
            }
            System.out.println("Asignatura Seleccionada: "+codigoAsignatura);
            
            String tipo = main.cbTipo.getSelectedItem().toString();
            if ("Null".equals(tipo)) {
                List<String> clavesTipo = new ArrayList<>(tipos.keySet());
                String claveAleatoria = clavesTipo.get(rd.nextInt(clavesTipo.size()));
                codigoTipo = tipos.get(claveAleatoria);
            } else {
                codigoTipo = obtenerCodigoTipo(tipo);
            }
            System.out.println("Tipo Seleccionado: "+codigoTipo);
            
            String nivel = main.cbBloom.getSelectedItem().toString();
            if ("Null".equals(nivel)) {
                List<String> clavesNivel = new ArrayList<>(niveles.keySet());
                String claveAleatoria = clavesNivel.get(rd.nextInt(clavesNivel.size()));
                codigoNivel = niveles.get(claveAleatoria);
            } else {
                codigoNivel = getNivel(nivel);
            }
            System.out.println("Nivel Seleccionado: "+codigoNivel);
            
            codigoCantidad = (Integer) main.cbCantidad.getValue();
            System.out.println("Cantidad Seleccionada: "+codigoCantidad+"\n");
            
            cargarPreguntasDesdeBD();
            LimpiarPrincipal();
            
        } else if (source == main.btnAbrirEvaluacion) {
            vista.setVisible(true);
            main.setVisible(false);
            
        } else if (source == main.btnSalir) {
            con.CerrarConexion();
            System.out.println("Botón salir presionado.");
            System.exit(0);
        }
        
        //Pantalla Preguntas
        if (source == vista.btnIniciar && vista.btnIniciar.getText().equals("Iniciar")) {
            iniciarEvaluacion();
        } else if (source == vista.btnIniciar && vista.btnIniciar.getText().equals("Salir")) {
            vista.setVisible(false);
            main.setVisible(true);
        } else if (source == vista.btnSiguiente && vista.btnSiguiente.getText().equals("Evaluar")) {
            revision = true;
            guardarRespuestaSeleccionada();
            mostrarPregunta(indice);
            evaluarRespuestas();
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
                vista.btnSiguiente.setEnabled(false);
                vista.btnReiniciar.setEnabled(true);
            }
        } else if (source == vista.btnAnterior) {
            guardarRespuestaSeleccionada();
            if (indice > 0) {
                indice--;
                mostrarPregunta(indice);
            }
        } else if (source == vista.btnReiniciar) {
            revision = false;
            reiniciarEvaluacion();
        }
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
        
        vista.rdBtnOpcion1.setOpaque(true);
        vista.rdBtnOpcion2.setOpaque(true);
        vista.rdBtnOpcion3.setOpaque(true);
        vista.rdBtnOpcion4.setOpaque(true);
        
        vista.rdBtnOpcion1.setBackground(UIManager.getColor("RadioButton.background"));
        vista.rdBtnOpcion2.setBackground(UIManager.getColor("RadioButton.background"));
        vista.rdBtnOpcion3.setBackground(UIManager.getColor("RadioButton.background"));
        vista.rdBtnOpcion4.setBackground(UIManager.getColor("RadioButton.background"));
        
        if (respuestasUsuario.containsKey(p.getId())) {
            int seleccion = respuestasUsuario.get(p.getId());
            switch (seleccion) {
                case 0: vista.rdBtnOpcion1.setSelected(true); break;
                case 1: vista.rdBtnOpcion2.setSelected(true); break;
                case 2: vista.rdBtnOpcion3.setSelected(true); break;
                case 3: vista.rdBtnOpcion4.setSelected(true); break;
            }

            if (revision) {
                vista.rdBtnOpcion1.setEnabled(false);
                vista.rdBtnOpcion2.setEnabled(false);
                vista.rdBtnOpcion3.setEnabled(false);
                vista.rdBtnOpcion4.setEnabled(false);
                if (seleccion != p.getCorrecta()){
                    switch (seleccion) {
                        case 0: vista.rdBtnOpcion1.setBackground(Color.red);break;
                        case 1: vista.rdBtnOpcion2.setBackground(Color.red); break;
                        case 2: vista.rdBtnOpcion3.setBackground(Color.red); break;
                        case 3: vista.rdBtnOpcion4.setBackground(Color.red); break;
                    }
                }
                
                int respuestaCorrecta = p.getCorrecta();
                switch (respuestaCorrecta) {
                        case 0: vista.rdBtnOpcion1.setBackground(Color.green);break;
                        case 1: vista.rdBtnOpcion2.setBackground(Color.green); break;
                        case 2: vista.rdBtnOpcion3.setBackground(Color.green); break;
                        case 3: vista.rdBtnOpcion4.setBackground(Color.green); break;
                    }
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
            
            DefaultTableModel modelo = (DefaultTableModel) main.tbLista.getModel();
            if (modelo.getColumnCount() == 0) {
                modelo.addColumn("Enunciado");
                modelo.addColumn("Tipo");
                modelo.addColumn("Nivel");
                modelo.addColumn("Tiempo");
                modelo.addColumn("Asignatura");
            } else {
                modelo.setRowCount(0);
            }
            
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
                
                Pregunta pregunta = new Pregunta(
                    rs.getInt("Id_Pregunta"),
                    enunciado,
                    rs.getString("Respuesta_1"),
                    rs.getString("Respuesta_2"),
                    rs.getString("Respuesta_3"),
                    rs.getString("Respuesta_4"),
                    rs.getInt("Respuesta_Correcta"),
                    tipo,
                    rs.getString("Nivel"),
                    tiempo,
                    asignaturaCodigo
                );
                preguntas.add(pregunta);
            }
            System.out.println("Preguntas encontradas: "+contador);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar preguntas: " + ex.getMessage());
        }
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
    
    private void reiniciarEvaluacion() {
        vista.grupoRespuestas.clearSelection();
        iniciarEvaluacion();
        respuestasUsuario.clear();
        vista.btnSiguiente.setEnabled(true);
        vista.btnAnterior.setEnabled(false);
        vista.btnReiniciar.setEnabled(false);
        vista.rdBtnOpcion1.setEnabled(true);
        vista.rdBtnOpcion2.setEnabled(true);
        vista.rdBtnOpcion3.setEnabled(true);
        vista.rdBtnOpcion4.setEnabled(true);
    }
    
    private void iniciarEvaluacion() {
        this.respuestasUsuario.clear();
        this.indice = -1;
        vista.AreaPregunta.setText("");
        vista.rdBtnOpcion1.setText("Opcion 1");
        vista.rdBtnOpcion2.setText("Opcion 2");
        vista.rdBtnOpcion3.setText("Opcion 3");
        vista.rdBtnOpcion4.setText("Opcion 4");
        mostrarInfo();
        vista.btnIniciar.setEnabled(false);
        vista.btnAnterior.setEnabled(false);
        vista.btnReiniciar.setEnabled(false);
        vista.btnSiguiente.setEnabled(true);
        vista.btnSiguiente.setText("Pregunta Siguiente");
    }
    
    private void mostrarInfo() {
        int totalPreguntas = preguntas.size();
        int tiempoEstimado = preguntas.stream().mapToInt(Pregunta::getTiempoEstimado).sum();
        vista.txtInfo.setText("==================\n  INFORMACIÓN DEL EXAMEN\n==================\n\nCantidad de Preguntas: " + totalPreguntas +
                "\nTiempo Estimado: " + tiempoEstimado + " mins");
    }
    
    private void evaluarRespuestas() {
        int total = 0;
        int correctas = 0;

        // Inicializar contadores con tus claves
        Map<String, int[]> resumenAsignaturas = new LinkedHashMap<>();
        for (String nombre : asignaturas.keySet()) {
            resumenAsignaturas.put(nombre, new int[]{0, 0});
        }

        Map<String, int[]> resumenTipos = new LinkedHashMap<>();
        for (String nombre : tipos.keySet()) {
            resumenTipos.put(nombre, new int[]{0, 0});
        }

        Map<String, int[]> resumenNiveles = new LinkedHashMap<>();
        for (String nombre : niveles.keySet()) {
            resumenNiveles.put(nombre, new int[]{0, 0});
        }

        // Recorrer preguntas respondidas
        for (int i = 0; i < preguntas.size(); i++) {
            Integer respuestaUsuario = respuestasUsuario.get(i);
            if (respuestaUsuario != null) {
                Pregunta p = preguntas.get(i);
                total++;
                boolean esCorrecta = respuestaUsuario == p.getCorrecta();
                System.out.println(esCorrecta);
                if (esCorrecta) correctas++;

                // Asignatura
                String nombreAsignatura = obtenerNombreAsignatura(p.getCodigoAsignatura());
                int[] datosAsignatura = resumenAsignaturas.get(nombreAsignatura);
                if (datosAsignatura != null) {
                    datosAsignatura[1]++;
                    if (esCorrecta) datosAsignatura[0]++;
                }

                // Tipo
                String tipoUI = tipos.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(p.getTipo()))
                        .map(Map.Entry::getKey)
                        .findFirst().orElse("Desconocido");
                int[] datosTipo = resumenTipos.get(tipoUI);
                if (datosTipo != null) {
                    datosTipo[1]++;
                    if (esCorrecta) datosTipo[0]++;
                }

                // Nivel
                String nombreNivel = obtenerNombreNivel(getNivel(p.getNivelBloom()));
                int[] datosNivel = resumenNiveles.get(nombreNivel);
                if (datosNivel != null) {
                    datosNivel[1]++;
                    if (esCorrecta) datosNivel[0]++;
                }
            }
        }

        // Mostrar resumen
        StringBuilder sb = new StringBuilder();
        sb.append("==========================\nResumen de Respuestas Correctas\n==========================\n\n");
        sb.append("Total preguntas: ").append(formatoPorcentaje(correctas, total)).append("\n\n");

        sb.append("Total por Asignatura:\n");
        for (Map.Entry<String, int[]> entry : resumenAsignaturas.entrySet()) {
            sb.append("- ").append(entry.getKey()).append(": ")
              .append(formatoPorcentaje(entry.getValue()[0], entry.getValue()[1])).append("\n");
        }

        sb.append("\nTotal por tipo:\n");
        for (Map.Entry<String, int[]> entry : resumenTipos.entrySet()) {
            sb.append("- ").append(entry.getKey()).append(": ")
              .append(formatoPorcentaje(entry.getValue()[0], entry.getValue()[1])).append("\n");
        }

        sb.append("\nTotal por Nivel:\n");
        for (Map.Entry<String, int[]> entry : resumenNiveles.entrySet()) {
            sb.append("- ").append(entry.getKey()).append(": ")
              .append(formatoPorcentaje(entry.getValue()[0], entry.getValue()[1])).append("\n");
        }

        vista.txtInfo.setText(sb.toString());
    }
    
    private String formatoPorcentaje(int correctas, int total) {
        if (total == 0) return "N/A";
        double porcentaje = ((double) correctas / total) * 100;
        return String.format("%d/%d (%.1f%%)", correctas, total, porcentaje);
    }

}