/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.AbstractClasses;

import com.Liamengine.Engine.Components.Transform;
import com.Liamengine.Engine.Utils.LevelLoader;
import com.Liamengine.Engine.Components.Vector;
import com.Liamengine.Engine.Utils.imageUtils;
import com.Liamengine.Engine.Utils.MusicUtils;
import com.Liamengine.Engine.Entry.Game;
import com.Liamengine.Engine.Utils.UtilManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import jdk.nashorn.internal.parser.JSONParser;

/**
 * 
 * this is the core Object runs the update loop and draws things to the screen
 * 
 * @see Game#SetLevelActive
 * @see LevelLoader#LoadLevel
 * 
 * @author Liam Woolley 1748910
 */
public abstract class ILevel extends JPanel implements ActionListener {


    /**
     * this is the object that makes the hookes into the Action listener and makes the Update loop happen
     */
    private javax.swing.Timer timer;

    /**
     * collection of all things that can be drawn on the screen
     * it also hooks into the update loop in this object
     */
    private ArrayList<IDrawable> gameObjs = new ArrayList<IDrawable>();

    /**
     * total time elapsed in the level
     * @see ILevel#actionPerformed
     * @see ILevel#getTime
     */
    private double Time = 0;
    /**
     * the scale of the time to be added to the Time variable
     */
    private static float timeScale = 1;

    /**
     * the is the object that hookes into certain action listeners
     * @see #Inputadapter
     * @see ILevel#start
     * @see ILevel#stop
     */
    private TAdapter Inputadapter = null;
    /**
     * this make all audio be stoped if the is true 
     * @see Game#SetLevelActive
     */
    private boolean StopAudioOnStart = true;

    /**
     * limits the amount of collisions to 1 per frame(useful in menus looks better)
     * @see ILevel#checkCollionsions
     */
    private boolean SimpleCollison = true;
    /**
     * defualt backgound of the level
     * if false then it doesnt run
     */
    private BufferedImage background;

    /**
     * this is true of the mouse is draging(clicking and moving)
     * if this is true then IsClicking is false
     * @see ILevel#TAdapter#mouseDragged
     */
    private boolean IsDragging = false;
    /**
     * this is true when the mouse is on the screen and false if outside
     * @see TAdapter#mouseEntered
     * @see TAdapter#mouseExited
     */
    private boolean IsInside = true;
    /**
     * this is true of the mouse is clicking ( not clicking and moving)
     * if this is true then IsDragging is false
     * @see TAdapter#mouseDragged
     */
    private boolean IsClicking = false;
    /**
     * boolean to see if the user has clicked this frame
     */
    private boolean hasClicked = false;
    /**
     * storage of the last key pressed in the current Level
     */
    private KeyEvent LastKeyPress = null;
    /**
     * this is storage of all the mouse pressed 
     * @see ILevel#GetMouseButtonDown
     * @see TAdapter#mousePressed
     * @see TAdapter#mouseReleased
     */
    private HashMap<Integer, Boolean> MouseButtonPressed = new HashMap<Integer, Boolean>();
    /**
     * current mouse postition relative to the top left corner of the screen
     */
    private static Vector MousePos = new Vector(Vector.Zero());
    /**
     * current frame rate of the sceen
     * note can be dynamicaly changed
     */
    private static int FPS = 60;
    /**
     * Currently loaded Level
     */
    private static ILevel current;
    /**
     * if this true then it shows the bouding boxes of all the collision 
     */
    private static boolean DebugCollisons = false;


    /**
     * constructor for the Level 
     */
    public ILevel() {
        current = this;
        timer = new javax.swing.Timer(15, this);
    }
 /**
     *
     * @return the timescale of all ILevels
     */
    public float getTimeScale() {
        return ILevel.timeScale;
    }

    /**
     *
     * @param timeScale sets object timeScale to that value
     */
    public void setTimeScale(float timeScale) {
        ILevel.timeScale = timeScale;
    }



    /**
     * 
     * @return gets the scaled delta if the level
     */
    public static float getDelta() {
        return Game.getDelta()*(float)timeScale;
    }

    /**
     *
     * @return the background image set
     */
    public BufferedImage getBackgroundimage() {
        return background;
    }

    /**
     *
     * @return whether or not the use has clicked
     */
    public boolean HasClicked() {
        return hasClicked;
    }

    /**
     *
     * @return how many IDrawable objects have been added to the level
     */
    public int AmountOfObjects() {
        return gameObjs.size();
    }

    /**
     *
     * @param background sets object timeScale to that value
     */
    public void setBackgroundimage(BufferedImage background) {
        this.background = background;
    }

    /**
     *
     * @return the current set fps of the level
     */
    public static int getFPS() {
        return FPS;
    }

    /**
     *
     * @param FPS sets the fps of the level also does this dynamicly 
     */
    public static void setFPS(int FPS) {
        ILevel.FPS = FPS;
        current.stop();
        current.start();
    }

    /**
     * @return whether or not to stop audio on load 
     * @see Game#SetLevelActive
     */
    public boolean StopAudioOnStart() {
        return StopAudioOnStart;
    }

    /**
     *
     * @param StopAudioOnStart the object StopAudioOnStart to this 
     * note should be set in the contructor 
     */
    public void setStopAudioOnStart(boolean StopAudioOnStart) {
        this.StopAudioOnStart = StopAudioOnStart;
    }

   
    /**
     *
     * @return total time elapsed in the level
     */
    public double getTime() {
        return Time;
    }
    /**
     *
     * @return the last key pressed by the user
     */
    public KeyEvent getLastKeyPress() {
        return LastKeyPress;
    }

    /**
     *
     * @return to see if the debug mode is enabled
     */
    public boolean isDebugCollisons() {
        return DebugCollisons;
    }

    /**
     * reset values once level is closed
     * mainly used for static things or just things 
     * I want to help dereference and the get collected by
     * the GC(garbage collector)
     */
    void resetParams() {
        IsDragging = false;
        IsInside = true;
        IsClicking = false;
        Inputadapter = null;
        gameObjs = new ArrayList<IDrawable>();
        timer = null;
        Time = 0;
    }

    /**
     *
     * @return current mouse position on the screen relative to the Top Left of the screen
     */
    public Vector getMousePos() {
        return MousePos;
    }

    /**
     *
     * @return the value of IsDragging
     */
    public boolean isDragging() {
        return IsDragging;
    }

    /**
     *
     * @return the value of IsInside
     */
    public boolean isInside() {
        return IsInside;
    }

    /**
     *
     * @return the value of IsClicking
     */
    public boolean isClicking() {
        return IsClicking;
    }

    /** 
     * 1 left
     * 2 middle
     * 3 right
     * @param ind
     * @return the value of the mosue click
     */
    public boolean GetMouseButtonDown(int ind) {
        //if it hasnt been added 
        //return false
        if (!MouseButtonPressed.containsKey(ind)) {
            return false;
        }
        return MouseButtonPressed.get(ind);
    }

    /**
     * just gets the this for an Ilevel
     * useful to getting in the embeded class TAdapter
     */
    private ILevel get() {
        return this;
    }

    /**
     *
     * @param URL the URL address of the image
     * @return the image it found null if non found
     */
    public BufferedImage getOnlineImage(String URL) {
        try {
            return ImageIO.read(new URL(URL));
        } catch (IOException ex) {
            Logger.getLogger(ILevel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ILevel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param APIURL the api url of the 
     * @return the image it found else null if non found
     * @see ILevel#getOnlineImage
     */
    public BufferedImage getFromApi(String APIURL) {
        BufferedReader in = null;
        String Data = "";
        try {
            in = new BufferedReader(new InputStreamReader(new URL(APIURL).openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                Data += (line);
            }
            in.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ILevel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ILevel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
        Data = Data.substring(Data.indexOf("data\",\"") + 10, Data.length() - 2);
        Data = Data.replace("\\/", "/");
        System.out.println(Data);
        return getOnlineImage(Data);
    }

    /**
     * what happenes when the level is first loaded
     */
    public void OnStart() {
        setFocusable(true);
        setDoubleBuffered(true);
        setVisible(true);
        init();
    }

    /**
     *
     * @param <T> type of the object object that is wanted to be added
     * @param Drawable object to add
     * @return the obejct that was added
     */
    public synchronized <T extends IDrawable> T AddObject(T Drawable) {
        gameObjs.add(Drawable);
        //how the init in the object is ran
        Drawable.CoreInit();
        return Drawable;
    }

    /**
     * this is the update loop
     * does things like sets the delta and collision and updating the objects stored 
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        Update(ae);
        movement();
        checkCollionsions();
        this.repaint();
        Time += ILevel.getDelta();
        Game.SetDelta();
    }

    /**
     * runns all the IDrawable doMove function
     * enables them to use the updateloop
     */
    public void movement() {
        if (gameObjs.size() == 0) {
            return;
        }
        for (int i = 0; i < gameObjs.size(); i++) {
            if (gameObjs.get(i) != null) {
                //this an abtract function call
                // this is where the main logic of things are or transformation changes
                gameObjs.get(i).doMove();
            }
        }
    }


    /**
     * graphical update loop
     * 
     */
    public void paintComponent(Graphics g) {
        // inherited component super
        super.paintComponent(g);
        //changes the graphical context to something more useful
        Graphics2D g2d = (Graphics2D) g;

        //draws background
        if (background != null) {
            g.drawImage(background, 0, 0, (int) (Game.getWindowWidth()), (int) (Game.getWindowHeight()), null);
        }
        //sets the default font of the level
        Font title = new Font("Comic sans serif ms", 0, (int) (Game.ButtonDims().getY() * 30f));
        g2d.setFont(title);
        //ILevel draw
        Draw(g2d);
        try {
            //IDrawable Draw
            for (int i = 0; i < gameObjs.size(); i++) {
                //check to see if not null and enabled
                if (gameObjs.get(i) != null) {
                    gameObjs.get(i).CoreUpdate(g2d);
                    gameObjs.get(i).setIsColliding(false);
                }
            }
        } catch (Exception e) {
            // theirs always an error here cant figure out how to resolve :/
//            System.err.println(e);
        }
        //shows FPS
        if (DebugCollisons) {
            g2d.drawString("" + 1f / Game.getDelta() + "fps", 20, 20);
        }
        hasClicked = false;
    }

    /**
     *
     * @param index array index from 0 ... n
     * @return the object at that address or null if past the array
     */
    public IDrawable GetObject(int index) {
        if (gameObjs.size() < index) {
            return gameObjs.get(index);
        } else {
            UtilManager.FindUseClass(3);
        }
        return null;
    }

    /**
     *
     * @return the amount of objects in the game 
     */
    public int GetObjectCount() {
        return gameObjs.size();
    }

    /**
     *
     * @param g the object to remove 
     */
    public void RemoveObject(IDrawable object) {
        gameObjs.remove(object);
    }

    /**
     * starts the timer and adds action delegats
     */
    public void start() {
        if (Inputadapter == null) {
            Inputadapter = new TAdapter();
        }
        current.timer = new javax.swing.Timer((int) ((float) 1000 / (float) FPS), this);
        timer.start();

        if (getKeyListeners().length == 0) {
            addKeyListener(Inputadapter);
        } else {
            System.err.println("com.FuturePixels.Utils.ILevel.start() their was a problem disposing of the KeyListener");
        }
        if (getMouseListeners().length == 0) {
            addMouseListener(Inputadapter);
        } else {
            System.err.println("com.FuturePixels.Utils.ILevel.start() their was a problem disposing of the MouseListeners");
        }
        if (getMouseMotionListeners().length == 0) {
            addMouseMotionListener(Inputadapter);
        } else {
            System.err.println("com.FuturePixels.Utils.ILevel.start() their was a problem disposing of the MouseMotionListeners");
        }

    }

    /**
     * stops obejct and remove delegates
     */
    public void stop() {
        if (Inputadapter == null) {
            try {
                throw new Exception("tried to stop it before it even ran");
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } else {
            timer.stop();
        }
        if (getKeyListeners().length != 0) {
            removeKeyListener(Inputadapter);
        }
        if (getMouseListeners().length != 0) {
            removeMouseListener(Inputadapter);
        }
        if (getMouseMotionListeners().length != 0) {
            removeMouseMotionListener(Inputadapter);
        }

    }

    /**
     *
     * @param soundResource the URI of the sound resourse oftern starting with a /
     */
    public void play(String soundResource) {
        play(soundResource, 0, 0, false);
    }

    /**
     *
     * @param soundResource the URI of the sound resourse oftern starting with a /
     * @param seconds how far in should it start
     */
    public void play(String soundResource, float seconds) {
        play(soundResource, seconds, 0, false);
    }

    /**
     *
     * @param soundResource the URI of the sound resourse oftern starting with a /
     * @param seconds how far in should it start
     * @param LoopAmt the amount of times to loop Clip.LOOP_CONTINUOUSLY or -1 for neverstoping
     */
    public void play(String soundResource, float seconds, int LoopAmt) {
        
        play(soundResource, seconds, LoopAmt, false);
    }

    /**
     * embeded function so no more than 1 audio thread is made at a time 
     */
    private synchronized void play(String soundResource, float seconds, int LoopAmt, boolean a) {
        MusicUtils.play(soundResource, seconds, LoopAmt);
    }

    /**
     *
     * @param URI the URI of the image resourse oftern starting with a /images/
     * @return the image if any found else null
     */
    protected BufferedImage GetSprite(String URI) {
        BufferedImage g = imageUtils.T.GetImage(URI);
        return g;
    }

    /**
     * this is using java swing native functionality (polygon collisions) but
     * their are exameples or raycasting in use in the player.onCollison
     * function
     *
     */
    public void checkCollionsions() {

        //check to see if amount of objects is greater than 0
        if (gameObjs.size() > 0) {
            // loop through all the object
            for (int i = 0; i < gameObjs.size(); i++) {
                //get object at i
                IDrawable a = gameObjs.get(i);
                //loop from the i index to the end of the array
                //this is done so 2 of the same collisions arnt detected at the same time 
                for (int j = i + 1; j < gameObjs.size(); j++) {
                    //get object at j
                    IDrawable b = gameObjs.get(j);
                    // check for not the same index, one is null and both are enabled and collidable 
                    if (i == j || (a == null || b == null) || !(a.isEnabled() && b.isEnabled()) || !(a.IsCollidable() && b.IsCollidable())) {
                        continue;
                    }
                    //check to see if they arnt the same object
                    if (a != b) {
                        //check if either one is colliding with the other
                        if (a.CheckCollions(b) || b.CheckCollions(a)) {
                            //sets it has collided 
                            a.setIsColliding(true);
                            //runs abstact function
                            a.onCollison(b);
                            //sets it has collided 
                            b.setIsColliding(true);
                            //runs abstact function
                            b.onCollison(a);
                            // stops more than 1 collision per frame (useful in menus)
                            if (SimpleCollison) {
                                return;
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     *
     * @return whether or not it uses simple collisions 
     */
    public boolean isSimpleCollison() {
        return SimpleCollison;
    }

    /**
     *
     * @param SimpleCollison the local variable to use or not use simple collisons
     * not should be set in the contructor
     */
    public void setSimpleCollison(boolean SimpleCollison) {
        this.SimpleCollison = SimpleCollison;
    }

    //wrapped class for use in detecting user interactions
    class TAdapter extends InputAdapter {


        /**
         * key release this triggers
         */
        @Override
        public void keyReleased(KeyEvent e) {
            //check to see if the same level
            if (get() != Game.GetLevel()) {
                return;
            }
            keyRelease(e);
        }
        /**
         * key pressed this triggers
         */
        @Override
        public void keyPressed(KeyEvent e) {
            //check to see if the same level
            if (get() != Game.GetLevel()) {
                return;
            }
            //f9 
            if (e.getKeyCode() == KeyEvent.VK_F9) {
                DebugCollisons = !DebugCollisons;
            }
            //loads the defualt level
            if (e.getKeyCode() == KeyEvent.VK_F10) {
                MusicUtils.StopAllSounds();
                try {
                    Game.SetLevelActive(Game.getDefualtLevel().getClass().newInstance());
                } catch (InstantiationException ex) {
                    Logger.getLogger(ILevel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ILevel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //reloads the currently loaded level
            if (e.getKeyCode() == KeyEvent.VK_F11) {
                try {
                    LevelLoader.LoadLevel(gameObjs.get(0).Level().getClass().newInstance());
                } catch (InstantiationException ex) {
                    Logger.getLogger(ILevel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ILevel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //sets full screen
            if (e.getKeyCode() == 10 && e.isAltDown()) {
                Game.FullScreen();
            } else 
            // toggle OS mouse
            if (e.getKeyCode() == 10) {
               Game.toggleCursor();
            }
            //set last key press
            LastKeyPress = e;
            //keypressed abstract function
            keyPress(e);
        }
        /**
         * mouse pressed this triggers
         */
        @Override
        public void mousePressed(MouseEvent e) {
            IsClicking = true;
            hasClicked = true;
            //adds reference to the button that is pressed
            MouseButtonPressed.put(e.getButton(), true);
        }
        /**
         * mouse release this triggers
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            //sets the referenc to false
            MouseButtonPressed.put(e.getButton(), false);
            //local
            boolean isactiveOne = false;

            IsDragging = false;
            //if the hashmap has a size greater that 0
            //put the values in the array
            Integer[] arr = new Integer[MouseButtonPressed.size()];
            MouseButtonPressed.keySet().toArray(arr);

            //check to see if any are pressed
            for (Integer a : arr) {

                //NOT gate toggle with an OR to see if triggered before
                isactiveOne = isactiveOne != MouseButtonPressed.get(a) || isactiveOne;
                //dont need to continue if found
                if(isactiveOne){
                    break;
                }
            }
            //if their is a key pressed then true else false
            IsClicking = isactiveOne;
        }
        /**
         * mouse is just moved on the screen this triggers
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            //this would stop the programm if the mouse was not on the screen
//            start();
            IsInside = true;
        }
        /**
         * mouse isnt on the screen this triggers
         */
        @Override
        public void mouseExited(MouseEvent e) {
            //this would start the programm if the mouse was on the screen
//            stop();
            IsInside = false;
        }

        /**
         * mouse is clicking and moving this is triggered 
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            IsDragging = true;
            IsClicking = false;
            MousePos = new Vector(e.getX(), e.getY());
        }
        /**
         * mouse is moved on the screen this triggers
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            MousePos = new Vector(e.getX(), e.getY());
        }
        /**
         * key is pressed or held this triggers
         */
        @Override
        public void keyTyped(KeyEvent e) {
            keytyped(e);
        }

    }

    /**
     * removes as meny references
     */
    public void dispose() {
        stop();
        ArrayList<IDrawable> drawable = gameObjs;
        gameObjs = new ArrayList<IDrawable>();
        drawable.forEach((a) -> {
            a.dispose();
        });
        for (int i = drawable.size() - 1; i > 0; i--) {
            drawable.remove(i);
        }

        resetParams();
        Transform.setOffsetTranslation(Vector.Zero());
    }

    /**
     * abstract fucntion runs once when the level is set as the current 
     * used for populating the gameObjs array
     */
    public abstract void init();

    /**
     * this happens every frame
     * @param ae an event that the timer passes
     */
    public abstract void Update(ActionEvent ae);

    /**
     *
     * @param g graphical context
     */
    public abstract void Draw(Graphics2D g);

    /**
     * this runns when a keys pressed and this is in focus
     * @param e key that was pressed
     */
    public abstract void keyPress(KeyEvent e);

    /**
     * this runns when a keys released and this is in focus
     * @param e key that is released
     */
    public abstract void keyRelease(KeyEvent e);

    /**
     * this runns when a keys pressed and/or held and this is in focus
     * @param e key that is pressed
     * should be overwritten to used using "@Override" modifier
     */
    public void keytyped(KeyEvent e) {
        System.out.println("com.Liamengine.Engine.AbstractClasses.ILevel.keytyped()");
    }
}
