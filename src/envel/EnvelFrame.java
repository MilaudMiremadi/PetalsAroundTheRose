package envel;

import java.awt.Frame;
import java.awt.Graphics;

/**
 * Simple Java Frame with a 4 pixel border around it.
 * 
 * @author Milaud Miremadi
 *
 */
public final class EnvelFrame extends Frame {

	private static final long serialVersionUID = 8319938748238761982L;

	public static final int DECORATION_THICKNESS = 22;

	public static final int BORDER_SIZE = 4;

	public static final int BORDER_SIZE_DOUBLE = BORDER_SIZE << 1;

	public static final int GFX_TRANSLATION_X = BORDER_SIZE;
	public static final int GFX_TRANSLATION_Y = DECORATION_THICKNESS + BORDER_SIZE;

	private final int width;
	private final int height;

	public EnvelFrame(int width, int height) {
		this.width = width;
		this.height = height;

		setTitle("Envel");
		setSize(width, height);
		setLocationRelativeTo(null);
		setResizable(false);
		setIgnoreRepaint(true);

		setVisible(true);
		toFront();
	}

	@Override
	public final void addNotify() {
		super.addNotify();
		requestFocus();
	}

	@Override
	public final Graphics getGraphics() {
		Graphics g = super.getGraphics();
		g.translate(GFX_TRANSLATION_X, GFX_TRANSLATION_Y);
		return g;
	}

	@Override
	public final int getWidth() {
		return width;
	}

	@Override
	public final int getHeight() {
		return height;
	}

	@Override
	public final void setSize(int width, int height) {
		super.setSize(width + BORDER_SIZE_DOUBLE, height + DECORATION_THICKNESS + BORDER_SIZE_DOUBLE);
	}

	@Override
	public final String toString() {
		return String.format("EnvelFrame \"" + getTitle() + "\"\nResolution : %dx%d", getWidth(), getHeight());
	}

}
