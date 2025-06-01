/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

public class Lista {
    private String enunciado;
    private String tipo;
    private String nivel;
    private int tiempo;
    private String asignatura;

    public Lista(String enunciado, String tipo, String nivel, int tiempo, String asignatura) {
        this.enunciado = enunciado;
        this.tipo = tipo;
        this.nivel = nivel;
        this.tiempo = tiempo;
        this.asignatura = asignatura;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNivel() {
        return nivel;
    }

    public int getTiempo() {
        return tiempo;
    }

    public String getAsignatura() {
        return asignatura;
    }
    
    
}
