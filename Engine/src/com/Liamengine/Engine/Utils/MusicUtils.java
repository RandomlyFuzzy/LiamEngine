/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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
 * handles all the audio interactions and storage of the audio created should
 * have used the minim audio libary but this was already done so yeah
 *
 * @author Liam Woolley 1748910
 */
public class MusicUtils {

    /**
     * all music threads currenly running
     */
    private static ArrayList<MusicThread> sounds = new ArrayList<MusicThread>();
    /**
     * defualt volume
     *
     * @see Musicutil#SetVolume
     * @see MusicThread#setVolume
     */
    private static float val = 1f;

    /**
     *
     * @param soundResource local file address
     */
    public static void play(String soundResource) {
        play(soundResource, 0);
    }

    /**
     *
     * @param soundResource local file address
     * @param time start time of the audio
     * @see MusicThread#Search
     */
    public static void play(String soundResource, float time) {
        play(soundResource, time);
    }

    /**
     *
     * @param soundResource local file address
     * @param time start time of the audio
     * @param LoopAmt amount of times to be looped
     * @see MusicThread#Search
     * @see #MusicThread
     */
    public synchronized static void play(String soundResource, float time, int LoopAmt) {
        //local variable to check if currently running (stops spam)
        boolean isPlaying = false;
        //if sounds are playing
        if (sounds.size() != 0) {
            //look through all sounds backwards(so things can be removed without worry)
            for (int i = sounds.size() - 1; i >= 0; i--) {
                //get sound
                MusicThread mt = sounds.get(i);
                //check to see if finished
                if (mt.isFinished()) {
                    //remove it 
                    sounds.remove(mt);
                    //next i value
                    continue;
                } else //if is the currently searched for audio
                if (mt.Path == soundResource) {
                    //its found so it doesnt need to be replayed
                    isPlaying = true;
                    break;
                }
            }
        }
        //if not found or the collection is empty
        if (!isPlaying) {
            try {
                //create new audio using sound resouce
                MusicThread d = new MusicThread(soundResource);
                //set to the default value
                d.setVolume(val);
                //go to this place in the audio clip
                d.Search(time);
                //set whether or not it should loop and how much
                d.Loop(LoopAmt);
                //start the clip
                d.Start();
                //add to the collection
                sounds.add(d);
            } catch (Exception ex) {
                System.err.println("their was an error running " + soundResource);
            }
        }
    }

    /**
     * replay last audio added to the collection if their was anyplayed before
     * it and not finished
     */
    public static void playLastSound() {
        if (sounds.size() == 0) {
            return;
        }
        sounds.get(sounds.size() - 1).Start();
    }

    /**
     *
     * @param path path of the file to stop
     */
    public static void StopASounds(String path) {
        if (sounds.size() == 0) {
            return;
        }
        sounds.forEach((A) -> {
            if (A.getPath() == path) {
                A.Stop();
                System.out.println("sound stopped of path " + path);
            }
        });
    }

    /**
     * stops all currently playing music threads
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
     * @param Val value to set all the volumes to should be between 0.0 ... 1.0
     *
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

    /**
     * @return the current global volume
     */
    public static float GetVolume() {
        return val;
    }

    /**
     * this is created for each piece of audio loaded
     */
    private static class MusicThread {

        /**
         * path of the Audio was created from
         */
        private String Path = "";
        /**
         * clip to play from
         */
        private Clip clip;
        /**
         * current stream for the audio
         */
        private AudioInputStream ais;
        /**
         * true if the thread has finished
         */
        private boolean finished = false;
        /**
         * true if loop wasnt set to 0
         */
        private boolean isLooping = false;
        /**
         * true if forcably stoped
         */
        private boolean stoped = false;
        /**
         * current audio thread reference
         */
        private Thread t = null;

        /**
         * @param Source the local uri of the audio file
         */
        public MusicThread(String Source) {
            super();
            try {
                this.Path = Source;
                clip = AudioSystem.getClip();
                //this extra stream is needed for ioexception mark/reset that accurres 
                FileInputStream is = new FileInputStream(new File("resources"+Source));
                BufferedInputStream myStream = new BufferedInputStream(is); 
                this.ais = AudioSystem.getAudioInputStream(myStream);
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

        /**
         * @return returns the local definishion of finished
         */
        public boolean isFinished() {
            if (isLooping && finished) {
                t.destroy();
                Start();
            }
            return (finished && !isLooping) || stoped;
        }

        /**
         * @return gets the currently playing audio clip
         */
        public Clip getClip() {
            return clip;
        }

        /**
         * @param time set the current postition in the audio clip in seconds
         */
        public void Search(float time) {
            if (time == 0) {
                return;
            }
            //check to see if past the file length
            if (time * clip.getFormat().getFrameRate() >= ais.getFrameLength()) {
                System.err.println("input size to big");
            } else {
                //sets the current position of the to that of Time * the refresh rate of the audio (time * the amount of updates per second)
                clip.setFramePosition(((int) (time * clip.getFormat().getFrameRate())));
            }
        }

        /**
         * starts the audio and creates a way to check when finished
         */
        public void Start() {
            //sets finished to to not finished
            finished = false;
            //creates a new thread
            t = new Thread(() -> {
                try {
                        //starts the audio
                        clip.start();
                        //waits the file duration (should be the file length - start place) in milliseconds
                        Thread.sleep((int) ((ais.getFrameLength() / clip.getFormat().getFrameRate()) * 1000f));
                        //if is not looping
                        if (!isLooping) {
                            //stop the music and define it as finsihed
                            finished = true;
                            Stop();
                        }
                        //stop the thread 

                    t.stop();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MusicUtils.class.getName()).log(Level.SEVERE, null, ex);
                }catch(Exception ex){
                    System.err.println("their was an error running "+Path);
                }
            });
            //starts thread
            t.start();
        }

        /**
         * cleans up streams and audio and defines as forcably stoped
         */
        public void Stop() {
            try {
                stoped = true;
                clip.stop();
                clip.flush();
                ais.close();
            } catch (IOException ex) {
                Logger.getLogger(MusicUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * @param amt set the loop amount to that
         */
        public void Loop(int amt) {
            if (amt != 0) {
                isLooping = true;
            }
            clip.loop(amt);
        }

        /**
         * @param val the volume of the audio
         */
        public void setVolume(float val) {
            //controls the audio volume
            FloatControl fc = null;
            //aparently there are multiple audio middleware and they use different things that why this is here
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } else if (clip.isControlSupported(FloatControl.Type.VOLUME)) {
                fc = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
            } else {
                System.err.println("their is no volume control supported shutting down Audio");
                return;
            }
            //maps the current volume from 0 ... 1 to minval ... max value
            // dont know the decibels gain so just did this generic implementation
            fc.setValue(Map(val, 0f, 1f, 0.5f + fc.getMinimum(), fc.getMaximum() - 0.1f));
            //System.out.println("" + fc.getValue());
        }

        /**
         *
         * @param X value to map
         * @param A X min
         * @param B X max
         * @param C map to min
         * @param D map to max
         */
        private float Map(float X, float A, float B, float C, float D) {
            return //offset the value to go to from 0 ... B - A
                    (X - A)
                    //check to see how far it is to b relative to (percent and decimal 0 ... 1)
                    / (B - A)
                    //mutiply that that by the map maximum offset to between 0 ... Max Map - Min map
                    * (D - C)
                    // offset to the proper start position from 0 ... D-C to C ... D
                    + C;
        }

        //get the currently loaded file path
        public String getPath() {
            return Path;
        }
    }

}
