package romeogolf;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MainController implements Initializable{
	// знакоместа пользовательских цифр
	@FXML private Label charsell_1_1;
	@FXML private Label charsell_1_2;
	@FXML private Label charsell_1_3;
	@FXML private Label charsell_1_4;
	// знакоместа цифр 2 игрока
	@FXML private Label charsell_2_1;
	@FXML private Label charsell_2_2;
	@FXML private Label charsell_2_3;
	@FXML private Label charsell_2_4;
	// поле отображения загаданной последовательности
	@FXML private TextField indicator;
	// кнопки для изменения цифр
	@FXML private Button btUp_1;
	@FXML private Button btDown_1;
	@FXML private Button btUp_2;
	@FXML private Button btDown_2;
	@FXML private Button btUp_3;
	@FXML private Button btDown_3;
	@FXML private Button btUp_4;
	@FXML private Button btDown_4;
	// поля для отображения ходов игроков 1 и 2
	@FXML private VBox vbPlayer1;
	@FXML private ScrollPane spPlayer1;
	@FXML private VBox vbPlayer2;
	@FXML private ScrollPane spPlayer2;
	// переключение режима
	@FXML private ToggleGroup tgMode;
	// кнопки управления
	@FXML private Button btSetQuad;
	@FXML private Button btGenerateQuad;
	@FXML private Button btReset;

	private Integer mode = 0;
	void setMode(Integer m){
		this.mode = m;
		switch(mode){
		case 0 :	// режим "человек угадывает"
			this.btSetQuad.setDisable(true);
			this.btGenerateQuad.setDisable(false);
			break;
		case 1 :	// режим "человек и машина угадывают друг у друга"
			this.btSetQuad.setDisable(false);
			this.btGenerateQuad.setDisable(false);
			break;
		case 2 :	// режим "человек и машина угадвают одно наперегонки"
			this.btSetQuad.setDisable(true);
			this.btGenerateQuad.setDisable(false);
			break;
		case 3 :	// режим "машина угадывает (тестовый)"
			this.btSetQuad.setDisable(false);
			this.btGenerateQuad.setDisable(false);
			break;
		default :	// режим "ХЗ"
			indicator.setText("режим ХЗ");
		}
	}

	Integer getMode(){
		return this.mode;
	}

	// Цифры, вводимые пользователем
    Integer[] DigitsForShow = new Integer[4];
    // массив знакомест для пользовательских цифр
    ArrayList<Label> aQuad1 = new ArrayList<Label>();
    // массив знакомест для загаданных цифр
    ArrayList<Label> aQuad2 = new ArrayList<Label>();
    // карта соответствия кнопок цифрам	
    public Map<Button, Integer> df = new HashMap<Button, Integer>();
    // множество кнопок увеличения
    public Set<Button> sUp = new HashSet<Button>();;

    // тестовая кнопка - тестовый обработчик
    @FXML protected void onTest(ActionEvent event) {
    	//indicator.setText("qwerty");
    	//this.ShowStepInfo("zxcvbn", true);
    	//this.ShowStepInfo("asdfgh", false);
    	//String s = new String(Arrays.toString(DigitsForShow));
    	String s = new String(Arrays.toString(curator.getQuad(2)));
    	indicator.setText(s);
    };

    // общий обработчик для всех кнопок Up & Down
    @FXML protected void onUpDown(ActionEvent e) {
    		int Num = df.get(e.getSource());	// получение номера цифры, чью кнопку нажали
    		if (sUp.contains(e.getSource())){	// изменение цифры
    			DigitsForShow[Num]++;
    		} else {
    			DigitsForShow[Num]--;
    		}
    		// проверка выхода цифры за пределы
    		if (DigitsForShow[Num] < 0){DigitsForShow[Num] = 9;}
    			// и соответствующее изменение
    		if (DigitsForShow[Num] > 9){DigitsForShow[Num] = 0;}
    		aQuad1.get(Num).setText(Integer.toString(DigitsForShow[Num]));
    		IsDifferent();
    };

    @FXML protected void onShot(ActionEvent e) {
    	SelfAnswer();
    }

    @FXML protected void onSetQwad(ActionEvent e) {
    	if (!this.IsDifferent()){
    		curator.setQuad(this.DigitsForShow, 2);
    		for(int i = 0; i < 4; i++){
    			aQuad2.get(i).setText(curator.getQuad(2)[i].toString());
    		}
    	}
    }

    @FXML protected void onGenerateQwad(ActionEvent e) {
    	if((this.getMode() != 0) && (this.getMode() != 2)){
    		curator.generateQuad(2);
    		for(int i = 0; i < 4; i++){
    			aQuad2.get(i).setText(curator.getQuad(2)[i].toString());
    		}
    	}
    	curator.generateQuad(1);
    	//indicator.setText(Arrays.toString(curator.getQuad(1)));
    }

    @FXML protected void onReset(ActionEvent e) {
    	vbPlayer1.getChildren().removeAll(vbPlayer1.getChildren());

    }

    protected void onModeToggle(){
		if (tgMode.getSelectedToggle() != null) {
			setMode(Integer.decode(tgMode.getSelectedToggle().getUserData().toString()));
		}
    }


    // проверка цифр на совпадение
    boolean IsDifferent() {
    	//TODO: обрабатывать не только пары?
    	for(int i = 0; i < 4; i++) {
    		aQuad1.get(i).setStyle("-fx-text-fill: #000000;");
    	}
    	for(int i = 0; i < 3; i++) {
    		for(int j = i + 1; j < 4; j++) {
    			if(DigitsForShow[i] == DigitsForShow[j]) {
    				aQuad1.get(i).setStyle("-fx-text-fill: #FF0000;");
    				aQuad1.get(j).setStyle("-fx-text-fill: #FF0000;");
    				return true;
    			}
    		}
    	}
    	return false;
    };

    // инициализация интерфейса
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// добавление обработчика изменения размера панели на ScrollPane
		// для прокрутки до упора
		vbPlayer1.setSpacing(3);
		vbPlayer1.heightProperty().addListener(new ChangeListener<Object>() {
    		@Override
    		public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
    			if (vbPlayer1.getHeight() > spPlayer1.getHeight()) {
    				spPlayer1.setVvalue((Double)newValue );
    			}
            }
        });
		vbPlayer2.setSpacing(3);
		vbPlayer2.heightProperty().addListener(new ChangeListener<Object>() {
    		@Override
    		public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
    			if (vbPlayer2.getHeight() > spPlayer2.getHeight()) {
    				spPlayer2.setVvalue((Double)newValue );
    			}
            }
        });
		// заполнение карты соответствия кнопок их номерам
    	df.put(btUp_1, 0);
    	df.put(btDown_1, 0);
    	df.put(btUp_2, 1);
    	df.put(btDown_2, 1);
    	df.put(btUp_3, 2);
    	df.put(btDown_3, 2);
    	df.put(btUp_4, 3);
    	df.put(btDown_4, 3);
    	// множество кнопок "вверх"
    	sUp.add(btUp_1);
    	sUp.add(btUp_2);
    	sUp.add(btUp_3);
    	sUp.add(btUp_4);
    	// множество знакомест для цифр
    	aQuad1.add(charsell_1_1);
    	aQuad1.add(charsell_1_2);
    	aQuad1.add(charsell_1_3);
    	aQuad1.add(charsell_1_4);
    	// множество знакомест для загаданных цифр
    	aQuad2.add(charsell_2_1);
    	aQuad2.add(charsell_2_2);
    	aQuad2.add(charsell_2_3);
    	aQuad2.add(charsell_2_4);
    	// начальное заполнение массива цифр и его отображение
    	for(int i = 0; i < 4; i++){
    		this.DigitsForShow[i] = i;
    		aQuad1.get(i).setText(Integer.toString(i));
    	}

    	this.setMode(0);
        tgMode.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
        	public void changed(ObservableValue<? extends Toggle> ov,
        			Toggle old_toggle, Toggle new_toggle) {
        		onModeToggle();
        	}
        });
	}

	// вывод строки текста в ScrollPane
    void ShowStepInfo(String s, boolean Player1) {
    	HBox hb = new HBox();
    	hb.setAlignment(Pos.CENTER);
    	Text t = new Text(s);
    	t.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
    	hb.getChildren().add(t);
		hb.setStyle("-fx-background-color: #33FFFF;");
		if (Player1) {
			vbPlayer1.getChildren().add(hb);
		} else {
			vbPlayer2.getChildren().add(hb);
		}
    }

	DigitCurator curator = new DigitCurator();
	Solver solver = new Solver();
    Integer[] Digits = new Integer[4];			// Цифры, вводимые пользователем
    int bulls;
    int cows;
    int trying;

    // вывод результатов попытки
    void ShowNextShot(int trying) {
		String s = new String(Arrays.toString(Digits));
		s = Integer.toString(trying) + ": " + s;
		s = s + " -   " + Integer.toString(bulls) + " Б, " +
		    Integer.toString(cows) + " К";
		ShowStepInfo(s, true);
    }

    // ========= заготовка самостоятельной отгадки ======
    void SelfAnswer() {	    // отгадка
    	//curator.Init();
    	solver.Init(curator.RndAllDigits);
    	//curator.DigitMixer();			// перемешать цифры подготовить набор цифр

    	while(bulls + cows < 4) {		// цикл до отгадки всех цифр
    		Digits = solver.ToFindDigits(Digits);
    		Integer[] TmpBufI = new Integer[4];
    		for(int i = 0; i < 4; i++) {TmpBufI[i] = Digits[i];}
    		ShotData shot_data = curator.checkQuad(Digits, 2);
    		bulls = shot_data.getBulls();
    		cows = shot_data.getCows();
    		solver.shots_data.add(shot_data);
    		ShowNextShot(solver.shots_data.size());	// отображение
    		solver.ShotDigitIndex = 0;		// обнуление индексов
    		solver.DigitsForAnswerIndex = 0;
    	}
    	while(bulls < 4) {
    		solver.ToFindBulls(Digits);
    		ShotData shot_data = curator.checkQuad(Digits, 2);
    		bulls = shot_data.getBulls();
    		cows = shot_data.getCows();
    		solver.shots_data.add(shot_data);
    		ShowNextShot(solver.shots_data.size());	// отображение
    	}
    	solver.shots_data.clear();
    	bulls = 0;
    	cows = 0;
    }
}
