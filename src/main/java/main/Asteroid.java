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
    
    //cria um unico asteroid
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
    
    //criador do asteroid na forma de particulas(cria varias)
    public Asteroid(Vector particleLocation, Vector particleVelocity, int particleLifeTime, String file) {
        super(file);
        this.position = new Vector(3);
        this.position.add((float)particleLocation.get(0)); this.position.add((float)particleLocation.get(1));  this.position.add((float)particleLocation.get(2)); 
        this.velocity =new Vector(3);
        this.velocity.add((float) particleVelocity.get(0)); this.velocity.add((float) particleVelocity.get(1)); this.velocity.add((float) particleVelocity.get(2)); 
        this.expireTime = expireTime;
    }
    
    
    //particulas, indica pra destruir asteroid(nunca eh usado nesse caso)
    public boolean isDestroyed(){
        return expireTime == 0;
    }
    
    //atualiza o asteroid com a velocidade que ele possui e a gravidade dado pelo sys.particulas
    public void update(Vector gravity){
        this.addPosition((float)velocity.get(0), (float)velocity.get(1), (float)velocity.get(2));
        this.addPosition((float)gravity.get(0), (float)gravity.get(1), (float)gravity.get(2));
    }
     
    //desenha o asteroid
    public void draw( Matrix4 modelMatrix, boolean isParticle, Material material, boolean isMaterial){
        modelMatrix.loadIdentity();
        if(isParticle)
            modelMatrix.translate((float) this.position.get(0), (float) this.position.get(1), (float) this.position.get(2));
            modelMatrix.scale(this.getObj().getSize()[0], this.getObj().getSize()[1], this.getObj().getSize()[2]);
        modelMatrix.bind();
        this.obj.draw();
    }
    
    //adiciona uma quantidade na posicao do asteroid, usado para ele se mover(olhar update o uso dele)
    public void addPosition(float x, float y, float z){
        this.position.set(0, (float) this.position.get(0) + x);
        this.position.set(1, (float) this.position.get(1) + y);
        this.position.set(2, (float) this.position.get(2) + z);
    }
    
    //inicializa Posicao(nome nao ta os melhores)
    public void updatePosition(){
        this.addPosition(0, 0, 0);
    }
    
    //modifica o tamanho do asteroid(somando ou subtraindo valores)
    //ex: inicial = 1, se por this.addSize(-0.5 , -0.5, -0.5); ele tera a metade do tamanho
    //                 se por this.addSize(1,1,1); ele tera o dobro do tamanho(2)
    public void addSize(float x, float y, float z){
        this.obj.addSize(x, y, z);
    }
    
}
