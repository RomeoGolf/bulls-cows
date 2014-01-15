package romeogolf;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import javafx.beans.value.*;
import javax.swing.JOptionPane;
import romeogolf.DigitCurator;


public class Main extends Application {
	DigitCurator curator = new DigitCurator();
    Integer[] Digits = new Integer[4];			// Цифры, вводимые пользователем
    TextField[] atfDigits = new TextField[4];		// Поля ввода цифр
    int bulls;
    int cows;
    int trying;
    // кнопки увеличения цифры
    public Button btUp1;
    public Button btUp2;
    public Button btUp3;
    public Button btUp4;
    // кнопки уменьшения цифры
    public Button btDown1;
    public Button btDown2;
    public Button btDown3;
    public Button btDown4;

    VBox vbCenter = new VBox();
    ScrollPane spCenter = new ScrollPane();

    public Map<Button, Integer> df;	// карта соответствия кнопок цифрам
    public Set<Button> sUp;		// множество кнопок увеличения
    public void Info(String s) {
    	JOptionPane.showMessageDialog(null, s, "Info", JOptionPane.INFORMATION_MESSAGE);
    };

    // общий обработчик для всех кнопок
    EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
    	@Override
    	public void handle(ActionEvent e) {
    		int Num = df.get(e.getSource());	// получение номера цифры, чью кнопку нажали
    		if (sUp.contains(e.getSource())){	// изменение цифры
    				Digits[Num]++;
    		} else {
    			Digits[Num]--;
    		}
    		if (Digits[Num] < 0){Digits[Num] = 9;}	// проверка выхода цифры за пределы
    		if (Digits[Num] > 9){Digits[Num] = 0;}	// и соответствующее изменение
    		atfDigits[Num].setText(Integer.toString(Digits[Num]));  // отображение цифры

    		IsDifferent();
    	};
    };


    boolean IsDifferent() {
    	for(int i = 0; i < 4; i++) {
    		atfDigits[i].setStyle("-fx-text-fill: #000000;");
    	}
    	for(int i = 0; i < 3; i++) {
    		for(int j = i + 1; j < 4; j++) {
    			if(Digits[i] == Digits[j]) {
    				atfDigits[i].setStyle("-fx-text-fill: #FF0000;");
    				atfDigits[j].setStyle("-fx-text-fill: #FF0000;");
    				return true;
    			}
    		}
    	}
    	return false;
    };



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

    public static void main(String[] args) {
    	launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
    	trying = 0;
    	// инициализация цифр, создание полей ввода
    	for(int i = 0; i <= 3; i++){
    		Digits[i] = i; //rg.nextInt(9);
    		atfDigits[i] = new TextField(Integer.toString(Digits[i]));
    		atfDigits[i].setPrefColumnCount(1);
    		atfDigits[i].setEditable(false);
    	}

    	curator.Init();

    	primaryStage.setTitle("CheckOut");
    	BorderPane bp = new BorderPane();
    	Scene scene = new Scene(bp, 300, 375);

    	HBox hbTop = new HBox();
    	hbTop.setAlignment(Pos.CENTER);
    	hbTop.setPadding(new Insets(15, 12, 15, 12));
    	VBox vbForTop = new VBox();
    	vbForTop.setAlignment(Pos.CENTER);
    	vbForTop.setSpacing(10);
    	hbTop.getChildren().add(vbForTop);

    	HBox hbBottom = new HBox();
    	hbTop.setStyle("-fx-background-color: #336699;");
    	bp.setTop(hbTop);
    	hbBottom.setAlignment(Pos.CENTER);
    	bp.setBottom(hbBottom);

    	bp.setCenter(spCenter);
    	vbCenter.setAlignment(Pos.TOP_CENTER);
    	vbCenter.setSpacing(3);
    	spCenter.setContent(vbCenter);
    	spCenter.setFitToWidth(true);

    	vbCenter.heightProperty().addListener(new ChangeListener<Object>() {
    		@Override
    		public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
    			spCenter.setVvalue((Double)newValue );
            }
        });

    	// ------ прорисовка кнопок и полей ввода цифр --------
    	btUp1 = new Button();
    	btDown1 = new Button();
    	btUp1.setOnAction(eh);
    	btDown1.setOnAction(eh);

    	VBox vb1 = new VBox();
    	vb1.setAlignment(Pos.CENTER);
    	vb1.getChildren().add(btUp1);
    	vb1.getChildren().add(atfDigits[0]);
    	vb1.getChildren().add(btDown1);

    	btUp2 = new Button();
    	btDown2 = new Button();
    	btUp2.setAlignment(Pos.CENTER);
    	btUp2.setOnAction(eh);
    	btDown2.setOnAction(eh);

    	VBox vb2 = new VBox();
    	vb2.setAlignment(Pos.CENTER);
    	vb2.getChildren().add(btUp2);
    	vb2.getChildren().add(atfDigits[1]);
    	vb2.getChildren().add(btDown2);

    	btUp3 = new Button();
    	btDown3 = new Button();
    	btUp3.setOnAction(eh);
    	btDown3.setOnAction(eh);

    	VBox vb3 = new VBox();
    	vb3.setAlignment(Pos.CENTER);
    	vb3.getChildren().add(btUp3);
    	vb3.getChildren().add(atfDigits[2]);
    	vb3.getChildren().add(btDown3);

    	btUp4 = new Button();
    	btDown4 = new Button();
    	btUp4.setOnAction(eh);
    	btDown4.setOnAction(eh);

    	VBox vb4 = new VBox();
    	vb4.setAlignment(Pos.CENTER);
    	vb4.getChildren().add(btUp4);
    	vb4.getChildren().add(atfDigits[3]);
    	vb4.getChildren().add(btDown4);

    	HBox hbDigits = new HBox();
    	hbDigits.setSpacing(5);
    	hbDigits.getChildren().addAll(vb1, vb2, vb3, vb4);
    	vbForTop.getChildren().add(hbDigits);

    	Button btEnter = new Button("Enter");
    	vbForTop.getChildren().add(btEnter);
    	btEnter.setOnAction(new EventHandler<ActionEvent>() {
    		@Override 
    		public void handle(ActionEvent e) {
    			if(IsDifferent()){return;}
    			trying++;
    			CalcBullCow();
    			ShowNextShot(trying);
    		}
    	});
    	Button btSelf = new Button("Self");
    	vbForTop.getChildren().add(btSelf);
    	btSelf.setOnAction(new EventHandler<ActionEvent>() {
    		@Override 
    		public void handle(ActionEvent e) {
    			SelfAnswer();
    		}
    	});
    	// -------- конец прорисовки --------------

    	// ----- нижняя панель ----------
    	final TextField tfBottom = new TextField();
    	Button btBottom = new Button("Test");
    	hbBottom.getChildren().addAll(tfBottom, btBottom);
    	btBottom.setOnAction(new EventHandler<ActionEvent>() {
    		@Override 
    		public void handle(ActionEvent e) {
    			curator.DigitMixer();
    			String s = "";
//		for(int i = 0; i <= 9; i++){
//		    s = s + Integer.toString(RndAllDigits[i]) + " ";
//		}
    			s = Arrays.toString(curator.RndDigits);
    			tfBottom.setText(s);
    		}
    	});

    	// ------------------------------

    	// заполнение карты кнопка-номер и множества кнопок увеличения
    	df = new HashMap<Button, Integer>();
    	df.put(btUp1, 0);
    	df.put(btDown1, 0);
    	df.put(btUp2, 1);
    	df.put(btDown2, 1);
    	df.put(btUp3, 2);
    	df.put(btDown3, 2);
    	df.put(btUp4, 3);
    	df.put(btDown4, 3);
    	sUp = new HashSet<Button>();
    	sUp.add(btUp1);
    	sUp.add(btUp2);
    	sUp.add(btUp3);
    	sUp.add(btUp4);

    	primaryStage.setScene(scene); 
    	primaryStage.show();
    }

    void ShowNextShot(int trying) {
		String s = new String(Arrays.toString(Digits));
		s = Integer.toString(trying) + ": " + s;
		s = s + " -   " + Integer.toString(bulls) + " Б, " +
		    Integer.toString(cows) + " К";
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		Text t = new Text(s);
		t.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		hb.getChildren().add(t);
		hb.setStyle("-fx-background-color: #33FFFF;");
		vbCenter.getChildren().add(hb);
		spCenter.setVvalue(spCenter.getVmax());
    }

    void ShowStepInfo(String s) {
    	HBox hb = new HBox();
    	hb.setAlignment(Pos.CENTER);
    	Text t = new Text(s);
    	t.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
    	hb.getChildren().add(t);
//	hb.setStyle("-fx-background-color: #336699;");
    	vbCenter.getChildren().add(hb);
    	spCenter.setVvalue(spCenter.getVmax());
    }


    // ========= заготовка самостоятельной отгадки ======
    ArrayList<Integer[]> Shots_digits = new ArrayList<Integer[]>();	// массивы цифр попыток
    ArrayList<Integer> Shots_bulls = new ArrayList<Integer>();		// быки попыток
    ArrayList<Integer> Shots_cows = new ArrayList<Integer>();		// коровы попыток
    ArrayList<Integer> DigitsForAnswer = new ArrayList<Integer>();	// набор цифр для отгадки
    Integer[] NextShot;					// массив очередной попытки
    Integer[] ShotDigitInDigits_index = new Integer[4];	// индекс цифры попытки в наборе цифр

    // проверка допустимости подмассива по результатам предыдущих попыток
    boolean IsSuitable(Integer[] a, int length) {
    	int BullCow = 0;	// сумма быков и коров попытки
    	int Intersection = 0;	// мощность пересечения цифр старой попытки
				//      и цифр подмассива очередной попытки
    	for (int i = 0; i <= (Shots_digits.size() - 1); i++) {
    		BullCow = Shots_bulls.get(i) + Shots_cows.get(i);
    		for (int j = 0; j <= 3; j++) {	// подсчет мощности пересечения
    			for (int k = 0; k <= length; k++) {
    				if (a[k] == Shots_digits.get(i)[j]){Intersection++;}
    			}
    		}
    		// если пересечение больше числа угаданных - есть лишнее в отгадке
    		if (Intersection > BullCow) {return false;}
    		Intersection = 0;
    	}
    	return true;
    }

    // Цифры и индексы текущей попытки для проверки на соответствие быкам
    ArrayList<Integer> ShotDigits = new ArrayList<Integer>();
    ArrayList<Integer> Indexes = new ArrayList<Integer>();

    // проверка на допустимость подмассива быков
    boolean IsSuitableBulls(int Max) {
    	for (int i = 0; i <= (Shots_digits.size() - 1); i++) {
    		int coincidence = 0;
    		for(int j = 0; j <= Max; j++) {
    			if (ShotDigits.get(j) == Shots_digits.get(i)[Indexes.get(j)]) {coincidence++;}
    		}
    		if (coincidence > Shots_bulls.get(i)) {return false;}
    	}
    	return true;
    }

    // Проверка пересечения индексов с коррекцией
    boolean IsIndexValid(int max) {
    	Set<Integer> Ind = new HashSet<Integer>();
    	for(int i = 0; i < max; i++) {Ind.add(Indexes.get(i));}
    	while(Indexes.get(max) < 4) {
    		if(Ind.contains(Indexes.get(max))) {
    			Indexes.set(max, Indexes.get(max) + 1);
    			continue;
    		} else {
    			return true;
    		}
    	}
    	return false;
    }

    // попытка расстановки быков
    boolean TrySetBulls() {
    	int i = 0;
    	Indexes.set(i, -1);
    	while (i < 4) {
    		Indexes.set(i, Indexes.get(i) + 1);
    		if(IsIndexValid(i)) {
    			if(IsSuitableBulls(i)) {
    				i++;
    				if(i > 3) {break;}
    				Indexes.set(i, -1);
    				continue;
    			} else {
    				continue;
    			}
    		} else {
    			i--;
    			if(i < 0) {
    				// error
    				return false;
    			} else {
    				continue;
    			}
    		}
    	}
    	return true;
    }

    void SelfAnswer() {	    // отгадка
    	Indexes.clear();
    	for(int i = 0; i < 4; i++) {Indexes.add(i);}
    	ShotDigits.clear();
    	ShotDigits.addAll(Indexes);

    	int ShotDigitIndex = 0;		// индекс массива цифр очередной попытки
    	int DigitsForAnswerIndex = 0;	// индекс в наборе цифр

    	curator.DigitMixer();			// перемешать цифры подготовить набор цифр
    	for(int i = 0; i < 10; i++) {
    		DigitsForAnswer.add(curator.RndAllDigits[i]);
//	    DigitsForAnswer.add(i);
    	}

    	while(bulls + cows < 4) {		// цикл до отгадки всех цифр
    		NextShot = new Integer[4];		// формирование очередной попытки
    		while (ShotDigitIndex < 4) {
    			// подстановка очередной цмфры
    			NextShot[ShotDigitIndex] = DigitsForAnswer.get(DigitsForAnswerIndex);
    			// запоминание индекса цифры в наборе
    			ShotDigitInDigits_index[ShotDigitIndex] = DigitsForAnswerIndex;
    			// проверка набранного подмассива попытки на допустимость
    			if (!IsSuitable(NextShot, ShotDigitIndex)) {
    				// если очередная цифра не подошла - берем следующую
    				DigitsForAnswerIndex++;
    				// проверка выхода за пределы набора
    				if (DigitsForAnswerIndex > (DigitsForAnswer.size() - 1)) {
    					// если вышли - возврат в попытке на одну цифру назад
    					ShotDigitIndex--;
    					// если слишком назад - ошибка в быках и коровах
    					if (ShotDigitIndex < 0) {
    						// есть ошибка в переданных ранее быках и коровах // Info("Error");
    						return;
    					}
    					// для первого элемента отгадки берем слеюующую цифру из набора
    					DigitsForAnswerIndex = ShotDigitInDigits_index[ShotDigitIndex] + 1;
    				}
    				// если вернулись к младшему элементу - младшая цифра точно не верна,
    				if (ShotDigitIndex == 0) {
    					DigitsForAnswer.remove(0);	// ее нужно выкинуть из набора
    					ShotDigitIndex = 0;		// и обнулить индексы
    					DigitsForAnswerIndex = 0;
    				}
    				continue;
    			}
    			ShotDigitIndex++;	    // переход к следующему элементу отгадки
    			DigitsForAnswerIndex++;	    // и следующей цифре набора
    			// если вышли за пределы набора, когда массив еще не кончился
    			if ((DigitsForAnswerIndex > DigitsForAnswer.size() - 1) &&
    					(ShotDigitIndex <= 3)) {
    				ShotDigitIndex--;	    // надо опять вернуться назад
    				ShotDigitIndex--;
    				if (ShotDigitIndex < 0) {
    					// есть ошибка в ранее переданных быках и коровах // Info("Error");
    					return;
    				}
    				// и подставить другую цифру на спорное место
    				DigitsForAnswerIndex = ShotDigitInDigits_index[ShotDigitIndex] + 1;
    				if (ShotDigitIndex == 0) {
    					DigitsForAnswer.remove(0);
    					ShotDigitIndex = 0;
    					DigitsForAnswerIndex = 0;
    				}
    				continue;
    			}
    		}

    		for(int i = 0; i < 4; i++) {ShotDigits.set(i, NextShot[i]);}
    		for(int i = 0; i < 4; i++) {Indexes.set(i, -1);}

    		if(TrySetBulls()) {
    			for(int n = 0; n < 4; n++) {
    				Digits[Indexes.get(n)] = ShotDigits.get(n);
    			}
    		} else {
    			// есть несочетаемый вариант быков в цифрах // ShowStepInfo("bull error");
    			Digits = NextShot.clone();
    		}

//	    Digits = NextShot.clone();	    // подстановка свойства для вычисления быков и коров
    		CalcBullCow();		    // вычисление
    		Integer[] TmpBufI = new Integer[4];
    		for(int i = 0; i < 4; i++) {TmpBufI[i] = Digits[i];}
    		Shots_digits.add(TmpBufI);	    // заполнение списков попыток очередной попыткой
//	    Shots_digits.add(NextShot);	    // заполнение списков попыток очередной попыткой
					    // (цифры, быки, коровы)
    		Shots_bulls.add(bulls);
    		Shots_cows.add(cows);

    		ShowNextShot(Shots_digits.size());	// отображение

    		ShotDigitIndex = 0;		// обнуление индексов
    		DigitsForAnswerIndex = 0;
    	}
//	ShowStepInfo("Цифры получены.");
    	while(bulls < 4) {
    		for(int i = 0; i < 4; i++) {ShotDigits.set(i, NextShot[i]);}
    		for(int i = 0; i < 4; i++) {Indexes.set(i, -1);}

    		if(TrySetBulls()) {
    			for(int n = 0; n < 4; n++) {
    				Digits[Indexes.get(n)] = ShotDigits.get(n);
    			}
    		} else {
    			// есть несочетаемый вариант быков в цифрах // ShowStepInfo("bull error");
    			Digits = NextShot.clone();
    		}
    		CalcBullCow();
    		Integer[] NextShot2 = new Integer[4];
    		NextShot2 = Digits.clone();
    		Shots_digits.add(NextShot2);
    		Shots_bulls.add(bulls);
    		Shots_cows.add(cows);
    		ShowNextShot(Shots_digits.size());	// отображение

    	}
    }
}

