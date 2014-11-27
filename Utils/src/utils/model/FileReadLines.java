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
            updateInterval = 5;
    
    // internal variables
    private BufferedReader reader = null;
    private FileReader fileReader = null;
    private File fileInput = null;
    
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
                System.out.println("FR58 - Critical error, unable to create"
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
        }
    }
      
    /**
     * Provides the next line on our text file
     * @return The next line that was available, or null if IOException occurs
     */
    public String getNextLine(){
        try {
            final String line = reader.readLine();
            // increase the offset, include the "\n" character on the count
            // we intentionally ignore Windows "\r\n" line breaks on this code
            currentOffset += line.length() + 1;
            currentLine++;
            return line;
        } catch (IOException ex) {
            return null;
        }
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
            
            reader.close();
            fileReader.close();
            
        } catch (IOException ex) {
            Logger.getLogger(FileReadLines.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    /**
     * Go through all the files on the archive
     * @param big           The bigzip we want to process
     * @throws IOException  If something went wrong
     */
    private void processLines(){
        String sourceCode;
        // iterate all files inside the archive
        while((sourceCode = getNextLine()) != null){
            processSourceCode(sourceCode);
        }
      
    }
   
    /**
     * Do the line counting
     * @param sourceCode    The code to process
     */
    public abstract void processSourceCode(final String sourceCode);
  
    
    /**
     * Launch a thread that will check how the indexing is progressing
     */
    void launchMonitoringThread(){
                Thread thread = new Thread(){
                @Override
                public void run(){
                    utils.time.wait(3);
                    while(true){
                        // get the counter of lines
                        long counterLines = getCurrentLine();
                        // get the number properly formatted
                        final String valueLines 
                                = utils.text.convertToHumanNumbers(counterLines);
                        // output the number of lines already read
                        System.out.println(valueLines + " lines");
                        // just keep waiting
                        utils.time.wait(3);
                        }
                        
                    }
                };
            thread.start();
    }
 
    
    public void processArchive(){
         // transform into a file
        final String fileTemp = utils.files.getCanonical(fileInput);
        // get the final version in clean state
        fileInput = new File(fileTemp);

        // get some output about the processing progress
        launchMonitoringThread();
        // now get to read the source code files in sequence
        processLines();
        // conclude operations
        close();
    }
    
}
