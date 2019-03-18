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
 *
 * @author Liam Woolley 1748910
 */
public class LevelLoader {

    /**
     *
     */
    public static LevelLoader LL = new LevelLoader();

    private ArrayList<ILevel> LEVELS = new ArrayList<ILevel>();

    /**
     *
     * @param Levels
     */
    public void SetLevels(ILevel[] Levels) {
        for (ILevel Level : Levels) {
            LEVELS.add(Level);
        }
    }

    LevelLoader() {}

    /**
     *
     * @param level
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
     * @param level
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
