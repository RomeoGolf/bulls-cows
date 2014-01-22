package romeogolf;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MainController implements Initializable{
	@FXML private TextField charsell_1;
	@FXML private TextField charsell_2;
	@FXML private TextField charsell_3;
	@FXML private TextField charsell_4;

	@FXML private TextField indicator;

	@FXML private Button btUp_1;
	@FXML private Button btDown_1;
	@FXML private Button btUp_2;
	@FXML private Button btDown_2;
	@FXML private Button btUp_3;
	@FXML private Button btDown_3;
	@FXML private Button btUp_4;
	@FXML private Button btDown_4;

    Integer[] DigitsForShow = new Integer[4];			// Цифры, вводимые пользователем
    ArrayList<TextField> atfDigits = new ArrayList<TextField>();

    // карта соответствия кнопок цифрам	
    public Map<Button, Integer> df = new HashMap<Button, Integer>();
    // множество кнопок увеличения
    public Set<Button> sUp = new HashSet<Button>();;

    @FXML protected void onTest(ActionEvent event) {
    	indicator.setText("qwerty");
    };

    // общий обработчик для всех кнопок Up & Down
    @FXML protected void onUpDown(ActionEvent e) {
    		int Num = df.get(e.getSource());	// получение номера цифры, чью кнопку нажали
    		if (sUp.contains(e.getSource())){	// изменение цифры
    			DigitsForShow[Num]++;
    		} else {
    			DigitsForShow[Num]--;
    		}
    		if (DigitsForShow[Num] < 0){DigitsForShow[Num] = 9;}	// проверка выхода цифры за пределы
    		if (DigitsForShow[Num] > 9){DigitsForShow[Num] = 0;}	// и соответствующее изменение
    		atfDigits.get(Num).setText(Integer.toString(DigitsForShow[Num]));  // отображение цифры

    		IsDifferent();
    };

    boolean IsDifferent() {
    	for(int i = 0; i < 4; i++) {
    		atfDigits.get(i).setStyle("-fx-text-fill: #000000;");
    	}
    	for(int i = 0; i < 3; i++) {
    		for(int j = i + 1; j < 4; j++) {
    			if(DigitsForShow[i] == DigitsForShow[j]) {
    				atfDigits.get(i).setStyle("-fx-text-fill: #FF0000;");
    				atfDigits.get(j).setStyle("-fx-text-fill: #FF0000;");
    				return true;
    			}
    		}
    	}
    	return false;
    };

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	df.put(btUp_1, 0);
    	df.put(btDown_1, 0);
    	df.put(btUp_2, 1);
    	df.put(btDown_2, 1);
    	df.put(btUp_3, 2);
    	df.put(btDown_3, 2);
    	df.put(btUp_4, 3);
    	df.put(btDown_4, 3);

    	sUp.add(btUp_1);
    	sUp.add(btUp_2);
    	sUp.add(btUp_3);
    	sUp.add(btUp_4);

    	atfDigits.add(charsell_1);
    	atfDigits.add(charsell_2);
    	atfDigits.add(charsell_3);
    	atfDigits.add(charsell_4);

    	for(int i = 0; i < 4; i++){
    		this.DigitsForShow[i] = i;
    		atfDigits.get(i).setText(Integer.toString(i));
    	}
	}

}
