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
 *
 * @author Liam Woolley 1748910
 */
public class Game {

    private static boolean isFullScreen = false;
    private static final int WINDOW_WIDTH = 1280, WINDOW_HEIGHT = 720;
    private static JFrame gameWindow;
    private static long deltalong = 0;
    private static ILevel CurrentLevel;
    private static ILevel DefualtLevel;

    private static Rectangle FrameBounds;
    private static Cursor Swap;
    private static String LastLevelName = "";
    private static Vector buttondims = Vector.One();
    private static Vector worldDims = Vector.One();
    private static Vector worldrelDims = Vector.One();
    private static Date deltaCalc = new Date();

    /**
     *
     * @param StartLevel
     */
    public Game(ILevel StartLevel) {
        new UtilManager();
        Swap = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");

        InitWindow();
        try {
            //FullScreen();
            DefualtLevel = StartLevel.getClass().newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        Game.SetLevelActive(StartLevel);
        toggleCursor();
        deltalong = System.nanoTime();
        System.out.println("com.FuturePixels.Engine.Entry.Game.main()");
    }

    /**
     *
     */
    public static void InitWindow() {
        gameWindow = new JFrame();
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setLocation(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
        gameWindow.setMinimumSize(new Dimension(20, 20));
        SetDimentions(WINDOW_WIDTH, WINDOW_HEIGHT);
        gameWindow.getContentPane().setLayout(new CardLayout());
        gameWindow.setResizable(false);
        gameWindow.setVisible(true);
        FrameBounds = gameWindow.getBounds();
    }

    /**
     *
     */
    public static void toggleCursor() {
        Cursor blankCursor = gameWindow.getContentPane().getCursor();
        gameWindow.getContentPane().setCursor(Swap);
        Swap = blankCursor;
    }

    /**
     *
     * @return
     */
    public static ILevel getDefualtLevel() {
        return DefualtLevel;
    }

    /**
     *
     * @param DefualtLevel
     */
    public static void setDefualtLevel(ILevel DefualtLevel) {
        Game.DefualtLevel = DefualtLevel;
    }

    /**
     *
     */
    public static void FullScreen() {
        GraphicsDevice graphicalDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        gameWindow.dispose();
        if (!isFullScreen) {
            FrameBounds = gameWindow.getBounds();
            gameWindow.setLocation(0, 0);
            gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
            gameWindow.setUndecorated(true);
            gameWindow.pack();
        } else {
            gameWindow.setExtendedState(JFrame.NORMAL);
            gameWindow.setUndecorated(false);
            gameWindow.pack();
            gameWindow.setBounds(FrameBounds);
        }
        gameWindow.setVisible(true);
        SetDimentions(gameWindow.getWidth(), gameWindow.getHeight());
        isFullScreen = !isFullScreen;
    }

    /**
     *
     * @return
     */
    public static JFrame GetFrame() {
        return gameWindow;
    }

    /**
     *
     * @param Level
     */
    public synchronized static void SetLevelActive(ILevel Level) {
        System.out.println("com.FuturePixels.Entry.Game.SetLevelActive() " + Level.getClass().toString() + " loading");
        if (CurrentLevel != null) {
            LastLevelName = CurrentLevel.getClass().toString();
            CurrentLevel.dispose();
            CurrentLevel.removeAll();
            CurrentLevel.setFocusable(false);
            CurrentLevel.setEnabled(false);
            CurrentLevel = null;
            gameWindow.getContentPane().removeAll();
            System.gc();
        }
        Level.setPreferredSize(new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT));
        gameWindow.getContentPane().add(Level);
        CardLayout cl = (CardLayout) gameWindow.getContentPane().getLayout();
        cl.next(gameWindow.getContentPane());
        Level.requestFocusInWindow();
        Level.requestFocus();
        CurrentLevel = Level;
        if (CurrentLevel.StopAudioOnStart()) {
            MusicUtils.StopAllSounds();
        }
        CurrentLevel.OnStart();
        CurrentLevel.start();

    }

    /**
     *
     * @param w
     * @param h
     */
    public static void SetDimentions(int w, int h) {
        Rectangle bo = Game.GetFrame().getBounds();
        gameWindow.setBounds(bo.x, bo.y, w, h);
        CalculateDims();
    }

    /**
     *
     * @return
     */
    public static ILevel GetLevel() {
        return CurrentLevel;
    }

    /**
     *
     */
    public static void CalculateDims() {
        float hypot = (float) Math.sqrt((Game.GetFrame().getWidth() * Game.GetFrame().getWidth()) + (Game.GetFrame().getHeight() * Game.GetFrame().getHeight()));
        buttondims = new Vector(0.8f, 0.79f);
        worldDims = new Vector((Game.GetFrame().getWidth() / hypot) * Game.GetFrame().getWidth() / 1280, (Game.GetFrame().getWidth() / hypot) * Game.GetFrame().getWidth() / 1280);
    }

    /**
     *
     * @return
     */
    public static Vector ButtonDims() {
        return new Vector(buttondims);
    }

    /**
     *
     * @return
     */
    public static Vector WorldScale() {
        return new Vector(worldDims).mult(new Vector(worldrelDims));
    }

    /**
     *
     * @return
     */
    public static String getLastLevelName() {
        return LastLevelName;
    }

    /**
     *
     * @param LastLevelName
     */
    public static void setLastLevelName(String LastLevelName) {
        Game.LastLevelName = LastLevelName;
    }
    private static float DeltaTime = 0;

    /**
     *
     */
    public static void SetDelta() {
        Long milli = System.currentTimeMillis();
        DeltaTime = milli - deltalong;
        deltalong = milli;
    }

    /*
        this is the Delta time in milliseconds for each time the screen is drawn
     */
    /**
     *
     * @return
     */
    public static float getDelta() {
        return DeltaTime / 1000.0f;
    }

    /**
     *
     * @return
     */
    public static int getScaledWidth() {
        return (int) ((float) gameWindow.getWidth() * (1f / Game.WorldScale().getX()));
    }

    /**
     *
     * @return
     */
    public static int getScaledHeight() {
        return (int) ((float) gameWindow.getHeight() * (1f / Game.WorldScale().getY()));
    }

    /**
     *
     * @return
     */
    public static int getWindowWidth() {
        return (int) (gameWindow.getWidth());
    }

    /**
     *
     * @return
     */
    public static int getWindowHeight() {
        return (int) ((gameWindow.getHeight()));
    }

    /**
     *
     * @return
     */
    public static Vector getWorldrelDims() {
        return worldrelDims;
    }

    /**
     *
     * @param worldrelDims
     */
    public static void setWorldrelDims(Vector worldrelDims) {
        Game.worldrelDims = worldrelDims;
    }
}
