package romeogolf;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class SettingsController implements Initializable{
	// переключатели права первого хода
	@FXML private RadioButton rbFirstStep0;
	@FXML private RadioButton rbFirstStep1;
	@FXML private RadioButton rbFirstStep2;
	@FXML private RadioButton rbFirstStep3;
	@FXML private ToggleGroup tgFirstStep;

	//флажок сброса цифр в исходное (1234)
	@FXML private CheckBox cbDigitsReset;

	private Stage stage;
	private StoredDataManager sdm;
	private ArrayList<RadioButton> aFirstStep = new ArrayList<RadioButton>();

	@FXML protected void onOK(ActionEvent e){
		this.sdm.setDigitsReset(this.cbDigitsReset.isSelected());
		this.sdm.setFirstStep(this.aFirstStep.indexOf(
							(RadioButton)this.tgFirstStep.getSelectedToggle()));
		stage.close();
	}

	@FXML protected void onCancel(ActionEvent e){
		stage.close();
	}

    // получение ссылки на окно, установка обработчика событий окна 
    public void setStage_Listener(final Stage stage){
    	this.stage = stage;
    }

    public void setSDM(final StoredDataManager sdm){
    	this.sdm = sdm;
    	Boolean checked = sdm.getDigitsReset();
    	if(checked != null){
    		this.cbDigitsReset.setSelected(checked);
    	}
    	Integer firstStepIndex = sdm.getFirstStep();
    	if((firstStepIndex != null) && (firstStepIndex >= 0) 
    												&& (firstStepIndex <= 3)){
    		int ind = aFirstStep.indexOf(
    						(RadioButton)this.tgFirstStep.getSelectedToggle());
    		if(ind != firstStepIndex){
    			tgFirstStep.selectToggle(this.aFirstStep.get(firstStepIndex));
    		}
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		aFirstStep.add(rbFirstStep0);
		aFirstStep.add(rbFirstStep1);
		aFirstStep.add(rbFirstStep2);
		aFirstStep.add(rbFirstStep3);
	}

}
