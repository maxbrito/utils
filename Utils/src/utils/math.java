/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-12-11T12:15:10Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: math.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Math related sub-routines.</text> 
 */

package utils;

import java.util.Random;

/**
 *
 * @author Nuno Brito, 11th of December 2014 in Darmstadt, Germany
 */
public class math {

    /**
     * Returns a random value between a range of values
     * @param min The minimum value
     * @param max The maximum value
     * @return An integer
     */
    public static int random(final int min, final int max){
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
    
}
