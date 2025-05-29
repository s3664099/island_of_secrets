/*
Title: Island of Secrets Move Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 29 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Data.GameEntities;
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
		} else if (((int) player.getStat("food")+1)<1) {
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
		} else if (((int) player.getStat("drink")+1)<1) {
			game.addMessage("You have no drink.",true,true);
		}
		
		return new ActionResult(game,player);
	}
	
	public ActionResult executeCommand(Game game,Player player,ParsedCommand command) {
		
		ActionResult result = new ActionResult(game,player);
		
		if (command.checkEat()) {
			result = executeEat(game,player,command);
		} else if (command.checkDrink()) {
			result = executeDrink(game,player,command);
		} else if (command.checkRest()) {
			
		}
		return result;
	}
	
	private ActionResult executeEat(Game game,Player player, ParsedCommand command) {
		
		int nounNumber = command.getNounNumber();
		ActionResult result = new ActionResult(game,player);
		
		if (isEatingLillies(nounNumber,game)) {
			result = eatLillies(game,player);
		} else {
			result = eatFood(game,player);
		}
		return result;
	}
	
	private ActionResult executeDrink(Game game,Player player,ParsedCommand command) {
		int nounNumber = command.getNounNumber();
		ActionResult result = new ActionResult(game,player);

		if (isDrinkingLiquid(nounNumber)) {
			result = drinkLiquid(game,player,nounNumber);
		} else {
			result = drink(game,player);
		}
		return result;
	}
	
	private boolean isEatingLillies(int nounNumber,Game game) {
		
		boolean isEatingLillies = false;
		if(nounNumber == GameEntities.ITEM_LILY && 
		   game.getItem(GameEntities.ITEM_LILY).getItemLocation()==0) {
			   isEatingLillies = true;
		   }
		return isEatingLillies;
	}
	
	private boolean isDrinkingLiquid(int nounNumber) {
		boolean isDrinkingLiquid = false;
		if (nounNumber == GameEntities.ITEM_LIQUID) {
			isDrinkingLiquid = true;
		}
		return isDrinkingLiquid;
	}
	
	private ActionResult eatLillies(Game game, Player player) {
		player.setStat("wisdom",(int) player.getStat("wisdom")-5);
		player.setStat("strength",(float) player.getStat("strength")-2);
		game.addMessage("They make you very ill",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult eatFood(Game game, Player player) {
		player.setStat("food",((int) player.getStat("food"))-1);
		player.setStat("strength",(float) player.getStat("strength")+10);
		game.addMessage("Ok",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult drinkLiquid(Game game,Player player,int nounNumber) {
		
		if(game.getItemFlagSum(GameEntities.ITEM_JUG)!=-1) {
			game.addMessage("You don't have "+game.getItem(nounNumber).getItemName(),true,true);
		} else {
			game.addMessage("Ouch!",true,true);
			player.setStat("strength",(float) player.getStat("strength")-4);
			player.setStat("wisdom",(int) player.getStat("wisdom")-7);
			game.setMessageGameState();
			
			int count = rest(game,player,true);
			
			game.addPanelMessage("You taste a drop and ...",true);
			
			for (int i=0;i<count;i++) {
				game.addPanelMessage("Time passes ...", false);
			}
		}
		return new ActionResult(game,player);
	}
	
	private ActionResult drink(Game game,Player player) {
		
		player.setStat("drink",((int) player.getStat("drink"))-1);
		player.setStat("strength",(float) player.getStat("strength")+7);
		game.addMessage("Ok",true,true);
		
		return new ActionResult(game,player);
	}
	
}

/*		
		??EAT
		//Eating lillies (moved here since in original game code wouldn't reach)
		if (noun == 3 && game) {
			
		
		//Item unedible
		} else 
		//Eat
		} else {
			
			
			if () {
				
			}
		}
		
		DRINK!!!
				//Drinking green liquid
		if (noun==31) {
			
			if () {
				
			} else {

			}
			
		//Item undrinkable
		}  else {
			
			
			if (((int) player.getStat("drink"))>0) {

			}
		}
		
		public int rest(Game game, Player player, boolean msgSet) {
		
		//Bases time to wait based on Living Storm flag
		int count = Math.abs(game.getItem(36).getItemFlag()+3);
						
		//Waits and increases strength
		for (int i=1;i<count;i++) {
			player.reduceStat("timeRemaining");
			if (((float) player.getStat("strength"))<100 || game.getItem(22).getItemFlag()==(player.getRoom()*-1)) {
				player.setStat("strength",(float) player.getStat("strength")-8);
			}
		}
		
		if ((int) player.getStat("timeRemaining")>100 || game.getItem(36).getItemFlag()<1) {
			player.setStat("wisdom",(int) player.getStat("wisdom")+2);
			game.getItem(36).setItemFlag(1);
		}
				
		if (!msgSet) {
			
			game.addPanelMessage("Time passes ...", true);
			for (int i=1;i<count;i++) {
				game.addPanelMessage("Time passes ...", false);
			}
			game.addMessage("Ok",true,true);
			game.setMessageGameState();
		}
		
		return count;		
	}
 */

/* 28 May 2025 - Created File
 * 29 May 2025 - Added the eat function
 * 			   - Added drink function and set up for rest
 */