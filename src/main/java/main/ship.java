/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import java.io.File;

/**
 *
 * @author paulogdm
 */
public class ship {

    private final simpleObject ship_obj;
    
    public  ship(){
        this.ship_obj = new simpleObject(new JWavefrontObject(new File("./data/feisar/Feisar_Ship.obj")), new float[] {0.25f, 0.25f, 0.25f}, 
                new float[] {270f, 0f, 0f},
                new float[] {0f, 0f, 0f}, 
                new float[] {0f, 0f, 0f},
                0f);
    }
    
    /**
     *
     * @return 
     */
    public simpleObject getObj(){
        return this.ship_obj;
    }
    
    public void update(){
        this.ship_obj.updatePosition();
    }
    
    public void firstLand(int start_time, int minutes, int seconds){
        this.ship_obj.addSpeed(simpleObject.KEEP_VALUE, -0.003f, simpleObject.KEEP_VALUE);
//        this.ship_obj.addPosition(simpleObject.KEEP_VALUE, 0.01f, simpleObject.KEEP_VALUE);
        this.ship_obj.addRotation(8.2f, 0f, 0f);
        
//this.ship_obj.setPosition(simpleObject.KEEP_VALUE, 0.01f, simpleObject.KEEP_VALUE);
                /*if (this.ship_obj.getPosition()[1] > -2.0f) {    //Aplica gravidade
                    this.ship_obj.addSpeed(0f, -0.003f, 0f);
                }
                else {
                    this.ship_obj.setSpeed(simpleObject.KEEP_VALUE, 0f, simpleObject.KEEP_VALUE);
                    this.ship_obj.setPosition(simpleObject.KEEP_VALUE, 0f, simpleObject.KEEP_VALUE);
                }
                
                if (this.ship_obj.getPosition()[1] > 1f) {    //Faz rodar o this.ship_obj
                    this.ship_obj.addRotation(8.2f, 0f, 0f);
                }
                else {
                    this.ship_obj.setRotation(0f, simpleObject.KEEP_VALUE, simpleObject.KEEP_VALUE);                   
                }
                
                if (seconds == start_time + 4 && minutes == 0) {
                }*/
    }
}


