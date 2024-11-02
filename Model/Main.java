/*
Title: Island of Secrets Main
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.0
Date: 2 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/
package Model;

import javax.swing.SwingUtilities;
import View.GameFrame;

public class Main {

	public void startGame() {

		//Initialises the game data
		InitialiseGame game = new InitialiseGame();
		InitialisePlayer player = new InitialisePlayer();
		
		//Creating a thread to allow multiple objects
		SwingUtilities.invokeLater(new Runnable( ) {
			
			//Runs the thread as an inner class
			public void run ()
			{ 
				new GameFrame();
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
*/