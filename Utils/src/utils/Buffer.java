/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-11-20T20:43:10Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: Buffer.java  
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Makes it easier to handle portions of text being
    written to a text file.</text> 
 */

package utils;

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
public class Buffer {

    private BufferedWriter out;
    
    // should we hold all data into memory or not?
    private boolean holdInMemory = false;
    
    final int bufferLimit = 20000;
    private String bufferLines = "";
    
    public Buffer(final File resultFile){
    
    try {
        out = new BufferedWriter(new FileWriter(resultFile));
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
            Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Adds a piece of text to our buffer
     * @param text 
     */
    public void write(final String text){
        // add the text to our buffer
        bufferLines = bufferLines.concat(text);
        
        // should we keep everything in memory for now?
        if(holdInMemory){
            return;
        }
        
        // do we need to flush this data to disk?
        if(bufferLines.length() > bufferLimit){
            saveToDisk(bufferLines);
            bufferLines = "";
        }
    }

    public boolean isHoldInMemory() {
        return holdInMemory;
    }

    public synchronized void setHoldInMemory(boolean holdInMemory) {
        this.holdInMemory = holdInMemory;
    }
    
    /**
     * Adds some text at the beginning of the buffer, this only works
     * when we are in memory mode
     * @param text 
     */
    public synchronized void setHeaderText(final String text){
        if(isHoldInMemory()==false){
            return;
        }
        // add the text in front of the buffer
        bufferLines = text.concat(bufferLines);
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
        // flush everything from memory
        out.flush();
        // close the stream
        out.close();
        } catch (IOException e){
                System.err.println("Error: " + e.getMessage());
        }
    }
    
    
}
