/*
Title: Island of Secrets Main
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/



import javax.swing.SwingUtilities;

import game.Game;
import game.GameEngine;
import game.GameInitialiser;
import game.Player;
import view.GameFrame;

/**
 * Entry point for the adventure game.
 * Initializes game data and launches the user interface.
 */
public class Main {
	
    /**
     * Starts the game by initializing core data structures and launching the UI.
     */
	public void startGame()  {

		try {
			
			//Initialises the game data
			Game gameData =  GameInitialiser.initialiseGame();
			Player player = new Player();
			GameEngine game = new GameEngine(gameData,player);
			
			//Launch UI
			SwingUtilities.invokeLater(() -> {
				try {
					new GameFrame(game);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("Main - Failed to launch game UI: "+e.getMessage());
				}
			});

		} catch (Exception e) {
			System.err.println("Failed to start the game: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

/*
7 Sept 2024 - Created File
8 Sept 2024 - Finished Initialisation
29 October 2024 - Updated to version 1
2 November 2024 - Added the player class to hold the player details
				- Removed the extraneous files
3 November 2024 - Added code to create a new game
4 November 2024 - Moved game engine into GrameFrame instead
23 December 2024 - Updated to version 2.
31 January 2025 - Completed Testing and increased version
5 March 2025 - Increased to v4.0
9 March 2025 - Added error handling
31 July 2025 - Added JavaDocs
3 December 2025 - Increased version number
*/