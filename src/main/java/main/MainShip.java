/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import java.io.File;
import static main.SimpleObject.KEEP_VALUE;


public class MainShip extends ObjectContainer{
    
    private Missile missile;
    boolean missile_is_inside;
    
    public MainShip() {
        super("./data/feisar/Feisar_Ship.obj");
        this.getObj().setSize(0.25f, 0.25f, 0.25f);
        
        missile = new Missile();
        missile_is_inside = true;
    }
    
    public SimpleObject getMissileObj(){
        return this.missile.getObj();            
    }
    
    public void rotate(float anglex, float angley, float anglez){
        this.obj.addRotation(anglex, angley, anglez);
        this.update();
    }
    
    
    public void shoot(){
        float position[] = this.missile.getObj().getPosition();
        
        if(position[2] > 5f){
            this.missile.getObj().setPosition(this.getObj().getX(),
                    this.getObj().getY(),
                    this.getObj().getZ()+0.3f);
            this.missile.getObj().setSpeed(0f, 0f, 0f);
            this.missile_is_inside = true;
        } else if (!missile_is_inside) {
            this.missile.getObj().setSpeed(KEEP_VALUE, KEEP_VALUE, 0.1f);
        }
        
        this.missile.getObj().updatePosition();
    }
    
    public boolean getMissileFlag(){
        return this.missile_is_inside;
    }
    
    public void toogleMissileFlag(){
        missile_is_inside = !missile_is_inside;
    }
}

