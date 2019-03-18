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
 *
 * @author Liam Woolley 1748910
 */
public class imageUtils {

    /**
     *
     */
    public static imageUtils T;
    private HashMap<String, BufferedImage> Images = new HashMap<String, BufferedImage>();

    /**
     *
     */
    public imageUtils() {
        T = this;
        LoadResoureces();
    }

    /**
     *
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
            File f = new File("resources/images/");
            f.mkdir();
        }
    }

    //recursive function reaching into all the sub folders
    private void LoadDir(File dir) {
        for (File f2 : dir.listFiles()) {
            if (f2.isDirectory()) {
                LoadDir(f2);
                System.out.println("preloading image from /resources/images/ " + f2.getAbsolutePath());
            } else {
                //form uri
                String from = f2.getAbsolutePath().toString();
                from = from.substring(from.indexOf("resources") + "resources".length());
                from = from.replace('\\', '/');
                System.out.println("preloading image " + from);
                imageUtils.T.GetImage(from);
            }
        }
    }

    /**
     *
     * @param URI
     * @return
     */
    public BufferedImage GetImage(String URI) {
        return GetImage(URI, false);
    }

    /**
     *
     * @param URI
     * @param isDefault
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
                g = ImageIO.read(getClass().getResourceAsStream(URI));
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

        return GetImage("/images/defualt.png", true);
    }

    /**
     *
     * @param Name
     * @param img
     */
    public void setImage(String Name, BufferedImage img) {
        T.Images.put(Name, img);
    }

}
