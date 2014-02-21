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
	private Integer firstStep;
	private Boolean digitsReset;

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

	public void setFirstStep(Integer value){
		firstStep = value;
		this.saveInteger("firstStep", value);
	}

	public void setDigitsReset(Boolean value){
		digitsReset = value;
		this.saveBool("digitsReset", value);
	}
	//---------------------------------
	public Double getTop(){
		top = this.getDouble("top", 0.0);
		return top;
	}

	public Double getLeft(){
		left = this.getDouble("left", 0.0);
		return left;
	}

	public Double getHeight(){
		height = this.getDouble("height", 0.0);
		return height;
	}

	public Integer getMode(){
		mode = this.getInteger("mode", 0);
		return mode;
	}

	public Integer getFirstStep(){
		firstStep = this.getInteger("firstStep", 0);
		return firstStep;
	}

	public Boolean getDigitsReset(){
		digitsReset = this.getBool("digitsReset", false);
		return digitsReset;
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

    private void saveBool(String key, Boolean value){
    	props.setProperty(key, Boolean.toString(value));
    }

    private Integer getInteger(String key, Integer defValue){
    	String s = props.getProperty(key);
    	Integer value = defValue;
    	if(s != null){
    		try{
    			value = Integer.decode(s);
    		} catch(NumberFormatException e){
    			e.printStackTrace();
    		}
    	}
    	return value;
    }

    private Double getDouble(String key, Double defValue){
    	String s = props.getProperty(key);
    	Double value = defValue;
    	if(s != null){
    		try{
    			value = Double.valueOf(s);
    		} catch(NumberFormatException e){
    			e.printStackTrace();
    		}
    	}
    	return value;
    }

    private Boolean getBool(String key, Boolean defValue){
    	String s = props.getProperty(key);
    	Boolean value = defValue;
    	if(s != null){
    		try{
    			value = Boolean.valueOf(s);
    		} catch(NumberFormatException e){
    			e.printStackTrace();
    		}
    	}
    	return value;
    }

    public Boolean isPosition(){
    	Boolean result = false;
    	String s = props.getProperty("top");
    	Double value = null;
    	if(s != null){
    		try{
    			value = Double.valueOf(s);
    		} catch(NumberFormatException e){
    			e.printStackTrace();
    		}
    	}
    	if(value != null){
    		result = true;
    	}

    	s = props.getProperty("left");
    	value = null;
    	if(s != null){
    		try{
    			value = Double.valueOf(s);
    		} catch(NumberFormatException e){
    			e.printStackTrace();
    		}
    	}
    	if(value != null){
    		result = true;
    	}

    	s = props.getProperty("height");
    	value = null;
    	if(s != null){
    		try{
    			value = Double.valueOf(s);
    		} catch(NumberFormatException e){
    			e.printStackTrace();
    		}
    	}
    	if(value != null){
    		result = true;
    	}
    	return result;
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
