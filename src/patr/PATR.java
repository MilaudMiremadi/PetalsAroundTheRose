package patr;

import java.awt.Graphics;

import envel.Display;
import envel.EnvelApplication;
import envel.EnvelInput;

/**
 * Petals Around the Rose
 * 
 * @author Milaud Miremadi
 *
 */
public class PATR extends EnvelApplication {

	private Display display;

	private Bitmap24 dice;
	private Bitmap24[] sides;

	private boolean check_clicked;
	private boolean reroll_clicked;

	private boolean checked;
	private boolean correct;

	private int player_guess;
	private int correct_answer;

	// Input array
	private int[] in;

	private int[] dice_indices;

	private int next;

	public static void main(String[] args) {
		new PATR(600, 420).start();
	}

	public PATR(int width, int height) {
		super(width, height);
	}

	@Override
	public void init(int width, int height) {
		display = new Display(600, 420);
		Video.init(width, height, display.memory);
		in = new int[] { 0, -1, -1 };
		dice = Bitmap24.load_from_stream(PATR.class.getResourceAsStream("/dice.png"));
		sides = new Bitmap24[6];
		sides[0] = new Bitmap24(85, 85);
		sides[1] = new Bitmap24(85, 85);
		sides[2] = new Bitmap24(85, 85);
		sides[3] = new Bitmap24(85, 85);
		sides[4] = new Bitmap24(85, 85);
		sides[5] = new Bitmap24(85, 85);

		int dw = dice.width;
		for (int x = 0; x < 85; x++) {
			for (int y = 0; y < 85; y++) {
				int i = x + y * 85;
				sides[0].pixels[i] = dice.pixels[x + y * dw];
				sides[1].pixels[i] = dice.pixels[(x + 85) + y * dw];
				sides[2].pixels[i] = dice.pixels[(x + 170) + y * dw];
				sides[3].pixels[i] = dice.pixels[x + (y + 85) * dw];
				sides[4].pixels[i] = dice.pixels[(x + 85) + (y + 85) * dw];
				sides[5].pixels[i] = dice.pixels[(x + 170) + (y + 85) * dw];
			}
		}
		reroll();
	}

	@Override
	public void update() {
		EnvelInput input = getInput();
		if (input.keyWasPressed(EnvelInput.KEY_ESCAPE)) {
			stop();
		}
		
		// Check if user clicked on a button
		check_clicked = false;
		if (input.mouseLeftButtonPressed()) {
			if (inside_rect(input.mouseX(), input.mouseY(), (getWidth() / 3) + (getWidth() / 12), 240, getWidth() / 6, 17)) {
				check_clicked = true;
			}
		}
		reroll_clicked = false;
		if (input.mouseLeftButtonPressed()) {
			if (inside_rect(input.mouseX(), input.mouseY(), (getWidth() / 3) + (getWidth() / 12), 270, getWidth() / 6, 17)) {
				reroll_clicked = true;
			}
		}

		if (check_clicked) {
			check();
		}

		if (reroll_clicked) {
			reroll();
		}

		// Get next typed key, validate it, insert
		int num = EnvelInput.get_key_num(input.nextKey());
		if (!(num == 0 && next == 0)) {
			if (num != -1) {
				if (next == 0) {
					in[0] = num;
					next++;
				} else {
					if (next < in.length) {
						insert(in, 0, num);
						next++;
					}
				}
			}
		}
		
		// Delete value from input
		if (input.keyWasPressed(EnvelInput.KEY_BACK_SPACE) || input.keyWasPressed(EnvelInput.KEY_DELETE)) {
			if (next - 1 >= 0) {
				in[next - 1] = -1;
				next--;
			}
		}
		
		// Empty input formatting
		if (next == 0) {
			in[0] = 0;
			in[1] = -1;
			in[2] = -1;
		}
	}

	@Override
	public void render() {
		Video.reset();

		for (int i = 0; i < sides.length; i++) {
			Bitmap24 bmp = sides[dice_indices[i]];
			Video.blit_image(bmp.pixels, bmp.width, bmp.height, 20 + (i * 95), 20);
		}

		Video.draw_rect(getWidth() / 3, 200, getWidth() / 3, 17, 0xFFFFFF);
		Video.draw_rect((getWidth() / 3) + (getWidth() / 12), 240, getWidth() / 6, 17, check_clicked ? 0xFF0000 : 0xFFFFFF);
		Video.draw_rect((getWidth() / 3) + (getWidth() / 12), 270, getWidth() / 6, 17, reroll_clicked ? 0xFF0000 : 0xFFFFFF);
		setImage(display.getImage());
	}

	@Override
	public void drawAWT(Graphics g) {
		String inp = in[2] + "" + in[1] + "" + in[0];
		String dinp = (in[2] == -1 ? "" : in[2]) + "";
		dinp += (in[1] == -1 ? "" : in[1]) + "";
		dinp += (in[0] == -1 ? "" : in[0]);
		g.drawString(dinp, ((getWidth() / 2) - (getWidth() / 16)) + g.getFontMetrics().stringWidth(inp), 212);
		g.drawString("Check Answer", (getWidth() / 3) + (getWidth() / 12) + 9, 252);
		g.drawString("Reroll", (getWidth() / 3) + (getWidth() / 12) + 32, 282);
		if (checked) {
			if (correct) {
				g.drawString("Correct!", (getWidth() / 3) + (getWidth() / 12) + 32, 138);
			} else {
				String s = "Incorrect! You guessed " + player_guess + " but the correct answer was " + correct_answer + ".";
				g.drawString(s, (getWidth() / 2) - (g.getFontMetrics().stringWidth(s) / 2), 158);
			}
		}
		g.drawString("Rules:", 100, 320);
		g.drawString("1. The name of the game is Petals Around the Rose, and the name is significant.", 100, 340);
		g.drawString("2. Every answer is zero or an even number.", 100, 360);
		g.drawString("3. There is one correct answer for every throw of the dice.", 100, 380);
	}

	@Override
	public void exit() {

	}

	// Check if the player input the correct answer
	private void check() {
		if (checked) {
			return;
		}
		// last digit is in the hundreds place, next digit in the tens, first in the ones.
		player_guess = (in[2] == -1 ? 0 : in[2] * 100) + (in[1] == -1 ? 0 : in[1] * 10) + (in[0] == -1 ? 0 : in[0]);
		correct_answer = 0;
		for (int i = 0; i < dice_indices.length; i++) {
			int j = dice_indices[i];
			if (j == 2 || j == 4) {
				correct_answer += j;
			}
		}
		correct = player_guess == correct_answer;
		checked = true;
	}
	
	// Reroll the dice
	private void reroll() {
		if (dice_indices == null) {
			dice_indices = new int[6];
		}
		for (int i = 0; i < 6; i++) {
			dice_indices[i] = (int) (Math.random() * 6);
		}
		checked = false;
	}

	// Insert a value 'n' into an array 'a' at position 'pos'
	private static int[] insert(int[] a, int pos, int n) {
		for (int i = a.length - 1; i > pos; i--) {
			a[i] = a[i - 1];
		}
		a[pos] = n;
		return a;
	}

	// Check if a given (x,y) coordinate is within a rectangle (used for button clicking)
	private static boolean inside_rect(int x, int y, int rect_x, int rect_y, int rect_width, int rect_height) {
		return (x >= rect_x && x <= (rect_x + rect_width)) && (y >= rect_y && y <= (rect_y + rect_height));
	}

}
