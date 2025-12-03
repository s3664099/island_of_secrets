/*
Title: Island of Secrets Starter
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Entry point for the adventure game.
 * 
 * This class is responsible for:
 * Configuring logging to an external file
 * Initializing and launching the game
 * Handling startup errors and logging critical failures
 */
public class Start {
	
	private static final Logger logger = Logger.getLogger(Start.class.getName());
	
    /**
     * Main method. Initializes logging and launches the game.
     *
     * @param args Command-line arguments (unused)
     */
	public static void main(String[] args) {
		
		 // Set up logging to file
		try {
			FileHandler fileHander = new FileHandler("mylog.log",true);
			fileHander.setFormatter(new SimpleFormatter());
			fileHander.setLevel(Level.ALL);
			logger.addHandler(fileHander);
			
		
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "An error occured while creating the log: "+e.getMessage());
		} 

		// Start the game
		try {
			logger.log(Level.INFO, "Starting the game ...");
			
			Main game = new Main();
			game.startGame();
			
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
3 December 2025 - Increased version number
*/
