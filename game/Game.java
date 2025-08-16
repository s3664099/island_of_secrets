/*
Title: Island of Secrets Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.14
Date: 28 July 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import data.Constants;
import data.Item;
import data.Location;

public class Game implements Serializable {
	
	private static final long serialVersionUID = 3473676803014192040L;
	private static final Logger logger = Logger.getLogger(Game.class.getName());
	
	private Location[] locationList;
	private Item[] itemList;
	private boolean[] exitNumbers;
	private SpecialExitHandler specialExitHandler = new SpecialExitHandler();
	private SpecialItemHandler specialItemHandler = new SpecialItemHandler();
	private RandomExitHandler randomExitHandler = new RandomExitHandler();
	
	private MessageBuilder normalMessage = new MessageBuilder("Let your quest begin!");
	private MessageBuilder panelMessage = new MessageBuilder();
		
	private String[] commands = {"","",""};
	
	private enum GameState { STARTED,RUNNING,SAVED_GAMES,ENDED,SHELTER,GIVE,LIGHTNING }
	private boolean hasMessage = false;
	private GameState gameState = GameState.STARTED;
	private String giveNoun = "";
	
	private int saveGameCount = 0;
	private int startGameCount = 2;
	private boolean upperLimitSavedGames = false;
	private boolean lowerLimitSavedGames = false;
	private String[] savedGamesDisplayed = {"","","","",""};
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
		
		exitNumbers = locationList[roomNumber].getExits();
		String exits = "";
		
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
		return exitNumbers[direction];
	}
	
	//Returns the room based on the number passed through
	public Location getRoom(int roomNumber) {
		return locationList[roomNumber];
	}
	
	//Map Functions
	public boolean getRoomVisited(int roomNumber) {
		
		return locationList[roomNumber].getVisited();
	}
	
	public String getRoomImageType(int roomNumber) {
		return locationList[roomNumber].getRoomType();
	}
	
	public boolean[] getRoomExits(int roomNumber) {
		return locationList[roomNumber].getExits();
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
	public void addMessage(String message,boolean clear, boolean isLongMessage ) {
		logger.info("Adding message: " + message);
		
		if (isLongMessage) {
			normalMessage.addLongMessage(message, clear);
		} else {
			normalMessage.addMessage(message, clear);
		}
	}
		
	public void addPanelMessage(String message,boolean clear) {
		logger.info("Adding Panel message: " + message);
		panelMessage.addMessage(message, clear);
	}
	
	public String getCommand(int number) {
		
		if (number<0||number>=commands.length) {
			throw new IllegalArgumentException("Invalid command number: " + number);
		}
		
		return commands[number];
	}
	
	public Item getItem(int itemNumber) {
		
		if (itemNumber<0||itemNumber >=itemList.length) {
			throw new IllegalArgumentException("Invalid item number: " + itemNumber);
		}
		
		return itemList[itemNumber];
	}
	
	//Gets the sum of the item's flag and location
	public int getItemFlagSum(int itemNumber) {
		
		if (itemNumber<0||itemNumber >=itemList.length) {
			throw new IllegalArgumentException("Invalid item number: " + itemNumber);
		}
		
		return itemList[itemNumber].getItemFlag() + itemList[itemNumber].getItemLocation();
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
		
	//Checks how many apples left
	public boolean checkApples() {
		
		boolean applesLeft = false;
		
		if (apple_count>0) {
			apple_count --;
			applesLeft = true;
			logger.info("Apple count decreased. Remaining apples: " + apple_count);
		}
		return applesLeft;
	}
	
	//Handling saved game display
	public void setUpperLimitSavedGames(boolean moreGames) {
		this.upperLimitSavedGames = moreGames;
		logger.info("More game position set to: " + this.upperLimitSavedGames);
	}
	
	public boolean getUpperLimitSavedGames() {
		return this.upperLimitSavedGames;
	}
	
	public void setLowerLimitSavedGames(boolean lessGames) {
		this.lowerLimitSavedGames = lessGames;
		logger.info("Less game position set to: " + this.lowerLimitSavedGames);
	}
	
	public boolean getLowerLimitSavedGames() {
		return this.lowerLimitSavedGames;
	}
	
	public String[] getDisplayedSavedGames() {
		return this.savedGamesDisplayed;
	}
	
	public String getGiveNoun() {
		gameState = GameState.RUNNING;
		return giveNoun;
	}
	
	public void setDisplayedGames(String[] gameDisplayed) {
		this.savedGamesDisplayed = gameDisplayed;
	}	
	
	public void setRunningGameState() {
		gameState = GameState.RUNNING;
	}

	public void setSavedGameState() {
		gameState = GameState.SAVED_GAMES;
	}
	
	public void setGiveState(String noun) {
		giveNoun = noun;
		gameState = GameState.GIVE;
	}
	
	public void setShelterGameState() {
		gameState = GameState.SHELTER;
	}
	
	public void setMessageGameState() {
		hasMessage = !hasMessage;
	}
	
	public void setLightingGameState() {
		gameState = GameState.LIGHTNING;
	}
	
	public void setEndGameState() {
		
		logger.info("Game ended.");
		gameState = GameState.ENDED;
	}
	
	public boolean isInitialGameState() {
		
		boolean started = false;

		if (startGameCount>0) {
			startGameCount --;
		} else if (gameState == GameState.STARTED) {
			gameState = GameState.RUNNING;
			started = true;
		}
		return started;
	}
		
	public boolean isSavedGameState() {
		
		boolean saveGame = false;
		if (gameState == GameState.SAVED_GAMES) {
			saveGame = true;
		}
		return saveGame;
	}
	
	public boolean isEndGameState() {
		
		boolean endGame = false;
		if (gameState == GameState.ENDED) {
			endGame = true;
		}
		return endGame;
	}
	
	public boolean isGiveState() {
		
		boolean giveState = false;
		if (gameState == GameState.GIVE) {
			giveState = true;
		}
		return giveState;
	}
	
	public boolean isShelterState() {
		
		boolean shelterState = false;
		if (gameState == GameState.SHELTER) {
			shelterState = true;
		}
		return shelterState;
	}
	
	public boolean isMessageState() {
		return hasMessage;
	}
	
	public boolean isLightningState() {
		
		boolean lightningState = false;
		if (gameState == GameState.LIGHTNING) {
			lightningState = true;
		}
		return lightningState;
	}
	
	public boolean isRunningState() {
		
		boolean runningState = false;
		if (gameState == GameState.RUNNING) {
			runningState = true;
		}
		return runningState;
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
 * 23 March 2025 - Combined addMessage and addNormalMessage
 * 				   Updated check start to Enum
 * 25 March 2025 - Updated method name for checking initial game state
 * 				 - Added gameState for checking saved games & end game
 * 18 April 2025 - Added startGameCount to display button to open book
 * 23 April 2025 - Removed Response Required and replaced with Enum
 * 25 April 2025 - Added more states
 * 28 April 2025 - Added methods to set the Message and Lightning State
 * 30 June 2025 - Updated give state for holding object to give. Changed bools to represent the function
 * 14 July 2025 - Made the items passed through to a long message
 * 15 July 2025 - Moved array holding directions to top as a global variable
 * 25 July 2025 - Added message state set when panel message added.
 * 28 July 2025 - Created separate boolean for message state
 */