/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Utils;

import com.Liamengine.Engine.Utils.imageUtils;
import com.Liamengine.Engine.Utils.MusicUtils;

/**
 *
 * utility manager kind of redundent was here because i didnt know the "static
 * {}" existed
 *
 *
 * @author Liam Woolley 1748910
 */
public class UtilManager {

    /**
     * creates the utilitys that need to be created
     */
    public UtilManager() {
        new imageUtils();
        new MusicUtils();
    }

    /**
     *
     * @param depth the depth of the stack to look up shoul be starting from 1
     * can be -1 to get the final in the stack trace
     */
    public static void FindUseClass(int depth) {
        try {
            try {
                throw new Exception();
            } catch (Exception e) {
                System.err.println("FindUseClass() " + e.getStackTrace()[depth == -1 ? e.getStackTrace().length - 1 : depth]);
            }
        } catch (Exception e) {
        }
    }
}
