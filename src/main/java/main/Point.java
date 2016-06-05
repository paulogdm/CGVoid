/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import br.usp.icmc.vicg.gl.model.SimpleModel;
import static java.io.FileDescriptor.in;
import static java.lang.System.in;
import java.nio.IntBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;

/**
 *
 * @author xima
 */
class Point extends SimpleModel {
        float x, y, z;
        GL3 gl;
        
        public Point(float x, float y, float z,GL3 gl){
            this.gl = gl;
            this.x = x;
            this.y = y;
            this.z = z;
            vertex_buffer = new float[]{x,y,z};
            normal_buffer = new float[]{0.0f,0.0f,1.0f};
            gl.glPointSize(2.0f);
            
            gl.glPointParameterf(0, 255);
            
        }
        

        @Override
        public void draw() {
            draw(GL3.GL_POINTS);
        }
        
        public float getX(){
            return (float) this.x;
        }
        
        public float getY(){
            return (float) this.y;
        }
        
        public float getZ(){
            return (float) this.z;
        }
        
    }
