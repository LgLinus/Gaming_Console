package PI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

/**
 * Controller class for the console GUI
 * @author Ivan Larsson
 *
 */
public class Controller {
	
	/* Arrow commands */
	private static final String INPUT_LEFT = AndroidInput.SENDLEFTRELEASED;
	private static final String INPUT_RIGHT = AndroidInput.SENDRIGHTRELEASED;
	private static final String INPUT_UP = AndroidInput.SENDUPRELEASED;
	private static final String INPUT_DOWN = AndroidInput.SENDDOWNRELEASED;
	private static final String INPUT_PI = AndroidInput.SENDPIRELEASED;
	private static final String INPUT_OMEGA = AndroidInput.SENDOMEGARELEASED;
	
	/* Players connected */
	private int nbrConnected = 0;
	
	/* Check if sound is enabled */
	private boolean muted = false;
	
	/* Used to send information to the console */
	private SendLocalInput sendLocalInput = null;
	private GUI_Pi gui;
	private int selectedNumber = 0;
	
	/* Main menu buttons */
	private ArrayList<Menu_Button> Main_Menu_Buttons;
	private ArrayList<Menu_Button> menu_Button_Options = new ArrayList<Menu_Button>(); 
	
	private Menu_Button[] gameBtns;
	
	private GameRunner gameRunner = null;
	
	private Sender sender;
	public Controller() {
		gui = new GUI_Pi(this);
		this.Main_Menu_Buttons = gui.getMain_Menu_Buttons();
		Receiver re = new Receiver(this); // Launches the receiver (which is run in a separate thread)
		sender = new Sender(this);
	}

	
	/**
	 * Updates the button in the under menu "Games"
	 * @param gameBtns
	 */
	public void updateButtons(Menu_Button[] gameBtns) {
		this.gameBtns = gameBtns;
		for (int i = 0; i < gameBtns.length; i++) {
			gameBtns[i].update(getSelectedNumber());
		}
	}

	/**
	 * Updates all the buttons in the current menu
	 * @param Main_Menu_Buttons
	 */
	public void updateButtons(ArrayList<Menu_Button> Main_Menu_Buttons) {
//		this.Main_Menu_Buttons = Main_Menu_Buttons;
		for (int i = 0; i < Main_Menu_Buttons.size(); i++) {
			Main_Menu_Buttons.get(i).update(getSelectedNumber());
		}
	}

	/* ********************************************************************* */
	/* * Separate (private) navigation methods for the different sub-menus * */
	/* * because they might have different layouts and different behaviour * */
	/* ********************************************************************* */
	
	// Method used to navigate main menu
	private void navigateMain(String inputLine) {
		System.out.println("Navigate main");
		if (inputLine != null) {
			if (inputLine.equals(INPUT_DOWN)) {
				selectedNumber++;
				if (selectedNumber >= gui.getNumberOfButtons())
					selectedNumber = 0;
				updateButtons(Main_Menu_Buttons);
			}
			if (inputLine.equals(INPUT_UP)) {
				selectedNumber--;
				if (selectedNumber < 0)
					selectedNumber = gui.getNumberOfButtons() - 1;
				updateButtons(Main_Menu_Buttons);
			}
			if (inputLine.equals(INPUT_PI)) {
				System.out.println("test 1 ");
				pressList(Main_Menu_Buttons);
				updateButtons(Main_Menu_Buttons);
			}			
			if (inputLine.equals(INPUT_OMEGA)) {
				navigateOmega();
			}
		}
	}
	
	/* Method used to navigate the games menu*/
	private void navigateGames(String inputLine) {
		if (inputLine != null) {
			
			if (inputLine.equals(INPUT_DOWN)) {
				selectedNumber++;
			
			}
			if (inputLine.equals(INPUT_UP)) {
				selectedNumber--;
//				sender.send();
				System.out.println("SEND");
			}
		
			if (inputLine.equals(INPUT_LEFT)) {
				gui.goToPreviousGamePanel();
				selectedNumber = 0;
			}
			if (inputLine.equals(INPUT_RIGHT)) {
				gui.goToNextGamePanel();
				selectedNumber = 0;
			}
			// Download and run the selected game
			if (inputLine.equals(INPUT_PI)) {
				String file = gui.gameButtonPressed(selectedNumber);
				downloadAndRun(file);
				pressArray();
			}
			if (inputLine.equals(INPUT_OMEGA)) {
				navigateOmega();
			}
			checkSelectedNumber();
			updateButtons(gameBtns);
		}
	}
	
	/* Navigate options menu */
	private void navigateOptions(String inputLine) {
		if (inputLine != null) {
			
			if (inputLine.equals(INPUT_DOWN)) {
				selectedNumber++;
				if (selectedNumber >= gui.getNumberOfButtons())
					selectedNumber = 0;
			}
			if (inputLine.equals(INPUT_UP)) {
				selectedNumber--;
				if (selectedNumber < 0)
					selectedNumber = gui.getNumberOfButtons() - 1;
			}
			if (inputLine.equals(INPUT_PI)) { // Because there is only one button in the options menu...
				// This is the events that occur when "sound on/off" is pressed

				pressList(this.menu_Button_Options);
			}
			if (inputLine.equals(INPUT_OMEGA)) {
				navigateOmega();
			}
			checkSelectedNumber();
			updateButtons(this.menu_Button_Options);
		}
	}
	
	// 140509 TA
	private void navigateAbout(String inputLine) {
		if (inputLine != null) {
			if (inputLine.equals(INPUT_DOWN)) {
				selectedNumber++;
				if (selectedNumber >= gui.getNumberOfButtons())
					selectedNumber = 0;
				updateButtons(Main_Menu_Buttons);
				updateButtons(gameBtns);
			}
			if (inputLine.equals(INPUT_UP)) {
				selectedNumber--;
				if (selectedNumber < 0)
					selectedNumber = gui.getNumberOfButtons() - 1;
				updateButtons(Main_Menu_Buttons);
				updateButtons(gameBtns);
			}

			if (inputLine.equals(INPUT_PI)) {
				System.out.println("test 1 ");
				pressList(Main_Menu_Buttons);

				updateButtons(Main_Menu_Buttons);
				updateButtons(gameBtns);
			}
			if (inputLine.equals(INPUT_OMEGA)) {
				navigateOmega();
			}
		}
	}
	
	// 140509 TA
	// Omega button always returns to the main menu
	private void navigateOmega() {
		System.out.println("GOTO MAIN MENU");
		gui.getCurrentPanel().hide(); // Changed 2014-05-20 previously called after gui.getcardlayout
		gui.getCardLayout().show(gui.getcontentPane(), "Main");
		gui.getMainPanel().show();
		gui.setCurrentPanel("Main");
		gui.changeNumberOfButtons(3);
		setSelectedNumber(0); // sets the top button as selected
		updateButtons(Main_Menu_Buttons);

	}
	/* ************************************** */
	/* * End of separate navigation methods * */
	/* ************************************** */
	
	/**
	 * Here it takes the inputLine from the android device and processes it to something the GUI understands  
	 * @param inputLine
	 */
	public void navigate(String inputLine) { 			// edited 140508 TA

		if(gameRunner!=null){
			if((!gameRunner.checkIfProccessRuns()) && gameRunner.gotInput()){
				gameRunner = null;
				sendLocalInput = null;}
			else if (sendLocalInput != null && gameRunner.gotInput()){
				sendLocalInput.sendInput(inputLine);
			}}
		if(gameRunner==null){
			JPanel panel = gui.getCurrentPanel();
			String id = panel.getName(); 	//added 140508 TA
			
			String command;
			if(inputLine.contains(",")){
				String[] split = inputLine.split(",");
				String player = split[0];
				command = split[1];
			}
			else
				command = inputLine;
			// if/else statements that calls the MATCHING navigation method
		if (id.equals("Main"))
			navigateMain(command);
		else if (id.equals("Games"))
			navigateGames(command);
		else if (id.equals("Options"))
			navigateOptions(command);
		else if (id.equals("About"))
			navigateAbout(command);
		// end if/else statements
		}
		
	}
	
	// Downloads the game and runs it when finished downloading
	private void downloadAndRun(String file) {
		GetDataFromServer gdfs = new GetDataFromServer();
		try {
			gdfs.getGame(file);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String cmd = "java -jar "+gdfs.toPath+file;
		try {
			gameRunner  = new GameRunner(this,cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gdfs = null;
		System.out.println(file+" downloaded");
	}
	
	/**
	 * Add a new local input
	 */
	public void addSendLocalInput(){
		sendLocalInput = new SendLocalInput(this);
	}
	/**
	 * This method activates when the select button this pressed
	 * this only works with the game under menu
	 */
	private void pressArray() {
		for (int i = 0; i < gameBtns.length; i++) {
			if ((gameBtns[i].getSelected()) && (gameBtns[i].isShowing())) {
				gameBtns[i].press();
				selectedNumber = 0;
				break;
			}
		}
	}

	/**
	 * This method activates when the select button this pressed
	 * this works with all buttons except the ones in the game menu
	 * Edited by Linus at 10:48 2014-05-20
	 */
	private void pressList(ArrayList<Menu_Button> buttons) {
		for (int i = 0; i < buttons.size(); i++) {
			if ((buttons.get(i).getSelected())
					&& (buttons.get(i).isShowing())) {
				buttons.get(i).press();
				selectedNumber = 0;
				break;
			}
		}
	}
	
	public String getIPbaseunit() {
		InetAddress local = null;
		try {
			local = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String address = local.getHostAddress();
		return address;
	}
	
	public void addConnected() {
		nbrConnected++;
	}
	
	public void subtractConnected() {
		nbrConnected--;
	}
	
	public int getNbrConnected() {
		return nbrConnected;
	}
	

	public void closeProcess() {
		if(sendLocalInput!=null)
		sendLocalInput.interrupt();
		sendLocalInput = null;
		gameRunner = null;
		System.out.println("WOWOWPOP");
	}

	public GameRunner getGameRunner() {
		return gameRunner;
	}

	public void addOptionMenuButton(Menu_Button btn) {
		this.menu_Button_Options.add(btn);
	}
	
	public ArrayList<Menu_Button> getOptionMenuButtons() {
		return this.menu_Button_Options;
	}

	/**
	 * Change the sound option
	 */
	public void changeMuted() {
		this.muted = !muted;
	}
	
	/**
	 * Return the sound option
	 * @return
	 */
	public boolean getMuted(){
		return this.muted;
	}

	public void sendLayoutToDevice(byte[] content) {
		sender.send(content);
		
		}
	/**
	 * Returns the selected button in the current menu
	 * @return selectedNumber
	 */
	public int getSelectedNumber() {
		return selectedNumber;
	}
	
	/** Sets the parameterized button id as selected
	 * @param id the id of the button to set to selected
	 */
	// 140509 TA
	public void setSelectedNumber(int id){
		selectedNumber = id;
	}
	// Check if the selected number is valid
	private void checkSelectedNumber(){
		if (selectedNumber >= gui.getNumberOfButtons())
			selectedNumber = 0;
		else if (selectedNumber < 0)
			selectedNumber = gui.getNumberOfButtons() - 1;
	
	}
}
