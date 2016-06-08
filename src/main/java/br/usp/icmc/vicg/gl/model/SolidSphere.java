/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.vicg.gl.model;

import java.util.ArrayList;
import java.util.Vector;
import javax.media.opengl.GL;

/**
 *
 * @author PC
 */
public class SolidSphere extends SimpleModel {

  private final int POLUDNIKOV = 36;
  private final int ROVNOBEZIEK = 18;
  private final double UHOL_KROK = ((2 * Math.PI / 360.0) * 10.0);
  private Vector position;
    private Vector size;
  public SolidSphere(float px, float py, float pz) {
    ArrayList<Float> vertex = new ArrayList<>();
    
    this.position = new Vector(3);
    this.position.add(px);
    this.position.add(py);
    this.position.add(pz);
    
    this.size = new Vector(3);
    this.size.add(1.0f);this.size.add(1.0f);this.size.add(1.0f);
        
    int p2 = POLUDNIKOV / 2;
    int r2 = ROVNOBEZIEK / 2;
    for (int y = -r2; y < r2; ++y) {
      double cy = Math.cos(y * UHOL_KROK);
      double cy1 = Math.cos((y + 1) * UHOL_KROK);
      double sy = Math.sin(y * UHOL_KROK);
      double sy1 = Math.sin((y + 1) * UHOL_KROK);

      for (int i = -p2; i < p2; ++i) {
        double ci = Math.cos(i * UHOL_KROK);
        double si = Math.sin(i * UHOL_KROK);

        vertex.add((float) (ci * cy));
        vertex.add((float) (sy));
        vertex.add((float) (si * cy));

        vertex.add((float) (ci * cy1));
        vertex.add((float) (sy1));
        vertex.add((float) (si * cy1));
      }
    }

    vertex_buffer = new float[vertex.size()];
    normal_buffer = new float[vertex.size()];
    for (int i = 0; i < vertex.size(); i++) {
      vertex_buffer[i] = normal_buffer[i] = vertex.get(i);
    }
  }

    public SolidSphere() {
         ArrayList<Float> vertex = new ArrayList<>();
   
        
    int p2 = POLUDNIKOV / 2;
    int r2 = ROVNOBEZIEK / 2;
    for (int y = -r2; y < r2; ++y) {
      double cy = Math.cos(y * UHOL_KROK);
      double cy1 = Math.cos((y + 1) * UHOL_KROK);
      double sy = Math.sin(y * UHOL_KROK);
      double sy1 = Math.sin((y + 1) * UHOL_KROK);

      for (int i = -p2; i < p2; ++i) {
        double ci = Math.cos(i * UHOL_KROK);
        double si = Math.sin(i * UHOL_KROK);

        vertex.add((float) (ci * cy));
        vertex.add((float) (sy));
        vertex.add((float) (si * cy));

        vertex.add((float) (ci * cy1));
        vertex.add((float) (sy1));
        vertex.add((float) (si * cy1));
      }
    }

    vertex_buffer = new float[vertex.size()];
    normal_buffer = new float[vertex.size()];
    for (int i = 0; i < vertex.size(); i++) {
      vertex_buffer[i] = normal_buffer[i] = vertex.get(i);
    }
    }

  @Override
  public void draw() {
    draw(GL.GL_TRIANGLE_STRIP);
  }
  
  public float getX(){
        return (float) this.position.get(0);
    }
    
    public float getY(){
        return (float) this.position.get(1);
    }
    
    public float getZ(){
        return (float) this.position.get(2);
    }
    
    public void changePosition(float x, float y, float z){
        this.position.set(0, (float) this.position.get(0) + x);
        this.position.set(1, (float) this.position.get(1) + y);
        this.position.set(2, (float) this.position.get(2) + z);
    }
    
    public float getSizeX(){
        return (float) this.size.get(0);
    }
    public float getSizeY(){
        return (float) this.size.get(1);
    }
    public float getSizeZ(){
        return (float) this.size.get(2);
    }
    
    public void addSize(float x, float y, float z){
        this.size.set(0, (float)this.size.get(0)+x);
        this.size.set(1, (float)this.size.get(1)+y);
        this.size.set(2, (float)this.size.get(2)+z);
    }
}
