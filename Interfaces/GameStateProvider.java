/*
Title: Island of Secrets Read Operation Interface
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 14 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Interfaces;

import java.util.List;

/**
 * Provides read-only access to game state information.
 * Segregates query operations for game status, map data, and UI state.
 */
public interface GameStateProvider {

    /* Core Game Status Methods */
    
    /**
     * @return Formatted description of current room
     */
	String getRoom();
	
    /**
     * @return Formatted list of visible items in current location
     */
	String getItems();
	
    /**
     * @return Formatted list of available exits
     */
	String getExits();
	
    /**
     * @return Current in-game time representation
     */
	String getTime();

	/**
     * @return The player's current score
     */
	int getFinalScore();
	
    /**
     * @return Special exit descriptions beyond standard directions
     */
	String getSpecialExits();
	
	 /**
     * @return Current player status (health, inventory count, etc.)
     */
	String getStatus();
	
    /**
     * @return Array of saved game names for display
     */
	String[] getDisplayedSavedGames();
	
    /**
     * @return True if at beginning of saved games list
     */
	boolean getLowerLimitSavedGames();
	
    /**
     * @return True if at end of saved games list
     */
	boolean getUpperLimitSavedGames();
	
    /**
     * @return Last executed commands (for history/recall)
     */
	String[] getCommands();
	
    /**
     * @return Unmodifiable list of game messages/notifications
     */
	List<String> getMessage();
	
    /* Map Data Access */
    
    /**
     * @param roomNumber The room ID to check
     * @return True if player has visited this room
     */
	boolean getRoomVisited(int roomNumber);
	
    /**
     * @param roomNumber The room ID to check
     * @return Array of available exits [N, S, E, W] 
     */
	boolean[] getRoomExits(int roomNumber);
	
    /**
     * @param roomNumber The room ID to check
     * @return Image asset identifier for this room type
     */
	String getRoomImageType(int roomNumber);
	
    /**
     * @return Current room ID
     */
	int getCurrentRoom();
	
    /* UI State Flags */
    
    /**
     * @return True during initial game setup
     */
	public boolean isInitialGameState();
	
    /**
     * @return True when in saved game selection mode
     */
	public boolean isSavedGameState();
	
    /**
     * @return True when game has ended
     */
	public boolean isEndGameState();

    /**
     * @return Current UI response type (normal/give/shelter)
     */
	public int getResponseType();
	
    /**
     * @return Current UI panel configuration flag
     */
	public int getPanelFlag();
}

/* 24 March 2025 - Created File
 * 25 March 2025 - Added further required methods
 * 4 April 2025 - Added functions for handling the mapPanel
 * 14 April 2025 - Added JavaDocs
 */
