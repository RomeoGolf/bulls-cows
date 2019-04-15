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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	@FXML private Button btTest;
	// ������������� ������
	@FXML private RadioButton rbMode0;
	@FXML private RadioButton rbMode1;
	@FXML private RadioButton rbMode2;
	@FXML private RadioButton rbMode3;

	private Integer Player1ShotNum = 0;

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
    	String s = new String("q1= ");
    	s = s +	Arrays.toString(curator.getQuad(1));
    	s = s +	"; q2= " + Arrays.toString(curator.getQuad(2));
    	s = s + "; dec=" + Arrays.toString(curator.getDecade());
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
		switch(mode){
		case 0 :	// ����� "������� ���������"
			this.ShotMode0();
			break;
		case 1 :	// ����� "������� � ������ ��������� ���� � �����"
			this.ShotMode1();
			break;
		case 2 :	// ����� "������� � ������ �������� ���� �����������"
			this.ShotMode2();
			break;
		case 3 :	// ����� "������ ��������� (��������)"
			this.Reset();
			SelfAnswer();
			break;
		default :	// ����� "��"
			indicator.setText("����� ��");
		}
    }

    @FXML protected void onSetQwad(ActionEvent e) {
    	if (!this.IsDifferent()){
    		curator.setQuad(this.DigitsForShow, 2);
    		solver.Init(curator.getDecade());
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
		solver.Init(curator.getDecade());
    	//indicator.setText(Arrays.toString(curator.getQuad(1)));
    }

    void Reset(){
    	vbPlayer1.getChildren().removeAll(vbPlayer1.getChildren());
    	vbPlayer2.getChildren().removeAll(vbPlayer2.getChildren());
    	curator.DigitMixer();			// ���������� ����� ����������� ����� ����
    	solver.Init(curator.getDecade());
    	Player1ShotNum = 0;
    	bulls = 0;
    	cows = 0;
		solver.Init(curator.getDecade());
    }

    @FXML protected void onReset(ActionEvent e) {
    	this.Reset();
    }

    protected void onModeToggle(){
		if (tgMode.getSelectedToggle() != null) {
			setMode(Integer.decode(tgMode.getSelectedToggle().getUserData().toString()));
		}
		solver.Init(curator.getDecade());
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
		getImages();
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
		setControlMaps();
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

	private void setControlMaps(){
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
	}

	private void getImages(){
		Image iTest = new Image(this.getClass().getResourceAsStream("/res/img/test.png"));
		this.btTest.setGraphic(new ImageView(iTest));

		Image iUp = new Image(this.getClass().getResourceAsStream("/res/img/up.png"));
		Image iDown = new Image(this.getClass().getResourceAsStream("/res/img/down.png"));
		this.btUp_1.setGraphic(new ImageView(iUp));
		this.btUp_2.setGraphic(new ImageView(iUp));
		this.btUp_3.setGraphic(new ImageView(iUp));
		this.btUp_4.setGraphic(new ImageView(iUp));
		this.btDown_1.setGraphic(new ImageView(iDown));
		this.btDown_2.setGraphic(new ImageView(iDown));
		this.btDown_3.setGraphic(new ImageView(iDown));
		this.btDown_4.setGraphic(new ImageView(iDown));

		Image iMode0 = new Image(this.getClass().getResourceAsStream("/res/img/user-comp.png"));
		this.rbMode0.setGraphic(new ImageView(iMode0));
		this.rbMode0.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		Image iMode1 = new Image(this.getClass().getResourceAsStream("/res/img/user vs comp.png"));
		this.rbMode1.setGraphic(new ImageView(iMode1));
		this.rbMode1.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		Image iMode2 = new Image(this.getClass().getResourceAsStream("/res/img/user,comp-comp.png"));
		this.rbMode2.setGraphic(new ImageView(iMode2));
		this.rbMode2.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		Image iMode3 = new Image(this.getClass().getResourceAsStream("/res/img/comp-comp.png"));
		this.rbMode3.setGraphic(new ImageView(iMode3));
		this.rbMode3.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

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
    void ShowNextShot(int trying, boolean Player1) {
		String s = new String(Arrays.toString(Digits));
		s = Integer.toString(trying) + ": " + s;
		s = s + " -   " + Integer.toString(bulls) + " �, " +
		    Integer.toString(cows) + " �";
		ShowStepInfo(s, Player1);
    }

    // ========= �������� ������� ======
    void SelfAnswer() {	    // �������
    	//curator.Init();
    	curator.DigitMixer();			// ���������� ����� ����������� ����� ����
    	solver.Init(curator.getDecade());

    	while(bulls + cows < 4) {		// ���� �� ������� ���� ����
    		Digits = solver.ToFindDigits(Digits);
    		Integer[] TmpBufI = new Integer[4];
    		for(int i = 0; i < 4; i++) {TmpBufI[i] = Digits[i];}
    		ShotData shot_data = curator.checkQuad(Digits, 2);
    		bulls = shot_data.getBulls();
    		cows = shot_data.getCows();
    		solver.shots_data.add(shot_data);
    		ShowNextShot(solver.shots_data.size(), false);	// �����������
    		solver.ShotDigitIndex = 0;		// ��������� ��������
    		solver.DigitsForAnswerIndex = 0;
    	}
    	while(bulls < 4) {
    		solver.ToFindBulls(Digits);
    		ShotData shot_data = curator.checkQuad(Digits, 2);
    		bulls = shot_data.getBulls();
    		cows = shot_data.getCows();
    		solver.shots_data.add(shot_data);
    		ShowNextShot(solver.shots_data.size(), false);	// �����������
    	}
    	solver.shots_data.clear();
    	bulls = 0;
    	cows = 0;
    }

    void ShotMode0(){
    	if (!this.IsDifferent()){
    		Player1ShotNum++;
    		ShotData shot_data = curator.checkQuad(this.DigitsForShow, 1);
    		String s = new String(Arrays.toString(DigitsForShow));
    		s = Integer.toString(Player1ShotNum) + ": " + s;
    		s = s + " -   " + Integer.toString(shot_data.getBulls()) + " �, " +
    		    Integer.toString(shot_data.getCows()) + " �";
    		ShowStepInfo(s, true);
    	}
    }

    void ShotMode1(){
    	if (!this.IsDifferent()){
    		Player1ShotNum++;
    		ShotData shot_data = curator.checkQuad(this.DigitsForShow, 1);
    		String s = new String(Arrays.toString(DigitsForShow));
    		s = Integer.toString(Player1ShotNum) + ": " + s;
    		s = s + " -   " + Integer.toString(shot_data.getBulls()) + " �, " +
    		    Integer.toString(shot_data.getCows()) + " �";
    		ShowStepInfo(s, true);

    		Digits = solver.ToFindDigits(Digits);
    		Integer[] TmpBufI = new Integer[4];
    		for(int i = 0; i < 4; i++) {TmpBufI[i] = Digits[i];}
    		ShotData shot_data2 = curator.checkQuad(Digits, 2);
    		bulls = shot_data2.getBulls();
    		cows = shot_data2.getCows();
    		solver.shots_data.add(shot_data2);
    		ShowNextShot(solver.shots_data.size(), false);	// �����������
    		solver.ShotDigitIndex = 0;		// ��������� ��������
    		solver.DigitsForAnswerIndex = 0;
    	}
    }

    void ShotMode2(){
    	if (!this.IsDifferent()){
    		Player1ShotNum++;
    		ShotData shot_data = curator.checkQuad(this.DigitsForShow, 1);
    		String s = new String(Arrays.toString(DigitsForShow));
    		s = Integer.toString(Player1ShotNum) + ": " + s;
    		s = s + " -   " + Integer.toString(shot_data.getBulls()) + " �, " +
    		    Integer.toString(shot_data.getCows()) + " �";
    		ShowStepInfo(s, true);

    		Digits = solver.ToFindDigits(Digits);
    		Integer[] TmpBufI = new Integer[4];
    		for(int i = 0; i < 4; i++) {TmpBufI[i] = Digits[i];}
    		ShotData shot_data2 = curator.checkQuad(Digits, 1);
    		bulls = shot_data2.getBulls();
    		cows = shot_data2.getCows();
    		solver.shots_data.add(shot_data2);
    		//ShowNextShot(solver.shots_data.size(), false);	// �����������
    		String s2 = new String(Integer.toString(solver.shots_data.size()));
    		s2 = s2 + ": -   " + Integer.toString(bulls) + " �, " +
    		    Integer.toString(cows) + " �";
    		ShowStepInfo(s2, false);

    		solver.ShotDigitIndex = 0;		// ��������� ��������
    		solver.DigitsForAnswerIndex = 0;
    	}
    }

}
