/*
Title: Island of Secrets Move Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 4 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Game.Game;
import Game.Player;

public class Move {
	
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
		
		if ((room == 12 && noun.equals("cave")) || 
			(room == 45 && noun.equals("hut")) ||
			(room ==53 && noun.equals("hut"))) {
			nounNumber = 4;
		} else if (room == 70 && noun.equals("hut")) {
			nounNumber=1;
		}
		
		if (code.equals("500012") || code.equals("500053") || code.equals("500045")) {
			nounNumber = 4;
		} else if (code.equals("500070")||code.equals("500037")||code.equals("510011")||
				   code.equals("510041") ||code.equals("510043")||code.equals("490066")||
				   code.equals("490051")) {
			nounNumber = 1;
		} else if (code.equals("510060")||code.equals("480056")) {
			nounNumber = 2;
		} else if (code.equals("510044")||code.equals("510052")) {
			nounNumber = 3;
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
		if (game.getItem(39).isAtLocation(player.getRoom()) &&
			(player.getStrengthWisdon()<180 || player.getRoom()==10)) {
			game.addMessage("Omegan's presence prevents you from leaving!",true,true);

		} else if (player.getRoom() == game.getItem(32).getItemLocation() && 
					game.getItem(32).getItemFlag()<1 && command.getNounNumber() == 3) {
			game.addMessage("He will not let you pass.",true,true);
		
		//The Rocks
		} else if (player.getRoom() == 47 && game.getItem(44).getItemFlag()==0) {
			game.addMessage("The rocks move to prevent you",true,true);
		
		//Room with Arms
		} else if (player.getRoom() == 28 && game.getItem(7).getItemFlag()!=1) {
			game.addMessage("The arms hold you fast",true,true);
		
		//Snake at grandpa's Shack
		} else if (player.getRoom()==45 && game.getItem(40).getItemFlag()==0 && command.getNounNumber() == 4) {
			game.addMessage("Hisss!",true,true);
		
		//Looks like need canyon beast to climb the path	
		} else if (player.getRoom() == 25 && game.getItemFlagSum(16) != -1 && command.getNounNumber() ==3) {
			game.addMessage("Too steep to climb",true,true);
		
		} else if (player.getRoom() == 51 && command.getNounNumber() == 3) {
			game.addMessage("The door is barred!",true,true);
		}
		
		return new ActionResult(game,player);
	}	
}

/* 4 May 2025 - Created File
 * 
 */