package romeogolf;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.*;

public class Main extends Application {
		public static void main(String[] args) {
			launch(args);
		}
		@Override
		public void start(Stage stage) throws Exception {
			//Parent root = FXMLLoader.load(getClass().getResource("bc.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("bc.fxml"));
			Parent root = (Parent) loader.load();
			MainController controller = (MainController)loader.getController();
			controller.setStage_Listener(stage); 

		    //Scene scene = new Scene(root, 400, 500);
		    Scene scene = new Scene(root);
		    stage.setTitle("���� � ������");

		    stage.getIcons().add(new Image(this.getClass().getResourceAsStream(
					"/res/img/Roman32.png")));
		    stage.getIcons().add(new Image(this.getClass().getResourceAsStream(
					"/res/img/Roman16.png")));

		    stage.setScene(scene); 
		    stage.show();
		    stage.setMaxWidth(stage.getWidth());
		    stage.setMinWidth(stage.getWidth());
		    stage.setMinHeight(600);
		}
	}
