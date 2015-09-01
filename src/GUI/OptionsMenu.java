package GUI;

import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import PI.Controller;
import PI.GUI_Pi;
import PI.Menu_Button;

/**
 * Gui class for the options menu
 * @author Linus Granath, Ivan Larsson
 *
 */
public class OptionsMenu extends JPanel {
	
	JTextArea taSound = new JTextArea("Sound is:    on");
	JTextArea taIP = new JTextArea();
	Menu_Button btnSound;
	Menu_Button btnGetGameList;
	Menu_Button btnReconnect = new Menu_Button(1);
	GUI_Pi main_gui;
	Controller controller;
	public OptionsMenu(Controller icontroller,GUI_Pi imain_gui,String connectedIP){
		
		this.main_gui = imain_gui;
		this.controller = main_gui.getController();
		setName("Options");
		setLayout(null);
		
		taSound.setFont(new Font("Tahoma", Font.PLAIN, 15));
		taSound.setBounds(748, 163 + 59 + 68/4, 165, 34);
		add(taSound);
		
		taIP.setText(connectedIP);
		taIP.setFont(new Font("Tahoma", Font.PLAIN, 15));
		taIP.setBounds(748, 210 + 59 + 68/4, 165, 234);
		add(taIP);
		// Options buttons and texts
		Menu_Button btnSound = new Menu_Button(1) {
			public void press() {
				controller.changeMuted(); // Toggle sound on off;
				toggleSoundText(controller.getMuted());
			}
		};
		Menu_Button btnGetGameList = new Menu_Button(0) {
			public void press() {
				main_gui.addGamePanels();
			}
		};
		Menu_Button btnReconnect = new Menu_Button(1);
		btnSound.setBounds(748, 83 + 59 + 68/4, 165, 68);
		this.controller.addOptionMenuButton(btnSound);
		this.add(btnSound);
		btnSound.setText("Turn sound on/off");
		
		btnGetGameList.setBounds(748, 83, 165, 68);
		this.controller.addOptionMenuButton(btnGetGameList);
		this.add(btnGetGameList);
		btnGetGameList.setText("Update game list");
		
		main_gui.getContentPane().add(this, "name_7946040729344");
		
	}
	
	/** Toggle sound on and off */
	public void toggleSoundText(boolean muted) {
		if (!muted) {
			taSound.setText("Sound is:    on");
		} else {
			taSound.setText("Sound is:    off");
		}

		main_gui.getContentPane().revalidate();

		main_gui.getContentPane().repaint();
	}
	
	/* *************************** Options Stuff ********************** */

	/* ************************ End options Stuff ********************* */

}
