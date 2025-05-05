/*
Title: Island of Secrets Move Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 5 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Game.Game;
import Game.Player;

public class Move {
	
	// === Rooms ===
	private static final int ROOM_ROCKY_PATH = 25;
	private static final int ROOM_LOW_PASSAGE = 28;
	private static final int ROOM_CASTLE_ENTRANCE = 47;
	private static final int ROOM_STOREROOM = 51;
	private static final int ROOM_CAVE = 12;
	private static final int ROOM_CLEARING = 45;
	private static final int ROOM_BUILDING = 53;
	private static final int ROOM_ABODE_HUT = 70;
	private static final int ROOM_SANCTUM = 10;

	// === Items ===
	private static final int ITEM_OMEGAN = 39;
	private static final int ITEM_SNAKE = 40;
	private static final int ITEM_CANYON_BEAST = 16;
	private static final int ITEM_TORCH = 7;
	private static final int ITEM_SWAMPMAN = 32;
	private static final int ITEM_ROCKS = 44;
	
	// === Directions ===
	private static final int NORTH = 1;
	private static final int SOUTH = 2;
	private static final int EAST = 3;
	private static final int WEST = 4;
	
	public ParsedCommand parseMove(ParsedCommand command,int room) {
		
		int verbNumber = command.getVerbNumber();
		int nounNumber = command.getNounNumber();
		String noun = command.getSplitCommand()[1];
		String code = command.getCodedCommand();
		
		if (nounNumber == -1) {
			nounNumber = verbNumber;
		} else if (nounNumber>Constants.NUMBER_OF_ITEMS && nounNumber<Constants.NUMBER_OF_NOUNS) {
			nounNumber = nounNumber-Constants.NUMBER_OF_ITEMS;
		}
		
		if ((room == ROOM_CAVE && noun.equals("cave")) || 
			(room == ROOM_CLEARING && noun.equals("hut")) ||
			(room ==ROOM_BUILDING && noun.equals("hut"))) {
			nounNumber = WEST;
		} else if (room == ROOM_ABODE_HUT && noun.equals("hut")) {
			nounNumber=NORTH;
		}
		
		if (code.equals("500012") || code.equals("500053") || code.equals("500045")) {
			nounNumber = WEST;
		} else if (code.equals("500070")||code.equals("500037")||code.equals("510011")||
				   code.equals("510041") ||code.equals("510043")||code.equals("490066")||
				   code.equals("490051")) {
			nounNumber = NORTH;
		} else if (code.equals("510060")||code.equals("480056")) {
			nounNumber = SOUTH;
		} else if (code.equals("510044")||code.equals("510052")) {
			nounNumber = EAST;
		}
		
		return new ParsedCommand(verbNumber,nounNumber,command.getCodedCommand(),
				command.getSplitCommand(),command.getCommand());
	}

	public ActionResult validateMove(ParsedCommand command, Game game, int room) {
		
		boolean validMove = true;
		
		if (!game.checkExit(room,command.getNounNumber()-1)) {
			game.addMessage("You can't go that way",true,true);
			validMove = false;
		}
		
		return new ActionResult(game,validMove);
	}
	
	//Can move but event blocks movement
	public ActionResult moveBlocked(Game game, Player player, ParsedCommand command) {
		
		//Prevents Player from leaving is Omegan present and strength/wisdom too little, or in lair
		if (game.getItem(ITEM_OMEGAN).isAtLocation(player.getRoom()) &&
			(player.getStrengthWisdon()<180 || player.getRoom()==ROOM_SANCTUM)) {
			game.addMessage("Omegan's presence prevents you from leaving!",true,true);

		} else if (player.getRoom() == game.getItem(ITEM_SWAMPMAN).getItemLocation() && 
					game.getItem(ITEM_SWAMPMAN).getItemFlag()<1 && 
					command.getNounNumber() == EAST) {
			game.addMessage("He will not let you pass.",true,true);
		
		//The Rocks
		} else if (player.getRoom() == ROOM_CASTLE_ENTRANCE && 
				   game.getItem(ITEM_ROCKS).getItemFlag()==0) {
			game.addMessage("The rocks move to prevent you",true,true);
		
		//Room with Arms
		} else if (player.getRoom() == ROOM_LOW_PASSAGE && 
				   game.getItem(ITEM_TORCH).getItemFlag()!=1) {
			game.addMessage("The arms hold you fast",true,true);
		
		//Snake at grandpa's Shack
		} else if (player.getRoom()==ROOM_CLEARING && 
				   game.getItem(ITEM_SNAKE).getItemFlag()==0 && 
				   command.getNounNumber() == WEST) {
			game.addMessage("Hisss!",true,true);
		
		//Looks like need canyon beast to climb the path	
		} else if (player.getRoom() == ROOM_ROCKY_PATH && 
				   game.getItemFlagSum(ITEM_CANYON_BEAST) != -1 && 
				   command.getNounNumber() == EAST) {
			game.addMessage("Too steep to climb",true,true);
		
		} else if (player.getRoom() == ROOM_STOREROOM && command.getNounNumber() == EAST) {
			game.addMessage("The door is barred!",true,true);
		}
		
		return new ActionResult(game,player);
	}	
}

/* 4 May 2025 - Created File
 * 5 May 2025 - Removed Magic Numbers and replaced with constants
 */