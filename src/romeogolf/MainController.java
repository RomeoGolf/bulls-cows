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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MainController implements Initializable{
	// знакоместа пользовательских цифр
	@FXML private TextField charsell_1;
	@FXML private TextField charsell_2;
	@FXML private TextField charsell_3;
	@FXML private TextField charsell_4;
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
	// Цифры, вводимые пользователем
    Integer[] DigitsForShow = new Integer[4];
    // массив знакомест для пользовательских цифр
    ArrayList<TextField> atfDigits = new ArrayList<TextField>();
    // карта соответствия кнопок цифрам	
    public Map<Button, Integer> df = new HashMap<Button, Integer>();
    // множество кнопок увеличения
    public Set<Button> sUp = new HashSet<Button>();;

    // тестовая кнопка - тестовый обработчик
    @FXML protected void onTest(ActionEvent event) {
    	indicator.setText("qwerty");
    	this.ShowStepInfo("zxcvbn", true);
    	this.ShowStepInfo("asdfgh", false);
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
    		atfDigits.get(Num).setText(Integer.toString(DigitsForShow[Num]));
    		IsDifferent();
    };

    @FXML protected void onShot(ActionEvent e) {
    	SelfAnswer();
    }

    // проверка цифр на совпадение
    boolean IsDifferent() {
    	//TODO: обрабатывать не только пары?
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
    	atfDigits.add(charsell_1);
    	atfDigits.add(charsell_2);
    	atfDigits.add(charsell_3);
    	atfDigits.add(charsell_4);
    	// начальное заполнение массива цифр и его отображение
    	for(int i = 0; i < 4; i++){
    		this.DigitsForShow[i] = i;
    		atfDigits.get(i).setText(Integer.toString(i));
    		
        curator.Init();
    	}
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
    	curator.Init();
    	solver.Init(curator.RndAllDigits);
    	//curator.DigitMixer();			// перемешать цифры подготовить набор цифр

    	while(bulls + cows < 4) {		// цикл до отгадки всех цифр

    		Digits = solver.ToFindDigits(Digits);

    		CalcBullCow();		    // вычисление
    		Integer[] TmpBufI = new Integer[4];
    		for(int i = 0; i < 4; i++) {TmpBufI[i] = Digits[i];}
    		solver.Shots_digits.add(TmpBufI);	    // заполнение списков попыток очередной попыткой
    		solver.Shots_bulls.add(bulls);
    		solver.Shots_cows.add(cows);

    		ShowNextShot(solver.Shots_digits.size());	// отображение

    		solver.ShotDigitIndex = 0;		// обнуление индексов
    		solver.DigitsForAnswerIndex = 0;
    	}
    	while(bulls < 4) {
    		solver.ToFindBulls(Digits);

    		CalcBullCow();
    		Integer[] NextShot2 = new Integer[4];
    		NextShot2 = Digits.clone();
    		solver.Shots_digits.add(NextShot2);
    		solver.Shots_bulls.add(bulls);
    		solver.Shots_cows.add(cows);
    		ShowNextShot(solver.Shots_digits.size());	// отображение
    	}
    }

    void CalcBullCow() {
    	bulls = 0;
    	cows = 0;
    	for(int i = 0; i < 4; i++) {
    		if (Digits[i] == curator.RndDigits[i]) {
    			bulls++;
    		}
    	}
    	Set<Integer> s = new HashSet<Integer>();
    	for(int i = 0; i < 4; i++) {
    		s.add(curator.RndDigits[i]);
    	}
    	for(int i = 0; i < 4; i++) {
    		if(s.contains(Digits[i])){
    			cows++;
    		}
    	}
    	cows = cows - bulls;
    };
}
