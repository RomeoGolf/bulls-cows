package romeogolf;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import java.util.*;

public class Main extends Application {
    Integer[] Digits = new Integer[4];			// Цифры, вводимые пользователем
    TextField[] atfDigits = new TextField[4];		// Поля ввода цифр
    Random rg = new Random(System.currentTimeMillis());	// Генератор ПСП, инициализируемый временем
    Integer[] RandDigits = new Integer[10];		// для перемешанного массива цифр
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

    public Map<Button, Integer> df;	// карта соответствия кнопок цифрам
    public Set<Button> sUp;		// множество кнопок увеличения

    // общий обработчик для всех кнопок
    EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
	@Override
	public void handle(ActionEvent e) {
	    int Num = df.get(e.getSource());		// получение номера цифры, чью кнопку нажали
	    if (sUp.contains(e.getSource())){		// изменение цифры
		Digits[Num]++;
	    } else {
		Digits[Num]--;
	    }
	    if (Digits[Num] < 0){Digits[Num] = 9;}	// проверка выхода цифры за пределы
	    if (Digits[Num] > 9){Digits[Num] = 0;}	// и соответствующее изменение
	    atfDigits[Num].setText(Integer.toString(Digits[Num]));  // отображение цифры
	};
    };

    public static void main(String[] args) {
	launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
	for(int i = 0; i <= 3; i++){		// инициализация цифр, создание полей ввода
	    Digits[i] = rg.nextInt(9);
	    atfDigits[i] = new TextField(Integer.toString(Digits[i]));
	    atfDigits[i].setPrefColumnCount(1);
	    atfDigits[i].setEditable(false);
	}
	for(int i = 0; i <= 9; i++){
	    RandDigits[i] = i;
	}
	// перемешивание массива цифр
	int i = 9;
	int n = 0;
	int buf = 0;
	while(i > 0){
	    n = rg.nextInt(i);
	    buf = RandDigits[n];
	    RandDigits[n] = RandDigits[9];
	    RandDigits[9] = buf;
	    i--;
	}

	// сцена на основе BorderPane
	BorderPane bp = new BorderPane();

	HBox hbTop = new HBox();
	hbTop.setAlignment(Pos.CENTER);
	hbTop.setPadding(new Insets(15, 12, 15, 12));

	HBox hbBottom = new HBox();
	hbTop.setStyle("-fx-background-color: #336699;");
	bp.setTop(hbTop);
	hbBottom.setAlignment(Pos.CENTER);
	bp.setBottom(hbBottom);


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
	hbDigits.getChildren().addAll(vb1, vb2, vb3, vb4);
	hbTop.getChildren().add(hbDigits);
	// -------- конец прорисовки --------------

	// ----- нижняя панель ----------
	final TextField tfBottom = new TextField();
	Button btBottom = new Button("Test");
	hbBottom.getChildren().addAll(tfBottom, btBottom);
	btBottom.setOnAction(new EventHandler<ActionEvent>() {
	    @Override public void handle(ActionEvent e) {
		String s = "";
		for(int i = 0; i <= 9; i++){
		    s = s + Integer.toString(RandDigits[i]) + " ";
		}
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

	primaryStage.setTitle("CheckOut");
	Scene scene = new Scene(bp, 300, 275);

	primaryStage.setScene(scene); 
	primaryStage.show();
    }
}

