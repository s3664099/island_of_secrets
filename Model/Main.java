/*
Title: Island of Secrets Main
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 2.0
Date: 23 December 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import javax.swing.SwingUtilities;
import View.GameFrame;

public class Main {

	public void startGame() {

		//Initialises the game data
		Game gameData = new Game();
		Player player = new Player();
		GameEngine game = new GameEngine(gameData,player);
		
		//Creating a thread to allow multiple objects
		SwingUtilities.invokeLater(new Runnable( ) {
			
			//Runs the thread as an inner class
			public void run ()
			{ 
				new GameFrame(game);
			}
		});
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
*/