/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Components;

import com.Liamengine.Engine.AbstractClasses.IDrawable;

/**
 *
 * @author RandomlyFuzzy
 */
public class Collison {

    public final Vector hitLocation;
    IDrawable ObjectHit;
    public final boolean IsHit;

    public Collison(IDrawable obj, Vector location, boolean ishit) {
        IsHit = ishit;
        hitLocation = location;
        ObjectHit = obj;
    }

    public Collison() {
        IsHit = false;
        hitLocation = Vector.Zero();
    }

    public Collison(IDrawable obj, Vector location) {
        IsHit = true;
        hitLocation = location;
        ObjectHit = obj;
    }

    public Collison(Vector location) {
        IsHit = true;
        hitLocation = location;
    }

    void setObjectHit(IDrawable ObjectHit) {
        this.ObjectHit = ObjectHit;
    }

    public IDrawable getObjectHit() {
        return ObjectHit;
    }
}
