/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.AbstractClasses;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author RandomlyFuzzy
 */
public abstract class IComponent {

    private IDrawable Parent;

    public IComponent(IDrawable parent) {
        Parent = parent;
    }

    public IDrawable getParent() {
        if(Parent == null){
            dispose();
            return null;
        }
        return Parent;
    }

    public abstract void Init();

    public abstract void Update(Graphics2D g);

    void dispose() {
        Parent = null;
    }

}
