/*
Title: Island of Secrets Move Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 28 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Game.Game;
import Game.Player;

public class Consume {

	//CHECK CARRYING
	
	public ParsedCommand parseEat(ParsedCommand command) {
		
		int nounNumber = command.getNounNumber();
		String noun = command.getSplitTwoCommand()[1];
		
		if (noun.equals("food")) {
			nounNumber = 17;
		}
		
		return new ParsedCommand(command.getVerbNumber(),nounNumber,command.getCodedCommand(),
				command.getSplitTwoCommand(),command.getCommand());
	}
	
	public ActionResult validateEat(ParsedCommand command, Game game, Player player) {
		int nounNumber = command.getNounNumber();
		String noun = command.getSplitTwoCommand()[1];
		
		if ((nounNumber<=Constants.FOOD_THRESHOLD || nounNumber>=Constants.DRINK_THRESHOLD) 
				&& noun.length()>0) {
				game.addMessage("You can't "+command,true,true);
				player.setStat("wisdom",(int) player.getStat("wisdom")-1);
		} else if (((int) player.getStat("food")+1)>0) {
			game.addMessage("You have no food",true,true);
		}
		
		return new ActionResult(game,player);
	}
	
	public ActionResult validateDrink(ParsedCommand command, Game game, Player player) {
		int nounNumber = command.getNounNumber();
		String noun = command.getSplitTwoCommand()[1];
		
		if ((nounNumber<Constants.DRINK_THRESHOLD || nounNumber>Constants.MAX_CARRIABLE_ITEMS) 
				&& noun.length()>0) {
				game.addMessage("You can't "+command,true,true);
				player.setStat("wisdom",(int) player.getStat("wisdom")-1);
		} else if (((int) player.getStat("drink")+1)>0) {
			game.addMessage("You have no drink.",true,true);
		}
		
		return new ActionResult(game,player);
	}
	
	
	
}

/*		
		??EAT
		//Eating lillies (moved here since in original game code wouldn't reach)
		if (noun == 3 && game.getItem(3).getItemLocation()==0) {
			player.setStat("wisdom",(int) player.getStat("wisdom")-5);
			player.setStat("strength",(float) player.getStat("strength")-2);
			game.addMessage("They make you very ill",true,true);
		
		//Item unedible
		} else 
		//Eat
		} else {
			
			
			if () {
				player.setStat("food",((int) player.getStat("food"))-1);
				player.setStat("strength",(float) player.getStat("strength")+10);
				game.addMessage("Ok",true,true);
			}
		}
		
		DRINK!!!
				//Drinking green liquid
		if (noun==31) {
			
			if (game.getItemFlagSum(4)!=-1) {
				game.addMessage("You don't have "+game.getItem(noun).getItemName(),true,true);
			} else {
				game.addMessage("Ouch!",true,true);
				player.setStat("strength",(float) player.getStat("strength")-4);
				player.setStat("wisdom",(int) player.getStat("wisdom")-7);
				game.setMessageGameState();
				
				int count = rest(game,player,true);
				
				//Sets messages
				game.addPanelMessage("You taste a drop and ...",true);
				
				for (int i=0;i<count;i++) {
					game.addPanelMessage("Time passes ...", false);
				}
			}
			
		//Item undrinkable
		}  else {
			
			
			if (((int) player.getStat("drink"))>0) {
				player.setStat("drink",((int) player.getStat("drink"))-1);
				player.setStat("strength",(float) player.getStat("strength")+7);
				game.addMessage("Ok",true,true);
			}
		}
 */

/* 28 May 2025 - Created File
*/