/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.8
Date: 25 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import Data.Constants;
import Model.Swimming;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 495300605316911022L;
	private static final Logger logger = Logger.getLogger(Game.class.getName());
	
	private int room = 23;
	private int roomToDisplay = this.room;
	private final Map<String,Object> stats = new HashMap<>();
	private final Random rand = new Random();
	private enum PlayerState { NORMAL,START_SWIM,SWIMMING };
	private PlayerState playerState = PlayerState.NORMAL;
	private Swimming swim;
		
	private static final int RANDOM_ROOM_TRIGGER = 20;
	
	public Player() {
		
		//Initialize Stats
		stats.put("strength", Constants.STARTING_STRENGTH);			
		stats.put("wisdom", Constants.STARTING_WISDOM);				
		stats.put("timeRemaining",Constants.STARTING_TIME);		
		stats.put("weight", 0);
		stats.put("food",2);
		stats.put("drink", 2);
	}
	
	public int getDisplayRoom() {
		return this.roomToDisplay;
	}
		
	public int updateDisplayRoom() {
		
		this.roomToDisplay = this.room;
		
		if (this.room == RANDOM_ROOM_TRIGGER) {
			this.roomToDisplay = rand.nextInt(Constants.NUMBER_OF_ROOMS-1)+1;
		}
		
		return this.roomToDisplay;
	}
	
	public void turnUpdateStats() {
		
		int timeRemaining = (int) stats.get("timeRemaining");
		stats.put("timeRemaining", timeRemaining-1);
		
		float strength = (float) stats.get("strength");
		int weight = (int) stats.get("weight");
		stats.put("strength", strength - (weight/Constants.NUMBER_OF_ITEMS+0.1f));
	}
		
	public float getStrengthWisdon() {
		return ((float) stats.get("strength"))+((int) getStat("wisdom"));
	}

	//Getters & Setters
	public int getRoom() {
		return this.room;
	}
	
	public void setRoom(int room) {
		logger.log(Level.INFO, "Player moved to room: " + room);

		this.room = room;
		this.roomToDisplay = room;
	}
		
	public Object getStat(String statName) {
		return stats.get(statName);
	}
	
	public void setStat(String statName,Object value) {
		stats.put(statName,value);
	}
	
	public Swimming getSwimming() {
		return swim;
	}
	
	public void setSwimming(Swimming swim) {
		this.swim = swim;
	}
	
	public void reduceStat(String statName) {
		int stat = (int) stats.get(statName);
		stat --;
		stats.put(statName, stat);
	}
	
	public void setPlayerStateNormal() {
		playerState = PlayerState.NORMAL;
	}
	
	public void setPlayerStateStartSwimming() {
		playerState = PlayerState.START_SWIM;
	}
	
	public void setPlayerStateSwimming() {
		playerState = PlayerState.SWIMMING;
	}
	
	public boolean isPlayerStateNormal() {
		boolean state = false;
		if (playerState == PlayerState.NORMAL) {
			state = true;
		}
		return state;
	}
	
	public boolean isPlayerStateStartSwimming() {
		boolean state = false;
		if (playerState == PlayerState.START_SWIM) {
			state = true;
		}
		return state;
	}	
	
	public boolean isPlayerStateSwimming() {
		boolean state = false;
		if (playerState == PlayerState.SWIMMING) {
			state = true;
		}
		return state;
	}		
		
	//ToString Methods
	public String toStringStatus() {
		return String.format("Strength: %.2f         wisdom: %d", stats.get("strength"),stats.get("wisdom"));
	}

	public String toStringTimeRemaining() {
		return String.format("Time Remaining: %d",stats.get("timeRemaining"));
	}
	
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
 */