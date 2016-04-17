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
    
    public void rotate(float anglex, float angley, float anglez){
        this.ship_obj.addRotation(anglex, angley, anglez);
        this.update();
    }
}

