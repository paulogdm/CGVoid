/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author xima
 * Cria o tempo para que seja gerenciado o filminho aparesentado
 */
public class Timer {
    private long lastFrame;//ultimo frame desenhado(o tempo dele)
    private long firstFrame;//primeiro frame depois que tudo ja foi inicializado
    
    public Timer(){
        lastFrame = 0;
    }
    
    public void init(){
        firstFrame  = this.getTime();
    }
    //pega o tempo atual
    private long getTime(){
        return System.currentTimeMillis();
    }
    //gera o tempo que comecou ate o momento
    public int getDelta(){
        long current_time = this.getTime();
        int delta = (int) (current_time - this.firstFrame);
        this.lastFrame = this.getTime();
        return delta;
    }
    
    
}
