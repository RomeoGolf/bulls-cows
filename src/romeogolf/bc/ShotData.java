package romeogolf.bc;

class ShotData {
    private final Integer bulls;
    private final Integer cows;
    private final Integer[] qwad = new Integer[4];

    Integer getBulls(){
        return bulls;
    }

    Integer getCows(){
        return cows;
    }

    Integer[] getQwad(){
        return qwad;
    }

    ShotData(Integer bulls, Integer cows, Integer[] qwad) {
        this.bulls = bulls;
        this.cows = cows;
        System.arraycopy(qwad, 0, this.qwad, 0, 4);
    }

}
