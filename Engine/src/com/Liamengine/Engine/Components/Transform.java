/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Components;

import com.Liamengine.Engine.AbstractClasses.IComponent;
import com.Liamengine.Engine.AbstractClasses.IDrawable;
import com.Liamengine.Engine.Entry.Game;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * this is what transforms all the IDrawable objects in the game
 * does it through remebering the old transformations 
 * doing the tranformation
 * reseting to old transforms
 * 
 * @author Liam Woolley 1748910
 */
public class Transform extends IComponent {

    private AffineTransform old;

    /**
     *  the postition of the object
     */
    public Vector Translation = Vector.Zero();

    /**
     * the scale of the object
     */
    public Vector Scale = Vector.One();

    /**
     * the rotation of the obejct
     */
    public double RotationZ = 0;
    /**
     * this is used for the Camera like movement its a universal translation of everything
     */
    private static Vector offsetTranslation = Vector.Zero();
    /**
     * universal world scale 
     * @see Game#CalculateDims
     * @see Game#WorldScale
     */
    private static Vector WorldScale = new Vector(1, 1);

    /**
     *
     * @return gets the current camera translation
     */
    public static Vector getOffsetTranslation() {
        return offsetTranslation;
    }

    /**
     *
     * this should be only changed once per frame optimally
     * @param offsetTranslation sets the current camera translation
     */
    public static synchronized void setOffsetTranslation(Vector offsetTranslation) {
        Transform.offsetTranslation = offsetTranslation;
        offsetTranslation = null;
    }

    /**
     *
     * @param parent the object it was create from
     */
    public Transform(IDrawable parent) {
        super(parent);
    }

    /**
     * not in use 
     */
    @Override
    public void Init() {}
    @Override
    public void Update(Graphics2D g) {}

    /**
     *
     * @param g the graphical context to push the transfomations too
     */
    public void PushTransforms(Graphics2D g) {
        Scale = getParent().getScale();
        Translation = getParent().getPosition().add(new Vector(getParent().getPoffset()));
        RotationZ = getParent().getRotation();
        WorldScale = Game.WorldScale();

        old = g.getTransform();
        g.translate(
            ((((int) Translation.getX())) + offsetTranslation.getX())*WorldScale.getX(), 
            ((((int) Translation.getY())) + offsetTranslation.getY())*WorldScale.getY());
        g.rotate((RotationZ));
        g.scale(Scale.getX() * WorldScale.getX(), Scale.getY() * WorldScale.getY());
        Scale = null;
        Translation = null;
    }

    /**
     * 
     * @param g the graphical context to reset
     */
    public void PopTransforms(Graphics2D g) {
        g.setTransform(old);
        old = null;
    }


    
    /**
     *
     * @return the world scales x component
     * @see Game#WorldScale
     */
    public static float GetWorldScaleX() {
        return WorldScale.getX();
    }

    /**
     *
     * @return the world scales y component
     * @see Game#WorldScale
     */
    public static float GetWorldScaleY() {
        return WorldScale.getY();
    }


    /**
     *
     * @return the relative upwards vector in a noramlized form (x+y = 1)
     */
    public Vector GetUp() {
        return new Vector((float) Math.sin(RotationZ), (float) -Math.cos(RotationZ));
    }

       /**
     *
     * @return the relative rightwards vector in a noramlized form (x+y = 1)
     */
    public Vector GetRight() {
        return new Vector((float) Math.sin(RotationZ + Math.PI / 2), (float) -Math.cos(RotationZ + Math.PI / 2));
    }

}
