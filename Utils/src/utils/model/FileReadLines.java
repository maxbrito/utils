/**
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-11-27T11:20:23Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: FileReadLines.java  
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> A model to read a large text file from disk, processing
 *  each line at maximum speed.</text> 
 */

package utils.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Brito, 27th of November 2014 in Tettnang, Germany
 */
public abstract class FileReadLines {
  
    // available options
    protected int 
            monitorWaitingTime = 3;
    
    // internal variables
    private BufferedReader reader = null;
    private FileReader fileReader = null;
    private File fileInput = null;
    private boolean isRunning = false;
    
    // mark the offset on disk
    private long 
            currentOffset = 0,
            currentLine = 0;
    
    
    public FileReadLines(final File textFileTarget){
        try {
            // get the file where we want to store or read the variables from
            fileInput = textFileTarget;
           
            // doublecheck if the file was really created
            if(fileInput.exists() == false){
                System.out.println("FR52 - Critical error, unable to create"
                        + " variable file: " + fileInput.getAbsolutePath());
                return;
            }
           
            // initialise the objects from where to read text
            fileReader = new FileReader(fileInput);
            reader = new BufferedReader(fileReader);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileReadLines.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileReadLines.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            isRunning = false;
        }
    }
      
    /**
     * Provides the next line on our text file
     * @return The next line that was available, or null if IOException occurs
     */
    private String getNextLine() throws IOException{
           final String line = reader.readLine();
            // increase the line count
            currentLine++;
            return line;
    }

    public long getCurrentOffset() {
        return currentOffset;
    }

    public long getCurrentLine() {
        return currentLine;
    }
    
    /**
     * Close all the files that were open
     */
    public void close(){
        try {
            // close the streams
            reader.close();
            fileReader.close();
            
        } catch (IOException ex) {
            Logger.getLogger(FileReadLines.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    /**
     * Go through all the files on the archive
     */
    private void processLines(){
        String line;
        // iterate all files inside the archive
        try{
            while((line = getNextLine()) != null){
                 // increase the offset, include the "\n" character on the count as +1
                 // and we intentionally ignore Windows "\r\n" line breaks on this code
                currentOffset += line.length() + 1;
                // call the abstract method
                processTextLine(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            // output the error coordinates
            System.out.println("Failed to read line "
                + getCurrentLine() + " at offset " + getCurrentOffset()
            );
        }finally{
            // we are no longer running
            isRunning = false;
        }
    }
   
    /**
     * Do the line processing on a line-by-line basis
     * @param textLine  The line to process
     */
    public abstract void processTextLine(final String textLine);
    
    /**
     * The message called when we have interest in seeing a progress message
     */
    public abstract void monitorMessage();

    public int getMonitorWaitingTime() {
        return monitorWaitingTime;
    }

    public void setMonitorWaitingTime(int monitorWaitingTime) {
        this.monitorWaitingTime = monitorWaitingTime;
    }

    public boolean isIsRunning() {
        return isRunning;
    }
    
    /**
     * Launch a thread that will check how the indexing is progressing
     */
    void launchMonitoringThread(){
        Thread thread = new Thread(){
        @Override
        public void run(){
            // initial waiting period to let things start
            utils.time.wait(monitorWaitingTime);
            while(isRunning){
                // call the monitoring message
                monitorMessage();
                // just keep waiting
                utils.time.wait(monitorWaitingTime);
                }
            }
        };
        // launch the thread
        thread.start();
    }
 
    /**
     * Go from top to bottom on a text file
     */
    public void processTextFile(){
         // transform into a file using the canonical path
        final String fileTemp = utils.files.getCanonical(fileInput);
        // get the final version in clean state
        fileInput = new File(fileTemp);
        // mark the flag as running
        isRunning = true;
        // get some output about the processing progress
        launchMonitoringThread();
        // now get to read the source code files in sequence
        processLines();
        // conclude operations
        close();
    }
    
}
