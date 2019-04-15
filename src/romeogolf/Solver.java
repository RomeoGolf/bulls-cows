package romeogolf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Solver {
    ArrayList<Integer[]> Shots_digits = new ArrayList<Integer[]>();	// ������� ���� �������
    ArrayList<Integer> Shots_bulls = new ArrayList<Integer>();		// ���� �������
    ArrayList<Integer> Shots_cows = new ArrayList<Integer>();		// ������ �������
    ArrayList<Integer> DigitsForAnswer = new ArrayList<Integer>();	// ����� ���� ��� �������
    Integer[] NextShot;					// ������ ��������� �������
    Integer[] ShotDigitInDigits_index = new Integer[4];	// ������ ����� ������� � ������ ����
    
	int ShotDigitIndex = 0;		// ������ ������� ���� ��������� �������
	int DigitsForAnswerIndex = 0;	// ������ � ������ ����
	
    // �������� ������������ ���������� �� ����������� ���������� �������
    boolean IsSuitable(Integer[] a, int length) {
    	int BullCow = 0;	// ����� ����� � ����� �������
    	int Intersection = 0;	// �������� ����������� ���� ������ �������
				//      � ���� ���������� ��������� �������
    	for (int i = 0; i <= (Shots_digits.size() - 1); i++) {
    		BullCow = Shots_bulls.get(i) + Shots_cows.get(i);
    		for (int j = 0; j <= 3; j++) {	// ������� �������� �����������
    			for (int k = 0; k <= length; k++) {
    				if (a[k] == Shots_digits.get(i)[j]){Intersection++;}
    			}
    		}
    		// ���� ����������� ������ ����� ��������� - ���� ������ � �������
    		if (Intersection > BullCow) {return false;}
    		Intersection = 0;
    	}
    	return true;
    }

    // ����� � ������� ������� ������� ��� �������� �� ������������ �����
    ArrayList<Integer> ShotDigits = new ArrayList<Integer>();
    ArrayList<Integer> Indexes = new ArrayList<Integer>();

    // �������� �� ������������ ���������� �����
    boolean IsSuitableBulls(int Max) {
    	for (int i = 0; i <= (Shots_digits.size() - 1); i++) {
    		int coincidence = 0;
    		for(int j = 0; j <= Max; j++) {
    			if (ShotDigits.get(j) == Shots_digits.get(i)[Indexes.get(j)]) {coincidence++;}
    		}
    		if (coincidence > Shots_bulls.get(i)) {return false;}
    	}
    	return true;
    }

    // �������� ����������� �������� � ����������
    boolean IsIndexValid(int max) {
    	Set<Integer> Ind = new HashSet<Integer>();
    	for(int i = 0; i < max; i++) {Ind.add(Indexes.get(i));}
    	while(Indexes.get(max) < 4) {
    		if(Ind.contains(Indexes.get(max))) {
    			Indexes.set(max, Indexes.get(max) + 1);
    			continue;
    		} else {
    			return true;
    		}
    	}
    	return false;
    }

    // ������� ����������� �����
    boolean TrySetBulls() {
    	int i = 0;
    	Indexes.set(i, -1);
    	while (i < 4) {
    		Indexes.set(i, Indexes.get(i) + 1);
    		if(IsIndexValid(i)) {
    			if(IsSuitableBulls(i)) {
    				i++;
    				if(i > 3) {break;}
    				Indexes.set(i, -1);
    				continue;
    			} else {
    				continue;
    			}
    		} else {
    			i--;
    			if(i < 0) {
    				// error
    				return false;
    			} else {
    				continue;
    			}
    		}
    	}
    	return true;
    }

    public Integer[] ToFindDigits(Integer[] Dgt){
		NextShot = new Integer[4];		// ������������ ��������� �������
		while (ShotDigitIndex < 4) {
			// ����������� ��������� �����
			NextShot[ShotDigitIndex] = DigitsForAnswer.get(DigitsForAnswerIndex);
			// ����������� ������� ����� � ������
			ShotDigitInDigits_index[ShotDigitIndex] = DigitsForAnswerIndex;
			// �������� ���������� ���������� ������� �� ������������
			if (!IsSuitable(NextShot, ShotDigitIndex)) {
				// ���� ��������� ����� �� ������� - ����� ���������
				DigitsForAnswerIndex++;
				// �������� ������ �� ������� ������
				if (DigitsForAnswerIndex > (DigitsForAnswer.size() - 1)) {
					// ���� ����� - ������� � ������� �� ���� ����� �����
					ShotDigitIndex--;
					// ���� ������� ����� - ������ � ����� � �������
					if (ShotDigitIndex < 0) {
						// ���� ������ � ���������� ����� ����� � ������� // Info("Error");
						return Dgt;
					}
					// ��� ������� �������� ������� ����� ��������� ����� �� ������
					DigitsForAnswerIndex = ShotDigitInDigits_index[ShotDigitIndex] + 1;
				}
				// ���� ��������� � �������� �������� - ������� ����� ����� �� �����,
				if (ShotDigitIndex == 0) {
					DigitsForAnswer.remove(0);	// �� ����� �������� �� ������
					ShotDigitIndex = 0;		// � �������� �������
					DigitsForAnswerIndex = 0;
				}
				continue;
			}
			ShotDigitIndex++;	    // ������� � ���������� �������� �������
			DigitsForAnswerIndex++;	    // � ��������� ����� ������
			// ���� ����� �� ������� ������, ����� ������ ��� �� ��������
			if ((DigitsForAnswerIndex > DigitsForAnswer.size() - 1) &&
					(ShotDigitIndex <= 3)) {
				ShotDigitIndex--;	    // ���� ����� ��������� �����
				ShotDigitIndex--;
				if (ShotDigitIndex < 0) {
					// ���� ������ � ����� ���������� ����� � ������� // Info("Error");
					return Dgt;
				}
				// � ���������� ������ ����� �� ������� �����
				DigitsForAnswerIndex = ShotDigitInDigits_index[ShotDigitIndex] + 1;
				if (ShotDigitIndex == 0) {
					DigitsForAnswer.remove(0);
					ShotDigitIndex = 0;
					DigitsForAnswerIndex = 0;
				}
				continue;
			}
		}
		
		for(int i = 0; i < 4; i++) {ShotDigits.set(i, NextShot[i]);}
		for(int i = 0; i < 4; i++) {Indexes.set(i, -1);}

		if(TrySetBulls()) {
			for(int n = 0; n < 4; n++) {
				Dgt[Indexes.get(n)] = ShotDigits.get(n);
			}
		} else {
			// ���� ������������ ������� ����� � ������ // ShowStepInfo("bull error");
			Dgt = NextShot.clone();
		}
		return Dgt;
    }

    public Integer[] ToFindBulls(Integer[] Dgt){
		for(int i = 0; i < 4; i++) {ShotDigits.set(i, NextShot[i]);}
		for(int i = 0; i < 4; i++) {Indexes.set(i, -1);}

		if(TrySetBulls()) {
			for(int n = 0; n < 4; n++) {
				Dgt[Indexes.get(n)] = ShotDigits.get(n);
			}
		} else {
			// ���� ������������ ������� ����� � ������
			// ShowStepInfo("bull error");
			Dgt = NextShot.clone();
		}
		return Dgt;
    }

    public void Init(Integer[] InitArray){
    	Indexes.clear();
    	for(int i = 0; i < 4; i++) {Indexes.add(i);}
    	ShotDigits.clear();
    	ShotDigits.addAll(Indexes);
    	ShotDigitIndex = 0;		// ������ ������� ���� ��������� �������
    	DigitsForAnswerIndex = 0;	// ������ � ������ ����

    	for(int i = 0; i < 10; i++) {
    		DigitsForAnswer.add(InitArray[i]);
    	}
    }

	public Solver() {


	}

}
