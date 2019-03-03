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
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author RandomlyFuzzy
 */
public class MusicUtils {

    private static ArrayList<MusicThread> sounds = new ArrayList<MusicThread>();

    public static void play(String soundResource) {
        play(soundResource, 0);
    }

    public static void play(String soundResource, float time) {
        play(soundResource, time);
    }

    public synchronized static void play(String soundResource, float time, int LoopAmt) {
        boolean isPlaying = false;
        for (int i = sounds.size() - 1; i >= 0; i--) {
            MusicThread mt = sounds.get(i);
            if (mt.isFinished()) {
                sounds.remove(mt);
                continue;
            }
            if (!mt.isFinished() && mt.Path == soundResource) {
                isPlaying = true;
                break;
            }
        }
        if (!isPlaying) {
            MusicThread d = new MusicThread(soundResource);
            d.Search(time);
            d.Loop(LoopAmt);
//            d.Sstart();
            sounds.add(d);
        }
    }

    public static void playLastSound() {
        sounds.get(sounds.size() - 1).Start();
    }

    public static void StopASounds(String path) {
        sounds.forEach((A) -> {
            if (A.getPath() == path) {
                A.Stop();
            }
        });
        sounds = new ArrayList<MusicThread>();
    }

    public static void StopAllSounds() {
        sounds.forEach((A) -> {
            A.Stop();
        });
        sounds = new ArrayList<MusicThread>();
    }

    private static class MusicThread {

        private String Path = "";

        private Clip clip;
        private AudioInputStream ais;
        private boolean finished = false, isLooping = true,stoped = false;
        private Thread t = null;

        public boolean isFinished() {
            if (isLooping && finished) {
                Start();
            }
            return finished && !isLooping&&!stoped;
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
            t = new Thread(() -> {
                try {
                    clip.start();
                    Thread.sleep((int) ((ais.getFrameLength() / clip.getFormat().getFrameRate()) * 1000f));
                    finished = true;
                    t.destroy();
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
            clip.loop(amt);
        }

        public String getPath() {
            return Path;
        }
    }

}
