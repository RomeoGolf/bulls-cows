package romeogolf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.Properties;

class StoredDataManager {
	Properties props = new Properties();

    CodeSource src;
    public URL url = null;
    public String test_str;
    String decodedPath = null;

    public void writeData(){
    	File pf = new File(decodedPath);
    	if (pf.canWrite()) {
    		try {
    			props.setProperty("test", test_str);
				props.store(new FileOutputStream(pf), "comment");
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public StoredDataManager(){
    	src = Main.class.getProtectionDomain().getCodeSource();
    	if (src != null) {
			try {
				url = new URL(src.getLocation(), "bc.properties");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
    		String path = url.getPath();
    		try {
				decodedPath = URLDecoder.decode(path, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
    	}

    	File pf = new File(decodedPath);
    	if (pf.canRead()) {
    		try {
				props.load(new FileInputStream(pf));
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}

    	//test_str = decodedPath;
    	test_str = props.getProperty("test");

    }
}
