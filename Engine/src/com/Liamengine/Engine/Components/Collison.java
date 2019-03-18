/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Components;

import com.Liamengine.Engine.AbstractClasses.IDrawable;

/**
 * this was my inital plan for doing collisions with 
 * i did end up implementing some collision functions 
 * like that of line intersection but not much else this 
 * is just keep sake for later but still used for non 
 * swift collision
 * 
 * 
 * @author Liam Woolley 1748910
 */
public class Collison {

    /**
     * the place it was hit
     */
    public final Vector HITLOCATION;
    /**
     * obejct it was hit
     */
    public final IDrawable OBJECTHIT;

    /**
     * simple way of saying it has a collision inside
     */
    public final boolean ISHIT;

    /**
     *
     * @param obj the obejct it collided with
     * @param location where it collided with
     * @param ishit whether or not it had collided
     */
    public Collison(IDrawable obj, Vector location, boolean ishit) {
        ISHIT = ishit;
        HITLOCATION = location;
        OBJECTHIT = obj;
    }

    /**
     * this is used if nothing had collided with it
     */
    public Collison() {
        ISHIT = false;
        HITLOCATION = Vector.Zero();
        OBJECTHIT = null;
    }

    /**
     * if it can pass in these values then it must have collided with something
     * @param obj the obejct it collided with
     * @param location where it collided with
     */
    public Collison(IDrawable obj, Vector location) {
        ISHIT = true;
        HITLOCATION = location;
        OBJECTHIT = obj;
    }

    /**
     *
     * @param location where it collided with
     */
    public Collison(Vector location) {
        ISHIT = true;
        HITLOCATION = location;
        OBJECTHIT = null;
    }

}
