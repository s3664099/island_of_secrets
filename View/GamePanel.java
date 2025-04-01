/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 30 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import Interfaces.GameStateProvider;
import Interfaces.GameUI;
import Model.GameController;

public class GamePanel extends JPanel implements GameUI {
	
	private GameStateProvider state;
	private final GameController game;
		
	private static final long serialVersionUID = 1L;
	private StatusPanel statusPanel;
	private RoomPanel roomPanel;
	private CommandPanel commandPanel;

	public GamePanel(GameController game) {
		this.state = game.getEngine();
		this.game = game;
		initialiseUI();
	}
	
	public void initialiseUI() {
				
		//Gets the background colour for the frame
		this.setLayout (new BorderLayout()); 
		
		// Top section for status and label panels
		statusPanel = new StatusPanel(state);
		roomPanel = new RoomPanel(state);
		commandPanel = new CommandPanel(game);
				
		this.add(statusPanel, BorderLayout.NORTH); 
		this.add(roomPanel,BorderLayout.CENTER);
		this.add(commandPanel,BorderLayout.SOUTH);
	}

	@Override
	public void refreshUI(GameController game) {
		this.state = game.getEngine();
		
		statusPanel.refresh();
		roomPanel.refresh();
		commandPanel.refresh();

		revalidate();
		repaint();
		commandPanel.requestCommandFocus();

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
 */