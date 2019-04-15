package romeogolf;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MainController implements Initializable{
	// ���������� ���������������� ����
	@FXML private Label charsell_1_1;
	@FXML private Label charsell_1_2;
	@FXML private Label charsell_1_3;
	@FXML private Label charsell_1_4;
	// ���������� ���� 2 ������
	@FXML private Label charsell_2_1;
	@FXML private Label charsell_2_2;
	@FXML private Label charsell_2_3;
	@FXML private Label charsell_2_4;
	// ���� ����������� ���������� ������������������
	@FXML private TextField indicator;
	// ������ ��� ��������� ����
	@FXML private Button btUp_1;
	@FXML private Button btDown_1;
	@FXML private Button btUp_2;
	@FXML private Button btDown_2;
	@FXML private Button btUp_3;
	@FXML private Button btDown_3;
	@FXML private Button btUp_4;
	@FXML private Button btDown_4;
	// ���� ��� ����������� ����� ������� 1 � 2
	@FXML private VBox vbPlayer1;
	@FXML private ScrollPane spPlayer1;
	@FXML private VBox vbPlayer2;
	@FXML private ScrollPane spPlayer2;
	// ������������ ������
	@FXML private ToggleGroup tgMode;
	// ������ ����������
	@FXML private Button btSetQuad;
	@FXML private Button btGenerateQuad;
	@FXML private Button btReset;

	private Integer mode = 0;
	void setMode(Integer m){
		this.mode = m;
		switch(mode){
		case 0 :	// ����� "������� ���������"
			this.btSetQuad.setDisable(true);
			this.btGenerateQuad.setDisable(false);
			break;
		case 1 :	// ����� "������� � ������ ��������� ���� � �����"
			this.btSetQuad.setDisable(false);
			this.btGenerateQuad.setDisable(false);
			break;
		case 2 :	// ����� "������� � ������ �������� ���� �����������"
			this.btSetQuad.setDisable(true);
			this.btGenerateQuad.setDisable(false);
			break;
		case 3 :	// ����� "������ ��������� (��������)"
			this.btSetQuad.setDisable(false);
			this.btGenerateQuad.setDisable(false);
			break;
		default :	// ����� "��"
			indicator.setText("����� ��");
		}
	}

	Integer getMode(){
		return this.mode;
	}

	// �����, �������� �������������
    Integer[] DigitsForShow = new Integer[4];
    // ������ ��������� ��� ���������������� ����
    ArrayList<Label> aQuad1 = new ArrayList<Label>();
    // ������ ��������� ��� ���������� ����
    ArrayList<Label> aQuad2 = new ArrayList<Label>();
    // ����� ������������ ������ ������	
    public Map<Button, Integer> df = new HashMap<Button, Integer>();
    // ��������� ������ ����������
    public Set<Button> sUp = new HashSet<Button>();;

    // �������� ������ - �������� ����������
    @FXML protected void onTest(ActionEvent event) {
    	//indicator.setText("qwerty");
    	//this.ShowStepInfo("zxcvbn", true);
    	//this.ShowStepInfo("asdfgh", false);
    	//String s = new String(Arrays.toString(DigitsForShow));
    	String s = new String(Arrays.toString(curator.getQuad(2)));
    	indicator.setText(s);
    };

    // ����� ���������� ��� ���� ������ Up & Down
    @FXML protected void onUpDown(ActionEvent e) {
    		int Num = df.get(e.getSource());	// ��������� ������ �����, ��� ������ ������
    		if (sUp.contains(e.getSource())){	// ��������� �����
    			DigitsForShow[Num]++;
    		} else {
    			DigitsForShow[Num]--;
    		}
    		// �������� ������ ����� �� �������
    		if (DigitsForShow[Num] < 0){DigitsForShow[Num] = 9;}
    			// � ��������������� ���������
    		if (DigitsForShow[Num] > 9){DigitsForShow[Num] = 0;}
    		aQuad1.get(Num).setText(Integer.toString(DigitsForShow[Num]));
    		IsDifferent();
    };

    @FXML protected void onShot(ActionEvent e) {
    	SelfAnswer();
    }

    @FXML protected void onSetQwad(ActionEvent e) {
    	if (!this.IsDifferent()){
    		curator.setQuad(this.DigitsForShow, 2);
    		for(int i = 0; i < 4; i++){
    			aQuad2.get(i).setText(curator.getQuad(2)[i].toString());
    		}
    	}
    }

    @FXML protected void onGenerateQwad(ActionEvent e) {
    	if((this.getMode() != 0) && (this.getMode() != 2)){
    		curator.generateQuad(2);
    		for(int i = 0; i < 4; i++){
    			aQuad2.get(i).setText(curator.getQuad(2)[i].toString());
    		}
    	}
    	curator.generateQuad(1);
    	//indicator.setText(Arrays.toString(curator.getQuad(1)));
    }

    @FXML protected void onReset(ActionEvent e) {
    	vbPlayer1.getChildren().removeAll(vbPlayer1.getChildren());

    }

    protected void onModeToggle(){
		if (tgMode.getSelectedToggle() != null) {
			setMode(Integer.decode(tgMode.getSelectedToggle().getUserData().toString()));
		}
    }


    // �������� ���� �� ����������
    boolean IsDifferent() {
    	//TODO: ������������ �� ������ ����?
    	for(int i = 0; i < 4; i++) {
    		aQuad1.get(i).setStyle("-fx-text-fill: #000000;");
    	}
    	for(int i = 0; i < 3; i++) {
    		for(int j = i + 1; j < 4; j++) {
    			if(DigitsForShow[i] == DigitsForShow[j]) {
    				aQuad1.get(i).setStyle("-fx-text-fill: #FF0000;");
    				aQuad1.get(j).setStyle("-fx-text-fill: #FF0000;");
    				return true;
    			}
    		}
    	}
    	return false;
    };

    // ������������� ����������
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// ���������� ����������� ��������� ������� ������ �� ScrollPane
		// ��� ��������� �� �����
		vbPlayer1.setSpacing(3);
		vbPlayer1.heightProperty().addListener(new ChangeListener<Object>() {
    		@Override
    		public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
    			if (vbPlayer1.getHeight() > spPlayer1.getHeight()) {
    				spPlayer1.setVvalue((Double)newValue );
    			}
            }
        });
		vbPlayer2.setSpacing(3);
		vbPlayer2.heightProperty().addListener(new ChangeListener<Object>() {
    		@Override
    		public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
    			if (vbPlayer2.getHeight() > spPlayer2.getHeight()) {
    				spPlayer2.setVvalue((Double)newValue );
    			}
            }
        });
		// ���������� ����� ������������ ������ �� �������
    	df.put(btUp_1, 0);
    	df.put(btDown_1, 0);
    	df.put(btUp_2, 1);
    	df.put(btDown_2, 1);
    	df.put(btUp_3, 2);
    	df.put(btDown_3, 2);
    	df.put(btUp_4, 3);
    	df.put(btDown_4, 3);
    	// ��������� ������ "�����"
    	sUp.add(btUp_1);
    	sUp.add(btUp_2);
    	sUp.add(btUp_3);
    	sUp.add(btUp_4);
    	// ��������� ��������� ��� ����
    	aQuad1.add(charsell_1_1);
    	aQuad1.add(charsell_1_2);
    	aQuad1.add(charsell_1_3);
    	aQuad1.add(charsell_1_4);
    	// ��������� ��������� ��� ���������� ����
    	aQuad2.add(charsell_2_1);
    	aQuad2.add(charsell_2_2);
    	aQuad2.add(charsell_2_3);
    	aQuad2.add(charsell_2_4);
    	// ��������� ���������� ������� ���� � ��� �����������
    	for(int i = 0; i < 4; i++){
    		this.DigitsForShow[i] = i;
    		aQuad1.get(i).setText(Integer.toString(i));
    	}

    	this.setMode(0);
        tgMode.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
        	public void changed(ObservableValue<? extends Toggle> ov,
        			Toggle old_toggle, Toggle new_toggle) {
        		onModeToggle();
        	}
        });
	}

	// ����� ������ ������ � ScrollPane
    void ShowStepInfo(String s, boolean Player1) {
    	HBox hb = new HBox();
    	hb.setAlignment(Pos.CENTER);
    	Text t = new Text(s);
    	t.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
    	hb.getChildren().add(t);
		hb.setStyle("-fx-background-color: #33FFFF;");
		if (Player1) {
			vbPlayer1.getChildren().add(hb);
		} else {
			vbPlayer2.getChildren().add(hb);
		}
    }

	DigitCurator curator = new DigitCurator();
	Solver solver = new Solver();
    Integer[] Digits = new Integer[4];			// �����, �������� �������������
    int bulls;
    int cows;
    int trying;

    // ����� ����������� �������
    void ShowNextShot(int trying) {
		String s = new String(Arrays.toString(Digits));
		s = Integer.toString(trying) + ": " + s;
		s = s + " -   " + Integer.toString(bulls) + " �, " +
		    Integer.toString(cows) + " �";
		ShowStepInfo(s, true);
    }

    // ========= ��������� ��������������� ������� ======
    void SelfAnswer() {	    // �������
    	//curator.Init();
    	solver.Init(curator.RndAllDigits);
    	//curator.DigitMixer();			// ���������� ����� ����������� ����� ����

    	while(bulls + cows < 4) {		// ���� �� ������� ���� ����
    		Digits = solver.ToFindDigits(Digits);
    		Integer[] TmpBufI = new Integer[4];
    		for(int i = 0; i < 4; i++) {TmpBufI[i] = Digits[i];}
    		ShotData shot_data = curator.checkQuad(Digits, 2);
    		bulls = shot_data.getBulls();
    		cows = shot_data.getCows();
    		solver.shots_data.add(shot_data);
    		ShowNextShot(solver.shots_data.size());	// �����������
    		solver.ShotDigitIndex = 0;		// ��������� ��������
    		solver.DigitsForAnswerIndex = 0;
    	}
    	while(bulls < 4) {
    		solver.ToFindBulls(Digits);
    		ShotData shot_data = curator.checkQuad(Digits, 2);
    		bulls = shot_data.getBulls();
    		cows = shot_data.getCows();
    		solver.shots_data.add(shot_data);
    		ShowNextShot(solver.shots_data.size());	// �����������
    	}
    	solver.shots_data.clear();
    	bulls = 0;
    	cows = 0;
    }
}
