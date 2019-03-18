/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Liamengine.Engine.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

/**
 *
 * @author Liam Woolley 1748910
 */
public class FileUtils {

    /**
     *
     * @param URI
     * @return
     */
    public static String GetFileContence(String URI) {
        String str = "";
        FileReader fis = null;
        try {
            File file = new File(URI);
            if (!file.exists()) {
                file.createNewFile();
            }
            fis = new FileReader(file);
            char[] data = new char[(int) file.length()];
            fis.read(data);
            str = new String(data);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return str;
    }

    /**
     *
     * @param URI
     * @param Data
     */
    public static void SetFileContence(String URI, String Data) {
        File file = new File(URI);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        PrintStream FileStream = null;
        try {
            FileStream = new PrintStream(new File(URI));
            FileStream.print(Data);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            FileStream.close();
        }
    }

    /**
     *
     * @param URI
     * @param Data
     */
    public static void AppendToFile(String URI, String Data) {
        String contence = GetFileContence(URI);
        SetFileContence(URI, contence + Data);
    }

    /**
     *
     * @param URI
     * @param regex
     * @return
     */
    public static String[] GetFileSplit(String URI, String regex) {
        File file = new File(URI);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String contence = GetFileContence(URI);
        contence = contence.trim();
        return contence.split(regex);
    }

    /**
     *
     * @param URI
     * @param regex
     * @param AsCollection
     * @return
     */
    public static ArrayList<String> GetFileSplit(String URI, String regex, Object AsCollection) {
        ArrayList<String> ret = new ArrayList<String>();
        String[] contence = GetFileSplit(URI, regex);
        System.out.println("com.FuturePixels.Engine.Utils.FileUtils.GetFileSplit() " + contence.length);
        for (String s : contence) {
            ret.add(s);
        }
        return ret;
    }
}
