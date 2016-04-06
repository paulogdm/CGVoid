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
public class planet {

    private final simpleObject planet_obj;
    
    public  planet(){
        this.planet_obj = new simpleObject(new JWavefrontObject(new File("./data/planet2.obj")), 
                new float[] {1f, 1f, 1f}, 
                new float[] {0f, 0f, 0f},
                new float[] {0f, -2.0f, 0f}, 
                new float[] {0f, 0f, 0f},
                0f);
    }
    
    /**
     *
     * @return 
     */
    public simpleObject getObj(){
        return this.planet_obj;
    }
    
    public void update(){
        this.planet_obj.updatePosition();
    }
}