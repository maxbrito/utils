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

import de.java2html.Java2Html;
import de.java2html.JavaSourceConversionSettings;
import de.java2html.gui.Java2HtmlOptionsPanel;

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
 
    /**
     * Converts a piece of source code to an HTML page
     * @param sourceCode    The input as Java source code
     * @return              The output as HTML code
     */
    static public String convertToHTML(final String sourceCode){
        Java2HtmlOptionsPanel optionsPanel = new Java2HtmlOptionsPanel();
        JavaSourceConversionSettings options = optionsPanel.getConversionSettings();
        options.getConversionOptions().setShowJava2HtmlLink(false);
        options.getConversionOptions().setShowTableBorder(false);
        options.getConversionOptions().setShowFileName(false);
        options.getConversionOptions().setAddLineAnchors(false);
        
        final String 
                s1 = "<!-- = Java Sourcecode to HTML automatically converted code = -->",
                s2 = "<!-- =       END of automatically generated HTML code       = -->";
        
        // convert the code to HTML
        String code = Java2Html.convertToHtmlPage(sourceCode, options);
        // remove everything before and after the <body> tags
        int i0 = code.indexOf("<font color=");
        int i1 = code.indexOf(s1);
        int i2 = code.indexOf(s2);
        // cut the code
        code = code.substring(i0, i2 - s2.length());
        
        // remove the code tags to make the text looks better
        code = code.replaceAll("<code>", "");
        code = code.replaceAll("</font></code>", "</font>");
        
        
        return code;
    }
    
    
}
