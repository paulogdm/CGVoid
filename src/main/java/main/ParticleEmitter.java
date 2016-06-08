/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import br.usp.icmc.vicg.gl.core.Material;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.util.Shader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import javax.media.opengl.GL3;


/**
 *
 * @author edu
 */
public class ParticleEmitter {
    private static Random randomGenerator = new Random();
    private final List<Point> particles;
    private Vector location;
    private float swapwingRate;
    private int particleLifeTime;
    private Vector gravity;
    private Vector initialVelocity;
    private float velocityModifier;
    private Matrix4 modelMatrix;
    private GL3 gl;
    private Shader shader;
    
    public ParticleEmitter(Vector location, float swapingRate, int particleLifeTime, Vector gravity,
                                                Vector initialVelocity, float velocityModifier, Matrix4 modelMatrix, GL3 gl, Shader shader){
        this.location = location;
        this.swapwingRate = swapingRate;
        this.particleLifeTime = particleLifeTime;
        this.gravity = gravity;
        this.particles = new ArrayList<Point>((int) swapingRate * particleLifeTime);
        this.initialVelocity = initialVelocity;
        this.velocityModifier = velocityModifier;
        this.modelMatrix = modelMatrix;
        this.gl = gl;
        this.shader = shader;
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

    
    public Point generateNewParticle(int dx, int dy){
        Vector particleLocation = new Vector(location);
        Vector particleVelocity = new Vector();
        
        float randomX = (float) randomGenerator.nextDouble() - 0.5f;
        float randomY = (float) randomGenerator.nextDouble() - 0.5f;
        float randomZ = (float) randomGenerator.nextDouble() - 0.5f;
        
        particleVelocity.add( (randomX +(float) this.initialVelocity.get(0) + dx/10)/120 );
        particleVelocity.add( (randomY +(float) this.initialVelocity.get(1) + dy/10)/120 );
        particleVelocity.add( (randomZ +(float) this.initialVelocity.get(2))/120 );
        //MUDAR VELOCIDADE PELO VELOCITYMODIFIER NA HORA
        Point p = new Point(particleLocation, particleVelocity, particleLifeTime);
        p.init(gl, shader);
        return p;
    }
    
    public void update() {
        System.out.println("particle size: "+particles.size());
        for(int i = 0; i < particles.size(); i++){
            Point point = particles.get(i);
            point.update(gravity, modelMatrix);
            if(point.isDestroyed()){
                particles.remove(i);
                i--;
            }
        }
        for( int i = 0; i< swapwingRate; i++){
            particles.add(generateNewParticle(0, 0));
            System.out.println("criate particle "+i);
        }
    }
    
    public void draw(Material material){
        for(Point point: particles){
            point.update(gravity, modelMatrix);
        }
    }
    
}
