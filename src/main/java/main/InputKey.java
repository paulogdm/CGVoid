package main;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author paulogdm
 * das keys possiveis retorna se for precionada ou nao
 */
public class InputKey implements KeyListener {
    private final boolean[] keys = new boolean[120];  
    public boolean up, down, left, right, exit;
    public boolean arrow_up, arrow_down, arrow_left, arrow_right;
    public boolean spaceBar, shift, G, Ctrl;
    public boolean E, Q, W, S, A, D;
    
    public void update(){
        up = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
        arrow_up = keys[KeyEvent.VK_UP];
        arrow_down = keys[KeyEvent.VK_DOWN];
        arrow_right = keys[KeyEvent.VK_RIGHT];
        arrow_left = keys[KeyEvent.VK_LEFT];
        shift = keys[KeyEvent.VK_SHIFT];
        spaceBar = keys[KeyEvent.VK_SPACE];
        exit = keys[KeyEvent.VK_ESCAPE];
        G = keys[KeyEvent.VK_G];
        Ctrl = keys[KeyEvent.VK_CONTROL];
        E = keys[KeyEvent.VK_E];
        Q = keys[KeyEvent.VK_Q];
        W = keys[KeyEvent.VK_W];
        S = keys[KeyEvent.VK_S];
        A = keys[KeyEvent.VK_A];
        D = keys[KeyEvent.VK_D];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e){
    }
    
    public boolean getUp(){
        return this.up;
    }
    
    public boolean getDown(){
        return this.down;
    }
    
    public boolean getLeft(){
        return this.left;
    }
    
    public boolean getRight(){
        return this.right;
    }
    
    public boolean getExit(){
        return this.exit;
    }
    
    public boolean getArrowUp(){
        return this.arrow_up;
    }
    
    public boolean getArrowDown(){
        return this.arrow_down;
    }
    
    public boolean getArrowLeft(){
        return this.arrow_left;
    }
    
    public boolean getArrowRight(){
        return this.arrow_right;
    }
    
    public boolean getShift(){
        return this.shift;
    }
    
    public boolean getSpaceBar(){
        return this.spaceBar;
    }
    
    public boolean getG(){
        return this.G;
    }
    
    public boolean getCtrl(){
        return this.Ctrl;
    }
    
    public boolean getQ(){
        return this.Q;
    }
    
    public boolean getE(){
        return this.E;
    }
    
    public boolean getW(){
        return this.W;
    }
    
    public boolean getS(){
        return this.S;
    }
    
    public boolean getA(){
        return this.A;
    }
    
    public boolean getD(){
        return this.D;
    }
}
