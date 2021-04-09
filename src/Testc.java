
public class Testc {

	public static void main(String [] args) {
		
		Face f = FaceGenerator.generateFace(FaceID.FIVE, FacePosition.TOP_LEFT);
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.println(f.getCases()[i][j]);
			}
		}
	}
	
	
}
