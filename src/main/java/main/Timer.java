/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author xima
 */
public class Timer {
    private long lastFrame;
    private long firstFrame;
    
    public Timer(){
        lastFrame = 0;
        firstFrame = this.getTime();
    }
    
    private long getTime(){
        return System.currentTimeMillis();
    }
    
    public int getDelta(){
        long current_time = this.getTime();
        int delta = (int) (current_time - this.firstFrame);
        this.lastFrame = this.getTime();
        return delta;
    }
    
    
}
