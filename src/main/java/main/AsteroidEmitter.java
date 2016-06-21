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
 *
 *Systema de particulas para criacao de asteroids
 * usado para terra explodida
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
    
    //criador do systema de particulas(asteroid no caso)
    public AsteroidEmitter(Vector location, float swapingRate, int particleLifeTime, Vector gravity,
                                                Vector initialVelocity, float velocityModifier, Matrix4 modelMatrix, GL3 gl, Shader shader, String file){
        this.file = file; //arquivo no asteroid
        this.location = location;//posicao no 3d
        this.swapwingRate = swapingRate;//quantas particulas criada toda vez q o update eh chamado, nesse caso eh nenhuma para eles nao sumirem e nem criarem mais
        this.particleLifeTime = particleLifeTime;//tempo da particula exitente
        this.gravity = gravity; //gravidade interfere para onde a particula vai(se -y entao ela cai pro chao...)
        this.particles = new ArrayList<Asteroid>((int) swapingRate );//cria uma lista com todos asteroids criados
        this.initialVelocity = initialVelocity;
        this.velocityModifier = velocityModifier;
        this.modelMatrix = modelMatrix;//para poder criar e dar draw
        this.gl = gl; //para dar draw
        this.shader = shader;//para dar draw
        this.espace_change = new Vector();//ajuda a gerar a particular envolta da localizacao do systema(ver generateNewParticle)
        this.espace_change.add(0.5f); this.espace_change.add(0.5f); this.espace_change.add(0.5f);
        
        for( int i = 0; i< swapwingRate; i++){
        //cria as asteroid ja aqui(feito para chamar só aqui e nao fazer nada 
        //no update(o quer seria certo se as particulas sumissem e precisasse aparecer novas)
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

    /* cria uma nova particula
     * primeiro pega x, y e z em uma distancia aleatoria da localizacao inicial 
     * gera o asteroid na posicao inicial + randomX/Y/Z para as particulas nao iniciarem no mesmo lugar
     * na criacao do randX/Y/Z é criado entre -0.5 e +0.5
     * ja que nexDouble retorna entre 0.0 e -1.0 e espace_change inicializa com 0.5 em todas coordenadas
     */
    public Asteroid generateNewParticle(int dx, int dy) throws IOException{
        Vector particleLocation = new Vector(this.location);
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
        p.getObj().getReady(this.gl, this.shader);
        p.addSize(-0.8f, -0.8f, -0.8f);//deixa pequenas
        return p;
    }
    
    //modifica as distancias entre as particulas quando inicializa
    public void setEspaceChange(float x, float y, float z){
        this.espace_change.set(0, x);
        this.espace_change.set(0, y);
        this.espace_change.set(0, z);
    }
    
    //para cada asteroid chama o update dele para que a particula ande pelo espaco
    public void update() {
        for(int i = 0; i < particles.size(); i++){
            Asteroid point = particles.get(i);
            point.update(gravity);
        }
    }
    
    //desenha todas as "particulas"
    public void draw(Material material, boolean useMaterial){
        for(Asteroid point: particles){
           
           point.draw(modelMatrix,true, material, false);
        }
    }
    
}
