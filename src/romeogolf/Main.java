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
import javax.swing.JOptionPane;

public class Main extends Application {
    Integer[] Digits = new Integer[4];
    TextField[] atfDigits = new TextField[4];
    Random rg = new Random(System.currentTimeMillis());

    public Button btUp1;
    public Button btUp2;
    public Button btUp3;
    public Button btUp4;

    public Button btDown1;
    public Button btDown2;
    public Button btDown3;
    public Button btDown4;

    public Map<Button, Integer> df;
    public Set<Button> sUp;

    EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
	@Override
	public void handle(ActionEvent e) {
//	    JOptionPane.showMessageDialog(null, e.getSource() /*df.get(btUp1)*/, "qwerty", JOptionPane.INFORMATION_MESSAGE);
		};
    };

    public static void main(String[] args) {
	launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
	for(int i = 0; i <= 3; i++){
	    Digits[i] = rg.nextInt(9);
	}

	primaryStage.setTitle("CheckOut");

	GridPane grid = new GridPane();

	grid.setAlignment(Pos.CENTER);
	grid.setHgap(5);
//	grid.setVgap(10);
	grid.setPadding(new Insets(25, 25, 25, 25));
//	grid.setGridLinesVisible(true);

	atfDigits[0] = new TextField(Integer.toString(Digits[0]));
	atfDigits[0].setPrefColumnCount(1);
	btUp1 = new Button();
	btUp1.setId("11");
	btDown1 = new Button();
	btDown1.setId("12");
	btUp1.setOnAction(eh);
	btDown1.setOnAction(eh);

	VBox vb1 = new VBox();
	vb1.setAlignment(Pos.CENTER);
	vb1.getChildren().add(btUp1);
	vb1.getChildren().add(atfDigits[0]);
	vb1.getChildren().add(btDown1);
	grid.add(vb1, 0, 0);

	atfDigits[1] = new TextField(Integer.toString(Digits[1]));
	atfDigits[1].setPrefColumnCount(1);
	btUp2 = new Button();
	btUp2.setId("21");
	btDown2 = new Button();
	btDown2.setId("22");
	btUp2.setAlignment(Pos.CENTER);
	btUp2.setOnAction(eh);
	btDown2.setOnAction(eh);

	VBox vb2 = new VBox();
	vb2.setAlignment(Pos.CENTER);
	vb2.getChildren().add(btUp2);
	vb2.getChildren().add(atfDigits[1]);
	vb2.getChildren().add(btDown2);
	grid.add(vb2, 1, 0);

	atfDigits[2] = new TextField(Integer.toString(Digits[2]));
	atfDigits[2].setPrefColumnCount(1);
	btUp3 = new Button();
	btUp3.setId("31");
	btDown3 = new Button();
	btDown3.setId("32");
	btUp3.setOnAction(eh);
	btDown3.setOnAction(eh);

	VBox vb3 = new VBox();
	vb3.setAlignment(Pos.CENTER);
	vb3.getChildren().add(btUp3);
	vb3.getChildren().add(atfDigits[2]);
	vb3.getChildren().add(btDown3);
	grid.add(vb3, 2, 0);

	atfDigits[3] = new TextField(Integer.toString(Digits[3]));
	atfDigits[3].setPrefColumnCount(1);
	btUp4 = new Button();
	btUp4.setId("41");
	btDown4 = new Button();
	btDown4.setId("42");
	btUp4.setOnAction(eh);
	btDown4.setOnAction(eh);

	VBox vb4 = new VBox();
	vb4.setAlignment(Pos.CENTER);
	vb4.getChildren().add(btUp4);
	vb4.getChildren().add(atfDigits[3]);
	vb4.getChildren().add(btDown4);
	grid.add(vb4, 3, 0);

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

	Scene scene = new Scene(grid, 300, 275);
	primaryStage.setScene(scene); 

	primaryStage.show();

    }
}

