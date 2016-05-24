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
public class Planet extends ObjectContainer{

    public  Planet(){
        super("./data/planet/planet2.obj");
        this.obj.setPosition(1.5f, 0.0f, 2.0f);
    }
    
    public void changePosition(float x, float y, float z){
        this.obj.setPosition(x, y, z);
    }
}