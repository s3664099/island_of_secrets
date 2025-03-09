/*
Title: Island of Secrets Starter
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 9 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

import Model.Main;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Start {
	
	private static final Logger logger = Logger.getLogger(Start.class.getName());
	
	//Starts the game
	public static void main(String[] args) {

		try {
			
			// Configure the FileHandler to append to the log file
			FileHandler fileHander = new FileHandler("mylog.log",true);
			fileHander.setLevel(Level.ALL);
			logger.addHandler(fileHander);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "An error occured while creating the log: "+e.getMessage());
		} 

		try {
			
			//Log the start of the application
			logger.log(Level.INFO, "Starting the game ...");
			
			//Initialise & Start Game
			Main game = new Main();
			game.startGame();
			
			//Log the successful start of the game
			logger.log(Level.INFO, "Game started successfully");
			
		} catch (Exception e) {
			
			//Log the exception with details
			logger.log(Level.SEVERE, "An error occured while starting the game: "+e.getMessage());
			
			System.err.println("Failed to start the game. Please check the logs for more details");
			System.exit(1);
		}
	}
}

/*
7 September 2024 - Created File
29 October 2024 - Updated to version 1.
23 December 2024 - Updated to version 2.
31 January 2025 - Completed Testing and increased version
5 March 2025 - Increased to v4.0
9 March 2025 - Added try/catch & logging
		
*/
