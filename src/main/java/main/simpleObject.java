/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.util.Shader;
import java.io.IOException;
import javax.media.opengl.GL3;

/**
 *
 * @author Nathan
 */
public class simpleObject {
    public static final float KEEP_VALUE = 314.159265f;
    
    private JWavefrontObject object;
    private float[] size;
    private float[] rotation;
    private float[] position;
    private float[] speed;
    private float auxFactor;
    private boolean drawable;
    
    public simpleObject(JWavefrontObject object) {
        this.object = object;
        this.size = new float[] {0f, 0f, 0f};
        this.rotation = new float[] {0f, 0f, 0f};
        this.position = new float[] {0f, 0f, 0f};
        this.speed = new float[] {0f, 0f, 0f};
        this.auxFactor = 0f;
        this.drawable = true;
    }
    public simpleObject(JWavefrontObject object, float[] size, float[] rotation, float[] position, float[] speed, float auxFactor) {
        this.object = object;
        this.size = size;
        this.rotation = rotation;
        this.position = position;
        this.speed = speed;
        this.auxFactor = auxFactor;
        this.drawable = true;
    }
    
    public void setSize(float x, float y, float z) {
        this.size[0] = (x != simpleObject.KEEP_VALUE) ? x : this.size[0];
        this.size[1] = (y != simpleObject.KEEP_VALUE) ? y : this.size[1];
        this.size[2] = (z != simpleObject.KEEP_VALUE) ? z : this.size[2];
    }
    public void setRotation(float x, float y, float z) {
        this.rotation[0] = (x != simpleObject.KEEP_VALUE) ? x : this.rotation[0];
        this.rotation[1] = (y != simpleObject.KEEP_VALUE) ? y : this.rotation[1];
        this.rotation[2] = (z != simpleObject.KEEP_VALUE) ? z : this.rotation[2];
    }
    public void setPosition(float x, float y, float z) {
        this.position[0] = (x != simpleObject.KEEP_VALUE) ? x : this.position[0];
        this.position[1] = (y != simpleObject.KEEP_VALUE) ? y : this.position[1];
        this.position[2] = (z != simpleObject.KEEP_VALUE) ? z : this.position[2];
    }
    public void setSpeed(float x, float y, float z) {
        this.speed[0] = (x != simpleObject.KEEP_VALUE) ? x : this.speed[0];
        this.speed[1] = (y != simpleObject.KEEP_VALUE) ? y : this.speed[1];
        this.speed[2] = (z != simpleObject.KEEP_VALUE) ? z : this.speed[2];
    }
    public void setAuxFactor(float auxFactor) {
        this.auxFactor = auxFactor;
    }    
    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
    }
    
    public void addSize(float x, float y, float z) {
        this.size[0] += x;
        this.size[1] += y;
        this.size[2] += z;       
    }
    public void addRotation(float x, float y, float z) {
        this.rotation[0] += x;
        this.rotation[1] += y;
        this.rotation[2] += z;       
    }
    public void addPosition(float x, float y, float z) {
        this.position[0] += x;
        this.position[1] += y;
        this.position[2] += z;       
    }
    public void addSpeed(float x, float y, float z) {
        this.speed[0] += x;
        this.speed[1] += y;
        this.speed[2] += z;       
    }
    
    public float[] getSize() {
        return this.size.clone();
    }
    public float[] getRotation() {
        return this.rotation.clone();
    }
    public float[] getPosition() {
        return this.position.clone();
    }
    public float[] getSpeed() {
        return this.speed.clone();
    }
    public float getAuxFactor() {
        return this.auxFactor;
    }
    
    public void getReady(GL3 gl, Shader shader) throws IOException{
        this.object.init(gl, shader);
        this.object.unitize();
        this.object.dump();
    }
    
    public void updatePosition() {
        this.position[0] += this.speed[0];
        this.position[1] += this.speed[1];
        this.position[2] += this.speed[2];
    }
    
    public void draw() {
        if(this.drawable) {
            this.object.draw();
        }
    }
    
    public void dispose() {
        this.object.dispose();
    }
}
