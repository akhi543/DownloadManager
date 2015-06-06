/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vishal
 */

import java.io.*;
import java.net.*;

public class Crawler {
    
    URL url;
    String searchurl;
    String strippedurl;
    
    Crawler(URL ur,String surl)
    {
        url=ur;
        searchurl=surl;
        strippedurl=searchurl.substring(searchurl.lastIndexOf(".com")+4);
    }
    
    int search() throws Exception
    {
        URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                                    yc.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) 
        {
        	//System.out.println(inputLine);
        	if (inputLine.contains(searchurl)||inputLine.contains(strippedurl))
        	{
                    return 1;
        	}
        }
        return 0;
    }
}
