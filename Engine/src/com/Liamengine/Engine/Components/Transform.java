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
 *
 * @author Liam Woolley 1748910
 */
public class Transform extends IComponent {

    private AffineTransform old;

    /**
     *
     */
    public Vector Translation = Vector.Zero();

    /**
     *
     */
    public Vector Scale = Vector.One();

    /**
     *
     */
    public double RotationZ = 0;
    private static Vector offsetTranslation = Vector.Zero();
    private static Vector WorldScale = new Vector(1, 1);

    /**
     *
     * @return
     */
    public static Vector getOffsetTranslation() {
        return offsetTranslation;
    }

    /**
     *
     * @param offsetTranslation
     */
    public static void setOffsetTranslation(Vector offsetTranslation) {
        Transform.offsetTranslation = offsetTranslation;
        offsetTranslation = null;
    }

    /**
     *
     * @param parent
     */
    public Transform(IDrawable parent) {
        super(parent);
    }

    /**
     *
     */
    @Override
    public void Init() {
        Scale = getParent().getScale();
        Translation = getParent().getPosition();
        RotationZ = getParent().getRotation();
    }

    /**
     *
     * @param g
     */
    @Override
    public void Update(Graphics2D g) {

    }

    /**
     *
     * @param g
     */
    public void PushTransforms(Graphics2D g) {
        Scale = getParent().getScale();
        Translation = getParent().getPosition();
        RotationZ = getParent().getRotation();
        WorldScale = Game.WorldScale();

        old = g.getTransform();
        g.translate(((((int) Translation.getX())) + offsetTranslation.getX())*WorldScale.getX(), ((((int) Translation.getY())) + offsetTranslation.getY())*WorldScale.getX());
        g.rotate((RotationZ) + getParent().getOffset());
        g.scale(Scale.getX() * WorldScale.getX(), Scale.getY() * WorldScale.getY());
        Scale = null;
        Translation = null;
    }

    /**
     *
     * @return
     */
    public static float GetWorldScaleX() {
        return WorldScale.getX();
    }

    /**
     *
     * @return
     */
    public static float GetWorldScaleY() {
        return WorldScale.getY();
    }

    /**
     *
     * @param g
     */
    public void PopTransforms(Graphics2D g) {
        g.setTransform(old);
        old = null;
    }

    /**
     *
     * @return
     */
    public Vector GetUp() {
        return new Vector((float) Math.sin(RotationZ), (float) -Math.cos(RotationZ));
    }

    /**
     *
     * @return
     */
    public Vector GetRight() {
        return new Vector((float) Math.sin(RotationZ + Math.PI / 2), (float) -Math.cos(RotationZ + Math.PI / 2));
    }

}
