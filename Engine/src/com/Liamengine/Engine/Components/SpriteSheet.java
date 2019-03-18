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
    private float CurrentX = 1;
    private float CurrentY = 1;
    private int SegWidth = 1;
    private int SegHeight = 1;


 /**
     *
     * @param segsx
     * @param segsy
     * @param segwidth
     * @param segheight
     * @param image
     */
    public SpriteSheet(int segsx, int segsy, int segwidth, int segheight, BufferedImage image) {
        this(segsx, segsy, segwidth, segheight);
        inputImage(image);
    }

    /**
     *
     * @param segx
     * @param segy
     * @param segwidth
     * @param segheight
     */
    public SpriteSheet(int segx, int segy, int segwidth, int segheight) {
        this.CurrentX = (segx);
        this.CurrentY = (segy);
        SegWidth = segwidth;
        SegHeight = segheight;
    }

    /**
     *
     * @param image
     */
    public void inputImage(BufferedImage image) {
        ImageWidth = image.getWidth();
        ImageHeight = image.getHeight();
        MaxY = (ImageHeight / SegHeight);
        MaxX = (ImageWidth / SegWidth);
    }

 /**
     *
     * @param x
     * @param y
     */
    public void increment(float x, float y) {
        IncrementX(x);
        IncrementY(y);
    }

    /**
     *
     * @param amt
     */
    public void IncrementX(float amt) {
        CurrentX += amt;
        if (CurrentX >= MaxX) {
            CurrentX %= MaxX;
        }
    }

    /**
     *
     * @param amt
     */
    public void IncrementY(float amt) {
        CurrentY += amt;
        if (CurrentY >= MaxY) {
            CurrentY %= MaxY;
        }
    }

    /**
     *
     * @param g
     * @param img
     * @param x
     * @param y
     * @param width
     * @param height
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
