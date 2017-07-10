package envel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Basic keyboard and mouse input handling.
 * Also handles key typing using a circular buffer.
 * 
 * @author Milaud Miremadi
 *
 */
public class EnvelInput implements KeyListener, MouseListener, MouseMotionListener {

	public static final int MOUSE_BUTTON_LEFT = 1;
	public static final int MOUSE_BUTTON_MIDDLE = 2;
	public static final int MOUSE_BUTTON_RIGHT = 3;

	public static final int KEY_FIRST = 400;
	public static final int KEY_LAST = 402;

	public static final int KEY_TYPED = KEY_FIRST;
	public static final int KEY_PRESSED = 1 + KEY_FIRST;
	public static final int KEY_RELEASED = 2 + KEY_FIRST;

	public static final int KEY_UNDEFINED = 0x00;
	public static final int KEY_ENTER = '\n';
	public static final int KEY_BACK_SPACE = '\b';
	public static final int KEY_TAB = '\t';
	public static final int KEY_CANCEL = 0x03;
	public static final int KEY_CLEAR = 0x0c;
	public static final int KEY_SHIFT = 0x10;
	public static final int KEY_CONTROL = 0x11;
	public static final int KEY_ALT = 0x12;
	public static final int KEY_PAUSE = 0x13;
	public static final int KEY_CAPS_LOCK = 0x14;
	public static final int KEY_ESCAPE = 0x1b;
	public static final int KEY_SPACE = 0x20;
	public static final int KEY_PAGE_UP = 0x21;
	public static final int KEY_PAGE_DOWN = 0x22;
	public static final int KEY_END = 0x23;
	public static final int KEY_HOME = 0x24;

	public static final int KEY_LEFT = 0x25;
	public static final int KEY_UP = 0x26;
	public static final int KEY_RIGHT = 0x27;
	public static final int KEY_DOWN = 0x28;

	public static final int KEY_COMMA = 0x2c;
	public static final int KEY_MINUS = 0x2d;
	public static final int KEY_PERIOD = 0x2e;
	public static final int KEY_SLASH = 0x2f;

	public static final int KEY_0 = 0x30;
	public static final int KEY_1 = 0x31;
	public static final int KEY_2 = 0x32;
	public static final int KEY_3 = 0x33;
	public static final int KEY_4 = 0x34;
	public static final int KEY_5 = 0x35;
	public static final int KEY_6 = 0x36;
	public static final int KEY_7 = 0x37;
	public static final int KEY_8 = 0x38;
	public static final int KEY_9 = 0x39;

	public static final int KEY_SEMICOLON = 0x3b;
	public static final int KEY_EQUALS = 0x3d;

	public static final int KEY_A = 0x41;
	public static final int KEY_B = 0x42;
	public static final int KEY_C = 0x43;
	public static final int KEY_D = 0x44;
	public static final int KEY_E = 0x45;
	public static final int KEY_F = 0x46;
	public static final int KEY_G = 0x47;
	public static final int KEY_H = 0x48;
	public static final int KEY_I = 0x49;
	public static final int KEY_J = 0x4a;
	public static final int KEY_K = 0x4b;
	public static final int KEY_L = 0x4c;
	public static final int KEY_M = 0x4d;
	public static final int KEY_N = 0x4e;
	public static final int KEY_O = 0x4f;
	public static final int KEY_P = 0x50;
	public static final int KEY_Q = 0x51;
	public static final int KEY_R = 0x52;
	public static final int KEY_S = 0x53;
	public static final int KEY_T = 0x54;
	public static final int KEY_U = 0x55;
	public static final int KEY_V = 0x56;
	public static final int KEY_W = 0x57;
	public static final int KEY_X = 0x58;
	public static final int KEY_Y = 0x59;
	public static final int KEY_Z = 0x5a;

	public static final int KEY_OPEN_BRACKET = 0x5b;
	public static final int KEY_BACK_SLASH = 0x5c;
	public static final int KEY_CLOSE_BRACKET = 0x5d;
	public static final int KEY_QUOTE = 0xdE;

	public static final int KEY_NUMPAD0 = 0x60;
	public static final int KEY_NUMPAD1 = 0x61;
	public static final int KEY_NUMPAD2 = 0x62;
	public static final int KEY_NUMPAD3 = 0x63;
	public static final int KEY_NUMPAD4 = 0x64;
	public static final int KEY_NUMPAD5 = 0x65;
	public static final int KEY_NUMPAD6 = 0x66;
	public static final int KEY_NUMPAD7 = 0x67;
	public static final int KEY_NUMPAD8 = 0x68;
	public static final int KEY_NUMPAD9 = 0x69;
	public static final int KEY_MULTIPLY = 0x6a;
	public static final int KEY_ADD = 0x6b;

	public static final int KEY_SEPARATOR = 0x6c;

	public static final int KEY_SUBTRACT = 0x6d;
	public static final int KEY_DECIMAL = 0x6e;
	public static final int KEY_DIVIDE = 0x6f;
	public static final int KEY_DELETE = 0x7f;

	public static final int KEY_F1 = 0x70;
	public static final int KEY_F2 = 0x71;
	public static final int KEY_F3 = 0x72;
	public static final int KEY_F4 = 0x73;
	public static final int KEY_F5 = 0x74;
	public static final int KEY_F6 = 0x75;
	public static final int KEY_F7 = 0x76;
	public static final int KEY_F8 = 0x77;
	public static final int KEY_F9 = 0x78;
	public static final int KEY_F10 = 0x79;
	public static final int KEY_F11 = 0x7a;
	public static final int KEY_F12 = 0x7b;

	private static final int NUM_BUTTONS = 3;
	private static final int NUM_KEYS = 1024;

	// States
	private static final int UP = 0;
	private static final int DOWN = 1;
	private static final int ONCE = 2;

	private int mousePressX;
	private int mousePressY;
	private int mouseReleaseX;
	private int mouseReleaseY;

	// Polled coordinates
	private int mx = 0;
	private int my = 0;
	// Live coordinates
	private int cx = 0;
	private int cy = 0;
	private boolean[] state;
	private int[] mouse;

	private boolean[] curr_keys;

	private int[] keys;
	private int[] queued_keys = new int[0x80];
	private int current_key;
	private int typed_keys;

	public EnvelInput() {
		state = new boolean[NUM_BUTTONS];
		mouse = new int[NUM_BUTTONS];
		curr_keys = new boolean[NUM_KEYS];

		for (int i = 0; i < NUM_BUTTONS; i++) {
			mouse[i] = UP;
		}
		keys = new int[NUM_KEYS];
		for (int i = 0; i < NUM_KEYS; i++) {
			keys[i] = UP;
		}
		for (int i = 0; i < queued_keys.length; i++) {
			queued_keys[i] = 0;
		}
		current_key = typed_keys;
	}

	public synchronized void pollMouse() {
		mx = cx;
		my = cy;

		for (int i = 0; i < NUM_BUTTONS; i++) {
			if (state[i]) {
				if (mouse[i] == UP) {
					mouse[i] = ONCE;
				} else {
					mouse[i] = DOWN;
				}
			} else {
				mouse[i] = UP;
			}
		}
	}

	public synchronized void pollKeyboard() {
		for (int i = 0; i < NUM_KEYS; i++) {
			if (curr_keys[i]) {
				if (keys[i] == UP) {
					keys[i] = ONCE;
				} else
					keys[i] = DOWN;
			} else {
				keys[i] = UP;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		state[e.getButton() - 1] = true;
		mouseConsume(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		state[e.getButton() - 1] = false;
		mouseConsume(e);
	}

	private final void mouseConsume(MouseEvent e) {
		switch (e.getID()) {
			case MouseEvent.MOUSE_PRESSED:
				mousePressX = e.getX() - EnvelFrame.GFX_TRANSLATION_X;
				mousePressY = e.getY() - EnvelFrame.GFX_TRANSLATION_Y;
				break;
			case MouseEvent.MOUSE_RELEASED:
				mouseReleaseX = e.getX() - EnvelFrame.GFX_TRANSLATION_X;
				mouseReleaseY = e.getY() - EnvelFrame.GFX_TRANSLATION_Y;
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
		mouseConsume(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		cx = e.getX() - EnvelFrame.GFX_TRANSLATION_X;
		cy = e.getY() - EnvelFrame.GFX_TRANSLATION_Y;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	public int mouseX() {
		return mx;
	}

	public int mouseY() {
		return my;
	}

	public boolean buttonDownOnce(int button) {
		return mouse[button - 1] == ONCE;
	}

	public boolean buttonDown(int button) {
		return mouse[button - 1] == ONCE || mouse[button - 1] == DOWN;
	}

	public boolean mouseLeftButtonActive() {
		return buttonDown(MOUSE_BUTTON_LEFT);
	}

	public boolean mouseMiddleButtonActive() {
		return buttonDown(MOUSE_BUTTON_MIDDLE);
	}

	public boolean mouseRightButtonActive() {
		return buttonDown(MOUSE_BUTTON_RIGHT);
	}

	public boolean mouseLeftButtonPressed() {
		return buttonDownOnce(MOUSE_BUTTON_LEFT);
	}

	public boolean mouseMiddleButtonPressed() {
		return buttonDownOnce(MOUSE_BUTTON_MIDDLE);
	}

	public boolean mouseRightButtonPressed() {
		return buttonDownOnce(MOUSE_BUTTON_RIGHT);
	}

	public int mousePressX() {
		return mousePressX;
	}

	public int mousePressY() {
		return mousePressY;
	}

	public int mouseReleaseX() {
		return mouseReleaseX;
	}

	public int mouseReleaseY() {
		return mouseReleaseY;
	}

	public int nextKey() {
		int key = -1;
		if (typed_keys != current_key) {
			key = queued_keys[current_key];
			current_key = current_key + 1 & 0x7f;
		}
		return key;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() > 4) {
			queued_keys[typed_keys] = e.getKeyChar();
			typed_keys = (typed_keys + 1) & 0x7f;
		}
		int keyCode = e.getKeyCode();
		if (keyCode >= 0 && keyCode < NUM_KEYS) {
			curr_keys[keyCode] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode >= 0 && keyCode < NUM_KEYS) {
			curr_keys[keyCode] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public boolean keyWasPressed(int key) {
		return keys[key] == ONCE;
	}

	public boolean keyIsHeld(int key) {
		return keys[key] == ONCE || keys[key] == DOWN;
	}

	public boolean keyWasPressed(char key) {
		int k = key - 32;
		if (k > 0 && k < NUM_KEYS) {
			return keys[k] == ONCE;
		}
		return false;
	}

	public boolean keyIsHeld(char key) {
		int k = key - 32;
		if (k > 0 && k < NUM_KEYS) {
			return keys[k] == ONCE || keys[k] == DOWN;
		}
		return false;
	}

	public static int get_key_num(int key) {
		if (!is_numerical(key)) {
			return -1;
		}
		return key - KEY_0;
	}

	public static boolean is_numerical(int key) {
		return key >= KEY_0 && key <= KEY_9;
	}

}