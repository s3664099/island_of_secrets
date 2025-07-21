/*
Title: Island of Secrets Move Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.12
Date: 21 July 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Data.GameEntities;
import Game.Game;
import Game.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Move {
	
	private static final int[] DIRECTION_MODIFIERS = {-10, +10, +01, -01};
	private final Random rand = new Random();
	
	public ParsedCommand parseMove(ParsedCommand command,int room) {
		
		int verbNumber = command.getVerbNumber();
		int nounNumber = command.getNounNumber();
		String noun = command.getSplitTwoCommand()[1];
		String code = command.getCodedCommand();
		nounNumber = handleSpecialRooms(room, noun, nounNumber);
		nounNumber = handleCodedCommand(code,nounNumber);
		
		if (verbNumber == GameEntities.CMD_GO && nounNumber>0 && nounNumber<5) {
			nounNumber = 8;
		} else if(nounNumber>4 && nounNumber != GameEntities.ITEM_BOAT) {
			nounNumber -= 43;
		}
		
		return new ParsedCommand(verbNumber,nounNumber,command.getCodedCommand(),
				command.getSplitTwoCommand(),command.getCommand());
	}
	
	public int parseSingleDirection(int nounNumber, int verbNumber) {

		if (nounNumber == -1) {
			nounNumber = verbNumber;
		} else if (nounNumber>Constants.NUMBER_OF_ITEMS && nounNumber<Constants.NUMBER_OF_NOUNS) {
			nounNumber = nounNumber-Constants.NUMBER_OF_ITEMS;
		}
		return nounNumber;
	}
	
	private int handleCodedCommand(String code,int nounNumber) {
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
		codeToDirection.put(GameEntities.CODE_DOWN_TRAPDOOR,GameEntities.NORTH);
		codeToDirection.put(GameEntities.CODE_OUT_ABODE_HUT,GameEntities.SOUTH);
		codeToDirection.put(GameEntities.CODE_UP_PYRAMID,GameEntities.SOUTH);
		codeToDirection.put(GameEntities.CODE_OUT_SHACK,GameEntities.EAST);
		codeToDirection.put(GameEntities.CODE_OUT_HALL,GameEntities.EAST);
		return codeToDirection.getOrDefault(code, nounNumber);
	}
	
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

	public ActionResult validateMove(ParsedCommand command, Game game, int room) {
		
		boolean validMove = true;
		ActionResult result = new ActionResult(game,validMove);
		
		if (isGoBoat(command.getNounNumber())) {
			result = goBoat(game);
		} else if (isNonStandardDirection(command)) {
			result = exitBlocked(game);
		} else if (isNotDirection(command)) {
			result = notDirection(game);
		} else if (isExitBlocked(game,room,command.getNounNumber())) {
			result = exitBlocked(game);
		} 
		
		return result;
	}
	
	public ActionResult executeMove(Game game, Player player, ParsedCommand command) {
		
		ActionResult blockedCheck = checkMoveBlocked(game,player,command);
		
		//Move is not blocked
		if (!blockedCheck.getValid()) {
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
	
	private int calculateNewRoom(int currentRoom, int direction) {
		return currentRoom + DIRECTION_MODIFIERS[direction-1];
	}
	
	//Can move but event blocks movement
	private ActionResult checkMoveBlocked(Game game, Player player, ParsedCommand command) {
		
		ActionResult result = new ActionResult(game,player,false);
		
		if (isOmeganPresent(game,player)) {
			//game.addMessage("Omegan's presence prevents you from leaving!",true,true);
			//result = new ActionResult(game,player,true);

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
		
		ActionResult result = new ActionResult(game,player);
		
		//Handling the ferry man - MOVE TO SEPARATE FUNCTION
		if (isCatchingFerry(game,player,command)) {
			result = catchingFerry(game,player);
		}
		return result;
	}
	
	private boolean isOmeganPresent(Game game, Player player) {
		boolean omeganPresent = false;
		if (game.getItem(GameEntities.ITEM_OMEGAN).isAtLocation(player.getRoom()) &&
		   (player.getStrengthWisdon()<180 || player.getRoom()==GameEntities.ROOM_SANCTUM)) {
			omeganPresent = true;
		}
		return omeganPresent;
	}
	
	private boolean isSwampmanPresent(Game game, Player player, ParsedCommand command) {
		boolean swampmanPresent = false;
		if(player.getRoom() == game.getItem(GameEntities.ITEM_SWAMPMAN).getItemLocation() && 
		   game.getItem(GameEntities.ITEM_SWAMPMAN).getItemFlag()<1 && 
		   command.getNounNumber() == GameEntities.EAST) {
			swampmanPresent = true;
		}
		return swampmanPresent;
	}
	
	private boolean isNonStandardDirection(ParsedCommand command) {
		boolean nonStandard = false;
		if (command.getNounNumber()>4 && command.getNounNumber()<8)  {
			nonStandard = true;
		}
		return nonStandard;
	}
	
	private boolean areRocksMoving(Game game, Player player) {
		boolean rocksMoving = false;
		if(player.getRoom() == GameEntities.ROOM_CASTLE_ENTRANCE && 
		   game.getItem(GameEntities.ITEM_ROCKS).getItemFlag()==0) {
			rocksMoving = true;
		}
		return rocksMoving;
	}
	
	private boolean doArmsHoldYou(Game game,Player player) {
		boolean armsHoldYou = false;
		if(player.getRoom() == GameEntities.ROOM_WITH_HANDS && 
		   game.getItem(GameEntities.ITEM_TORCH).getItemFlag()!=1) {
			armsHoldYou = true;
		}
		return armsHoldYou;
	}
	
	private boolean isSnakePresent(Game game,Player player,ParsedCommand command) {
		boolean snakePresent = false;
		if(player.getRoom()==GameEntities.ROOM_CLEARING && 
		   game.getItem(GameEntities.ITEM_SNAKE).getItemFlag()==0 && 
		   command.getNounNumber() == GameEntities.WEST) {
			snakePresent = true;
		}
		return snakePresent;
	}
	
	private boolean isPlayerRidingBeast(Game game,Player player,ParsedCommand command) {
		boolean playerRidingBeast = false;
		if(player.getRoom() == GameEntities.ROOM_ROCKY_PATH && 
		   game.getItemFlagSum(GameEntities.ITEM_BEAST) != -1 && 
		   command.getNounNumber() == GameEntities.EAST) {
			playerRidingBeast = true;
		}
		return playerRidingBeast;
	}
	
	private boolean isDoorClosed(Player player,ParsedCommand command) {
		boolean doorClosed = false;
		if(player.getRoom() == GameEntities.ROOM_STOREROOM && 
		   command.getNounNumber() == GameEntities.EAST) {
			doorClosed = true;
		}
		return doorClosed;
	}
	
	private boolean isInHandsRoom(Game game,Player player) {
		boolean isInHandsRoom = false;
		if (player.getRoom()==GameEntities.ROOM_WITH_HANDS && 
			game.getItem(GameEntities.ITEM_TORCH).getItemFlag()!=1) {
			isInHandsRoom = true;
		}
		return isInHandsRoom;
	}
	
	private boolean isInEntranceHall(Player player, ParsedCommand command) {
		boolean isInEntranceHall = false;
		if(player.getRoom()==GameEntities.ROOM_ENTRANCE_CHAMBER && command.getNounNumber()==1) {
			isInEntranceHall = true;
		}
		return isInEntranceHall;
	}
	
	private boolean isOnJetty(Game game, Player player) {
		boolean isOnJetty = false;
		if(player.getRoom() == GameEntities.ROOM_JETTY && 
		   game.getItem(GameEntities.ITEM_BEAST).getItemLocation()==0) {
			isOnJetty = true;
		}
		return isOnJetty;
	}
	
	private boolean isGoBoat(int nounNumber) {
		boolean goBoat = false;
		if (nounNumber == GameEntities.ITEM_BOAT) {
			goBoat = true;
		}
		return goBoat;
	}
	
	private ActionResult goBoat(Game game) {
		game.addMessage("That is not possible",true,true);
		return new ActionResult(game,true);
	}
	
	private boolean isNotDirection(ParsedCommand command) {
		boolean notDirection = false;
		if(command.getNounNumber()>4) {
			notDirection = true;
		}
		return notDirection;
	}
	
	private ActionResult notDirection(Game game) {
		game.addMessage("I don't understand",true,true);
		return new ActionResult(game,false);
	}
	
	private boolean isExitBlocked(Game game, int room, int nounNumber) {
		boolean exitBlocked = false;
		if (!game.checkExit(room,nounNumber-1)) {
			exitBlocked = true;
		}
		return exitBlocked;
	}
	
	private ActionResult exitBlocked(Game game) {
		game.addMessage("You can't go that way",true,true);
		return new ActionResult(game, false);
	}
	
	private boolean isCatchingFerry(Game game,Player player,ParsedCommand command) {
		boolean isCatchingFerry = false;
		if(player.getRoom()==game.getItem(GameEntities.ITEM_BOAT).getItemLocation() && 
		   command.getNounNumber() == GameEntities.ITEM_BOAT) {
			isCatchingFerry = true;
		}
		return isCatchingFerry;
	}
	
	private ActionResult catchingFerry(Game game,Player player) {
		
		if ((int) player.getStat("wisdom")<60) {
			game.addPanelMessage("You board the craft ...", true);
			game.addPanelMessage("falling under the spell of the boatman",false);
			game.addPanelMessage("and are taken to the Island of Secrets ...", false);
			game.addPanelMessage("to serve Omegan forever.", false);
			game.getItem(Constants.NUMBER_OF_NOUNS).setItemFlag(1);
		} else {
			game.addPanelMessage("You board the craft ...", true);
			game.addPanelMessage("and are taken to the Island of Secrets ...", false);
			player.setRoom(57);
		}
		game.addMessage("The boat skims the dark and silent waters.",true,true);
		game.setMessageGameState();
		
		return new ActionResult(game,player);
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
 */