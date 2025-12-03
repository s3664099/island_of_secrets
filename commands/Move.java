/*
Title: Island of Secrets Move Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package commands;

import data.Constants;
import data.GameEntities;
import game.Game;
import game.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import command_process.ActionResult;
import command_process.ParsedCommand;

/**
 * Handles player movement logic, including parsing movement commands,
 * validating moves, applying restrictions, and executing room transitions.
 */
public class Move {
	
    /** Direction modifiers for room transitions (N, S, E, W). */
	private static final int[] DIRECTION_MODIFIERS = {-10, +10, +01, -01};
	
    /** Random instance for item placement on events (e.g., jetty). */
	private final Random rand = new Random();
	
    /**
     * Normalizes a parsed move command by resolving direction, special rooms,
     * and coded commands into a usable noun number.
     *
     * @param command the parsed command to normalize
     * @param room the player's current room
     * @return a normalized {@link ParsedCommand}
     */
	public ParsedCommand normaliseMoveCommand(ParsedCommand command,int room) {
		
		int verbNumber = command.getVerbNumber();
		int nounNumber = command.getNounNumber();
		String noun = command.getSplitTwoCommand()[1];
		String code = command.getCodedCommand();
		nounNumber = handleSpecialRooms(room, noun, nounNumber);
		nounNumber = mapCodedCommandToDirection(code,nounNumber);
		if(nounNumber>GameEntities.MOVE_NOT_DIRECTION && nounNumber != GameEntities.ITEM_BOAT) {
			nounNumber -= 43;
		}
		if (isCodeDownTrapdoor(code)) {
			code = codeEnterTrapdoor();
		}
		
		return new ParsedCommand(verbNumber,nounNumber,code,
				command.getSplitTwoCommand(),command.getCommand());
	}
	
    /**
     * Resolves single-direction commands where the noun may be omitted.
     *
     * @param nounNumber the noun number from the command
     * @param verbNumber the verb number from the command
     * @return a corrected noun number representing a direction
     */
	public int parseSingleDirection(int nounNumber, int verbNumber) {

		if (nounNumber == -1) {
			nounNumber = verbNumber;
		} else if (nounNumber>Constants.NUMBER_OF_ITEMS && nounNumber<Constants.NUMBER_OF_NOUNS) {
			nounNumber = nounNumber-Constants.NUMBER_OF_ITEMS;
		}
		return nounNumber;
	}
	
    /**
     * Maps a coded room command to a directional movement if applicable.
     *
     * @param code the coded command string
     * @param nounNumber the original noun number
     * @return the mapped direction or the original noun number if no match
     */
	private int mapCodedCommandToDirection(String code,int nounNumber) {
		Map<String,Integer> codeToDirection = new HashMap<>();
		codeToDirection.put(GameEntities.CODE_IN_LAIR,GameEntities.WEST);
		codeToDirection.put(GameEntities.CODE_IN_LOG_HUT,GameEntities.WEST);
		codeToDirection.put(GameEntities.CODE_IN_SHACK,GameEntities.WEST);
		codeToDirection.put(GameEntities.CODE_IN_ABODE_HUT,GameEntities.NORTH);
		codeToDirection.put(GameEntities.CODE_IN_PORTAL,GameEntities.NORTH);
		codeToDirection.put(GameEntities.CODE_OUT_LAIR,GameEntities.EAST);
		codeToDirection.put(GameEntities.CODE_OUT_LOG_HUT,GameEntities.NORTH);
		codeToDirection.put(GameEntities.CODE_OUT_LOG_CABIN,GameEntities.NORTH);
		codeToDirection.put(GameEntities.CODE_DOWN_PYRAMID,GameEntities.NORTH);
		codeToDirection.put(GameEntities.CODE_OUT_ABODE_HUT,GameEntities.SOUTH);
		codeToDirection.put(GameEntities.CODE_UP_PYRAMID,GameEntities.SOUTH);
		codeToDirection.put(GameEntities.CODE_OUT_SHACK,GameEntities.EAST);
		codeToDirection.put(GameEntities.CODE_OUT_HALL,GameEntities.EAST);
		return codeToDirection.getOrDefault(code, nounNumber);
	}
	
    /**
     * Handles special-case rooms where nouns map directly to fixed directions.
     *
     * @param room the player's current room
     * @param noun the noun string from the command
     * @param nounNumber the original noun number
     * @return adjusted noun number based on room-specific rules
     */
	private int handleSpecialRooms(int room, String noun, int nounNumber) {
		
		if ((room == GameEntities.ROOM_CAVE && noun.equals("cave")) || 
			(room == GameEntities.ROOM_CLEARING && noun.equals("hut")) ||
			(room ==GameEntities.ROOM_BUILDING && noun.equals("hut"))) {
			nounNumber = GameEntities.WEST;
		} else if (room == GameEntities.ROOM_ABODE_HUT && noun.equals("hut")) {
			nounNumber=GameEntities.NORTH;
		}
		return nounNumber;	
	}

    /**
     * Validates whether a move is possible based on the parsed command,
     * player state, and room conditions.
     *
     * @param command the move command
     * @param game the current game state
     * @param player the player attempting the move
     * @return an {@link ActionResult} describing validity and effects
     */
	public ActionResult validateMove(ParsedCommand command, Game game, Player player) {

		boolean validMove = true;
		int room = player.getRoom();
		ActionResult result = new ActionResult(game,player,validMove);
		
		//Send the result instead of the game.
		if (isGoBoat(command.getNounNumber())) {
			result = goBoat(game,player);
		} else if (isNotDirection(command)) {
			result = notDirection(game,player);
		} else if (isExitBlocked(game,room,command.getNounNumber())
				  && !isDoorClosed(player,command)) {
			result = exitBlocked(game,player);
		} 
		
		return result;
	}
	
    /**
     * Executes a validated move by updating the player's room,
     * applying room entry effects, and generating messages.
     *
     * @param game the current game state
     * @param player the player making the move
     * @param command the parsed move command
     * @return an {@link ActionResult} describing the outcome
     */
	public ActionResult executeMove(Game game, Player player, ParsedCommand command) {
		
		ActionResult blockedCheck = evaluateMovementRestrictions(game,player,command);
		
		//Move is not blocked
		if (!blockedCheck.isValid()) {
			int direction = command.getNounNumber();
			
			if(command.getNounNumber()!=GameEntities.ITEM_BOAT) {
				int newRoom = calculateNewRoom(player.getRoom(),direction);
				player.setRoom(newRoom);
				game.addMessage("Ok",true,true);
				game.getRoom(newRoom).setVisited();
			}
			
			blockedCheck = handleRoomEntryEffects(game,player,command);
		}
		
		return blockedCheck;
	}
	
    /**
     * Calculates the new room index based on the current room and direction.
     *
     * @param currentRoom the player's current room
     * @param direction the movement direction
     * @return the new room index
     */
	private int calculateNewRoom(int currentRoom, int direction) {
		return currentRoom + DIRECTION_MODIFIERS[direction-1];
	}
	
    /**
     * Evaluates whether movement is blocked by entities, events,
     * or environmental restrictions.
     *
     * @param game the current game state
     * @param player the player attempting to move
     * @param command the parsed command
     * @return an {@link ActionResult} indicating if movement is blocked
     */
	private ActionResult evaluateMovementRestrictions(Game game, Player player, ParsedCommand command) {
		
		ActionResult result = new ActionResult(game,player,false);

		if (isOmeganPresent(game,player)) {
			game.addMessage("Omegan's presence prevents you from leaving!",true,true);
			result = new ActionResult(game,player,true);

		} else if (isSwampmanPresent(game,player,command)) {
			game.addMessage("He will not let you pass.",true,true);
			result = new ActionResult(game,player,true);
		
		} else if (areRocksMoving(game,player)) {
			game.addMessage("The rocks move to prevent you",true,true);
			result = new ActionResult(game,player,true);
		
		} else if (doArmsHoldYou(game,player)) {
			game.addMessage("The arms hold you fast",true,true);
			result = new ActionResult(game,player,true);
		
		} else if (isSnakePresent(game,player,command)) {
			game.addMessage("Hisss!",true,true);
			result = new ActionResult(game,player,true);
		
		} else if (isPlayerRidingBeast(game,player,command)) {
			game.addMessage("Too steep to climb",true,true);
			result = new ActionResult(game,player,true);
		
		} else if (isDoorClosed(player,command)) {
			game.addMessage("The door is barred!",true,true);
			result = new ActionResult(game,player,true);
		}
		
		return result;
	}
	
    /**
     * Handles effects that trigger upon entering a room (e.g., traps, ferry).
     *
     * @param game the current game state
     * @param player the player entering the room
     * @param command the parsed move command
     * @return an {@link ActionResult} describing room entry outcomes
     */
	private ActionResult handleRoomEntryEffects(Game game,Player player,ParsedCommand command) {
		
		if (isInHandsRoom(game,player)) {
			game.addMessage("You enter the room and giant hands grab you and hold you fast",false,true);
		} else if (player.getRoom()==GameEntities.ROOM_WITH_HANDS) {
			game.addMessage("You enter the room and brightly shining torch force the arms to retreat to the walls",false,true);
		} else if (isInEntranceHall(player,command)) {
			game.addMessage("The doors slam shut behind you preventing you from leaving",false,true);
		}
		
		if (isOnJetty(game,player)) {
			game.getItem(16).setItemLocation(rand.nextInt(4)+1);
			game.getItem(16).setItemFlag(0);
			game.addMessage("The beast runs away",false,true);
		}
		
		ActionResult result = new ActionResult(game,player,true);
		
		if (isCatchingFerry(game,player,command)) {
			result = catchingFerry(game,player);
		}
		return result;
	}
	
	/**
     * Checks if Omegan prevents the player from leaving the room.
     *
     * @param game the current game state
     * @param player the player
     * @return true if Omegan blocks movement
     */
	private boolean isOmeganPresent(Game game, Player player) {
		return game.getItem(GameEntities.ITEM_OMEGAN).isAtLocation(player.getRoom()) &&
				   (player.getStrengthWisdon()<180 || player.getRoom()==GameEntities.ROOM_SANCTUM);
	}
	
    /**
     * Checks if Swampman prevents the player from moving east.
     */
	private boolean isSwampmanPresent(Game game, Player player, ParsedCommand command) {
		return player.getRoom() == game.getItem(GameEntities.ITEM_SWAMPMAN).getItemLocation() && 
				   game.getItem(GameEntities.ITEM_SWAMPMAN).getItemFlag()<1 && 
				   command.getNounNumber() == GameEntities.EAST;
	}
	
    /**
     * Checks if rocks block the castle entrance.
     */
	private boolean areRocksMoving(Game game, Player player) {
		return player.getRoom() == GameEntities.ROOM_CASTLE_ENTRANCE && 
				   game.getItem(GameEntities.ITEM_ROCKS).getItemFlag()==0;
	}
	
    /**
     * Checks if the arms in the room prevent the player from leaving.
     */
	private boolean doArmsHoldYou(Game game,Player player) {
		return player.getRoom() == GameEntities.ROOM_WITH_HANDS && 
			   game.getItem(GameEntities.ITEM_TORCH).getItemFlag()!=1;
	}
	
    /**
     * Checks if a snake blocks the player from moving west.
     */
	private boolean isSnakePresent(Game game,Player player,ParsedCommand command) {
		return player.getRoom()==GameEntities.ROOM_CLEARING && 
				game.getItem(GameEntities.ITEM_SNAKE).getItemFlag()==0 && 
				command.getNounNumber() == GameEntities.WEST;
	}
	
    /**
     * Checks if the player is riding a beast and prevented from moving east.
     */
	private boolean isPlayerRidingBeast(Game game,Player player,ParsedCommand command) {
		return player.getRoom() == GameEntities.ROOM_ROCKY_PATH && 
				game.getItemFlagSum(GameEntities.ITEM_BEAST) != -1 && 
				command.getNounNumber() == GameEntities.EAST;
	}
	
    /**
     * Checks if the storeroom door blocks the exit.
     */
	private boolean isDoorClosed(Player player,ParsedCommand command) {
		return player.getRoom() == GameEntities.ROOM_STOREROOM && 
				command.getNounNumber() == GameEntities.EAST;
	}
	
    /**
     * Checks if the player is inside the hands room without a torch lit.
     */
	private boolean isInHandsRoom(Game game,Player player) {
		return player.getRoom()==GameEntities.ROOM_WITH_HANDS && 
				game.getItem(GameEntities.ITEM_TORCH).getItemFlag()!=1;
	}
	
    /**
     * Checks if the player is entering the entrance hall and doors slam shut.
     */
	private boolean isInEntranceHall(Player player, ParsedCommand command) {
		return player.getRoom()==GameEntities.ROOM_ENTRANCE_CHAMBER && command.getNounNumber()==1;
	}
	
    /**
     * Checks if the player is on the jetty while the beast is absent.
     */
	private boolean isOnJetty(Game game, Player player) {
		return player.getRoom() == GameEntities.ROOM_JETTY && 
				game.getItem(GameEntities.ITEM_BEAST).getItemLocation()==0;
	}
	
    /**
     * Checks if the noun refers to the boat.
     */
	private boolean isGoBoat(int nounNumber) {
		return nounNumber == GameEntities.ITEM_BOAT;
	}
	
    /**
     * Checks if the code is go down into the trapdoor.
     */
	private boolean isCodeDownTrapdoor(String code) {
		return code.equals(GameEntities.CODE_DOWN_TRAPDOOR);
	}
	
    /**
     * Changed code to enter trapdoor.
     *
     * @return a String with new action code
     */
	private String codeEnterTrapdoor() {
		return GameEntities.CODE_ENTER_TRAPDOOR;
	}
	
    /**
     * Handles movement attempt onto a boat.
     *
     * @return an {@link ActionResult} indicating failure
     */
	private ActionResult goBoat(Game game,Player player) {
		game.addMessage("That is not possible",true,true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Checks if the command is not a valid direction.
     */
	private boolean isNotDirection(ParsedCommand command) {
		return command.getNounNumber()>GameEntities.MOVE_NOT_DIRECTION;
	}
	
    /**
     * Handles invalid directional commands.
     *
     * @return an {@link ActionResult} indicating invalid input
     */
	private ActionResult notDirection(Game game,Player player) {
		game.addMessage("I don't understand",true,true);
		return new ActionResult(game,player,false);
	}
	
    /**
     * Checks if there is no exit in the given direction.
     */
	private boolean isExitBlocked(Game game, int room, int nounNumber) {
		return !game.checkExit(room,nounNumber-1);
	}
	
    /**
     * Handles blocked exits (no exit in that direction).
     *
     * @return an {@link ActionResult} indicating failure
     */
	private ActionResult exitBlocked(Game game, Player player) {
		game.addMessage("You can't go that way",true,true);
		return new ActionResult(game, player, false);
	}
	
    /**
     * Checks if the player is catching the ferry.
     */
	private boolean isCatchingFerry(Game game,Player player,ParsedCommand command) {
		return player.getRoom()==game.getItem(GameEntities.ITEM_BOAT).getItemLocation() && 
				command.getNounNumber() == GameEntities.ITEM_BOAT;
	}
	
    /**
     * Handles the ferry sequence (either enslavement or transport).
     *
     * @return an {@link ActionResult} after ferry interaction
     */
	private ActionResult catchingFerry(Game game,Player player) {
		
		if ((int) player.getStat("wisdom")<60) {
			game.addPanelMessage("You board the craft ...", true);
			game.addPanelMessage("falling under the spell of the boatman",false);
			game.addPanelMessage("and are taken to the Island of Secrets ...", false);
			game.addPanelMessage("to serve Omegan forever.", false);
			game.getItem(Constants.NUMBER_OF_NOUNS).setItemFlag(1);
			game.setEndGameState();
		} else {
			game.addPanelMessage("You board the craft ...", true);
			game.addPanelMessage("and are taken to the Island of Secrets ...", false);
			player.setRoom(57);
		}
		game.addMessage("The boat skims the dark and silent waters.",true,true);
		game.setMessageGameState();
		
		return new ActionResult(game,player,true);
	}
}

/* 4 May 2025 - Created File
 * 5 May 2025 - Removed Magic Numbers and replaced with constants
 * 8 May 2025 - Added execution functionality
 * 21 May 2025 - Started moving constants to GameEntities
 * 22 May 2025 - Moved Constants to GameEntities
 * 26 May 2025 - Updated ParseMove
 * 28 May 2025 - Moved checks to separate functions for readability
 * 22 June 2025 - Moved single direction elsewhere
 * 23 June 2025 - Fixed problem with movement noun number. Reduced to reflect direction.
 * 24 June 2025 - Fixed error that resulted in movement number greater than 4
 * 25 June 2025 - Fixed problem with boat, and made move validation readable.
 * 6 July 2025 - Fixed problem going into and out of hut
 * 21 July 2025 - Added check to prevent using first four nouns to move
 * 3 September 2025 - Updated for new ActionResult
 * 					- Updated based on recommendations
 * 					- Added JavaDocs
 * 28 October 2025 - Added function to handle going down into trapdoor
 * 29 October 2025 - Fixed so that says door closed
 * 29 November 2025 - Added stat clearing to go boat
 * 3 December 2025 - Increased version number
 */