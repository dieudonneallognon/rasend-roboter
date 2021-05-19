import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardGenerator {

	public static final String BOARD_IMGS_DIR = "assets/board_images/";
	public static final String GAME_BOARD_IMGS_DIR = "assets/game_board/";

	public static volatile boolean isReady = false;
	
	private static final int MINI_BOARD_IMAGE_NBR = 4;
	private static final int MINI_BOARD_IMAGE_WITH_POS_NBR = MINI_BOARD_IMAGE_NBR * 2;

	public static synchronized List<BufferedImage> generateImages() {

		List<BufferedImage> images = new ArrayList<BufferedImage>(); 

		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {


				Random rand = new Random();

				List<FaceID> imageIds= new ArrayList<FaceID>();

				ResourceLoarder boardImageLoader = new ResourceLoarder(BOARD_IMGS_DIR);

				while (imageIds.size() < MINI_BOARD_IMAGE_NBR) {

					FaceID id = FaceID.intToID(rand.nextInt((MINI_BOARD_IMAGE_WITH_POS_NBR - 1) + 1) +1);

					if (!(imageIds.contains(id) || imageIds.contains(id.linked()))) {
						imageIds.add(id);
					}
				}
				
				for (FaceID faceID : imageIds) {
					
					System.out.println(faceID);
				}

				Board.setFaceIds(imageIds);

				synchronized (images) {
					for (int i = 0, length = imageIds.size(); i < length; i++) {

						images.add(ImageRotator.rotate(
								boardImageLoader.getResource(imageIds.get(i).value()+".jpg"),
								new File(GAME_BOARD_IMGS_DIR+(i+1)+".jpg").getAbsoluteFile(),
								RotationDegree.intToRotationDegree(i)));
					}

					isReady = true;
				}
			}
		});

		th.start();

		return images;
	}
}
