/*
Title: Island of Secrets Command Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.13
Date: 18 April 2025
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
import UISupport.GameController;

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
			addNormalUIComponents();	
		}
								
		revalidateAndRepaint();
	}
	
	private void addNormalUIComponents() {
		
		if(showMapButton()) {
			add(createButtonPanel("Map",new MapButton(game,panel),320));
		}
		
		addCommandHistoryButtons();
		add(createSpacePanel());
		add(createCommandInputPanel());	
	}
	
	private boolean isInitialGameState() {
		return state.isInitialGameState();
	}
	
	private boolean isSeekingShelter() {
		return state.isShelterState();
	}
	
	private boolean isShowSavedGameState() {
		return state.isSavedGameState();
	}
	
	private boolean isNormalUI() {
		return !state.isShelterState() &&
				!state.isSavedGameState() &&
				!state.isEndGameState();
	}
	
	private boolean isEndGameState() {
		return state.isEndGameState();
	}
	
	private boolean showMapButton() {
		return state.getPanelFlag()!=4;
	}
	
	private JPanel createSpacePanel() {
		JPanel spacePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		spacePanel.add(spaceLabel);
		spaceLabel.setText("");
		return spacePanel;
	}
	
	private void addShelterButtonPanels() {
		String[] shelters = {"Grandpa's Shack","Cave of Snelm","Log Cabin"};
		Integer[] shelterLocations = {44,11,41};
		
		for (int i=0;i<SHELTER_COUNT;i++) {
			add(createButtonPanel(shelters[i],new ShelterButton(game,shelterLocations[i]),BUTTON_INDENT));
		}
	}
	
	private void addCommandHistoryButtons() {
		
		for (String command:state.getCommands()) {
			if (command.isEmpty() || !state.isRunningState()) {
				add(createSpacePanel());
			} else {
				add(createButtonPanel(command,new CommandButton(game,command),BUTTON_INDENT));
			}
		}
	}
	
	private JPanel createCommandInputPanel() {
		JPanel panel = new JPanel(new GridLayout(1,1));
		panel.setBorder(BorderFactory.createEmptyBorder(0,COMMAND_FIELD_INDENT,0,COMMAND_FIELD_INDENT));
		
		if (activeListener != null) {
			commandField.removeKeyListener(activeListener);
		}
		
		activeListener = new CommandListener(commandField,game);
		commandField.addKeyListener(activeListener);
		panel.add(commandField);
		
		requestCommandFocus();
		return panel;
	}
		
	private void addSaveGameButtonPanels() {
		
		for (String gameName:state.getDisplayedSavedGames()) {
			if (!gameName.isEmpty()) {
				addSavedGameButton(gameName);
			}
		}
		
		if (state.getLowerLimitSavedGames()) {
			add(createButtonPanel("Previous",new SearchGameButton(game,false),BUTTON_INDENT));
		}
		
		if (state.getUpperLimitSavedGames()) {
			add(createButtonPanel("Next",new SearchGameButton(game,true),BUTTON_INDENT));
		}
		
		add(createButtonPanel("Back to Game",new GameButton(game,panel),BUTTON_INDENT));
	}
	
	private void addSavedGameButton(String gameName) {
		String loadName = gameName.split("\\,")[0];
		add(createButtonPanel(gameName,new CommandButton(game,"load "+loadName),BUTTON_INDENT));
	}
	
	private JPanel createButtonPanel(String title,ActionListener action,int indent) {
		JPanel panel = new JPanel(new GridLayout(1,1));
		JButton button = new JButton(title);
		button.addActionListener(action);
		panel.add(button);
		panel.setBorder(BorderFactory.createEmptyBorder(0,indent,0,indent));
		return panel;
	}
	
	private void revalidateAndRepaint() {
		revalidate();
		repaint();
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
 * 18 April 2025 - Updated code based on recommendations by DeepSeek.
 */
