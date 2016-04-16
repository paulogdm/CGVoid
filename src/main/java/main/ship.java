/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import java.io.File;


public class ship {

    private final simpleObject ship_obj;
    
    public ship(){
        this.ship_obj = new simpleObject(new JWavefrontObject(new File("./data/feisar/Feisar_Ship.obj")), new float[] {0.25f, 0.25f, 0.25f}, 
                new float[] {1f, 1f, 1f},
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
    
    public void firstLand(){
//        this.ship_obj.addSpeed(simpleObject.KEEP_VALUE, -0.003f, simpleObject.KEEP_VALUE);
//        this.ship_obj.addPosition(simpleObject.KEEP_VALUE, 0.01f, simpleObject.KEEP_VALUE);;
        this.ship_obj.addRotation(8.0f, 0f, 0f);
        this.update();
    }
    
    /**
     *
     * @param camera
     */

    public void focusOnMe(Matrix4 camera){
        camera.loadIdentity();
        camera.lookAt(
                //this.ship_obj.getX(), this.ship_obj.getY(), this.ship_obj.getZ(),
                1, 1, 1,
                0, 0, 0, 
                0, 1, 0);
        camera.bind();
    }
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


