/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.4
Date: 17 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.util.Random;

import Data.Constants;

public class Player {
	
	private int room = 23;
	private int roomToDisplay = this.room;
	private float strength = 100;
	private int wisdom = 35;
	private int timeRemaining = 1000;
	private int weight = 0;
	private int food = 2;
	private int drink = 2;
	private Random rand = new Random();
	
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

	public String getTime() {
		return String.format("Time Remaining: %d",this.timeRemaining);
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
	
	public int getWisdom() {
		return this.wisdom;
	}
	
	public float getStrength() {
		return this.strength;
	}
	
	public void setWisdom(int newWisdom) {
		this.wisdom = newWisdom;
	}
	
	public void setStrength(float newStrength) {
		this.strength = newStrength;
	}
	
	public void adjustFood(int change) {
		this.food += change;
	}
	
	public void adjustDrink(int change) {
		this.drink += change;
	}
	
}

/* 2 November 2024 - Create File
 * 3 November 2024 - Added methods to manipulate the value of the room
 * 				   - Added method to return the player status as a string
 * 5 November 2024 - Added code to randomise location is at room 20
 * 13 November 2024 - Added method to update current room
 * 17 November 2024 - Added getters & setters
 */