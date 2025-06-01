/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

//import Modelo.Pregunta;
import frontend.Principal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlVista{
    Principal vista;
    
    public ControlVista(Principal vista) {
        this.vista = vista;
        
        this.vista.btnAddPreguntas.addActionListener((ActionListener) this);
        //this.vista.btnListado.addActionListener((ActionListener) this);
        this.vista.btnSalir.addActionListener((ActionListener) this);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == vista.btnAddPreguntas) {
            crearExamen();
        }// else if (source == vista.btnListado) {
          //  mostrarListadoTotal();} 
        else if (source == vista.btnSalir) {
            System.exit(0);
        }
    }
    
    public void crearExamen(){
        System.out.println("1");
    }
    
    public void mostrarListadoTotal(){
        System.out.println("2");
        
    }
}
