/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.SimpleModel;
import static java.io.FileDescriptor.in;
import static java.lang.System.in;
import java.nio.IntBuffer;
import java.util.Vector;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;

/**
 *
 * @author xima
 */
class Point extends SimpleModel {
        private int expireTime;
        private Vector position;
        private Vector velocity;
        
        public Point(float x, float y, float z){//,GL3 gl){
            //this.gl = gl;
            this.position = new Vector(3);
            this.position.add(x);
            this.position.add(y);
            this.position.add(z);
            
            vertex_buffer = new float[]{x,y,z};
            normal_buffer = new float[]{0.0f,0.0f,1.0f};
            
        }
        
        public Point(Vector position, Vector velocity, int expireTime){
            this.position = position;
            this.velocity = velocity;
            this.expireTime = expireTime;
            
            vertex_buffer = new float[] {(float)this.position.get(0), (float)this.position.get(1), (float)this.position.get(2)};
            normal_buffer = new float[] {0.0f, 0.0f, 1.0f};
        }
        
        

        @Override
        public void draw() {
            draw(GL3.GL_POINTS);
        }
        
        public float getX(){
            return (float) this.position.get(0);
            //return (float) this.x;
        }
        
        public float getY(){
            return (float) this.position.get(1);
            //return (float) this.y;
        }
        
        public float getZ(){
            return (float) this.position.get(2);
            //return (float) this.z;
        }
        
        public void setPointSize(float size, GL3 gl){
           gl.glPointSize(size);
        }
        
        public boolean isDestroyed(){
            return expireTime == 0;
        }
        
        //NAO FOI IMPLEMENTADO O UPDATE
        
        public void update(Vector gravity, Matrix4 modelMatrix){
            modelMatrix.loadIdentity();
            
            modelMatrix.translate((float)velocity.get(0), (float)velocity.get(1), (float)velocity.get(2));
            modelMatrix.translate((float)gravity.get(0), (float)gravity.get(1), (float)gravity.get(2));
            expireTime -=1;
            
            modelMatrix.bind();
            this.bind();
            this.draw();
        }
        
        public void update( Matrix4 modelMatrix){
            modelMatrix.loadIdentity();
            modelMatrix.bind();
            this.bind();
            this.draw();
        }
    }
