package romeogolf;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class HelpController implements Initializable{

	@FXML private WebView wvHelp;
	private WebEngine we;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		we = wvHelp.getEngine();
		String s = this.getClass().getResource(
								"/res/help/index.html").toExternalForm();
		we.load(s);
	}

}
