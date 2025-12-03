/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.Constants;

/**
 * Represents the player in the game.
 * <p>
 * The player has stats such as strength, wisdom, time, weight, food, and drink. 
 * They can move between rooms, swim, and their stats change over time.
 */
public class Player implements Serializable {
	
	private static final long serialVersionUID = 495300605316911022L;
	private static final Logger logger = Logger.getLogger(Player.class.getName());
	
	private int room = 23;
	private int roomToDisplay = this.room;
	private final Map<String,Object> stats = new HashMap<>();
	private final Random rand = new Random();
	private enum PlayerState { NORMAL,SWIMMING };
	private PlayerState playerState = PlayerState.NORMAL;
	private Swimming swim;
		
	private static final int RANDOM_ROOM_TRIGGER = 20;
	
    /**
     * Creates a new player with default starting stats.
     */
	public Player() {
		
		//Initialize Stats
		stats.put("strength", Constants.STARTING_STRENGTH);			
		stats.put("wisdom", Constants.STARTING_WISDOM);				
		stats.put("timeRemaining",Constants.STARTING_TIME);		
		stats.put("weight", 0);
		stats.put("food",2);
		stats.put("drink", 2);
	}
	
    /**
     * Gets the current display room for the player.
     *
     * @return the room number to display
     */
	public int getDisplayRoom() {
		return this.roomToDisplay;
	}
	
    /**
     * Updates and returns the room to display.
     * If the player is in the {@link #RANDOM_ROOM_TRIGGER}, 
     * they are shown a random room instead.
     *
     * @return the updated display room
     */
	public int updateDisplayRoom() {
		
		this.roomToDisplay = this.room;
		
		if (this.room == RANDOM_ROOM_TRIGGER) {
			this.roomToDisplay = rand.nextInt(Constants.NUMBER_OF_ROOMS-1)+1;
		}
		
		return this.roomToDisplay;
	}
	
    /**
     * Applies turn-based updates to the player's stats.
     * Decreases time remaining and reduces strength based on weight carried.
     */
	public void turnUpdateStats() {
		logger.info(stats.get("timeRemaining").toString());
		int timeRemaining = (int) stats.get("timeRemaining");
		stats.put("timeRemaining", timeRemaining-1);
		logger.info(stats.get("timeRemaining").toString());
		
		logger.info(stats.get("strength").toString());
		float strength = (float) stats.get("strength");
		int weight = (int) stats.get("weight");
		stats.put("strength", strength - (weight/Constants.NUMBER_OF_ITEMS+0.1f));
		logger.info(stats.get("strength").toString());
	}
	
    /**
     * Gets a combined value of strength and wisdom.
     *
     * @return strength + wisdom
     */
	public float getStrengthWisdon() {
		return ((float) stats.get("strength"))+((int) getStat("wisdom"));
	}

    /** @return the current room number */
	public int getRoom() {
		return this.room;
	}
	
    /**
     * Sets the player's current room.
     *
     * @param room the new room number
     */
	public void setRoom(int room) {
		logger.log(Level.INFO, "Player moved to room: " + room);

		this.room = room;
		this.roomToDisplay = room;
	}
	
    /**
     * Retrieves a stat by name.
     *
     * @param statName the stat key (e.g., "strength", "wisdom")
     * @return the stat value, or {@code null} if not found
     */
	public Object getStat(String statName) {
		return stats.get(statName);
	}
	
    /**
     * Updates a stat.
     *
     * @param statName the stat key
     * @param value    the new value
     */
	public void setStat(String statName,Object value) {
		logger.info("Adjust "+statName+" by "+value);
		stats.put(statName,value);
	}
	
    /** @return the {@link Swimming} instance if player is swimming */
	public Swimming getSwimming() {
		return swim;
	}
	
    /**
     * Sets the player's swimming state handler.
     *
     * @param swim the swimming instance
     */
	public void setSwimming(Swimming swim) {
		this.swim = swim;
	}
	
    /**
     * Reduces a named stat by 1.
     *
     * @param statName the stat key
     */
	public void reduceStat(String statName) {
		int stat = (int) stats.get(statName);
		if(stat>0) {
			stats.put(statName, stat-1);
		}
	}
	
    /** Sets the player back to the {@link PlayerState#NORMAL} state. */
	public void setPlayerStateNormal() {
		playerState = PlayerState.NORMAL;
		swim = null;
	}
	
    /** Puts the player into the {@link PlayerState#SWIMMING} state. */
	public void setPlayerStateSwimming() {
		playerState = PlayerState.SWIMMING;
		setSwimming(new Swimming());
	}
	
    /**
     * Checks if the player is in the {@link PlayerState#NORMAL} state.
     *
     * @return true if normal
     */
	public boolean isPlayerStateNormal() {
		return playerState == PlayerState.NORMAL;
	}
	
    /**
     * Checks if the player is in the {@link PlayerState#SWIMMING} state.
     *
     * @return true if swimming
     */
	public boolean isPlayerStateSwimming() {
		return playerState == PlayerState.SWIMMING;
	}		
		
    /** @return formatted string of strength and wisdom */
	public String toStringStatus() {
		return String.format("Strength: %.2f         wisdom: %d", stats.get("strength"),stats.get("wisdom"));
	}

    /** @return formatted string of time remaining */
	public String toStringTimeRemaining() {
		return String.format("Time Remaining: %d",stats.get("timeRemaining"));
	}
	
    /** {@inheritDoc} */
	@Override
	public String toString() {
	    return "Player{" +
	            "room=" + room +
	            ", strength=" + stats.get("strength") +
	            ", wisdom=" + stats.get("wisdom")  +
	            ", timeRemaining=" + stats.get("timeRemaining")  +
	            ", weight=" + stats.get("weight") +
	            ", food=" + stats.get("food") +
	            ", drink=" + stats.get("drink") +
	            '}';
	}
	
}

/* 2 November 2024 - Create File
 * 3 November 2024 - Added methods to manipulate the value of the room
 * 				   - Added method to return the player status as a string
 * 5 November 2024 - Added code to randomise location is at room 20
 * 13 November 2024 - Added method to update current room
 * 17 November 2024 - Added getters & setters
 * 25 November 2024 - Added flag for give (though will need to be changed for other screens)
 * 29 November 2024 - Added getter for drink
 * 1 December 2024 - Added adjust function for strength/wisdom/food/drink
 * 9 December 2024 - Made class serializable
 * 16 December 2024 - Added methods to handle swimming in poisoned waters section
 * 23 December 2024 - Updated to version 2.
 * 1 January 2025 - Removed the changeRoom method as duplicates setRoom.
 * 31 January 2025 - Completed Testing and increased version
 * 1 February 2025 - Added serializable ID and removed unused variables.
 * 5 March 2025 - Increased to v4.0
 * 11 March 2025 - Fixed issues and moved stats to a map
 * 12 March 2025 - Removed timeRemaining stats. Removed wisdom, strength, weight getters & setters
 * 14 March 2025 - Removed food & Drink
 * 15 March 2025 - Removed Swimming and added check if swimming. Added toString method.
 * 17 March 2025 - Added logging to list room player has entered.
 * 25 March 2025 - Added Enums Player State. Removed Message and Lightning states (should be in game)
 * 27 March 2025 - Added Swimming class to store swimming state. Created Start Swimming state
 * 25 June 2025 - Restored strength to correct starting value. Moved starting constants to the constants file
 * 19 July 2025 - Removed start swimming state
 * 20 July 2025 - Removed setting the swim position. Removed START_SWIM state
 * 19 August 2025 - Made minor fixes. Added JavaDocs
 * 18 October 2025 - Added logger for when stat changes
 * 3 December 2025 - Increased version number
 */