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
	// =========================================================================
	// data:
	private Double top;
	private Double left;
	private Double height;
	private Integer mode;

	//--------------------------------
	public void setTop(Double value){
		top = value;
		this.saveDouble("top", value);
	}

	public void setLeft(Double value){
		left = value;
		this.saveDouble("left", value);
	}

	public void setHeight(Double value){
		height = value;
		this.saveDouble("height", value);
	}

	public void setMode(Integer value){
		mode = value;
		this.saveInteger("mode", value);
	}

	//---------------------------------
	public Double getTop(){
		top = this.getDouble("top");
		return top;
	}

	public Double getLeft(){
		left = this.getDouble("left");
		return left;
	}

	public Double getHeight(){
		height = this.getDouble("height");
		return height;
	}

	public Integer getMode(){
		mode = this.getInteger("mode");
		return mode;
	}
	// =========================================================================
	
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

    private void saveInteger(String key, Integer value){
    	props.setProperty(key, Integer.toString(value));
    }

    private void saveDouble(String key, Double value){
    	props.setProperty(key, Double.toString(value));
    }

    /*private Integer getInteger(String key, Integer defValue){
    	String s = props.getProperty(key, Integer.toString(defValue));
    	Integer value = 0; //NumberFormatException
    	try{
    		value = Integer.decode(s);
    	} catch(NumberFormatException e){
    		e.printStackTrace();
    	}
    	return value;
    }*/

    private Integer getInteger(String key){
    	String s = props.getProperty(key);
    	Integer value = null; //NumberFormatException
    	if(s != null){
    		try{
    			value = Integer.decode(s);
    		} catch(NumberFormatException e){
    			e.printStackTrace();
    		}
    	}
    	return value;
    }
    
    private Double getDouble(String key){
    	String s = props.getProperty(key);
    	Double value = null; //NumberFormatException
    	if(s != null){
    		try{
    			value = Double.valueOf(s);
    		} catch(NumberFormatException e){
    			e.printStackTrace();
    		}
    	}
    	return value;
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
    			//props.setProperty("test", test_str);
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
