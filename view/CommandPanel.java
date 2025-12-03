/*
Title: Island of Secrets Command Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package view;

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

import controller.BookButton;
import controller.CommandButton;
import controller.CommandListener;
import controller.ShowMainViewButton;
import data.GameEntities;
import controller.MapButton;
import controller.QuitButton;
import controller.SearchGameButton;
import controller.ShelterButton;
import interfaces.GameStateProvider;
import ui.GameController;

/**
 * CommandPanel is the interactive command input and control panel
 * for the adventure game UI.  
 * <p>
 * It dynamically rebuilds its layout based on the current
 * {@link GameStateProvider} so that different sets of buttons
 * and controls are shown for:
 * <ul>
 *   <li>Initial game start</li>
 *   <li>Seeking shelter</li>
 *   <li>Viewing or loading saved games</li>
 *   <li>Normal gameplay</li>
 *   <li>End–game screens</li>
 * </ul>
 * Players can type commands, repeat previous commands,
 * view a map, or trigger game actions through generated buttons.
 */
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

    /**
     * Creates a CommandPanel bound to a game controller, current game state,
     * and the parent {@link GamePanel}.
     *
     * @param game   the main {@link GameController} for dispatching commands
     * @param state  the initial {@link GameStateProvider} describing game state
     * @param panel  the parent {@link GamePanel} used for view switching
     * @throws NullPointerException if any argument is {@code null}
     */
	public CommandPanel(GameController game,GameStateProvider state,GamePanel panel) {
		
		this.state = Objects.requireNonNull(state);
		this.game = Objects.requireNonNull(game);
		this.panel = Objects.requireNonNull(panel);
		
		setLayout(new GridLayout(9,1));
		configureLayout();
	}

    /**
     * Refreshes the panel when the game state changes.
     * Rebuilds the layout to match the new state.
     *
     * @param state the updated {@link GameStateProvider}; must not be {@code null}
     */
	public void refreshUI(GameStateProvider state) {
		this.state = Objects.requireNonNull(state);
		SwingUtilities.invokeLater(this::configureLayout);
	}
	
    /**
     * Requests focus for the command text field and selects all text,
     * allowing the player to immediately type a new command.
     * Runs on the Swing event-dispatch thread.
     */
	public void requestCommandFocus() {
		SwingUtilities.invokeLater(() -> {
			commandField.requestFocusInWindow();
			commandField.selectAll();
		});
	}
	
	
    // -----------------------------------------------------------------
    // Private helpers (overview only)
    // -----------------------------------------------------------------

    /**
     * Reconfigures the panel’s layout and child components
     * based on the current {@link GameStateProvider}.
     * Called on refresh and during construction.
     */
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
		} else if (isRestartGameState()) {
			add(createButtonPanel("Restart",new QuitButton(game,true),BUTTON_INDENT));
		}
				
		if (isNormalUI()) {
			addNormalUIComponents();	
		}
								
		revalidateAndRepaint();
	}
	
    /**
     * Builds a one-line blank spacer panel.
     * @return a left-aligned empty panel used to create visual spacing
     */
	private JPanel createSpacePanel() {
		JPanel spacePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		spacePanel.add(spaceLabel);
		spaceLabel.setText("");
		return spacePanel;
	}
	
    /**
     * Adds panels containing buttons that allow the player
     * to choose a shelter location during the “seeking shelter” state.
     */
	private void addShelterButtonPanels() {
		String[] shelters = {"Grandpa's Shack","Cave of Snelm","Log Cabin"};
		Integer[] shelterLocations = {GameEntities.ROOM_GRANDPAS_SHACK,GameEntities.ROOM_SNELM_LAIR,GameEntities.ROOM_HUT};
		
		for (int i=0;i<SHELTER_COUNT;i++) {
			if(state.getRoomVisited(shelterLocations[i])) {
				add(createButtonPanel(shelters[i],new ShelterButton(game,shelterLocations[i]),BUTTON_INDENT));
			}
		}
	}
	
    /**
     * Adds buttons representing recent command history,
     * allowing the player to quickly repeat past commands.
     */
	private void addCommandHistoryButtons() {
		
		for (String command:state.getCommands()) {
			if (command.isEmpty() || !state.isRunningState()) {
				add(createSpacePanel());
			} else {
				add(createButtonPanel(command,new CommandButton(game,command),BUTTON_INDENT));
			}
		}
	}
	
    /**
     * Creates the input field panel where the player types commands.
     * Attaches a {@link CommandListener} to process key events.
     *
     * @return a panel containing the command input field
     */
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
	
    /**
     * Adds buttons to load or page through saved games
     * when the game is in the saved-game view state.
     */	
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
		
		add(createButtonPanel("Back to Game",new ShowMainViewButton(panel),BUTTON_INDENT));
	}
	
    /**
     * Creates a panel containing a single button with the specified title,
     * listener, and horizontal indent.
     *
     * @param title  the button text
     * @param action the action listener to invoke when pressed
     * @param indent left/right padding in pixels
     * @return a panel containing the configured button
     */
	private JPanel createButtonPanel(String title,ActionListener action,int indent) {
		JPanel panel = new JPanel(new GridLayout(1,1));
		JButton button = new JButton(title);
		button.addActionListener(action);
		panel.add(button);
		panel.setBorder(BorderFactory.createEmptyBorder(0,indent,0,indent));
		return panel;
	}
	
    /**
     * Invokes {@link #revalidate()} and {@link #repaint()} to refresh
     * the Swing component hierarchy after layout changes.
     */
	private void revalidateAndRepaint() {
		revalidate();
		repaint();
	}
	
    /**
     * Adds the standard set of UI controls used during normal gameplay.
     * <p>
     * This includes:
     * <ul>
     *   <li>A Map button (if the player is not swimming)</li>
     *   <li>Buttons for recent command history</li>
     *   <li>A spacer panel for layout balance</li>
     *   <li>The command input text field</li>
     * </ul>
     * Called when the game is in a normal (non-special) state.
     */
	private void addNormalUIComponents() {
		
		if(showMapButton()) {
			add(createButtonPanel("Map",new MapButton(panel),320));
		}
		
		addCommandHistoryButtons();
		add(createSpacePanel());
		add(createCommandInputPanel());	
	}
	
    /**
     * Checks if the current game state is the initial game state.
     *
     * @return {@code true} if the player is at the very start of the game
     */
	private boolean isInitialGameState() {
		return state.isInitialGameState();
	}
	
    /**
     * Checks if the player is currently seeking shelter.
     *
     * @return {@code true} if the game state requires the player to choose a shelter
     */
	private boolean isSeekingShelter() {
		return state.isShelterState();
	}
	
	/**
     * Checks if the game is currently displaying the saved-games list.
     *
     * @return {@code true} if the UI should show saved-game options
     */
	private boolean isShowSavedGameState() {
		return state.isSavedGameState();
	}
	
    /**
     * Determines whether the UI should render the normal
     * gameplay controls (map, command history, input box).
     *
     * @return {@code true} if the game is not in shelter,
     *         saved-game, or end-game states
     */
	private boolean isNormalUI() {
		return !state.isShelterState() &&
				!state.isSavedGameState() &&
				!state.isEndGameState() &&
				!state.isRestartGameState();
	}
	
    /**
     * Checks if the game has reached an end-game state
     * where exit or restart options are shown.
     *
     * @return {@code true} if the game has ended
     */
	private boolean isEndGameState() {
		return state.isEndGameState();
	}
	
    /**
     * Checks if the game is to be restarted.
     * where restart options is shown.
     *
     * @return {@code true} if the game has ended
     */
	private boolean isRestartGameState() {
		return state.isRestartGameState();
	}
	
    /**
     * Determines whether the Map button should be displayed.
     * <p>
     * The Map button is hidden when the player is in a swimming state.
     *
     * @return {@code true} if the map button can be shown; {@code false} otherwise
     */
	private boolean showMapButton() {
		boolean showMapButton = true;
		if(state.isSwimmingState()) {
			showMapButton = false;
		}
		return showMapButton;
	}
	
    /**
     * Adds a button representing a saved game entry.
     * <p>
     * The button text shows the saved game name, and clicking it
     * loads that game using a {@link CommandButton} with a
     * {@code load <gameName>} command.
     *
     * @param gameName the saved-game name (may include a comma-delimited description)
     */
	private void addSavedGameButton(String gameName) {
		String loadName = gameName.split("\\,")[0];
		add(createButtonPanel(gameName,new CommandButton(game,"load "+loadName),BUTTON_INDENT));
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
 * 11 July 2025 - Update show map check.
 * 27 July 2025 - Removed code not needed on map button
 * 26 September 2025 - Removed magic numbers. Added JavaDocs
 * 9 October 2025 - Changed lair to Snelm's Lair
 * 6 November 2025 - Added restart game command
 * 2 December 2025 - Hid locations not visited from shelter
 * 3 December 2025 - Increased version number
 */
