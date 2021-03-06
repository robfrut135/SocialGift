package org.mobilecloud.capstone.potlach.common;

import org.mobilecloud.capstone.potlach.common.repository.Gift;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class TestUtils {

    private static final String[] POSSIBLE_LOCALHOSTS = {
            "10.0.2.2", // Android Emulator
            "192.168.56.1" // Genymotion
    };

    public static String findTheRealLocalhostAddress() {
        String realLocalHost = null;

        for(String localhost : POSSIBLE_LOCALHOSTS) {
            try {
                URL url = new URL("http://"+localhost+":8080");
                URLConnection con = url.openConnection();
                con.setConnectTimeout(500);
                con.setReadTimeout(500);
                InputStream in = con.getInputStream();
                if (in != null){
                    realLocalHost = localhost;
                    in.close();
                    break;
                }
            } catch (Exception e) {}
        }

        return realLocalHost;
    }

    public static Gift randomGift(File f) {
    	try{    		
	    	return new Gift(
		                randomGiftName(),
		                randomGiftName() + " GIFT DESCRIPTION",	                
		                Gift.getContentTypeFromFile(f)
		                ); 
    	}catch(Exception e){
    		return null;
    	}
    }

    public static String randomGiftName() {
        return UUID.randomUUID().toString();
    }

}

