package romeogolf.bc;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class HelpController implements Initializable{

    @FXML private WebView wvHelp;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        WebEngine we = wvHelp.getEngine();
        String s = this.getClass().getResource(
                                "/res/help/index.html").toExternalForm();
        we.load(s);
    }

}
