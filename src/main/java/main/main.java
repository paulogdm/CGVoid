package main;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

public class main {
    
    public static void main(String []args){
        //define propriedades
        GLProfile profile = GLProfile.get(GLProfile.GL3);
        
        GLCapabilities cap = new GLCapabilities(profile);
        cap.setDoubleBuffered(true);
        cap.setHardwareAccelerated(true);
        //criar canvas
        GLCanvas canvas = new GLCanvas(cap);
        //registra meu desenhador
        canvas.addGLEventListener(new ObjectBeta());
        
        JFrame frame = new JFrame("Beta");
        frame.getContentPane().add(canvas);
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//quando fechar a janela sai do sistema
        
        AnimatorBase animator = new FPSAnimator(canvas, 30);
        
        frame.setVisible(true);
        animator.start();
    }
     
    private static GLCanvas newGLCanvas(GLCapabilities cap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
