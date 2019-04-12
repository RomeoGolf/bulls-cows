package romeogolf.bc;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
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
import javafx.scene.control.*;
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

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;

public class MainController implements Initializable{
    // знакоместа пользовательских цифр
    @FXML private Label charSell11Label;
    @FXML private Label charSell12Label;
    @FXML private Label charSell13Label;
    @FXML private Label charSell14Label;
    // знакоместа цифр 2 игрока
    @FXML private Label charSell2_1Label;
    @FXML private Label charSell22Label;
    @FXML private Label charSell23Label;
    @FXML private Label charSell24Label;
    // кнопки для изменения цифр
    @FXML private Button up1Button;
    @FXML private Button down1Button;
    @FXML private Button up2Button;
    @FXML private Button down2Button;
    @FXML private Button up3Button;
    @FXML private Button down3Button;
    @FXML private Button up4Button;
    @FXML private Button down4Button;
    // поля для отображения ходов игроков 1 и 2
    @FXML private VBox player1VBox;
    @FXML private ScrollPane player1ScrollPane;
    @FXML private VBox player2VBox;
    @FXML private ScrollPane player2ScrollPane;
    // переключение режима
    @FXML private ToggleGroup modeToggleGroup;
    // кнопки управления
    @FXML private Button settingQuadButton;
    @FXML private Button generatingQuadButton;
    @FXML private Button resettingButton;
    @FXML private Button testButton;
    @FXML private Button shotButton;
    @FXML private Button settingsButton;
    @FXML private Button helpButton;
    // переключатели режима
    @FXML private RadioButton mode0RadioButton;
    @FXML private RadioButton mode1RadioButton;
    @FXML private RadioButton mode2RadioButton;
    @FXML private RadioButton mode3RadioButton;
    // нижняя панель
    @FXML private HBox bottomHBox;
    // Метки имен полей с результатами попыток
    @FXML private Label player1Label;
    @FXML private Label player2Label;
    // панель для вывода информации всякого рода
    @FXML private Pane infoPane;

    // количество попыток первого игрока (человека)
    private Integer player1ShotNum = 0;

    // право первого хода
    private Boolean isFirstStepPlayer1 = true;
    private Boolean getIsFirstStepPlayer1(){
        return isFirstStepPlayer1;
    }

    private void setFirstPlayer(){
        Integer firstStep = storedDataManager.getFirstStep();
        switch(firstStep){
        case 1: // первым - второй игрок
            isFirstStepPlayer1 = false;
            break;
        case 2: // первый ход по очереди
            isFirstStepPlayer1 = !isFirstStepPlayer1;
            break;
        case 3: // первый ход случайно
            isFirstStepPlayer1 = curator.getRandomBool();
            break;
        default:    // первым - первый игрок (в т. ч. в непонятных ситуациях)
            isFirstStepPlayer1 = true;
        }
        this.isFirstSrepPlayer2 = true;
    }

    private void drawFirstStep(Boolean needDraw){
        if(needDraw){
            Image iWhite = new Image(this.getClass().getResourceAsStream(
                    "/res/img/castle_white.png"));
            Image iBlack = new Image(this.getClass().getResourceAsStream(
                    "/res/img/castle_black.png"));
            if(this.getIsFirstStepPlayer1()){
                this.player1Label.setGraphic(new ImageView(iWhite));
                this.player2Label.setGraphic(new ImageView(iBlack));
                this.shotButton.setText("Попытка");
            } else {
                this.player2Label.setGraphic(new ImageView(iWhite));
                this.player1Label.setGraphic(new ImageView(iBlack));
                this.shotButton.setText("Старт");
            }
        } else {
            this.player1Label.setGraphic(null);
            this.player2Label.setGraphic(null);
            this.shotButton.setText("Попытка");
        }
    }

    // первый ход игрока 2 при его праве первого хода
    private Boolean isFirstSrepPlayer2 = true;

    // режим игры
    private Integer mode = 0;
    private void setMode(Integer mode){
        this.mode = mode;
        this.reset();
        // TODO: сделать перечисление
        /*
        0 : // режим "человек угадывает"
        1 : // режим "человек и машина угадывают друг у друга"
        2 : // режим "человек и машина угадвают одно наперегонки"
        3 : // режим "машина угадывает (тестовый)"
         */
        if (mode == 0){
            curator.generateQuad(1);
        } else {
            generateQwads();
        }
        // если нет загадки для машины
        this.setDisableButtons(false);
        if ((mode == 0) || (mode == 2)){
            this.setXToPlayer2();
        }
        // если играет человек с машиной - отобразить право хода
        this.shotButton.setText("Попытка");
        this.drawFirstStep((mode == 1) || (mode == 2));
    }

    private Integer getMode(){
        return this.mode;
    }

    // Цифры, вводимые пользователем
    private final Integer[] digitsForShow = new Integer[4];
    // массив знакомест для пользовательских цифр
    private final ArrayList<Label> quad1User = new ArrayList<>();
    // массив знакомест для загаданных цифр // TODO: уточнить название
    private final ArrayList<Label> quad2 = new ArrayList<>();
    // массив радиокнопок - переключатель режима
    private final ArrayList<RadioButton> modeRadioButtons = new ArrayList<>();
    // карта соответствия кнопок цифрам
    private final Map<Button, Integer> buttonToDigitMap = new HashMap<>();
    // множество кнопок увеличения
    private final Set<Button> upButtonsSet = new HashSet<>();

    // тестовая кнопка - тестовый обработчик
    @FXML protected void onTest(ActionEvent event) {
        //this.FullTestForAlgorithm();
        this.reset(true);
        if(!this.isTestRun){
            this.algorithmTestAnimationTimer.start();
            isTestRun = true;
        }
    }

    // общий обработчик для всех кнопок Up & Down
    @FXML protected void onUpDown(ActionEvent e) {
            // получение номера цифры, чью кнопку нажали
            int Num = buttonToDigitMap.get(e.getSource());
            if (upButtonsSet.contains(e.getSource())){   // изменение цифры
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
            quad1User.get(Num).setText(Integer.toString(digitsForShow[Num]));
            isEqualDigits();
    }

    // Обработчик кнопки "попытка"
    @FXML protected void onShot(ActionEvent e) {
        this.setDisableButtons(true);
        switch(mode){
        case 0 :    // режим "человек угадывает"
            this.shotMode0();
            break;
        case 1 :    // режим "человек и машина угадывают друг у друга"
            this.shotMode1();
            break;
        case 2 :    // режим "человек и машина угадвают одно наперегонки"
            this.shotMode2();
            break;
        case 3 :    // режим "машина угадывает (тестовый)"
            this.reset();
            shotMode3();
            break;
        default :   // режим "ХЗ"

        }
    }

    // обработчик кнопки установки четверки игроком 1 для игрока 2
    @FXML protected void onSetQwad(ActionEvent e) {
        this.reset();
        if (!this.isEqualDigits()){
            curator.setQuad(this.digitsForShow, 2);
            solver.Init(curator.getDecade());
            for(int i = 0; i < 4; i++){
                quad2.get(i).setText(curator.getQuad(2)[i].toString());
            }
        }
        this.shotButton.setDisable(false);
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
                quad2.get(i).setText(curator.getQuad(2)[i].toString());
            }
        }
        curator.generateQuad(1);
        solver.Init(curator.getDecade());
        this.shotButton.setDisable(false);
    }

    // сброс отображения предыдущей игры и подготовка к следующей
    private void reset(Boolean clearPInfo){
        player1VBox.getChildren().removeAll(player1VBox.getChildren());
        player2VBox.getChildren().removeAll(player2VBox.getChildren());
        this.generateQwads();
        player1ShotNum = 0;
        bulls = 0;
        cows = 0;
        this.setDisableButtons(false);
        this.shotButton.setDisable(false);
        if(this.storedDataManager.getDigitsReset()){
            this.digitsReset();
        }
        this.drawFirstStep((getMode() == 1) || (getMode() == 2));

        if(clearPInfo){
            infoPane.getChildren().removeAll(infoPane.getChildren());
            infoPane.getChildren().add(this.statBoxes.get(this.getMode()));
        }
    }

    private void reset(){
        reset(true);
    }

    // установка доступности кнопок в зависимости от режима
    private void setDisableButtons(Boolean disable){
        // доступность кнопок "загадать" и "сгенерить"
        this.generatingQuadButton.setDisable(disable);
        if (!disable){
            this.settingQuadButton.setDisable((this.mode == 0) || (this.mode == 2));
        } else {
            this.settingQuadButton.setDisable(true);
        }
    }

    // кнопка "сброс"
    @FXML protected void onReset(ActionEvent e) {
        this.algorithmTestAnimationTimer.stop();
        this.isTestRun = false;
        this.reset();
    }

    // кнопка "Настройка"
    @FXML protected void onSettings(ActionEvent e) throws IOException {
        Stage stageSettings = new Stage();
        FXMLLoader loaderSettings = 
                    new FXMLLoader(getClass().getResource("bc_settings.fxml"));
        Parent rootSettings = null;
        try {
            rootSettings = loaderSettings.load();
        } catch (IOException e1) {
            // TODO Не знаю, что тут делать. FXML-файл в составе JAR, его не может не быть.
            e1.printStackTrace();
        }
        SettingsController controllerSettings =
                loaderSettings.getController();
        controllerSettings.setStage_Listener(stageSettings);
        controllerSettings.setSDM(storedDataManager);

        Scene sceneSettings = new Scene(rootSettings);
        stageSettings.setTitle("Настройки");
        stageSettings.setScene(sceneSettings);
        stageSettings.initOwner(this.stage);
        stageSettings.initModality(Modality.APPLICATION_MODAL);
        stageSettings.initStyle(StageStyle.UTILITY);
        stageSettings.show();
    }

    private Stage stageHelp;
    @FXML protected void onHelp(ActionEvent e) throws IOException {
        if(stageHelp == null){
            stageHelp = new Stage();
            FXMLLoader loaderHelp =
                    new FXMLLoader(getClass().getResource("bc_help.fxml"));
            Parent rootHelp = null;
            try {
                rootHelp = loaderHelp.load();
            } catch (IOException e1) {
                // TODO Не знаю, что тут делать. FXML-файл в составе JAR, его не может не быть.
                e1.printStackTrace();
            }
 
            Scene sceneHelp= new Scene(rootHelp);
            stageHelp.setHeight(800);
            stageHelp.setWidth(910);
            stageHelp.setTitle("Справка");
            stageHelp.setScene(sceneHelp);
            stageHelp.initOwner(this.stage);
        }
        stageHelp.show();
    }

    // обработка переключения режима
    private void onModeToggle(){
        if (modeToggleGroup.getSelectedToggle() != null) {
            setMode(this.modeRadioButtons.indexOf(
                    this.modeToggleGroup.getSelectedToggle()));
        } else {
            setMode(0);
        }
    }

    // проверка цифр на совпадение
    private boolean isEqualDigits() {
        for(int i = 0; i < 4; i++) {
            quad1User.get(i).setStyle("-fx-text-fill: #000000;");
        }
        boolean result = false;
        for(int i = 0; i < 3; i++) {
            for(int j = i + 1; j < 4; j++) {
                if (digitsForShow[i].equals(digitsForShow[j])) {
                    quad1User.get(i).setStyle("-fx-text-fill: #FF0000;");
                    quad1User.get(j).setStyle("-fx-text-fill: #FF0000;");
                    result = true;
                }
            }
        }
        return result;
    }

    // =================== инициализация интерфейса ============================
    // список панелей для отображения статистики прошлых игр
    private final ArrayList<VBox> statBoxes = new ArrayList<>();
    // список меток для отображения статистики
    private final ArrayList<Label> totalLabels = new ArrayList<>();
    private final ArrayList<Label> winLabels = new ArrayList<>();
    private final ArrayList<Label> tieLabels = new ArrayList<>();
    // создание панелей статистики, заполнение списков панелей и меток
    private void buildBox(String tl1, String tl2, String tl3){
        DropShadow ds = new DropShadow();
        ds.setOffsetX(5.0);
        ds.setOffsetY(5.0);
        ds.setColor(Color.GRAY);
        ds.setWidth(5.0);

        VBox vb = new VBox();
        vb.setMinHeight(this.infoPane.getPrefHeight());
        vb.setMaxHeight(this.infoPane.getPrefHeight());
        vb.setMinWidth(this.infoPane.getPrefWidth());
        vb.setMaxWidth(this.infoPane.getPrefWidth());
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        statBoxes.add(vb);
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
        totalLabels.add(l2);
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
        winLabels.add(l2);
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
        tieLabels.add(l2);
    }

    private void buildStatBoxes(){
        // для режима 0 (человек угадывает)
        buildBox("Сыграно - ", "Мин. попыток - ", "Макс. попыток - ");
        // для режима 1 (человек <-> машина)
        buildBox("Сыграно - ", "Счет - ", "Ничьих - ");
        // для режима 2 (человек, машина -> машина)
        buildBox("Сыграно - ", "Счет - ", "Ничьих - ");

        // для режима 3 (машина -> машина)
        VBox vb = new VBox();
        vb.setMinHeight(this.infoPane.getPrefHeight());
        vb.setMaxHeight(this.infoPane.getPrefHeight());
        vb.setMinWidth(this.infoPane.getPrefWidth());
        vb.setMaxWidth(this.infoPane.getPrefWidth());
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        statBoxes.add(vb);
    }

    // объект для управления данными, сохраняемыми в память (настройки, etc)
    private final StoredDataManager storedDataManager = new StoredDataManager();
    private Stage stage;
    private void storePrefs(){
        storedDataManager.setTop(stage.getY());
        storedDataManager.setLeft(stage.getX());
        storedDataManager.setHeight(stage.getHeight());
        storedDataManager.setMode(this.mode);
        storedDataManager.writeData();
    }

    private void drawStatLabels(){
        this.totalLabels.get(0).setText(Integer.toString(storedDataManager.getMode0Total()));
        int total = storedDataManager.getMode1Player1Win() + storedDataManager.getMode1Player2Win() +
                storedDataManager.getMode1Tie();
        this.totalLabels.get(1).setText(Integer.toString(total));
        total = storedDataManager.getMode2Player1Win() + storedDataManager.getMode2Player2Win() +
                storedDataManager.getMode2Tie();
        this.totalLabels.get(2).setText(Integer.toString(total));

        this.winLabels.get(0).setText(Integer.toString(storedDataManager.getMode0Min()));
        StringBuffer sb = 
                new StringBuffer(Integer.toString(storedDataManager.getMode1Player1Win()));
        sb.append(" : ");
        sb.append(storedDataManager.getMode1Player2Win());
        this.winLabels.get(1).setText(sb.toString());
        sb = new StringBuffer(Integer.toString(storedDataManager.getMode2Player1Win()));
        sb.append(" : ");
        sb.append(storedDataManager.getMode2Player2Win());
        this.winLabels.get(2).setText(sb.toString());

        this.tieLabels.get(0).setText(Integer.toString(storedDataManager.getMode0Max()));
        this.tieLabels.get(1).setText(Integer.toString(storedDataManager.getMode1Tie()));
        this.tieLabels.get(2).setText(Integer.toString(storedDataManager.getMode2Tie()));
    }

    // восстановление параметров игры из файла настроек
    private void readPrefs(){
        this.drawStatLabels();

        // чтение режима
        Integer bufInt = storedDataManager.getMode();
        if((bufInt < 0) || (bufInt > 3)){
            bufInt = 0;
        }
        this.setMode(0);
        if(this.modeRadioButtons.indexOf(this.modeToggleGroup.getSelectedToggle())
                                                                    != bufInt){
            this.modeToggleGroup.selectToggle(modeRadioButtons.get(bufInt));
        }
    }

    // восстановление параметров окна из файла настроек
    private void readStagePrefs(){
        if(storedDataManager.isPosition()){
            stage.setY(storedDataManager.getTop());
            stage.setX(storedDataManager.getLeft());
            stage.setHeight(storedDataManager.getHeight());
        }
    }

    // получение ссылки на окно, установка обработчика событий окна 
    //  и чтение параметров окна
    void setStage_Listener(final Stage stage){
        this.stage = stage;
        stage.setOnCloseRequest(event -> {
            //event.consume();
            // сохранение настроек перед закрытием окна
            storePrefs();
        });
        // чтение сохраненных параметров окна после получения
        //  ссылки на окно
        this.readStagePrefs();
    }

    // соответствие панели VBox своему контейнеру ScrollPane
    private final Map<VBox, ScrollPane> playerPanes = new HashMap<>();

    // общий обработчик увеличения для обоих VBox Player[X]
    private void setVBoxScroller(final VBox box){
        box.setSpacing(3);
        box.heightProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> {
            if((Double)oldValue > 0){
                playerPanes.get(box).setVvalue(1.0);
            }
        });
    }

    private void digitsReset(){
        // заполнение массива цифр и его отображение
        for(int i = 0; i < 4; i++){
            this.digitsForShow[i] = i + 1;
            quad1User.get(i).setText(Integer.toString(this.digitsForShow[i]));
        }
    }

    // собственно инициализация
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // загрузка изображений в элементы управления
        getImages();
        // добавление обработчика изменения размера панели на ScrollPane
        // для прокрутки до упора вниз
        playerPanes.put(player1VBox, player1ScrollPane);
        playerPanes.put(player2VBox, player2ScrollPane);
        setVBoxScroller(this.player1VBox);
        setVBoxScroller(this.player2VBox);

        setControlMaps();   // для групповой обработки кнопок
        this.digitsReset();

        // установка обработчика переключения режима игры
        modeToggleGroup.selectedToggleProperty().addListener(
                (ov, old_toggle, new_toggle) -> {
                    if (old_toggle != new_toggle) {
                        onModeToggle();
                    }
                });

        this.buildStatBoxes();
        this.readPrefs();   // чтение сохраненных параметров игры
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
            this.quad2.get(i).setEffect(ds);
        }
        for(int i = 0; i < 10; i++){
            this.aidDigits.get(i).setEffect(ds);
        }

        DropShadow ds2 = new DropShadow();
        ds2.setOffsetX(4.0);
        ds2.setOffsetY(4.0);
        ds2.setColor(Color.GRAY);
        ds2.setWidth(5.0);
        this.player1Label.setEffect(ds2);
        this.player2Label.setEffect(ds2);
        for(int i = 0; i < 4; i++){
            this.quad1User.get(i).setEffect(ds2);
        }
    }

    private void setControlMaps(){
        // заполнение карты соответствия кнопок их номерам
        buttonToDigitMap.put(up1Button, 0);
        buttonToDigitMap.put(down1Button, 0);
        buttonToDigitMap.put(up2Button, 1);
        buttonToDigitMap.put(down2Button, 1);
        buttonToDigitMap.put(up3Button, 2);
        buttonToDigitMap.put(down3Button, 2);
        buttonToDigitMap.put(up4Button, 3);
        buttonToDigitMap.put(down4Button, 3);
        // множество кнопок "вверх"
        upButtonsSet.add(up1Button);
        upButtonsSet.add(up2Button);
        upButtonsSet.add(up3Button);
        upButtonsSet.add(up4Button);
        // множество знакомест для цифр
        quad1User.add(charSell11Label);
        quad1User.add(charSell12Label);
        quad1User.add(charSell13Label);
        quad1User.add(charSell14Label);
        // множество знакомест для загаданных цифр
        quad2.add(charSell2_1Label);
        quad2.add(charSell22Label);
        quad2.add(charSell23Label);
        quad2.add(charSell24Label);
        // множество радиокнопок - переключатель режима
        modeRadioButtons.add(mode0RadioButton);
        modeRadioButtons.add(mode1RadioButton);
        modeRadioButtons.add(mode2RadioButton);
        modeRadioButtons.add(mode3RadioButton);
    }

    private void getImages(){
        Image testImage = new Image(this.getClass().getResourceAsStream(
                                                        "/res/img/test.png"));
        this.testButton.setGraphic(new ImageView(testImage));

        Image upImage = new Image(this.getClass().getResourceAsStream(
                                                            "/res/img/up.png"));
        Image downImage = new Image(this.getClass().getResourceAsStream(
                                                        "/res/img/down.png"));
        this.up1Button.setGraphic(new ImageView(upImage));
        this.up2Button.setGraphic(new ImageView(upImage));
        this.up3Button.setGraphic(new ImageView(upImage));
        this.up4Button.setGraphic(new ImageView(upImage));
        this.down1Button.setGraphic(new ImageView(downImage));
        this.down2Button.setGraphic(new ImageView(downImage));
        this.down3Button.setGraphic(new ImageView(downImage));
        this.down4Button.setGraphic(new ImageView(downImage));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5.0);
        dropShadow.setOffsetY(5.0);
        dropShadow.setColor(Color.GRAY);
        dropShadow.setWidth(5.0);
        ImageView imageView;

        Image mode0Image = new Image(this.getClass().getResourceAsStream(
                                                    "/res/img/user-comp.png"));
        imageView = new ImageView(mode0Image);
        imageView.setEffect(dropShadow);
        this.mode0RadioButton.setGraphic(imageView);
        this.mode0RadioButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        Image mode1Image = new Image(this.getClass().getResourceAsStream(
                                                "/res/img/user vs comp.png"));
        imageView = new ImageView(mode1Image);
        imageView.setEffect(dropShadow);
        this.mode1RadioButton.setGraphic(imageView);
        this.mode1RadioButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        Image mode2Image = new Image(this.getClass().getResourceAsStream(
                                                "/res/img/user,comp-comp.png"));
        imageView = new ImageView(mode2Image);
        imageView.setEffect(dropShadow);
        this.mode2RadioButton.setGraphic(imageView);
        this.mode2RadioButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        Image mode3Image = new Image(this.getClass().getResourceAsStream(
                                                    "/res/img/comp-comp.png"));
        imageView = new ImageView(mode3Image);
        imageView.setEffect(dropShadow);
        this.mode3RadioButton.setGraphic(imageView);
        this.mode3RadioButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        Image shotImage = new Image(this.getClass().getResourceAsStream(
                                                    "/res/img/check.png"));
        this.shotButton.setGraphic(new ImageView(shotImage));
        this.shotButton.setGraphicTextGap(10);

        Image generatingImage = new Image(this.getClass().getResourceAsStream(
                                                    "/res/img/dice.png"));
        this.generatingQuadButton.setGraphic(new ImageView(generatingImage));

        Image settingQuadImage = new Image(this.getClass().getResourceAsStream(
                                                    "/res/img/touch.png"));
        this.settingQuadButton.setGraphic(new ImageView(settingQuadImage));

        Image resettingImage = new Image(this.getClass().getResourceAsStream(
                                                    "/res/img/refresh.png"));
        this.resettingButton.setGraphic(new ImageView(resettingImage));

        Image settingsImage = new Image(this.getClass().getResourceAsStream(
                                                "/res/img/configuration.png"));
        this.settingsButton.setGraphic(new ImageView(settingsImage));

        Image helpImage = new Image(this.getClass().getResourceAsStream(
                                                "/res/img/help.png"));
        this.helpButton.setGraphic(new ImageView(helpImage));
    }

    // массив меток для вспомогательных цифр и массив кодов цвета для них
    private final ArrayList<Label> aidDigits = new ArrayList<>();
    private final ArrayList<Integer> aidDigitsColor = new ArrayList<>();
    // заполнение массивов меток и цвета, установка обработчика клика меток
    private void setAidDigitsMap(){
        for(int i = 0; i < bottomHBox.getChildren().size(); i++){
            if(bottomHBox.getChildren().get(i).getClass() == Label.class){
                final Label label = (Label) bottomHBox.getChildren().get(i);
                label.setText(Integer.toString(i));
                aidDigits.add(label);
                aidDigitsColor.add(1);
                label.setOnMouseClicked(this::onAidDigitClick);
            }
        }
    }

    // обработка клика мышью по метке - циклическое изменение трех цветов
    private void onAidDigitClick(MouseEvent e) {
        Label label = (Label)e.getSource();
        int num = this.aidDigits.indexOf(label);
        int colorNum = this.aidDigitsColor.get(num);
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
        this.aidDigitsColor.set(num, colorNum);
        switch(colorNum){
        case 2:
            label.setStyle("-fx-text-fill: #FF0000;");
            break;
        case 3:
            label.setStyle("-fx-text-fill: #00AFFF;");
            break;
        default:
            label.setStyle("-fx-text-fill: #000000;");
        }
    }

    // обработчик кнопки сброса цвета меток вспомогательных цифр на черный
    @FXML protected void onAidDigitsReset(ActionEvent e) {
        for(int i = 0; i < this.aidDigits.size(); i++){
            aidDigitsColor.set(i, 1);
            aidDigits.get(i).setStyle("-fx-text-fill: #000000;");
        }
    }
    // ================ конец инициализации интерфейса =========================

    // вывод строки текста в ScrollPane - информация о шаге игры
    private void showStepInfo(String s, boolean Player1, int img) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        if(img == 0){
            hBox.setPadding(new Insets(0, 0, 0, 30));
        }
        Text t = new Text(s);
        t.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        hBox.getChildren().add(t);
        //hb.setStyle("-fx-background-color: #33FFFF;");
        hBox.setStyle("-fx-background-color: #CCFF99;");

        // при игре в одиночку (режим 0) можно добавить флажок определенного
        //   цвета в зависимости от числа попыток
        Image flagImage = null;
        switch(img){
        case 1:
            flagImage = new Image(this.getClass().getResourceAsStream(
                                                "/res/img/ball2_green.png"));
            break;
        case 2:
            flagImage = new Image(this.getClass().getResourceAsStream(
                                            "/res/img/ball2_yellow.png"));
            break;
        case 3:
            flagImage = new Image(this.getClass().getResourceAsStream(
                                            "/res/img/ball2_red.png"));
            break;
        }
        if (flagImage != null){
            hBox.getChildren().add(0, new ImageView(flagImage));
            hBox.setSpacing(20);
        }

        if (Player1) {
            player1VBox.getChildren().add(hBox);
        } else {
            player2VBox.getChildren().add(hBox);
        }
    }

    private final DigitCurator curator = new DigitCurator();  // класс слежения за игрой
    private final Solver solver = new Solver();               // класс-игрок
    private Integer[] digits = new Integer[4];          // Цифры, вводимые пользователем
    private int bulls;
    private int cows;

    // вывод результатов попытки
    private void showNextShot(int trying, boolean Player1, int img) {
        String s = Arrays.toString(digits);
        s = trying + ": " + s;
        s = s + " -   " + bulls + " Б, " + cows + " К";
        showStepInfo(s, Player1, img);
    }

    // ========= машинная отгадка ======
    // машина -> машина, режим 3
    private void shotMode3() {
        curator.digitMixer();       // перемешать цифры подготовить набор цифр
        solver.Init(curator.getDecade());

        while(bulls + cows < 4) {       // цикл до отгадки всех цифр
            digits = solver.toFindDigits();
            Integer[] TmpBufI = new Integer[4];
            System.arraycopy(digits, 0, TmpBufI, 0, 4);
            ShotData shotData = curator.checkQuad(digits, 2);
            bulls = shotData.getBulls();
            cows = shotData.getCows();
            solver.addShotData(shotData);
            showNextShot(solver.getNumberOfShots(), false, 0);  // отображение
        }
        while(bulls < 4) {
            solver.toFindBulls(digits);
            ShotData shotData = curator.checkQuad(digits, 2);
            bulls = shotData.getBulls();
            cows = shotData.getCows();
            solver.addShotData(shotData);
            showNextShot(solver.getNumberOfShots(), false, 0);  // отображение
        }
        solver.clearShotsData();
        bulls = 0;
        cows = 0;
    }

    // человек -> машина
    private void shotMode0(){
        if (!this.isEqualDigits()){
            player1ShotNum++;
            ShotData shotData = curator.checkQuad(this.digitsForShow, 1);
            String s = Arrays.toString(digitsForShow);
            s = player1ShotNum + ": " + s;
            s = s + " -   " + shotData.getBulls() + " Б, " + shotData.getCows() + " К";
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
            if (shotData.getBulls() == 4){
                this.doEndOfGame(0);
            }
        }
    }

    private Boolean tryPlayer1(){
        boolean result = false;
        player1ShotNum++;
        ShotData shotData = curator.checkQuad(this.digitsForShow, 1);
        String s = Arrays.toString(digitsForShow);
        s = player1ShotNum + ": " + s;
        s = s + " -   " + shotData.getBulls() + " Б, " + shotData.getCows() + " К";
        showStepInfo(s, true, 0);
        if (shotData.getBulls() == 4){
            result = true;
        }
        return result;
    }

    private Boolean tryPlayer2(int quadNum){
        boolean result = false;

        digits = solver.toFindDigits();
        Integer[] TmpBufI = new Integer[4];
        System.arraycopy(digits, 0, TmpBufI, 0, 4);
        ShotData shotData = curator.checkQuad(digits, quadNum);
        bulls = shotData.getBulls();
        cows = shotData.getCows();
        solver.addShotData(shotData);

        if(quadNum == 2){
            showNextShot(solver.getNumberOfShots(), false, 0);  // отображение
        } else {
            String s2 = Integer.toString(solver.getNumberOfShots());
            s2 = s2 + ": -   " + bulls + " Б, " + cows + " К";
            showStepInfo(s2, false, 0);
        }

        if (bulls == 4){
            result = true;
        }
        return result;
    }

    private Boolean isPlayer1End = false;
    private Boolean isPlayer2End = false;

    private Boolean whoWin(Boolean p1, Boolean p2){
        boolean result = false;
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
            isPlayer1End = false;
            isPlayer2End = false;
        }
        return result;
    }

    // человек <-> машина
    private void shotMode1(){
        if(this.getIsFirstStepPlayer1()){
            if (!this.isEqualDigits()){
                isPlayer1End = this.tryPlayer1();
                isPlayer2End = this.tryPlayer2(2);
                this.whoWin(isPlayer1End, isPlayer2End);
            }
        } else {
            if(this.isFirstSrepPlayer2){
                isFirstSrepPlayer2 = false;
                this.shotButton.setText("Попытка");
                isPlayer2End = this.tryPlayer2(2);
            } else {
                if (!this.isEqualDigits()){
                    isPlayer1End = this.tryPlayer1();
                    if(!this.whoWin(isPlayer1End, isPlayer2End)){
                        isPlayer2End = this.tryPlayer2(2);
                    }
                }
            }
        }
    }

    // человек, машина -> машина
    private void shotMode2(){
        if(this.getIsFirstStepPlayer1()){
            if (!this.isEqualDigits()){
                isPlayer1End = this.tryPlayer1();
                isPlayer2End = this.tryPlayer2(1);
                this.whoWin(isPlayer1End, isPlayer2End);
            }
        } else {
            if(this.isFirstSrepPlayer2){
                isFirstSrepPlayer2 = false;
                this.shotButton.setText("Попытка");
                isPlayer2End = this.tryPlayer2(1);
            } else {
                if (!this.isEqualDigits()){
                    isPlayer1End = this.tryPlayer1();
                    if(!this.whoWin(isPlayer1End, isPlayer2End)){
                        isPlayer2End = this.tryPlayer2(1);
                    }
                }
            }
        }
    }

    // действия при окончании игры - когда кто-то угадал
    private void doEndOfGame(int player){
        this.shotButton.setDisable(true);
        Integer wins;
        switch(player){
        case 0:
            this.showStepInfo("Победа!", true, 0);
            storedDataManager.setMode0Total(storedDataManager.getMode0Total() + 1);
            Integer max = storedDataManager.getMode0Max();
            Integer min = storedDataManager.getMode0Min();
            if(this.player1ShotNum > max){
                storedDataManager.setMode0Max(player1ShotNum);
            }
            if((this.player1ShotNum < min) || (min <= 0)){
                storedDataManager.setMode0Min(player1ShotNum);
            }
            break;
        case 1:
            this.showStepInfo("Победа игрока 1!", true, 0);
            this.showStepInfo("Победа игрока 1!", false, 0);
            if(this.getMode() == 1){
                wins = storedDataManager.getMode1Player1Win();
                storedDataManager.setMode1Player1Win(wins + 1);
            } else {
                wins = storedDataManager.getMode2Player1Win();
                storedDataManager.setMode2Player1Win(wins + 1);
            }
            break;
        case 2:
            this.showStepInfo("Победа игрока 2!", true, 0);
            this.showStepInfo("Победа игрока 2!", false, 0);
            if(this.getMode() == 1){
                wins = storedDataManager.getMode1Player2Win();
                storedDataManager.setMode1Player2Win(wins + 1);
            } else {
                wins = storedDataManager.getMode2Player2Win();
                storedDataManager.setMode2Player2Win(wins + 1);
            }
            break;
        case 3:
            this.showStepInfo("Ничья!", true, 0);
            this.showStepInfo("Ничья!", false, 0);
            if(this.getMode() == 1){
                wins = storedDataManager.getMode1Tie();
                storedDataManager.setMode1Tie(wins++);
            } else {
                wins = storedDataManager.getMode2Tie();
                storedDataManager.setMode2Tie(wins + 1);
            }
            break;
        }
        this.setDisableButtons(false);
        this.drawStatLabels();
    }

    // заполнение знакомест игрока 2 крестиками
    private void setXToPlayer2(){
        for(int i = 0; i < 4; i++){
            quad2.get(i).setText("X");
        }
    }

    // =========================================================================
    // проверка алгоритма перебором всех вариантов
    //   с отображением процесса

    // для гистограммы
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private BarChart<String, Number> histogramBarChart;
    private XYChart.Series<String, Number> series;

    // тестовая четверка для перебора
    private final Integer[] testQuad = {0, 0, 0, 0};
    // постоянный набор цифр для отгадывания
    private final Integer[] testDecade = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    // учет количества попыток на вариант для подсчета статистики
    private final ArrayList<Integer> shotNumbers = new ArrayList<>();
    // учет количества попыток на вариант для гистограммы
    private final Map<Integer, Integer> shotNumMap = new HashMap<>();
    // счетчик для перебора вариантов
    private int variantCounter = 0;
    // тест запущен?
    private Boolean isTestRun = false;

    // метод для вызова из AnimationTimer.handle
    private void animatedTestOfAlgorithm(){
        // если графика гистограммы еще не существует - создать
        if(histogramBarChart == null){
            histogramBarChart = new BarChart<>(xAxis, yAxis);
            series = new XYChart.Series<>();
            histogramBarChart.getData().add(series);
            histogramBarChart.setMaxHeight(200);
            histogramBarChart.setPadding(new Insets(0,0,0,0));
            histogramBarChart.setLegendVisible(false);
            histogramBarChart.setBarGap(0);
            histogramBarChart.setCategoryGap(0);
            histogramBarChart.setAnimated(false);
            histogramBarChart.setMaxHeight(infoPane.getHeight());
            histogramBarChart.setMaxWidth(infoPane.getWidth());
            infoPane.getChildren().removeAll(infoPane.getChildren());
            infoPane.getChildren().add(histogramBarChart);
        } else {    // иначе - добавить на панель и очистить данные
            if(!infoPane.getChildren().contains(histogramBarChart)){
                infoPane.getChildren().removeAll(infoPane.getChildren());
                infoPane.getChildren().add(histogramBarChart);
                this.series.getData().clear();
            }
        }

        // создание четверки из счетчика с отсевом повторяющихся цифр
        boolean isQuadReady = true;
        do{
            isQuadReady = true;
            variantCounter++;
            testQuad[3] = variantCounter % 10;
            testQuad[2] = (variantCounter / 10) % 10;
            testQuad[1] = (variantCounter / 100) % 10;
            testQuad[0] = (variantCounter / 1000) % 10;
            for(int i = 0; i < 3; i++) {
                for(int j = i + 1; j < 4; j++) {
                    if (testQuad[i].equals(testQuad[j])) {
                        isQuadReady = false;
                    }
                }
            }
        }while(!isQuadReady && (variantCounter <= 9999));

        if(isQuadReady){    // условие вставлено для чисел (d > 9870)
            // сброс отображения, подготовка к решению
            this.reset(false);
            this.curator.setQuad(testQuad, 2);
            this.solver.Init(testDecade);
            // отображение подготовленной четверки
            for(int i = 0; i < 4; i++){
                quad2.get(i).setText(testQuad[i].toString());
            }

            // решение
            while(bulls + cows < 4) {       // цикл до отгадки всех цифр
                digits = solver.toFindDigits();
                Integer[] TmpBufI = new Integer[4];
                System.arraycopy(digits, 0, TmpBufI, 0, 4);
                ShotData shot_data = curator.checkQuad(digits, 2);
                bulls = shot_data.getBulls();
                cows = shot_data.getCows();
                solver.addShotData(shot_data);
                showNextShot(solver.getNumberOfShots(), false, 0); // отображение
            }
            while(bulls < 4) {
                solver.toFindBulls(digits);
                ShotData shot_data = curator.checkQuad(digits, 2);
                bulls = shot_data.getBulls();
                cows = shot_data.getCows();
                solver.addShotData(shot_data);
                showNextShot(solver.getNumberOfShots(), false, 0); // отображение
            }

            // заполнение коллекций для статистического учета
            int size = solver.getNumberOfShots();
            shotNumbers.add(size);
            if(shotNumMap.containsKey(size)){
                shotNumMap.put(size, shotNumMap.get(size) + 1);
            } else {
                shotNumMap.put(size, 1);
            }
            // подчистка для следующего решения
            solver.clearShotsData();
            bulls = 0;
            cows = 0;

            // работа с гистограммой
            boolean isData;
            if(histogramBarChart != null){
                // проверка существования столбика для элемента shotNumMap
                for(int i = 0; i < shotNumMap.keySet().size(); i++){
                    isData = false;
                    Integer key = (Integer) shotNumMap.keySet().toArray()[i];
                    for(int j = 0;
                        j < histogramBarChart.getData().get(0).getData().size(); j++){
                        // если есть - изменить данные столбика
                        String xv = 
                            histogramBarChart.getData().get(0).getData().get(j).getXValue();
                        Integer yv = shotNumMap.get(key);
                        if(xv.equals(Integer.toString(key))){
                            histogramBarChart.getData().get(0).getData().get(j).setYValue(yv);
                            isData = true;
                            break;
                        }
                    }
                    // если нету - очистить серию и наполнить заново 
                    //    в сортированном виде
                    //   (если просто вставлять в нужную позицию -
                    //    может быть несортированное отображение - баг JavaFX)
                    if(!isData){
                        this.series.getData().clear();
                        int size2 = shotNumMap.keySet().size();
                        Integer[] buf = new Integer[size2];
                        for(int k = 0; k < size2; k++){
                            buf[k] = (Integer) shotNumMap.keySet().toArray()[k];
                        }
                        Arrays.sort(buf);

                        // если столбцы начинаются не с 1 -
                        //   заполнить промежуток нулевыми столбцами
                        if(buf[0] > 1){
                            for(int n = 1; n < buf[0]; n++){
                                shotNumMap.put(n, 0);
                            }
                        }

                        // если новый столбец не сразу после предыдущего -
                        //   заполнить промежуток нулевыми столбцами
                        if((size2 > 2) && 
                                        (buf[size2 - 1] - buf[size2 - 2] > 1)){
                            for(int n = buf[size2 - 2]; 
                                                    n < buf[size2 - 1]; n++){
                                shotNumMap.put(n, 0);
                            }
                        }

                        // если новый столбец не сразу перед предыдущим -
                        //   заполнить промежуток нулевыми столбцами
                        if((size2 > 1) && (buf[1] - buf[0] > 1)){
                            for(int n = buf[0]; n < buf[1]; n++){
                                shotNumMap.put(n, 0);
                            }
                        }

                        // заново формируем сортированный массив ключей
                        int size3 = shotNumMap.keySet().size();
                        Integer[] buf2 = new Integer[size3];
                        for(int k = 0; k < size3; k++){
                            buf2[k] = (Integer) shotNumMap.keySet().toArray()[k];
                        }
                        Arrays.sort(buf2);

                        // заполнение гистограммы
                        for(int k = 0; k < size3; k++){
                            Integer key2 = buf2[k];
                            this.series.getData().add(new Data<>(Integer.toString(key2),
                                    shotNumMap.get(key2)));
                        }
                    }
                }
            }
        }

        // перебор вариантов закончен
        if(variantCounter >= 9999){
            // остановка AnimationTimer, сброс отображения
            this.algorithmTestAnimationTimer.stop();
            this.reset(false);
            // подсчет статистики
            Double sum = 0.0;
            int max = 0;
            for (Integer integer : shotNumbers) {
                sum = sum + integer;
                if (max < integer) {
                    max = integer;
                }
            }

            // отображение результатов
            this.showStepInfo("Перебор всех вариантов:", false, 0);
            this.showStepInfo("всего - " + shotNumbers.size(),
                                                                    false, 0);
            this.showStepInfo("Максимум попыток - " + max, false, 0);
            Double aver = sum / shotNumbers.size();
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(2);
            this.showStepInfo("В среднем - " + nf.format(aver), false, 0);
            this.showStepInfo("[Попыток]: [вариантов]", true, 0);
            for(int i = 1; i <= max; i++){
                this.showStepInfo(i + ": " + shotNumMap.get(i), true, 0);
            }
            // подчистка данных для нового вызова
            shotNumbers.clear();
            shotNumMap.clear();
            variantCounter = 0;
            this.isTestRun = false;
        }
    }

    // экземпляр AnimationTimer для анимации процесса проверки алгоритма
    private final AnimationTimer algorithmTestAnimationTimer = new AnimationTimer(){
        @Override
        public void handle(long now) {
            animatedTestOfAlgorithm();
        }
    };
    // =========================================================================



}
