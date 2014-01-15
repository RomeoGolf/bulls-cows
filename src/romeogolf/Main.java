package romeogolf;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import javafx.beans.value.*;
import javax.swing.JOptionPane;
import romeogolf.DigitCurator;
import romeogolf.Solver;


public class Main extends Application {
	DigitCurator curator = new DigitCurator();
	Solver solver = new Solver();
    Integer[] Digits = new Integer[4];			// �����, �������� �������������
    TextField[] atfDigits = new TextField[4];	// ���� ����� ����
    int bulls;
    int cows;
    int trying;
    // ������ ���������� �����
    public Button btUp1;
    public Button btUp2;
    public Button btUp3;
    public Button btUp4;
    // ������ ���������� �����
    public Button btDown1;
    public Button btDown2;
    public Button btDown3;
    public Button btDown4;

    VBox vbCenter = new VBox();
    ScrollPane spCenter = new ScrollPane();

    public Map<Button, Integer> df;	// ����� ������������ ������ ������
    public Set<Button> sUp;			// ��������� ������ ����������
    public void Info(String s) {
    	JOptionPane.showMessageDialog(null, s, "Info", JOptionPane.INFORMATION_MESSAGE);
    };

    // ����� ���������� ��� ���� ������
    EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
    	@Override
    	public void handle(ActionEvent e) {
    		int Num = df.get(e.getSource());	// ��������� ������ �����, ��� ������ ������
    		if (sUp.contains(e.getSource())){	// ��������� �����
    				Digits[Num]++;
    		} else {
    			Digits[Num]--;
    		}
    		if (Digits[Num] < 0){Digits[Num] = 9;}	// �������� ������ ����� �� �������
    		if (Digits[Num] > 9){Digits[Num] = 0;}	// � ��������������� ���������
    		atfDigits[Num].setText(Integer.toString(Digits[Num]));  // ����������� �����

    		IsDifferent();
    	};
    };

    boolean IsDifferent() {
    	for(int i = 0; i < 4; i++) {
    		atfDigits[i].setStyle("-fx-text-fill: #000000;");
    	}
    	for(int i = 0; i < 3; i++) {
    		for(int j = i + 1; j < 4; j++) {
    			if(Digits[i] == Digits[j]) {
    				atfDigits[i].setStyle("-fx-text-fill: #FF0000;");
    				atfDigits[j].setStyle("-fx-text-fill: #FF0000;");
    				return true;
    			}
    		}
    	}
    	return false;
    };

    void CalcBullCow() {
    	bulls = 0;
    	cows = 0;
    	for(int i = 0; i < 4; i++) {
    		if (Digits[i] == curator.RndDigits[i]) {
    			bulls++;
    		}
    	}
    	Set<Integer> s = new HashSet<Integer>();
    	for(int i = 0; i < 4; i++) {
    		s.add(curator.RndDigits[i]);
    	}
    	for(int i = 0; i < 4; i++) {
    		if(s.contains(Digits[i])){
    			cows++;
    		}
    	}
    	cows = cows - bulls;
    };

    public static void main(String[] args) {
    	launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
    	trying = 0;
    	// ������������� ����, �������� ����� �����
    	for(int i = 0; i <= 3; i++){
    		Digits[i] = i; //rg.nextInt(9);
    		atfDigits[i] = new TextField(Integer.toString(Digits[i]));
    		atfDigits[i].setPrefColumnCount(1);
    		atfDigits[i].setEditable(false);
    	}

    	curator.Init();

    	primaryStage.setTitle("CheckOut");
    	BorderPane bp = new BorderPane();
    	Scene scene = new Scene(bp, 300, 375);

    	HBox hbTop = new HBox();
    	hbTop.setAlignment(Pos.CENTER);
    	hbTop.setPadding(new Insets(15, 12, 15, 12));
    	VBox vbForTop = new VBox();
    	vbForTop.setAlignment(Pos.CENTER);
    	vbForTop.setSpacing(10);
    	hbTop.getChildren().add(vbForTop);

    	HBox hbBottom = new HBox();
    	hbTop.setStyle("-fx-background-color: #336699;");
    	bp.setTop(hbTop);
    	hbBottom.setAlignment(Pos.CENTER);
    	bp.setBottom(hbBottom);

    	bp.setCenter(spCenter);
    	vbCenter.setAlignment(Pos.TOP_CENTER);
    	vbCenter.setSpacing(3);
    	spCenter.setContent(vbCenter);
    	spCenter.setFitToWidth(true);

    	vbCenter.heightProperty().addListener(new ChangeListener<Object>() {
    		@Override
    		public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
    			spCenter.setVvalue((Double)newValue );
            }
        });

    	// ------ ���������� ������ � ����� ����� ���� --------
    	btUp1 = new Button();
    	btDown1 = new Button();
    	btUp1.setOnAction(eh);
    	btDown1.setOnAction(eh);

    	VBox vb1 = new VBox();
    	vb1.setAlignment(Pos.CENTER);
    	vb1.getChildren().add(btUp1);
    	vb1.getChildren().add(atfDigits[0]);
    	vb1.getChildren().add(btDown1);

    	btUp2 = new Button();
    	btDown2 = new Button();
    	btUp2.setAlignment(Pos.CENTER);
    	btUp2.setOnAction(eh);
    	btDown2.setOnAction(eh);

    	VBox vb2 = new VBox();
    	vb2.setAlignment(Pos.CENTER);
    	vb2.getChildren().add(btUp2);
    	vb2.getChildren().add(atfDigits[1]);
    	vb2.getChildren().add(btDown2);

    	btUp3 = new Button();
    	btDown3 = new Button();
    	btUp3.setOnAction(eh);
    	btDown3.setOnAction(eh);

    	VBox vb3 = new VBox();
    	vb3.setAlignment(Pos.CENTER);
    	vb3.getChildren().add(btUp3);
    	vb3.getChildren().add(atfDigits[2]);
    	vb3.getChildren().add(btDown3);

    	btUp4 = new Button();
    	btDown4 = new Button();
    	btUp4.setOnAction(eh);
    	btDown4.setOnAction(eh);

    	VBox vb4 = new VBox();
    	vb4.setAlignment(Pos.CENTER);
    	vb4.getChildren().add(btUp4);
    	vb4.getChildren().add(atfDigits[3]);
    	vb4.getChildren().add(btDown4);

    	HBox hbDigits = new HBox();
    	hbDigits.setSpacing(5);
    	hbDigits.getChildren().addAll(vb1, vb2, vb3, vb4);
    	vbForTop.getChildren().add(hbDigits);

    	Button btEnter = new Button("Enter");
    	vbForTop.getChildren().add(btEnter);
    	btEnter.setOnAction(new EventHandler<ActionEvent>() {
    		@Override 
    		public void handle(ActionEvent e) {
    			if(IsDifferent()){return;}
    			trying++;
    			CalcBullCow();
    			ShowNextShot(trying);
    		}
    	});
    	Button btSelf = new Button("Self");
    	vbForTop.getChildren().add(btSelf);
    	btSelf.setOnAction(new EventHandler<ActionEvent>() {
    		@Override 
    		public void handle(ActionEvent e) {
    			SelfAnswer();
    		}
    	});
    	// -------- ����� ���������� --------------

    	// ----- ������ ������ ----------
    	final TextField tfBottom = new TextField();
    	Button btBottom = new Button("Test");
    	hbBottom.getChildren().addAll(tfBottom, btBottom);
    	btBottom.setOnAction(new EventHandler<ActionEvent>() {
    		@Override 
    		public void handle(ActionEvent e) {
    			curator.DigitMixer();
    			String s = "";
    			s = Arrays.toString(curator.RndDigits);
    			tfBottom.setText(s);
    		}
    	});
    	// ------------------------------

    	// ���������� ����� ������-����� � ��������� ������ ����������
    	df = new HashMap<Button, Integer>();
    	df.put(btUp1, 0);
    	df.put(btDown1, 0);
    	df.put(btUp2, 1);
    	df.put(btDown2, 1);
    	df.put(btUp3, 2);
    	df.put(btDown3, 2);
    	df.put(btUp4, 3);
    	df.put(btDown4, 3);
    	sUp = new HashSet<Button>();
    	sUp.add(btUp1);
    	sUp.add(btUp2);
    	sUp.add(btUp3);
    	sUp.add(btUp4);

    	primaryStage.setScene(scene); 
    	primaryStage.show();
    }

    void ShowNextShot(int trying) {
		String s = new String(Arrays.toString(Digits));
		s = Integer.toString(trying) + ": " + s;
		s = s + " -   " + Integer.toString(bulls) + " �, " +
		    Integer.toString(cows) + " �";
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		Text t = new Text(s);
		t.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		hb.getChildren().add(t);
		hb.setStyle("-fx-background-color: #33FFFF;");
		vbCenter.getChildren().add(hb);
		spCenter.setVvalue(spCenter.getVmax());
    }

    void ShowStepInfo(String s) {
    	HBox hb = new HBox();
    	hb.setAlignment(Pos.CENTER);
    	Text t = new Text(s);
    	t.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
    	hb.getChildren().add(t);
//		hb.setStyle("-fx-background-color: #336699;");
    	vbCenter.getChildren().add(hb);
    	spCenter.setVvalue(spCenter.getVmax());
    }

    // ========= ��������� ��������������� ������� ======
    void SelfAnswer() {	    // �������
    	curator.Init();
    	solver.Init(curator.RndAllDigits);
    	//curator.DigitMixer();			// ���������� ����� ����������� ����� ����

    	while(bulls + cows < 4) {		// ���� �� ������� ���� ����

    		Digits = solver.ToFindDigits(Digits);

    		CalcBullCow();		    // ����������
    		Integer[] TmpBufI = new Integer[4];
    		for(int i = 0; i < 4; i++) {TmpBufI[i] = Digits[i];}
    		solver.Shots_digits.add(TmpBufI);	    // ���������� ������� ������� ��������� ��������
    		solver.Shots_bulls.add(bulls);
    		solver.Shots_cows.add(cows);

    		ShowNextShot(solver.Shots_digits.size());	// �����������

    		solver.ShotDigitIndex = 0;		// ��������� ��������
    		solver.DigitsForAnswerIndex = 0;
    	}
    	while(bulls < 4) {
    		solver.ToFindBulls(Digits);

    		CalcBullCow();
    		Integer[] NextShot2 = new Integer[4];
    		NextShot2 = Digits.clone();
    		solver.Shots_digits.add(NextShot2);
    		solver.Shots_bulls.add(bulls);
    		solver.Shots_cows.add(cows);
    		ShowNextShot(solver.Shots_digits.size());	// �����������
    	}
    }
}

