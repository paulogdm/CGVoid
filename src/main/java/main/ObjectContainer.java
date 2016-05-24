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
public class ObjectContainer {
    
    protected SimpleObject obj;
    
    public ObjectContainer(String file){
        this.obj = new SimpleObject(new JWavefrontObject(new File(file)), 
        new float[] {1f, 1f, 1f}, 
        new float[] {0f, 0f, 0f},
        new float[] {0f, 0f, 0f}, 
        new float[] {0f, 0f, 0f},
        0f);
    }
    
    /**
     *
     * @return 
     */
    public SimpleObject getObj(){
        return this.obj;
    }
    
    public void update(){
        this.obj.updatePosition();
    }
}
