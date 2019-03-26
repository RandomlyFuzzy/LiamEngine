/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Utils;

import com.Liamengine.Engine.Components.Collison;
import com.Liamengine.Engine.AbstractClasses.IDrawable;
import com.Liamengine.Engine.Components.Vector;
import java.util.ArrayList;

/**
 *
 * @author Liam Woolley 1748910
 */
public class CollisonUtils {

    /**
     *
     * @param a start
     * @param b end
     * @param c compaire line start
     * @param d compaire line end
     * @return a collison object
     */
    public static Collison CheckForLineHits(Vector a, Vector b, Vector c, Vector d) {

        //translate the vectors to start at 0,0
        //so its as if it starts at 0,0
        Vector r = new Vector(b.getX() - a.getX(), b.getY() - a.getY());
        Vector s = new Vector(d.getX() - c.getX(), d.getY() - c.getY());

        //find relative magnitude of the lines
        double g = r.getX() * s.getY() - r.getY() * s.getX();

        // scale the magnitude of the bounds of line from 0 .. n to 0 .. 1
        double u = (((c.getX() - a.getX()) * r.getY()) - ((c.getY() - a.getY()) * r.getX())) / g;
        double t = (((c.getX() - a.getX()) * s.getY()) - ((c.getY() - a.getY()) * s.getX())) / g;

        //check if within both the bounds 
        if (0 <= u && u <= 1 && t >= 0 && t <= 1) {

            //scale the in vector by whe bounds and add the a offset so it start at the begining
            //note can use u also instead of t
            //returns the collison at a certain point
            return new Collison(
                    new Vector(
                        (a.getX() + ((float)t * r.getX()))
                       ,(a.getY() + ((float)t * r.getY()))
                        )
                    );
        }

        return new Collison();
    }
}
