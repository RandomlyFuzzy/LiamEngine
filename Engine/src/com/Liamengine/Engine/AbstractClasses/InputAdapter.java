/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.AbstractClasses;

import java.awt.event.*;

/**
 *
 * @author RandomlyFuzzy
 */
public abstract class InputAdapter implements KeyListener, MouseListener, MouseMotionListener {

    @Override
    public abstract void keyTyped(KeyEvent e);

    public abstract void keyPressed(KeyEvent e);

    public abstract void keyReleased(KeyEvent e);

    @Override
    public abstract void mouseClicked(MouseEvent e);

    public abstract void mousePressed(MouseEvent e);

    public abstract void mouseReleased(MouseEvent e);

    public abstract void mouseEntered(MouseEvent e);

    public abstract void mouseExited(MouseEvent e);

    public abstract void mouseDragged(MouseEvent e);

    public abstract void mouseMoved(MouseEvent e);
}
