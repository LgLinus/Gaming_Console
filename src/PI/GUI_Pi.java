package PI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * The GUI for the Console
 * 
 * @author Ivan Larsson
 * 
 */
public class GUI_Pi extends JFrame {

	private JPanel contentPane = new JPanel();
	private ArrayList<Menu_Button> Main_Menu_Buttons = new ArrayList<Menu_Button>();

	private JPanel mainMenu = new JPanel();
	private JPanel optionsMenu;
	private JPanel aboutMenu;
	private JPanel previousPanel;
	private JPanel currentPanel;
	private CardLayout cl;
	private JLabel bgMain = new JLabel();
	private JLabel bgOptions = new JLabel();
	private JLabel bgAbout = new JLabel();
	private int resWidth;
	private int resHeight;

	private String optionsMenuConnectedIP = "Connected units:";

	// Main menu buttons
	private Menu_Button btnGames;
	private Menu_Button btnOptions;
	private Menu_Button btnAbout;

	// Options buttons and texts
	Menu_Button btnSound = new Menu_Button(1) {
		public void press() {
			controller.changeMuted(); // Toggle sound on off;
			toggleSoundText(controller.getMuted());
		}
	};
	Menu_Button btnGetGameList = new Menu_Button(0) {
		public void press() {
			addGamePanels();
		}
	};
	Menu_Button btnReconnect = new Menu_Button(1);
	JTextArea taSound = new JTextArea("Sound is:    on");
	JTextArea taIP = new JTextArea();

	// About stuff declarations
	JTextArea txtrAbout = new JTextArea();
	private String aboutText = "X-station gaming console\n"
			+ "Version 1.0 Firmware May 12, 2014\n\n"
			+ "(c) Skalsoft Hackware Enterprises\n\n"
			+ "Lead code enhancer: Skalis\n"
			+ "Lead game designer: Linux Da Bomb\n"
			+ "Lead GUI designer: Ivano The Terrible\n"
			+ "Lead server designer: HIFaren\n"
			+ "Lead Linux programmer: Leo The Lion\n\n"
			+ "The Dictator: Magnus Krampell";

	private Controller controller;
	private GameSelectionPanels gamePanel;
//	= new GameSelectionPanels(resWidth,
//			resHeight);

	private int nbrButtons = 3; // The CURRENT number of buttons (varies
								// depending on the panel shown, Main menu = 3)

	/**
	 * Constructor for the GUI
	 */
	public GUI_Pi(Controller controller) {
		this.controller = controller;
		
		KeyDetector key = new KeyDetector(this, controller); 
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		resWidth = screenSize.width;
		resHeight = screenSize.height;
		initGui();

		mainMenu.setName("Main");
		// gamesMenu panels are initiated and given their name in a separate
		// class
		optionsMenu.setName("Options");
		aboutMenu.setName("About");
		
		this.controller.updateButtons(Main_Menu_Buttons);
		this.controller.updateButtons(gamePanel.buttons());
		this.contentPane.revalidate();
		this.contentPane.repaint();
	}

	/**
	 * Inits the GUI
	 */
	public void initGui() {
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, resWidth, resHeight);
		// setBounds(0, 0, screenSize.width, screenSize.height);

		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		cl = new CardLayout(0, 0);
		contentPane.setLayout(cl);

		contentPane.add(mainMenu, "name_6798406101994");
		mainMenu.setLayout(null);
		previousPanel = mainMenu;
		currentPanel = mainMenu;
		addGamePanels();

		/* ************************ Extra Exit button ********************* */
		// This extra exit button is only used during the development, for easy
		// shutdown of the program.
		// In the delivery version, there will be no exit button, because the
		// user should never be able
		// to return the operating system of the console.
		// JButton btnClose = new JButton("Exit");
		// btnClose.setBounds(100, 10, 165, 68);
		// mainMenu.add(btnClose);
		// btnClose.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// System.exit(0); // Yes, system.exit in the GUI - simply because this
		// is only a dev's button.
		// }
		// });
		/* *********************** end Extra Exit button******************* */

		/* *************************** Options Stuff ********************** */
		optionsMenu = new JPanel();
		contentPane.add(optionsMenu, "name_7946040729344");
		optionsMenu.setLayout(null);

		taSound.setFont(new Font("Tahoma", Font.PLAIN, 15));
		taSound.setBounds(748, 163 + 59 + 68/4, 165, 34);
		optionsMenu.add(taSound);

		taIP.setText(optionsMenuConnectedIP);
		taIP.setFont(new Font("Tahoma", Font.PLAIN, 15));
		taIP.setBounds(748, 210 + 59 + 68/4, 165, 234);
		optionsMenu.add(taIP);

		btnSound.setBounds(748, 83 + 59 + 68/4, 165, 68);
		this.controller.addOptionMenuButton(btnSound);
		optionsMenu.add(btnSound);
		btnSound.setText("Turn sound on/off");
		
		btnGetGameList.setBounds(748, 83, 165, 68);
		this.controller.addOptionMenuButton(btnGetGameList);
		optionsMenu.add(btnGetGameList);
		btnGetGameList.setText("Update game list");
		/* ************************ End options Stuff ********************* */

		/* ************************** "About" stuff *********************** */
		aboutMenu = new JPanel();
		contentPane.add(aboutMenu, "name_8765118079202");
		aboutMenu.setLayout(null);
		txtrAbout.setBounds(648, 210, 265, 234);
		aboutMenu.add(txtrAbout);
		/* *********************** End "About" stuff ********************** */
		mainMenuButtons();

		initBG(mainMenu,bgMain,"/bg1.jpg");
		initBG(optionsMenu,bgOptions,"/bg2.jpg");
		initBG(aboutMenu,bgAbout,"/bg2.jpg");
	}
	private void initBG(JPanel panel,JLabel lbl, String path)
	{
		lbl.setSize(resWidth, resHeight);
		/* Create the background and resize it to fit the entire panel */
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(path)); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(resWidth, resHeight,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		imageIcon = new ImageIcon(newimg);  // transform it back
		lbl.setIcon(imageIcon);
		panel.add(lbl);
	}
	public void addGamePanels() {
		gamePanel = new GameSelectionPanels(resWidth, resHeight);
		for (int i = 0; i < gamePanel.getNumberOfPanels(); i++) { // Adds the
																	// game
																	// panels to
																	// the
																	// content
																	// pane.
			contentPane.add(gamePanel.getPanel(i), "p1");
		}

		this.controller.updateButtons(gamePanel.buttons());
	}

	/**
	 * Inits the buttons in the main menu and the options menu
	 */
	public void mainMenuButtons() {
		btnGames = new Menu_Button(0) { // Games button gets the id=0
			public void press() { // This is what happens when the Games button
									// is pressed
				if (gamePanel != null && gamePanel.getPanels().length > 0) {
					cl.show(contentPane, "panel_1");
					previousPanel = mainMenu;
					currentPanel = gamePanel.getPanel(0); // Fetches the FIRST
															// game panel (id=0)
					mainMenu.hide();
					gamePanel.getPanel(0).show();
					nbrButtons = gamePanel.nbrGamesOnPanel(0); // Checks the
																// number of
																// buttons on
																// panel with
																// id=0
					controller.setSelectedNumber(0);
				} else {
					currentPanel = mainMenu;
					new MessagePopUp("No games found");
				}
			}
		};

		btnOptions = new Menu_Button(1) { // Options button gets the id=1
			public void press() { // This is what happens when the Options
									// button is pressed
				optionsMenuConnectedIP = "Connection data\n\nBase unit IP:\n"
						+ controller.getIPbaseunit()
						+ "\n\nNumber of connected\nhandunits: "
						+ controller.getNbrConnected();
				cl.show(contentPane, "panel_2");
				previousPanel = mainMenu;
				currentPanel = optionsMenu;
				mainMenu.hide();
				optionsMenu.show();
				nbrButtons = 2;
				controller.setSelectedNumber(0);
				taIP.setText(optionsMenuConnectedIP);
				controller.updateButtons(controller.getOptionMenuButtons());
			}
		};

		btnAbout = new Menu_Button(2) { // About button gets the id=2
			public void press() { // This is what happens when the About button
									// is pressed
				cl.show(contentPane, "panel_3");
				mainMenu.hide();
				previousPanel = mainMenu;
				aboutMenu.show();
				currentPanel = aboutMenu;
				nbrButtons = 1;
				contentPane.repaint();
				contentPane.revalidate();
				txtrAbout.setText(aboutText);
			}
		};

		// Adds the buttons to the arraylist of main menu buttons and options
		// buttons
		Main_Menu_Buttons.add(btnGames);
		Main_Menu_Buttons.add(btnAbout);
		Main_Menu_Buttons.add(btnOptions);
		// Main_Menu_Buttons.add(btnSound);

		btnGames.setBounds(748, 83, 165, 68);
		btnOptions.setBounds(748, 248, 165, 68);
		btnAbout.setBounds(748, 413, 165, 68);

		btnGames.setText("Games");
		btnOptions.setText("Options");
		btnAbout.setText("About");
		btnSound.setText("Sound");

		// Adds the buttons to the GUI
		mainMenu.add(btnGames);
		mainMenu.add(btnOptions);
		mainMenu.add(btnAbout);
	}

	/**
	 * Returns the main menu panel
	 * 
	 * @return main menu panel
	 */
	public JPanel getMainPanel() {
		return mainMenu;
	}

	/**
	 * Returns the 1st game panel
	 * 
	 * @return game menu panel
	 */
	public JPanel getGamePanel() {
		return gamePanel.getPanel(0);
	}

	/**
	 * Returns the cardLayOut
	 * 
	 * @return cardLayOut
	 */
	public CardLayout getCardLayout() {
		return cl;
	}

	/**
	 * Returns the current panel
	 * 
	 * @return current panel
	 */
	public JPanel getCurrentPanel() {
		return currentPanel;
	}

	/**
	 * Sets the current panel to match the parameterized name
	 * 
	 * @param panelName
	 *            the name of the panel to be set to current
	 */
	// 140509 TA
	public void setCurrentPanel(String panelName) {
		if (panelName.equals("Main"))
			currentPanel = mainMenu;
		else if (panelName.equals("Games"))
			currentPanel = gamePanel.getPanel(0);
		else if (panelName.equals("Options"))
			currentPanel = optionsMenu;
		else if (panelName.equals("About"))
			currentPanel = aboutMenu;
	}

	/**
	 * Returns the content pane
	 * 
	 * @return content pane
	 */
	public JPanel getcontentPane() {
		return contentPane;
	}

	/**
	 * Events that occur when a game button is pressed
	 * 
	 * @param selectedButton
	 *            the game button that was selected (and pressed)
	 * @return file the file that is matched with the button pressed
	 */
	// 140509 TA
	public String gameButtonPressed(int selectedButton) {
		String[] list = gamePanel.getListOfGames();
		String file = "";
		// Calculate which button was actually pressed, based on which panel is
		// shown and which button (0-x) is selected.
		int gameNumber = gamePanel.getCurrentPanelId()
				* gamePanel.getNbrGamesPerPage() + selectedButton;
		file = list[gameNumber];
		return file;
	}

	/**
	 * Hides the current game panel and shows the next game panel. If already at
	 * the last game panel, the 1st is shown.
	 */
	// 140509 TA
	public void goToNextGamePanel() {
		this.getCardLayout().show(this.getcontentPane(), "Main");
		this.getCurrentPanel().hide();
		currentPanel = gamePanel.getNextPanel();
		nbrButtons = gamePanel.nbrGamesOnPanel(gamePanel.getCurrentPanelId());
		this.currentPanel.show();
	}

	/**
	 * Hides the current game panel and shows the previous game panel. If
	 * already at the 1st game panel, the last is shown.
	 */
	// 140509 TA
	public void goToPreviousGamePanel() {
		this.getCardLayout().show(this.getcontentPane(), "Main");
		this.getCurrentPanel().hide();
		currentPanel = gamePanel.getPreviousPanel();
		nbrButtons = gamePanel.nbrGamesOnPanel(gamePanel.getCurrentPanelId());
		System.out.println(nbrButtons + " " + gamePanel.getCurrentPanelId());
		this.currentPanel.show();
	}

	public void toggleSoundText(boolean muted) {
		if (!muted) {
			taSound.setText("Sound is:    on");
		} else {
			taSound.setText("Sound is:    off");
		}
		contentPane.revalidate();
		contentPane.repaint();
	}

	/**
	 * Returns the number of buttons in the current panel
	 * 
	 * @return number of buttons in the current panel
	 */
	public int getNumberOfButtons() {
		return nbrButtons;
	}

	/**
	 * Change number of buttons
	 * 
	 * @param i
	 *            the number of buttons on the current panel
	 */
	public void changeNumberOfButtons(int i) {
		nbrButtons = i;
	}

	public ArrayList<Menu_Button> getMain_Menu_Buttons() {
		// TODO Auto-generated method stub
		return this.Main_Menu_Buttons;
	}
	
	public Controller getController(){
		return controller;
	}
}
