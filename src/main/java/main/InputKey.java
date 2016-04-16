package main;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author paulogdm
 */
public class InputKey implements KeyListener {
    private final boolean[] keys = new boolean[120];  
    public boolean up, down, left, right, exit;
        
    public void update(){
        up = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
        exit = keys[KeyEvent.VK_ESCAPE];
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
}
