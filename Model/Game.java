/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 16 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.io.Serializable;
import java.util.Random;

import Data.Constants;
import Data.Item;
import Data.Location;

public class Game implements Serializable {
	
	private static final long serialVersionUID = 3473676803014192040L;
	
	private Location[] locationList;
	private Item[] itemList;
	private SpecialExitHandler specialExitHandler;
	private SpecialItemHandler specialItemHandler = new SpecialItemHandler();
	
	private String message = "Let your quest begin!";
	private boolean newMessage = false;
	private String[] commands = {"","",""};
	private Random rand = new Random();
	private String panelMessageOne;
	private String panelMessageTwo;
	private int panelLoop;
	private boolean endGame = false;
	private int saveGameCount = 0;
	private int responseRequired = 0;
	private boolean displayGames = false;
	private boolean moreGames = false;
	private boolean lessGames = false;
	private boolean start = true;
	private String[] gameDisplayed = {"","","","",""};
	
	private int APPLE_COUNT = 3;
	private int START_LOCATION = 23;
	private int RANDOM_ROOM = 39;
	private int RANDOM_EXIT_COMBO = 5;
	
	private String NORTH = "North";
	private String SOUTH = "South";
	private String EAST = "East";
	private String WEST = "West";

	public Game(Location[] locations, Item[] items,SpecialExitHandler specialExitHandler) {
		
		this.locationList = locations;
		this.itemList = items;
		this.specialExitHandler = specialExitHandler;
		
		//sets start location
		locationList[START_LOCATION].setVisited();
		
	}
	
	public String getRoomName(int roomNumber) {
		return this.locationList[roomNumber].getName();
	}
	
	//Goes through the items and checks what is present
	public String getItems(int roomNumber) {
				
		int count = 0;
		String items = specialItemHandler.getSpecialItems(roomNumber, itemList, locationList);

		if (items.length()>0) {
			count ++;
		}
		
		//Goes through each of the items
		for (Item item:itemList) {
			if(item != null) {
				
				//If the items are visible display themString.
				if(item.isAtLocation(roomNumber) && item.getItemFlag()<1) {
					
					count ++;
					if (count>1) {
						items = String.format("%s, %s",items,item.getItemName());
					} else {
						items = String.format("%s %s",items,item.getItemName());
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
		SpecialExitHandler exitHandler = new SpecialExitHandler();
		
		if (roomNumber == RANDOM_ROOM) {
			int randExit = rand.nextInt(RANDOM_EXIT_COMBO);
			boolean[] exitArray = {false,true,false,false,false,true,false,true,true};
			int count = 0;
			
			for (int x=randExit;x<randExit+Constants.NUMBER_EXITS;x++) {
				exitNumbers[count]=exitArray[x];
				count ++;
			}
		}
		
		//Checks if the exit is a special exit. If not, displays it normally.
		if (exitNumbers[0] && (exitHandler.displayExit(roomNumber,NORTH))) {
			exits = addExit(NORTH,exits);
		}
		
		if (exitNumbers[1] && (exitHandler.displayExit(roomNumber,SOUTH))) {
			exits = addExit(SOUTH,exits);
		}
		
		if (exitNumbers[2] && (exitHandler.displayExit(roomNumber,EAST))) {
			exits = addExit(EAST,exits);
		}
		
		if (exitNumbers[3] && (exitHandler.displayExit(roomNumber,WEST))) {
			exits = addExit(WEST,exits);
		}
		
		if (exits.length()>0) {
			exits = String.format("You can go:%s",exits);
		}
		
		return exits;
	}
	
	//Displays the special location.
	public String getSpecialExits(int roomNumber) {
		return specialExitHandler.getSpecialExit(roomNumber, itemList);	
	}
	
	//Checks if it is possible to move through the exit
	public boolean checkExit(int room, int direction) {
		return locationList[room].getExits()[direction];
	}
	
	//Returns the room based on the number passed through
	public Location getRoom(int roomNumber) {
		return locationList[roomNumber];
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
		this.newMessage = false;
	}
		
	//Extends the message
	public void addMessage(String message) {
		
		if (this.newMessage) {
			
			if (this.message.endsWith(".")) {
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
		return itemList[itemNumber].getItemFlag() + itemList[itemNumber].getItemLocation();
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
	
	public void increaseCount() {
		this.saveGameCount++;
	}
	
	public void descreaseCount() {
		this.saveGameCount--;
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
		
		if (APPLE_COUNT>0) {
			APPLE_COUNT --;
			applesLeft = true;
		}
		
		return applesLeft;
	}
	
	//Getters & Setters for displaying games
	public void setGameDisplay(boolean display) {
		this.displayGames = display;
	}
	
	public boolean getGameDisplay() {
		return this.displayGames;
	}
	
	public void setMoreGames(boolean moreGames) {
		this.moreGames = moreGames;
	}
	
	public boolean getMoreGames() {
		return this.moreGames;
	}
	
	public void setLessGames(boolean lessGames) {
		this.lessGames = lessGames;
	}
	
	public boolean getLessGames() {
		return this.lessGames;
	}
	
	public String[] getDisplayedGames() {
		return this.gameDisplayed;
	}
	
	public void setDisplayedGames(String[] gameDisplayed) {
		this.gameDisplayed = gameDisplayed;
	}	
	
	//Checks if the start of game
	public boolean checkStart() {
		
		boolean start = true;
		
		if (this.start) {
			this.start = false;
		} else {
			start = false;
		}
		
		return start;
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
 * 31 January 2025 - Completed Testing and increased version
 *                   Added code to display torches in room.
 *                   Updated the description in Grandpa's shack.
 * 2 February 2025 - Added int to outlines the location type
 * 3 February 2025 - Added description for flint
 * 5 February 2025 - Added getter for specific room. Set the visited flag for initial room
 * 9 February 2025 - Added room type to the room.
 * 18 February 2025 - Added link to book in first message
 * 25 February 2025 - Started working on displaying saved games as buttons
 * 2 March 2025 - Added variable to confirm start of game
 * 5 March 2025 - Increased to v4.0
 * 15 March 2025 - Moved initialisation to separate section. Refactored way to handle exits
 * 16 March 2025 - Added specialItemHandler
 */