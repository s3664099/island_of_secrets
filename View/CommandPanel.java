/*
Title: Island of Secrets Command Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 28 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controller.BookButton;
import Controller.CommandListener;
import Interfaces.GameCommandHandler;
import Interfaces.GameStateProvider;

public class CommandPanel  extends JPanel  {
	
	private static final long serialVersionUID = 5738616866958583642L;
	private final GameStateProvider state;
	private final GameCommandHandler commander;
	private JTextField commandField = new JTextField(2);
	
	private final JLabel spaceLabel = new JLabel();

	public CommandPanel(GameStateProvider state,GameCommandHandler commander) {
		this.state = state;
		this.commander = commander;
		setLayout(new GridLayout(9,1));
		configureLayout();
	}
	
	private void configureLayout() {
		removeAll();
		add(createSpacePanel());
		
		if (state.isInitialGameState()) {
			add(createBookPanel());
		}
		
		if (state.getResponseType()==2) {
			addShelterButtonPanels();
		} else if (state.isSavedGameState()) {
			addSaveGameButtonPanels();
		}
		
		add(createSpacePanel());
		add(createCommandInputPanel());
		
		revalidate();
		repaint();
	}
	
	public void refresh() {
		configureLayout();
		this.commandField.requestFocusInWindow();
	}
		
	/*				
	//Creates the command field
	String[] commands = state.getCommands();
	JPanel bottomPanel = new JPanel(new GridLayout(6,1));
	bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20)); //Top, Left, Bottom, Right
	JPanel inputPanel = new JPanel(new GridLayout(1,1));

	//Checks if displaying load games or shelter locations
	if (!state.isSavedGameState() && state.getResponseType()!=2) {
		
		//Button to display the map
		if (state.getPanelFlag()!=4) {
			addButton(inputPanel,"Map",new MapButton(game,this),320);
			bottomPanel.add(inputPanel);
		}
		
		//Command Field includes four labels above which contain the last three commands.
		//Also one for a blank spot
		for (int i=0;i<commands.length;i++) {
		
			//If blank, adds blank label
			if (commands[i].length()==0 || state.getResponseType() !=0) {
				bottomPanel.add(CreateLabelPanel(commands[i],1));
		
				//Otherwise add button with command
			} else {
				inputPanel = new JPanel(new GridLayout(1,1));
				addButton(inputPanel,commands[i],new CommandButton(game,this,commands[i]),320);
				inputPanel.setBorder(BorderFactory.createEmptyBorder(0,50,0,520));
				bottomPanel.add(inputPanel);
			}
		}
			
		--->inputPanel = new JPanel(new GridLayout(1,1));
	
		if (!state.isEndGameState()) {
		

			
		} else {
			addButton(inputPanel,"Exit",new QuitButton(this.frame,false,game,this),280);
			addButton(inputPanel,"Restart",new QuitButton(this.frame,true,game,this),280);
		}
	
		bottomPanel.add(inputPanel);
		
	} else if (state.isSavedGameState()) {
		
		//Add Escape save game button here and hide map
		commander.setSavedGameState(false);

		//Button to escape shelter
	} else {}
	*/
	
	//Component Builders
	//Adds a space between panels
	private JPanel createSpacePanel() {
		JPanel spacePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		spacePanel.add(spaceLabel);
		spaceLabel.setText("");
		return spacePanel;
	}
	
	//Creates a button and adds it to the panel.
	private void addButton(JPanel panel,String buttonName,ActionListener action,int size) {
		
		//Create Exit Button
		JButton button = new JButton(buttonName);
		panel.add(button);
		
		//Closes frame when clicked
		panel.setBorder(BorderFactory.createEmptyBorder(0,size,0,size));
	    button.addActionListener(action);
	}
	
	private JPanel createBookPanel() {
		JPanel inputPanel = new JPanel(new GridLayout(1,1));
		//addButton(inputPanel,"Click for Clues & Hints",new BookButton(game,this),260);
		addButton(inputPanel,"Click for Clues & Hints",null,260);
		return inputPanel;
	}
	
	private void addShelterButtonPanels() {
		String[] shelters = {"Grandpa's Shack","Cave of Snelm","Log Cabin"};
		Integer[] shelterLocations = {44,11,41};
		
		for (int i=0;i<3;i++) {
			JPanel panel = new JPanel(new GridLayout(1,1));
			//JButton button = addButton(shelters[i],new ShelterButton(game,this,shelterLocations[i]),320);
			//panel.add(button);
			add(panel);
		}
	}
	
	private void addSaveGameButtonPanels() {
		
		for (String gameName:state.getDisplayedSavedGames()) {
			
			//Is there a saved game?
			if (gameName.length()>0) {
				JPanel panel = new JPanel(new GridLayout(1,1));
				//JButton button = addButton(gameName,new LoadGameButton(game,this,gameName),320);
				//panel.add(button);
				add(panel);
			}
		}
		
		//Checks if move forward/back and adds buttons for that.
		if (state.getLowerLimitSavedGames()) {
			JPanel panel = new JPanel(new GridLayout(1,1));
			//JButton button = addButton(inputPanel,"Previous",new SearchGameButton(game,this,false),320);
			//panel.add(button);
			add(panel);
		}
		
		if (state.getUpperLimitSavedGames()) {
			JPanel panel = new JPanel(new GridLayout(1,1));
			//JButton button = addButton("Next",new SearchGameButton(game,this,true),320);
			//panel.add(button);
			add(panel);
		}
		
		JPanel panel = new JPanel(new GridLayout(1,1));
		//JButton button = addButton(inputPanel,"Back to Game",new GameButton(game,this),320);
		//panel.add(button);
		add(panel);
	}
	
	private JPanel createCommandInputPanel() {
		JPanel panel = new JPanel(new GridLayout(1,1));
		panel.setBorder(BorderFactory.createEmptyBorder(0,170,0,170));
		//commandField.addKeyListener(new CommandListener(commandField,commander,state,panel));
		panel.add(commandField);
		return panel;
	}
}

/* 26 March 2025 - Created File
 * 27 March 2025 - Added command line
 * 28 March 2025 - Added space creation panel
 */
