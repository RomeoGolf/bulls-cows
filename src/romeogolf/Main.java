package romeogolf;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.fxml.*;

public class Main extends Application {
		public static void main(String[] args) {
			launch(args);
		}
		@Override
		public void start(Stage stage) throws Exception {
		    Parent root = FXMLLoader.load(getClass().getResource("bc.fxml"));

		    //Scene scene = new Scene(root, 400, 500);
		    Scene scene = new Scene(root);
		    stage.setTitle("FXML Welcome");
		    stage.setScene(scene); 
		    stage.show();

		}
	}
