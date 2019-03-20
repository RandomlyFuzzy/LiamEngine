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
 * this is used for simple file operation like read write 
 * and append to make life easier
 * 
 * 
 * @author Liam Woolley 1748910
 */
public class FileUtils {

    /**
     *
     * @param URI local file address
     * @return the contence of a file if exsist else just and empty string 
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
     * @param URI local file address
     * @param Data the data to set the contence to
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
     * @param URI local file address
     * @param Data data to append to the end of the file
     */
    public static void AppendToFile(String URI, String Data) {
        String contence = GetFileContence(URI);
        SetFileContence(URI, contence + Data);
    }

    /**
     *
     * @param URI local file address
     * @param regex a regular expression to split when mached
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
     * @param regex a regular expression to split when mached
     * @param AsCollection arbitary vaiable just because of java method signitures 
     * @return
     */
    public static ArrayList<String> GetFileSplit(String URI, String regex, Object AsCollection) {
        ArrayList<String> ret = new ArrayList<String>();
        String[] contence = GetFileSplit(URI, regex);
        for (String s : contence) {
            ret.add(s);
        }
        return ret;
    }
}
