/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import com.sun.javafx.geom.Vec3f;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author paulogdm
 */

public class Planet extends ObjectContainer{

    public  Planet(){
        super("./data/planet/planet2.obj");
        this.obj.setPosition(1.5f, 0.0f, 2.0f);
    }
    
    public void changePosition(float x, float y, float z){
        this.obj.setPosition(x, y, z);
    }
}
/*
public class Planet {
     public Planet(){
         Model m = null;
         try {
             m = OBJLoader.loadModel(new File("./data/planet/planet2.obj"));
         } catch (FileNotFoundException e){
             e.printStackTrace();
             Display.destroy();
             System.exit(1);
         } catch (IOException e){
             e.printStackTrace();
             Display.destroy();
             System.exit(1);
         }
         GL11.glBegin(GL11.GL_POLYGON);
         for ( Face face: m.faces) {
             Vector3f n1 = m.normals.get((int) face.normal.x -1);
             GL11.glNormal3f(n1.x, n1.y, n1.z);
             Vector3f v1 = m.vertices.get((int) face.vertex.x -1);
             GL11.glVertex3f(v1.x, v1.y, v1.z);
             
             Vector3f n2 = m.normals.get((int) face.normal.x -1);
             GL11.glNormal3f(n2.x, n2.y, n2.z);
             Vector3f v2 = m.vertices.get((int) face.vertex.x -1);
             GL11.glVertex3f(v2.x, v2.y, v2.z);
             
             Vector3f n3 = m.normals.get((int) face.normal.x -1);
             GL11.glNormal3f(n3.x, n3.y, n3.z);
             Vector3f v3 = m.vertices.get((int) face.vertex.x -1);
             GL11.glVertex3f(v3.x, v3.y, v3.z);
         }
         GL11.glEnd();
     }
}*/