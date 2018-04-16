/*
 * Copyright (c) Max Brito
 * License: EUPL-1.2
 */

import junit.framework.Assert;
import org.junit.Test;
import utils.internet;

/**
 * Test if the internet class is working as expected
 * @author Max Brito, Germany
 */
public class TestInternet {
    
    public TestInternet() {
    }

    @Test
    public void testDownloadPage() {
        String URL = "http://www.fogcam.org/";
        String text = utils.internet.getWebPage(URL);
        Assert.assertTrue(text.contains("we are the oldest"));
    }
    
    @Test
    public void testGetRedirectedDownloadPage() {
        // test with an original URL 
        String urlOriginal = "http://docs.jquery.com/UI/Resizables";
        // this page will be redirected to http://api.jqueryui.com/resizable/
        String text = utils.internet.getWebPage(urlOriginal);
        Assert.assertTrue(text.contains("Resizable Widget"));
    }
    
    @Test
    public void testRedirectionURL() {
        // test with an original URL 
        String urlOriginal = "http://docs.jquery.com/UI/Resizables";
        // this page will be redirected to http://api.jqueryui.com/resizable/
        String urlNew = utils.internet.getRedirection(urlOriginal);
        Assert.assertEquals("http://api.jqueryui.com/resizable/", urlNew);
        
        
        // check if this is the final redirect
        // http://learn.jquery.com/UI/Widget
        // http://learn.jquery.com/jquery-ui/widget-factory/
    }
    
    
    @Test
    public void testNetworkConnected(){
        // this test only works when a network cable is connected
        boolean isConnected = internet.checkThatNetworkIsAvailable();
        Assert.assertTrue(isConnected);
    }
    
    
    
}
