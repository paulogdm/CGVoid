/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import br.usp.icmc.vicg.gl.core.Material;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import java.util.Vector;

/**
 *
 * @author xima
 */
public class Asteroid extends ObjectContainer{
    private Vector position;
    private Vector velocity;
    private int expireTime;
    
    public Asteroid(String file) {
        super(file);
        this.position = new Vector();
        this.velocity = new Vector();
        this.expireTime = 0;
    }

    /**
     *
     * @param particleLocation
     * @param particleVelocity
     * @param particleLifeTime
     */
    public Asteroid(Vector particleLocation, Vector particleVelocity, int particleLifeTime, String file) {
        super(file);
        //System.out.println("init particleLocation: "+particleLocation.size());
        //System.out.println(file);
        this.position = new Vector(3);
        this.position.add((float)particleLocation.get(0)); this.position.add((float)particleLocation.get(1));  this.position.add((float)particleLocation.get(2)); 
        //this.position.set(0, (float)particleLocation.get(0));this.position.set(1, (float)particleLocation.get(1)); this.position.set(2, (float)particleLocation.get(2));
        //this.position = position;
        this.velocity =new Vector(3);
        //this.velocity.set(0,(float) particleVelocity.get(0)); this.velocity.set(1,(float) particleVelocity.get(1)); this.velocity.set(2,(float) particleVelocity.get(2));
        this.velocity.add((float) particleVelocity.get(0)); this.velocity.add((float) particleVelocity.get(1)); this.velocity.add((float) particleVelocity.get(2)); 
        this.expireTime = expireTime;
    }
    
    public boolean isDestroyed(){
        return expireTime == 0;
    }
    
    public void update(Vector gravity){
        //this.obj.addPosition((float)velocity.get(0), (float)velocity.get(1), (float)velocity.get(2));
        //this.obj.addPosition((float)gravity.get(0), (float)gravity.get(1), (float)gravity.get(2));
        //expireTime -=1;
        this.addPosition((float)velocity.get(0), (float)velocity.get(1), (float)velocity.get(2));
        this.addPosition((float)gravity.get(0), (float)gravity.get(1), (float)gravity.get(2));
    }
     
    public void draw( Matrix4 modelMatrix, boolean isParticle, Material material, boolean isMaterial){
        //System.out.println("draw position: "+this.position.size());
        modelMatrix.loadIdentity();
        if(isParticle)
            //System.out.println("posicao do draw: "+this.position.size());
            modelMatrix.translate((float) this.position.get(0), (float) this.position.get(1), (float) this.position.get(2));
            modelMatrix.scale(this.getObj().getSize()[0], this.getObj().getSize()[1], this.getObj().getSize()[2]);
        modelMatrix.bind();
        //this.obj.bind();
        this.obj.draw();
    }
    
    public void addPosition(float x, float y, float z){
        //System.out.println("lenth positon: "+this.position.size());
        this.position.set(0, (float) this.position.get(0) + x);
        this.position.set(1, (float) this.position.get(1) + y);
        this.position.set(2, (float) this.position.get(2) + z);
    }
    
    public void updatePosition(){
        this.addPosition(0, 0, 0);
    }
    
    public void addSize(float x, float y, float z){
        this.obj.addSize(x, y, z);
    }
    
}
