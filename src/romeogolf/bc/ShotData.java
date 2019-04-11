package romeogolf.bc;

public class ShotData {
	private Integer bulls;
	private Integer cows;
	private Integer[] qwad = new Integer[4];

	public Integer getBulls(){
		return bulls;
	}

	public Integer getCows(){
		return cows;
	}

	public Integer[] getQwad(){
		return qwad;
	}

	public ShotData(Integer bulls, Integer cows, Integer[] qwad) {
		this.bulls = bulls;
		this.cows = cows;
		for(int i = 0; i < 4; i++){
			this.qwad[i] = qwad[i];
		}
	}

}
