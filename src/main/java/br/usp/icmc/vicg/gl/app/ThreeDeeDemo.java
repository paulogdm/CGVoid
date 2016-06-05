/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.vicg.gl.app;

import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.SimpleModel;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import br.usp.icmc.vicg.gl.util.Shader;
import br.usp.icmc.vicg.gl.util.ShaderFactory;
import java.util.Random;
import javax.swing.JFrame;
import main.InputKey;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;


/**
 *
 * @author xima
 */
public class ThreeDeeDemo implements GLEventListener {
    private InputKey input;
    private Shader shader;
    private Matrix4 modelMatrix;//Matrix4 é implementacao da primeira e da segunda prova
    private Matrix4 projectionMatrix;
    private Matrix4 viewMatrix;
    private Point[] points;
    
    
    @Override
    public void init(GLAutoDrawable glad) {
        input = new InputKey();
        GL3 gl = glad.getGL().getGL3();//
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepth(1.0f);
        
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_CULL_FACE);
        shader = ShaderFactory.getInstance(ShaderFactory.ShaderType.COMPLETE_SHADER);
        shader.init(gl);
        shader.bind();//indica que shader esta ativo   
        
        modelMatrix = new Matrix4();
        projectionMatrix = new Matrix4();
        viewMatrix = new Matrix4();
        
        
        modelMatrix.init(gl,shader.getUniformLocation("u_modelMatrix"));
        projectionMatrix.init(gl, shader.getUniformLocation("u_projectionMatrix"));
        viewMatrix.init(gl, shader.getUniformLocation("u_viewMatrix"));
        
        points = new Point[10000];
        Random random = new Random();
        
        for(int i=0; i < points.length; i++){
            //x = random entre  -50 e 50
            //y = random entre -50 e 50
            //z = random entre 0 e -200
            points[i] = new Point((random.nextFloat() - 0.5f)*100, (random.nextFloat()-0.5f)*100, random.nextInt(200) - 200);
        }
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void display(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        for(int i=0; i< points.length; i++){
            modelMatrix.loadIdentity();
            modelMatrix.bind();
            points[i].bind();
            points[i].draw();
        }
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
        
    }
    public InputKey getKeyListener(){
        return this.input;
    }
    
     public static void main(String []args){
        //define propriedades
        GLProfile profile = GLProfile.get(GLProfile.GL3);
        
        GLCapabilities cap = new GLCapabilities(profile);
        cap.setDoubleBuffered(true);
        cap.setHardwareAccelerated(true);
        //criar canvas
        GLCanvas canvas = new GLCanvas(cap);
        //registra meu desenhador
        ThreeDeeDemo obj = new ThreeDeeDemo();
        canvas.addGLEventListener(obj);
        canvas.addKeyListener(obj.getKeyListener());
        
        JFrame frame = new JFrame("Beta");
        frame.getContentPane().add(canvas);
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//quando fechar a janela sai do sistema
        
        AnimatorBase animator = new FPSAnimator(canvas, 30);
        
        frame.setVisible(true);
        
        frame.setFocusable(true);
        
        animator.start();
    }
     
     private static class Point extends SimpleModel{
        float x, y, z;
        
        public Point(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
            vertex_buffer = new float[]{this.x ,this.y ,this.z};
            normal_buffer = new float[]{0.0f, 0.0f, 1.0f};
        }

        @Override
        public void draw() {
            draw(GL3.GL_POINTS);
        }
    }

}
