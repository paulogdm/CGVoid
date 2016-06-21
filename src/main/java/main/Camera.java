/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import br.usp.icmc.vicg.gl.matrix.Matrix4;
import java.util.Vector;
import javax.media.opengl.GL3;

/**
 *
 * @author xima
 * Classe que gerencia a Camera
 */
public class Camera {
    private float[] viewMatrix_stored;
    private Vector position_cam, position_lookat, vec_up, rotateXYZ;
    private Matrix4 viewMatrix;
    
    public Camera(){
        position_cam = new Vector(3);
        
        position_lookat = new Vector(3);
        
        vec_up = new Vector(3);
        
        rotateXYZ = new Vector(3);
        
        viewMatrix = new Matrix4();
    }
    
    /*
     * inicializa a camera lookAt e manda um bind pra placa de video
     */
    public void init(GL3 gl, int handle){
        position_cam.add(0.0f);
        position_cam.add(-3.0f);
        position_cam.add(-3.0f);
        
        position_lookat.add(0.0f);
        position_lookat.add(0.0f);
        position_lookat.add(-1.0f);
        
        vec_up.add(0.0f);
        vec_up.add(1.0f);
        vec_up.add(0.0f);
        
        rotateXYZ.add(0.0f);
        rotateXYZ.add(0.0f);
        rotateXYZ.add(0.0f);
        
        viewMatrix.init(gl, handle);
        viewMatrix.lookAt((float) position_cam.get(0), (float) position_cam.get(1), (float) position_cam.get(2),
                (float) position_lookat.get(0), (float) position_lookat.get(1), (float) position_lookat.get(2), 
                (float) vec_up.get(0), (float) vec_up.get(1), (float) vec_up.get(2));
        viewMatrix.bind();
    }
    
    /*
     * modifica a camera devidamente rotacionando ou transladando e modificando para onde foi "pedido"
     * nao modifica a posicao nem nada, so rotaciona e translada para posicao que os vetores quardam(se nao modificou faz mesmo assim!)
     */
    public void useView(){
        viewMatrix.loadIdentity();
        viewMatrix.rotate((float) rotateXYZ.get(0), 1, 0, 0);
        viewMatrix.rotate((float) rotateXYZ.get(1), 0, 1, 0);
        viewMatrix.rotate((float) rotateXYZ.get(2), 0, 0, 1);
        viewMatrix.translate((float)position_cam.get(0), (float)position_cam.get(1), (float)position_cam.get(2));
        viewMatrix.bind();
    }
    
    /*
     * move a camera para frente ou para traz por uma certa quantidade
     * se for positiva vai pra frente, negativo para traz
     */
    public void move(float amt){
        float ry = (float) this.rotateXYZ.get(1);
        float z = (float) (amt * Math.sin(Math.toRadians(ry + 90)));
        float x = (float) (amt * Math.cos(Math.toRadians(ry + 90)));
        position_cam.set(0, (float) position_cam.get(0) + x);
        position_cam.set(2, (float) position_cam.get(2) + z);
    }
    
    /*
     * rotaciona no exio Y , ou seja olha pra esquerda ou pra direita
     */
    public void rotateY(float amt){
        this.rotateXYZ.set(1, (float) this.rotateXYZ.get(1) + amt);
    }
    /*
     * flutua a camera no exio Y uma certa quantidade para cima ou baixo
     * se a quantidade for positiva entao flutua para baixo a camera
     * se negativo entao flutua para cima a camera(sim, para cima negativo)
     */
    public void up(float amt){
        position_cam.set(1, (float) position_cam.get(1)+amt);
    }
    
    /*
     * a camera olha para cima ou para baixo para um certo amount
     * se positivo olha para baixo
     * se tiver negativo olha para cima
     */
    public void lookDown(float amt){
        this.rotateXYZ.set(0,(float)this.rotateXYZ.get(0) + amt);
    }
    
    // nao funfo adequandamente, nao esta sendo usado
    public void lookLeft(float amt){
        this.rotateXYZ.set(2,(float) this.rotateXYZ.get(2) + amt);
    }
}
