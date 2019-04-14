package romeogolf.bc;

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
    @FXML private RadioButton firstStep0RadioButton;
    @FXML private RadioButton firstStep1RadioButton;
    @FXML private RadioButton firstStep2RadioButton;
    @FXML private RadioButton firstStep3RadioButton;
    @FXML private ToggleGroup firstStepToggleGroup;

    //флажок сброса цифр в исходное (1234)
    @FXML private CheckBox digitsResetCheckBox;

    private Stage stage;
    private StoredDataManager storedDataManager;
    private final ArrayList<RadioButton> firstSteps = new ArrayList<>();

    @FXML protected void onOK(ActionEvent e){
        this.storedDataManager.setDigitsReset(this.digitsResetCheckBox.isSelected());
        this.storedDataManager.setFirstStep(this.firstSteps.indexOf(
                this.firstStepToggleGroup.getSelectedToggle()));
        stage.close();
    }

    @FXML protected void onCancel(ActionEvent e){
        stage.close();
    }

    // получение ссылки на окно, установка обработчика событий окна 
    void setStage_Listener(final Stage stage){
        this.stage = stage;
    }

    void setSDM(final StoredDataManager sdm){
        this.storedDataManager = sdm;
        Boolean checked = sdm.getDigitsReset();
        if(checked != null){
            this.digitsResetCheckBox.setSelected(checked);
        }
        Integer firstStepIndex = sdm.getFirstStep();
        if((firstStepIndex != null) && (firstStepIndex >= 0) 
                                                    && (firstStepIndex <= 3)){
            int ind = firstSteps.indexOf(
                    this.firstStepToggleGroup.getSelectedToggle());
            if(ind != firstStepIndex){
                firstStepToggleGroup.selectToggle(this.firstSteps.get(firstStepIndex));
            }
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        firstSteps.add(firstStep0RadioButton);
        firstSteps.add(firstStep1RadioButton);
        firstSteps.add(firstStep2RadioButton);
        firstSteps.add(firstStep3RadioButton);
    }

}
