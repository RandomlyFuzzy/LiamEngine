/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Components;

import java.io.Serializable;


/**
 *
 * @author Liam Woolley 1748910
 */
public class Vector implements Serializable {

    /**
     *
     * @return
     */
    public static Vector Zero() {
        return new Vector(0, 0);
    }

    /**
     *
     * @return
     */
    public static Vector One() {
        return new Vector(1, 1);
    }
    private float x, y;

    /**
     *
     * @param x
     * @param y
     */
    public Vector(float x, float y) {
        if (!isNaN(x, y)) {
            this.x = x;
            this.y = y;
        } else {
            this.x = 0;
            this.y = 0;
        }
    }

    /**
     *
     * @param angle
     */
    public Vector(float angle) {
        this.x = (float) Math.sin(angle);
        this.y = (float) -Math.cos(angle);
    }

    /**
     *
     * @param angle
     * @param Magnitude
     * @return
     */
    public Vector DirectionalVector(float angle, float Magnitude) {
        Vector v = new Vector(angle).mult(Magnitude);
        return v;
    }

    /**
     *
     * @param v
     * @return
     */
    public boolean isNaN(Vector v) {
        return Float.isNaN(v.getX()) || Float.isNaN(v.getY());
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isNaN(float x, float y) {
        return Float.isNaN(x) || Float.isNaN(y);
    }

    /**
     *
     * @param v
     */
    public Vector(Vector v) {
        if (!isNaN(v)) {
            x = v.getX();
            y = v.getY();
        }
        v = null;
    }

    /**
     *
     * @param v
     * @return
     */
    public Vector add(Vector v) {
        this.x += v.getX();
        this.y += v.getY();
        v = null;
        return this;
    }

    /**
     *
     * @param v
     * @return
     */
    public Vector addto(Vector v) {
        Vector v2 = new Vector(this);
        v2.x += v.getX();
        v2.y += v.getY();
        v = null;
        return v2;
    }

    /**
     *
     * @param v
     * @return
     */
    public Vector mult(Vector v) {
        this.x *= v.getX();
        this.y *= v.getY();
        v = null;
        return this;
    }

    /**
     *
     * @param v
     * @return
     */
    public Vector mult(float v) {
        this.x *= v;
        this.y *= v;
        return this;
    }

    /**
     *
     * @param v
     */
    public void setToVector(Vector v) {
        x = v.getX();
        y = v.getY();
        v = null;
    }

    /**
     *
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     *
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     *
     * @param y
     */
    public void addY(float y) {
        this.y += y;
    }

    /**
     *
     * @param x
     */
    public void addX(float x) {
        this.x += x;
    }

    /**
     *
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     *
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

    public String toString() {
        return "x:" + this.x + "\ny:" + this.y;
    }

    /**
     *
     * @return
     */
    public double Length() {
        return (getX() * getX()) + (getY() * getY());
    }

    /**
     *
     * @return
     */
    public double Lengthsqrt() {
        return Math.sqrt((getX() * getX()) + (getY() * getY()));
    }

    /**
     *
     * @return
     */
    public Vector Normalized() {
        double hypot = Lengthsqrt();
        double X = (getX()) / hypot;
        double Y = (getY()) / hypot;
        //needs the conversion else small but relivant inaccuracy
        return new Vector((float) X, (float) Y);
    }
}
