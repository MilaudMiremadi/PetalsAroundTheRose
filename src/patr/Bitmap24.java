package patr;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * Represents an RGB bitmap. Images read from a file are purposely converted to RGB
 * to get rid of their alpha channels.
 * 
 * @author Milaud Miremadi
 *
 */
public class Bitmap24 {

	int[] pixels;
	int width;
	int height;

	public Bitmap24(int w, int h) {
		width = w;
		height = h;
		pixels = new int[w * h];
	}

	public Bitmap24(int w, int h, int[] memory) {
		width = w;
		height = h;
		pixels = memory;
	}

	public static Bitmap24 load_from_stream(InputStream in) {
		try {
			BufferedImage image = ImageIO.read(in);
			int type = image.getType();
			if (type != BufferedImage.TYPE_INT_RGB) {
				BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = newImage.createGraphics();
				g2.drawImage(image, null, 0, 0);
				image = newImage;
			}
			return new Bitmap24(image.getWidth(), image.getHeight(), ((DataBufferInt) image.getRaster().getDataBuffer()).getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}