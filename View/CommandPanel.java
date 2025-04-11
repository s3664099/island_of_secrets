/*
Title: Island of Secrets Command Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.12
Date: 11 April 2025
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
import javax.swing.SwingUtilities;

import Controller.BookButton;
import Controller.CommandButton;
import Controller.CommandListener;
import Controller.MapButton;
import Controller.QuitButton;
import Controller.ShelterButton;
import Interfaces.GameStateProvider;
import Model.GameController;

public class CommandPanel  extends JPanel  {
	
	private static final long serialVersionUID = 5738616866958583642L;
	private GameStateProvider state;
	private final GameController game;
	private JTextField commandField = new JTextField(2);
	private CommandListener activeListener;
	private GamePanel panel;
	
	private final JLabel spaceLabel = new JLabel();

	public CommandPanel(GameController game,GameStateProvider state,GamePanel panel) {
		
		this.state = state;
		this.game = game;
		this.panel = panel;
		
		setLayout(new GridLayout(9,1));
		refresh();
	}
	
	private void configureLayout() {
		removeAll();
		add(createSpacePanel());
				
		if (state.isInitialGameState()) {
			add(addButtonPanel("Click for Clues & Hints",new BookButton(panel,game),260));
		}
		
		if (state.getResponseType()==2) {
			addShelterButtonPanels();
		} else if (state.isSavedGameState()) {
			addSaveGameButtonPanels();
		}
		
		if (state.getResponseType()!=2 && !state.isSavedGameState() && 
			!state.isEndGameState()) {
			
			//Button to display the map
			if (state.getPanelFlag()!=4) {
				add(addButtonPanel("Map",new MapButton(game,panel),320));
			}
			
			//Command Field includes four labels above which contain the last three commands.
			String[] commands = state.getCommands();
			for (int i=0;i<commands.length;i++) {
				
				//If blank, adds blank label
				if (commands[i].length()==0 || state.getResponseType() !=0) {
					add(createSpacePanel());
			
				//Otherwise add button with command
				} else {
					add(addButtonPanel(commands[i],new CommandButton(game,commands[i]),320));
				}
			}
			
			add(createSpacePanel());		
			add(createCommandInputPanel());
		} else if (state.isEndGameState()) {
			add(addButtonPanel("Exit",new QuitButton(game,false),320));
			add(addButtonPanel("Restart",new QuitButton(game,true),320));
		}
				
		revalidate();
		repaint();
	}

	public void refreshUI(GameStateProvider state) {
		this.state = state;
		refresh();
		
	}
	
	private void refresh() {
		
		configureLayout();
		requestCommandFocus();
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
			add(addButtonPanel(shelters[i],new ShelterButton(game,shelterLocations[i]),320));
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
			add(addButtonPanel("Previous",null,320));
			//JButton button = addButton(inputPanel,"Previous",new SearchGameButton(game,this,false),320);
		}
		
		if (state.getUpperLimitSavedGames()) {
			add(addButtonPanel("Next",null,320));
			//JButton button = addButton("Next",new SearchGameButton(game,this,true),320);
		}
		
		add(addButtonPanel("Back to Game",null,320));
		//JButton button = addButton(inputPanel,"Back to Game",new GameButton(game,this),320);
	}
	
	private JPanel createCommandInputPanel() {
		JPanel panel = new JPanel(new GridLayout(1,1));
		panel.setBorder(BorderFactory.createEmptyBorder(0,170,0,170));
		
		if (activeListener != null) {
			commandField.removeKeyListener(activeListener);
		}
		
		activeListener = new CommandListener(commandField,game);
		commandField.addKeyListener(activeListener);
		
		panel.add(commandField);
		return panel;
	}

	public void requestCommandFocus() {
		SwingUtilities.invokeLater(() -> {
			commandField.requestFocusInWindow();
			commandField.selectAll();
		});
	}
}

/* 26 March 2025 - Created File
 * 27 March 2025 - Added command line
 * 28 March 2025 - Added space creation panel
 * 30 March 2025 - Completed Class
 * 31 March 2025 - Changed GameEngine to game controller
 * 1 April 2025 - Updated code to request focus in the commandField. Command button works
 * 3 April 2025 - Fixed problem with multiple command and initial state not changing. Updated
 *                to handle command state.
 * 6 April 2025 - Updated Map Button
 * 7 April 2025 - Activated button to open webpage
 * 8 April 2025 - Updated the quit and restart buttons to display
 * 9 April 2025 - Activated the restart button
 * 10 April 2025 - Updated shelterButton
 * 11 April 2025 - Displayed buttons for loading saved games
 */
