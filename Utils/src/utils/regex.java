/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-08-05T22:57:12Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: regex.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text>
    Methods related to source code handling and other text substitution
    operations that involve regular expressions.
 </text> 
 */

package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nuno Brito, 5th of August 2014 in Darmstadt, Germany
 */
public class regex {

    /**
     * For our tokenization process we don't need the value placed within the
     * string quotes
     * @param line  The line from where we want to extract quotes
     * @return the line of text with the resulting changes
     */
    public static String replaceQuotesWithKeyword(final String line){
        String text = line; //"some \"\" string with \"hello\" inside \"and inside\"";
        Pattern pattern = Pattern.compile("\"[^\"]*\"");
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()){
            text = text.replace(matcher.group(), "##");
            //System.out.println(matcher.group());
        }
//        System.out.println("->" + text);
//        System.exit(-1);
        return text;
    }
    
    /**
     * Replace the method declarations with a defined keyword
     * @param line  The line from where we want to extract quotes
     * @return  A text with the applied changes (if any)
     */
    public static String replaceMethodsWithKeyword(final String line){
        String text = line;
        Pattern pattern = Pattern.compile(
                "\\b[a-z\\.A-Z]+\\b(?=\\([a-z\\[\\]]*)"
               // \b[a-z\.A-Z]+\b(?=\([a-z\[\]]*)
        );
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()){
            // get what was found
            final String match = matcher.group().toLowerCase();
            // set the default result
            String result = "ºM";
            
            // was this a boolean literal?
            if(utils.text.equals(match, "if")){
                result = "ºIF";
            }else
            if(utils.text.equals(match, "for")){
                result = "ºFOR";
            }else
            if(utils.text.equals(match, "while")){
                result = "ºWH";
            }
                
                
            text = text.replace(matcher.group(), result);
        }
        return text;
    }

    /**
     * Replace the method declarations with a defined keyword
     * @param line  The line from where we want to extract quotes
     * @return  A text with the applied changes (if any)
     */
    public static String replaceVariablesWithKeyword(final String line){
        String text = line;
        Pattern pattern = Pattern.compile("([_A-Za-z0-9\\.]{1,50})(?=(| )[\\;\\,\\+-\\=\\<\\>\\)\\[\\!])");
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()){
            // get what was found
            final String match = matcher.group();
            // set the default result
            String result = "ºV";
            
            // was this a boolean literal?
            if(utils.text.equals(match, "true")){
                result = "ºTE";
            }else
            if(utils.text.equals(match, "false")){
                result = "ºF";
            }else
            if(utils.text.equals(match, "null")){
                result = "ºNU";
            }
            else
            if(utils.text.equals(match, "return")){
                result = "ºR";
            }
            
            // do the normal variable convert
            text = text.replace(matcher.group(), result);
            
        }
        return text;
    }
    
    
        
    /**
     * Replace the method declarations with a defined keyword
     * @param oldKeyword    The old keyword we want to change
     * @param newKeyword    The replacements
     * @param line  The line from where we want to extract quotes
     * @return  A text with the applied changes (if any)
     */
    public static String replaceWithKeyword(final String oldKeyword, final String newKeyword,
            final String line){
        String text = line;
        Pattern pattern = Pattern.compile(""
                + "\\b"+oldKeyword+"\\b"
        ); 
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()){
            text = text.replace(matcher.group(), newKeyword);
        }
        return text;
    }
    
    /**
     * Removes all the empty spaces, tab symbols and related white space
     * characters from a given line of text
     * @param line  The line to process
     * @return      The line without white-spaces
     */
    public static String removeWhiteSpaces(final String line){
        return line.replaceAll("\\s+","");
    }
    
    
    
}
