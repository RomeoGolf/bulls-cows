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

public class Main extends Application {
	public TextField tfDigit1;
	public TextField tfDigit2;
	public TextField tfDigit3;
	public TextField tfDigit4;
	public Button btUp1;
	public Button btUp2;
	public Button btUp3;
	public Button btUp4;

	public Button btDown1;
	public Button btDown2;
	public Button btDown3;
	public Button btDown4;
	EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
			tfDigit1.setText(Integer.toString(Integer.valueOf(tfDigit1.getText()) + 1));
		    };
	};

	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("CheckOut");

		GridPane grid = new GridPane();

		grid.setAlignment(Pos.CENTER);
//		grid.setHgap(10);
//		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setGridLinesVisible(true);

		tfDigit1 = new TextField("1");
		tfDigit1.setPrefColumnCount(1);
		grid.add(tfDigit1, 0, 1);
		btUp1 = new Button();
		grid.add(btUp1, 0, 0);
		btDown1 = new Button();
		grid.add(btDown1, 0, 2);
		btUp1.setOnAction(eh);

		tfDigit2 = new TextField("1");
		tfDigit2.setPrefColumnCount(1);
		grid.add(tfDigit2, 2, 1);
		btUp2 = new Button();
		grid.add(btUp2, 2, 0);
		btDown2 = new Button();
		grid.add(btDown2, 2, 2);
		btUp2.setOnAction(eh);

		tfDigit3 = new TextField("1");
		tfDigit3.setPrefColumnCount(1);
		grid.add(tfDigit3, 4, 1);
		btUp3 = new Button();
		grid.add(btUp3, 4, 0);
		btDown3 = new Button();
		grid.add(btDown3, 4, 2);
		btUp3.setOnAction(eh);

		tfDigit4 = new TextField("1");
		tfDigit4.setPrefColumnCount(1);
		grid.add(tfDigit4, 6, 1);
		btUp4 = new Button();
		grid.add(btUp4, 6, 0);
		btDown4 = new Button();
		grid.add(btDown4, 6, 2);
		btUp4.setOnAction(eh);


		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene); 

		primaryStage.show();
	
	}
}

