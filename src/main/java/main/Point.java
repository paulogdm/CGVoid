/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import br.usp.icmc.vicg.gl.model.SimpleModel;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;

/**
 *
 * @author xima
 */
public class Point extends SimpleModel {
    GL3 gl;
    public Point(float x, float y, float z, GL3 gl){
        this.gl = gl;
        vertex_buffer = new float[]{
            x ,y ,z};

        normal_buffer = new float[]{
            0.0f, 0.0f, 1.0f};
        
    }

    @Override
    public void draw() {
        draw(GL.GL_POINTS);
    }
    
}
