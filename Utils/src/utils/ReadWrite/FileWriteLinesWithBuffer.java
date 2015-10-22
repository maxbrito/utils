/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-11-20T20:43:10Z
 * LicenseName: AGPL-3.0+
 * FileName: FileWriteLinesWithBuffer.java  
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Makes it easier to handle portions of text being
    written to a text file. Sometimes we need to write millions of times
    to a text file. However, this causes "disk error" under Linux after a
    few hundred thousand writes. This class adds a buffer, keeping in memory
    a significant portion of the data to be written and only writing back
    in periodic intervals. This reduces the number of file writes dramatically.
    Downside is that it will use more RAM (you can define how much).
</text> 
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
 * @author Nuno Brito, 20th of November 2014 in Paris, France
 */
public class FileWriteLinesWithBuffer {

    private BufferedWriter out;
    private FileWriter fileWriter;
    
    // should we hold all data into memory or not?
    private boolean 
            isOpen = false;
    
    
    private int bufferLimit = 5000;
    private String bufferLines = "";
    
    public FileWriteLinesWithBuffer(final File resultFile){
    try {
        fileWriter = new FileWriter(resultFile);
        out = new BufferedWriter(fileWriter);
        } catch (IOException e){
                System.err.println("Error: " + e.getMessage());
        }
    }
 
    /**
     * Creates a new object but appends new lines to the end of a text file
     * @param resultFile
     * @param append 
     */
    public FileWriteLinesWithBuffer(final File resultFile, Boolean append){
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
                out.write(text);
            
        } catch (IOException ex) {
            Logger.getLogger(FileWriteLinesWithBuffer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Adds a piece of text to our buffer
     * @param text 
     */
    public synchronized void write(final String text){
        // add the text to our buffer
        bufferLines = bufferLines.concat(text);
        // do we need to flush this data to disk?
        if(bufferLines.length() > bufferLimit){
            saveToDisk(bufferLines);
            bufferLines = "";
        }
    }

    

    /**
     * How much data is being kept in RAM?
     * @return The size in bytes
     */
    public int getBufferLimit() {
        return bufferLimit;
    }

    /**
     * How much data should we hold in RAM before writing the data to disk?
     * @param bufferLimit The size in bytes
     */
    public void setBufferLimit(int bufferLimit) {
        this.bufferLimit = bufferLimit;
    }
    
    /**
     * Close the buffer and write changes to disk
     */
    public void close(){
    try {
        // do we still have any data left to write?
        if(bufferLines.length() > 0){
            // write it up
            saveToDisk(bufferLines);
        }
        
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
