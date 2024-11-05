/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.2
Date: 5 November 2024
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
	
	public int getRoom() {
		return this.room;
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
	
}

/* 2 November 2024 - Create File
 * 3 November 2024 - Added methods to manipulate the value of the room
 * 				   - Added method to return the player status as a string
 * 5 November 2024 - Added code to randomise location is at room 20
 */