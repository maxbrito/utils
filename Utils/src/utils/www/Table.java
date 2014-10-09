/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-08-01T14:40:05Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: Table.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> 
    Fast way to create an HTML table
</text> 
 */

package utils.www;

import java.util.ArrayList;

/**
 *
 * @author Nuno Brito, 1st of August 2014 in Darmstadt, Germany
 */
public class Table {
    
    
        
    String 
            headerData = "",
            footerData = "<tfoot></tfoot>\n";
    
    ArrayList<String> itemData = new ArrayList();
    
    // these values don't change after assigned on the first time
    final String
            tableHeader;

    
    /**
     * Public constructor, define the header of our table
     * @param className What is the class name of the table(used on CSS styling)
     */
    public Table(final String className){
        if(className == null){
            tableHeader = "<table class=\"table striped margin20\">\n";
        } else{
            tableHeader = "<table class=\""
                    + className
                    + "\">\n";
        }
    }
    
    /**
     * Public constructor, define the header of our table
     * @param header    The column titles
     * @param headerClass The class name for each column
     */
    public void setHeader(final String[] header, 
            final String headerClass){
        
        // default value for the header class
        String headerDataClass;  
        if(headerClass == null){
            headerDataClass = " class=\"text-left\"";
            // use the assigned value
        }else{
            headerDataClass = " class=\""
                    + headerClass
                    + "\"";
        }
        
        String result = "";
        for(String item : header){
            result += 
                    "\t\t<th"
                    + headerDataClass
                    + ">"
                    + item 
                    + "</th>\n"
                    ;
        }
        // do the header
        headerData = 
                "<thead>\n"
                + "\t<tr>\n"
                + result
                + "\t</tr>\n"
                + "</thead>\n"
                    ;
    }

    public void addData(final String[] data, 
            final String dataClass){
        
        // default value for the header class
        String derDataClass;  
        if(dataClass == null){
            derDataClass = " class=\"right\"";
            // use the assigned value
        }else{
            derDataClass = " class=\""
                    + dataClass
                    + "\"";
        }
        
        String result = "";
        for(String item : data){
            result += 
                    "\t\t<td"
                    + derDataClass
                    + ">"
                    + item 
                    + "</td>\n"
                    ;
        }
        
        result =  "\t<tr>\n"
                + result
                + "\t</tr>\n"
                ;
       
        // add the item on our list
        itemData.add(result);
    }

    
    /**
     * Get the resulting table
     * @return 
     */
    public String getResult() {
        
        String itemOutput = "";
        for(String item : itemData){
            itemOutput += item;
        }
        
        return tableHeader
                + headerData
                    + "<tbody>\n"
                             + itemOutput
                    + "</tbody>\n"
                + footerData
                + "</table>"
                ;
    }

}
