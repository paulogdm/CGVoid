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
    private Vector espace_change;
    
    public ParticleEmitter(Vector location, float swapingRate, int particleLifeTime, Vector gravity,
                                                Vector initialVelocity, float velocityModifier, Matrix4 modelMatrix, GL3 gl, Shader shader){
        this.location = location; //posicao que o sistema de particulas
        this.swapwingRate = swapingRate;//quantidade de particulas criadas por update
        this.particleLifeTime = particleLifeTime;//tempo de duracao de uma particula
        this.gravity = gravity;//gravidade(direcao para onde a particula cai)
        this.particles = new ArrayList<Point>((int) swapingRate * particleLifeTime);//todas as particulas
        this.initialVelocity = initialVelocity;
        this.velocityModifier = velocityModifier;
        this.modelMatrix = modelMatrix;//necessario para dar draw
        this.gl = gl;//draw
        this.shader = shader;//draw
        this.espace_change = new Vector();//ajuda a gerar a particular envolta da localizacao do systema(ver generateNewParticle)
        this.espace_change.add(0.5f); this.espace_change.add(0.5f); this.espace_change.add(0.5f);
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

     /* cria uma nova particula
     * primeiro pega x, y e z em uma distancia aleatoria da localizacao inicial 
     * gera o asteroid na posicao inicial + randomX/Y/Z para as particulas nao iniciarem no mesmo lugar
     * na criacao do randX/Y/Z é criado entre -0.5 e +0.5
     * ja que nexDouble retorna entre 0.0 e -1.0 e espace_change inicializa com 0.5 em todas coordenadas
     */
    public Point generateNewParticle(int dx, int dy){
        Vector particleLocation = new Vector(location);
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
        Point p = new Point(particleLocation, particleVelocity, particleLifeTime);
        p.init(gl, shader);
        return p;
    }
    
    public void setEspaceChange(float x, float y, float z){
        this.espace_change.set(0, x);
        this.espace_change.set(0, y);
        this.espace_change.set(0, z);
    }
    
    
    /*
        para cada particula eh chamado o update
        se a particula tiver tempo igual a zero entao remove ela do sistema
        cria n particulas novas(n==swapwingRate)
    */
    public void update() {
        System.out.println("particle size: "+particles.size());
        for(int i = 0; i < particles.size(); i++){
            Point point = particles.get(i);
            point.update(gravity);
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
    /*
        redesenha as particulas do sistema
    */
    public void draw(Material material, boolean useMaterial){
        for(Point point: particles){
           
           point.draw(modelMatrix,true, material, true);
        }
    }
    
}
