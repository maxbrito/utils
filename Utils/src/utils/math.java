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
    
     /**
     * A distance is not a percentage, but we simplify and compare them to 
     * ease life of the end-user and avoid confusions since a smaller distance,
     * means a higher similarity and this might be understood as smaller percentage
     * of similarity
     * @param distanceProposed
     * @return 
     */
    public static int convertTlshDistanceToTemptivePercentage(final int distanceProposed){
        double distance = distanceProposed;
        // no distance? Assumed as exact match (small possibility this isn't exact)
        if(distance == 0){
            return 100;
        }
        // do the inverse rule of proportion
        double value = 200 / distance; // 200 is the max distance to 0%
        value = 100 / value; // divide the value by 100%
        value = 100 - value; // get the inverse value
        // remove the decimal values, we want a single number
        int result = (int) value;
//        System.out.println("---> Distance: "
//                + distance
//                + "-->"
//                + result
//                + "%");
        return result;
    }
    
}
