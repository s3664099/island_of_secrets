/*
Title: Island of Secrets Read Operation Interface
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package interfaces;

import java.util.List;

/**
 * Provides read-only access to game state information.
 * Segregates query operations for game status, map data, and UI state.
 */
public interface GameStateProvider {

    /* Core Game Status Methods */
    
    /** @return Formatted description of current room*/
	String getRoom();
	
    /** @return Formatted list of visible items in current location*/
	String getItems();
	
    /**
     * @return Formatted list of available exits
     */
	String getExits();
	
    /** @return Current in-game time representation */
	String getTime();

	/** @return The player's current score */
	int getFinalScore();
	
    /** @return Special exit descriptions beyond standard directions */
	String getSpecialExits();
	
	 /** @return Current player status (health, inventory count, etc.) */
	String getStatus();
	
    /** @return Array of saved game names for display */
	String[] getDisplayedSavedGames();
	
    /** @return True if at beginning of saved games list */
	boolean getLowerLimitSavedGames();
	
    /** @return True if at end of saved games list */
	boolean getUpperLimitSavedGames();
	
    /** @return Last executed commands (for history/recall) */
	String[] getCommands();
	
    /** @return Unmodifiable list of game messages/notifications */
	List<String> getMessage();
	
    /** @return Unmodifiable list of panel messages */
	List<String> getPanelMessage();
	
    /* Map Data Access */
    
    /**
     * @param roomNumber The room ID to check
     * @return the name of the room
     */
	String getRoomName(int roomNumber);
	
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
	
    /** @return Current room ID */
	int getCurrentRoom();
	
    /* UI State Flags */
    
    /** @return True during initial game setup */
	public boolean isInitialGameState();
	
    /** @return True when in saved game selection mode */
	public boolean isSavedGameState();
	
    /** @return True when game has ended */
	public boolean isEndGameState();
	
    /** @return True when game has restarted */
	public boolean isRestartGameState();
	
    /** @return True when a give response is required */
	public boolean isGiveState();
	
    /** @return True when seeking shelter */
	public boolean isShelterState();

    /** @return True when standard game state */
	public boolean isRunningState();
	
    /** @return True when swimming player state */
	public boolean isSwimmingState();
	
    /** @return True when normal player state */
	public boolean isNormalState();
	
    /** @return True when message player state */
	public boolean isMessageState();
}

/* 24 March 2025 - Created File
 * 25 March 2025 - Added further required methods
 * 4 April 2025 - Added functions for handling the mapPanel
 * 14 April 2025 - Added JavaDocs
 * 20 April 2025 - Added getRoomName
 * 23 April 2025 - Removed response required
 * 25 April 2025 - Added 
 * 24 July 2025 - Added getPanelMessage
 * 1 October 2025 - Tightened Comments
 * 6 November 2025 - Added restart game check
 * 23 November 2025 - Removed Lightning State
 * 3 December 2025 - Increased version number
 */
