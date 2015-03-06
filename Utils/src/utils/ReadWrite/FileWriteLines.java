/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2015-01-05T18:20:01Z
 * LicenseName: AGPL-3.0+
 * FileName: FileWriteLinesWithBuffer.java  
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Makes it easier to handle portions of text being
    written to a text file. </text> 
 */

package utils.ReadWrite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Brito, 5th of January 2015 in Darmstadt, Germany
 */
public class FileWriteLines {

    private BufferedWriter out;
    private FileWriter fileWriter;
    
    // should we hold all data into memory or not?
    private boolean 
            isOpen = false;
    
    public FileWriteLines(final File resultFile){
    try {
        fileWriter = new FileWriter(resultFile);
        out = new BufferedWriter(fileWriter);
        isOpen = true;
        } catch (IOException e){
                System.err.println("Error: " + e.getMessage());
        }
    }
 
    /**
     * Creates a new object but appends new lines to the end of a text file
     * @param resultFile
     * @param append 
     */
    public FileWriteLines(final File resultFile, Boolean append){
    try {
        // does the folder for this file exists?
        if(resultFile.getParentFile().exists() == false){
            // create one
            utils.files.mkdirs(resultFile.getParentFile());
        }
        
        // try to create the file if it does not exist
        if(resultFile.exists() == false){
            utils.files.touch(resultFile);
        }
        
        fileWriter = new FileWriter(resultFile, append);
        out = new BufferedWriter(fileWriter);
        isOpen = true;
        } catch (IOException e){
                System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Physically write this text to disk
     * @param text 
     */
    private void saveToDisk(final String text){
        try {
            if(out != null && isOpen){
                out.write(text);
                //out.flush();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(FileWriteLines.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Adds a piece of text to our buffer
     * @param text 
     */
    public void write(final String text){
        saveToDisk(text);
    }

    /**
     * Close the buffer and write changes to disk
     */
    public void close(){
    try {
       
        isOpen = false;
        // flush everything from memory
        out.flush();
        // close the stream
        out.close();
        } catch (IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }

}
