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
    public boolean arrow_up, arrow_down, arrow_left, arrow_right, shift;
    
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
}
