/*
 * A class to save and load settings from disk using XML and simple properties
 * class built-in support for XML
 */
package utils;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Brito, 25th of December 2012 in Kirchberg, Austria.
 */
public class Settings {
    
// 2013-MAY-11 psc create properties in constructor
    private Properties properties;
    private File settingsFile;
    private String comment = "";
    
    
    public Enumeration getAllKeys(){
        return properties.keys();
    }
    
    /**
     * Creates a new settings object where you can keep pairs of key-values that
     * will be kept on disk
     * 
     * @param settingsFile The XML file where all settings will be kept
     */
    public Settings(File settingsFile, String comment){
        properties = new Properties();
        this.settingsFile = settingsFile;
        if(settingsFile == null){
            return;
        }
        this.comment = comment;
        if(settingsFile.exists()){
            load();
        }
    }
    
    /**
     * Writes a key in the settings, each written key is automatically written
     * back to the corresponding XML file.
     * @param key
     * @param value 
     */
    public void write(String key, String value){
        // for some odd reason we need to physically delete old values
        properties.remove(key);
        save();
        // only now can we write them back
        properties.setProperty(key, value);
        save();
    }
    
    /**
     * Removes a given key from the settings
     * @param key 
     */
    public void delete(String key){
        properties.remove(key);
        save();
    }
    
    /**
     * Simply saves all contents of this object at an XML file on disk
     */
    private boolean save(){
         OutputStream out;
        try {
            out = new FileOutputStream(settingsFile);
            properties.storeToXML(out, comment);
// 2013-MAY-11 psc close it!
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    
    /**
     * If a given XML file with settings already exist, then try to load it up
     * and get the values back
     */
    private void load(){
         InputStream in;
        try {
            in = new FileInputStream(settingsFile);
            properties.loadFromXML(in);
// 2013-MAY-11 psc close it!            
            in.close();
        } catch (IOException ex) {}
    } 

    /**
     * Returns the value of a given key, if no key exists then the return value
     * is empty.
     */
    public String read(String key){
        return properties.getProperty(key);
    }
    
    /**
     * Returns the value of a given key. If the key does not exist then it will
     * return the alternative value instead.
     * @param key
     * @param alternative
     */
    public String read(String key, String alternative){
        return properties.getProperty(key, alternative);
    }
    
    
    public Boolean hasKey(String key){
        return properties.containsKey(key);
    }
    
    
    public static void main(String[] args) {
        
        Settings set = new Settings(new File("test1234.xml"), "testing the "
                + "settings to see how they work");
        
        set.write("test", "1234");
        set.write("test1", "1234aas");
        set.write("test", "1234343");

        String test = set.read("test");
        
        if(test.isEmpty()==false) {
            System.out.println(test);
        }
        
        
    }

    public Properties getProperties() {
        return properties;
    }
    
    
    
}
