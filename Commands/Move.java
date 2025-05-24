/*
Title: Island of Secrets Move Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 22 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Data.GameEntities;
import Game.Game;
import Game.Player;
import java.util.Random;

public class Move {
	
	public ParsedCommand parseMove(ParsedCommand command,int room) {
		
		int verbNumber = command.getVerbNumber();
		int nounNumber = command.getNounNumber();
		String noun = command.getSplitTwoCommand()[1];
		String code = command.getCodedCommand();
		
		if (nounNumber == -1) {
			nounNumber = verbNumber;
		} else if (nounNumber>Constants.NUMBER_OF_ITEMS && nounNumber<Constants.NUMBER_OF_NOUNS) {
			nounNumber = nounNumber-Constants.NUMBER_OF_ITEMS;
		}
		
		if ((room == GameEntities.ROOM_CAVE && noun.equals("cave")) || 
			(room == GameEntities.ROOM_CLEARING && noun.equals("hut")) ||
			(room ==GameEntities.ROOM_BUILDING && noun.equals("hut"))) {
			nounNumber = GameEntities.WEST;
		} else if (room == GameEntities.ROOM_ABODE_HUT && noun.equals("hut")) {
			nounNumber=GameEntities.NORTH;
		}
		
		if (code.equals("500012") || code.equals("500053") || code.equals("500045")) {
			nounNumber = GameEntities.WEST;
		} else if (code.equals("500070")||code.equals("500037")||code.equals("510011")||
				   code.equals("510041") ||code.equals("510043")||code.equals("490066")||
				   code.equals("490051")) {
			nounNumber = GameEntities.NORTH;
		} else if (code.equals("510060")||code.equals("480056")) {
			nounNumber = GameEntities.SOUTH;
		} else if (code.equals("510044")||code.equals("510052")) {
			nounNumber = GameEntities.EAST;
		}
		
		return new ParsedCommand(verbNumber,nounNumber,command.getCodedCommand(),
				command.getSplitTwoCommand(),command.getCommand());
	}

	public ActionResult validateMove(ParsedCommand command, Game game, int room) {
		
		boolean validMove = true;
		
		if (!game.checkExit(room,command.getNounNumber()-1)) {
			game.addMessage("You can't go that way",true,true);
			validMove = false;
		} else if (command.getNounNumber()>4) {
			game.addMessage("I don't understand",true,true);
			validMove = false;
		}
		
		return new ActionResult(game,validMove);
	}
	
	public ActionResult executeMove(Game game, Player player, ParsedCommand command) {
		
		ActionResult result = moveBlocked(game,player,command);
		
		//Move is not blocked
		if (!result.getValid()) {
			int direction = command.getNounNumber();
			int newRoom = player.getRoom() + Integer.parseInt(
					"-10+10+01-01".substring((direction-1)*3, ((direction-1)*3)+3));
			player.setRoom(newRoom);
			game.addMessage("Ok",true,true);
			game.getRoom(newRoom).setVisited();
			
			result = moveResponse(game,player,command);
		}
		
		return result;
	}
	
	//Can move but event blocks movement
	private ActionResult moveBlocked(Game game, Player player, ParsedCommand command) {
		
		boolean moveBlocked = false;
		
		//Prevents Player from leaving is Omegan present and strength/wisdom too little, or in lair
		if (game.getItem(GameEntities.ITEM_OMEGAN).isAtLocation(player.getRoom()) &&
			(player.getStrengthWisdon()<180 || player.getRoom()==GameEntities.ROOM_SANCTUM)) {
			game.addMessage("Omegan's presence prevents you from leaving!",true,true);
			moveBlocked = true;

		} else if (player.getRoom() == game.getItem(GameEntities.ITEM_SWAMPMAN).getItemLocation() && 
					game.getItem(GameEntities.ITEM_SWAMPMAN).getItemFlag()<1 && 
					command.getNounNumber() == GameEntities.EAST) {
			game.addMessage("He will not let you pass.",true,true);
			moveBlocked = true;
		
		//The Rocks
		} else if (player.getRoom() == GameEntities.ROOM_CASTLE_ENTRANCE && 
				   game.getItem(GameEntities.ITEM_ROCKS).getItemFlag()==0) {
			game.addMessage("The rocks move to prevent you",true,true);
			moveBlocked = true;
		
		//Room with Arms
		} else if (player.getRoom() == GameEntities.ROOM_WITH_HANDS && 
				   game.getItem(GameEntities.ITEM_TORCH).getItemFlag()!=1) {
			game.addMessage("The arms hold you fast",true,true);
			moveBlocked = true;
		
		//Snake at grandpa's Shack
		} else if (player.getRoom()==GameEntities.ROOM_CLEARING && 
				   game.getItem(GameEntities.ITEM_SNAKE).getItemFlag()==0 && 
				   command.getNounNumber() == GameEntities.WEST) {
			game.addMessage("Hisss!",true,true);
			moveBlocked = true;
		
		//Looks like need canyon beast to climb the path	
		} else if (player.getRoom() == GameEntities.ROOM_ROCKY_PATH && 
				   game.getItemFlagSum(GameEntities.ITEM_BEAST) != -1 && 
				   command.getNounNumber() == GameEntities.EAST) {
			game.addMessage("Too steep to climb",true,true);
			moveBlocked = true;
		
		} else if (player.getRoom() == GameEntities.ROOM_STOREROOM && command.getNounNumber() == GameEntities.EAST) {
			game.addMessage("The door is barred!",true,true);
			moveBlocked = true;
		}
		
		return new ActionResult(game,player,moveBlocked);
	}
	
	private ActionResult moveResponse(Game game,Player player,ParsedCommand command) {
		
		Random rand = new Random();
		
		//Room with the hands
		if (player.getRoom()==28 && game.getItem(7).getItemFlag()!=1) {
			game.addMessage("You enter the room and giant hands grab you and hold you fast",false,true);
		} else if (player.getRoom()==28) {
			game.addMessage("You enter the room and brightly shining torch force the arms to retreat to the walls",false,true);
		} else if (player.getRoom()==27 && command.getNounNumber()==1) {
			game.addMessage("The doors slam shut behind you preventing you from leaving",false,true);
		}
		
		//Does the player have the beast and is on the jetty
		if (player.getRoom() == 33 && game.getItem(16).getItemLocation()==0) {
			game.getItem(16).setItemLocation(rand.nextInt(4)+1);
			game.getItem(16).setItemFlag(0);
			game.addMessage("The beast runs away",false,true);
		}
		
		//Handling the ferry man
		if (player.getRoom()==game.getItem(25).getItemLocation() && command.getNounNumber() == 25) {
			
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
		}
		return new ActionResult(game,player);
	}
}

/* 4 May 2025 - Created File
 * 5 May 2025 - Removed Magic Numbers and replaced with constants
 * 8 May 2025 - Added execution functionality
 * 21 May 2025 - Started moving constants to GameEntities
 * 22 May 2025 - Moved Constants to GameEntities
 */