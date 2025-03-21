/*
Title: Island of Secrets Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 20 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import Data.Constants;
import Data.Item;
import Data.Location;

public class Game implements Serializable {
	
	private static final long serialVersionUID = 3473676803014192040L;
	private static final Logger logger = Logger.getLogger(Game.class.getName());
	
	private Location[] locationList;
	private Item[] itemList;
	private SpecialExitHandler specialExitHandler = new SpecialExitHandler();
	private SpecialItemHandler specialItemHandler = new SpecialItemHandler();
	private RandomExitHandler randomExitHandler = new RandomExitHandler();
	
	private MessageBuilder normalMessage = new MessageBuilder("Let your quest begin!");
	private MessageBuilder panelMessage = new MessageBuilder();
	
	private String[] commands = {"","",""};
	
	private boolean endGame = false;
	private int saveGameCount = 0;
	private int responseRequired = 0;
	private boolean displayGames = false;
	private boolean moreGames = false;
	private boolean lessGames = false;
	private boolean start = true;
	private String[] gameDisplayed = {"","","","",""};
	private int apple_count = 3;
	
	public Game(Location[] locations, Item[] items,SpecialExitHandler specialExitHandler) {
		
		this.locationList = locations;
		this.itemList = items;
		this.specialExitHandler = specialExitHandler;
		
		//sets start location
		locationList[Constants.START_LOCATION].setVisited();
		
	}
	
	public String getRoomName(int roomNumber) {
		
	    if (roomNumber < 0 || roomNumber >= locationList.length) {
	        throw new IllegalArgumentException("Invalid room number: " + roomNumber);
	    }
		
		return this.locationList[roomNumber].getName();
	}
	
	//Goes through the items and checks what is present
	public String getItems(int roomNumber) {
		
	    if (roomNumber < 0 || roomNumber >= locationList.length) {
	        throw new IllegalArgumentException("Invalid room number: " + roomNumber);
	    }
		
		String items = specialItemHandler.getSpecialItems(roomNumber, itemList, locationList);
		int count = items.isEmpty() ? 0:1;
				
		//Goes through each of the items
		for (Item item:itemList) {
			if(item != null && item.isAtLocation(roomNumber) && item.getItemFlag()<1) {
				count ++;
				if (count>1) {
					items = String.format("%s, %s",items,item.getItemName());
				} else {
					items = String.format("%s %s",items,item.getItemName());
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
		specialExitHandler = new SpecialExitHandler();
		
		if (roomNumber == Constants.RANDOM_ROOM) {
			exitNumbers = randomExitHandler.generateRandomExits();			
		}
		
		//Checks if the exit is a special exit. If not, displays it normally.
		if (exitNumbers[0] && (specialExitHandler.displayExit(roomNumber,Constants.NORTH))) {
			exits = addExit(Constants.NORTH,exits);
		}
		
		if (exitNumbers[1] && (specialExitHandler.displayExit(roomNumber,Constants.SOUTH))) {
			exits = addExit(Constants.SOUTH,exits);
		}
		
		if (exitNumbers[2] && (specialExitHandler.displayExit(roomNumber,Constants.EAST))) {
			exits = addExit(Constants.EAST,exits);
		}
		
		if (exitNumbers[3] && (specialExitHandler.displayExit(roomNumber,Constants.WEST))) {
			exits = addExit(Constants.WEST,exits);
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
	public List<String> getNormalMessage() {
		return normalMessage.getMessages();
	}
	
	public List<String> getPanelMessage() {
		return panelMessage.getMessages();
	}
			
	//Adds Message
	public void addMessage(String message,Boolean clear) {
		logger.info("Adding message: " + message);
		normalMessage.addMessage(message, clear);
	}
	
	public void addNormalMessage(String message,boolean clear) {
		
		logger.info("Adding message: " + message);
		normalMessage.addLongMessage(message, clear);
		
	}
	
	public void addPanelMessage(String message,boolean clear) {
		logger.info("Adding Panel message: " + message);
		panelMessage.addMessage(message, clear);
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
			
	//Flag to determine whether the game has ended.
	public void endGame() {
		
		logger.info("Game ended.");
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
		
	    if (responseType < 0 || responseType >= Constants.NUMBER_RESPONSES) {
	        throw new IllegalArgumentException("Invalid response type: " + responseType);
	    }
		
		this.responseRequired = responseType;
	}
	
	public int getResponse() {
		return this.responseRequired;
	}
	
	//Checks how many apples left
	public boolean checkApples() {
		
		boolean applesLeft = false;
		
		if (apple_count>0) {
			apple_count --;
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
 * 16 March 2025 - Added specialItemHandler. Moved constants to constants class
 * 				   Moved random exits to a separate class to generate the random exits
 * 				   Generated SpecialExitHandler once
 * 20 March 2025 - Updated class to handle message builder
 */