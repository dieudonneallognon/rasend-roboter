
public enum FacePosition {
	TOP_RIGHT(1),
	BOTTOM_RIGHT(2),
	BOTTOM_LEFT(2),
	TOP_LEFT(4);
	
	private int position;
	
	private FacePosition(int pos) {
		this.position = pos;
	}
	
	public static FacePosition intToFacePosition(int i) {
		return values()[i-1];
	}
	
	public int getPosition() {
		return position; 
	}
}
