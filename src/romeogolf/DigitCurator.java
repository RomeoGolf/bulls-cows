package romeogolf;

import java.util.Random;

public class DigitCurator {
    public Integer[] RndDigits = new Integer[4];		// "����������" �����
    Random rg = new Random(System.currentTimeMillis());	// ��������� ���, ���������������� ��������
    public Integer[] RndAllDigits = new Integer[10];		// ��� ������������� ������� ����

    public void DigitMixer() {		// ������������� ������� ����
    	for(int j = rg.nextInt(10) + 3; j > 0; j--) {
    		int i = 9;
    		int n, buf;
    		while(i > 0){
    			n = rg.nextInt(i);
    			buf = RndAllDigits[n];
    			RndAllDigits[n] = RndAllDigits[9];
    			RndAllDigits[9] = buf;
    			i--;
    		}
    	}
    };

    public void MakeRndDigits() {
    	int n = rg.nextInt(5);
    	for(int i = 0; i < 4; i++) {
    		RndDigits[i] = RndAllDigits[i + n];
    	}
    };

    public void Init(){
    	this.DigitMixer();
    	this.MakeRndDigits();
    }

	public DigitCurator() {
    	for(int i = 0; i <= 9; i++){
    		RndAllDigits[i] = i;
    	}
	}

}
