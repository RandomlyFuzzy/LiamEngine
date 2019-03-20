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
import com.Liamengine.Engine.Components.SpriteSheet;
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
 * this is the most utilize class it has most of the functionality and
 * accessibility to the rest(things that might be needed in the ILevel classes)
 * it also can utilize another object IComponentss which can be see as attached
 * to the this object
 *
 * @see com.Liamengine.Engine.Componentss.Transform
 *
 * @author Liam Woolley 1748910
 */
public abstract class IDrawable {

    /**
     * basic check to see if the object has been supered can cause some errors
     * if not
     */
    private boolean hasSupered = false;

    /**
     * position utilised by the transform to translate the object its also the
     * what the bounding box will read from to get the correct values
     */
    private Vector position;
    /**
     * offset from the position utilized by the transform to translate the
     * object further than the position but still utilized in the bounding box
     */
    private Vector Toffset = Vector.Zero();

    /**
     * currently loaded image width
     */
    private int spriteWidth = 0;
    /**
     * currently loaded image height
     */
    private int spriteHeight = 0;
    /**
     * rotation utilized by the transform to rotate the object along the Z Plane
     * its also the what the bounding box will read from to get the correct
     * values
     */
    private double Rotation = 0;
    /**
     * offset from the rotation utilized by the transform to rotate the object
     * further than the rotation but still utilized in the bounding box
     */
    private double offset = 0;
    /**
     * Scale utilised by the transform to scale the object along the x,y Planes
     * its also the what the bounding box will read from to get the correct
     * values
     */
    private Vector Scale = Vector.One();
    /**
     * will enable or disable the object respectively please note that it stops
     * rendering and turn off the collisions updating
     */
    private boolean Enabled = true;
    /**
     * boolean set if is colliding with anything
     *
     * @see ILevel#checkCollionsions
     * @see ILevel#paintComponents
     */
    private boolean isColliding = false;
    /**
     * boolean to make it able to collide with other IDrawable objects
     *
     * @see ILevel#checkCollionsions
     */
    private boolean isCollidable = true;
    /**
     * boolean to make it utilises the transformations natively
     *
     * @see IDrawable#CoreUpdate
     */
    private boolean useTransforms = true;
    /**
     * last loaded image note can be set with
     *
     * @see IDrawable#setLastImage
     *
     * but also set when loading a sprite too
     * @see IDrawable#GetSprite
     */
    private BufferedImage LastImage = null;
    /**
     * last loaded image address note will not be unset if using
     *
     * @see IDrawable#setLastImage
     *
     * but will be set if using the
     * @see IDrawable#GetSprite
     */
    private String LastimageAddress = "";
    /**
     * set when the object is created
     *
     * @see #IDrawable
     */
    private String LoadedLevel = "";
    /**
     * this is the thing that does the transformations for this class it also
     * scales according to the world scale
     *
     * @see com.Liamengine.Engine.Entry.Game#CalculateDims
     * @see com.Liamengine.Engine.Entry.Game#WorldScale
     */
    private Transform transform;

    /**
     * all 4 vertexes for the polygon collisions
     *
     * @see IDrawable#UpdateBounds
     * @see IDrawable#getBounds
     * @see IDrawable#checkForIntersections
     * @see IDrawable#sideUp
     */
    private Vector vert1;
    private Vector vert2;
    private Vector vert3;
    private Vector vert4;

    /**
     * for storing all the Components created on this object type
     *
     * @see IDrawable#AddComponents
     * @see IDrawable#CoreUpdate
     */
    private ArrayList<IComponent> Components = new ArrayList<IComponent>();

    /**
     * main constuctor of the class sets the initial position transform object
     * and the level it was in
     */
    public IDrawable() {
        transform = new Transform(this);
        position = new Vector(0, 0);
//        AddComponents(transform);
        hasSupered = true;
        LoadedLevel = Game.GetLevel().getClass().toString();
    }

    /**
     *
     * @return gets the positions offset not the position
     */
    public Vector getPoffset() {
        return Toffset;
    }

    /**
     *
     * @return gets the last loaded image address note it can be null if no las
     * image
     */
    public String getLastimageAddress() {
        return LastimageAddress;
    }

    /**
     *
     * @param img image you want to set as the currently utilised image also
     * updates the bounds of colliding
     */
    public void setLastimage(BufferedImage img) {
        LastImage = img;
        LastimageAddress = "";
        this.spriteWidth = LastImage.getWidth();
        this.spriteHeight = LastImage.getHeight();
        UpdateBounds();
    }

    /**
     *
     * @param Toffset sets object Toffset to that value
     */
    public void setPoffset(Vector Toffset) {
        this.Toffset = Toffset;
    }

    /**
     *
     * @param spriteWidth sets object spriteWidth to that value
     */
    public void setSpriteWidth(int spriteWidth) {
        this.spriteWidth = spriteWidth;
        UpdateBounds();
    }

    /**
     *
     * @param spriteHeight sets object spriteHeight to that value
     */
    public void setSpriteHeight(int spriteHeight) {
        this.spriteHeight = spriteHeight;
        UpdateBounds();
    }

    /**
     *
     * @return the variable IsCollidable 
     * 
     */
    public boolean IsCollidable() {
        return isCollidable;
    }

    /**
     * used to stop collisions if graphical or background functionality is
     * needed
     *
     * @param isCollidable sets object isCollidable to that value
     */
    public void setIsCollidable(boolean isCollidable) {
        this.isCollidable = isCollidable;
    }

    /**
     *
     * @return local useTransforms variable
     */
    public boolean canUseTransforms() {
        return useTransforms;
    }

    /**
     *
     * @param useTransforms sets object useTransforms to that value
     */
    public void UseTransforms(boolean useTransforms) {
        this.useTransforms = useTransforms;
    }

    /**
     *
     * @return the rotation of the object Rotation note doesn't add the offset
     * rotation
     * @see IDrawable#getOffset
     */
    public double getRotation() {
        return Rotation;
    }

    /**
     *
     * @return gets the core object scale with out the world scale taken into
     * account
     */
    public Vector getScale() {
        return Scale;
    }
    /**
     *
     * @param Rotation sets object Rotation to that value
     *
     */
    public void setRotation(double Rotation) {
        this.Rotation = Rotation;
        UpdateBounds();
    }

    /**
     *
     * @param Scale sets object Scale to that value
     */
    public void setScale(Vector Scale) {
        this.Scale = Scale;
        Scale = null;
        UpdateBounds();
    }

    /**
     *
     * @return whether or not is enabled
     */
    public boolean isEnabled() {
        return Enabled;
    }

    /**
     *
     * @param Enabled sets object Scale to that value
     */
    public void setEnabled(boolean Enabled) {
        this.Enabled = Enabled;
    }

    /**
     *
     * @param comp adds an object to the current objects Components Collection
     * @see IDrawable#Components
     */
    public void AddComponents(IComponent comp) {
        Components.add(comp);
        comp.Init();
    }

    /**
     * wrapping function to get the current Level from game used a lot so this is
     * easier
     *
     * @return the Current Level
     */
    public ILevel Level() {
        return Game.GetLevel();
    }

    /**
     * Wrapper for the getting the upwards vector using polar to cartesian
     * coordinates math#
     *
     * @return the relative up vector of the object
     */
    public Vector GetUp() {
        return transform.GetUp();
    }

    /**
     * Wrapper for the getting the upwards vector using polar to cartesian
     * coordinates math
     *
     * @return the relative right vector of the object
     */
    public Vector GetRight() {
        return transform.GetRight();
    }

    /**
     *
     * @return gets the total rotation with offset and base rotation
     */
    public double getTotalRotation() {
        return getRotation() + getOffset();
    }

    /**
     *
     * updates the bounds of the collision box uses trigonometry (tan) with the
     * relative sprite width and height (x,y) then scales by the hypotinuse then
     * converted from polar to cartesian coordinates and adds the offset and
     * position
     *
     * it is used whenever it is moved scaled or rotated or the image is changed
     *
     * @see IDrawable#setPosition
     * @see IDrawable#setPoffset
     * @see IDrawable#setRotation
     * @see IDrawable#setScale
     * @see IDrawable#setImage
     * @see IDrawable#GetSprite
     * @see IDrawable#setLastImage
     */
    public synchronized void UpdateBounds() {
        float sw = getScaledSpriteWidth(), sh = getScaledSpriteHeight();
        double tr = getTotalRotation();
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

    /**
     *
     *
     * @return the bounds of the object in a polygon object as a rectangle
     */
    public Polygon getBounds() {
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

    /**
     *
     * @param g object to check against
     * @return whether or not it is colliding with the object g
     */
    public boolean checkForIntersections(Polygon g) {
        //error checking just in case
        if (vert1 == null || vert2 == null || vert3 == null || vert4 == null) {
            //will update bounds otherwise
            UpdateBounds();
        }
        return g.contains(vert1.getX(), vert1.getY()) || g.contains(vert2.getX(), vert2.getY()) || g.contains(vert3.getX(), vert3.getY()) || g.contains(vert4.getX(), vert4.getY());
    }

    /**
     * check for a collision between this object and another (t);
     *
     * @param t the object to check for a collision with
     * @return whether or not it collided with that object
     */
    public boolean CheckCollions(IDrawable t) {
        if (checkForIntersections(t.getBounds())) {
//            System.out.println("com.FuturePixels.Utils.IDrawable.CheckCollions()");
            if (t.isEnabled() == true) {
            //#################################################update and remove this :/######################################################################
                onCollison(t);
                return true;
            }
        }
        return false;
    }

    /**
     * wrapper for the image utils
     *
     * @return gets the object if it exists else the default image
     */
    BufferedImage GetImage(String URI) {
        return imageUtils.T.GetImage(URI);
    }

    /**
     * sets an image in the image util to the image presented
     *
     * @param name to save it as in the image utils
     * @param img the object to save
     */
    public void SetImage(String name, BufferedImage img) {
        imageUtils.T.setImage(name, img);
    }

    /**
     *
     * @return an instance of the position (just incase get overwritten)
     */
    public Vector getPosition() {
        return new Vector(this.position);
    }

    /**
     *
     * @param v to add to position
     */
    public void addPosition(Vector v) {
        this.setPosition(getPosition().add(v));
        UpdateBounds();
    }

    /**
     *
     * @param URI
     * @return this object (done like this can be done to this object in the
     * ILevel class afterwards)
     */
    public IDrawable GetSprite(String URI) {
        if (!LastimageAddress.equals(URI)) {
            LastImage = GetImage(URI);
            this.spriteWidth = LastImage.getWidth();
            this.spriteHeight = LastImage.getHeight();
            UpdateBounds();
            LastimageAddress = URI;
        }
        return this;
    }

    /**
     *
     * @param X x coord to be set to
     * @param Y y coord to be set to
     */
    public void setPosition(float X, float Y) {
        this.position.setX(X);
        this.position.setY(Y);
        UpdateBounds();
    }

    /**
     *
     * @param v to be set too
     */
    public void setPosition(Vector v) {
        this.position.setX(v.getX());
        this.position.setY(v.getY());
        v = null;
        UpdateBounds();
    }

    /**
     *
     * @return the sprite height
     */
    public int getSpriteHeight() {
        return spriteHeight;
    }

    /**
     *
     * @return the sprite width
     */
    public int getSpriteWidth() {
        return spriteWidth;
    }

    /**
     *
     * @return the local scaled sprite height
     */
    public float getScaledSpriteHeight() {
        return spriteHeight * getScale().getY();
    }

    /**
     *
     * @return the local scaled sprite width
     */
    public float getScaledSpriteWidth() {
        return spriteWidth * getScale().getX();
    }

    /**
     * just returns it as a string
     */
    @Override
    public String toString() {
        return getClass().toString() + " at " + getPosition().getX() + " ," + getPosition().getY();
    }

    /**
     * this is for getting components added to the class
     *
     *
     * @param <T> type you want to get
     * @param g StaticObject ( because generic functions have to have a
     * paramiter for this sort of operation in java)
     * @return the component with the same name in this object
     */
    public <T extends IComponent> T getComponents(T g) {
        T ret = null;
        for (IComponent t : Components) {
            if (t.getClass().toString().equals(g.getClass().toString())) {
                return (T) t;
            }
        }
        return ret;
    }

    /**
     * this is what happens when the object is added to level
     *
     * @see ILevel#AddObject
     */
    void CoreInit() {
        //basic check to find the buggy class as it had not suped itself in the constructor 
        if (!hasSupered) {
            //find the object from an error stack trace
            UtilManager.FindUseClass(3);
            System.err.println("you must super this ");
        }
        init();

    }

    /**
     * this is the core update to the object this is run every frame
     */
    void CoreUpdate(Graphics2D g) {
        //check to see if enabled 
        if (!isEnabled()) {
            // if not just returns from the function
            return;
        }

        //this shouldnt be here but if it is the game runs fine
        doMove();
        //pushing a transformation
        if (canUseTransforms()) {
            transform.PushTransforms(g);
        }
        //this an abtract function call
        //graphical drawing
        Update(g);
        for (int i = Components.size() - 1; i > 0; i--) {
            Components.get(i).Update(g);
        }
        //runs the object(is still a Component)
        transform.Update(g);
        //resets the variable
        setIsColliding(false);
        //pop tranfomations
        if (canUseTransforms()) {
            transform.PopTransforms(g);
        }
        //return if not in debug mode 
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
                (int) (((int) Transform.getOffsetTranslation().getX() + _left[0].getX()) * Game.WorldScale().getX()),
                (int) (((int) Transform.getOffsetTranslation().getY() + (int) _left[0].getY()) * Game.WorldScale().getY()),
                (int) (((int) Transform.getOffsetTranslation().getX() + (int) _left[1].getX()) * Game.WorldScale().getX()),
                (int) (((int) Transform.getOffsetTranslation().getY() + (int) _left[1].getY()) * Game.WorldScale().getY())
        );
        g.drawLine(
                (int) (((int) Transform.getOffsetTranslation().getX() + (int) _right[0].getX()) * Game.WorldScale().getX()),
                (int) (((int) Transform.getOffsetTranslation().getY() + (int) _right[0].getY()) * Game.WorldScale().getY()),
                (int) (((int) Transform.getOffsetTranslation().getX() + (int) _right[1].getX()) * Game.WorldScale().getX()),
                (int) (((int) Transform.getOffsetTranslation().getY() + (int) _right[1].getY()) * Game.WorldScale().getY())
        );
        g.drawLine(
                (int) (((int) Transform.getOffsetTranslation().getX() + (int) _Top[0].getX()) * Game.WorldScale().getX()),
                (int) (((int) Transform.getOffsetTranslation().getY() + (int) _Top[0].getY()) * Game.WorldScale().getY()),
                (int) (((int) Transform.getOffsetTranslation().getX() + (int) _Top[1].getX()) * Game.WorldScale().getX()),
                (int) (((int) Transform.getOffsetTranslation().getY() + (int) _Top[1].getY()) * Game.WorldScale().getY())
        );
        g.drawLine(
                (int) (((int) Transform.getOffsetTranslation().getX() + (int) _down[0].getX()) * Game.WorldScale().getX()),
                (int) (((int) Transform.getOffsetTranslation().getY() + (int) _down[0].getY()) * Game.WorldScale().getY()),
                (int) (((int) Transform.getOffsetTranslation().getX() + (int) _down[1].getX()) * Game.WorldScale().getX()),
                (int) (((int) Transform.getOffsetTranslation().getY() + (int) _down[1].getY()) * Game.WorldScale().getY())
        );
    }

    /**
     * draws the last loaded image centered to the position with the offset
     * taken into account
     *
     * @param g the graphical context to draw to
     */
    public synchronized void DrawLastLoadedImage(Graphics2D g) {
        if (LastImage == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                System.err.println("error Drawing last image as their was not last image in " + e.getStackTrace()[1] + " try pre loading it in init() to get rid of this warning");
            }
        } else {
                g.drawImage(LastImage, (int) Toffset.getX() + (int) -(getSpriteWidth()) / 2, (int) Toffset.getY() + (int) -(getSpriteHeight()) / 2, (int) (getSpriteWidth()), (int) (getSpriteHeight()), null);
        }
    }

    /**
     * draws the last loaded image as a sprite sheet
     *
     * @param g the graphical context to draw to
     * @see #SpriteSheet
     */
    public synchronized void DrawLastLoadedImageAsSpriteSheet(Graphics2D g, SpriteSheet she) {
        if (LastImage == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                System.err.println("error Drawing last image as their was not last image in " + e.getStackTrace()[1] + " try pre loading it in init() to get rid of this warning");
            }
        } else {
            setSpriteWidth(she.GetSegWidth());
            setSpriteHeight(she.GetSegHeight());
            she.DrawFromGraphic(g, LastImage, (int) Toffset.getX() + (int) -(getSpriteWidth()) / 2, (int) Toffset.getY() + (int) -(getSpriteHeight()) / 2, (int) (getSpriteWidth()) / 2, (int) (getSpriteHeight()) / 2);

        }
    }

    /**
     *
     * @return the last loaded image
     */
    public BufferedImage getLastImage() {
        return LastImage;
    }

    /**
     * disposes everything need partaining to the object
     *
     * @see ILevel#dispose
     */
    public void dispose() {
        LastImage = null;
        ArrayList<IComponent> c = Components;
        Components = new ArrayList<IComponent>();
        for (int i = Components.size() - 1; i > 0; i--) {
            Components.remove(i);
        }
        c.forEach((a) -> {
            a.dispose();
        });
        this.isColliding = false;
    }

    /**
     *
     * @return this gets the objects top Line/side as Vectors [0] start [1] end
     * reliative to world not each other or the postition of the object
     */
    public Vector[] sideUp() {
        if (vert4 == null || vert1 == null) {
            UpdateBounds();
        }
        return new Vector[]{vert4, vert1};

    }

    /**
     *
     * @return this gets the objects left Line/side as Vectors [0] start [1] end
     * reliative to world not each other or the postition of the object
     */
    public Vector[] sideLeft() {
        if (vert2 == null || vert1 == null) {
            UpdateBounds();
        }
        return new Vector[]{vert1, vert2};
    }

    /**
     *
     * @return this gets the objects down Line/side as Vectors [0] start [1] end
     * reliative to world not each other or the postition of the object
     */
    public Vector[] sideDown() {
        if (vert3 == null || vert2 == null) {
            UpdateBounds();
        }
        return new Vector[]{vert3, vert2};
    }

    /**
     *
     * @return this gets the objects right Line/side as Vectors [0] start [1]
     * end reliative to world not each other or the postition of the object
     */
    public Vector[] sideRight() {
        if (vert3 == null || vert4 == null) {
            UpdateBounds();
        }
        return new Vector[]{vert3, vert4};

    }

    /**
     *
     * @return the rotational offset not the rotation
     */
    public double getOffset() {
        return offset;
    }

    /**
     *
     * @param offset sets object offset to that value
     */
    public void setOffset(double offset) {
        this.offset = offset;
    }

    /**
     *
     * @return returns whether or not is colliding
     */
    public boolean isColliding() {
        return isColliding;
    }

    /**
     *
     * @param isColliding sets object isColliding to that value
     */
    public void setIsColliding(boolean isColliding) {
        this.isColliding = isColliding;
    }

    /**
     * runs once when the object is created
     *
     * @see ILevel#AddObject
     */
    public abstract void init();

    /**
     * runs every frame is used for setting up to be displayed
     *
     * @see ILevel#actionPerformed
     */
    public abstract void doMove();

    /**
     * runs every frame is used for graphical purposes
     *
     * @param g
     */
    public abstract void Update(Graphics2D g);

    /**
     * runs when the object is detected to collide with another object
     *
     * @param im the object it collides with
     */
    public abstract void onCollison(IDrawable im);
}
