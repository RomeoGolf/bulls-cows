package romeogolf.bc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class Solver {
    // массив данных попыток
    private final ArrayList<ShotData> shots_data = new ArrayList<>();
    void addShotData(ShotData sd){
        this.shots_data.add(sd);
    }
    void clearShotsData(){
        this.shots_data.clear();
    }
    Integer getNumberOfShots(){
        return this.shots_data.size();
    }
    // набор цифр для отгадки
    private final ArrayList<Integer> digitsForAnswer = new ArrayList<>();
    private Integer[] nextShot;                 // массив очередной попытки
    // индекс цифры попытки в наборе цифр
    private final Integer[] shotDigitInDigitsIndex = new Integer[4];

    // проверка допустимости подмассива по результатам предыдущих попыток
    private boolean isSuitable(Integer[] a, int length){
        int BullCow;    // сумма быков и коров попытки
        for (int i = 0; i <= (shots_data.size() - 1); i++){
            int Intersection = 0;   // мощность пересечения цифр старой попытки
                //      и цифр подмассива очередной попытки
            BullCow = shots_data.get(i).getBulls() + shots_data.get(i).getCows();
            for (int j = 0; j <= 3; j++){   // подсчет мощности пересечения
                for (int k = 0; k <= length; k++){
                    if (a[k].equals(shots_data.get(i).getQwad()[j])){
                        Intersection++;
                    }
                }
            }
            // если пересечение больше числа угаданных - есть лишнее в отгадке
            if (Intersection > BullCow) {
                return false;
            }
        }
        return true;
    }

    // Цифры и индексы текущей попытки для проверки на соответствие быкам
    private final ArrayList<Integer> shotDigits = new ArrayList<>();
    private final ArrayList<Integer> indices = new ArrayList<>();

    // проверка на допустимость подмассива быков
    private boolean isSuitableBulls(int Max){
        for (int i = 0; i <= (shots_data.size() - 1); i++){
            int coincidence = 0;
            for(int j = 0; j <= Max; j++){
                if (shotDigits.get(j).equals(shots_data.get(i).getQwad()[indices.get(j)])){
                    coincidence++;
                }
            }
            if (coincidence > shots_data.get(i).getBulls()){
                return false;
            }
        }
        return true;
    }

    // Проверка пересечения индексов с коррекцией
    private boolean isIndexValid(int max){
        Set<Integer> Ind = new HashSet<>();
        for(int i = 0; i < max; i++) {
            Ind.add(indices.get(i));
        }
        while(indices.get(max) < 4){
            if(Ind.contains(indices.get(max))){
                indices.set(max, indices.get(max) + 1);
            } else {
                return true;
            }
        }
        return false;
    }

    // попытка расстановки быков
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
                }
            } else {
                i--;
                if(i < 0){
                    // error
                    return false;
                }
            }
        }
        return true;
    }

    // при ошибках в Б и К будет возвращать null, что может вызвать исключение
    Integer[] toFindDigits(){
        nextShot = new Integer[4];      // формирование очередной попытки
        int ShotDigitIndex = 0;         // индекс массива цифр очередной попытки
        int DigitsForAnswerIndex = 0;   // индекс в наборе цифр
        while (ShotDigitIndex < 4){
            // подстановка очередной цифры
            nextShot[ShotDigitIndex] = 
                                    digitsForAnswer.get(DigitsForAnswerIndex);
            // запоминание индекса цифры в наборе
            shotDigitInDigitsIndex[ShotDigitIndex] = DigitsForAnswerIndex;
            // проверка набранного подмассива попытки на допустимость
            if (!isSuitable(nextShot, ShotDigitIndex)){
                // если очередная цифра не подошла - берем следующую
                DigitsForAnswerIndex++;
                // проверка выхода за пределы набора
                if (DigitsForAnswerIndex > (digitsForAnswer.size() - 1)){
                    // если вышли - возврат в попытке на одну цифру назад
                    ShotDigitIndex--;
                    // если слишком назад - ошибка в быках и коровах
                    if (ShotDigitIndex < 0){
                        // есть ошибка в переданных ранее быках и коровах 
                        // Info("Error");
                        return null;
                    }
                    // для первого элемента отгадки берем следующую цифру 
                    //   из набора
                    DigitsForAnswerIndex = 
                                    shotDigitInDigitsIndex[ShotDigitIndex] + 1;
                }
                // если вернулись к младшему элементу - 
                //   младшая цифра точно не верна,
                if (ShotDigitIndex == 0){
                    digitsForAnswer.remove(0);  // ее нужно выкинуть из набора
                    DigitsForAnswerIndex = 0;   // и обнулить индексы
                }
                continue;
            }
            DigitsForAnswerIndex++;     // переход следующей цифре набора
            // если вышли за пределы набора, когда еще осталась цифра набора
            if ((DigitsForAnswerIndex > digitsForAnswer.size() - 1) &&
                    (ShotDigitIndex < 3)){
                ShotDigitIndex--;       // надо опять вернуться назад
                if (ShotDigitIndex < 0){
                    // есть ошибка в ранее переданных быках и коровах 
                    // Info("Error");
                    return null;
                }
                // и подставить другую цифру на спорное место
                DigitsForAnswerIndex = 
                                    shotDigitInDigitsIndex[ShotDigitIndex] + 1;
                if (ShotDigitIndex == 0){
                    digitsForAnswer.remove(0);
                    DigitsForAnswerIndex = 0;
                }
                continue;
            }
            ShotDigitIndex++;       // переход к следующему элементу отгадки
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
            // есть несочетаемый вариант быков в цифрах 
            // ShowStepInfo("bull error");
            Dgt = nextShot.clone();
        }
        return Dgt;
    }

    void toFindBulls(Integer[] Dgt){
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
            // есть несочетаемый вариант быков в цифрах
            // ShowStepInfo("bull error");
            Dgt = nextShot.clone();
        }
    }

    void Init(Integer[] InitArray){
        this.shots_data.clear();
        indices.clear();
        for(int i = 0; i < 4; i++){
            indices.add(i);
        }
        shotDigits.clear();
        shotDigits.addAll(indices);

        digitsForAnswer.clear();
        digitsForAnswer.addAll(Arrays.asList(InitArray).subList(0, 10));
    }
}
