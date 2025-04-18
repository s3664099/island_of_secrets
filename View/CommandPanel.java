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
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Controller.BookButton;
import Controller.CommandButton;
import Controller.CommandListener;
import Controller.GameButton;
import Controller.MapButton;
import Controller.QuitButton;
import Controller.SearchGameButton;
import Controller.ShelterButton;
import Interfaces.GameStateProvider;
import Model.GameController;

public class CommandPanel  extends JPanel  {
	
	private static final long serialVersionUID = 5738616866958583642L;
	
	//Constants
	private static final int BUTTON_INDENT= 320;
	private static final int WIDE_BUTTON_INDENT = 260;
	private static final int COMMAND_FIELD_INDENT = 170;
	private static final int SHELTER_COUNT = 3;
	
	//Componants
	private JTextField commandField = new JTextField(2);
	private final JLabel spaceLabel = new JLabel();
	private final GameController game;
	private GamePanel panel;
	
	//State
	private GameStateProvider state;
	private CommandListener activeListener;

	public CommandPanel(GameController game,GameStateProvider state,GamePanel panel) {
		
		this.state = Objects.requireNonNull(state);
		this.game = Objects.requireNonNull(game);
		this.panel = Objects.requireNonNull(panel);
		
		setLayout(new GridLayout(9,1));
		configureLayout();
	}

	public void refreshUI(GameStateProvider state) {
		this.state = Objects.requireNonNull(state);
		SwingUtilities.invokeLater(this::configureLayout);
	}
	
	private void configureLayout() {
		removeAll();
		add(createSpacePanel());
				
		if (isInitialGameState()) {
			add(createButtonPanel("Click for Clues & Hints",new BookButton(panel,game),WIDE_BUTTON_INDENT));
		} else if (isSeekingShelter()) {
			addShelterButtonPanels();
		} else if (isShowSavedGameState()) {
			addSaveGameButtonPanels();
		} else if (isEndGameState()) {
			add(createButtonPanel("Exit",new QuitButton(game,false),BUTTON_INDENT));
			add(createButtonPanel("Restart",new QuitButton(game,true),BUTTON_INDENT));
		}
				
		if (isNormalUI()) {
			//Button to display the map
			if (state.getPanelFlag()!=4) {
				add(createButtonPanel("Map",new MapButton(game,panel),320));
			}
			
			//Command Field includes four labels above which contain the last three commands.
			String[] commands = state.getCommands();
			for (int i=0;i<commands.length;i++) {
				
				//If blank, adds blank label
				if (commands[i].length()==0 || state.getResponseType() !=0) {
					add(createSpacePanel());
			
				//Otherwise add button with command
				} else {
					add(createButtonPanel(commands[i],new CommandButton(game,commands[i]),320));
				}
			}
			
			add(createSpacePanel());		
			add(createCommandInputPanel());			
		}
								
		revalidate();
		repaint();
	}
	
	private boolean isInitialGameState() {
		return state.isInitialGameState();
	}
	
	private boolean isSeekingShelter() {
		return state.getResponseType() == 2;
	}
	
	private boolean isShowSavedGameState() {
		return state.isSavedGameState();
	}
	
	private boolean isNormalUI() {
		return state.getResponseType() !=2 &&
				!state.isSavedGameState() &&
				!state.isEndGameState();
	}
	
	private boolean isEndGameState() {
		return state.isEndGameState();
	}
				
	private JPanel createSpacePanel() {
		JPanel spacePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		spacePanel.add(spaceLabel);
		spaceLabel.setText("");
		return spacePanel;
	}
	
	private JPanel createButtonPanel(String title,ActionListener action,int indent) {
		JPanel panel = new JPanel(new GridLayout(1,1));
		JButton button = new JButton(title);
		button.addActionListener(action);
		panel.add(button);
		panel.setBorder(BorderFactory.createEmptyBorder(0,indent,0,indent));
		return panel;
	}
					
	private void addShelterButtonPanels() {
		String[] shelters = {"Grandpa's Shack","Cave of Snelm","Log Cabin"};
		Integer[] shelterLocations = {44,11,41};
		
		for (int i=0;i<3;i++) {
			add(createButtonPanel(shelters[i],new ShelterButton(game,shelterLocations[i]),320));
		}
	}
	
	private void addSaveGameButtonPanels() {
			
		for (String gameName:state.getDisplayedSavedGames()) {
				
			//Is there a saved game?
			if (gameName.length()>0) {
				String loadName=gameName.split("\\.")[0];
				add(createButtonPanel(gameName,new CommandButton(game, "load "+loadName),320));
			}
		}
		
		//Checks if move forward/back and adds buttons for that.
		if (state.getLowerLimitSavedGames()) {
			add(createButtonPanel("Previous",new SearchGameButton(game,false),320));
		}
		
		if (state.getUpperLimitSavedGames()) {
			add(createButtonPanel("Next",new SearchGameButton(game,true),320));
		}
		
		add(createButtonPanel("Back to Game",new GameButton(game,panel),320));
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
 * 11 April 2025 - Displayed buttons for loading saved games. Removed LoadGameButton and just used command button
 * 				   Fixed issue with loading game
 */
