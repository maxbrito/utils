/**
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2015-01-10T10:21:23Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: FileReadLinesSimple.java  
 * FileCopyrightText: <text> Copyright 2015 Nuno Brito, TripleCheck </text>
 * FileComment: <text> A model to read a large text file from disk, processing
 *  each line at maximum speed.</text> 
 */

package utils.ReadWrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Brito, 10th of January 2015 in Darmstadt, Germany
 */
public class FileReadLines {
  
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
                System.out.println("FRLS52 - Critical error, unable to create"
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
        }
    }
      
    /**
     * Provides the next line on our text file
     * @return The next line that was available, or null when nothing was read
     * @throws java.io.IOException
     */
    public String getNextLine() throws IOException{
           final String line = reader.readLine();
            // increase the line count
           if(line != null){
                currentLine++;
                currentOffset += line.length() + 1;
           }
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
   
}
