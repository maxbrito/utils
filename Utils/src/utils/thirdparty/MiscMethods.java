/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-04-10T00:00:00Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: MiscMethods.java  
 * FileType: SOURCE
 * FileCopyrightText: <text>
 * Copyright 2014 Nuno Brito, TripleCheck
 * Copyright 2010 Aterai (https://community.oracle.com/people/aterai)
 * Copyright 2010 Mkyong (http://www.mkyong.com/)  
 * </text>
 * FileComment: <text> Code from other authors. Permission and credits 
 * are assigned where possible. This class does not intend to keep a complete
 * list of all the third-party snippets, it is used whenever possible.</text> 
 */

package utils.thirdparty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/**
 *
 * @author Nuno Brito, 15th of May 2014 in Darmstadt, Germany.
 *  nuno.brito@triplecheck.de | http://nunobrito.eu
 */
public class MiscMethods {

    
    /**
     * Run a command on the local system
     * @param command The command to read
     * @author mkyong (January 8, 2014)  
     * @return   The result from the console
     * @origin http://www.mkyong.com/java/how-to-execute-shell-command-from-java/
     * @license CC-BY-SA-3.0
     * @retrieved 2014-12-01 by Nuno Brito
     */
    public static String executeCommand(final String command) {
		
        StringBuilder output = new StringBuilder();
 
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            InputStreamReader i = new InputStreamReader(p.getInputStream());
            BufferedReader reader = 
                new BufferedReader(i);

            String line;			
            while ((line = reader.readLine())!= null) {
                    output.append(line).append("\n");
                    System.out.println(line);
            }

            i.close();
            reader.close();
        } catch (IOException e) {
                e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
    
     
    /**
     * Sort an hashmap according to its value.
     * @param unsortMap
     * @return      A sorted map according to the highest value
     * @author mkyong (July 7, 2010)  
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
     * Sort an hashmap according to its value.
     * @param unsortMap
     * @return      A sorted map according to the highest value
     * @origin http://www.mkyong.com/java/how-to-sort-a-map-in-java/
     * @license CC-BY-SA-3.0
     * @retrieved 2014-04-24 by Nuno Brito
     */
    public static Map sortByComparatorSmallerFirst(Map unsortMap) {
 
		List list = new LinkedList(unsortMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
                        @Override
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
                                       .compareTo(((Map.Entry) (o2)).getValue());
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
    
    
}
