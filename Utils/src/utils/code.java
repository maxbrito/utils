/**
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (http://triplecheck.de)
 * Created: 2014-08-01T21:02:00Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: Code.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright (c) 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Methods related to source code files </text> 
 */

package utils;

/**
 *
 * @author Nuno Brito, 1st of August 2014 in Darmstadt, Germany
 */
public class code {

     /**
     * Given a piece of source code, count the number of lines that are code.
     * On this calculation, comments are included. However, empty lines aren't
     * @param sourceCodeText A snippet of code
     * @return The number of non-empty lines inside a source code text/file
     */
    public static int getLOC(final String sourceCodeText){
        final String[] lines = sourceCodeText.split("\n");
        int LOC = 0;
        // iterate all lines on the text file
        for(final String line : lines){
            // skip the empty lines
            if(line.isEmpty()){
                continue;
            }
            // count one line
            LOC++;
        }
        return LOC;
    }
    
}
