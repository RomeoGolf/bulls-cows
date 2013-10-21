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
	public TextField userTextField;
	EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
			userTextField.setText(Integer.toString(Integer.valueOf(userTextField.getText()) + 1));
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

//		final TextField userTextField = new TextField("1");
		userTextField = new TextField("1");
		userTextField.setPrefColumnCount(1);
		grid.add(userTextField, 0, 0, 1, 2);

		Button btUp1 = new Button();
		grid.add(btUp1, 1, 0);
		Button btDown1 = new Button();
		grid.add(btDown1, 1, 1);
//		btUp1.setOnAction(new EventHandler<ActionEvent>() {
//		    @Override
//		    public void handle(ActionEvent e) {
//			userTextField.setText(Integer.toString(Integer.valueOf(userTextField.getText()) + 1));
//		    }
//		});
		btUp1.setOnAction(eh);


		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene); 

		primaryStage.show();
	
	}
}

