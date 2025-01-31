/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 2.5
Date: 31 January 2025
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
	
	private static final long serialVersionUID = 1L;
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
	private int saveGameCount = 0;
	private int responseRequired = 0;
	private int appleCount = 3;

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
			
			if(itemNumber<=Constants.noItems) {
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
		
		if (roomNumber == 45) {
			items = "A tree bristling with apples";
			count ++;
		} else if (roomNumber == 27) {
			
			items = "Torches illuminating the room";
			if (itemList[7].getLocation()==27 && itemList[7].getFlag()==9) {
				items += ", one of which is within reach";
			}
			count ++;
		}
		
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
		
		boolean[] exitNumbers = locationList[roomNumber].getExits();
		String exits = "";
		
		if (roomNumber == 39) {
			int randExit = rand.nextInt(5);
			boolean[] exitArray = {false,true,false,false,false,true,false,true,true};
			int count = 0;
			
			for (int x=randExit;x<randExit+4;x++) {
				exitNumbers[count]=exitArray[x];
				count ++;
			}
		}
		
		//Checks if the exit is a special exit. If not, displays it normally.
		if (exitNumbers[0] && (roomNumber != 70 && roomNumber != 37 && roomNumber != 11 &&
							   roomNumber != 41 && roomNumber != 43 && roomNumber != 66)) {
			exits = addExit("North",exits);
		}
		
		if (exitNumbers[1] && (roomNumber != 60 && roomNumber !=56)) {
			exits = addExit("South",exits);
		}
		
		if (exitNumbers[2] && (roomNumber != 44 && roomNumber != 52)) {
			exits = addExit("East",exits);
		}
		
		if (exitNumbers[3] && (roomNumber !=12 && roomNumber !=53 && roomNumber !=45)) {
			exits = addExit("West",exits);
		}
		
		if (exits.length()>0) {
			exits = String.format("You can go:%s",exits);
		}
		
		return exits;
	}
	
	//Displays the special location.
	public String getSpecialExits(int roomNumber) {
		
		String exit = "";
		
		//Checks the room number, and displays the special location.
		if (roomNumber == 51) {
			exit = "There is a door to the east";
			if (itemList[29].getFlag()!=0) {
				exit += " and a closed trapdoor in the floor";
			}
		} else if (roomNumber == 12) {
			exit = "You can also go west into the cave";
		} else if (roomNumber == 53 || roomNumber == 45) {
			exit = "You can also go west into the hut";
		} else if (roomNumber == 70) {
			exit = "You can also go north into the hut";
		} else if (roomNumber == 37) {
			exit = "You can also go north into the portal";
		} else if (roomNumber == 11) {
			exit = "You can also go north out of the lair";
		} else if (roomNumber == 41) {
			exit = "You can also go north out of the hut";
		} else if (roomNumber == 43) {
			exit = "You can also go north out of the cabin";
		} else if (roomNumber == 66) {
			exit = "You can also go north down of the pyramid";
		} else if (roomNumber == 60) {
			exit = "You can also go south out of the hut";
		} else if (roomNumber == 56) {
			exit = "You can also go south up the pyramid";
		} else if (roomNumber == 44) {
			exit = "You can go east out of the shack";
		} else if (roomNumber == 52) {
			exit = "You can go east out of the hall";
		}		
		return exit;
	}
	
	//Checks if it is possible to move through the exit
	public boolean checkExit(int room, int direction) {
	
		return locationList[room].getExits()[direction];
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
	
	//Cycle through number of save games
	public int getCount() {
		return this.saveGameCount;
	}
	
	public void setCount() {
		this.saveGameCount++;
	}
	
	public void resetCount() {
		this.saveGameCount=0;
	}
	
	//Set the response required for the input
	//0 - Standard Response
	//1 - Give Response
	//2 - Shelter Response
	public void setResponse(int responseType) {
		this.responseRequired = responseType;
	}
	
	public int getResponse() {
		return this.responseRequired;
	}
	
	//Checks how many apples left
	public boolean checkApples() {
		
		boolean applesLeft = false;
		
		if (appleCount>0) {
			appleCount --;
			applesLeft = true;
		}
		
		return applesLeft;
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
 * 20 December 2024 - Added count for displaying saved games available.
 * 22 December 2024 - Added response required for give and shelter
 * 23 December 2024 - Updated to version 2.
 * 25 December 2024 - Changed exits to boolean
 * 29 December 2024 - Added function for getting special exits
 * 30 December 2024 - Added more detail for some of the directions, and removed them from the main direction list.
 * 3 January 2024 - Fixed problem where Median wasn't being loaded.
 * 30 January 2025 - Added code for number of apples. Also added code to display apple tree.
 * 31 January 2025 - Added code to display torches in room.
 */