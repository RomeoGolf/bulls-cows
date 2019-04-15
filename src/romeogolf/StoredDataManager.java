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
	private Properties props = new Properties();
	private CodeSource src;
    private URL url = null;
    private String decodedPath = null;
    // ��� ����� ��������
    private final String PropFileName = "bc.properties";
    // ���� ��� ������/������ ��������
    private File propFile = null;
    // ��������� ������������� ��������
    private Boolean Loaded = false;
    public Boolean isLoaded(){
    	return this.Loaded;
    }

    // �������� ������
    public String test_str = "qwerty";

    // ������ ������
    public void writeData(){
    	if(!propFile.exists()){
    		try {
				propFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	if (propFile.canWrite()) {
    		try {
    			props.setProperty("test", test_str);
				props.store(new FileOutputStream(propFile), "");
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public StoredDataManager(){
    	// ��� ��������� ���� � ������������ �����
    	src = this.getClass().getProtectionDomain().getCodeSource();
    	if (src != null) {
			try {
				url = new URL(src.getLocation(), PropFileName);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			// URL �����
    		String path = url.getPath();
    		try {
    			// ������������� URL ��� ���������� �������� � ���������
				decodedPath = URLDecoder.decode(path, "UTF-8");
				// ������������� ��������� ����� ��������
				propFile = new File(decodedPath);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
    	}

    	// �������� ����������� �������� (���� ��������)
    	if (propFile.canRead()) {
    		try {
				props.load(new FileInputStream(propFile));
				Loaded = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	test_str = props.getProperty("test");
    }
}
