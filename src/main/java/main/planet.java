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
public class planet extends objectContainer{

    public  planet(){
        super("./data/planet2.obj");
        this.getObj().setPosition(0f, -2.0f, 0f);
    }
}