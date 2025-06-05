/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

public class Pregunta {
    private int id;
    private String enunciado;
    private String[] respuestas;
    private int correcta;
    private String tipo;
    private String nivelBloom;
    private int tiempoEstimado;
    private int asignaturas;

    public Pregunta(int id, String enunciado, String r1, String r2, String r3, String r4, int correcta, String tipo, String nivelBloom, int tiempoEstimado, int asignaturas) {
        this.id = id;
        this.enunciado = enunciado;
        this.respuestas = new String[]{r1, r2, r3, r4};
        this.correcta = correcta;
        this.tipo = tipo;
        this.nivelBloom = nivelBloom;
        this.tiempoEstimado = tiempoEstimado;
        this.asignaturas = asignaturas;
    }
    
    public Pregunta() {
    }

    public int getId() {
        return id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getRespuestas(int i) {
        return (respuestas != null && i >= 0 && i < respuestas.length) ? respuestas[i] : null;
    }

    public int getCorrecta() {
        return correcta;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNivelBloom() {
        return nivelBloom;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }
}