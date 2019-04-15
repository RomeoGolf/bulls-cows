package romeogolf.bc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class DigitCurator {
    // "загаданные" цифры для первого игрока
    private Integer[] quad1 = new Integer[4];
    // "загаданные" цифры для второго игрока
    private Integer[] quad2 = new Integer[4];
    // Генератор ПСП, инициализируемый временем
    private final Random randomGenerator = new Random(System.currentTimeMillis());
    // для перемешанного массива цифр
    private Integer[] rndAllDigits = new Integer[10];
    Integer[] getDecade(){
        Integer[] Result = rndAllDigits.clone();
        int i, n, buf;
        i = 9;
        while(i > 0){
            n = randomGenerator.nextInt(i);
            buf = Result[n];
            Result[n] = Result[i];
            Result[i] = buf;
            i--;
        }
        rndAllDigits = Result.clone();
        return Result;
    }

    Boolean getRandomBool(){
        return randomGenerator.nextBoolean();
    }

    Integer[] getQuad(int num){
        if (num == 1){
            return quad1;
        }else{
            return quad2;
        }
    }

    void setQuad(Integer[] Quad, int num){
        if (num == 1){
            System.arraycopy(Quad, 0, this.quad1, 0, 4);
        }else{
            System.arraycopy(Quad, 0, this.quad2, 0, 4);
        }
    }

    void generateQuad(int num){
        this.getDecade();
        Integer[] Result = new Integer[4];
        int ind = randomGenerator.nextInt(6);
        System.arraycopy(this.rndAllDigits, ind, Result, 0, 4);

        if (num == 1){
            this.quad1 = Result.clone();
        }else{
            this.quad2 = Result.clone();
        }
    }

    void digitMixer() {      // перемешивание массива цифр
        for(int j = randomGenerator.nextInt(10) + 3; j > 0; j--) {
            int i = 9;
            int n, buf;
            while(i > 0){
                n = randomGenerator.nextInt(i);
                buf = rndAllDigits[n];
                rndAllDigits[n] = rndAllDigits[9];
                rndAllDigits[9] = buf;
                i--;
            }
        }
    }

    ShotData checkQuad(Integer[] quad, int num){
        Integer[] q;
        if (num == 1) {
            q = this.quad1.clone();
        }else{
            q = this.quad2.clone();
        }

        int bulls = 0;
        int cows = 0;
        for(int i = 0; i < 4; i++) {
            if (quad[i].equals(q[i])) {
                bulls++;
            }
        }
        Set<Integer> s = new HashSet<>(Arrays.asList(q).subList(0, 4));
        for(int i = 0; i < 4; i++) {
            if(s.contains(quad[i])){
                cows++;
            }
        }
        cows = cows - bulls;
        return new ShotData(bulls, cows, quad);
    }

    private void init(){
        this.digitMixer();
        this.generateQuad(1);
        this.digitMixer();
        this.generateQuad(2);
        this.digitMixer();
    }

    DigitCurator() {
        for(int i = 0; i <= 9; i++){
            rndAllDigits[i] = i;
        }
        this.init();
    }

}
