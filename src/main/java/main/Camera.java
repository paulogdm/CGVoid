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
    
    public void init(GL3 gl, int handle){
        position_cam.add(0.0f);
        position_cam.add(-4.0f);
        position_cam.add(-5.0f);
        
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
    
    public void useView(){
        viewMatrix.loadIdentity();
        viewMatrix.rotate((float) rotateXYZ.get(0), 1, 0, 0);
        viewMatrix.rotate((float) rotateXYZ.get(1), 0, 1, 0);
        viewMatrix.rotate((float) rotateXYZ.get(2), 0, 0, 1);
        viewMatrix.translate((float)position_cam.get(0), (float)position_cam.get(1), (float)position_cam.get(2));
        viewMatrix.bind();
    }
    
    public void move(float amt){
        float ry = (float) this.rotateXYZ.get(1);
        float z = (float) (amt * Math.sin(Math.toRadians(ry + 90)));
        float x = (float) (amt * Math.cos(Math.toRadians(ry + 90)));
        position_cam.set(0, (float) position_cam.get(0) + x);
        position_cam.set(2, (float) position_cam.get(2) + z);
    }
    
    public void rotateY(float amt){
        this.rotateXYZ.set(1, (float) this.rotateXYZ.get(1) + amt);
    }
    
    public void up(float amt){
        position_cam.set(1, (float) position_cam.get(1)+amt);
    }
    
}
