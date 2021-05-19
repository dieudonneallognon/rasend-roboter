import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Testc {

	public static void main(String [] args) {
		
		//BoardGenerator.generateImages();
		
		System.out.println(new File(BoardGenerator.BOARD_IMGS_DIR+"1.jpg"));
		
		try {
			ImageIO.createImageInputStream(new File(BoardGenerator.BOARD_IMGS_DIR+"1.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
