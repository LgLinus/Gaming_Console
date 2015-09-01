package PI;

import java.awt.Image;

import javax.swing.*;

/**
 * The class constructs an array of JPanels, consisting of an array of JButtons (5 buttons per panel, the number
 * of panels depends on the number of games on the server). There are getters for most combintions of buttons/panels.
 * 
 * @author Skalis (TA)
 *
 */
public class GameSelectionPanels {
	private int numberOfGames; // The total number of games.
	private int numberOfPanels; // The total number of panels with games, for use in the GUI.
	private Menu_Button[] gameBtns; // An array of Menu_Buttons for the different games.
	private JPanel[] gamePanels; // An array of all the panels that are created.
	private JLabel[] bg; // An array of JLabel (backgrounds) for the game panels created.
	private int currentPanel=0; // This variable remembers which panel is currently the shown one.
	private int gamesPerPage = 4; // The number of games per page to be added to the panels.
	private GetDataFromServer gdfs;
	private String [] listOfGames; // A list of all the games found on the server.
	private int resHeight;
	private int resWidth;

	/**
	 * Constructs the game panels for the GUI.
	 */
	public GameSelectionPanels(int w, int h) {
		this.resWidth = w;
		this.resHeight = h;
		gdfs = new GetDataFromServer(); // An object to download data from the server (list of games, or a game);
		listOfGames = gdfs.getGameList();
//		for(int i =0;i<listOfGames.length;i++) // Prints a list of all the games in the console window.
//			System.out.println(listOfGames[i]);
		if(listOfGames!= null )
		numberOfGames = listOfGames.length; // the total number of games (files) found on the server
		else{
			new MessagePopUp("Failed to load game list from server.\nCheck your connection");
//			JOptionPane.showMessageDialog(null,"Failed to load game list from server.\nCheck your connection");
			}
		initiatePanels();
	
		}
	
	/**
	 * Returns the array of game buttons (as Menu_Button[]).
	 * @return gameBtns the array of game buttons
	 */
	public Menu_Button[] buttons(){ // Added by Ivan Larsson
		return gameBtns;
	}
	
	// Initiating method, calculating number of panels and adding the correct number of game buttons to every panel.
	private void initiatePanels() {
		String str;
		gameBtns = new Menu_Button[numberOfGames]; // Create an array of matching size to the number of games.
		for (int i=0; i<numberOfGames;i++) {
			gameBtns[i] = new Menu_Button(i%gamesPerPage); //i%gpp = id of the button, starting with 0.
			str = listOfGames[i].substring(0, listOfGames[i].length() - 4); // Remove the last 4 chars (suffix)...
			gameBtns[i].setText(str); // ...and put the filename as text for the button.
		}
		numberOfPanels = (numberOfGames+gamesPerPage-1)/gamesPerPage; // x games per panel
		gamePanels = new JPanel[numberOfPanels];
		bg = new JLabel[numberOfPanels];
		for (int i=0;i<numberOfPanels;i++) { // Go through all panels, and add the matching gamebuttons to it.
			gamePanels[i] = new JPanel();
			gamePanels[i].setName("Games");
			gamePanels[i].setLayout(null);
			addGamebuttonsToPanel(gamePanels[i], i);
			bg[i] = new JLabel();
			bg[i].setSize(resWidth,  resHeight);
			
			ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/bg2.jpg")); // load the image to a imageIcon
			Image image = imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(resWidth, resHeight,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			imageIcon = new ImageIcon(newimg);  // transform it back
			bg[i].setIcon(imageIcon);
			gamePanels[i].add(bg[i]);
		}	
	}
	
	// Adds the appropriate game buttons to the panel parameterized.
	private void addGamebuttonsToPanel(JPanel panel, int panelNumber) {
		int firstGameNumber = panelNumber*gamesPerPage;
		int lastGameNumber = firstGameNumber+gamesPerPage-1;
		int counter=0;
		if (lastGameNumber >= (gameBtns.length-1)) // The last panel might have less buttons.
			lastGameNumber = gameBtns.length-1;
		for (int i=firstGameNumber;i<=lastGameNumber;i++) {
			gameBtns[i].setBounds(748, 90+counter*150, 165, 68);
			gameBtns[i].setEnabled(false);
			panel.add(gameBtns[i]);
			counter++;
		}
		gameBtns[firstGameNumber].setEnabled(true); // Sets the top button as enabled, the rest disabled.
}
	
	/**
	 * Returns the amount of games on the parameterized panel id.
	 * @param panelNumber the id/panelnumber to calculate the number of games on
	 * @return number the amount of games on the panel
	 */
	public int nbrGamesOnPanel(int panelNumber) {
		int number;
		int firstGameNumber = panelNumber*gamesPerPage;
		int lastGameNumber = firstGameNumber+gamesPerPage-1;
		if (lastGameNumber >= numberOfGames)
			lastGameNumber = numberOfGames-1;
		number = lastGameNumber-firstGameNumber+1;
		return number;
	}
	
	/**
	 * Returns the amount of games on the currently shown panel
	 * @return nbr the amount of games on the currently shown panel
	 */
	public int getNbrGamesOnCurrentPanel() {
		int nbr = nbrGamesOnPanel(getCurrentPanelId());
		return nbr;
	}
	
	/**
	 * Returns the chosen games per panel (except for the last page, that may contain less games)
	 * @return the chosen number of games per panel
	 */
	public int getNbrGamesPerPage() {
		return gamesPerPage;
	}
	
	/**
	 * Returns the id of the currently shown game panel
	 * @return currentPanel the id-number of the currently shown panel
	 */
	public int getCurrentPanelId() {
		return currentPanel;
	}
	
	/**
	 * Returns the total amount of game panels created
	 * @return numberOfPanels the total amount of game panels created
	 */
	public int getNumberOfPanels() {
		return numberOfPanels;
	}
	
	/**
	 * Returns the panel that matches the parameterized index
	 * @param index the indexnumber of the panel to return
	 * @return gamePanels[index] the gamePanel matching the index number
	 */
	public JPanel getPanel(int index) {
		return gamePanels[index];
	}
	
	/** Returns the currently shown game panel
	 * @return gamePanels[currentPanel] the currently shown game panel
	 */
	public JPanel getCurrentPanel() {
		return gamePanels[currentPanel];
	}
	
	/** Returns the array of game panels
	 * @return gamePanels the array of game panels
	 */
	public JPanel[] getPanels() {
		return gamePanels;
	}
	
	/** Returns the next panel in the panel-array and updates the current panel index
	 * @return gamePanels[currentPanel] the game panel of the UPDATED current panel index
	 */
	public JPanel getNextPanel() {
		currentPanel++;
		if (currentPanel>gamePanels.length-1)
			currentPanel = 0;
		return gamePanels[currentPanel];
	}

	/**
	 * Returns the previous panel in the panel-array and updates the current panel index
	 * @return gamePanels[currentPanel] the game panel of the UPDATED current panel index
	 */
	public JPanel getPreviousPanel() {
		currentPanel--;
		if (currentPanel<0)
			currentPanel = gamePanels.length-1;
		return gamePanels[currentPanel];			
	}
	
	public String[] getListOfGames() {
		return listOfGames;
	}
}
