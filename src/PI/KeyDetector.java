package PI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/** Class to use during development, to be able to control the console with arrow keys instead
 * of having to connect a handheld device every time we want to test something. Not used in the
 * deliverable product. Also, BACKSPACE=OMEGA, ENTER=PI.
 * 
 * @author Skalis (TA)
 *
 */
public class KeyDetector implements KeyListener {
	private GUI_Pi gui;
	Controller controller;
	
	/** Constructs the KeyDetector with the main GUI and main controller as parameters.
	 * 
	 * @param gui the main GUI of the Pi-console
	 * @param controller the main controller of the Pi-console
	 */
	public KeyDetector(GUI_Pi gui, Controller controller) {
		this.gui=gui;
		this.controller=controller;
		this.gui.addKeyListener(this);
		
	}

	/** Events when one of the specified keys are (pressed and) released.
	 * @param e the KeyEvent as registered from the listener
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT) controller.navigate(AndroidInput.SENDLEFTRELEASED);
		if (key == KeyEvent.VK_RIGHT) controller.navigate(AndroidInput.SENDRIGHTRELEASED);
		if (key == KeyEvent.VK_UP) controller.navigate(AndroidInput.SENDUPRELEASED);
		if (key == KeyEvent.VK_DOWN) controller.navigate(AndroidInput.SENDDOWNRELEASED);
		if (key == KeyEvent.VK_ENTER) controller.navigate(AndroidInput.SENDPIRELEASED);
		if (key == KeyEvent.VK_BACK_SPACE) controller.navigate(AndroidInput.SENDOMEGARELEASED);
	}

	/**
	 * Inherited but not implemented.
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub		
	}

	/**
	 * Inherited but not implemented.
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

}
