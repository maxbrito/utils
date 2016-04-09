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
        text = text.replaceAll("\"[^\"]*\"", "##");

        return text;
    }
    
    /**
     * Replace the method declarations with a defined keyword
     * @param line  The line from where we want to extract quotes
     * @return  A text with the applied changes (if any)
     */
    public static String replaceMethodsWithKeyword(final String line){
        String text = line;
        text = text.replaceAll("\\b(if|IF|If)\\b(?=\\([a-z\\[\\]]*)", "ºIF");
        text = text.replaceAll("\\b(for|FOR|For)\\b(?=\\([a-z\\[\\]]*)", "ºFOR");
        text = text.replaceAll("\\b(while|WHILE|While)\\b(?=\\([a-z\\[\\]]*)", "ºWH");
        text = text.replaceAll("\\b[a-z\\.A-Z]+\\b(?=\\([a-z\\[\\]]*)", "ºM");

        return text;
    }

    /**
     * Replace the method declarations with a defined keyword
     * @param line  The line from where we want to extract quotes
     * @return  A text with the applied changes (if any)
     */
    public static String replaceVariablesWithKeyword(final String line){
        String text = line;
        text = text.replaceAll("(true)(?=(| )[\\;\\,\\+-\\=\\<\\>\\)\\[\\!])", "ºTE");
        text = text.replaceAll("(false)(?=(| )[\\;\\,\\+-\\=\\<\\>\\)\\[\\!])", "ºF");
        text = text.replaceAll("(null)(?=(| )[\\;\\,\\+-\\=\\<\\>\\)\\[\\!])", "ºNU");
        text = text.replaceAll("(return)(?=(| )[\\;\\,\\+-\\=\\<\\>\\)\\[\\!])", "ºR");
        text = text.replaceAll("([_A-Za-z0-9\\.]{1,50})(?=(| )[\\;\\,\\+-\\=\\<\\>\\)\\[\\!])", "ºV");

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
        text = text.replaceAll("\\b"+oldKeyword+"\\b", newKeyword);

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
