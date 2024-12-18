/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.13
Date: 15 December 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.io.Serializable;
import java.util.Random;

import Data.Constants;
import Data.Item;
import Data.Location;
import Data.RawData;

public class Game implements Serializable {
	
	private int noRooms = Constants.noRooms;
	private int noItems = Constants.noNouns;
	private Location[] locationList = new Location[noRooms+1];
	private Item[] itemList = new Item[noItems+1];
	private String message = "Let your quest begin!";
	private String[] commands = {"","",""};
	private Random rand = new Random();
	private String panelMessageOne;
	private String panelMessageTwo;
	private int panelLoop;
	private boolean endGame = false;

	public Game() {
				
		locationList[0] = null;
		itemList[0] = null;
		
		//Builds the location objects
		for (int roomNumber=0;roomNumber<noRooms;roomNumber++) {
			
			Location newLocation = new Location(RawData.getLocation(roomNumber),
												RawData.getPrepositions());
			locationList[roomNumber+1] = newLocation;
		}
		
		//Builds the item objects
		for (int itemNumber=1;itemNumber<noItems+1;itemNumber++) {
			
			String item = "";
			
			if(itemNumber<Constants.noItems) {
				item = RawData.getObjects(itemNumber);
			}
			
			Item newItem = new Item(RawData.getItemFlag(itemNumber),
									RawData.getItemLocation(itemNumber),
									item);			
			itemList[itemNumber] = newItem;
		}
	}
	
	public String getRoomName(int roomNumber) {
		return this.locationList[roomNumber].getName();
	}
	
	//Goes through the items and checks what is present
	public String getItems(int roomNumber) {
				
		int count = 0;
		String items = "";
		
		//Goes through each of the items
		for (Item item:itemList) {
			if(item != null) {
				
				//If the items are visible display them.
				if(item.checkLocation(roomNumber) && item.getFlag()<1) {
					
					count ++;
					if (count>1) {
						items = String.format("%s, %s",items,item.getItem());
					} else {
						items = String.format("%s %s",items,item.getItem());
					}
				}
			}
		}
		
		if (count>0) {
			items = String.format("%s %s","You see:",items);
		}
		
		return items;
	}
	
	//Returns a display of the available exits.
	public String getExits(int roomNumber) {
		
		int[] exitNumbers = locationList[roomNumber].getExits();
		String exits = "";
		
		if (roomNumber == 39) {
			int randExit = rand.nextInt(5);
			int[] exitArray = {1,0,1,1,1,0,1,0,0};
			int count = 0;
			
			for (int x=randExit;x<randExit+4;x++) {
				exitNumbers[count]=exitArray[x];
				count ++;
			}
		}
		
		if (exitNumbers[0] == 0) {
			exits = addExit("North",exits);
		}
		
		if (exitNumbers[1] == 0) {
			exits = addExit("South",exits);
		}
		
		if (exitNumbers[2] == 0) {
			exits = addExit("East",exits);
		}
		
		if (exitNumbers[3] == 0) {
			exits = addExit("West",exits);
		}
		
		if (exits.length()>0) {
			exits = String.format("You can go:%s",exits);
		}
		
		return exits;
	}
	
	//Checks if it is possible to move through the exit
	public boolean checkExit(int room, int direction) {
		
		boolean open = false;
		
		if (locationList[room].getExits()[direction]==0) {
			open = true;
		}
		
		return open;
	}
	
	//Checks to see if an exit has already been added
	private String addExit(String exit, String exits) {
		
		if (exits.length()>0) {
			exits = String.format("%s, %s",exits,exit);
		} else {
			exits = String.format("%s %s",exits,exit);
		}
		
		return exits;
	}
	
	//Retrieves the message
	public String getMessage() {
		return this.message;
	}
	
	//Resets the message
	public void clearMessage() {
		this.message = "";
	}
	
	//sets the message
	public void setMessage(String message) {
		
		this.message = message;
	}
	
	//Extends the message
	public void addMessage(String message) {
		
		if (this.message.length()>0) {
			
			if (this.message.endsWith("|")) {
				this.message = String.format("%s %s", this.message, message);
			} else {
				this.message = String.format("%s, %s", this.message, message);
			}
		}  else {
			this.message = message;
		}		
	}
	
	public String getCommand(int number) {
		return commands[number];
	}
	
	public Item getItem(int itemNumber) {		
		return itemList[itemNumber];
	}
	
	//Gets the sum of the item's flag and location
	public int getItemFlagSum(int itemNumber) {
		return itemList[itemNumber].getFlag() + itemList[itemNumber].getLocation();
	}
	
	//Sets up and gets the panel messages
	public void setPanelMessages(String messageOne,String messageTwo,int loop) {
		this.panelMessageOne = messageOne;
		this.panelMessageTwo = messageTwo;
		this.panelLoop = loop;
	}
	
	public String getMsgOne() {
		return this.panelMessageOne;
	}
	
	public String getMsgTwo() {
		return this.panelMessageTwo;
	}
	
	public int getLoop() {
		return this.panelLoop;
	}
	
	
	//Flag to determine whether the game has ended.
	public void endGame() {
		this.endGame = true;
	}
	
	public boolean checkEndGame() {
		return this.endGame;
	}
}

/* 30 October 2024 - Created File
 * 31 October 2024 - Added description to the locations.
 * 				   - Adjusted way to extract rooms
 * 1 November 2024 - Added the items
 * 3 November 2024 - Added method to retrieve the player's current location
 * 4 November 2024 - Added method to retrieve items at the player's location.
 * 5 November 2024 - Updated get items method. Added get exits method
 * 				   - added methods to deal with the message
 * 10 November 2024 - Removed Description for location & items
 * 11 November 2024 - Added second method so that only some messages are extended.
 * 13 November 2024 - Added method to check if exit available
 * 17 November 2024 - Added code to retrieve flag and location sum
 * 1 December 2024 - Added variables to hold messages to go into the panel.
 * 8 December 2024 - Updated the add message method so comma doesn't appear at start
 * 9 December 2024 - Made class serializable
 * 15 December 2024 - Added flag to end game
 */