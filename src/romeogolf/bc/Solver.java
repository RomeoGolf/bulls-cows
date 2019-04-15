package romeogolf.bc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Solver {
	// ������ ������ �������
	private ArrayList<ShotData> shots_data = new ArrayList<ShotData>();
	public void addShotData(ShotData sd){
		this.shots_data.add(sd);
	}
	public void clearShotsData(){
		this.shots_data.clear();
	}
	public Integer getNumberOfShots(){
		return this.shots_data.size();
	}
	// ����� ���� ��� �������
	private ArrayList<Integer> digitsForAnswer = new ArrayList<Integer>();
	private Integer[] nextShot;					// ������ ��������� �������
    // ������ ����� ������� � ������ ����
	private Integer[] shotDigitInDigitsIndex = new Integer[4];

    // �������� ������������ ���������� �� ����������� ���������� �������
	private boolean isSuitable(Integer[] a, int length){
    	int BullCow = 0;	// ����� ����� � ����� �������
    	for (int i = 0; i <= (shots_data.size() - 1); i++){
    		int Intersection = 0;	// �������� ����������� ���� ������ �������
				//      � ���� ���������� ��������� �������
    		BullCow = shots_data.get(i).getBulls()
    											+ shots_data.get(i).getCows();
    		for (int j = 0; j <= 3; j++){	// ������� �������� �����������
    			for (int k = 0; k <= length; k++){
    				if (a[k] == shots_data.get(i).getQwad()[j]){
    					Intersection++;
    				}
    			}
    		}
    		// ���� ����������� ������ ����� ��������� - ���� ������ � �������
    		if (Intersection > BullCow) {
    			return false;
    		}
    	}
    	return true;
    }

    // ����� � ������� ������� ������� ��� �������� �� ������������ �����
    private ArrayList<Integer> shotDigits = new ArrayList<Integer>();
    private ArrayList<Integer> indices = new ArrayList<Integer>();

    // �������� �� ������������ ���������� �����
    private boolean isSuitableBulls(int Max){
    	for (int i = 0; i <= (shots_data.size() - 1); i++){
    		int coincidence = 0;
    		for(int j = 0; j <= Max; j++){
    			if (shotDigits.get(j) ==
    			shots_data.get(i).getQwad()[indices.get(j)]){
    				coincidence++;
    			}
    		}
    		if (coincidence > shots_data.get(i).getBulls()){
    			return false;
    		}
    	}
    	return true;
    }

    // �������� ����������� �������� � ����������
    private boolean isIndexValid(int max){
    	Set<Integer> Ind = new HashSet<Integer>();
    	for(int i = 0; i < max; i++) {
    		Ind.add(indices.get(i));
    	}
    	while(indices.get(max) < 4){
    		if(Ind.contains(indices.get(max))){
    			indices.set(max, indices.get(max) + 1);
    			continue;
    		} else {
    			return true;
    		}
    	}
    	return false;
    }

    // ������� ����������� �����
    private boolean trySetBulls(){
    	int i = 0;
    	indices.set(i, -1);
    	while(i < 4){
    		indices.set(i, indices.get(i) + 1);
    		if(isIndexValid(i)){
    			if(isSuitableBulls(i)){
    				i++;
    				if(i > 3){
    					break;
    				}
    				indices.set(i, -1);
    				continue;
    			} else {
    				continue;
    			}
    		} else {
    			i--;
    			if(i < 0){
    				// error
    				return false;
    			} else {
    				continue;
    			}
    		}
    	}
    	return true;
    }

    // ��� ������� � � � � ����� ���������� null, ��� ����� ������� ����������
    public Integer[] toFindDigits(){
		nextShot = new Integer[4];		// ������������ ��������� �������
		int ShotDigitIndex = 0;			// ������ ������� ���� ��������� �������
		int DigitsForAnswerIndex = 0;	// ������ � ������ ����
		while (ShotDigitIndex < 4){
			// ����������� ��������� �����
			nextShot[ShotDigitIndex] = 
									digitsForAnswer.get(DigitsForAnswerIndex);
			// ����������� ������� ����� � ������
			shotDigitInDigitsIndex[ShotDigitIndex] = DigitsForAnswerIndex;
			// �������� ���������� ���������� ������� �� ������������
			if (!isSuitable(nextShot, ShotDigitIndex)){
				// ���� ��������� ����� �� ������� - ����� ���������
				DigitsForAnswerIndex++;
				// �������� ������ �� ������� ������
				if (DigitsForAnswerIndex > (digitsForAnswer.size() - 1)){
					// ���� ����� - ������� � ������� �� ���� ����� �����
					ShotDigitIndex--;
					// ���� ������� ����� - ������ � ����� � �������
					if (ShotDigitIndex < 0){
						// ���� ������ � ���������� ����� ����� � ������� 
						// Info("Error");
						return null;
					}
					// ��� ������� �������� ������� ����� ��������� ����� 
					//   �� ������
					DigitsForAnswerIndex = 
									shotDigitInDigitsIndex[ShotDigitIndex] + 1;
				}
				// ���� ��������� � �������� �������� - 
				//   ������� ����� ����� �� �����,
				if (ShotDigitIndex == 0){
					digitsForAnswer.remove(0);	// �� ����� �������� �� ������
					DigitsForAnswerIndex = 0;	// � �������� �������
				}
				continue;
			}
			DigitsForAnswerIndex++;	    // ������� ��������� ����� ������
			// ���� ����� �� ������� ������, ����� ��� �������� ����� ������
			if ((DigitsForAnswerIndex > digitsForAnswer.size() - 1) &&
					(ShotDigitIndex < 3)){
				ShotDigitIndex--;	    // ���� ����� ��������� �����
				if (ShotDigitIndex < 0){
					// ���� ������ � ����� ���������� ����� � ������� 
					// Info("Error");
					return null;
				}
				// � ���������� ������ ����� �� ������� �����
				DigitsForAnswerIndex = 
									shotDigitInDigitsIndex[ShotDigitIndex] + 1;
				if (ShotDigitIndex == 0){
					digitsForAnswer.remove(0);
					DigitsForAnswerIndex = 0;
				}
				continue;
			}
			ShotDigitIndex++;	    // ������� � ���������� �������� �������
		}

		for(int i = 0; i < 4; i++){
			shotDigits.set(i, nextShot[i]);
		}
		for(int i = 0; i < 4; i++){
			indices.set(i, -1);
		}

		Integer[] Dgt;
		if(trySetBulls()){
			Dgt = new Integer[4];
			for(int n = 0; n < 4; n++){
				Dgt[indices.get(n)] = shotDigits.get(n);
			}
		} else {
			// ���� ������������ ������� ����� � ������ 
			// ShowStepInfo("bull error");
			Dgt = nextShot.clone();
		}
		return Dgt;
    }

    public Integer[] toFindBulls(Integer[] Dgt){
		for(int i = 0; i < 4; i++){
			shotDigits.set(i, nextShot[i]);
		}
		for(int i = 0; i < 4; i++){
			indices.set(i, -1);
		}

		if(trySetBulls()){
			for(int n = 0; n < 4; n++){
				Dgt[indices.get(n)] = shotDigits.get(n);
			}
		} else {
			// ���� ������������ ������� ����� � ������
			// ShowStepInfo("bull error");
			Dgt = nextShot.clone();
		}
		return Dgt;
    }

    public void Init(Integer[] InitArray){
    	this.shots_data.clear();
    	indices.clear();
    	for(int i = 0; i < 4; i++){
    		indices.add(i);
    	}
    	shotDigits.clear();
    	shotDigits.addAll(indices);

    	digitsForAnswer.clear();
    	for(int i = 0; i < 10; i++){
    		digitsForAnswer.add(InitArray[i]);
    	}
    }
}