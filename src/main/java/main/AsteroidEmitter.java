/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import br.usp.icmc.vicg.gl.core.Material;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.util.Shader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL3;


/**
 *
 * @author edu
 */
public class AsteroidEmitter {
    private static Random randomGenerator = new Random();
    private final List<Asteroid> particles;
    private Vector location;
    private float swapwingRate;
    private int particleLifeTime;
    private Vector gravity;
    private Vector initialVelocity;
    private float velocityModifier;
    private Matrix4 modelMatrix;
    private GL3 gl;
    private Shader shader;
    private Vector espace_change;
    private String file;
    
    public AsteroidEmitter(Vector location, float swapingRate, int particleLifeTime, Vector gravity,
                                                Vector initialVelocity, float velocityModifier, Matrix4 modelMatrix, GL3 gl, Shader shader, String file){
        this.file = file;
        this.location = location;
       // System.out.println("location size: "+location.size());
       // System.out.println("location size: "+this.location.size());
        this.swapwingRate = swapingRate;
        this.particleLifeTime = particleLifeTime;
        this.gravity = gravity;
        this.particles = new ArrayList<Asteroid>((int) swapingRate );//* particleLifeTime);
        this.initialVelocity = initialVelocity;
        this.velocityModifier = velocityModifier;
        this.modelMatrix = modelMatrix;
        this.gl = gl;
        this.shader = shader;
        this.espace_change = new Vector();
        this.espace_change.add(0.5f); this.espace_change.add(0.5f); this.espace_change.add(0.5f);
        
        for( int i = 0; i< swapwingRate; i++){
            try {
                particles.add(generateNewParticle(0, 0));
            } catch (IOException ex) {
                Logger.getLogger(AsteroidEmitter.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("criate particle A "+i);
        }
    }
    
    public float getVelocityModifier(){
        return velocityModifier;
    }
    
    public void setVelocityModifier(float velocityModifier){
        this.velocityModifier = velocityModifier;
    }
    
    public Vector getLocation(){
        return location;
    }
    
    public void setLocation(Vector location){
        this.location = location;
    }
    
    public void setLocation(float x, float y, float z){
        this.location.set(0, (float)this.location.get(0) + x);
        this.location.set(1, (float)this.location.get(1) + y);
        this.location.set(2, (float)this.location.get(2) + z);
    }
    
    public float getSwapingRate(){
        return swapwingRate;
    }
    
    public void setSwapingRate(float swapingRate){
        this.swapwingRate = swapingRate;
    }
    
    public Vector getGravity(){
        return gravity;
    }
    
    public void setGravity(Vector gravity){
        this.gravity = gravity;
    }
    
    public int getParticleLifeTime(){
        return particleLifeTime;
    }
    
    public void setParticleLifeTime(int particleLifeTime){
        this.particleLifeTime = particleLifeTime;
    }
    
    public Vector getInitialVelocity(){
        return initialVelocity;
    }
    
    public void setInitialVelocity(Vector initilVelocity){
        this.initialVelocity = initialVelocity;
    }

    
    public Asteroid generateNewParticle(int dx, int dy) throws IOException{
        Vector particleLocation = new Vector(this.location);
       // System.out.println("generate location: "+this.location.size());
       // System.out.println("generate particlelocation: "+particleLocation.size());
        Vector particleVelocity = new Vector();
        
        float randomX = (float) randomGenerator.nextDouble() - (float) espace_change.get(0);
        float randomY = (float) randomGenerator.nextDouble() - (float) espace_change.get(1);
        float randomZ = (float) randomGenerator.nextDouble() - (float) espace_change.get(2);
        
        particleVelocity.add( (randomX +(float) this.initialVelocity.get(0) + dx/10)/40 );
        if(randomGenerator.nextBoolean())
            particleVelocity.add( (randomY +(float) this.initialVelocity.get(1) + dy/10)/40 );
        else
            particleVelocity.add( (randomY -(float) this.initialVelocity.get(1) + dy/10)/40 );
        particleVelocity.add( (randomZ +(float) this.initialVelocity.get(2))/40 );
        Asteroid p = new Asteroid(particleLocation, particleVelocity, particleLifeTime, file);
        //Point p = new Point(particleLocation, particleVelocity, particleLifeTime);
        p.getObj().getReady(this.gl, this.shader);
        //p.updatePosition();
        p.addSize(-0.8f, -0.8f, -0.8f);
        return p;
    }
    
    public void setEspaceChange(float x, float y, float z){
        this.espace_change.set(0, x);
        this.espace_change.set(0, y);
        this.espace_change.set(0, z);
    }
    
    
    public void update() {
        //System.out.println("particle size: "+particles.size());
        for(int i = 0; i < particles.size(); i++){
            Asteroid point = particles.get(i);
            point.update(gravity);
        }
        /*for( int i = 0; i< swapwingRate; i++){
            try {
                particles.add(generateNewParticle(0, 0));
            } catch (IOException ex) {
                Logger.getLogger(AsteroidEmitter.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("criate particle A "+i);
        }*/
    }
    
    public void draw(Material material, boolean useMaterial){
        for(Asteroid point: particles){
           
           point.draw(modelMatrix,true, material, false);
        }
    }
    
}
