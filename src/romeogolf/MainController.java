package romeogolf;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
	@FXML private VBox vbRight;
	// ������������ ������
	@FXML private ToggleGroup tgMode;
	// ������ ����������
	@FXML private Button btSetQuad;
	@FXML private Button btGenerateQuad;
	@FXML private Button btReset;
	@FXML private Button btTest;
	@FXML private Button btShot;
	// ������������� ������
	@FXML private RadioButton rbMode0;
	@FXML private RadioButton rbMode1;
	@FXML private RadioButton rbMode2;
	@FXML private RadioButton rbMode3;
	// ������ ������
	@FXML private HBox hbBottom;

	// ���������� ������� ������� ������ (��������)
	private Integer Player1ShotNum = 0;

	// ����� ����
	private Integer mode = 0;
	void setMode(Integer m){
		this.Reset();
		this.mode = m;
		/*
		0 :	// ����� "������� ���������"
		1 :	// ����� "������� � ������ ��������� ���� � �����"
		2 :	// ����� "������� � ������ �������� ���� �����������"
		3 :	// ����� "������ ��������� (��������)"
		 */
		if (mode == 0){
			curator.generateQuad(1);
		} else {
			generateQwads();
		}
		this.setDisableBt(false);
		if ((mode == 0) || (mode == 2)){
			this.setXToPlayer2();
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
    // ������ ����������� - ������������� ������
    ArrayList<RadioButton> aRbMode = new ArrayList<RadioButton>();
    // ����� ������������ ������ ������	
    public Map<Button, Integer> df = new HashMap<Button, Integer>();
    // ��������� ������ ����������
    public Set<Button> sUp = new HashSet<Button>();;

    // �������� ������ - �������� ����������
    @FXML protected void onTest(ActionEvent event) {
    	//this.FullTestForAlgotithm();
    	this.at.start();
    };

    // ����� ���������� ��� ���� ������ Up & Down
    @FXML protected void onUpDown(ActionEvent e) {
    		// ��������� ������ �����, ��� ������ ������
    		int Num = df.get(e.getSource());
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
    		isEqualDigits();
    };

    // ���������� ������ "�������"
    @FXML protected void onShot(ActionEvent e) {
    	this.setDisableBt(true);
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

    // ���������� ������ ��������� �������� ������� 1 ��� ������ 2
    @FXML protected void onSetQwad(ActionEvent e) {
    	this.Reset();
    	if (!this.isEqualDigits()){
    		curator.setQuad(this.DigitsForShow, 2);
    		solver.Init(curator.getDecade());
    		for(int i = 0; i < 4; i++){
    			aQuad2.get(i).setText(curator.getQuad(2)[i].toString());
    		}
    	}
    	this.btShot.setDisable(false);
    }

    // ���������� ������ ��������� ��������  ��� ����� �������
    @FXML protected void onGenerateQwad(ActionEvent e) {
    	this.Reset();
    	generateQwads();
    }

    private void generateQwads() {
    	if ((this.getMode() != 0) && (this.getMode() != 2)){
    		curator.generateQuad(2);
    		for(int i = 0; i < 4; i++){
    			aQuad2.get(i).setText(curator.getQuad(2)[i].toString());
    		}
    	}
    	curator.generateQuad(1);
		solver.Init(curator.getDecade());
    	this.btShot.setDisable(false);
    }

    // ����� ����������� ���������� ���� � ���������� � ���������
    void Reset(){
    	vbPlayer1.getChildren().removeAll(vbPlayer1.getChildren());
    	vbPlayer2.getChildren().removeAll(vbPlayer2.getChildren());
    	curator.DigitMixer();		// ���������� ����� ����������� ����� ����
    	solver.Init(curator.getDecade());
    	Player1ShotNum = 0;
    	bulls = 0;
    	cows = 0;
		solver.Init(curator.getDecade());
		this.setDisableBt(false);
		this.btShot.setDisable(false);
    }

    // ��������� ����������� ������ � ����������� �� ������
    private void setDisableBt(Boolean disable){
    	// ����������� ������ "��������" � "���������"
    	this.btGenerateQuad.setDisable(disable);
    	if (!disable){
    		this.btSetQuad.setDisable((this.mode == 0) || (this.mode == 2));
    	} else {
    		this.btSetQuad.setDisable(true);
    	}
    }

    // ������ "�����"
    @FXML protected void onReset(ActionEvent e) {
    	this.Reset();
    }

    // ��������� ������������ ������
    protected void onModeToggle(){
		if (tgMode.getSelectedToggle() != null) {
			setMode(Integer.decode(tgMode.getSelectedToggle().getUserData().toString()));
		} else {
			setMode(0);
		}
    }

    // �������� ���� �� ����������
    boolean isEqualDigits() {
    	for(int i = 0; i < 4; i++) {
    		aQuad1.get(i).setStyle("-fx-text-fill: #000000;");
    	}
    	for(int i = 0; i < 3; i++) {
    		for(int j = i + 1; j < 4; j++) {
    			if (DigitsForShow[i] == DigitsForShow[j]) {
    				aQuad1.get(i).setStyle("-fx-text-fill: #FF0000;");
    				aQuad1.get(j).setStyle("-fx-text-fill: #FF0000;");
    				return true;
    			}
    		}
    	}
    	return false;
    };


    // =================== ������������� ���������� ============================
    StoredDataManager sdm = new StoredDataManager();
    Stage stage;
    private void storePrefs(){
    	sdm.test_str = indicator.getText();
    	sdm.setTop(stage.getY());
    	sdm.setLeft(stage.getX());
    	sdm.setHeight(stage.getHeight());
    	sdm.setMode(this.mode);
    	sdm.writeData();
    }

    // �������������� ���������� ���� �� ����� ��������
    private void readPrefs(){
    	// ������ ������
    	Integer bufInt = sdm.getMode();
    	if(bufInt != null){
    		if((bufInt < 0) || (bufInt > 3)){
    			bufInt = 0;
    		}
    		if(this.aRbMode.indexOf((RadioButton)this.tgMode.getSelectedToggle()) != bufInt){
    			this.tgMode.selectToggle(aRbMode.get(bufInt));
    		}
    	}
    }

    // �������������� ���������� ���� �� ����� ��������
    private void readStagePrefs(){
    	Double bufDouble;
    	bufDouble = sdm.getTop();
    	if(bufDouble != null){
    		stage.setY(bufDouble);
    		//stage.setY(200);
    	}
    	bufDouble = sdm.getLeft();
    	if(bufDouble != null){
    		stage.setX(bufDouble);
    	}
    	bufDouble = sdm.getHeight();
    	if(bufDouble != null){
    		stage.setHeight(bufDouble);
    	}
    }

    // ��������� ������ �� ����, ��������� ����������� ������� ���� 
    //  � ������ ���������� ����
    public void setStage_Listener(final Stage stage){
    	this.stage = stage;
    	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    		@Override
    		public void handle(WindowEvent event) {
    			 //event.consume();
    			// ���������� �������� ����� ��������� ����
    			storePrefs();
    		}
    	});
    	// ������ ����������� ���������� ���� ����� ���������
    	//  ������ �� ����
    	this.readStagePrefs();
    }

    // ������������ ������ VBox ������ ���������� ScrollPane
    Map<VBox, ScrollPane> mPlayerPanes = new HashMap<VBox, ScrollPane>();

    // ����� ���������� ���������� ��� ����� VBox Player[X]
    private void setVBoxScroller(final VBox box){
    	box.setSpacing(3);
    	box.heightProperty().addListener(new ChangeListener<Object>() {
    		@Override
    		public void changed(ObservableValue<?> observable, Object oldValue, 
    														Object newValue) {
    			if((Double)oldValue > 0){
    				mPlayerPanes.get(box).setVvalue(1.0);
    			}
            }
        });
    }

    // ���������� �������������
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.indicator.setText(sdm.test_str);

		// �������� ����������� � �������� ����������
		getImages();
		// ���������� ����������� ��������� ������� ������ �� ScrollPane
		// ��� ��������� �� ����� ����
		mPlayerPanes.put(vbPlayer1, spPlayer1);
		mPlayerPanes.put(vbPlayer2, spPlayer2);
		setVBoxScroller(this.vbPlayer1);
		setVBoxScroller(this.vbPlayer2);

		setControlMaps();	// ��� ��������� ��������� ������
    	// ��������� ���������� ������� ���� � ��� �����������
    	for(int i = 0; i < 4; i++){
    		this.DigitsForShow[i] = i;
    		aQuad1.get(i).setText(Integer.toString(i));
    	}

    	// ��������� ����������� ������������ ������ ����
        tgMode.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
        	public void changed(ObservableValue<? extends Toggle> ov,
        			Toggle old_toggle, Toggle new_toggle) {
        		if(old_toggle != new_toggle){
        			onModeToggle();
        		}
        	}
        });

        this.readPrefs();	// ������ ����������� ���������� ����
        this.setAidDigitsMap();

		DropShadow ds = new DropShadow();
		ds.setOffsetX(3.0);
		ds.setOffsetY(3.0);
		ds.setColor(Color.GRAY);
		ds.setWidth(5.0);
		Reflection refl = new Reflection();
		refl.setFraction(0.5);
		ds.setInput(refl);
		for(int i = 0; i < 4; i++){
			this.aQuad2.get(i).setEffect(ds);
		}
		for(int i = 0; i < 10; i++){
			this.aAidDigits.get(i).setEffect(ds);
		}
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
    	// ��������� ����������� - ������������� ������
    	aRbMode.add(rbMode0);
    	aRbMode.add(rbMode1);
    	aRbMode.add(rbMode2);
    	aRbMode.add(rbMode3);
	}

	private void getImages(){
		Image iTest = new Image(this.getClass().getResourceAsStream(
														"/res/img/test.png"));
		this.btTest.setGraphic(new ImageView(iTest));

		Image iUp = new Image(this.getClass().getResourceAsStream(
															"/res/img/up.png"));
		Image iDown = new Image(this.getClass().getResourceAsStream(
														"/res/img/down.png"));
		this.btUp_1.setGraphic(new ImageView(iUp));
		this.btUp_2.setGraphic(new ImageView(iUp));
		this.btUp_3.setGraphic(new ImageView(iUp));
		this.btUp_4.setGraphic(new ImageView(iUp));
		this.btDown_1.setGraphic(new ImageView(iDown));
		this.btDown_2.setGraphic(new ImageView(iDown));
		this.btDown_3.setGraphic(new ImageView(iDown));
		this.btDown_4.setGraphic(new ImageView(iDown));

		DropShadow ds = new DropShadow();
		ds.setOffsetX(5.0);
		ds.setOffsetY(5.0);
		ds.setColor(Color.GRAY);
		ds.setWidth(5.0);
		ImageView iv;

		Image iMode0 = new Image(this.getClass().getResourceAsStream(
													"/res/img/user-comp.png"));
		iv = new ImageView(iMode0);
		iv.setEffect(ds);
		this.rbMode0.setGraphic(iv);
		this.rbMode0.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		Image iMode1 = new Image(this.getClass().getResourceAsStream(
												"/res/img/user vs comp.png"));
		iv = new ImageView(iMode1);
		iv.setEffect(ds);
		this.rbMode1.setGraphic(iv);
		this.rbMode1.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		Image iMode2 = new Image(this.getClass().getResourceAsStream(
												"/res/img/user,comp-comp.png"));
		iv = new ImageView(iMode2);
		iv.setEffect(ds);
		this.rbMode2.setGraphic(iv);
		this.rbMode2.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		Image iMode3 = new Image(this.getClass().getResourceAsStream(
													"/res/img/comp-comp.png"));
		iv = new ImageView(iMode3);
		iv.setEffect(ds);
		this.rbMode3.setGraphic(iv);
		this.rbMode3.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		Image iShot = new Image(this.getClass().getResourceAsStream(
													"/res/img/check.png"));
		this.btShot.setGraphic(new ImageView(iShot));
		this.btShot.setGraphicTextGap(10);

		Image iGenerate = new Image(this.getClass().getResourceAsStream(
													"/res/img/dice.png"));
		this.btGenerateQuad.setGraphic(new ImageView(iGenerate));

		Image iSetQuad = new Image(this.getClass().getResourceAsStream(
													"/res/img/touch.png"));
		this.btSetQuad.setGraphic(new ImageView(iSetQuad));

		Image iReset = new Image(this.getClass().getResourceAsStream(
													"/res/img/refresh.png"));
		this.btReset.setGraphic(new ImageView(iReset));
	}

	// ������ ����� ��� ��������������� ���� � ������ ����� ����� ��� ���
    ArrayList<Label> aAidDigits = new ArrayList<Label>();
    ArrayList<Integer> aAidDigitsColor = new ArrayList<Integer>();
    // ���������� �������� ����� � �����, ��������� ����������� ����� �����
    private void setAidDigitsMap(){
    	for(int i = 0; i < hbBottom.getChildren().size(); i++){
    		if(hbBottom.getChildren().get(i).getClass() == Label.class){
    			final Label l = (Label)hbBottom.getChildren().get(i);
    			l.setText(Integer.toString(i));
    			aAidDigits.add(l);
    			aAidDigitsColor.add(1);
    			l.setOnMouseClicked(new EventHandler<MouseEvent>() {
    			    @Override public void handle(MouseEvent e) {
    			        onAidDigitClick(e);
    			    }
    			});
    		}
    	}
    }

    // ��������� ����� ����� �� ����� - ����������� ��������� ���� ������
    protected void onAidDigitClick(MouseEvent e) {
    	Label l = (Label)e.getSource();
    	int num = this.aAidDigits.indexOf(l);
    	int colorNum = this.aAidDigitsColor.get(num);
    	if(e.getButton() == MouseButton.PRIMARY){
    		colorNum++;
    	}
    	if(e.getButton() == MouseButton.SECONDARY){
    		colorNum--;
    	}
    	if(e.getButton() == MouseButton.MIDDLE){
    		colorNum = 1;
    	}
    	
    	if(colorNum > 3){
    		colorNum = 1;
    	}
    	if(colorNum < 1){
    		colorNum = 3;
    	}
    	this.aAidDigitsColor.set(num, colorNum);
    	switch(colorNum){
    	case 2:
    		l.setStyle("-fx-text-fill: #FF0000;");
    		break;
    	case 3:
    		l.setStyle("-fx-text-fill: #00AFFF;");
    		break;
    	default:
    		l.setStyle("-fx-text-fill: #000000;");
    	}
    }

    // ���������� ������ ������ ����� ����� ��������������� ���� �� ������
    @FXML protected void onAidDigitsReset(ActionEvent e) {
    	for(int i = 0; i < this.aAidDigits.size(); i++){
    		aAidDigits.get(i).setStyle("-fx-text-fill: #000000;");
    	}
    }
    // ================ ����� ������������� ���������� =========================

	// ����� ������ ������ � ScrollPane - ���������� � ���� ����
    void ShowStepInfo(String s, boolean Player1, int img) {
    	HBox hb = new HBox();
    	hb.setAlignment(Pos.CENTER);
    	Text t = new Text(s);
    	t.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
    	hb.getChildren().add(t);
		//hb.setStyle("-fx-background-color: #33FFFF;");
		hb.setStyle("-fx-background-color: #CCFF99;");

		// ��� ���� � �������� (����� 0) ����� �������� ������ �������������
		//   ����� � ����������� �� ����� �������
		Image iFlag = null;
		switch(img){
		case 1:
			iFlag = new Image(this.getClass().getResourceAsStream(
											"/res/img/flag green small.png"));
			break;
		case 2:
			iFlag = new Image(this.getClass().getResourceAsStream(
											"/res/img/flag yellow small.png"));
			break;
		case 3:
			iFlag = new Image(this.getClass().getResourceAsStream(
											"/res/img/flag red small.png"));
			break;
		}
		if (iFlag != null){
			hb.getChildren().add(0, new ImageView(iFlag));
			hb.setSpacing(20);
		}

		if (Player1) {
			vbPlayer1.getChildren().add(hb);
		} else {
			vbPlayer2.getChildren().add(hb);
		}
    }

	DigitCurator curator = new DigitCurator();	// ����� �������� �� �����
	Solver solver = new Solver();				// �����-�����
    Integer[] Digits = new Integer[4];			// �����, �������� �������������
    int bulls;
    int cows;
    int trying;

    // ����� ����������� �������
    void ShowNextShot(int trying, boolean Player1, int img) {
		String s = new String(Arrays.toString(Digits));
		s = Integer.toString(trying) + ": " + s;
		s = s + " -   " + Integer.toString(bulls) + " �, " +
		    Integer.toString(cows) + " �";
		ShowStepInfo(s, Player1, img);
    }

    // ========= �������� ������� ======
    // ������ -> ������, ����� 3
    void SelfAnswer() {
    	//curator.Init();
    	curator.DigitMixer();		// ���������� ����� ����������� ����� ����
    	solver.Init(curator.getDecade());

    	while(bulls + cows < 4) {		// ���� �� ������� ���� ����
    		Digits = solver.ToFindDigits(Digits);
    		Integer[] TmpBufI = new Integer[4];
    		for(int i = 0; i < 4; i++) {TmpBufI[i] = Digits[i];}
    		ShotData shot_data = curator.checkQuad(Digits, 2);
    		bulls = shot_data.getBulls();
    		cows = shot_data.getCows();
    		solver.shots_data.add(shot_data);
    		ShowNextShot(solver.shots_data.size(), false, 0);	// �����������
    		solver.ShotDigitIndex = 0;		// ��������� ��������
    		solver.DigitsForAnswerIndex = 0;
    	}
    	while(bulls < 4) {
    		solver.ToFindBulls(Digits);
    		ShotData shot_data = curator.checkQuad(Digits, 2);
    		bulls = shot_data.getBulls();
    		cows = shot_data.getCows();
    		solver.shots_data.add(shot_data);
    		ShowNextShot(solver.shots_data.size(), false, 0);	// �����������
    	}
    	solver.shots_data.clear();
    	bulls = 0;
    	cows = 0;
    }

    // ������� -> ������
    void ShotMode0(){
    	if (!this.isEqualDigits()){
    		Player1ShotNum++;
    		ShotData shot_data = curator.checkQuad(this.DigitsForShow, 1);
    		String s = new String(Arrays.toString(DigitsForShow));
    		s = Integer.toString(Player1ShotNum) + ": " + s;
    		s = s + " -   " + Integer.toString(shot_data.getBulls()) + " �, " +
    		    Integer.toString(shot_data.getCows()) + " �";
    		int img = 0;
    		if (Player1ShotNum > 0){
    			img = 1;
    		}
    		if (Player1ShotNum > 7){
    			img = 2;
    		}
    		if (Player1ShotNum > 14){
    			img = 3;
    		}
    		ShowStepInfo(s, true, img);
    		if (shot_data.getBulls() == 4){
    			this.doEndOfGame(0);
    		}
    	}
    }

    // ������� <-> ������
    void ShotMode1(){
    	if (!this.isEqualDigits()){
    		Player1ShotNum++;
    		ShotData shot_data = curator.checkQuad(this.DigitsForShow, 1);
    		String s = new String(Arrays.toString(DigitsForShow));
    		s = Integer.toString(Player1ShotNum) + ": " + s;
    		s = s + " -   " + Integer.toString(shot_data.getBulls()) + " �, " +
    		    Integer.toString(shot_data.getCows()) + " �";
    		ShowStepInfo(s, true, 0);
    		if (shot_data.getBulls() == 4){
    			this.doEndOfGame(1);
    		}

    		Digits = solver.ToFindDigits(Digits);
    		Integer[] TmpBufI = new Integer[4];
    		for(int i = 0; i < 4; i++) {TmpBufI[i] = Digits[i];}
    		ShotData shot_data2 = curator.checkQuad(Digits, 2);
    		bulls = shot_data2.getBulls();
    		cows = shot_data2.getCows();
    		solver.shots_data.add(shot_data2);
    		ShowNextShot(solver.shots_data.size(), false, 0);	// �����������
    		solver.ShotDigitIndex = 0;		// ��������� ��������
    		solver.DigitsForAnswerIndex = 0;
    		if (bulls == 4){
    			this.doEndOfGame(2);
    		}
    	}
    }

    // �������, ������ -> ������
    void ShotMode2(){
    	if (!this.isEqualDigits()){
    		Player1ShotNum++;
    		ShotData shot_data = curator.checkQuad(this.DigitsForShow, 1);
    		String s = new String(Arrays.toString(DigitsForShow));
    		s = Integer.toString(Player1ShotNum) + ": " + s;
    		s = s + " -   " + Integer.toString(shot_data.getBulls()) + " �, " +
    		    Integer.toString(shot_data.getCows()) + " �";
    		ShowStepInfo(s, true, 0);
    		if (shot_data.getBulls() == 4){
    			this.doEndOfGame(1);
    		}

    		Digits = solver.ToFindDigits(Digits);
    		Integer[] TmpBufI = new Integer[4];
    		for(int i = 0; i < 4; i++) {
    			TmpBufI[i] = Digits[i];
    		}
    		ShotData shot_data2 = curator.checkQuad(Digits, 1);
    		bulls = shot_data2.getBulls();
    		cows = shot_data2.getCows();
    		solver.shots_data.add(shot_data2);
    		//ShowNextShot(solver.shots_data.size(), false);	// �����������
    		String s2 = new String(Integer.toString(solver.shots_data.size()));
    		s2 = s2 + ": -   " + Integer.toString(bulls) + " �, " +
    		    Integer.toString(cows) + " �";
    		ShowStepInfo(s2, false, 0);
    		solver.ShotDigitIndex = 0;		// ��������� ��������
    		solver.DigitsForAnswerIndex = 0;
    		if (bulls == 4){
    			this.doEndOfGame(2);
    		}
    	}
    }

    // �������� ��� ��������� ���� - ����� ���-�� ������
    private void doEndOfGame(int player){
    	this.btShot.setDisable(true);
    	if (player != 0){
    		this.ShowStepInfo("������ ������ " + Integer.toString(player) + "!",
    																true, 0);
    		this.ShowStepInfo("������ ������ " + Integer.toString(player) + "!",
    																false, 0);
    	} else {
    		this.ShowStepInfo("������!", true, 0);
    	}
    	this.setDisableBt(false);
    }

    // ���������� ��������� ������ 2 ����������
    private void setXToPlayer2(){
		for(int i = 0; i < 4; i++){
			aQuad2.get(i).setText("X");
		}
    }

    // �������� ��������� ��������� ���� ���������
    //   � ������������ ����������� � �����
    void FullTestForAlgotithm(){
    	Integer[] TestQuad = {0, 0, 0, 0};
    	Integer[] TestDecade = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    	ArrayList<Integer> ShotNum = new ArrayList<Integer>();
    	Boolean isQuadReady = true;
    	for(int d = 0; d <= 9999; d++){
    		TestQuad[3] = d % 10;
    		TestQuad[2] = (d / 10) % 10;
    		TestQuad[1] = (d / 100) % 10;
    		TestQuad[0] = (d / 1000) % 10;
    		this.Reset();
    		this.curator.setQuad(TestQuad, 2);
    		this.solver.Init(TestDecade);

    		isQuadReady = true;
        	for(int i = 0; i < 3; i++) {
        		for(int j = i + 1; j < 4; j++) {
        			if (TestQuad[i] == TestQuad[j]) {
        				isQuadReady = false;
        			}
        		}
        	}
        	if(!isQuadReady){continue;}

    		for(int i = 0; i < 4; i++){
    			aQuad2.get(i).setText(TestQuad[i].toString());
    		}

        	while(bulls + cows < 4) {		// ���� �� ������� ���� ����
        		Digits = solver.ToFindDigits(Digits);
        		Integer[] TmpBufI = new Integer[4];
        		for(int i = 0; i < 4; i++) {TmpBufI[i] = Digits[i];}
        		ShotData shot_data = curator.checkQuad(Digits, 2);
        		bulls = shot_data.getBulls();
        		cows = shot_data.getCows();
        		solver.shots_data.add(shot_data);
        		ShowNextShot(solver.shots_data.size(), false, 0); // �����������
        		solver.ShotDigitIndex = 0;		// ��������� ��������
        		solver.DigitsForAnswerIndex = 0;
        	}
        	while(bulls < 4) {
        		solver.ToFindBulls(Digits);
        		ShotData shot_data = curator.checkQuad(Digits, 2);
        		bulls = shot_data.getBulls();
        		cows = shot_data.getCows();
        		solver.shots_data.add(shot_data);
        		ShowNextShot(solver.shots_data.size(), false, 0); // �����������
        	}

        	ShotNum.add(solver.shots_data.size());
        	solver.shots_data.clear();
        	bulls = 0;
        	cows = 0;
    	}

    	Double sum = 0.0;
    	int max = 0;
    	for(int i = 0; i < ShotNum.size(); i++){
    		sum = sum + ShotNum.get(i);
    		if(max < ShotNum.get(i)){
    			max = ShotNum.get(i);
    		}
    	}

    	ArrayList<Integer> NumShotNum = new ArrayList<Integer>();
    	for(int i = 0; i <= max; i++){
    		NumShotNum.add(0);
    	}

    	for(int i = 0; i < ShotNum.size(); i++){
    		NumShotNum.set(ShotNum.get(i), NumShotNum.get(ShotNum.get(i)) + 1);
    	}

    	this.ShowStepInfo("������� ���� ���������:", false, 0);
    	this.ShowStepInfo("����� - " + Integer.toString(ShotNum.size()), 
    																false, 0);
    	this.ShowStepInfo("�������� ������� - " + Integer.toString(max), 
    																false, 0);
    	this.ShowStepInfo("� ������� - " + Double.toString(sum 
    											/ ShotNum.size()), false, 0);
    	this.ShowStepInfo("", false, 0);
    	this.ShowStepInfo("[�������]: [���������]", false, 0);

    	for(int i = 1; i <= max; i++){
    		this.ShowStepInfo(Integer.toString(i) + ": " 
    						+ Integer.toString(NumShotNum.get(i)), false, 0);
    	}
    }

    // =========================================================================
    // �������� ��������� ��������� ���� ���������
    //   � ������������ ��������

    // ��� �����������
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    BarChart<String, Number> bc;
    XYChart.Series<String, Number> series1;

    // �������� �������� ��� ��������
	Integer[] TestQuad = {0, 0, 0, 0};
	// ���������� ����� ���� ��� �����������
	final Integer[] TestDecade = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	// ���� ���������� ������� �� ������� ��� �������� ����������
	ArrayList<Integer> alShotNum = new ArrayList<Integer>();
	// ���� ���������� ������� �� ������� ��� �����������
	Map<Integer, Integer> hmShotNum = new HashMap<Integer, Integer>();
	// ������� ��� �������� ���������
	int d = 0;
	// ����� ��� ������ �� AnimationTimer.handle

    public void AnimatedTestOfAlgorithm(){
    	// ���� ������� ����������� ��� �� ���������� - �������
    	if(bc == null){
    		bc = new BarChart<String, Number>(xAxis, yAxis);
    		series1 = new XYChart.Series<String, Number>();
    		bc.getData().add(series1);
    		bc.setMaxHeight(200);
    		bc.setPadding(new Insets(0,0,0,0));
    		bc.setLegendVisible(false);
    		bc.setBarGap(0);
    		bc.setCategoryGap(0);
    		bc.setAnimated(false);
    		vbRight.getChildren().add(bc);
    	} else {	// ����� - �������� �� ������ � �������� ������
    		if(!vbRight.getChildren().contains(bc)){
    			vbRight.getChildren().add(bc);
    			this.series1.getData().clear();
    		}
    	}

    	// �������� �������� �� �������� � ������� ������������� ����
    	Boolean isQuadReady = true;
		do{
			isQuadReady = true;
			d++;
			TestQuad[3] = d % 10;
			TestQuad[2] = (d / 10) % 10;
			TestQuad[1] = (d / 100) % 10;
			TestQuad[0] = (d / 1000) % 10;
    		for(int i = 0; i < 3; i++) {
    			for(int j = i + 1; j < 4; j++) {
    				if (TestQuad[i] == TestQuad[j]) {
    					isQuadReady = false;
    				}
    			}
    		}
		}while(!isQuadReady && (d <= 9999));

		if(isQuadReady){	// ������� ��������� ��� ����� (d > 9870)
			// ����� �����������, ���������� � �������
			this.Reset();
			this.curator.setQuad(TestQuad, 2);
			this.solver.Init(TestDecade);
			// ����������� �������������� ��������
			for(int i = 0; i < 4; i++){
				aQuad2.get(i).setText(TestQuad[i].toString());
			}

			// �������
			while(bulls + cows < 4) {		// ���� �� ������� ���� ����
				Digits = solver.ToFindDigits(Digits);
				Integer[] TmpBufI = new Integer[4];
				for(int i = 0; i < 4; i++) {TmpBufI[i] = Digits[i];}
				ShotData shot_data = curator.checkQuad(Digits, 2);
				bulls = shot_data.getBulls();
				cows = shot_data.getCows();
				solver.shots_data.add(shot_data);
				ShowNextShot(solver.shots_data.size(), false, 0); // �����������
				solver.ShotDigitIndex = 0;		// ��������� ��������
				solver.DigitsForAnswerIndex = 0;
			}
			while(bulls < 4) {
				solver.ToFindBulls(Digits);
				ShotData shot_data = curator.checkQuad(Digits, 2);
				bulls = shot_data.getBulls();
				cows = shot_data.getCows();
				solver.shots_data.add(shot_data);
				ShowNextShot(solver.shots_data.size(), false, 0); // �����������
			}

			// ���������� ��������� ��� ��������������� �����
			int size = solver.shots_data.size();
			alShotNum.add(size);
			if(hmShotNum.containsKey((Integer)size)){
				hmShotNum.put(size, hmShotNum.get(size) + 1);
			} else {
				hmShotNum.put(size, 1);
			}
			// ��������� ��� ���������� �������
			solver.shots_data.clear();
			bulls = 0;
			cows = 0;

			// ������ � ������������
			Boolean isData = false;
        	if(bc != null){
        		// �������� ������������� �������� ��� �������� hmShotNum
        		for(int i = 0; i < hmShotNum.keySet().size(); i++){
        			isData = false;
        			Integer key = (Integer)hmShotNum.keySet().toArray()[i];
        			for(int j = 0; j < bc.getData().get(0).getData().size(); j++){
        				// ���� ���� - �������� ������ ��������
        				if(bc.getData().get(0).getData().get(j).getXValue().equals(Integer.toString(key))){
        					bc.getData().get(0).getData().get(j).setYValue((Integer)hmShotNum.get(key));
        					isData = true;
        					break;
        				}
        			}
        			// ���� ���� - �������� ����� � ��������� ������ 
        			//    � ������������� ����
        			//   (���� ������ ��������� � ������ ������� -
        			//    ����� ���� ��������������� ����������� - ��� JavaFX)
        			if(!isData){
        				this.series1.getData().clear();
        				int size2 = hmShotNum.keySet().size();
        				Integer[] buf = new Integer[size2];
        				for(int k = 0; k < size2; k++){
        					buf[k] = (Integer)hmShotNum.keySet().toArray()[k];
        				}
        				Arrays.sort(buf);

        				// ���� ������� ���������� �� � 1 -
        				//   ��������� ���������� �������� ���������
        				if(buf[0] > 1){
        					for(int n = 1; n < buf[0]; n++){
        						hmShotNum.put(n, 0);
        					}
        				}

        				// ���� ����� ������� �� ����� ����� ����������� -
        				//   ��������� ���������� �������� ���������
        				if((size2 > 2) && (buf[size2 - 1] - buf[size2 - 2] > 1)){
        					for(int n = buf[size2 - 2]; n < buf[size2 - 1]; n++){
        						hmShotNum.put(n, 0);
        					}
        				}

        				// ���� ����� ������� �� ����� ����� ���������� -
        				//   ��������� ���������� �������� ���������
        				if((size2 > 1) && (buf[1] - buf[0] > 1)){
        					for(int n = buf[0]; n < buf[1]; n++){
        						hmShotNum.put(n, 0);
        					}
        				}

        				// ������ ��������� ������������� ������ ������
        				int size3 = hmShotNum.keySet().size();
        				Integer[] buf2 = new Integer[size3];
        				for(int k = 0; k < size3; k++){
        					buf2[k] = (Integer)hmShotNum.keySet().toArray()[k];
        				}
        				Arrays.sort(buf2);

        				// ���������� �����������
                		for(int k = 0; k < size3; k++){
                			Integer key2 = buf2[k];
                			this.series1.getData().add(new Data<String, 
                							Number>(Integer.toString(key2), 
                							(Integer)hmShotNum.get(key2)));
                		}
        			}

        		}
        	}
		}

		// ������� ��������� ��������
    	if(d >= 9999){
    		// ��������� AnimationTimer, ����� �����������
    		this.at.stop();
    		this.Reset();
    		// ������� ����������
    		Double sum = 0.0;
    		int max = 0;
    		for(int i = 0; i < alShotNum.size(); i++){
    			sum = sum + alShotNum.get(i);
    			if(max < alShotNum.get(i)){
    				max = alShotNum.get(i);
    			}
    		}

    		// ����������� �����������
    		this.ShowStepInfo("������� ���� ���������:", false, 0);
    		this.ShowStepInfo("����� - " + Integer.toString(alShotNum.size()), 
    																false, 0);
    		this.ShowStepInfo("�������� ������� - " 
    										+ Integer.toString(max), false, 0);
    		this.ShowStepInfo("� ������� - " + Double.toString(sum 
    											/ alShotNum.size()), false, 0);
    		this.ShowStepInfo("", false, 0);
    		this.ShowStepInfo("[�������]: [���������]", false, 0);
    		for(int i = 1; i <= max; i++){
    			this.ShowStepInfo(Integer.toString(i) + ": " 
    							+ Integer.toString(hmShotNum.get(i)), false, 0);
    		}
    		// ��������� ������ ��� ������ ������
    		alShotNum.clear();
    		hmShotNum.clear();
    		d = 0;
    		// ����������� ����������� �� ������ ������ 
    		//   (����� ��������� �� Reset)
        	if(bc != null){
        		vbPlayer1.getChildren().add(bc);
        	}
    	}
    }

    // ��������� AnimationTimer ��� �������� �������� �������� ���������
    AnimationTimer at = new AnimationTimer(){
        @Override
        public void handle(long now) {
        	AnimatedTestOfAlgorithm();
        }
    };
    // =========================================================================



}
