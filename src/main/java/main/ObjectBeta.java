package main;

import br.usp.icmc.vicg.gl.core.Light;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.util.Shader;
import br.usp.icmc.vicg.gl.util.ShaderFactory;
import static com.sun.java.accessibility.util.AWTEventMonitor.*;
import java.io.IOException;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;

public class ObjectBeta implements GLEventListener {
    
    private Shader shader;
    private Matrix4 modelMatrix;//Matrix4 é implementacao da primeira e da segunda prova
    private Matrix4 projectionMatrix;
    private float[][] viewMatrix_stored;
    private Matrix4 viewMatrix;
    private Light light;
    
    private simpleObject planet;
    
    private ship main_ship;
    private planet moon;
//    private ship view_ship;
    
    private final InputKey input;
    
    private float left_right_angle;
    private float up_down_angle;
    
    public ObjectBeta() {

        shader = ShaderFactory.getInstance(ShaderFactory.ShaderType.COMPLETE_SHADER);
        modelMatrix = new Matrix4();
        projectionMatrix = new Matrix4();
        viewMatrix = new Matrix4();
        
        viewMatrix_stored = new float[][]{
            {0, 2, 2},
            {0, 0 ,0},
            {0, 1,0}
        };
        
        light = new Light();
        
        main_ship = new ship();
//        view_ship = new ship();
        
        moon = new planet();
        
        input = new InputKey();
        
        left_right_angle = 0;
        up_down_angle = 0;
    }
   
    @Override
    //quando ele começar a desenhar ele vai setar o init
    public void init(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3();//
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClearDepth(1.0f);
        
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_CULL_FACE);
        
        shader.init(gl);
        shader.bind();//indica que shader esta ativo   
        
        modelMatrix.init(gl,shader.getUniformLocation("u_modelMatrix"));
        projectionMatrix.init(gl, shader.getUniformLocation("u_projectionMatrix"));
        viewMatrix.init(gl, shader.getUniformLocation("u_viewMatrix"));
        
        try {
            
            main_ship.getObj().getReady(gl, shader);
            moon.getObj().getReady(gl, shader);
//            view_ship.getObj().getReady(gl, shader);
            
        } catch (IOException ex) {
            Logger.getLogger(ObjectBeta.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //init the light
        light.setPosition(new float[]{10, 10, 50, 1.0f});
        light.setAmbientColor(new float[]{0.1f, 0.1f, 0.1f, 1.0f});
        light.setDiffuseColor(new float[]{0.75f, 0.75f, 0.75f, 1.0f});
        light.setSpecularColor(new float[]{0.7f, 0.7f, 0.7f, 1.0f});
        light.init(gl, shader);
       
        
        viewMatrix.loadIdentity();
        viewMatrix.lookAt(viewMatrix_stored);
        viewMatrix.bind();
    }
    
    @Override
    public void display(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3();//pega gl3 pq pega todas as capacidades de mexer no shader
        
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
       
        // VARIABLE UPDATES ---------------
        
        moon.getObj().updatePosition();    
        
        projectionMatrix.loadIdentity();
        projectionMatrix.ortho(
                -2.0f, 2.0f, 
                -2.0f, 2.0f, 
                -2 * 2.0f, 2 * 2.0f);
        projectionMatrix.bind();
        
      
        main_ship.firstLand();

        light.bind();
        
        this.cameraUpdate();
        
        this.sceneUpdate();
        
        gl.glFlush();
        
        if(this.input.getExit()){
            //exit
        }
        
    }
    
    
    @Override
    public void dispose(GLAutoDrawable glad) {
        moon.getObj().dispose();
        main_ship.getObj().dispose();
//      view_ship.getObj().dispose();
    }
    

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
        
    }
    
    public void sceneUpdate(){
        modelMatrix.loadIdentity();
        modelMatrix.translate(moon.getObj().getPosition()[0], moon.getObj().getPosition()[1], moon.getObj().getPosition()[2]);
        modelMatrix.rotate(moon.getObj().getRotation()[0],1,0,0);
        modelMatrix.rotate(moon.getObj().getRotation()[1],0,1,0);
        modelMatrix.rotate(moon.getObj().getRotation()[2],0,0,1);
        modelMatrix.scale(moon.getObj().getSize()[0], moon.getObj().getSize()[1], moon.getObj().getSize()[2]);
        modelMatrix.bind();
        moon.getObj().draw();
        
        modelMatrix.loadIdentity();
        modelMatrix.translate(main_ship.getObj().getPosition()[0], main_ship.getObj().getPosition()[1], main_ship.getObj().getPosition()[2]);
        modelMatrix.rotate(main_ship.getObj().getRotation()[0],1,0,0);
        modelMatrix.rotate(main_ship.getObj().getRotation()[1],0,1,0);
        modelMatrix.rotate(main_ship.getObj().getRotation()[2],0,0,1);
        modelMatrix.scale(main_ship.getObj().getSize()[0], main_ship.getObj().getSize()[1], main_ship.getObj().getSize()[2]);
        modelMatrix.bind();
        main_ship.getObj().draw();
        
        modelMatrix.loadIdentity();
        modelMatrix.bind();        
    }
    
    public void cameraUpdate(){
        
        this.input.update();
      
        if(this.input.getDown()){
            System.out.println("DOWN");
            up_down_angle = (up_down_angle-0.1f)%360;
        } 
        
        if(this.input.getUp()){
            System.out.println("UP");
            up_down_angle = (up_down_angle+0.1f)%360;
        }
        
        if(this.input.getRight()){
            System.out.println("RIGHT");
            left_right_angle = (left_right_angle-0.1f)%360;
        }
        
        if(this.input.getLeft()){
            System.out.println("LEFT");
            left_right_angle = (left_right_angle+0.2f)%360;
        }
        
        viewMatrix_stored[0][0] = 2.0f*(float)(sin(left_right_angle)*cos(up_down_angle));
        viewMatrix_stored[0][1] = 2.0f*(float)(sin(left_right_angle)*sin(up_down_angle));
        viewMatrix_stored[0][2] = 2.0f*(float)(cos(left_right_angle));
        
        viewMatrix.loadIdentity();
        viewMatrix.lookAt(viewMatrix_stored);
        viewMatrix.bind();
    }
    
    public InputKey getKeyListener(){
        return this.input;
    }
}