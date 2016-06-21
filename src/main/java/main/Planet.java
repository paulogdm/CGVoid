/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import com.sun.javafx.geom.Vec3f;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 *
 * @author paulogdm
 */

public class Planet extends ObjectContainer{

    public  Planet(String planet){
        super(planet);
        this.obj.setPosition(1.5f, 0.0f, 2.0f);
    }
    
    public void changePosition(float x, float y, float z){
        this.obj.setPosition(x, y, z);
    }
}
