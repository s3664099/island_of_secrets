/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.9
Date: 16 December 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.io.Serializable;
import java.util.Random;

import Data.Constants;

public class Player implements Serializable {
	
	private int room = 23;
	private int roomToDisplay = this.room;
	private float strength = 100;
	private int wisdom = 35;
	private int timeRemaining = 1000;
	private int weight = 0;
	private int food = 2;
	private int drink = 2;
	private Random rand = new Random();
	private int panelFlag = 0;
	private int swimming = 0;
	private int swimPosition = 0;
	private int swimTarget = 0;
	
	public int getDisplayRoom() {
		return this.roomToDisplay;
	}
		
	public int updateDisplayRoom() {
		
		this.roomToDisplay = this.room;
		
		if (this.room == 20) {
			this.roomToDisplay = rand.nextInt(Constants.noRooms-1)+1;
		}
		
		return this.roomToDisplay;
	}
	
	public void changeRoom(int room) {
		this.room = room;
	}
	
	public String getStatus() {
		return String.format("Strength: %.2f         wisdom: %d", this.strength,this.wisdom);
	}
	
	public void update() {
		this.timeRemaining --;
		this.strength = (float) (this.strength-(this.weight/Constants.noItems+.1));
	}
		
	public float getStrengthWisdon() {
		return this.strength+this.wisdom;
	}

	//Getters & Setters
	public int getRoom() {
		return this.room;
	}
	
	public void setRoom(int newRoom) {
		this.room = newRoom;
	}
	
	//Time getter/setter
	public String getTimeDetails() {
		return String.format("Time Remaining: %d",this.timeRemaining);
	}
	
	public int getTime() {
		return this.timeRemaining;
	}
	
	public void reduceTime() {
		this.timeRemaining--;
	}
	
	public void setTime(int newTime) {
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
		this.weight = weight;
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
 */