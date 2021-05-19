import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class ImageRotator {


	public static BufferedImage rotateRight90(File input, File output) {

		BufferedImage rotated = null;

		try {
			ImageInputStream inputStrem = ImageIO.createImageInputStream(input);
			
			Iterator<ImageReader> iterator = ImageIO.getImageReaders(inputStrem);
			ImageReader reader = iterator.next();
			String format = reader.getFormatName();

			BufferedImage image = ImageIO.read(inputStrem);

			int width = image.getWidth();
			int height = image.getHeight();

			rotated = new BufferedImage(height, width, image.getType());


			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					rotated.setRGB((height-1) -y, x, image.getRGB(x, y));
				}
			}

			ImageIO.write(rotated, format, output);

		}catch (Exception e) {
			e.printStackTrace();
		}

		return rotated;
	}

	public static BufferedImage rotateRight180(File input, File output) {

		BufferedImage rotated = null;

		try {
			ImageInputStream inputStrem = ImageIO.createImageInputStream(input);

			Iterator<ImageReader> iterator = ImageIO.getImageReaders(inputStrem);
			ImageReader reader = iterator.next();
			String format = reader.getFormatName();

			BufferedImage image = ImageIO.read(inputStrem);

			int width = image.getWidth();
			int height = image.getHeight();

			rotated = new BufferedImage(width, height, image.getType());


			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					rotated.setRGB((width -1) -x, (height -1) -y, image.getRGB(x, y));
				}
			}

			ImageIO.write(rotated, format, output);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return rotated;
	}

	public static BufferedImage rotateRight270(File input, File output) {

		BufferedImage rotated = null;
		try {
			ImageInputStream inputStrem = ImageIO.createImageInputStream(input);
			
			Iterator<ImageReader> iterator = ImageIO.getImageReaders(inputStrem);
			ImageReader reader = iterator.next();
			String format = reader.getFormatName();

			BufferedImage image = ImageIO.read(inputStrem);

			int width = image.getWidth();
			int height = image.getHeight();

			rotated = new BufferedImage(height, width, image.getType());


			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					rotated.setRGB(y, (width -1) -x, image.getRGB(x, y));
				}
			}

			ImageIO.write(rotated, format, output);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return rotated;
	}

	public static BufferedImage rotate0(File input, File output) {
		
		try {
			InputStream in = new BufferedInputStream( new FileInputStream(input));	
			OutputStream out = new BufferedOutputStream( new FileOutputStream(output)); 

			byte[] buffer = new byte[1024];
			int lengthRead;

			while ((lengthRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, lengthRead);
			}

			in.close();
			out.close();

			return ((BufferedImage)  ImageIO.read(output));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static BufferedImage rotate(File input, File output, RotationDegree degree) {

		switch (degree) {
		case ZERO:
			return rotate0(input, output);
		case CIRCLE_QUATER: {
			return rotateRight90(input, output);
			//break;
		}
		case CIRCLE_HALF: {
			return rotateRight180(input, output);
		}
		case THREE_CIRCLE_QUATER: {
			return rotateRight270(input, output);
		}
		}
		return null;
	}
}
