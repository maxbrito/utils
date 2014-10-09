/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-06-05T15:22:47Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: Link.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Defines a basic HTML link </text> 
 */

package utils.www;

import java.util.ArrayList;


/**
 *
 * @author Nuno Brito, 5th of June 2014 in Darmstadt, Germany.
 *  nuno.brito@triplecheck.de | http://nunobrito.eu
 */
public class Link {

    private ArrayList<Link> links = new ArrayList();
    
    private String 
            title, 
            url,
            extra = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getLink(){
        if(links.isEmpty()){
            return html.link(title, url + extra);
        }else{
            String result = html.link(title, url + extra);
            
            for(Link link : links){
                result = result.concat(" | " + link.getLink());
            }
            
            return result;
        }
    }
    
    /**
     * Sometimes useful to have more than one link per object
     * @param link 
     */
    public void addLink(Link link){
        links.add(link);
    }

    /**
     * Add a parameter to a link
     * @param string 
     */
    public void addParameters(String text) {
        this.extra = text;
        if(links.isEmpty() == false){
            for(Link link : links){
                link.addParameters(text);
            }
        }
    }
    
}
