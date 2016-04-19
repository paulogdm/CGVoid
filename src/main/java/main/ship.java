/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import java.io.File;


public class ship extends objectContainer{

    public ship() {
        super("./data/feisar/Feisar_Ship.obj");
        this.getObj().setSize(0.25f, 0.25f, 0.25f);
    }
    
    public void rotate(float anglex, float angley, float anglez){
        this.obj.addRotation(anglex, angley, anglez);
        this.update();
    }
}

