/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.AbstractClasses;

import com.Liamengine.Engine.Components.Vector;
import com.Liamengine.Engine.Utils.UtilManager;
import com.Liamengine.Engine.Utils.imageUtils;
import com.Liamengine.Engine.Entry.Game;
import com.Liamengine.Engine.AbstractClasses.ILevel;
import com.Liamengine.Engine.Components.Transform;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author RandomlyFuzzy
 */
public abstract class IDrawable {

    private Vector position, Toffset = Vector.Zero();

    private int spriteWidth = 0;
    private int spriteHeight = 0;
    private double Rotation = 0, offset = 0;
    private Vector Scale = Vector.One();
    private boolean Enabled = true, isColliding = false, isCollidable = true, useTransforms = true;
    private BufferedImage LastImage = null;
    private String LastimageAddress = "";

    private ArrayList<IComponent> Component = new ArrayList<IComponent>();
    private Transform transform;

    private Vector vert1,
            vert2,
            vert3,
            vert4;

    public Vector getPoffset() {
        return Toffset;
    }

    public String getLastimageAddress() {
        return LastimageAddress;
    }

    public void setPoffset(Vector Toffset) {
        this.Toffset = Toffset;
    }

    public void setSpriteWidth(int spriteWidth) {
        this.spriteWidth = spriteWidth;
    }

    public void setSpriteHeight(int spriteHeight) {
        this.spriteHeight = spriteHeight;
    }

    public boolean IsCollidable() {
        return isCollidable;
    }

    public void setIsCollidable(boolean isCollidable) {
        this.isCollidable = isCollidable;
    }

    public boolean canUseTransforms() {
        return useTransforms;
    }

    public void UseTransforms(boolean useTransforms) {
        this.useTransforms = useTransforms;
    }

    //to do
    //1. add component based logic
    //2. make collisons work
    //3. make a prototype of the game 
    //4. extend and polish that game prototype
    //5. release everything
    private boolean hasSupered = false;

    public IDrawable() {
        transform = new Transform(this);
        position = new Vector(0, 0);
//        AddComponent(transform);
        hasSupered = true;
    }

    public double getRotation() {
        return Rotation;
    }

    public Vector getScale() {
        return Scale;
    }

    public void setRotation(double Rotation) {
        this.Rotation = Rotation;
        UpdateBounds();
    }

    public void setScale(Vector Scale) {
        this.Scale = Scale;
        Scale = null;
        UpdateBounds();
    }

    public boolean isEnabled() {
        return Enabled;
    }

    public void setEnabled(boolean Enabled) {
        this.Enabled = Enabled;
    }

    public void AddComponent(IComponent comp) {
        Component.add(comp);
        comp.Init();
    }

    public ILevel Level() {
        return Game.GetLevel();
    }

    public Vector GetUp() {
        return transform.GetUp();
    }

    public Vector GetRight() {
        return transform.GetRight();
    }

    public double getTotalRotation() {
        return getRotation() + getOffset();
    }

    public synchronized void UpdateBounds() {
        float sw = getScaledSpriteWidth(), sh = getScaledSpriteHeight();
        double tr = getRotation();
        Vector Sca = new Vector(getScale()).mult(Game.WorldScale()), pos = getPosition();

        float hy = (float) Math.sqrt((sw / 2 * sw / 2) + (sh / 2 * sh / 2)),
                a1 = (float) Math.atan2(sh / 2, sw / 2),
                a2 = (float) Math.atan2(-sh / 2, sw / 2),
                a3 = (float) Math.atan2(-sh / 2, -sw / 2),
                a4 = (float) Math.atan2(sh / 2, -sw / 2);
        vert1 = new Vector((int) (pos.getX() + Toffset.getX() + (float) Math.cos(a1 - tr) * hy), (int) (pos.getY() + Toffset.getY() + (float) -Math.sin(a1 - tr) * hy));
        vert2 = new Vector((int) (pos.getX() + Toffset.getX() + (float) Math.cos(a2 - tr) * hy), (int) (pos.getY() + Toffset.getY() + (float) -Math.sin(a2 - tr) * hy));
        vert3 = new Vector((int) (pos.getX() + Toffset.getX() + (float) Math.cos(a3 - tr) * hy), (int) (pos.getY() + Toffset.getY() + (float) -Math.sin(a3 - tr) * hy));
        vert4 = new Vector((int) (pos.getX() + Toffset.getX() + (float) Math.cos(a4 - tr) * hy), (int) (pos.getY() + Toffset.getY() + (float) -Math.sin(a4 - tr) * hy));
    }

    public Polygon getBounds() {
//        Rectangle objectRect = new Rectangle(
//                        (int) (getPosition().getX() - (((spriteWidth) / 2f) * Scale.getX())),
//                        (int) (getPosition().getY() - ((spriteHeight / 2f) * Scale.getY())),
//                        (int) ((spriteWidth) * Scale.getX()),
//                        (int) ((spriteHeight) * Scale.getY()));

        Polygon g = new Polygon();

        if (vert1 == null || vert2 == null || vert3 == null || vert4 == null) {
            UpdateBounds();
        }

        g.addPoint((int) (vert1.getX()), (int) (vert1.getY()));
        g.addPoint((int) (vert2.getX()), (int) (vert2.getY()));
        g.addPoint((int) (vert3.getX()), (int) (vert3.getY()));
        g.addPoint((int) (vert4.getX()), (int) (vert4.getY()));
        g.addPoint((int) (vert1.getX()), (int) (vert1.getY()));

        return g;
    }

    BufferedImage GetImage(String URI) {
        return imageUtils.T.GetImage(URI);
    }

    public void SetImage(String name, BufferedImage img) {
        imageUtils.T.setImage(name, img);
    }

    public boolean checkForIntersections(Polygon g) {
        if (vert1 == null || vert2 == null || vert3 == null || vert4 == null) {
            UpdateBounds();
            g = getBounds();
        }
        return g.contains(vert1.getX(), vert1.getY()) || g.contains(vert2.getX(), vert2.getY()) || g.contains(vert3.getX(), vert3.getY()) || g.contains(vert4.getX(), vert4.getY());
    }

    public Vector getPosition() {
        return new Vector(this.position);
    }

    public Vector addPosition(Vector v) {
        return this.position.add(v);
    }

    public IDrawable GetSprite(String URI) {
        LastImage = GetImage(URI);
        this.spriteWidth = LastImage.getWidth();
        this.spriteHeight = LastImage.getHeight();
        UpdateBounds();
        LastimageAddress = URI;
        return this;
    }

    public void setPosition(float X, float Y) {
        this.position.setX(X);
        this.position.setY(Y);
        UpdateBounds();
    }

    public void setPosition(Vector v) {
        this.position.setX(v.getX());
        this.position.setY(v.getY());
        v = null;
        UpdateBounds();
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public float getScaledSpriteHeight() {
        return spriteHeight * getScale().getY();
    }

    public float getScaledSpriteWidth() {
        return spriteWidth * getScale().getX();
    }

    @Override
    public String toString() {
        return getClass().toString() + " at " + getPosition().getX() + " ," + getPosition().getY();
    }

    public <T extends IComponent> T getComponent(T g) {
        T ret = null;
        System.out.println("com.FuturePixels.Utils.IDrawable.getComponent() " + g.getClass().toString());
        for (IComponent t : Component) {
            if (t.getClass().toString().equals(g.getClass().toString())) {
                System.out.println("com.FuturePixels.Utils.IDrawable.getComponent()");
                return (T) t;
            }
        }
        return ret;
    }

    public boolean CheckCollions(IDrawable t) {
        if (checkForIntersections(t.getBounds())) {
//            System.out.println("com.FuturePixels.Utils.IDrawable.CheckCollions()");
            if (t.isEnabled() == true) {
                onCollison(t);
                return true;
            }
        }
        return false;
    }

    void CoreInit() {
        if (!hasSupered) {
            UtilManager.FindUseClass(3);
            System.err.println("you must super this " + (transform != null) + position.toString());
        }
        init();

    }

    void CoreUpdate(Graphics g2) {
        if (!isEnabled()) {
            return;
        }

        Graphics2D g = (Graphics2D) g2;
        doMove();
        if (canUseTransforms()) {
            transform.Update(g);
            transform.PushTransforms(g);
        }
        Update(g);
        if (canUseTransforms()) {
            transform.PopTransforms(g);
        }
        if (!Level().isDebugCollisons() || (getSpriteWidth() + getSpriteHeight() == 0)) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);

        g2d.drawLine(
                (int) ((Transform.getOffsetTranslation().getX() + (int) getPosition().getX()) * Game.WorldScale().getX()),
                (int) ((Transform.getOffsetTranslation().getY() + (int) getPosition().getY()) * Game.WorldScale().getY()),
                (int) ((Transform.getOffsetTranslation().getX() + (int) (getPosition().getX() + (GetUp().getX()) * 20)) * Game.WorldScale().getX()),
                (int) ((Transform.getOffsetTranslation().getY() + (int) (getPosition().getY() + (GetUp().getY()) * 20)) * Game.WorldScale().getX()));
        g2d.drawLine(
                (int) ((Transform.getOffsetTranslation().getX() + (int) getPosition().getX()) * Game.WorldScale().getX()),
                (int) ((Transform.getOffsetTranslation().getY() + (int) getPosition().getY()) * Game.WorldScale().getY()),
                (int) ((Transform.getOffsetTranslation().getX() + (int) (getPosition().getX() + (GetRight().getX()) * 20)) * Game.WorldScale().getX()),
                (int) ((Transform.getOffsetTranslation().getY() + (int) (getPosition().getY() + (GetRight().getY()) * 20)) * Game.WorldScale().getX()));

        Vector[] _left = sideLeft();
        Vector[] _right = sideRight();
        Vector[] _Top = sideUp();
        Vector[] _down = sideDown();
        
        g.drawLine(
                (int)(((int) Transform.getOffsetTranslation().getX() +  _left[0].getX())* Game.WorldScale().getX()),
                (int)(((int) Transform.getOffsetTranslation().getY() + (int) _left[0].getY())* Game.WorldScale().getY()),
                (int)(((int) Transform.getOffsetTranslation().getX() + (int) _left[1].getX())* Game.WorldScale().getX()),
                (int)(((int) Transform.getOffsetTranslation().getY() + (int) _left[1].getY())* Game.WorldScale().getY())
        );
        g.drawLine(
                (int)(((int) Transform.getOffsetTranslation().getX() + (int) _right[0].getX())* Game.WorldScale().getX()),
                (int)(((int) Transform.getOffsetTranslation().getY() + (int) _right[0].getY())* Game.WorldScale().getY()),
                (int)(((int) Transform.getOffsetTranslation().getX() + (int) _right[1].getX())* Game.WorldScale().getX()),
                (int)(((int) Transform.getOffsetTranslation().getY() + (int) _right[1].getY())* Game.WorldScale().getY())
        );
        g.drawLine(
                (int)(((int) Transform.getOffsetTranslation().getX() + (int) _Top[0].getX())* Game.WorldScale().getX()),
                (int)(((int) Transform.getOffsetTranslation().getY() + (int) _Top[0].getY())* Game.WorldScale().getY()),
                (int)(((int) Transform.getOffsetTranslation().getX() + (int) _Top[1].getX())* Game.WorldScale().getX()),
                (int)(((int) Transform.getOffsetTranslation().getY() + (int) _Top[1].getY())* Game.WorldScale().getY())
        );
        g.drawLine(
                (int)(((int) Transform.getOffsetTranslation().getX() + (int) _down[0].getX())* Game.WorldScale().getX()),
                (int)(((int) Transform.getOffsetTranslation().getY() + (int) _down[0].getY())* Game.WorldScale().getY()),
                (int)(((int) Transform.getOffsetTranslation().getX() + (int) _down[1].getX())* Game.WorldScale().getX()),
                (int)(((int) Transform.getOffsetTranslation().getY() + (int) _down[1].getY())* Game.WorldScale().getY())
        );

    }

    public synchronized void DrawLastLoadedImage(Graphics2D g) {
        if (LastImage == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                System.err.println("error Drawing last image as their was not last image in " + e.getStackTrace()[1] + " try pre loading it in init() to get rid of this warning");
            }
        } else {
            g.drawImage(LastImage, (int) -(getSpriteWidth()) / 2, (int) -(getSpriteHeight()) / 2, (int) (getSpriteWidth()), (int) (getSpriteHeight()), null);
        }
    }

    public BufferedImage getLastImage() {
        return LastImage;
    }

    public void dispose() {
        LastImage = null;
        ArrayList<IComponent> c = Component;
        Component = new ArrayList<IComponent>();
        for (int i = Component.size() - 1; i > 0; i--) {
            Component.remove(i);
        }
        c.forEach((a) -> {
            a.dispose();
        });
    }

    public Vector[] sideUp() {
        if (vert4 == null || vert1 == null) {
            UpdateBounds();
        }
        return new Vector[]{vert4, vert1};

    }

    public Vector[] sideLeft() {
        if (vert2 == null || vert1 == null) {
            UpdateBounds();
        }
        return new Vector[]{vert1, vert2};
    }

    public Vector[] sideDown() {
        if (vert3 == null || vert2 == null) {
            UpdateBounds();
        }
        return new Vector[]{vert3, vert2};
    }

    public Vector[] sideRight() {
        if (vert3 == null || vert4 == null) {
            UpdateBounds();
        }
        return new Vector[]{vert3, vert4};

    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public boolean isColliding() {
        return isColliding;
    }

    public void setIsColliding(boolean isColliding) {
        this.isColliding = isColliding;
    }

    public abstract void init();

    public abstract void doMove();

    public abstract void Update(Graphics2D g);

    public abstract void onCollison(IDrawable im);
}
