/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-10-23T23:53:20Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: TokenizerJava.java  
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> 
    Use this class for generating an outputs that we later use for
    comparing similarities between two files or text
</text> 
 */

package utils;

import org.apache.commons.lang3.StringUtils;


/**
 *
 * @author Nuno Brito, 23rd of October 2014 in Tettnang, Germany
 */
public class similarity {

    /**
     * Compute the similarity between two strings and provide a percentage,
     * doesn't really matter in which order they are compared
     * @param s0    String 1
     * @param s1    String 2
     * @return  A value ranging from 0 to 100%
     */
    public static int levenshteinPercentage(final String s0, final String s1) {
        final int value = StringUtils.getLevenshteinDistance(s0, s1);
        int percentage =(int) (100 - (float) value * 100 / (float) (s0.length() + s1.length()));
        return percentage;
    }

    /**
     * It seems that the Levenshtein Distance algorithm is too expensive in
     * terms of computation. So I wrote a cheaper way to get similar result.
     * @param c1
     * @param c2
     * @return 
     */
    public static int nunoshteinPercentage(final char[] c1, final char[] c2) {
        int points = 0;
        
        int size;
        if(c1.length > c2.length){
            size = c2.length;
        }else{
            size = c1.length;
        }
        
        for(int i = 0; i < size; i++){
            if(c1[i] == c2[i]){
                points++;
            }
        }
        
        // get the percentual value
        return (points * 100) / size;
    }
    
    
    public static void main(String[] args){
        String a1 = "HelloHelloHelloHelloHelloHelloHello";
        String a2 = "HalloHall1HfllfHalloHalloHalloHalloHalloHallow";
        int result = nunoshteinPercentage(a1.toCharArray(), a2.toCharArray());
        System.out.println(result);
    }
    
}
