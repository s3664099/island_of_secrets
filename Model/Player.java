/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 11 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Data.Constants;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 495300605316911022L;
	private int room = 23;
	private int roomToDisplay = this.room;
	private final Map<String,Object> stats = new HashMap<>();
	private final Random rand = new Random();
	private int panelFlag = 0;
	private int swimming = 0;
	private int swimPosition = 0;
		
	private static final int RANDOM_ROOM_TRIGGER = 20;
	
	public Player() {
		
		//Initialize Stats
		stats.put("strength", 100.0f);
		stats.put("wisdom", 35);
		stats.put("timeRemaining",1000);
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
	
	public String toStringStatus() {
		return String.format("Strength: %.2f         wisdom: %d", stats.get("strength"),stats.get("wisdom"));
	}
	
	public void turnUpdateStats() {
		
		int timeRemaining = (int) stats.get("timeRemaining");
		stats.put("timeRemaining", timeRemaining-1);
		
		float strength = getStrength();
		int weight = (int) stats.get("weight");
		stats.put("strength", strength - (weight/Constants.NUMBER_OF_ITEMS+0.1f));
	}
		
	public float getStrengthWisdon() {
		return getStrength()+getWisdom();
	}

	//Getters & Setters
	public int getRoom() {
		return this.room;
	}
	
	public void setRoom(int room) {
		this.room = room;
	}
	
	//Time getter/setter
	public String toStringTimeRemaining() {
		return String.format("Time Remaining: %d",stats.get("timeRemaining"));
	}
	
	public Object getStat(String statName) {
		return stats.get(statName);
	}
	
	public void setStat(String statName,Object value) {
		stats.put(statName,value);
	}
		
	public void reduceTime() {
		this.timeRemaining--;
	}
	
	public void setTime(int newTime) {
		
		if (newTime<0) {
			throw new IllegalArgumentException("Player - Time cannot be negative");
		}
		
		this.timeRemaining = newTime;
	}

	//Wisdom getter/Setter
	public int getWisdom() {
		return this.wisdom;
	}

	public void setWisdom(int newWisdom) {
		
		this.wisdom = newWisdom;
	}
	
	public void adjustWisdom(int change) {
		this.wisdom += change;
	}
	
	//Strength getter/setter
	public float getStrength() {
		return this.strength;
	}

	public void setStrength(float newStrength) {
		this.strength = newStrength;
	}
	
	public void adjustStrength(float change) {
		this.strength += change;
	}
	
	//Weight getter/setter
	public int getWeight() {
		return this.weight;
	}
	
	public void setWeight(int newWeight) {
		this.weight = newWeight;
	}
	
	public void adjustWeight(int change) {
		this.weight += change;
	}
	
	//Food getter/setter
	public void adjustFood(int change) {
		this.food += change;
	}

	public int getFood() {
		return this.food;
	}
	
	//drink getter/setter
	public void adjustDrink(int change) {
		this.drink += change;
	}
	
	public int getDrink() {
		return this.drink;
	}
	
	/*     Flag Settings:
	 * 		0 - Normal Screen
	 * 		1 - Give Screen
	 * 		2 - Lightning Flashes 
	 */
	public void setPanelFlag(int panelFlag) {
		
		if (panelFlag<0) {
			throw new IllegalArgumentException("Player - Panel Flag Cannot be less than 0");
		}
		
		this.panelFlag = panelFlag;
	}
	
	public int getPanelFlag() {
		return this.panelFlag;
	}
	
	//Methods for handling the swimming in poisoned waters
	public void setSwimming() {
		this.swimming = this.room;
	}
		
	public void adjustPosition() {
		this.swimPosition++;
	}
	
	public void resetPosition() {
		this.swimPosition = 0;
	}
	
	public boolean checkPosition() {
		
		boolean checked = false;
		
		if ((this.swimming/2)<this.swimPosition && this.strength>0) {
			checked = true;
		}
		
		return checked;
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
 */