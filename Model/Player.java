/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.1
Date: 3 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

public class Player {
	
	private int room = 23;
	private float strength = 100;
	private int wisdom = 35;
	private int timeRemaining = 1000;
	private int weight = 0;
	private int food = 2;
	private int drink = 2;
	
	public int getRoom() {
		return this.room;
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
 */