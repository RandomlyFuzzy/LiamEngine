/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Components;


/**
 *
 * @author Liam Woolley 1748910
 */
public class Vector {

    public static Vector Zero() {
        return new Vector(0, 0);
    }

    public static Vector One() {
        return new Vector(1, 1);
    }
    private float x, y;

    public Vector(float x, float y) {
        if (!isNaN(x, y)) {
            this.x = x;
            this.y = y;
        } else {
            this.x = 0;
            this.y = 0;
        }
    }

    public Vector(float angle) {
        this.x = (float) Math.sin(angle);
        this.y = (float) -Math.cos(angle);
    }

    public Vector DirectionalVector(float angle, float Magnitude) {
        Vector v = new Vector(angle).mult(Magnitude);
        return v;
    }

    public boolean isNaN(Vector v) {
        return Float.isNaN(v.getX()) || Float.isNaN(v.getY());
    }

    public boolean isNaN(float x, float y) {
        return Float.isNaN(x) || Float.isNaN(y);
    }

    public Vector(Vector v) {
        if (!isNaN(v)) {
            x = v.getX();
            y = v.getY();
        }
        v = null;
    }

    public Vector add(Vector v) {
        this.x += v.getX();
        this.y += v.getY();
        v = null;
        return this;
    }

    public Vector addto(Vector v) {
        Vector v2 = new Vector(this);
        v2.x += v.getX();
        v2.y += v.getY();
        v = null;
        return v2;
    }

    public Vector mult(Vector v) {
        this.x *= v.getX();
        this.y *= v.getY();
        v = null;
        return this;
    }

    public Vector mult(float v) {
        this.x *= v;
        this.y *= v;
        return this;
    }

    public void setToVector(Vector v) {
        x = v.getX();
        y = v.getY();
        v = null;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void addY(float y) {
        this.y += y;
    }

    public void addX(float x) {
        this.x += x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String toString() {
        return "" + this.x + "," + this.y;
    }

    public double Length() {
        return (getX() * getX()) + (getY() * getY());
    }

    public double Lengthsqrt() {
        return Math.sqrt((getX() * getX()) + (getY() * getY()));
    }

    public Vector Normalized() {
        double hypot = Lengthsqrt();
        double X = (getX()) / hypot;
        double Y = (getY()) / hypot;
        //needs the conversion else small but relivant inaccuracy
        return new Vector((float) X, (float) Y);
    }
}
