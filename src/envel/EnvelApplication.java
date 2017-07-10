package envel;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Extend this class to create an interactive application.
 * 
 * @author Milaud Miremadi
 *
 */
public abstract class EnvelApplication implements Runnable, EngineCapabilities {

	private EnvelFrame frame;

	private EnvelInput input;

	private boolean running;

	private Thread envelThread;

	private Image image;

	private int fps;

	private boolean displayFPS;

	public EnvelApplication(int width, int height) {
		frame = new EnvelFrame(width, height);
		input = new EnvelInput();
		frame.addKeyListener(input);
		frame.addMouseListener(input);
		frame.addMouseMotionListener(input);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stop();
			}
		});
	}

	@Override
	public final void start() {
		System.out.println("Envel [EnvelApplication]: starting");
		init(getWidth(), getHeight());
		envelThread = new Thread(this);
		envelThread.setName("Envel-Thread");
		envelThread.setPriority(Thread.MIN_PRIORITY);
		envelThread.start();
	}

	@Override
	public final void run() {
		Graphics g = frame.getGraphics();

		long preferredSleepTime = (long) PREFERRED_SLEEP_TIME * 1000000;
		long delay = (long) MINIMUM_SLEEP_TIME * 1000000;
		long lastTime = System.nanoTime();

		running = true;

		int frames = 0;
		long fpsUpdateTime = System.currentTimeMillis() + SECOND_MILLIS;
		long time;

		int updates;
		int i;

		try {
			while (running) {
				long dt = lastTime - System.nanoTime();

				if (delay > dt) {
					dt = delay;
				}

				updates = 0;
				sleep(dt / 1000000);

				time = System.nanoTime();
				while (updates < 10 && (updates < 1 || (time > lastTime))) {
					updates++;
					lastTime += preferredSleepTime;
				}

				if (time > lastTime) {
					lastTime = time;
				}

				for (i = 0; i < updates; i++) {
					update();
					input.pollKeyboard();
					input.pollMouse();
				}

				time = System.currentTimeMillis();

				render();

				g = frame.getGraphics();

				if (image != null) {
					if (displayFPS) {
						image.getGraphics().drawString("fps: " + fps, 5, 40);
					}
					drawAWT(image.getGraphics());
					g.drawImage(image, 0, 0, null);
				}

				frames++;
				if (time >= fpsUpdateTime) {
					fps = frames;
					frames = 0;
					fpsUpdateTime = time + SECOND_MILLIS;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		terminate();
	}

	@Override
	public final void stop() {
		running = false;
	}

	private final void terminate() {
		System.out.println("Envel [EnvelApplication]: closing");

		exit();

		try {
			Thread.sleep(SECOND_MILLIS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Envel [EnvelApplication]: exited");

		System.exit(0);
	}

	public abstract void init(int width, int height);

	public abstract void drawAWT(Graphics g);

	public abstract void exit();

	public final EnvelInput getInput() {
		return input;
	}

	public final int getWidth() {
		return frame.getWidth();
	}

	public final int getHeight() {
		return frame.getHeight();
	}

	public final int getFPS() {
		return fps;
	}

	public final boolean isDisplayingFPS() {
		return displayFPS;
	}

	public final void setDisplayFPS(boolean displayFPS) {
		this.displayFPS = displayFPS;
	}

	public final void setResizable(boolean resizable) {
		frame.setResizable(resizable);
	}

	public final Image getImage() {
		return image;
	}

	public final void setImage(Image image) {
		this.image = image;
	}

	public final void setTitle(String title) {
		frame.setTitle(title);
	}

	private static final void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}