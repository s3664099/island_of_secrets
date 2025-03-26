/*
Title: Island of Secrets Command Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 26 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.GridLayout;
import javax.swing.JPanel;
import Interfaces.GameStateProvider;

public class CommandPanel  extends JPanel  {
	
	private static final long serialVersionUID = 5738616866958583642L;
	public final GameStateProvider state;

	public CommandPanel(GameStateProvider state) {
		this.state = state;
		configureLayout();
		refresh();
	}
	
	private void configureLayout() {
		setLayout(new GridLayout(15,1));
	}
	/*		
	if (state.isInitialGameState()) {
		JPanel inputPanel = new JPanel(new GridLayout(1,1));
		addButton(inputPanel,"Click for Clues & Hints",new BookButton(game,this),260);
		middlePanel.add(inputPanel);
	}
	
	if (state.getResponseType()==2) {
		
		String[] shelters = {"Grandpa's Shack","Cave of Snelm","Log Cabin"};
		Integer[] shelterLocations = {44,11,41};
		
		for (int i=0;i<3;i++) {
			JPanel inputPanel = new JPanel(new GridLayout(1,1));
			addButton(inputPanel,shelters[i],new ShelterButton(game,this,shelterLocations[i]),320);
			middlePanel.add(inputPanel);
		}
		
	} else if (state.isSavedGameState()) {
		
		for (String gameName:state.getDisplayedSavedGames()) {
			
			//Is there a saved game?
			if (gameName.length()>0) {
				JPanel inputPanel = new JPanel(new GridLayout(1,1));
				addButton(inputPanel,gameName,new LoadGameButton(game,this,gameName),320);
				middlePanel.add(inputPanel);
			}
		}
		
		//Checks if move forward/back and adds buttons for that.
		if (state.getLowerLimitSavedGames()) {
			JPanel inputPanel = new JPanel(new GridLayout(1,1));
			addButton(inputPanel,"Previous",new SearchGameButton(game,this,false),320);
			middlePanel.add(inputPanel);
		}
		
		if (state.getUpperLimitSavedGames()) {
			JPanel inputPanel = new JPanel(new GridLayout(1,1));
			addButton(inputPanel,"Next",new SearchGameButton(game,this,true),320);
			middlePanel.add(inputPanel);
		}
		
		JPanel inputPanel = new JPanel(new GridLayout(1,1));
		addButton(inputPanel,"Back to Game",new GameButton(game,this),320);
		middlePanel.add(inputPanel);
	}
	
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
			
		inputPanel = new JPanel(new GridLayout(1,1));
	
		if (!state.isEndGameState()) {
		
			JTextField commandField = new JTextField(2);
			commandField.addKeyListener(new CommandListener(commandField,game,this));
			inputPanel.setBorder(BorderFactory.createEmptyBorder(0,170,0,170));
			inputPanel.add(commandField);
			this.commandField = commandField;
			
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
	public void refresh() {
		
	}
}

/* 26 March 2025 - Created File
 */
