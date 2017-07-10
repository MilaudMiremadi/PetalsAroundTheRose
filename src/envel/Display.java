package envel;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Represents a simple software rendering display.
 * 
 * @author Milaud Miremadi
 *
 */
public final class Display {

	private BufferedImage image;
	public int[] memory;

	public int width;
	public int height;

	public Display(int w, int h) {
		width = w;
		height = h;

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		GraphicsConfiguration config = device.getDefaultConfiguration();

		image = config.createCompatibleImage(w, h);
		memory = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	public Image getImage() {
		return image;
	}

}