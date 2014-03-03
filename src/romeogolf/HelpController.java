package romeogolf;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class HelpController implements Initializable{
	@FXML private Button btTest;
	@FXML private Button btOk;
	@FXML private WebView wvHelp;
	private Stage stage;
	private WebEngine we;

	@FXML protected void onOk(ActionEvent e){
		stage.close();
	}

	@FXML protected void onTest(ActionEvent e){
		we.load("/res/html/HTML~/index.html");
	}

	// получение ссылки на окно, установка обработчика событий окна 
	public void setStage_Listener(final Stage stage){
		this.stage = stage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		we = wvHelp.getEngine();

	}

}
