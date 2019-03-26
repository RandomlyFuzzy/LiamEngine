/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.AbstractClasses;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * This can be used to extend the functionality of 
 * the IDrawable class to add certain functionality
 * that would normally need copy of the object it. 
 * This functions under the IDrawable class and can be 
 * made very complex IDrawable objects
 * 
 * @author Liam Woolley 1748910
 */
public abstract class IComponent {


    /**
     * this is the object that it was added to
    */
    private IDrawable Parent;

    /**
     *
     * basic contructor need for core function functionality
     * @param parent
     */
    public IComponent(IDrawable parent) {
        Parent = parent;
    }

    /**
     * the parent of the this
     * @return 
     */
    public IDrawable getParent() {
        if(Parent == null){
            dispose();
            return null;
        }
        return Parent;
    }

    /**
     * abstract function for the extended classes of this
     * it runs when the object is added to an idrawabel
     * @see IDrawable.AddComponent
     */
    public abstract void Init();

    /**
     * happens once per frame, the parmiter is the main 
     * graphical context that is being writen too
     * 
     * @see com.Liamengine.Engine.AbstractClasses.IDrawable#CoreUpdate
     *
     * @param g
     */

    public abstract void Update(Graphics2D g);


    /**
     * public function that runs when the level 
     * is unloaded intended to be overridden and supered to utilise  
     *
     * @see com.Liamengine.Engine.AbstractClasses.IDrawable#dispose
    */
    public void dispose() {
        Parent = null;
    }

}
