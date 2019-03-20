/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Components;

import java.io.Serializable;


/**
 * this is the most used class in the project it has certain 
 * functions that can be chained to give more flexabity towards 
 * minipulating the object
 * 
 * 
 * @author Liam Woolley 1748910
 */
public class Vector implements Serializable {

    /**
     *
     * @return the a vector with x and y equal to 0
     */
    public static Vector Zero() {
        return new Vector(0, 0);
    }

    /**
     *
     * @return @return the a vector with x and y equal to 1
     */
    public static Vector One() {
        return new Vector(1, 1);
    }
    private float x, y;

    /**
     *
     * @param x x value to set to the relative value
     * @param y y value to set to the relative value
     */
    public Vector(float x, float y) {
        //check to see if the 
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
     * @param v vector to set this to
     */
    public Vector(Vector v) {
        this(v.getX(),v.getY());
        v = null;
    }
    /**
     *
     * @param angle relative angle to be set to
     */
    public Vector(float angle) {
        //polar to cartesian coordinates math
        this.x = (float) Math.sin(angle);
        this.y = (float) -Math.cos(angle);
    }

    /**
     *
     * @param angle relative angle to be set to
     * @param Magnitude multiple of the vector
     * ,
     * @return
     */
    public Vector DirectionalVector(float angle, float Magnitude) {
        Vector v = new Vector(angle).mult(Magnitude);
        return v;
    }

    /**
     *
     * @param v to check if isNaN
     * @return true if either isNaN
     */
    public boolean isNaN(Vector v) {
        return Float.isNaN(v.getX()) || Float.isNaN(v.getY());
    }

    /**
     *
     * @param x x component
     * @param y y component
     * @return true if either isNaN
     */
    public boolean isNaN(float x, float y) {
        return Float.isNaN(x) || Float.isNaN(y);
    }

    /**
     *
     * @param v to add oftern just a new Vector
     * @return
     */
    public Vector add(Vector v) {
        this.x += v.getX();
        this.y += v.getY();
        // help the GC to collect it
        v = null;
        return this;
    }

    /**
     *
     * @param v vector to add to this as an instanceof
     * @return this vector add v
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
     * @param v to mult the X and Y component by
     * @return this vector's components multiply by the relative vector v's components
     */
    public Vector mult(Vector v) {
        this.x *= v.getX();
        this.y *= v.getY();
        v = null;
        return this;
    }

    /**
     *
     * @param val multiple
     * @return this vector multiplied by val on x and y component
     */
    public Vector mult(float val) {
        this.x *= val;
        this.y *= val;
        return this;
    }

    /**
     *
     * @param v to set this vector to
     */
    public void setToVector(Vector v) {
        x = v.getX();
        y = v.getY();
        v = null;
    }

    /**
     *
     * @return the X Component
     */
    public float getX() {
        return x;
    }

    /**
     *
     * @param x x to set the current X to
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     *
     * @param y y to set the current Y to
     */
    public void addY(float y) {
        this.y += y;
    }

    /**
     *
     * @param x x val to add to current X component
     */
    public void addX(float x) {
        this.x += x;
    }

    /**
     *
     * @return the current y component
     */
    public float getY() {
        return y;
    }

    /**
     *
     * @param y y to set the current y component to 
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return returns it as a JSON object
     */
    public String toString() {
        return "{x:" + this.x + ",\ny:" + this.y+"}";
    }

    /**
     *
     * @return the magnitude of the object un-squarerooted
     */
    public double Length() {
        return (getX() * getX()) + (getY() * getY());
    }

    /**
     *
     * @return the magnitude of the object squarerooted
     */
    public double Lengthsqrt() {
        return Math.sqrt((getX() * getX()) + (getY() * getY()));
    }

    /**
     *
     * @return the vector normalized i.e. X+Y = 1
     */
    public Vector Normalized() {
        double hypot = Lengthsqrt();
        double X = (getX()) / hypot;
        double Y = (getY()) / hypot;
        //needs the conversion else small but relivant inaccuracy
        return new Vector((float) X, (float) Y);
    }
}
