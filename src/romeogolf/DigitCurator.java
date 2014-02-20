package romeogolf;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DigitCurator {
    private Integer[] quad1 = new Integer[4];		// "загаданные" цифры для первого игрока
    private Integer[] quad2 = new Integer[4];		// "загаданные" цифры для второго игрока
    Random rg = new Random(System.currentTimeMillis());	// Генератор ПСП, инициализируемый временем
    private Integer[] RndAllDigits = new Integer[10];		// для перемешанного массива цифр
    public Integer[] getDecade(){
    	Integer[] Result = RndAllDigits.clone();
    	int i, n, buf;
    	i = 9;
    	while(i > 0){
    		n = rg.nextInt(i);
    		buf = Result[n];
    		Result[n] = Result[i];
    		Result[i] = buf;
    		i--;
    	}
    	RndAllDigits = Result.clone();
    	return Result;
    }

    public Boolean getRandomBool(){
    	return rg.nextBoolean();
    }

    public Integer[] getQuad(int num){
    	if (num == 1){
    		return quad1;
    	}else{
    		return quad2;
    	}
    }

    public void setQuad(Integer[] Quad, int num){
    	if (num == 1){
    		for(int i = 0; i < 4; i++){
    			this.quad1[i] = Quad[i];
    		}
    	}else{
    		for(int i = 0; i < 4; i++){
    			this.quad2[i] = Quad[i];
    		}
    	}
    }

    public void generateQuad(int num){
    	this.getDecade();
    	Integer[] Result = new Integer[4];
    	int ind = rg.nextInt(6);
    	for(int i = 0; i < 4; i++){
    		Result[i] = this.RndAllDigits[ind + i];
    	}

    	if (num == 1){
   			this.quad1 = Result.clone();
    	}else{
   			this.quad2 = Result.clone();
    	}
    }

    public void DigitMixer() {		// перемешивание массива цифр
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

    public ShotData checkQuad(Integer[] quad, int num){
    	Integer[] q;
    	if (num == 1) {
    		q = this.quad1.clone();
    	}else{
    		q = this.quad2.clone();
    	}

    	int bulls = 0;
    	int cows = 0;
    	for(int i = 0; i < 4; i++) {
    		if (quad[i] == q[i]) {
    			bulls++;
    		}
    	}
    	Set<Integer> s = new HashSet<Integer>();
    	for(int i = 0; i < 4; i++) {
    		s.add(q[i]);
    	}
    	for(int i = 0; i < 4; i++) {
    		if(s.contains(quad[i])){
    			cows++;
    		}
    	}
    	cows = cows - bulls;
    	return new ShotData(bulls, cows, quad);
    }

    public void Init(){
    	this.DigitMixer();
    	this.generateQuad(1);
    	this.DigitMixer();
    	this.generateQuad(2);
    	this.DigitMixer();
    }

	public DigitCurator() {
    	for(int i = 0; i <= 9; i++){
    		RndAllDigits[i] = i;
    	}
    	this.Init();
	}

}
