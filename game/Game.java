/*
Title: Island of Secrets Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import data.Constants;
import data.Item;
import data.Location;

/**
 * Core game state and logic container.
 * <p>
 * The {@code Game} class manages rooms, items, exits, game state transitions,
 * messages, save-game handling, and special mechanics such as apples and
 * special exits. It acts as the central hub for the adventure game.
 * </p>
 */
public class Game implements Serializable {
	
	private static final long serialVersionUID = 3473676803014192040L;
	private static final Logger logger = Logger.getLogger(Game.class.getName());
	
	/** List of all Game Locations. */
	private Location[] locationList;
	
	/** List of all game items. */
	private Item[] itemList;
	
	/** Active Exits for the current room. */
	private boolean[] exitNumbers;
	
	/** Handler for special exits. */	
	private SpecialExitHandler specialExitHandler = new SpecialExitHandler();
	
	/** Handler for special items. */
	private SpecialItemHandler specialItemHandler = new SpecialItemHandler();
	
	/** Handler for rooms with random exits. */
	private RandomExitHandler randomExitHandler = new RandomExitHandler();
	
	/** Standard in-game message builder. */
	private MessageBuilder normalMessage = new MessageBuilder("Let your quest begin!");
	
	/** Special message builder. */
	private MessageBuilder panelMessage = new MessageBuilder();
	
	/** Stores last commands entered by the player. */
	private String[] commands = {"","",""};
	
	/** Enum of possible game states. */
	private enum GameState { STARTED,RUNNING,SAVED_GAMES,ENDED,SHELTER,GIVE,RESTART }
	
	private boolean hasMessage = false;
	private GameState gameState = GameState.STARTED;
	private String giveNoun = "";
	
	/** Save game tracking. */
	private int saveGameCount = 0;
	private int startGameCount = Constants.INITIAL_START_COUNT;
	private boolean upperLimitSavedGames = false;
	private boolean lowerLimitSavedGames = false;
	private String[] savedGamesDisplayed = {"","","","",""};
	
	/** Remaining apples available to the player. */
	private int apple_count = Constants.INITIAL_APPLE_COUNT;
	
	/**
	 * Creates a new game instance with the given locations, items, and exit handler.
	 *
	 * @param locations the list of available locations
	 * @param items the list of available items
	 * @param specialExitHandler the handler for special exits
	 */
	public Game(Location[] locations, Item[] items,SpecialExitHandler specialExitHandler) {
		
		this.locationList = locations;
		this.itemList = items;
		this.specialExitHandler = specialExitHandler;
		
		// Sets start location as visited
		locationList[Constants.START_LOCATION].setVisited();
		
	}
	
	/**
	 * Returns the name of a room by number.
	 *
	 * @param roomNumber the room index
	 * @return the room name
	 * @throws IllegalArgumentException if the room number is invalid
	 */
	public String getRoomName(int roomNumber) {
		
	    if (roomNumber < 0 || roomNumber >= locationList.length) {
	        throw new IllegalArgumentException("Invalid room number: " + roomNumber);
	    }
		
		return this.locationList[roomNumber].getName();
	}
	
	/**
	 * Retrieves all items present in the given room, including special items.
	 *
	 * @param roomNumber the room index
	 * @return a string listing items, or empty if none
	 * @throws IllegalArgumentException if the room number is invalid
	 */
	public String getItems(int roomNumber) {
		
	    if (roomNumber < 0 || roomNumber >= locationList.length) {
	        throw new IllegalArgumentException("Invalid room number: " + roomNumber);
	    }
	    
	    List<String> itemsFound = new ArrayList<>();
		
		String specialItems = specialItemHandler.getSpecialItems(roomNumber, itemList, locationList, apple_count);
		if(!specialItems.isEmpty()) {
			itemsFound.add(specialItems);
		}
				
		//Goes through each of the items
		for (Item item:itemList) {
			if(item != null && item.isAtLocation(roomNumber) && item.getItemFlag()<1) {
				itemsFound.add(item.getItemName());
			}
		}
						
		return itemsFound.isEmpty()?"":"You see: "+String.join(", ",itemsFound);
	}
	
	/**
	 * Returns available exits for a given room.
	 * 
	 * @param roomNumber the room index
	 * @return formatted string of exits, or empty if none
	 */
	public String getExits(int roomNumber) {
		
		exitNumbers = locationList[roomNumber].getExits();
		if (roomNumber == Constants.RANDOM_ROOM) {
			exitNumbers = randomExitHandler.generateRandomExits();			
		}
		String exits = "";
		for (int i=0;i<Constants.DIRECTIONS.length;i++) {
			if (exitNumbers[i] && specialExitHandler.displayExit(roomNumber, Constants.DIRECTIONS[i])) {
				exits = addExit(Constants.DIRECTIONS[i],exits);
			}
		}
		
		return exits.isEmpty()?"":"You can go:"+exits;
	}
	
	/**
	 * Returns description of special exits in a room.
	 *
	 * @param roomNumber the room index
	 * @return special exits description, or empty if none
	 */
	public String getSpecialExits(int roomNumber) {
		return specialExitHandler.getSpecialExit(roomNumber, itemList);	
	}
	
	/**
	 * Checks whether an exit exists in a given direction.
	 *
	 * @param room the room index
	 * @param direction the direction index
	 * @return true if an exit exists, false otherwise
	 */
	public boolean checkExit(int room, int direction) {
		return exitNumbers[direction];
	}
	
	/**
	 * Gets a {@link Location} object by room number.
	 *
	 * @param roomNumber the room index
	 * @return the corresponding location
	 */
	public Location getRoom(int roomNumber) {
		return locationList[roomNumber];
	}
	
	// --- Map and location functions ---
	
	/** Returns whether a room has been visited. */
	public boolean getRoomVisited(int roomNumber) {
		return locationList[roomNumber].getVisited();
	}
	
	/** Returns the type of image associated with a room. */
	public String getRoomImageType(int roomNumber) {
		return locationList[roomNumber].getRoomType();
	}
	
	/** Returns the exits available from a given room. */
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
	

	// --- Messaging functions ---

	/** Gets the accumulated in-game messages. */
	public List<String> getNormalMessage() {
		return normalMessage.getMessages();
	}
	
	/** Gets messages destined for the panel view. */
	public List<String> getPanelMessage() {
		return panelMessage.getMessages();
	}
			
	/**
	 * Adds a message to the message object
	 *
	 * @param message the message text
	 * @param clear whether to clear existing messages first
	 * @param isLongMessage whether to format as a long message
	 */
	public void addMessage(String message,boolean clear, boolean isLongMessage ) {
		logger.info("Adding message: " + message);
		
		if (isLongMessage) {
			normalMessage.addLongMessage(message, clear);
		} else {
			normalMessage.addMessage(message, clear);
		}
	}
	
	/**
	 * Adds a message to the panel message object.
	 *
	 * @param message the message text
	 * @param clear whether to clear existing panel messages first
	 */
	public void addPanelMessage(String message,boolean clear) {
		logger.info("Adding Panel message: " + message);
		panelMessage.addMessage(message, clear);
	}
	
	// --- Item and command accessors ---

	/** Gets a stored command by index. */
	public String getCommand(int number) {
		if (number<0||number>=commands.length) {
			throw new IllegalArgumentException("Invalid command number: " + number);
		}
		return commands[number];
	}
	
	/** Gets an item by index. */
	public Item getItem(int itemNumber) {
		if (itemNumber<0||itemNumber >=itemList.length) {
			throw new IllegalArgumentException("Invalid item number: " + itemNumber);
		}
		return itemList[itemNumber];
	}
	
	/** Returns a sum of an item’s flag and location values. */
	public int getItemFlagSum(int itemNumber) {
		
		if (itemNumber<0||itemNumber >=itemList.length) {
			throw new IllegalArgumentException("Invalid item number: " + itemNumber);
		}
		
		return itemList[itemNumber].getItemFlag() + itemList[itemNumber].getItemLocation();
	}
				
	// --- Save game handling ---

	/** Gets the current save game count. */
	public int getCount() {
		return this.saveGameCount;
	}
	
	/** Increases the save game count. */
	public void increaseCount() {
		this.saveGameCount++;
	}
	
	/** Decreases the save game count. */
	public void descreaseCount() {
		this.saveGameCount--;
	}
	
	/** Resets the save game count. */
	public void resetCount() {
		this.saveGameCount=0;
	}
			
	/** Sets whether more saved games are available above. */
	public void setUpperLimitSavedGames(boolean moreGames) {
		this.upperLimitSavedGames = moreGames;
		logger.info("More game position set to: " + this.upperLimitSavedGames);
	}
	
	/** Returns whether more saved games exist above. */
	public boolean getUpperLimitSavedGames() {
		return this.upperLimitSavedGames;
	}
	
	/** Sets whether more saved games are available below. */
	public void setLowerLimitSavedGames(boolean lessGames) {
		this.lowerLimitSavedGames = lessGames;
		logger.info("Less game position set to: " + this.lowerLimitSavedGames);
	}
	
	/** Returns whether more saved games exist below. */
	public boolean getLowerLimitSavedGames() {
		return this.lowerLimitSavedGames;
	}
	
	/** Returns the list of displayed saved game slots. */
	public String[] getDisplayedSavedGames() {
		return this.savedGamesDisplayed;
	}

	/** Updates displayed saved games. */
	public void setDisplayedGames(String[] gameDisplayed) {
		this.savedGamesDisplayed = gameDisplayed;
	}	
	
	// --- Apple mechanic ---

	/**
	 * Checks if apples are left, decreasing the count if so.
	 *
	 * @return true if apples remain, false otherwise
	 */
	public boolean checkApples() {
		
		boolean applesLeft = false;
		
		if (apple_count>0) {
			apple_count --;
			applesLeft = true;
			logger.info("Apple count decreased. Remaining apples: " + apple_count);
		}
		return applesLeft;
	}
	

	/** Returns the noun associated with a GIVE command. */
	public String getGiveNoun() {
		gameState = GameState.RUNNING;
		return giveNoun;
	}
	

	/** Sets game state to running. */
	public void setRunningGameState() {
		gameState = GameState.RUNNING;
	}

	/** Sets game state to saved game selection. */
	public void setSavedGameState() {
		gameState = GameState.SAVED_GAMES;
	}
	
	/** Sets game state to GIVE with a noun. */
	public void setGiveState(String noun) {
		giveNoun = noun;
		gameState = GameState.GIVE;
	}
	
	/** Sets game state to shelter. */
	public void setShelterGameState() {
		gameState = GameState.SHELTER;
	}
	
	/** Toggles message state on/off. */
	public void setMessageGameState() {
		hasMessage = !hasMessage;
	}
		
	/** Sets game state to ended. */
	public void setEndGameState() {
		
		logger.info("Game ended.");
		gameState = GameState.ENDED;
	}
	
	/** Sets game state to restart. */
	public void setRestartGameState() {
		
		logger.info("Game ended.");
		gameState = GameState.RESTART;
	}
	
	/** Checks and progresses from STARTED to RUNNING state. */
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
	
	/** Returns true if game is in saved game state. */
	public boolean isSavedGameState() {
		return gameState == GameState.SAVED_GAMES;
	}
	
	/** Returns true if game is ended. */
	public boolean isEndGameState() {
		return gameState == GameState.ENDED;
	}
	
	/** Returns true if game has restarted. */
	public boolean isRestartGameState() {
		return gameState == GameState.RESTART;
	}
	
	/** Returns true if game is in GIVE state. */
	public boolean isGiveState() {
		return gameState == GameState.GIVE;
	}
	
	/** Returns true if game is in shelter state. */
	public boolean isShelterState() {
		return gameState == GameState.SHELTER;
	}
	
	/** Returns true if game is showing messages. */
	public boolean isMessageState() {
		return hasMessage;
	}
	
	/** Returns true if game is in running state. */
	public boolean isRunningState() {
		return gameState == GameState.RUNNING;
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
 * 19 August 2025 - Tightened code based on recommendations. Added JavaDocs
 * 6 November 2025 - Added a restart state
 * 23 November 2025 - Removed Lighting State
 * 3 December 2025 - Increased version number
 */