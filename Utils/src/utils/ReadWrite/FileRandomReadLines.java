/**
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2015-01-26T16:31:23Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: FileRandomReadLines.java  
 * FileCopyrightText: <text> Copyright 2015 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Reads text files from any position of the
 * file, using the benefit of buffered streams.</text> 
 */

package utils.ReadWrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuno Brito, 26th of January 2015 in Darmstadt, Germany
 */
public class FileRandomReadLines {
  
    // internal variables
    private BufferedReader reader = null;
    private RandomAccessFile fileRaf = null;
    private File fileInput = null;
    InputStream is;
    InputStreamReader isr;
    private File file;
    
    // mark the offset on disk
    private long 
            currentLine = 0;
    
    private final int bufferSize = 8192;
    
    public FileRandomReadLines(final File textFileTarget){
        try {
            // get the file where we want to store or read the variables from
            fileInput = textFileTarget;
           
            // doublecheck if the file was really created
            if(fileInput.exists() == false){
                System.out.println("FRLS52 - Critical error, unable to create"
                        + " variable file: " + fileInput.getAbsolutePath());
                return;
            }
           
            // associate the file
            file = textFileTarget;
            
            // initialise the objects from where to read text
            fileRaf = new RandomAccessFile(fileInput, "r");
	
            is = Channels.newInputStream(fileRaf.getChannel());
            isr = new InputStreamReader(is, Charset.forName("UTF-8"));
            
            reader = new BufferedReader(isr, bufferSize);
            
            
        } catch (Exception ex) {
            Logger.getLogger(FileRandomReadLines.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
        }
    }
      
    /**
     * Provides the next line on our text file
     * @return The next line that was available, or null when nothing was read
     * @throws java.io.IOException
     */
    public String getNextLine() throws IOException{
        // get the line to see what this is all about
        final String line = reader.readLine();
            // increase the line count
           if(line != null){
                currentLine++;
                return line;
           }else{
               return null;
           }
    }

    public synchronized long getCurrentOffset() throws IOException {
        return fileRaf.getChannel().position() - bufferSize;
    }

    public long getCurrentLine() {
        return currentLine;
    }
    
    /**
     * Moves to a specific position of the file that is being read
     * @param position 
     * @throws java.io.IOException 
     */
    public void seek(final long position) throws IOException{
            // avoid non-valid values from being requested
            if(position < 0){
                return;
            }
            
            if(position > file.length()){
                return;
            }
            // move pointer to selected position
            fileRaf.seek(position);
            // restart the BufferedReader because we changed the stream
            reader = new BufferedReader(isr, bufferSize);
            //TODO on purpose we don't close reader() before the new object
            // is created. It was causing exceptions to occur, the current
            // approach might create a memory leak but it wasn't confirmed yet.
    }
    
    /**
     * Close all the files that were open
     */
    public void close(){
        try {
            // close the streams
            reader.close();
            fileRaf.close();
            
        } catch (IOException ex) {
            Logger.getLogger(FileRandomReadLines.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    
    /**
     * Test if this implementation is working as expected.
     * @param args 
     * @throws java.io.IOException 
     */
    public static void main(String[] args) throws IOException{
        
        File testFile = new File("build.xml");
        FileRandomReadLines test = new FileRandomReadLines(testFile);
        
        // go to offset 3000 in our test file
        test.seek(3000);
        
        // show the file contents on the console
        String line = null;
        while((line = test.getNextLine())!= null){
            System.out.println(line);
        }
    }
    
    
}
