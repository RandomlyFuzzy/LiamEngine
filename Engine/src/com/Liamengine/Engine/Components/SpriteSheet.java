/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Liam Woolley 1748910
 */
public class SpriteSheet {

    /**
     * maximum currentX can be
     */
    private int MaxX = 1;
    /**
     * maximum currentY can be
     */
    private int MaxY = 1;
    /**
     * width of the image this object will be using 
     */
    private int ImageWidth = 1;
    /**
     * height of the image this object will be using 
     */
    private int ImageHeight = 1;
    /**
     * current xpostion of the sprite to render / SegWidth
     */
    private float CurrentX = 1;
    /**
     * current ypostion of the sprite to render / SegHeight
     */
    private float CurrentY = 1;
    /**
     * width of the sprite thaT should be rendered
     */
    private int SegWidth = 1;
    /**
     * width of the sprite that should be rendered
     */
    private int SegHeight = 1;


    /**
     * this is to be used if you know the image you want to use beforehand
     * @param segsx starting CurrentX
     * @param segsy starting CurrentY
     * @param segwidth setting the SegWidth to this value
     * @param segheight setting the SegHeight to this value
     * @param image image to use to calculate interal paramiters
     */
    public SpriteSheet(int segsx, int segsy, int segwidth, int segheight, BufferedImage image) {
        this(segsx, segsy, segwidth, segheight);
        inputImage(image);
    }

    /**
     * this can be used in congunction with inputimage to have the same effect as the first costructor
     * @param segsx starting CurrentX
     * @param segsy starting CurrentY
     * @param segwidth setting the SegWidth to this value
     * @param segheight setting the SegHeight to this value
     */
    public SpriteSheet(int segx, int segy, int segwidth, int segheight) {
        this.CurrentX = (segx);
        this.CurrentY = (segy);
        SegWidth = segwidth;
        SegHeight = segheight;
    }

    /**
     *
     * @param image you want to calculate bounds from
     * note if not set then can either the sprite renderpostition or create an error
     */
    public void inputImage(BufferedImage image) {
        ImageWidth = image.getWidth();
        ImageHeight = image.getHeight();
        MaxY = (ImageHeight / SegHeight);
        MaxX = (ImageWidth / SegWidth);
    }

    /**
     * Vector based incremnt
     * @param x float to increment x by
     * @param y float to increment y by
     * note only uses the currentX/currentY as an int to get the postion
     */
    public void increment(float x, float y) {
        IncrementX(x);
        IncrementY(y);
    }

    /**
     *
     * @param amt float to add to CurrentX
     */
    public void IncrementX(float amt) {
        CurrentX += amt;
        //Check to see if greater or equal to the maxXpos
        if (CurrentX >= MaxX) {
            //limit it to the modulas of the max
            CurrentX %= MaxX;
        }
    }

    /**
     *
     * @param amt float to add to CurrentX
     */
    public void IncrementY(float amt) {
        CurrentY += amt;
        //Check to see if greater or equal to the maxXpos
        if (CurrentY >= MaxY) {
            //limit it to the modulas of the max
            CurrentY %= MaxY;
        }
    }

    /**
     *
     * @param g graphcial context
     * @param img buffered image to render
     * @param x x pos to draw to
     * @param y y pos to draw to
     * @param width width to be rendered at
     * @param height height to be rendered at
     * this is oftern used in {@link IDrawable#DrawLastLoadedImageAsSpriteSheet}
     * @see IDrawable#DrawLastLoadedImageAsSpriteSheet
     */
    public void DrawFromGraphic(Graphics2D g, BufferedImage img, int x, int y, int width, int height) {
        inputImage(img);
        g.drawImage(img, x, y, width, height,GetXpos(), GetYpos(), GetXpos() + GetSegWidth(), GetYpos() + GetSegHeight(), null);
    }

//#############################the rest are getters and setters#####################################################################################3

    /**
     *
     * @return
     */
    public float getCurrentX() {
        return CurrentX;
    }

    /**
     *
     * @param CurrentX
     */
    public void setCurrentX(float CurrentX) {
        this.CurrentX = CurrentX;
    }

    /**
     *
     * @return
     */
    public float getCurrentY() {
        return CurrentY;
    }

    /**
     *
     * @param CurrentY
     */
    public void setCurrentY(float CurrentY) {
        this.CurrentY = CurrentY;
    }

    /**
     *
     * @return
     */
    public int getSegWidth() {
        return SegWidth;
    }

    /**
     *
     * @param SegWidth
     */
    public void setSegWidth(int SegWidth) {
        this.SegWidth = SegWidth;
    }

    /**
     *
     * @return
     */
    public int getSegHeight() {
        return SegHeight;
    }

    /**
     *
     * @param SegHeight
     */
    public void setSegHeight(int SegHeight) {
        this.SegHeight = SegHeight;
    }
   

    /**
     *
     * @return
     */
    public int getMaxX() {
        return MaxX;
    }

    /**
     *
     * @param MaxX
     */
    public void setMaxX(int MaxX) {
        this.MaxX = MaxX;
    }

    /**
     *
     * @return
     */
    public int getMaxY() {
        return MaxY;
    }

    /**
     *
     * @param MaxY
     */
    public void setMaxY(int MaxY) {
        this.MaxY = MaxY;
    }

    /**
     *
     * @return
     */
    public int getImageWidth() {
        return ImageWidth;
    }

    /**
     *
     * @param ImageWidth
     */
    public void setImageWidth(int ImageWidth) {
        this.ImageWidth = ImageWidth;
    }

    /**
     *
     * @return
     */
    public int getImageHeight() {
        return ImageHeight;
    }

    /**
     *
     * @param ImageHeight
     */
    public void setImageHeight(int ImageHeight) {
        this.ImageHeight = ImageHeight;
    }

   
    /**
     *
     * @return
     */
    public int GetXpos() {
        int ret = (int)CurrentX * SegWidth;

        return ret;
    }

    /**
     *
     * @return
     */
    public int GetYpos() {
        int ret = (int)CurrentY * SegHeight;

        return ret;
    }

    /**
     *
     * @return
     */
    public int GetSegWidth() {
        return SegWidth;
    }

    /**
     *
     * @return
     */
    public int GetSegHeight() {
        return SegHeight;
    }
}
