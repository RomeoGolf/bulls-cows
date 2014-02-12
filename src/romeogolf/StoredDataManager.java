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
    // имя файла настроек
    private final String PropFileName = "bc.properties";
    // файл для чтения/записи настроек
    private File propFile = null;
    // индикатор загруженности настроек
    private Boolean Loaded = false;
    public Boolean isLoaded(){
    	return this.Loaded;
    }

    // тестовая строка
    public String test_str = "qwerty";

    // запись данных
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
    	// для получения пути к исполняемому файлу
    	src = this.getClass().getProtectionDomain().getCodeSource();
    	if (src != null) {
			try {
				url = new URL(src.getLocation(), PropFileName);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			// URL файла
    		String path = url.getPath();
    		try {
    			// декодирование URL для устранения проблемы с пробелами
				decodedPath = URLDecoder.decode(path, "UTF-8");
				// окончательное получение файла настроек
				propFile = new File(decodedPath);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
    	}

    	// загрузка сохраненных настроек (если возможно)
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
