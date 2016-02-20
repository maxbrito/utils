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

import java.io.File;

/**
 *
 * @author Nuno Brito, 20th of November 2014 in Paris, France
 */
public class FileWriteLinesWithBufferAndException extends FileWriteLinesWithBuffer{

    public FileWriteLinesWithBufferAndException(final File resultFile){
        super(resultFile);
    }
    
    /**
     * Adds a piece of text to our buffer
     * @param text 
     * @throws java.lang.Exception 
     */
    public synchronized void writeWithException(final String text) throws Exception{
        // add the text to our buffer
        bufferLines = bufferLines.concat(text);
        // do we need to flush this data to disk?
        if(bufferLines.length() > bufferLimit){
            saveToDiskWithException(bufferLines);
            bufferLines = "";
        }
    }
    /**
     * Physically write this text to disk
     * @param text 
     * @throws java.lang.Exception 
     */
    protected void saveToDiskWithException(final String text) throws Exception{
            out.write(text);
    }
    
}
