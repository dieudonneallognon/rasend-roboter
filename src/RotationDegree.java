
public enum RotationDegree {
	
	ZERO(0),
	CIRCLE_QUATER(90),
	CIRCLE_HALF(180),
	THREE_CIRCLE_QUATER(270);
	
	
	private int degree;
	
	RotationDegree(int degree) {
		this.degree = degree;
	}
	
	public static RotationDegree intToRotationDegree(int i) {
		return values()[i];
	}
	
}
