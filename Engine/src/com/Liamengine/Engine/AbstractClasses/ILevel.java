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
import javax.imageio.ImageIO;
import jdk.nashorn.internal.parser.JSONParser;

/**
 *
 * @author RandomlyFuzzy
 */
public abstract class ILevel extends JPanel implements ActionListener {

    private javax.swing.Timer timer;
    private ArrayList<IDrawable> gameObjs = new ArrayList<IDrawable>();
    private double Time = 0;
    private TAdapter Inputadapter = null;
    private TAdapter keyadapter = null;
    private boolean DebugCollisons = false;
    private boolean StopAudioOnStart = true, SimpleCollison = true;
    private BufferedImage background;
    private boolean IsDragging = false, IsInside = true, IsClicking = false;
    private KeyEvent LastKeyPress = null;
    private HashMap<Integer, Boolean> MouseButtonPressed = new HashMap<Integer, Boolean>();
    private static Vector MousePos = new Vector(Vector.Zero());
    private static int FPS = 60;
    private static ILevel current;

    public BufferedImage getBackgroundimage() {
        return background;
    }

    public void setBackgroundimage(BufferedImage background) {
        this.background = background;
    }

    public static int getFPS() {
        return FPS;
    }

    public static void setFPS(int FPS) {
        ILevel.FPS = FPS;
        current.stop();
        current.start();
    }

    public boolean StopAudioOnStart() {
        return StopAudioOnStart;
    }

    public void setStopAudioOnStart(boolean StopAudioOnStart) {
        this.StopAudioOnStart = StopAudioOnStart;
    }

    public ILevel() {
        current = this;
        timer = new javax.swing.Timer(15, this);
    }

    public double getTime() {
        return Time;
    }

    public void setTime(double Time) {
        this.Time = Time;
    }

    public KeyEvent getLastKeyPress() {
        if (LastKeyPress == null) {
            System.err.println("their was no last key pressed");
        }
        return LastKeyPress;
    }

    public boolean isDebugCollisons() {
        return DebugCollisons;
    }

    public void setLastKeyPress(KeyEvent LastKeyPress) {
        this.LastKeyPress = LastKeyPress;
    }

    void resetParams() {
        IsDragging = false;
        IsInside = true;
        IsClicking = false;
        Inputadapter = null;
//        MousePos = new Vector(Vector.Zero());
        gameObjs = new ArrayList<IDrawable>();
        timer = null;
        Time = 0;
    }

    public Vector getMousePos() {
        return MousePos;
    }

    public boolean isDragging() {
        return IsDragging;
    }

    public boolean isInside() {
        return IsInside;
    }

    public boolean isClicking() {
        return IsClicking;
    }

    public boolean GetMouseButtonDown(int ind) {
        if (!MouseButtonPressed.containsKey(ind)) {
            return false;
        }
        return MouseButtonPressed.get(ind);
    }
    private int temp1;

    private ILevel get() {
        return this;
    }

    public BufferedImage getOnlineImage(String URL) {
        try {
            return ImageIO.read(new URL(URL));
        } catch (IOException ex) {
            Logger.getLogger(ILevel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

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

    public void OnStart() {
        setFocusable(true);
        setDoubleBuffered(true);
        setVisible(true);
        init();
    }

    public synchronized <T extends IDrawable> T AddObject(T Drawable) {
        gameObjs.add(Drawable);
        Drawable.CoreInit();
        return Drawable;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Time += Game.getDelta();
        Game.SetDelta();
        Update(ae);
        movement();
        checkCollionsions();
        this.repaint();
    }

    public void movement() {
        for (int i = 0; i < gameObjs.size(); i++) {
            gameObjs.get(i).doMove();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (background != null) {
            g.drawImage(background, 0, 0, (int) (Game.getWindowWidth()), (int) (Game.getWindowHeight()), null);
        }
        Font title = new Font("Comic sans serif ms", 0, (int) (Game.ButtonDims().getY() * 30f));
        g2d.setFont(title);
        Draw(g2d);
        if (DebugCollisons) {
            try {
            } catch (Exception e) {

            }

//            int CWH = 6;
//            for (int j = (int) -Transform.getOffsetTranslation().getX() - (int) Game.getScaledWidth() / 2; j < (int) -Transform.getOffsetTranslation().getX() + (int) Game.ggetScaledWidth(); j += CWH) {
//                for (int k = (int) -Transform.getOffsetTranslation().getY() - (int) Game.getScaledHeight() / 2; k < (int) -Transform.getOffsetTranslation().getY() + (int) Game.g.getScaledHeight(); k += CWH) {
//                    boolean draw = false;
//                    for (int i = 0; i < gameObjs.size(); i++) {
//                        if (gameObjs.get(i).getBounds().contains(j, k)) {
//                            draw = true;
//                            break;
//                        }
//                    }
//                    if (draw) {
//                        g.fillRect((int) Transform.getOffsetTranslation().getX() + j, (int) Transform.getOffsetTranslation().getY() + k, CWH, CWH);
//                    }
//                }
//            }
        } else {
        }
        PostUpdate(g2d);
    }

    public IDrawable GetObject(int index) {
        if (gameObjs.get(index) != null) {
            return gameObjs.get(index);
        }
        return null;

    }

    public int GetObjectCount() {
        return gameObjs.size();
    }

    public void PostUpdate(Graphics2D g) {
        try {
            if (gameObjs.size() != 0) {
                for (int i = 0; i < gameObjs.size(); i++) {
                    if (gameObjs.get(i) != null && gameObjs.get(i).isEnabled()) {
                        gameObjs.get(i).CoreUpdate(g);
                        gameObjs.get(i).setIsColliding(false);
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
//        g.dispose();
    }

    public void start() {
        if (Inputadapter == null) {
            Inputadapter = new TAdapter();
            keyadapter = new TAdapter();
        }
        current.timer = new javax.swing.Timer((int) ((float) 1000 / (float) FPS), this);
        timer.start();

        if (getKeyListeners().length == 0) {
            addKeyListener(keyadapter);
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
            removeKeyListener(keyadapter);
        }
        if (getMouseListeners().length != 0) {
            removeMouseListener(Inputadapter);
        }
        if (getMouseMotionListeners().length != 0) {
            removeMouseMotionListener(Inputadapter);
        }

    }

    public void play(String soundResource) {
        play(soundResource, 0);
    }

    public void play(String soundResource, float seconds) {
        play(soundResource, seconds, 0);
    }

    public synchronized void play(String soundResource, float seconds, int LoopAmt) {
//        System.out.println("com.FuturePixels.MainClasses.ILevel.play()");
        MusicUtils.play(soundResource, seconds, LoopAmt);
    }

    protected BufferedImage GetSprite(String URI) {
        BufferedImage g = imageUtils.T.GetImage(URI);
        return g;
    }

    //this is using java swing native functionality (polygon collisions) but their are exameples or raycasting  in use in the player.onCollison function 
    public void checkCollionsions() {
        if (gameObjs.size() <= 1) {
            return;
        }
        for (int i = 0; i < gameObjs.size(); i++) {
            IDrawable a = gameObjs.get(i);
            for (int j = 0; j < gameObjs.size(); j++) {
                IDrawable b = gameObjs.get(j);
                if (i == j || (a == null && b == null) || !(a.isEnabled() && b.isEnabled()) || !(a.IsCollidable() && b.IsCollidable())) {
                    continue;
                }
                if (a != b) {
                    if (a.CheckCollions(b) || b.CheckCollions(a)) {
                        a.setIsColliding(true);
                        a.onCollison(b);
                        b.setIsColliding(true);
                        b.onCollison(a);
                        if (SimpleCollison) {
                            return;
                        }
                    }
                }
            }

        }

//        thePlayer.checkCollision(theTreasure);
    }

    public boolean isSimpleCollison() {
        return SimpleCollison;
    }

    public void setSimpleCollison(boolean SimpleCollison) {
        this.SimpleCollison = SimpleCollison;
    }

    private class TAdapter extends InputAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            if (get() != Game.GetLevel()) {
                return;
            }
            keyRelease(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (get() != Game.GetLevel()) {
                return;
            }
            if (e.getKeyCode() == 120) {
                DebugCollisons = !DebugCollisons;
            }
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
            if (e.getKeyCode() == KeyEvent.VK_F11) {
                try {
                    LevelLoader.LoadLevel(gameObjs.get(0).Level().getClass().newInstance());
                } catch (InstantiationException ex) {
                    Logger.getLogger(ILevel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ILevel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (e.getKeyCode() == 10 && e.isAltDown()) {
                Game.FullScreen();
            } else if (e.getKeyCode() == 10) {
//                Game.toggleCursor();
            }
            LastKeyPress = e;
            keyPress(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            IsClicking = true;
            MouseButtonPressed.put(e.getButton(), true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            MouseButtonPressed.put(e.getButton(), false);
            boolean isactiveOne = false;

            IsDragging = false;

            if (MouseButtonPressed.keySet().toArray().length > 0) {
                Integer[] arr = new Integer[MouseButtonPressed.size()];
                MouseButtonPressed.keySet().toArray(arr);
                for (Integer a : arr) {
                    isactiveOne = isactiveOne != MouseButtonPressed.get(a) || isactiveOne;
                }
                IsClicking = isactiveOne;
            } else {
                IsClicking = false;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
//            start();
            IsInside = true;
        }

        @Override
        public void mouseExited(MouseEvent e) {
//            stop();
            IsInside = false;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            IsDragging = true;
            IsClicking = false;
            MousePos = new Vector(e.getX(), e.getY());
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            MousePos = new Vector(e.getX(), e.getY());
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }
    }

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

    public abstract void init();

    public abstract void Update(ActionEvent ae);

    public abstract void Draw(Graphics2D g);

    public abstract void keyPress(KeyEvent e);

    public abstract void keyRelease(KeyEvent e);

}
