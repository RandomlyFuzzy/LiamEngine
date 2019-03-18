/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Liam Woolley 1748910
 */
public class MusicUtils {

    private static ArrayList<MusicThread> sounds = new ArrayList<MusicThread>();
    private static float val = 1f;

    /**
     *
     * @param soundResource
     */
    public static void play(String soundResource) {
        play(soundResource, 0);
    }

    /**
     *
     * @param soundResource
     * @param time
     */
    public static void play(String soundResource, float time) {
        play(soundResource, time);
    }

    /**
     *
     * @param soundResource
     * @param time
     * @param LoopAmt
     */
    public synchronized static void play(String soundResource, float time, int LoopAmt) {
        boolean isPlaying = false;
        if (sounds.size() != 0) {
            for (int i = sounds.size() - 1; i >= 0; i--) {
                MusicThread mt = sounds.get(i);
                if (mt.isFinished()) {
                    sounds.remove(mt);
                    continue;
                } else if (mt.Path == soundResource) {
                    isPlaying = true;
                    break;
                }
            }
        }
        if (!isPlaying) {
            MusicThread d = new MusicThread(soundResource);
//            d.setVolume(val);
            d.Search(time);
            d.Loop(LoopAmt);
            d.Start();
            sounds.add(d);
        }
    }

    /**
     *
     */
    public static void playLastSound() {
        if (sounds.size() == 0) {
            return;
        }
        sounds.get(sounds.size() - 1).Start();
    }

    /**
     *
     * @param path
     */
    public static void StopASounds(String path) {
        if (sounds.size() == 0) {
            return;
        }
        sounds.forEach((A) -> {
            if (A.getPath() == path) {
                A.Stop();
            }
        });
        sounds = new ArrayList<MusicThread>();
    }

    /**
     *
     */
    public static void StopAllSounds() {
        if (sounds.size() == 0) {
            return;
        }
        sounds.forEach((A) -> {
            A.Stop();
        });
        sounds = new ArrayList<MusicThread>();
    }

    /**
     *
     * @param Val
     */
    public static void SetVolume(float Val) {
        if (sounds.size() == 0) {
            return;
        }
        sounds.forEach((A) -> {
            A.setVolume(Val);
        });
        val = Val;
    }

    private static class MusicThread {

        private String Path = "";
        private Clip clip;
        private AudioInputStream ais;
        private boolean finished = false, isLooping = false, stoped = false;
        private Thread t = null;

        public boolean isFinished() {
            if (isLooping && finished) {
                Start();
            }
            return (finished && !isLooping) || stoped;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public MusicThread(String Source) {
            super();
            try {
                this.Path = Source;
                clip = AudioSystem.getClip();
                this.ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(Source));
                clip.open(ais);

            } catch (LineUnavailableException ex) {
                Logger.getLogger(MusicUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MusicUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(MusicUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                System.err.println(Source);
                Logger.getLogger(MusicUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        public Clip getClip() {
            return clip;
        }

        public void Search(float time) {
            if (time * clip.getFormat().getFrameRate() >= ais.getFrameLength()) {
                System.err.println("input size to big");
            } else {
                clip.setFramePosition(((int) (time * clip.getFormat().getFrameRate())));
            }
        }

        public void Start() {
            finished = false;
            t = new Thread(() -> {
                try {
                    clip.start();
                    Thread.sleep((int) ((ais.getFrameLength() / clip.getFormat().getFrameRate()) * 1000f));
                    if (!isLooping) {
                        finished = true;
                        Stop();
                    }
                    t.stop();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MusicUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            t.start();
        }

        public void Stop() {
            try {
                clip.stop();
                clip.flush();
                ais.close();
                stoped = true;
            } catch (IOException ex) {
                Logger.getLogger(MusicUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void Loop(int amt) {
            if (amt == Clip.LOOP_CONTINUOUSLY) {
                isLooping = true;
            }
            clip.loop(amt);
        }

        public void setVolume(float val) {
            FloatControl fc = null;
            if (clip.getControl(FloatControl.Type.MASTER_GAIN) != null) {
                fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                fc = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
            }
            fc.setValue(Map(val, 0f, 1f, 0.5f + fc.getMinimum(), fc.getMaximum() - 0.1f));
            System.out.println("" + fc.getValue());
        }

        private float Map(float X, float A, float B, float C, float D) {
            return (X - A) / (B - A) * (D - C) + C;
        }

        public String getPath() {
            return Path;
        }
    }

}
