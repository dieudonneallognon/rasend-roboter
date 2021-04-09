
public class BorderGenerator {
	
	
	public static void placeTopBorderOn(Case[][] tmp, int i, int j) {
		tmp[i][j].setTopBorder(true);
		
		if (i > 0) {
			tmp[i-1][j].setBottomBorder(true);			
		}
	}
	
	public static void placeBottomBorderOn(Case[][] tmp, int i, int j) {
		tmp[i][j].setBottomBorder(true);
		
		if (i < 7) {
			tmp[i+1][j].setTopBorder(true);			
		}
		
	}
	
	public static void placeLeftBorderOn(Case[][] tmp, int i, int j) {
		tmp[i][j].setLeftBorder(true);
		
		if (j > 0) {			
			tmp[i][j-1].setRightBorder(true);
		}
		
	}
	
	public static void placeRightBorderOn(Case[][] tmp, int i, int j) {
		tmp[i][j].setRightBorder(true);
		
		if (j < 7) {			
			tmp[i][j+1].setLeftBorder(true);
		}	
	}
	
	
}
