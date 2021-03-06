package romeogolf.bc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

    // результаты игр
    private Integer mode0Max;
    private Integer mode0Min;
    private Integer mode0Total;
    private Integer mode1Player1Win;
    private Integer mode1Player2Win;
    private Integer mode1Tie;
    private Integer mode2Player1Win;
    private Integer mode2Player2Win;
    private Integer mode2Tie;

    //--------------------------------
    void setTop(Double value){
        top = value;
        this.saveDouble("top", value);
    }

    void setLeft(Double value){
        left = value;
        this.saveDouble("left", value);
    }

    void setHeight(Double value){
        height = value;
        this.saveDouble("height", value);
    }

    void setMode(Integer value){
        mode = value;
        this.saveInteger("mode", value);
    }

    void setFirstStep(Integer value){
        firstStep = value;
        this.saveInteger("firstStep", value);
    }

    void setDigitsReset(Boolean value){
        digitsReset = value;
        this.saveBool("digitsReset", value);
    }

    void setMode0Max(Integer value){
        mode0Max = value;
        this.saveInteger("mode0Max", value);
    }

    void setMode0Min(Integer value){
        mode0Min = value;
        this.saveInteger("mode0Min", value);
    }

    void setMode0Total(Integer value){
        mode0Total = value;
        this.saveInteger("mode0Total", value);
    }

    void setMode1Player1Win(Integer value){
        mode1Player1Win = value;
        this.saveInteger("mode1Player1Win", value);
    }

    void setMode1Player2Win(Integer value){
        mode1Player2Win = value;
        this.saveInteger("mode1Player2Win", value);
    }

    void setMode1Tie(Integer value){
        mode1Tie = value;
        this.saveInteger("mode1Tie", value);
    }

    void setMode2Player1Win(Integer value){
        mode2Player1Win = value;
        this.saveInteger("mode2Player1Win", value);
    }

    void setMode2Player2Win(Integer value){
        mode2Player2Win = value;
        this.saveInteger("mode2Player2Win", value);
    }

    void setMode2Tie(Integer value){
        mode2Tie = value;
        this.saveInteger("mode2Tie", value);
    }

    //---------------------------------
    Double getTop(){
        top = this.getDouble("top", 0.0);
        return top;
    }

    Double getLeft(){
        left = this.getDouble("left", 0.0);
        return left;
    }

    Double getHeight(){
        height = this.getDouble("height", 0.0);
        return height;
    }

    Integer getMode(){
        mode = this.getInteger("mode", 0);
        return mode;
    }

    Integer getFirstStep(){
        firstStep = this.getInteger("firstStep", 0);
        return firstStep;
    }

    Boolean getDigitsReset(){
        digitsReset = this.getBool("digitsReset", false);
        return digitsReset;
    }

    Integer getMode0Max(){
        mode0Max = this.getInteger("mode0Max", 0);
        return mode0Max;
    }

    Integer getMode0Min(){
        mode0Min = this.getInteger("mode0Min", 0);
        return mode0Min;
    }

    Integer getMode0Total(){
        mode0Total = this.getInteger("mode0Total", 0);
        return mode0Total;
    }

    Integer getMode1Player1Win(){
        mode1Player1Win = this.getInteger("mode1Player1Win", 0);
        return mode1Player1Win;
    }

    Integer getMode1Player2Win(){
        mode1Player2Win = this.getInteger("mode1Player2Win", 0);
        return mode1Player2Win;
    }

    Integer getMode1Tie(){
        mode1Tie = this.getInteger("mode1Tie", 0);
        return mode1Tie;
    }

    Integer getMode2Player1Win(){
        mode2Player1Win = this.getInteger("mode2Player1Win", 0);
        return mode2Player1Win;
    }

    Integer getMode2Player2Win(){
        mode2Player2Win = this.getInteger("mode2Player2Win", 0);
        return mode2Player2Win;
    }

    Integer getMode2Tie(){
        mode2Tie = this.getInteger("mode2Tie", 0);
        return mode2Tie;
    }

    // =========================================================================

    private final Properties props = new Properties();
    // файл для чтения/записи настроек
    private File propFile = null;

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
                // если никто руками не лез в файл настроек -
                //   исключения здесь не будет. Но...
                value = 0;
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
                value = 0.0;
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
                value = false;
                e.printStackTrace();
            }
        }
        return value;
    }

    Boolean isPosition(){
        boolean result = false;
        String s = props.getProperty("top");
        Double value = null;
        if(s != null){
            try{
                value = Double.valueOf(s);
            } catch(NumberFormatException e){
                value = 0.0;
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
                value = 0.0;
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
                value = 0.0;
                e.printStackTrace();
            }
        }
        if(value != null){
            result = true;
        }
        return result;
    }

    // запись данных
    void writeData(){
        if(!propFile.exists()){
            try {
                propFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (propFile.canWrite()) {
            try {
                props.store(new FileOutputStream(propFile), "");
            } catch (IOException e) {
                // Нечего тут делать. Нет возможности сохранить - 
                //   не будем сохранять.
                e.printStackTrace();
            }
        }
    }

    StoredDataManager(){
        // для получения пути к исполняемому файлу
        CodeSource src = this.getClass().getProtectionDomain().getCodeSource();
        if (src != null) {
            URL url = null;
            try {
                // имя файла настроек
                String propFileName = "bc.properties";
                url = new URL(src.getLocation(), propFileName);
            } catch (MalformedURLException e) {
                // С таким способом получения URL ошибка не должна возникать.
                e.printStackTrace();
            }
            // URL файла
            String path = url.getPath();
            // декодирование URL для устранения проблемы с пробелами
            String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
            // окончательное получение файла настроек
            propFile = new File(decodedPath);
        }

        // загрузка сохраненных настроек (если возможно)
        if (propFile.canRead()) {
            try {
                props.load(new FileInputStream(propFile));
                // индикатор загруженности настроек
            } catch (IOException e) {
                // Тут ничего не надо делать. Нет файла настроек - и ладно, 
                //   будут значения по умолчанию.
                e.printStackTrace();
            }
        }
    }
}
