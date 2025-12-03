/*
Title: Island of Secrets Write Operation Interface
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package interfaces;

import java.io.IOException;

/**
 * Defines the contract for handling game commands and state modifications.
 * Implementations should process these commands and manage game state transitions.
 */
public interface GameCommandHandler {

    /**
     * Sets game state to running
     * @param isSavedGame true to enter saved game selection, false to return to normal play
     */
	public void setRunningGameState();
	
    /**
     * Sets the game state to display saved games
     * @param isSavedGame true to enter saved game selection, false to return to normal play
     */
	public void setSavedGameState();
	
    /**
     * Sets the game state to display shelter options
     * @param isSavedGame true to enter saved game selection, false to return to normal play
     */
	public void setShelterGameState();
	
    /**
     * Processes a text command from the player
     * @param input The raw command input (e.g., "go north")
     * @throws IOException if command processing fails due to I/O operations
     * @throws GameCommandException if command is invalid or cannot be executed
     */	
	public void processCommand(String input) throws IOException;
			
    /**
     * Directly moves player to specified location
     * @param locationID The destination location identifier
     * @throws IllegalArgumentException if locationID is invalid
     */	
	public void setRoom(int locationID);
	
    /**
     * Navigates forward through saved game list
     * @throws IOException if navigation fails due to I/O operations
     * @throws IllegalStateException if at end of saved game list
     */
	public void increaseLoadPosition() throws IOException;
	
    /**
     * Navigates backward through saved game list
     * @throws IOException if navigation fails due to I/O operations
     * @throws IllegalStateException if at beginning of saved game list
     */
	public void decreaseLoadPosition() throws IOException;
	
}

/* 24 March 2025 - Created File
 * 25 March 2025 - Added method for setting saved game state
 * 10 April 2025 - Added setRoom function
 * 13 April 2025 - Added functions for increasing and decreasing load positions
 * 14 April 2025 - Added JavaDocs
 * 23 April 2025 - Removed process shelter
 * 25 April 2025 - Updated interface with new functions
 * 30 June 2024 - Removed separate process for give
 * 3 December 2025 - Increased version number
 */
