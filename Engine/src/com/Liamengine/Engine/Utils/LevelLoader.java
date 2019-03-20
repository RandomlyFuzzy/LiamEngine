/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Utils;

import com.Liamengine.Engine.Entry.Game;
import com.Liamengine.Engine.AbstractClasses.ILevel;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Level loader util needs to have all the 
 * ILevels preplaced in before creating the game object
 * 
 * @see LevelLoader#SetLevels
 * 
 * 
 * @author Liam Woolley 1748910
 */
public class LevelLoader {

    /**
     * static reference if its self to be called exsternally
     */
    public static LevelLoader LL = new LevelLoader();


    /**
     * collection of all the Level that can be loaded by string 
     */
    private ArrayList<ILevel> LEVELS = new ArrayList<ILevel>();

    /**
     *
     * @param Levels all the level wanted to be placed into the scene
     */
    public void SetLevels(ILevel[] Levels) {
        for (ILevel Level : Levels) {
            LEVELS.add(Level);
        }
    }
    /**
     * basic empty contructor for LL
     */
    LevelLoader() {}

    /**
     *
     * @param level Level to be created
     * note is an instance of that level 
     * if it has paramiters use the Game class
     * @see Game#SetLevelActive
     */
    public static void LoadLevel(ILevel level) {
        try {
            Game.SetLevelActive(level.getClass().newInstance());
        } catch (InstantiationException ex) {
            Logger.getLogger(LevelLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(LevelLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param level name of the level to be loaded
     */
    public static void LoadLevel(String level) {
        boolean found = false;
        for (ILevel i : LL.LEVELS) {
            if (i.getClass().getName().contains(level)) {
                try {
                    System.out.println("com.FuturePixels.MainClasses.LevelLoader.<init>() " + i.getClass().getName().toString());
                    Game.SetLevelActive(i.getClass().newInstance());
                    found = true;
                } catch (IllegalAccessException ex) {
                    System.err.println("com.Liamengine.Engine.Utils.LevelLoader.LoadLevel() " + level);
                    Logger.getLogger(LevelLoader.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    System.err.println("com.Liamengine.Engine.Utils.LevelLoader.LoadLevel() " + level);
                    Logger.getLogger(LevelLoader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (!found) {
            System.err.println("failed to load level named " + level);
            UtilManager.FindUseClass(2);
        }
    }

}
