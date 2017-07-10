package envel;

/**
 * The basic functions of an engine.
 * <p>
 * Fixed step loop.
 * Runs at approximately 58-60 fps since
 * 60 fps requires 16.67 milliseconds of sleep
 * but you can only sleep with integer times.
 * 
 * @author Milaud Miremadi
 *
 */
public interface EngineCapabilities {

	// Can't sleep 16.67, so ceil the value
	int PREFERRED_SLEEP_TIME = 17;

	int MINIMUM_SLEEP_TIME = 1;

	int SECOND_MILLIS = 1000;

	/**
	 * Start the engine
	 */
	void start();

	/**
	 * Update the engine (potentially called multiple times in
	 * one frame to keep up with the fixed step)
	 */
	void update();

	/**
	 * Called once each frame to render the image
	 */
	void render();

	/**
	 * Stop the engine
	 */
	void stop();

}