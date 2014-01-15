package romeogolf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Solver {
    ArrayList<Integer[]> Shots_digits = new ArrayList<Integer[]>();	// массивы цифр попыток
    ArrayList<Integer> Shots_bulls = new ArrayList<Integer>();		// быки попыток
    ArrayList<Integer> Shots_cows = new ArrayList<Integer>();		// коровы попыток
    ArrayList<Integer> DigitsForAnswer = new ArrayList<Integer>();	// набор цифр для отгадки
    Integer[] NextShot;					// массив очередной попытки
    Integer[] ShotDigitInDigits_index = new Integer[4];	// индекс цифры попытки в наборе цифр
    
	int ShotDigitIndex = 0;		// индекс массива цифр очередной попытки
	int DigitsForAnswerIndex = 0;	// индекс в наборе цифр
	
    // проверка допустимости подмассива по результатам предыдущих попыток
    boolean IsSuitable(Integer[] a, int length) {
    	int BullCow = 0;	// сумма быков и коров попытки
    	int Intersection = 0;	// мощность пересечения цифр старой попытки
				//      и цифр подмассива очередной попытки
    	for (int i = 0; i <= (Shots_digits.size() - 1); i++) {
    		BullCow = Shots_bulls.get(i) + Shots_cows.get(i);
    		for (int j = 0; j <= 3; j++) {	// подсчет мощности пересечения
    			for (int k = 0; k <= length; k++) {
    				if (a[k] == Shots_digits.get(i)[j]){Intersection++;}
    			}
    		}
    		// если пересечение больше числа угаданных - есть лишнее в отгадке
    		if (Intersection > BullCow) {return false;}
    		Intersection = 0;
    	}
    	return true;
    }

    // Цифры и индексы текущей попытки для проверки на соответствие быкам
    ArrayList<Integer> ShotDigits = new ArrayList<Integer>();
    ArrayList<Integer> Indexes = new ArrayList<Integer>();

    // проверка на допустимость подмассива быков
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

    // Проверка пересечения индексов с коррекцией
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

    // попытка расстановки быков
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
		NextShot = new Integer[4];		// формирование очередной попытки
		while (ShotDigitIndex < 4) {
			// подстановка очередной цмфры
			NextShot[ShotDigitIndex] = DigitsForAnswer.get(DigitsForAnswerIndex);
			// запоминание индекса цифры в наборе
			ShotDigitInDigits_index[ShotDigitIndex] = DigitsForAnswerIndex;
			// проверка набранного подмассива попытки на допустимость
			if (!IsSuitable(NextShot, ShotDigitIndex)) {
				// если очередная цифра не подошла - берем следующую
				DigitsForAnswerIndex++;
				// проверка выхода за пределы набора
				if (DigitsForAnswerIndex > (DigitsForAnswer.size() - 1)) {
					// если вышли - возврат в попытке на одну цифру назад
					ShotDigitIndex--;
					// если слишком назад - ошибка в быках и коровах
					if (ShotDigitIndex < 0) {
						// есть ошибка в переданных ранее быках и коровах // Info("Error");
						return Dgt;
					}
					// для первого элемента отгадки берем слеюующую цифру из набора
					DigitsForAnswerIndex = ShotDigitInDigits_index[ShotDigitIndex] + 1;
				}
				// если вернулись к младшему элементу - младшая цифра точно не верна,
				if (ShotDigitIndex == 0) {
					DigitsForAnswer.remove(0);	// ее нужно выкинуть из набора
					ShotDigitIndex = 0;		// и обнулить индексы
					DigitsForAnswerIndex = 0;
				}
				continue;
			}
			ShotDigitIndex++;	    // переход к следующему элементу отгадки
			DigitsForAnswerIndex++;	    // и следующей цифре набора
			// если вышли за пределы набора, когда массив еще не кончился
			if ((DigitsForAnswerIndex > DigitsForAnswer.size() - 1) &&
					(ShotDigitIndex <= 3)) {
				ShotDigitIndex--;	    // надо опять вернуться назад
				ShotDigitIndex--;
				if (ShotDigitIndex < 0) {
					// есть ошибка в ранее переданных быках и коровах // Info("Error");
					return Dgt;
				}
				// и подставить другую цифру на спорное место
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
			// есть несочетаемый вариант быков в цифрах // ShowStepInfo("bull error");
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
			// есть несочетаемый вариант быков в цифрах
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
    	ShotDigitIndex = 0;		// индекс массива цифр очередной попытки
    	DigitsForAnswerIndex = 0;	// индекс в наборе цифр

    	for(int i = 0; i < 10; i++) {
    		DigitsForAnswer.add(InitArray[i]);
    	}
    }

	public Solver() {


	}

}
