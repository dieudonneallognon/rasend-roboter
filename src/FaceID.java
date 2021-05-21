
public enum FaceID {
	ONE(1), TWO(2), TRHEE(3), FOUR(4),
	FIVE(5), SIX(6), SEVEN(7),EIGHT(8);
	
	private int id;
	
	private FaceID(int id) {
		this.id = id;
	}
	
	public static FaceID intToID(int i) {	
		return FaceID.values()[i-1];
	}
	
	public FaceID linked() {
		
		return (id % 2 == 0) 
			? FaceID.values()[id-1-1]
			: FaceID.values()[id-1+1];
	}
	
	public int value() {
		return id;
	}
}
