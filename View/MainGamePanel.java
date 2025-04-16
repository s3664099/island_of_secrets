/*
Title: Island of Secrets Main Game Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.13
Date: 16 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Interfaces.GameStateProvider;
import Interfaces.GameUI;
import Interfaces.GameView;
import Model.GameController;

/**
 * Main game view panel combining status, room, and command components.
 * Implements both GameUI and GameView interfaces for dual functionality.
 */
public class MainGamePanel extends JPanel implements GameUI, GameView {
	
	private static final long serialVersionUID = -9087851496246015145L;
	
	//Components
	private final GameController game;
	private final GamePanel parentPanel;
	private StatusPanel statusPanel;
	private RoomPanel roomPanel;
	private CommandPanel commandPanel;
	
	//State
	private GameStateProvider state;
	private boolean isInitialised = false;

	public MainGamePanel(GameController game, GamePanel panel) {
		this.state = game.getState();
        this.game = Objects.requireNonNull(game, "GameController cannot be null");
        this.parentPanel = Objects.requireNonNull(panel, "Parent panel cannot be null");
		
        setLayout(new BorderLayout());
	}
	
	@Override
	public void onViewActivated() {
		if (!isInitialised) {
			initialiseComponents();
			isInitialised=true;
		}
		refreshUI(game);
	}
	
	public void initialiseComponents() {
				
		// Top section for status and label panels
		statusPanel = new StatusPanel(state);
		roomPanel = new RoomPanel(state);
		commandPanel = new CommandPanel(game,state,parentPanel);
		
		this.add(statusPanel, BorderLayout.NORTH); 
		this.add(roomPanel,BorderLayout.CENTER);
		this.add(commandPanel,BorderLayout.SOUTH);
	}

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

	@Override
	public void showMapView(GameController game) {
		// Intentional no-op - handled by parent container
	}

	@Override
	public void onViewDeactivated() {
		// Optional cleanup can be added here
	}

	@Override
	public JComponent getViewComponent() {
		return this;
	}

	@Override
	public void closeUI() {
        // Cleanup resources if needed
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
 */