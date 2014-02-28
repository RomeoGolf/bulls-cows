package romeogolf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Solver {
	ArrayList<ShotData> shots_data = new ArrayList<ShotData>(); // массив данных попыток
    ArrayList<Integer> digitsForAnswer = new ArrayList<Integer>();	// набор цифр для отгадки
    Integer[] nextShot;					// массив очередной попытки
    Integer[] shotDigitInDigitsIndex = new Integer[4];	// индекс цифры попытки в наборе цифр

    // проверка допустимости подмассива по результатам предыдущих попыток
    boolean isSuitable(Integer[] a, int length) {
    	int BullCow = 0;	// сумма быков и коров попытки
    	int Intersection = 0;	// мощность пересечения цифр старой попытки
				//      и цифр подмассива очередной попытки
    	for (int i = 0; i <= (shots_data.size() - 1); i++) {
    		BullCow = shots_data.get(i).getBulls() + shots_data.get(i).getCows();
    		for (int j = 0; j <= 3; j++) {	// подсчет мощности пересечения
    			for (int k = 0; k <= length; k++) {
    				if (a[k] == shots_data.get(i).getQwad()[j]){Intersection++;}
    			}
    		}
    		// если пересечение больше числа угаданных - есть лишнее в отгадке
    		if (Intersection > BullCow) {return false;}
    		Intersection = 0;
    	}
    	return true;
    }

    // Цифры и индексы текущей попытки для проверки на соответствие быкам
    ArrayList<Integer> shotDigits = new ArrayList<Integer>();
    ArrayList<Integer> indices = new ArrayList<Integer>();

    // проверка на допустимость подмассива быков
    boolean isSuitableBulls(int Max) {
    	for (int i = 0; i <= (shots_data.size() - 1); i++) {
    		int coincidence = 0;
    		for(int j = 0; j <= Max; j++) {
    			if (shotDigits.get(j) == shots_data.get(i).getQwad()[indices.get(j)]) {coincidence++;}
    		}
    		if (coincidence > shots_data.get(i).getBulls()) {return false;}
    	}
    	return true;
    }

    // Проверка пересечения индексов с коррекцией
    boolean isIndexValid(int max) {
    	Set<Integer> Ind = new HashSet<Integer>();
    	for(int i = 0; i < max; i++) {Ind.add(indices.get(i));}
    	while(indices.get(max) < 4) {
    		if(Ind.contains(indices.get(max))) {
    			indices.set(max, indices.get(max) + 1);
    			continue;
    		} else {
    			return true;
    		}
    	}
    	return false;
    }

    // попытка расстановки быков
    boolean trySetBulls() {
    	int i = 0;
    	indices.set(i, -1);
    	while (i < 4) {
    		indices.set(i, indices.get(i) + 1);
    		if(isIndexValid(i)) {
    			if(isSuitableBulls(i)) {
    				i++;
    				if(i > 3) {break;}
    				indices.set(i, -1);
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

    public Integer[] toFindDigits(Integer[] Dgt){
		nextShot = new Integer[4];		// формирование очередной попытки
		int ShotDigitIndex = 0;			// индекс массива цифр очередной попытки
		int DigitsForAnswerIndex = 0;	// индекс в наборе цифр
		while (ShotDigitIndex < 4) {
			// подстановка очередной цифры
			nextShot[ShotDigitIndex] = digitsForAnswer.get(DigitsForAnswerIndex);
			// запоминание индекса цифры в наборе
			shotDigitInDigitsIndex[ShotDigitIndex] = DigitsForAnswerIndex;
			// проверка набранного подмассива попытки на допустимость
			if (!isSuitable(nextShot, ShotDigitIndex)) {
				// если очередная цифра не подошла - берем следующую
				DigitsForAnswerIndex++;
				// проверка выхода за пределы набора
				if (DigitsForAnswerIndex > (digitsForAnswer.size() - 1)) {
					// если вышли - возврат в попытке на одну цифру назад
					ShotDigitIndex--;
					// если слишком назад - ошибка в быках и коровах
					if (ShotDigitIndex < 0) {
						// есть ошибка в переданных ранее быках и коровах // Info("Error");
						return Dgt;
					}
					// для первого элемента отгадки берем слеюующую цифру из набора
					DigitsForAnswerIndex = shotDigitInDigitsIndex[ShotDigitIndex] + 1;
				}
				// если вернулись к младшему элементу - младшая цифра точно не верна,
				if (ShotDigitIndex == 0) {
					digitsForAnswer.remove(0);	// ее нужно выкинуть из набора
					DigitsForAnswerIndex = 0;	// и обнулить индексы
				}
				continue;
			}
			DigitsForAnswerIndex++;	    // переход следующей цифре набора
			// если вышли за пределы набора, когда еще осталась цифра набора
			if ((DigitsForAnswerIndex > digitsForAnswer.size() - 1) &&
					(ShotDigitIndex < 3)) {
				ShotDigitIndex--;	    // надо опять вернуться назад
				if (ShotDigitIndex < 0) {
					// есть ошибка в ранее переданных быках и коровах // Info("Error");
					return Dgt;
				}
				// и подставить другую цифру на спорное место
				DigitsForAnswerIndex = shotDigitInDigitsIndex[ShotDigitIndex] + 1;
				if (ShotDigitIndex == 0) {
					digitsForAnswer.remove(0);
					DigitsForAnswerIndex = 0;
				}
				continue;
			}
			ShotDigitIndex++;	    // переход к следующему элементу отгадки
		}
		
		for(int i = 0; i < 4; i++) {shotDigits.set(i, nextShot[i]);}
		for(int i = 0; i < 4; i++) {indices.set(i, -1);}

		if(trySetBulls()) {
			for(int n = 0; n < 4; n++) {
				Dgt[indices.get(n)] = shotDigits.get(n);
			}
		} else {
			// есть несочетаемый вариант быков в цифрах // ShowStepInfo("bull error");
			Dgt = nextShot.clone();
		}
		return Dgt;
    }

    public Integer[] toFindBulls(Integer[] Dgt){
		for(int i = 0; i < 4; i++) {shotDigits.set(i, nextShot[i]);}
		for(int i = 0; i < 4; i++) {indices.set(i, -1);}

		if(trySetBulls()) {
			for(int n = 0; n < 4; n++) {
				Dgt[indices.get(n)] = shotDigits.get(n);
			}
		} else {
			// есть несочетаемый вариант быков в цифрах
			// ShowStepInfo("bull error");
			Dgt = nextShot.clone();
		}
		return Dgt;
    }

    public void Init(Integer[] InitArray){
    	this.shots_data.clear();
    	indices.clear();
    	for(int i = 0; i < 4; i++) {indices.add(i);}
    	shotDigits.clear();
    	shotDigits.addAll(indices);

    	digitsForAnswer.clear();
    	for(int i = 0; i < 10; i++) {
    		digitsForAnswer.add(InitArray[i]);
    	}
    }

	public Solver() {


	}

}
