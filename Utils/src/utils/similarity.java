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
    public static int levenshteinPercentage(String s0, String s1) {
        int percentage =(int) (100 - (float) StringUtils.getLevenshteinDistance(s0, s1) * 100 / (float) (s0.length() + s1.length()));
        return percentage;
    }
    
}
