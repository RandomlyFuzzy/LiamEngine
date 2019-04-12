package com.Liamengine.Engine.Entry;

import com.Liamengine.Engine.AbstractClasses.ILevel;
import com.Liamengine.Engine.Components.Vector;
import com.Liamengine.Engine.Utils.MusicUtils;
import com.Liamengine.Engine.Utils.UtilManager;
import com.Liamengine.Engine.Utils.imageUtils;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * this is the first thing that is access in the engine it does all the level
 * changing and window minipulation
 *
 * @author Liam Woolley 1748910
 */
public class Game {

    /**
     * if the current game is in full screen
     */
    private static boolean isFullScreen = false;
    /**
     * first window width
     */
    private static final int WINDOW_WIDTH = 1280;
    /**
     * first window height
     */
    private static final int WINDOW_HEIGHT = 720;
    /**
     * the bounds of the current Jframe when the fullscreen is set to be able to
     * revert to it x,y,w,h
     *
     * @see Game#fullscreen
     */
    private static Rectangle FrameBounds;
    /**
     * current window that is being displayed on
     */
    private static JFrame gameWindow;
    /**
     * used to calulate the milliseconds from last update
     *
     * @see Game#getDelta
     * @see ILevel#getDelta
     */
    private static long deltalong = 0;
    /**
     * current frame delta time storage
     *
     * @see Game#getDelta
     * @see ILevel#getDelta
     */
    private static float DeltaTime = 0;

    /**
     * the level currently playing
     */
    private static ILevel CurrentLevel;
    /**
     * defualt level to fall back on
     *
     * @see TAdapter#KeyPressed
     */
    private static ILevel DefualtLevel;
    /**
     * used to turn the cursor on and of
     */
    private static Cursor Swap;
    /**
     * the last level loaded name
     *
     */
    private static String LastLevelName = "";
    /**
     * dimentions of the buttons prefered
     */
    private static Vector buttondims = new Vector(0.76f, 0.75f);
    /**
     * the world scaler
     */
    private static Vector worldDims = Vector.One();
    /**
     * dev scaler for the world scale
     */
    private static Vector worldrelDims = Vector.One();

    /**
     *
     * @param StartLevel the Level to first load
     */
    public Game(ILevel StartLevel) {
        //preloads all the utils 
        // should have used the static {} but didnt know it exsisted at the time 
        new UtilManager();
        //could have swaped with my own cursor but easier just to utilize the postition of the cursor instead
        Swap = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");
        //creates window with predefined domentions
        InitWindow();
        //creates a new instance of the level to be loaded 
        try {
            DefualtLevel = StartLevel.getClass().newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        //set the level active
        Game.SetLevelActive(StartLevel);
        //disables the cursor
        toggleCursor();
        //sets the inital time 
        deltalong = System.nanoTime();
        System.out.println("com.FuturePixels.Engine.Entry.Game.main()");
    }

    /**
     * creates a window to be draw to
     */
    public static void InitWindow() {
        //creats a new window
        gameWindow = new JFrame();
        //set defualt close operattion
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //not useful for this engine type
        gameWindow.setLocationRelativeTo(null);
        //sets intial position
        gameWindow.setLocation(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
        //sets the minimul size of the window
        gameWindow.setMinimumSize(new Dimension(20, 20));
        //changes the window size so value can change for it 
        SetDimentions(WINDOW_WIDTH, WINDOW_HEIGHT);
        //changes layout manager
        gameWindow.getContentPane().setLayout(new CardLayout());
        //disables resizing
        gameWindow.setResizable(false);
        //sets visable
        gameWindow.setVisible(true);
        //sets inital frame bounds
        FrameBounds = gameWindow.getBounds();
    }

    /**
     *
     */
    public static void toggleCursor() {
        //swaps the context pane cursor
        Cursor blankCursor = gameWindow.getContentPane().getCursor();
        gameWindow.getContentPane().setCursor(Swap);
        Swap = blankCursor;
    }

    /**
     *
     * @return the default level
     * @see TAdapter#KeyPressed
     */
    public static ILevel getDefualtLevel() {
        return DefualtLevel;
    }

    /**
     *
     * @param DefualtLevel sets the DefualtLevel to this value
     */
    public static void setDefualtLevel(ILevel DefualtLevel) {
        Game.DefualtLevel = DefualtLevel;
    }

    /**
     * toggles fullscreen
     */
    public static void FullScreen() {
        //disposes so i can set the top on or off (undecorated) the top
        gameWindow.dispose();
        //check to see if already fullscreen
        if (!isFullScreen) {
            //save the current screen postition and dimentions
            FrameBounds = gameWindow.getBounds();
            //sets the position to the top left
            gameWindow.setLocation(0, 0);
            //streaching to the screen size
            gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
            //removes top banner
            gameWindow.setUndecorated(true);
            //packs ready to be displayed
            gameWindow.pack();
        } else {
            //stop streaching the screen
            gameWindow.setExtendedState(JFrame.NORMAL);
            //add the top banner back
            gameWindow.setUndecorated(false);
            //packs ready to be displayed
            gameWindow.pack();
            //reset the position and dimentions of the window
            gameWindow.setBounds(FrameBounds);
        }
        //sets the window visable 
        gameWindow.setVisible(true);
        //set dimentions to change window and certain world scaler to
        SetDimentions(gameWindow.getWidth(), gameWindow.getHeight());
        //inverts state
        isFullScreen = !isFullScreen;
    }

    /**
     *
     * @return the current game window to change some more aspects of the window
     */
    public static JFrame GetFrame() {
        return gameWindow;
    }

    /**
     *
     * @param Level this will be the level that game will change too this sets
     * the current level
     * @see LevelLoader#LoadLevel
     */
    public synchronized static void SetLevelActive(ILevel Level) {
        //states what is being loaded and where from
        System.out.println("com.FuturePixels.Entry.Game.SetLevelActive() " + Level.getClass().toString() + " loading");
        //if their is no level currently loaded
        if (CurrentLevel != null) {
            //sets what last level was that was loaded
            LastLevelName = CurrentLevel.getClass().toString();
            //removes references of it 
            CurrentLevel.dispose();
            //Jpanel remove just incase
            CurrentLevel.removeAll();
            gameWindow.remove(CurrentLevel);
            //disables the focus on that level
            CurrentLevel.setFocusable(false);
            //stops it from being showm
            CurrentLevel.setEnabled(false);
            //sets to null for gc to get rid of it
            CurrentLevel = null;
            //Jpanel content pane remove all other levels just in case
            gameWindow.getContentPane().removeAll();
            //garbage collector
            System.gc();
        }
        //starting 
        //set currentlevel
        CurrentLevel = Level;
        gameWindow.getContentPane().add(Level);
        //sets the layout manager of the game window incase not set
        CardLayout cl = (CardLayout) gameWindow.getContentPane().getLayout();
        //cycles for the next level
        cl.next(gameWindow.getContentPane());
        //window focus request just incase not focus in window
        Level.requestFocusInWindow();
        //Jpanel focus requst
        Level.requestFocus();

        //check to see if music should be stoped
        if (CurrentLevel.StopAudioOnStart()) {
            //if so stop all audio elements
            MusicUtils.StopAllSounds();
        }
        //preinit things
        CurrentLevel.OnStart();
        //run the timer on the ILevel and attaches the event listeners
        CurrentLevel.start();

    }

    /**
     *
     * @param w width of the window
     * @param h height of te window
     */
    public static void SetDimentions(int w, int h) {
        //gets the x and y of the current window position
        Rectangle bo = Game.GetFrame().getBounds();
        //keeps current x and y but changes width and height
        gameWindow.setBounds(bo.x, bo.y, w, h);
        //calulates the world scale
        CalculateDims();
    }

    /**
     *
     * @return the currently active level
     */
    public static ILevel GetLevel() {
        return CurrentLevel;
    }

    /**
     *
     */
    public static void CalculateDims() {
        float hypot = (float) Math.sqrt((Game.GetFrame().getWidth() * Game.GetFrame().getWidth()) + ((Game.GetFrame().getHeight()-30) * (Game.GetFrame().getHeight()-30)));
        worldDims = new Vector(Game.GetFrame().getWidth() / 1280f, Game.GetFrame().getHeight() / (isFullScreen?720f:690f));
    }

    /**
     *
     * @return the predifined Button dims
     */
    public static Vector ButtonDims() {
        return new Vector(buttondims);
    }

    /**
     *
     * @return the adaptive world scale
     */
    public static Vector WorldScale() {
        return new Vector(worldDims).mult(new Vector(worldrelDims));
    }

    /**
     *
     * @return the last level loaded name
     */
    public static String getLastLevelName() {
        return LastLevelName;
    }

    /**
     * updates the current delta time
     */
    public static void SetDelta() {
        Long milli = System.nanoTime();
        DeltaTime = milli - deltalong;
        deltalong = milli;
    }

    /**
     *
     * @return the current frame delta time
     */
    public static float getDelta() {
        return DeltaTime / 1000000000.0f;
    }

    /**
     *
     * @return the current gameWindow width scaled to the world useful in game
     * when the scale is changed
     */
    public static int getScaledWidth() {
        return (int) ((float) gameWindow.getWidth() * (1f / Game.WorldScale().getX()));
    }

    /**
     *
     * @return the current gameWindow height scaled to the world
     */
    public static int getScaledHeight() {
        return (int) ((float) gameWindow.getHeight() * (1f / Game.WorldScale().getY()));
    }

    /**
     *
     * @return the unscaled width of the game window
     */
    public static int getWindowWidth() {
        return (int) (gameWindow.getWidth());
    }

    /**
     *
     * @return the unscaled height of the game window
     */
    public static int getWindowHeight() {
        return (int) ((gameWindow.getHeight()-(isFullScreen?0:30)));
    }

    /**
     *
     * @return the dev definied world scale
     */
    public static Vector getWorldrelDims() {
        return worldrelDims;
    }

    /**
     *
     * @param worldrelDims the scaler of the world of the world
     */
    public static void setWorldrelDims(Vector worldrelDims) {
        Game.worldrelDims = worldrelDims;
    }
}
