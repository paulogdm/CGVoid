package main;

import br.usp.icmc.vicg.gl.core.Light;
import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.Cube;
import br.usp.icmc.vicg.gl.model.SimpleModel;
import br.usp.icmc.vicg.gl.model.Sphere;
import br.usp.icmc.vicg.gl.model.WiredCube;
import br.usp.icmc.vicg.gl.util.Shader;
import br.usp.icmc.vicg.gl.util.ShaderFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import main.ship;
import main.planet;

public class ObjectBeta implements GLEventListener {
    
    private Shader shader;
    private Matrix4 modelMatrix;//Matrix4 é implementacao da primeira e da segunda prova
    private Matrix4 projectionMatrix;
    private Matrix4 viewMatrix;
    private Light light;
    
    private simpleObject planet;
    
    private ship main_ship;
    private planet moon;
//    private ship view_ship;
    
    private long timeStart;
    private long timeEnd;
    private long timeDiff;
    private int seconds;
    private int minutes;
    
    public ObjectBeta() {

        shader = ShaderFactory.getInstance(ShaderFactory.ShaderType.COMPLETE_SHADER);
        modelMatrix = new Matrix4();
        projectionMatrix = new Matrix4();
        viewMatrix = new Matrix4();
//        light = new Light();
        
        main_ship = new ship();
//        view_ship = new ship();
        
        moon = new planet();
        
        timeStart = System.currentTimeMillis();
        timeEnd = System.currentTimeMillis();
        timeDiff = 0;

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
        /*
        light.setPosition(new float[]{10, 10, 50, 1.0f});
        light.setAmbientColor(new float[]{0.1f, 0.1f, 0.1f, 1.0f});
        light.setDiffuseColor(new float[]{0.75f, 0.75f, 0.75f, 1.0f});
        light.setSpecularColor(new float[]{0.7f, 0.7f, 0.7f, 1.0f});
        light.init(gl, shader);
        */
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
        
        timeEnd = System.currentTimeMillis();
        timeDiff = timeEnd - timeStart;
        seconds = (int)(timeDiff / 1000) % 60;
        minutes = (int)(timeDiff / 60000) % 60;
        
        main_ship.firstLand();

//        viewMatrix.loadIdentity();
//       
//        viewMatrix.lookAt(
//                1, 1, 1,
//                0, 0, 0, 
//                0, 1, 0);
//        viewMatrix.bind();
//           
//        light.bind();
    
    main_ship.focusOnMe(viewMatrix);

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
        gl.glFlush();
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
}