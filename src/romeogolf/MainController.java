package romeogolf;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class MainController implements Initializable{
	// знакоместа пользовательских цифр
	@FXML private Label charsell_1_1;
	@FXML private Label charsell_1_2;
	@FXML private Label charsell_1_3;
	@FXML private Label charsell_1_4;
	// знакоместа цифр 2 игрока
	@FXML private Label charsell_2_1;
	@FXML private Label charsell_2_2;
	@FXML private Label charsell_2_3;
	@FXML private Label charsell_2_4;
	// поле отображения загаданной последовательности
	@FXML private TextField indicator;
	// кнопки для изменения цифр
	@FXML private Button btUp_1;
	@FXML private Button btDown_1;
	@FXML private Button btUp_2;
	@FXML private Button btDown_2;
	@FXML private Button btUp_3;
	@FXML private Button btDown_3;
	@FXML private Button btUp_4;
	@FXML private Button btDown_4;
	// поля для отображения ходов игроков 1 и 2
	@FXML private VBox vbPlayer1;
	@FXML private ScrollPane spPlayer1;
	@FXML private VBox vbPlayer2;
	@FXML private ScrollPane spPlayer2;
	@FXML private VBox vbRight;
	// переключение режима
	@FXML private ToggleGroup tgMode;
	// кнопки управления
	@FXML private Button btSetQuad;
	@FXML private Button btGenerateQuad;
	@FXML private Button btReset;
	@FXML private Button btTest;
	@FXML private Button btShot;
	@FXML private Button btSettings;
	// переключатели режима
	@FXML private RadioButton rbMode0;
	@FXML private RadioButton rbMode1;
	@FXML private RadioButton rbMode2;
	@FXML private RadioButton rbMode3;
	// нижняя панель
	@FXML private HBox hbBottom;
	// Метки имен полей с результатами попыток
	@FXML private Label lPlayer1;
	@FXML private Label lPlayer2;
	// панель для вывода информации всякого рода
	@FXML private Pane pInfo;

	// количество попыток первого игрока (человека)
	private Integer player1ShotNum = 0;

	// право первого хода
	private Boolean firstStepPlayer1 = true;
	private Boolean isFirstPlayer1(){
		return firstStepPlayer1;
	}

	private Boolean setFirstPlayer(){
		Boolean result;
		Integer firstStep = sdm.getFirstStep();
		switch(firstStep){
		case 1:	// первым - второй игрок
			firstStepPlayer1 = false;
			result = false;
			break;
		case 2:	// первый ход по очереди
			firstStepPlayer1 = !firstStepPlayer1;
			result = firstStepPlayer1;
			break;
		case 3:	// первый ход случайно
			firstStepPlayer1 = curator.getRandomBool();
			result = firstStepPlayer1;
			break;
		default:	// первым - первый игрок (в т. ч. в непонятных ситуациях)
			firstStepPlayer1 = true;
			result = true;
		}
		this.player2firstStep = true;

		return result;
	}

	private void drawFirstStep(Boolean toDraw){
		if(toDraw){
			Image iWhite = new Image(this.getClass().getResourceAsStream(
					"/res/img/castle_white.png"));
			Image iBlack = new Image(this.getClass().getResourceAsStream(
					"/res/img/castle_black.png"));
			if(this.isFirstPlayer1()){
				this.lPlayer1.setGraphic(new ImageView(iWhite));
				this.lPlayer2.setGraphic(new ImageView(iBlack));
				this.btShot.setText("Попытка");
			} else {
				this.lPlayer2.setGraphic(new ImageView(iWhite));
				this.lPlayer1.setGraphic(new ImageView(iBlack));
				this.btShot.setText("Старт");
			}
		} else {
			this.lPlayer1.setGraphic(null);
			this.lPlayer2.setGraphic(null);
			this.btShot.setText("Попытка");
		}
	}

	// первый ход игрока 2 при его праве первого хода
	private Boolean player2firstStep = true;

	// режим игры
	private Integer mode = 0;
	void setMode(Integer m){
		this.mode = m;
		this.reset();
		/*
		0 :	// режим "человек угадывает"
		1 :	// режим "человек и машина угадывают друг у друга"
		2 :	// режим "человек и машина угадвают одно наперегонки"
		3 :	// режим "машина угадывает (тестовый)"
		 */
		if (m == 0){
			curator.generateQuad(1);
		} else {
			generateQwads();
		}
		// если нет загадки для машины
		this.setDisableBt(false);
		if ((m == 0) || (m == 2)){
			this.setXToPlayer2();
		}
		// если играет человек с машиной - отобразить право хода
		this.btShot.setText("Попытка");
		this.drawFirstStep((m == 1) || (m == 2));
	}

	Integer getMode(){
		return this.mode;
	}

	// Цифры, вводимые пользователем
    Integer[] digitsForShow = new Integer[4];
    // массив знакомест для пользовательских цифр
    ArrayList<Label> aQuad1 = new ArrayList<Label>();
    // массив знакомест для загаданных цифр
    ArrayList<Label> aQuad2 = new ArrayList<Label>();
    // массив радиокнопок - переключатель режима
    ArrayList<RadioButton> aRbMode = new ArrayList<RadioButton>();
    // карта соответствия кнопок цифрам	
    public Map<Button, Integer> df = new HashMap<Button, Integer>();
    // множество кнопок увеличения
    public Set<Button> sUp = new HashSet<Button>();;

    // тестовая кнопка - тестовый обработчик
    @FXML protected void onTest(ActionEvent event) {
    	//this.FullTestForAlgotithm();
    	if(!this.isTestRun){
    		this.at.start();
    		isTestRun = true;
    	}
    };

    // общий обработчик для всех кнопок Up & Down
    @FXML protected void onUpDown(ActionEvent e) {
    		// получение номера цифры, чью кнопку нажали
    		int Num = df.get(e.getSource());
    		if (sUp.contains(e.getSource())){	// изменение цифры
    			digitsForShow[Num]++;
    		} else {
    			digitsForShow[Num]--;
    		}
    		// проверка выхода цифры за пределы
    		if (digitsForShow[Num] < 0){
    			digitsForShow[Num] = 9;
    		}
    			// и соответствующее изменение
    		if (digitsForShow[Num] > 9){
    			digitsForShow[Num] = 0;
    		}
    		aQuad1.get(Num).setText(Integer.toString(digitsForShow[Num]));
    		isEqualDigits();
    };

    // Обработчик кнопки "попытка"
    @FXML protected void onShot(ActionEvent e) {
    	this.setDisableBt(true);
		switch(mode){
		case 0 :	// режим "человек угадывает"
			this.shotMode0();
			break;
		case 1 :	// режим "человек и машина угадывают друг у друга"
			this.shotMode1();
			break;
		case 2 :	// режим "человек и машина угадвают одно наперегонки"
			this.shotMode2();
			break;
		case 3 :	// режим "машина угадывает (тестовый)"
			this.reset();
			shotMode3();
			break;
		default :	// режим "ХЗ"
			indicator.setText("режим ХЗ");
		}
    }

    // обработчик кнопки установки четверки игроком 1 для игрока 2
    @FXML protected void onSetQwad(ActionEvent e) {
    	this.reset();
    	if (!this.isEqualDigits()){
    		curator.setQuad(this.digitsForShow, 2);
    		solver.Init(curator.getDecade());
    		for(int i = 0; i < 4; i++){
    			aQuad2.get(i).setText(curator.getQuad(2)[i].toString());
    		}
    	}
    	this.btShot.setDisable(false);
    }

    // обработчик кнопки генерации четверки  для обоих игроков
    @FXML protected void onGenerateQwad(ActionEvent e) {
    	this.reset();
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

    // сброс отображения предыдущей игры и подготовка к следующей
    void reset(Boolean clearPInfo){
    	vbPlayer1.getChildren().removeAll(vbPlayer1.getChildren());
    	vbPlayer2.getChildren().removeAll(vbPlayer2.getChildren());
    	this.generateQwads();
    	player1ShotNum = 0;
    	bulls = 0;
    	cows = 0;
		this.setDisableBt(false);
		this.btShot.setDisable(false);
		if(this.sdm.getDigitsReset()){
			this.digitsReset();
		}
		this.drawFirstStep((getMode() == 1) || (getMode() == 2));

		if(clearPInfo){
			pInfo.getChildren().removeAll(pInfo.getChildren());
			pInfo.getChildren().add(this.aStatBoxes.get(this.getMode()));
		}
    }

    void reset(){
    	reset(true);
    }

    // установка доступности кнопок в зависимости от режима
    private void setDisableBt(Boolean disable){
    	// доступность кнопок "загадать" и "сгенерить"
    	this.btGenerateQuad.setDisable(disable);
    	if (!disable){
    		this.btSetQuad.setDisable((this.mode == 0) || (this.mode == 2));
    	} else {
    		this.btSetQuad.setDisable(true);
    	}
    }

    // кнопка "сброс"
    @FXML protected void onReset(ActionEvent e) {
    	this.at.stop();
    	this.isTestRun = false;
    	this.reset();
    }

    // кнопка "Настройка"
    @FXML protected void onSettings(ActionEvent e) throws IOException {
    	Stage stage2 = new Stage();
		FXMLLoader loader2 = 
					new FXMLLoader(getClass().getResource("bc_settings.fxml"));
		Parent root2 = null;
		try {
			root2 = (Parent) loader2.load();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SettingsController controller2 = 
									(SettingsController)loader2.getController();
		controller2.setStage_Listener(stage2);
		controller2.setSDM(sdm);

	    Scene scene2 = new Scene(root2);
	    stage2.setTitle("Настройки");
	    stage2.setScene(scene2);
	    stage2.initOwner(this.stage);
	    stage2.initModality(Modality.APPLICATION_MODAL);
	    stage2.initStyle(StageStyle.UTILITY);
	    stage2.show();
    }

    // обработка переключения режима
    protected void onModeToggle(){
		if (tgMode.getSelectedToggle() != null) {
			setMode(this.aRbMode.indexOf(
								(RadioButton)this.tgMode.getSelectedToggle()));
		} else {
			setMode(0);
		}
    }

    // проверка цифр на совпадение
    boolean isEqualDigits() {
    	for(int i = 0; i < 4; i++) {
    		aQuad1.get(i).setStyle("-fx-text-fill: #000000;");
    	}
    	Boolean result = false;
    	for(int i = 0; i < 3; i++) {
    		for(int j = i + 1; j < 4; j++) {
    			if (digitsForShow[i] == digitsForShow[j]) {
    				aQuad1.get(i).setStyle("-fx-text-fill: #FF0000;");
    				aQuad1.get(j).setStyle("-fx-text-fill: #FF0000;");
    				result = true;
    			}
    		}
    	}
    	return result;
    };

    // =================== инициализация интерфейса ============================
    // список панелей для отображения статистики прошлых игр
    ArrayList<VBox> aStatBoxes = new ArrayList<VBox>();
    // список меток для отображения статистики
    ArrayList<Label> aTotalLabels = new ArrayList<Label>();
    ArrayList<Label> aWinLabels = new ArrayList<Label>();
    ArrayList<Label> aTieLabels = new ArrayList<Label>();
    // создание панелей статистики, заполнение списков панелей и меток
    private void buildBox(Integer index, String tl1, String tl2, String tl3){
		DropShadow ds = new DropShadow();
		ds.setOffsetX(5.0);
		ds.setOffsetY(5.0);
		ds.setColor(Color.GRAY);
		ds.setWidth(5.0);

    	VBox vb = new VBox();
    	vb.setMinHeight(this.pInfo.getPrefHeight());
    	vb.setMaxHeight(this.pInfo.getPrefHeight());
    	vb.setMinWidth(this.pInfo.getPrefWidth());
    	vb.setMaxWidth(this.pInfo.getPrefWidth());
    	vb.setAlignment(Pos.CENTER);
    	vb.setSpacing(10);
    	aStatBoxes.add(vb);
    	HBox hb1 = new HBox();
    	hb1.setSpacing(10);
    	hb1.setAlignment(Pos.CENTER_LEFT);
    	hb1.setPadding(new Insets(0, 0, 0, 30));
    	Label l1 = new Label(tl1);
    	l1.getStyleClass().add("l_statistic_1");
    	l1.setEffect(ds);
    	Label l2 = new Label("0");
    	l2.getStyleClass().add("l_statistic_2");
    	l2.setEffect(ds);
    	hb1.getChildren().addAll(l1, l2);
    	vb.getChildren().add(hb1);
    	aTotalLabels.add(l2);
    	hb1 = new HBox();
    	hb1.setSpacing(10);
    	hb1.setAlignment(Pos.CENTER_LEFT);
    	hb1.setPadding(new Insets(0, 0, 0, 30));
    	l1 = new Label(tl2);
    	l1.setEffect(ds);
    	l1.getStyleClass().add("l_statistic_1");
    	l2 = new Label("0");
    	l2.getStyleClass().add("l_statistic_2");
    	l2.setEffect(ds);
    	hb1.getChildren().addAll(l1, l2);
    	vb.getChildren().add(hb1);
    	aWinLabels.add(l2);
    	hb1 = new HBox();
    	hb1.setSpacing(10);
    	hb1.setAlignment(Pos.CENTER_LEFT);
    	hb1.setPadding(new Insets(0, 0, 0, 30));
    	l1 = new Label(tl3);
    	l1.getStyleClass().add("l_statistic_1");
    	l1.setEffect(ds);
    	l2 = new Label("0");
    	l2.getStyleClass().add("l_statistic_2");
    	l2.setEffect(ds);
    	vb.getChildren().add(hb1);
    	hb1.getChildren().addAll(l1, l2);
    	aTieLabels.add(l2);
    }

    private void buildStatBoxes(){
    	// для режима 0 (человек угадывает)
    	buildBox(0, "Сыграно - ", "Мин. попыток - ", "Макс. попыток - ");
    	// для режима 1 (человек <-> машина)
    	buildBox(1, "Сыграно - ", "Счет - ", "Ничьих - ");
    	// для режима 2 (человек, машина -> машина)
    	buildBox(2, "Сыграно - ", "Счет - ", "Ничьих - ");

    	// для режима 3 (машина -> машина)
    	VBox vb = new VBox();
    	vb.setMinHeight(this.pInfo.getPrefHeight());
    	vb.setMaxHeight(this.pInfo.getPrefHeight());
    	vb.setMinWidth(this.pInfo.getPrefWidth());
    	vb.setMaxWidth(this.pInfo.getPrefWidth());
    	vb.setAlignment(Pos.CENTER);
    	vb.setSpacing(10);
    	aStatBoxes.add(vb);
    }

    // объект для управления данными, сохраняемыми в память (настройки, etc)
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

    private void drawStatLabels(){
   		this.aTotalLabels.get(0).setText(Integer.toString(sdm.getMode0Total()));
   		Integer total = sdm.getMode1Player1Win() + sdm.getMode1Player2Win() +
   				sdm.getMode1Tie();
   		this.aTotalLabels.get(1).setText(Integer.toString(total));
   		total = sdm.getMode2Player1Win() + sdm.getMode2Player2Win() +
   				sdm.getMode2Tie();
   		this.aTotalLabels.get(2).setText(Integer.toString(total));

   		this.aWinLabels.get(0).setText(Integer.toString(sdm.getMode0Min()));
   		StringBuffer sb = 
   				new StringBuffer(Integer.toString(sdm.getMode1Player1Win()));
   		sb.append(" : ");
   		sb.append(Integer.toString(sdm.getMode1Player2Win()));
   		this.aWinLabels.get(1).setText(sb.toString());
   		sb = new StringBuffer(Integer.toString(sdm.getMode2Player1Win()));
   		sb.append(" : ");
   		sb.append(Integer.toString(sdm.getMode2Player2Win()));
   		this.aWinLabels.get(2).setText(sb.toString());

   		this.aTieLabels.get(0).setText(Integer.toString(sdm.getMode0Max()));
   		this.aTieLabels.get(1).setText(Integer.toString(sdm.getMode1Tie()));
   		this.aTieLabels.get(2).setText(Integer.toString(sdm.getMode2Tie()));
    }

    // восстановление параметров игры из файла настроек
    private void readPrefs(){
    	this.drawStatLabels();

    	// чтение режима
    	Integer bufInt = sdm.getMode();
   		if((bufInt < 0) || (bufInt > 3)){
   			bufInt = 0;
   		}
   		this.setMode(0);
   		if(this.aRbMode.indexOf((RadioButton)this.tgMode.getSelectedToggle()) 
   																	!= bufInt){
   			this.tgMode.selectToggle(aRbMode.get(bufInt));
   		}
    }

    // восстановление параметров окна из файла настроек
    private void readStagePrefs(){
    	if(sdm.isPosition()){
    		stage.setY(sdm.getTop());
    		stage.setX(sdm.getLeft());
    		stage.setHeight(sdm.getHeight());
    	}
    }

    // получение ссылки на окно, установка обработчика событий окна 
    //  и чтение параметров окна
    public void setStage_Listener(final Stage stage){
    	this.stage = stage;
    	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    		@Override
    		public void handle(WindowEvent event) {
    			 //event.consume();
    			// сохранение настроек перед закрытием окна
    			storePrefs();
    		}
    	});
    	// чтение сохраненных параметров окна после получения
    	//  ссылки на окно
    	this.readStagePrefs();
    }

    // соответствие панели VBox своему контейнеру ScrollPane
    Map<VBox, ScrollPane> mPlayerPanes = new HashMap<VBox, ScrollPane>();

    // общий обработчик увеличения для обоих VBox Player[X]
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

    private void digitsReset(){
    	// заполнение массива цифр и его отображение
    	for(int i = 0; i < 4; i++){
    		this.digitsForShow[i] = i + 1;
    		aQuad1.get(i).setText(Integer.toString(this.digitsForShow[i]));
    	}
    }

    // собственно инициализация
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.indicator.setText(sdm.test_str);

		// загрузка изображений в элементы управления
		getImages();
		// добавление обработчика изменения размера панели на ScrollPane
		// для прокрутки до упора вниз
		mPlayerPanes.put(vbPlayer1, spPlayer1);
		mPlayerPanes.put(vbPlayer2, spPlayer2);
		setVBoxScroller(this.vbPlayer1);
		setVBoxScroller(this.vbPlayer2);

		setControlMaps();	// для групповой обработки кнопок
		this.digitsReset();

    	// установка обработчика переключения режима игры
        tgMode.selectedToggleProperty().addListener(
        										new ChangeListener<Toggle>(){
        	public void changed(ObservableValue<? extends Toggle> ov,
        			Toggle old_toggle, Toggle new_toggle) {
        		if(old_toggle != new_toggle){
        			onModeToggle();
        		}
        	}
        });

		this.buildStatBoxes();
        this.readPrefs();	// чтение сохраненных параметров игры
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

		DropShadow ds2 = new DropShadow();
		ds2.setOffsetX(4.0);
		ds2.setOffsetY(4.0);
		ds2.setColor(Color.GRAY);
		ds2.setWidth(5.0);
		this.lPlayer1.setEffect(ds2);
		this.lPlayer2.setEffect(ds2);
		for(int i = 0; i < 4; i++){
			this.aQuad1.get(i).setEffect(ds2);
		}
	}

	private void setControlMaps(){
		// заполнение карты соответствия кнопок их номерам
    	df.put(btUp_1, 0);
    	df.put(btDown_1, 0);
    	df.put(btUp_2, 1);
    	df.put(btDown_2, 1);
    	df.put(btUp_3, 2);
    	df.put(btDown_3, 2);
    	df.put(btUp_4, 3);
    	df.put(btDown_4, 3);
    	// множество кнопок "вверх"
    	sUp.add(btUp_1);
    	sUp.add(btUp_2);
    	sUp.add(btUp_3);
    	sUp.add(btUp_4);
    	// множество знакомест для цифр
    	aQuad1.add(charsell_1_1);
    	aQuad1.add(charsell_1_2);
    	aQuad1.add(charsell_1_3);
    	aQuad1.add(charsell_1_4);
    	// множество знакомест для загаданных цифр
    	aQuad2.add(charsell_2_1);
    	aQuad2.add(charsell_2_2);
    	aQuad2.add(charsell_2_3);
    	aQuad2.add(charsell_2_4);
    	// множество радиокнопок - переключатель режима
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

		Image iSettings = new Image(this.getClass().getResourceAsStream(
												"/res/img/configuration.png"));
		this.btSettings.setGraphic(new ImageView(iSettings));
	}

	// массив меток для вспомогательных цифр и массив кодов цвета для них
    ArrayList<Label> aAidDigits = new ArrayList<Label>();
    ArrayList<Integer> aAidDigitsColor = new ArrayList<Integer>();
    // заполнение массивов меток и цвета, установка обработчика клика меток
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

    // обработка клика мышью по метке - циклическое изменение трех цветов
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

    // обработчик кнопки сброса цвета меток вспомогательных цифр на черный
    @FXML protected void onAidDigitsReset(ActionEvent e) {
    	for(int i = 0; i < this.aAidDigits.size(); i++){
    		aAidDigitsColor.set(i, 1);
    		aAidDigits.get(i).setStyle("-fx-text-fill: #000000;");
    	}
    }
    // ================ конец инициализации интерфейса =========================

	// вывод строки текста в ScrollPane - информация о шаге игры
    void showStepInfo(String s, boolean Player1, int img) {
    	HBox hb = new HBox();
    	hb.setAlignment(Pos.CENTER_LEFT);
    	if(img == 0){
    		hb.setPadding(new Insets(0, 0, 0, 30));
    	}
    	Text t = new Text(s);
    	t.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
    	hb.getChildren().add(t);
		//hb.setStyle("-fx-background-color: #33FFFF;");
		hb.setStyle("-fx-background-color: #CCFF99;");

		// при игре в одиночку (режим 0) можно добавить флажок определенного
		//   цвета в зависимости от числа попыток
		Image iFlag = null;
		switch(img){
		case 1:
			iFlag = new Image(this.getClass().getResourceAsStream(
												"/res/img/ball2_green.png"));
			break;
		case 2:
			iFlag = new Image(this.getClass().getResourceAsStream(
											"/res/img/ball2_yellow.png"));
			break;
		case 3:
			iFlag = new Image(this.getClass().getResourceAsStream(
											"/res/img/ball2_red.png"));
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

	DigitCurator curator = new DigitCurator();	// класс слежения за игрой
	Solver solver = new Solver();				// класс-игрок
    Integer[] digits = new Integer[4];			// Цифры, вводимые пользователем
    int bulls;
    int cows;
    int trying;

    // вывод результатов попытки
    void showNextShot(int trying, boolean Player1, int img) {
		String s = new String(Arrays.toString(digits));
		s = Integer.toString(trying) + ": " + s;
		s = s + " -   " + Integer.toString(bulls) + " Б, " +
		    Integer.toString(cows) + " К";
		showStepInfo(s, Player1, img);
    }

    // ========= машинная отгадка ======
    // машина -> машина, режим 3
    void shotMode3() {
    	curator.digitMixer();		// перемешать цифры подготовить набор цифр
    	solver.Init(curator.getDecade());

    	while(bulls + cows < 4) {		// цикл до отгадки всех цифр
    		digits = solver.toFindDigits(digits);
    		Integer[] TmpBufI = new Integer[4];
    		for(int i = 0; i < 4; i++){
    			TmpBufI[i] = digits[i];
    		}
    		ShotData shot_data = curator.checkQuad(digits, 2);
    		bulls = shot_data.getBulls();
    		cows = shot_data.getCows();
    		solver.shots_data.add(shot_data);
    		showNextShot(solver.shots_data.size(), false, 0);	// отображение
    	}
    	while(bulls < 4) {
    		solver.toFindBulls(digits);
    		ShotData shot_data = curator.checkQuad(digits, 2);
    		bulls = shot_data.getBulls();
    		cows = shot_data.getCows();
    		solver.shots_data.add(shot_data);
    		showNextShot(solver.shots_data.size(), false, 0);	// отображение
    	}
    	solver.shots_data.clear();
    	bulls = 0;
    	cows = 0;
    }

    // человек -> машина
    void shotMode0(){
    	if (!this.isEqualDigits()){
    		player1ShotNum++;
    		ShotData shot_data = curator.checkQuad(this.digitsForShow, 1);
    		String s = new String(Arrays.toString(digitsForShow));
    		s = Integer.toString(player1ShotNum) + ": " + s;
    		s = s + " -   " + Integer.toString(shot_data.getBulls()) + " Б, " +
    		    Integer.toString(shot_data.getCows()) + " К";
    		int img = 0;
    		if (player1ShotNum > 0){
    			img = 1;
    		}
    		if (player1ShotNum > 7){
    			img = 2;
    		}
    		if (player1ShotNum > 14){
    			img = 3;
    		}
    		showStepInfo(s, true, img);
    		if (shot_data.getBulls() == 4){
    			this.doEndOfGame(0);
    		}
    	}
    }

    private Boolean tryPlayer1(){
    	Boolean result = false;
    	player1ShotNum++;
    	ShotData shot_data = curator.checkQuad(this.digitsForShow, 1);
    	String s = new String(Arrays.toString(digitsForShow));
    	s = Integer.toString(player1ShotNum) + ": " + s;
    	s = s + " -   " + Integer.toString(shot_data.getBulls()) + " Б, " +
    	    Integer.toString(shot_data.getCows()) + " К";
    	showStepInfo(s, true, 0);
    	if (shot_data.getBulls() == 4){
    		result = true;
    	}
    	return result;
    }

    private Boolean tryPlayer2(int QuadNum){
    	Boolean result = false;

    	digits = solver.toFindDigits(digits);
    	Integer[] TmpBufI = new Integer[4];
    	for(int i = 0; i < 4; i++){
    		TmpBufI[i] = digits[i];
    	}
    	ShotData shot_data2 = curator.checkQuad(digits, QuadNum);
    	bulls = shot_data2.getBulls();
    	cows = shot_data2.getCows();
    	solver.shots_data.add(shot_data2);

    	if(QuadNum == 2){
    		showNextShot(solver.shots_data.size(), false, 0);	// отображение
    	} else {
    		String s2 = new String(Integer.toString(solver.shots_data.size()));
    		s2 = s2 + ": -   " + Integer.toString(bulls) + " Б, " +
		    Integer.toString(cows) + " К";
    		showStepInfo(s2, false, 0);
    	}

    	if (bulls == 4){
    		result = true;
    	}
    	return result;
    }

    Boolean p1End = false;
    Boolean p2End = false;

    private Boolean whoWin(Boolean p1, Boolean p2){
    	Boolean result = false;
    	if(p1 && p2){
    		this.doEndOfGame(3);
    	} else if(p1) {
    		this.doEndOfGame(1);
    	} else if(p2) {
    		this.doEndOfGame(2);
    	}
    	if(p1 || p2){
    		this.setFirstPlayer();
    		result = true;
    		p1End = false;
    		p2End = false;
    	}
        return result;
    }

    // человек <-> машина
    void shotMode1(){
    	if(this.isFirstPlayer1()){
    		if (!this.isEqualDigits()){
    			p1End = this.tryPlayer1();
    			p2End = this.tryPlayer2(2);
    			this.whoWin(p1End, p2End);
    		}
    	} else {
    		if(this.player2firstStep){
    			player2firstStep = false;
    			this.btShot.setText("Попытка");
    			p2End = this.tryPlayer2(2);
    		} else {
    			if (!this.isEqualDigits()){
    				p1End = this.tryPlayer1();
    				if(!this.whoWin(p1End, p2End)){
    					p2End = this.tryPlayer2(2);
    				}
    			}
    		}
    	}
    }

    // человек, машина -> машина
    void shotMode2(){
    	if(this.isFirstPlayer1()){
    		if (!this.isEqualDigits()){
    			p1End = this.tryPlayer1();
    			p2End = this.tryPlayer2(1);
    			this.whoWin(p1End, p2End);
    		}
    	} else {
    		if(this.player2firstStep){
    			player2firstStep = false;
    			this.btShot.setText("Попытка");
    			p2End = this.tryPlayer2(1);
    		} else {
    			if (!this.isEqualDigits()){
    				p1End = this.tryPlayer1();
    				if(!this.whoWin(p1End, p2End)){
    					p2End = this.tryPlayer2(1);
    				}
    			}
    		}
    	}
    }

    // действия при окончании игры - когда кто-то угадал
    private void doEndOfGame(int player){
    	this.btShot.setDisable(true);
    	Integer wins;
    	switch(player){
    	case 0:
    		this.showStepInfo("Победа!", true, 0);
    		sdm.setMode0Total(sdm.getMode0Total() + 1);
    		Integer max = sdm.getMode0Max();
    		Integer min = sdm.getMode0Min();
    		if(this.player1ShotNum > max){
    			sdm.setMode0Max(player1ShotNum);
    		}
    		if((this.player1ShotNum < min) || (min <= 0)){
    			sdm.setMode0Min(player1ShotNum);
    		}
    		break;
    	case 1:
    		this.showStepInfo("Победа игрока 1!", true, 0);
    		this.showStepInfo("Победа игрока 1!", false, 0);
    		if(this.getMode() == 1){
    			wins = sdm.getMode1Player1Win();
    			sdm.setMode1Player1Win(wins + 1);
    		} else {
    			wins = sdm.getMode2Player1Win();
    			sdm.setMode2Player1Win(wins + 1);
    		}
    		break;
    	case 2:
    		this.showStepInfo("Победа игрока 2!", true, 0);
    		this.showStepInfo("Победа игрока 2!", false, 0);
    		if(this.getMode() == 1){
    			wins = sdm.getMode1Player2Win();
    			sdm.setMode1Player2Win(wins + 1);
    		} else {
    			wins = sdm.getMode2Player2Win();
    			sdm.setMode2Player2Win(wins + 1);
    		}
    		break;
    	case 3:
    		this.showStepInfo("Ничья!", true, 0);
    		this.showStepInfo("Ничья!", false, 0);
    		if(this.getMode() == 1){
    			wins = sdm.getMode1Tie();
    			sdm.setMode1Tie(wins++);
    		} else {
    			wins = sdm.getMode2Tie();
    			sdm.setMode2Tie(wins + 1);
    		}
    		break;
    	}
    	this.setDisableBt(false);
    	this.drawStatLabels();
    }

    // заполнение знакомест игрока 2 крестиками
    private void setXToPlayer2(){
		for(int i = 0; i < 4; i++){
			aQuad2.get(i).setText("X");
		}
    }

    // проверка алгоритма перебором всех вариантов
    //   с отображением результатов в конце
    void fullTestForAlgotithm(){
    	Integer[] TestQuad = {0, 0, 0, 0};
    	Integer[] TestDecade = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    	ArrayList<Integer> ShotNum = new ArrayList<Integer>();
    	Boolean isQuadReady = true;
    	for(int d = 0; d <= 9999; d++){
    		TestQuad[3] = d % 10;
    		TestQuad[2] = (d / 10) % 10;
    		TestQuad[1] = (d / 100) % 10;
    		TestQuad[0] = (d / 1000) % 10;
    		this.reset();
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
        	if(!isQuadReady){
        		continue;
        	}

    		for(int i = 0; i < 4; i++){
    			aQuad2.get(i).setText(TestQuad[i].toString());
    		}

        	while(bulls + cows < 4) {		// цикл до отгадки всех цифр
        		digits = solver.toFindDigits(digits);
        		Integer[] TmpBufI = new Integer[4];
        		for(int i = 0; i < 4; i++){
        			TmpBufI[i] = digits[i];
        		}
        		ShotData shot_data = curator.checkQuad(digits, 2);
        		bulls = shot_data.getBulls();
        		cows = shot_data.getCows();
        		solver.shots_data.add(shot_data);
        		showNextShot(solver.shots_data.size(), false, 0); // отображение
        	}
        	while(bulls < 4) {
        		solver.toFindBulls(digits);
        		ShotData shot_data = curator.checkQuad(digits, 2);
        		bulls = shot_data.getBulls();
        		cows = shot_data.getCows();
        		solver.shots_data.add(shot_data);
        		showNextShot(solver.shots_data.size(), false, 0); // отображение
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

    	this.showStepInfo("Перебор всех вариантов:", false, 0);
    	this.showStepInfo("всего - " + Integer.toString(ShotNum.size()), 
    																false, 0);
    	this.showStepInfo("Максимум попыток - " + Integer.toString(max), 
    																false, 0);
    	this.showStepInfo("В среднем - " + Double.toString(sum 
    											/ ShotNum.size()), false, 0);
    	this.showStepInfo("", false, 0);
    	this.showStepInfo("[Попыток]: [вариантов]", false, 0);

    	for(int i = 1; i <= max; i++){
    		this.showStepInfo(Integer.toString(i) + ": " 
    						+ Integer.toString(NumShotNum.get(i)), false, 0);
    	}
    }

    // =========================================================================
    // проверка алгоритма перебором всех вариантов
    //   с отображением процесса

    // для гистограммы
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    BarChart<String, Number> bc;
    XYChart.Series<String, Number> series1;

    // тестовая четверка для перебора
	Integer[] testQuad = {0, 0, 0, 0};
	// постоянный набор цифр для отгадывания
	final Integer[] testDecade = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	// учет количества попыток на вариант для подсчета статистики
	ArrayList<Integer> alShotNum = new ArrayList<Integer>();
	// учет количества попыток на вариант для гистограммы
	Map<Integer, Integer> hmShotNum = new HashMap<Integer, Integer>();
	// счетчик для перебора вариантов
	int d = 0;
	// тест запущен?
	private Boolean isTestRun = false;

	// метод для вызова из AnimationTimer.handle
    public void animatedTestOfAlgorithm(){
    	// если графика гистограммы еще не существует - создать
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
    		bc.setMaxHeight(pInfo.getHeight());
    		bc.setMaxWidth(pInfo.getWidth());
    		pInfo.getChildren().removeAll(pInfo.getChildren());
    		pInfo.getChildren().add(bc);
    	} else {	// иначе - добавить на панель и очистить данные
    		if(!pInfo.getChildren().contains(bc)){
    			pInfo.getChildren().removeAll(pInfo.getChildren());
    			pInfo.getChildren().add(bc);
    			this.series1.getData().clear();
    		}
    	}

    	// создание четверки из счетчика с отсевом повторяющихся цифр
    	Boolean isQuadReady = true;
		do{
			isQuadReady = true;
			d++;
			testQuad[3] = d % 10;
			testQuad[2] = (d / 10) % 10;
			testQuad[1] = (d / 100) % 10;
			testQuad[0] = (d / 1000) % 10;
    		for(int i = 0; i < 3; i++) {
    			for(int j = i + 1; j < 4; j++) {
    				if (testQuad[i] == testQuad[j]) {
    					isQuadReady = false;
    				}
    			}
    		}
		}while(!isQuadReady && (d <= 9999));

		if(isQuadReady){	// условие вставлено для чисел (d > 9870)
			// сброс отображения, подготовка к решению
			this.reset(false);
			this.curator.setQuad(testQuad, 2);
			this.solver.Init(testDecade);
			// отображение подготовленной четверки
			for(int i = 0; i < 4; i++){
				aQuad2.get(i).setText(testQuad[i].toString());
			}

			// решение
			while(bulls + cows < 4) {		// цикл до отгадки всех цифр
				digits = solver.toFindDigits(digits);
				Integer[] TmpBufI = new Integer[4];
				for(int i = 0; i < 4; i++){
					TmpBufI[i] = digits[i];
				}
				ShotData shot_data = curator.checkQuad(digits, 2);
				bulls = shot_data.getBulls();
				cows = shot_data.getCows();
				solver.shots_data.add(shot_data);
				showNextShot(solver.shots_data.size(), false, 0); // отображение
			}
			while(bulls < 4) {
				solver.toFindBulls(digits);
				ShotData shot_data = curator.checkQuad(digits, 2);
				bulls = shot_data.getBulls();
				cows = shot_data.getCows();
				solver.shots_data.add(shot_data);
				showNextShot(solver.shots_data.size(), false, 0); // отображение
			}

			// заполнение коллекций для статистического учета
			int size = solver.shots_data.size();
			alShotNum.add(size);
			if(hmShotNum.containsKey((Integer)size)){
				hmShotNum.put(size, hmShotNum.get(size) + 1);
			} else {
				hmShotNum.put(size, 1);
			}
			// подчистка для следующего решения
			solver.shots_data.clear();
			bulls = 0;
			cows = 0;

			// работа с гистограммой
			Boolean isData = false;
        	if(bc != null){
        		// проверка существования столбика для элемента hmShotNum
        		for(int i = 0; i < hmShotNum.keySet().size(); i++){
        			isData = false;
        			Integer key = (Integer)hmShotNum.keySet().toArray()[i];
        			for(int j = 0; 
        						j < bc.getData().get(0).getData().size(); j++){
        				// если есть - изменить данные столбика
        				String xv = 
        					bc.getData().get(0).getData().get(j).getXValue();
        				Integer yv = (Integer)hmShotNum.get(key);
        				if(xv.equals(Integer.toString(key))){
        					bc.getData().get(0).getData().get(j).setYValue(yv);
        					isData = true;
        					break;
        				}
        			}
        			// если нету - очистить серию и наполнить заново 
        			//    в сортированном виде
        			//   (если просто вставлять в нужную позицию -
        			//    может быть несортированное отображение - баг JavaFX)
        			if(!isData){
        				this.series1.getData().clear();
        				int size2 = hmShotNum.keySet().size();
        				Integer[] buf = new Integer[size2];
        				for(int k = 0; k < size2; k++){
        					buf[k] = (Integer)hmShotNum.keySet().toArray()[k];
        				}
        				Arrays.sort(buf);

        				// если столбцы начинаются не с 1 -
        				//   заполнить промежуток нулевыми столбцами
        				if(buf[0] > 1){
        					for(int n = 1; n < buf[0]; n++){
        						hmShotNum.put(n, 0);
        					}
        				}

        				// если новый столбец не сразу после предыдущего -
        				//   заполнить промежуток нулевыми столбцами
        				if((size2 > 2) && 
        								(buf[size2 - 1] - buf[size2 - 2] > 1)){
        					for(int n = buf[size2 - 2]; 
        											n < buf[size2 - 1]; n++){
        						hmShotNum.put(n, 0);
        					}
        				}

        				// если новый столбец не сразу перед предыдущим -
        				//   заполнить промежуток нулевыми столбцами
        				if((size2 > 1) && (buf[1] - buf[0] > 1)){
        					for(int n = buf[0]; n < buf[1]; n++){
        						hmShotNum.put(n, 0);
        					}
        				}

        				// заново формируем сортированный массив ключей
        				int size3 = hmShotNum.keySet().size();
        				Integer[] buf2 = new Integer[size3];
        				for(int k = 0; k < size3; k++){
        					buf2[k] = (Integer)hmShotNum.keySet().toArray()[k];
        				}
        				Arrays.sort(buf2);

        				// заполнение гистограммы
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

		// перебор вариантов закончен
    	if(d >= 9999){
    		// остановка AnimationTimer, сброс отображения
    		this.at.stop();
    		this.reset(false);
    		// подсчет статистики
    		Double sum = 0.0;
    		int max = 0;
    		for(int i = 0; i < alShotNum.size(); i++){
    			sum = sum + alShotNum.get(i);
    			if(max < alShotNum.get(i)){
    				max = alShotNum.get(i);
    			}
    		}

    		// отображение результатов
    		this.showStepInfo("Перебор всех вариантов:", false, 0);
    		this.showStepInfo("всего - " + Integer.toString(alShotNum.size()), 
    																false, 0);
    		this.showStepInfo("Максимум попыток - " 
    										+ Integer.toString(max), false, 0);
    		Double aver = sum / alShotNum.size();
    		NumberFormat nf = NumberFormat.getNumberInstance();
    		nf.setMaximumFractionDigits(2);
    		this.showStepInfo("В среднем - " + nf.format(aver), false, 0);
    		this.showStepInfo("[Попыток]: [вариантов]", true, 0);
    		for(int i = 1; i <= max; i++){
    			this.showStepInfo(Integer.toString(i) + ": " 
    							+ Integer.toString(hmShotNum.get(i)), true, 0);
    		}
    		// подчистка данных для нового вызова
    		alShotNum.clear();
    		hmShotNum.clear();
    		d = 0;
        	this.isTestRun = false;
    	}
    }

    // экземпляр AnimationTimer для анимации процесса проверки алгоритма
    AnimationTimer at = new AnimationTimer(){
        @Override
        public void handle(long now) {
        	animatedTestOfAlgorithm();
        }
    };
    // =========================================================================



}
