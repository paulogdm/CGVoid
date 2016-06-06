/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author paulogdm
 */
public class Missile extends ObjectContainer{

    public Missile() {
        super("./data/bullet/AGM/Files/Missile AGM-65.obj");
        this.getObj().setRotation(0.8f, 0.8f,0);

        this.getObj().setSize(0.1f, 0.1f, 0.1f);
        this.getObj().setPosition(0, 0, 0.3f);
    }
}

