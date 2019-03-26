/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * this is the main storage and loading util for buffered 
 * images it can save load and put images into the local storage
 * 
 * 
 * 
 * @author Liam Woolley 1748910
 */
public class imageUtils {

    /**
     * static reference of self
     */
    public static imageUtils T;
    /**
     * string Buffered image dictionary
     */
    private HashMap<String, BufferedImage> Images = new HashMap<String, BufferedImage>();

    /**
     * contructor only runs onces at games start
     */
    public imageUtils() {
        T = this;
        //recursive function to preload all images before starting
        LoadResoureces();
    }

    /**
     * this is used to start of pre loading all images in the resources/images/ directory
     */
    public void LoadResoureces() {
        if (new File("resources/images/").exists()) {
            File f = new File("resources/images/");
            if (f.isDirectory()) {
                LoadDir(f);
            } else {
                System.err.println("couldnt load images ?");
            }
        } else {
            System.err.println("/resources/images/ folder missing reconstructing");
            File f = new File(System.getenv("user.dir")+"/../resources/images/");
            f.mkdir();
        }
    }

    /**
     * recursive function reaching into all the sub folders of resources/images/
     */
    private void LoadDir(File dir) {
        //finds all files and folders in the directory
        for (File f2 : dir.listFiles()) {
            //if is a directoy
            if (f2.isDirectory()) {
                // run this funcation on that directory
                LoadDir(f2);
                //say what is being loaded
                System.out.println("preloading image from /resources/images/ " + f2.getAbsolutePath());
            } else {
                // gets the Absoulute URI of the file
                String from = f2.getAbsolutePath().toString();
                // trims so its the same as all the other calls to the diectionary
                from = from.substring(from.indexOf("resources") + "resources".length());
                //replace \ with /
                from = from.replace('\\', '/');
                // says it been preloaded
                System.out.println("preloading image " + from);
                //loads image to dictionary
                imageUtils.T.GetImage(from);
            }
        }
    }

    /**
     *
     * @param URI local file address
     * @return the bufferimage of the file if exsists
     */
    public BufferedImage GetImage(String URI) {
        return GetImage(System.getProperty("user.dir")+"/resources"+URI, false);
    }

    /**
     *
     * @param URI local file address
     * @param isDefault used the second run of this function to get defualt image
     */
    private synchronized BufferedImage GetImage(String URI, boolean isDefault) {
        URI = "" + URI;
        BufferedImage g = null;
        if (T.Images.containsKey(URI)) {
            try {
                throw new Exception();
            } catch (Exception e) {
//                System.out.println("Image loaded " + URI + " in " + e.getStackTrace()[isDefault ? 2 : 3].getClassName());
            }
            return T.Images.get(URI);
        } else {
            try {
                g = ImageIO.read(new File(URI));
                T.Images.put(URI, g);
                try {
                    throw new Exception();
                } catch (Exception e) {
                    System.out.println("Image loaded " + URI + " in " + e.getStackTrace()[isDefault ? 3 : 4].getClassName());
                    return g;
                }
            } catch (Exception e) {
                System.err.println("error loading " + URI + " in " + e.getStackTrace()[isDefault ? 3 : 4].getClassName());
            }
        }

        return GetImage(System.getProperty("user.dir")+"/resources/images/defualt.png", true);
    }

    /**
     *
     * @param Name what its going to be saved as
     * @param img buffer image to save
     */
    public void setImage(String Name, BufferedImage img) {
        T.Images.put(System.getProperty("user.dir")+"/resources"+Name, img);
    }

}
