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
		refresh();
	}
	
	private void configureLayout() {
		removeAll();
		add(createSpacePanel());
		
		if (state.isInitialGameState()) {
			add(addButtonPanel("Click for Clues & Hints",null,260));
		}
		
		if (state.getResponseType()==2) {
			addShelterButtonPanels();
		} else if (state.isSavedGameState()) {
			addSaveGameButtonPanels();
		}
		
		if (!state.isInitialGameState() && state.getResponseType()!=2 && !state.isSavedGameState()) {
			
			//Button to display the map
			if (state.getPanelFlag()!=4) {
				//addButton(inputPanel,"Map",new MapButton(game,this),320);
				add(addButtonPanel("Map",null,320));
			}
			
			//Command Field includes four labels above which contain the last three commands.
			String[] commands = state.getCommands();
			for (int i=0;i<commands.length;i++) {
				
				//If blank, adds blank label
				if (commands[i].length()==0 || state.getResponseType() !=0) {
					add(createSpacePanel());
			
				//Otherwise add button with command
				} else {
					//addButton(inputPanel,commands[i],new CommandButton(game,this,commands[i]),320);
					add(addButtonPanel(commands[i],null,320));
				}
			}
			
			add(createSpacePanel());		
			if (!state.isEndGameState()) {
				add(createCommandInputPanel());
			} else {
				//addButton(inputPanel,"Exit",new QuitButton(this.frame,false,game,this),280);
				//addButton(inputPanel,"Restart",new QuitButton(this.frame,true,game,this),280);
				add(addButtonPanel("Exit",null,320));
				add(addButtonPanel("Restart",null,320));
			}
		}
				
		revalidate();
		repaint();
	}
	
	public void refresh() {
		configureLayout();
		this.commandField.requestFocusInWindow();
	}
			
	//Component Builders
	//Adds a space between panels
	private JPanel createSpacePanel() {
		JPanel spacePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		spacePanel.add(spaceLabel);
		spaceLabel.setText("");
		return spacePanel;
	}
	
	//Creates a button and adds it to the panel.
	private JPanel addButtonPanel(String title,ActionListener action,int size) {
		JPanel panel = new JPanel(new GridLayout(1,1));
		addButton(panel,title,action,size);
		return panel;
	}
	
	private void addButton(JPanel panel,String buttonName,ActionListener action,int size) {
		
		//Create Exit Button
		JButton button = new JButton(buttonName);
		panel.add(button);
		
		//Closes frame when clicked
		panel.setBorder(BorderFactory.createEmptyBorder(0,size,0,size));
	    button.addActionListener(action);
	}
				
	private void addShelterButtonPanels() {
		String[] shelters = {"Grandpa's Shack","Cave of Snelm","Log Cabin"};
		Integer[] shelterLocations = {44,11,41};
		
		for (int i=0;i<3;i++) {
			//JButton button = addButton(shelters[i],new ShelterButton(game,this,shelterLocations[i]),320);
			add(addButtonPanel(shelters[i],null,320));
		}
	}
	
	private void addSaveGameButtonPanels() {
		
		for (String gameName:state.getDisplayedSavedGames()) {
			
			//Is there a saved game?
			if (gameName.length()>0) {
				//JButton button = addButton(gameName,new LoadGameButton(game,this,gameName),320);
				add(addButtonPanel(gameName,null,320));
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
