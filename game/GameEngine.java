/*
Title: Island of Secrets Game
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import interfaces.GameCommandHandler;
import interfaces.GameStateProvider;

import java.io.IOException;
import java.util.List;

import Tests.Test;
import command_process.ActionResult;
import command_process.CommandProcessor;

/**
 * Core engine for the adventure game.
 *
 * {@code GameEngine} handles command processing, state management,
 * and communication between the game logic and the UI.
 * It implements {@link GameCommandHandler} and {@link GameStateProvider}
 * to provide game functionality and status to the interface.
 */
public class GameEngine implements GameCommandHandler,GameStateProvider {
	
	private Game game;
	private Player player;
	private SwimmingHandler swimming = new SwimmingHandler();
	private CommandProcessor processor;
	private final String[] commandHistory = {"","",""};
	private final Test test = new Test();

    /**
     * Constructs a {@code GameEngine} with the specified game and player.
     *
     * @param game the game state object
     * @param player the player object
     */	
	public GameEngine(Game game,Player player) {
		this.game = game;
		this.player = player;
		this.test.setTest(this.game, this.player);
	}
	
	//=== Core Game Loop ===//

    /**
     * Processes a command issued by the player.
     * Delegates to {@link SwimmingHandler} if the player is swimming,
     * otherwise to {@link CommandProcessor}.
     *
     * @param command the command string to process
     * @throws IOException if command execution fails
     */
	public void processCommand(String command) throws IOException {
		
		ActionResult result = null;
		
		if(player.isPlayerStateSwimming()) {
			result = swimming.execute(command,player,game);
		} else {
			processor = new CommandProcessor();
			result = processor.execute(command,game,player);
		}
		
		applyResult(result);
		updateCommandHistory(command);
		
		test.displayValue(this.game, this.player);
	}

    /**
     * Applies the effects of an {@link ActionResult} to the game and player.
     *
     * @param result the action result containing updated game and player
     */
	private void applyResult(ActionResult result) {
		this.player = result.getPlayer();
		this.game = result.getGame();
		this.player.turnUpdateStats();
	}

    /**
     * Updates the history of the last three commands.
     *
     * @param command the command to add to history
     */
	private void updateCommandHistory(String command) {
		
		//Saves the commands into the previous command list
		if (this.commandHistory[0].equals("")) {
			this.commandHistory[0] = command;
		} else if (this.commandHistory[1].equals("")) {
			this.commandHistory[1] = command;
		} else if (this.commandHistory[2].equals("")) {
			this.commandHistory[2] = command;
		} else {
			this.commandHistory[0] = this.commandHistory[1];
			this.commandHistory[1] = this.commandHistory[2];
			this.commandHistory[2] = command;
		}
	}
		
	//=== State Management ===//

    /**
     * Adds a message to the game display.
     *
     * @param message the message text
     * @param clear true to clear previous messages, false to append
     * @param isLong true if the message is long
     */	
	public void addMessage(String message, boolean clear, boolean isLong) {
		game.addMessage(message,clear,isLong);
	}

    /** @return the player's remaining time as a string */
	public String getTime() {
		return player.toStringTimeRemaining();
	}

    /** @return the player's status as a string */
	public String getStatus() {
		return player.toStringStatus();
	}

    /**
     * Returns a description of the player's current room.
     *
     * @return the room description
     */
	public String getRoom() {
		
		String description = "";
		if (player.isPlayerStateSwimming()) {
			description = swimming.getDescription();
		} else {
			player.updateDisplayRoom();
			int room = player.getDisplayRoom();		
			description = String.format("You are %s",game.getRoomName(room));
		}
		
		return description;
	}

    /**
     * Returns a list of items in the player's current room.
     *
     * @return a string representation of items
     */
	public String getItems() {
		
		String itemDisplay = "";
		
		if (player.isPlayerStateNormal()) {
			itemDisplay = game.getItems(player.getDisplayRoom());
		}
		
		return itemDisplay;
	}
	
    /** @return the game object */
	public Game getGame() {
		return this.game;
	}
	
	/** @return the player object */
	public Player getPlayer() {
		return this.player;
	}
	
    /**
     * Returns the exits of the player's current room.
     *
     * @return a string describing the available exits
     */
	public String getExits() {
		
		String exitDisplay = "";
		
		if (player.isPlayerStateNormal()) {
			exitDisplay = game.getExits(player.getRoom());
		}		
		
		return exitDisplay;
	}

    /**
     * Returns special exits of the player's current room.
     *
     * @return a string describing special exits
     */
	public String getSpecialExits() {

		String exitDisplay = "";
		
		if (player.isPlayerStateNormal()) {
			exitDisplay = game.getSpecialExits(player.getRoom());
		}		
		
		return exitDisplay;		
		
	}

    /**
     * Returns the current game messages.
     *
     * @return a list of messages
     */
	public List<String> getMessage() {
		
		//Swimming in poisoned Waters?
		if (player.isPlayerStateSwimming()) {
			swimming.setMessage(game, player);
		}
		
		List<String> message = game.getNormalMessage();
		
		return message;
	}
		
    /** @return the last three commands entered */
	public String[] getCommands() {
		return this.commandHistory;
	}
	
    //=== Load Game Position ===//
	
	@Override
	public void increaseLoadPosition() throws IOException {
		this.game.increaseCount();
		processCommand("load");
	}
		
	@Override
	public void decreaseLoadPosition() throws IOException {
		this.game.descreaseCount();
		processCommand("load");
	}
	
    /** @return true if lower limit of saved games is reached */
	public boolean getLowerLimitSavedGames() {
		return game.getLowerLimitSavedGames();
	}
	
    /** @return true if upper limit of saved games is reached */
	public boolean getUpperLimitSavedGames() {
		return game.getUpperLimitSavedGames();
	}
	
	/** @return displayed saved games as a string array */
	public String[] getDisplayedSavedGames() {
		return game.getDisplayedSavedGames();
	}
	
	//=== Game State Setters ===//
	
	public void setRunningGameState() {
		game.setRunningGameState();
	}
	
	public void setSavedGameState() {
		game.setSavedGameState();
	}
	
	public void setShelterGameState() {
		game.setShelterGameState();
	}
	
	public void setMessageState() {
		game.setMessageGameState();
	}
	
	//=== Game State Setters ===//
	
	public boolean isInitialGameState() {
		return game.isInitialGameState();
	}
	
	public boolean isSavedGameState() {
		return game.isSavedGameState();
	}
	
	public boolean isEndGameState() {
		return game.isEndGameState();
	}
	
	public boolean isRestartGameState() {
		return game.isRestartGameState();
	}
	
	public boolean isRunningState() {
		return game.isRunningState();
	}
	
	public boolean isGiveState() {
		return game.isGiveState();
	}
	
	public boolean isShelterState() {
		return game.isShelterState();
	}
	
	public boolean isSwimmingState() {
		return player.isPlayerStateSwimming();
	}
	
	public boolean isMessageState() {
		return game.isMessageState();
	}
	
	public boolean isNormalState() {
		return player.isPlayerStateNormal();
	}
	
    /**
     * Calculates the player's final score based on stats.
     *
     * @return the final score
     */
	public int getFinalScore() {
		boolean timeBonus = (int) player.getStat("timeRemaining")<640;
		double timeScore = (int) player.getStat("timeRemaining")/7.0;
		double applyTimeBonus = timeBonus ? -timeScore:0;
		int wisdom = (int) player.getStat("wisdom");
		return (int) ((int) ((float) player.getStat("strength"))+wisdom+applyTimeBonus);
	}
	
	//=== Room Queries ===//
	
	@Override
	public boolean getRoomVisited(int roomNumber) {
		return game.getRoomVisited(roomNumber);
	}

	@Override
	public boolean[] getRoomExits(int roomNumber) {
		return game.getRoomExits(roomNumber);
	}

	@Override
	public String getRoomImageType(int roomNumber) {
		return game.getRoomImageType(roomNumber);
	}
	
	@Override
	public String getRoomName(int roomNumber) {
		return game.getRoomName(roomNumber);
	}

	@Override
	public int getCurrentRoom() {
		return player.getRoom();
	}

	@Override
	public void setRoom(int locationID) {
		player.setRoom(locationID);
	}

	@Override
	public List<String> getPanelMessage() {
		return game.getPanelMessage();
	}
} 

/*
8 September 2024 - Created File
9 September 2024 - Added method to retrieve input and started processing command
10 September 2024 - Added code to respond to incorrect commands
12 September 2024 - Started building the move methods.
14 September 2024 - Added the poison water method and continued the move method
					completed main move section.
17 September 2024 - Finished Movement and started on take objects
21 September 2024 - Finished give & drop methods2750 GOSUB720:GOSUB2760:RETURN
22 September 2024 - Completed Eat & Drink methods
23 September 2024 - Completed ride, open and started break
30 September 2024 - Continued working on the break function
4 October 2024 - Finished Break Method
5 October 2024 - Finished Attack Method
12 October 2024 - Finished Last part of attack method and added swim method.
				  Added shelter method & set up Help method
18 October 2024 - Added the help method
23 October 2024 - Added rub method, examine & fill method
24 October 2024 - Added the say & rest method
26 October 2024 - Added wave method and refactored data name and methods. Added info method
27 October 2024 - Started writing the updates for every move
28 October 2024 - Added Swampman Section
29 October 2024 - Added end game functions & completed basic conversion
	  			- Upgraded to version 1.0
1 November 2024 - Cleared previous methods and stored game & player class
4 November 2024 - Added methods to retrieve strings from game & player
5 November 2024 - Added get exit method
				- Added get message method
				- Added get previous command method
				- Added process command method
7 November 2024 - Added array to hold three previous commands
8 November 2024 - Change frame to panel and added refresh options.
9 November 2024 - Began working on processing the command
11 November 2024 - Got the command processing working and now working on coding the command
12 November 2024 - Added extra parameter to create coded command being noun number
13 November 2024 - Added code to execute the command
25 November 2024 - Added new panel for handling the give command.
				   Moved panel generating functions into new methods
29 November 2024 - Passed coded command to GivePanel
30 November 2024 - Added message panel for response to giving Median a shiny pebble
1 December 2024 - Added panel to display messages.
7 December 2024 - Added stub for poisonous waters subgame
8 December 2024 - Added code to retrieve loaded game details.
16 December 2024 - Added code to handle swimming in poisoned waters
22 December 2024 - Added check to determine response type
23 December 2024 - Added shelter process
				 - Updated to version 2.
26 December 2024 - Removed the strength display, and added some comments
29 December 2024 - Added calls to the test object
				 - Added call to the special exits in the room
3 January 2025 - Moved the panel determination to separate function
11 January 2025 - Added the end game flag to when the player runs out of strength while swimming.
15 January 2025 - Added code so shelter goes to the correct place.
31 January 2025 - Completed Testing and increased version
1 February 2025 - Removed unused variables
5 February 2025 - Added getter for the game
17 February 2025 - Added restart function
22 February 2025 - Added a getPlayer function
23 February 2025 - Fixed surfacing for the swimming in poisoned waters
3 March 2025 - Added call to focus on command line
5 March 2025 - Increased to v4.0
11 March 2025 - Updated code due to moving timeRemaining into a map for player stats
12 March 2025 - Updated code to use the hashmap for wisdom, strength & weight
15 March 2025 - Updated class to handle a separate swimming class.
17 March 2025 - Changed setMessage to addMessage
21 March 2025 - Added throws declaration
22 March 2025 - Updated MessagePanel constructor
23 March 2025 - Merged addMessage and addNormalMessage
24 March 2025 - Added Interfaces
25 March 2025 - Added method checking initial game state & displayed games
26 March 2025 - Commented out code to enable to run
29 March 2025 - Hid setCommandField
31 March 2025 - Removed panel refresh. Removed panel from process command
4 April 2025 - Removed UI Components. Added functions to retrieve room details for map
10 April 2025 - Removed restart and added set room function
12 April 2025 - Changed to calling correct limit for saved games
13 April 2025 - Removed panel from increase & descrease load. Updated to reflect interface
20 April 2025 - Added get Room Name function
23 April 2025 - Added the set response type and addMessage function
			  - Removed process shelter
			  - Removed command processing
24 April 2025 - Created CommandResult class and move methods
25 April 2025 - Added methods for player state
27 April 2025 - Created swimming handler and moved swimming related code there.
1 May 2025 - Fixed errors arising from changes to code
5 May 2025 - Made game & player mutable (due to need for changing)
		   - Updated for ActionResult
30 June 2025 - Removed separate process for give
24 July 2025 - Updated for messagePanel
22 July 2025 - Added get panel message
28 July 2025 - Added function to update message state
17 August 2025 - Added Javadocs
6 November 2025 - Added check for restart game state
23 November 2025 - Removed Lightning State
3 December 2025 - Increased version number
*/