/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.AbstractClasses;

import java.awt.event.*;

/**
 * this is used for getting all the usful user actions in one class that can be extended from 
 * the dev can also implement other ones in ILevel objects just in case
 * @author Liam Woolley 1748910
 */
public abstract class InputAdapter implements KeyListener, MouseListener, MouseMotionListener {

    /**
     * key pressed this happens
     */
    public abstract void keyPressed(KeyEvent e);
    /**
     * key released this happens
     */
    public abstract void keyReleased(KeyEvent e);
    /**
     * OS mouse pressed this happens
     */
    public abstract void mousePressed(MouseEvent e);
    /**
     * OS mouse released this happens
     */
    public abstract void mouseReleased(MouseEvent e);
    /**
     * OS mouse enters the JPanel this happens
     */
    public abstract void mouseEntered(MouseEvent e);
    /**
     * OS mouse exits the JPanel this happens
     */
    public abstract void mouseExited(MouseEvent e);
    /**
     * OS mouse clicks and moves in the JPanel this happens
     */
    public abstract void mouseDragged(MouseEvent e);
    /**
     * OS mouse moves in the JPanel this happens
     */
    public abstract void mouseMoved(MouseEvent e);

    
    //not really used so not created when it is extended from 
    @Override
    public void keyTyped(KeyEvent e){}
    @Override
    public void mouseClicked(MouseEvent e){}
}
