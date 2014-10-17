/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-04-04T00:00:00Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: misc.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> 
    Copyright 2014 Nuno Brito, TripleCheck
    Copyright 2013 Zig (http://mindprod.com/jgloss/compiletimestamp.html)
    Copyright (c) 2008-2014 Mkyong.com, all rights reserved.
</text>
 * FileComment: <text> Misc. methods </text> 
 */

package utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 *
 * @author Nuno Brito, 4th of April 2014 in Darmstadt, Germany.
 *  nuno.brito@triplecheck.de | http://nunobrito.eu
 */
public class misc {

     /**
     * Calculate a precise percentage on the value
     * @param value the value from which we want a percentage assigned
     * @param max the max value that represents 100%
     * @return a string representing the percentual value
     */
    public static String getPercentage(int value, int max){
        double dMax = max;
        double dValue = value;
        double per = (dValue*100)/ dMax;
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(per);
    }
    
    
    /**
     * Sort an hashmap according to its value.
     * @param unsortMap
     * @return      A sorted map according to the highest value
     * @origin http://www.mkyong.com/java/how-to-sort-a-map-in-java/
     * @license CC-BY-SA-3.0
     * @retrieved 2014-04-24 by Nuno Brito
     */
   
    public static Map sortByComparator(Map unsortMap) {
 
		List list = new LinkedList(unsortMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
                        @Override
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
                                       .compareTo(((Map.Entry) (o1)).getValue());
			}
		});
 
		// put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
    
    
   /**
    * Gets the date when a given class was created in ISO format
    * @param aClass     the target class
    * @return           a string containing the creation date
    */ 
   public static String getDate(Class aClass){
       String result = "";
        try {
            Date date = getCompileTimeStamp(aClass.getComponentType());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            result = "from " + df.format(date);
        } catch (IOException ex) {
        }
       
       return result;
   } 
    
/**
 * get date a class was compiled by looking at the corresponding class file in the jar.
 * @origin http://mindprod.com/jgloss/compiletimestamp.html
 * @retrieved in 2013-11-17 by Nuno Brito
     * @param cls   Class that we want to evaluate the time stamp
     * @return      The time/date result
     * @throws java.io.IOException
 */
public static Date getCompileTimeStamp( Class<?> cls ) throws IOException
{
   ClassLoader loader = cls.getClassLoader();
   String filename = cls.getName().replace('.', '/') + ".class";
   // get the corresponding class file as a Resource.
   URL resource=( loader!=null ) ?
                loader.getResource( filename ) :
                ClassLoader.getSystemResource( filename );
   URLConnection connection = resource.openConnection();
   // Note, we are using Connection.getLastModified not File.lastModifed.
   // This will then work both or members of jars or standalone class files.
   long time = connection.getLastModified();
   return( time != 0L ) ? new Date( time ) : null;
}

    /**
     * Are we running under a Windows machine or not?
     * @return      True when we are running Windows as host
     */
    public static boolean isWindows(){
            String os = System.getProperty("os.name").toLowerCase();
            //windows
        return (os.contains("win"));
    }
    

}
