/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import br.usp.icmc.vicg.gl.core.Material;
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
        
        public void update(Vector gravity){
            
            //modelMatrix.translate((float)velocity.get(0), (float)velocity.get(1), (float)velocity.get(2));
            //modelMatrix.translate((float)gravity.get(0), (float)gravity.get(1), (float)gravity.get(2));
            this.addPosition((float)velocity.get(0), (float)velocity.get(1), (float)velocity.get(2));
            this.addPosition((float)gravity.get(0), (float)gravity.get(1), (float)gravity.get(2));
            expireTime -=1;
        }
        
        public void draw( Matrix4 modelMatrix, boolean isParticle, Material material, boolean isMaterial){
            modelMatrix.loadIdentity();
            if(isParticle)
                modelMatrix.translate((float) this.position.get(0), (float) this.position.get(1), (float) this.position.get(2));
            modelMatrix.bind();
            this.bind();
            this.draw();
        }
        
        private void addPosition(float x, float y, float z){
            this.position.set(0, (float) this.position.get(0) + x);
            this.position.set(1, (float) this.position.get(1) + y);
            this.position.set(2, (float) this.position.get(2) + z);
        }
    }
