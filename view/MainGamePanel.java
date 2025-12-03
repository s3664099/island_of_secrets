/*
Title: Island of Secrets Main Game Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package view;

import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import interfaces.GameStateProvider;
import interfaces.GameUI;
import interfaces.GameView;
import ui.GameController;

/**
 * Main game view panel combining status, room, and command components.
 * <p>
 * This panel serves as the primary in-game view, displaying the player’s
 * current status, the room description, and a command input area. It
 * implements both {@link GameUI} and {@link GameView} so that it can be
 * refreshed by the controller and participate in the card-layout view
 * switching handled by {@link GamePanel}.
 * </p>
 */ 
class MainGamePanel extends JPanel implements GameUI, GameView {
	
	private static final long serialVersionUID = -9087851496246015145L;
	
    /** Game controller that drives state updates and handles commands. */
	private final GameController controller;
	
    /** Parent container that manages multiple game views. */
	private final GamePanel parentPanel;
	
    /** Displays current player status such as score, health, etc. */
	private StatusPanel statusPanel;
	
    /** Displays the current room description and related visuals. */
	private RoomPanel roomPanel;
	
    /** Provides the command input area for player actions. */
	private CommandPanel commandPanel;
	
    /** Current immutable snapshot of the game state. */
	private GameStateProvider state;
	
    /** Indicates whether child components have been created and added. */
	private boolean isInitialised = false;

    /**
     * Constructs the main game panel.
     *
     * @param controller the {@link GameController} managing game logic,
     *                   must not be {@code null}
     * @param panel      the parent {@link GamePanel} managing this view,
     *                   must not be {@code null}
     * @throws NullPointerException if {@code controller} or {@code panel} is null
     */
	public MainGamePanel(GameController controller, GamePanel panel) {
		
		this.state = controller.getState();
        this.controller = Objects.requireNonNull(controller, "GameController cannot be null");
        this.parentPanel = Objects.requireNonNull(panel, "Parent panel cannot be null");
        
        setLayout(new BorderLayout());
	}
	
    /**
     * Called when this view is activated (brought to the foreground).
     * Lazily initialises sub-components on first activation and refreshes the UI.
     */
	@Override
	public void onViewActivated() {
		if (!isInitialised) {
			initialiseComponents();
			isInitialised=true;
		}
		
		if (controller.isMessageState()) {
			controller.setMessageState();
		} else if (state.isSavedGameState()) {
			controller.setRunningGameState();
		}
		
		refreshUI(controller);
	}
	
    /**
     * Creates and adds all sub-components (status, room, command panels).
     * Called only once, during the first activation of the view.
     */
	public void initialiseComponents() {
				
		// Top section for status and label panels
		statusPanel = new StatusPanel(state);
		roomPanel = new RoomPanel(state);
		commandPanel = new CommandPanel(controller,state,parentPanel);
		
		this.add(statusPanel, BorderLayout.NORTH); 
		this.add(roomPanel,BorderLayout.CENTER);
		this.add(commandPanel,BorderLayout.SOUTH);
	}

    /**
     * Refreshes all child panels to reflect the latest game state.
     * Ensures updates occur on the Swing Event Dispatch Thread.
     *
     * @param game the current {@link GameController} providing fresh state
     */
	@Override
	public void refreshUI(GameController game) {
		
		SwingUtilities.invokeLater(()-> {
			this.state = game.getState();
			statusPanel.refreshUI(state);
			roomPanel.refreshUI(state);
			commandPanel.refreshUI(state);
			
			revalidate();
			repaint();
			commandPanel.requestCommandFocus();
		});
	}

    /**
     * No-op for this view. Map switching is handled by the parent container.
     *
     * @param game ignored
     */
	@Override
	public void showMapView(GameController game) {}

    /**
     * Called when this view is deactivated.
     * Currently does nothing but may be extended for cleanup.
     */
	@Override
	public void onViewDeactivated() {}

    /**
     * Returns this panel as the Swing component representing the view.
     *
     * @return this {@code MainGamePanel} instance
     */
	@Override
	public JComponent getViewComponent() {
		return this;
	}

    /**
     * Closes the view and marks it uninitialised,
     * allowing components to be rebuilt if needed later.
     */
	@Override
	public void closeUI() {
        isInitialised = false;
	}
}

/* 2 November 2024 - Created File
 * 3 November 2024 - Added Status Box at the top
 * 4 November 2024 - Moved Label Panel creations to separate box.
 * 				   - Added panel to display location items
 * 5 November 2024 - Updated the display for the items
 * 				   - Completed the display for the place location
 * 8 November 2024 - Change from Frame to Panel and created an add method
 * 8 December 2024 - Updated code so that messages will split with pipe and add
 * 					 to new line.
 * 15 December 2024 - Added final score display
 * 23 December 2024 - Added quit function
 * 					- Updated to version 2.
 * 29 December 2024 - Added entry for special exits
 * 15 January 2025 - Removed extraneous methods and moved the lineLength to a constant.
 * 31 January 2025 - Completed Testing and increased version
 * 				   - Fixed the styling for the input section.
 * 1 February 2025 - Added the map display button
 * 17 February 2025 - Added restart button when game ends
 * 23 February 2025 - Hid map button if swimming in poisoned waters.
 * 24 February 2025 - Added buttons for Shelter
 * 25 February 2025 - Changed previous commands to buttons
 * 26 February 2025 - Added the load game buttons
 * 2 March 2025 - Added button to open browser to book
 * 3 March 2025 - Added code to focus on the command line.
 * 5 March 2025 - Increased to v4.0
 * 21 March 2025 - Updated for messageBuilder
 * 24 March 2025 - Removed GameFrame
 * 26 March 2025 - Moved code to separate classes
 * 27 March 2025 - Updated CommandPanel Constructor
 * 30 March 2025 - Removed usued Code
 * 31 March 2025 - Added panel refresh
 * 1 April 2025 - Updated refresh for items
 * 3 April 2025 - Updated code to take Game State
 * 4 April 2025 - Added function for mapPanel
 * 5 April 2025 - Changed name to MainGamePanel
 * 6 April 2025 - Added GameView interface
 * 8 April 2025 - Added close panel function due to interface
 * 15 April 2025 - Started updating code based on Deepseek recommendations
 * 16 April 2025 - Completed recommended changes
 * 24 September 2025 - Added JavaDocs
 * 29 September 2025 - Fixed problem where couldn't exit load game status
 * 3 December 2025 - Increased version number
 */