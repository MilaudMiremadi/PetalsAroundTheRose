package patr;

/**
 * 2D software rendering routines.
 * 
 * @author Milaud Miremadi
 *
 */
public class Video {

	public static int width;
	public static int height;
	public static int[] memory;

	public static void init(int width, int height, int[] memory) {
		Video.width = width;
		Video.height = height;
		Video.memory = memory;
	}

	/**
	 * Reset screen to black
	 */
	public static void reset() {
		for (int i = 0; i < memory.length; i++) {
			memory[i] = 0;
		}
	}

	/**
	 * Blit an image to the display.
	 * 
	 * @param data the image data in A8_R8_G8_B8 format
	 * @param image_width width of the image
	 * @param image_height height of the image
	 * @param x_pos position on the raster to start drawing the top left corner of the image
	 * @param y_pos position on the raster to start drawing the top left corner of the image
	 */
	public static void blit_image(int[] data, int image_width, int image_height, int x_pos, int y_pos) {
		int start_x = CLAMP(x_pos, 0, width);
		int start_y = CLAMP(y_pos, 0, height);
		int end_x = CLAMP(x_pos + image_width, 0, width);
		int end_y = CLAMP(y_pos + image_height, 0, height);
		for (int y = start_y; y < end_y; y++) {
			int index = start_x + y * width;
			for (int x = start_x; x < end_x; x++) {
				int offset = (x - x_pos) + ((y - y_pos) * image_width);
				if (offset > 0) {
					memory[index] = data[offset];
				}
				index++;
			}
		}
	}

	/**
	 * Draws a wireframe rectangle
	 * @param x x coordinate of top left corner
	 * @param y y coordinate of top left corner
	 * @param w width of rectangle
	 * @param h height of rectangle
	 * @param color color of the rectangle
	 */
	public static void draw_rect(int x, int y, int w, int h, int color) {
		draw_horizontal_line(x, y, w, color);
		draw_horizontal_line(x, y + h - 1, w, color);
		draw_vertical_line(x, y, h, color);
		draw_vertical_line(x + w - 1, y, h, color);
	}

	/**
	 * Draws a filled rectangle
	 * @param x x coordinate of top left corner
	 * @param y y coordinate of top left corner
	 * @param w width of rectangle
	 * @param h height of rectangle
	 * @param color color of the rectangle
	 */
	public static void fill_rect(int x, int y, int w, int h, int color) {
		if (x < 0) {
			w += x;
			x = 0;
		}

		if (y < 0) {
			h += y;
			y = 0;
		}

		if (x + w > width) {
			w = width - x;
		}

		if (y + h > height) {
			h = height - y;
		}

		int step = width - w;
		int pos = x + y * width;

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				memory[pos++] = color;
			}
			pos += step;
		}
	}

	/**
	 * Draws a horizontal line
	 * @param x x coordinate of the left side of the line
	 * @param y y coordinate of the left side of the line
	 * @param w width of the line
	 * @param color color of the line
	 */
	public static void draw_horizontal_line(int x, int y, int w, int color) {
		if (y >= 0 && y < height) {
			if (x < 0) {
				w += x;
				x = 0;
			}

			if (x + w > width) {
				w = width - x;
			}

			int pos = x + y * width;

			for (int i = 0; i < w; i++) {
				memory[pos++] = color;
			}
		}
	}

	/**
	 * Draws a vertical line
	 * @param x x coordinate of the top of the line
	 * @param y y coordinate of the top of the line
	 * @param h height of the line
	 * @param color color of the line
	 */
	public static void draw_vertical_line(int x, int y, int h, int color) {
		if (x < 0 || x >= width) {
			return;
		}

		if (y < 0) {
			h += y;
			y = 0;
		}

		if (y + h > height) {
			h = height - y;
		}

		int pos = x + y * width;

		for (int i = 0; i < h; i++) {
			memory[pos] = color;
			pos += width;
		}
	}

	/**
	 * Pack separate RGB values into one int
	 * @param r red channel [0..255]
	 * @param g green channel [0..255]
	 * @param b blue channel [0..255]
	 * @return the packed int
	 */
	public static final int RGB_TO_I(int r, int g, int b) {
		return ((r << 16) | (g << 8) | b);
	}

	/**
	 * Clamp a value to the given bounds
	 * @param x the value
	 * @param min min value
	 * @param max max value
	 * @return the clamped value
	 */
	private static int CLAMP(int x, int min, int max) {
		return x < min ? min : x > max ? max : x;
	}

}