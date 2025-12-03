/*
Title: Island of Secrets GameUI interface
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/


package interfaces;

import ui.GameController;

/**
 * Defines the core user interface operations for the game.
 * Implementations handle UI updates, view management, and lifecycle operations.
 */
public interface GameUI {
	
    /**
     * Refreshes the entire UI with current game state.
     * @param controller The game controller providing current state and operations
     * @throws NullPointerException if controller is null
     */
	void refreshUI(GameController controller);
	
    /**
     * Activates and displays the map view.
     * @param controller The game controller providing map data and operations
     * @throws IllegalStateException if map cannot be displayed
     */
	void showMapView(GameController controller);
	
    /**
     * Closes and cleans up the UI resources.
     * Implementations should:
     * - Release any system resources
     * - Save persistent state
     * - Terminate any background operations
     */
	void closeUI();
}

/* 30 March 2025 - Created file
 * 31 March 2025 - Updated GameUI
 * 4 April 2025 - Added function to set mapPanel
 * 8 April 2025 - Added closePanel function definition
 * 14 April 2025 - Updated and added JavaDocs
 * 3 December 2025 - Increased version number
 */
