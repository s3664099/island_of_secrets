/*
Title: Island of Secrets Move Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.7
Date: 26 July 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package commands;

import data.Constants;
import data.GameEntities;
import game.Game;
import game.Player;

public class Consume {
	
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
		boolean validEat = true;
		
		if (noun.equals("apple")) {
			nounNumber = Constants.FOOD_THRESHOLD+1;
		}

		if ((nounNumber<=Constants.FOOD_THRESHOLD || nounNumber>=Constants.DRINK_THRESHOLD) 
				&& noun.length()>0) {
				
				game.addMessage("You can't "+command.getCommand(),true,true);
				player.setStat("wisdom",(int) player.getStat("wisdom")-1);
				validEat = false;
		} else if (((int) player.getStat("food"))<1) {
			game.addMessage("You have no food",true,true);
			validEat = false;
		}
		
		return new ActionResult(game,player,validEat);
	}
	
	public ActionResult validateDrink(ParsedCommand command, Game game, Player player) {
		int nounNumber = command.getNounNumber();
		String noun = command.getSplitTwoCommand()[1];
		boolean validDrink = true;
		
		if ((nounNumber<Constants.DRINK_THRESHOLD || nounNumber>Constants.MAX_CARRIABLE_ITEMS) 
				&& noun.length()>0) {
				game.addMessage("You can't "+command,true,true);
				player.setStat("wisdom",(int) player.getStat("wisdom")-1);
				validDrink = false;
		} else if (((int) player.getStat("drink"))<1) {
			game.addMessage("You have no drink.",true,true);
			validDrink = false;
		}
		return new ActionResult(game,player,validDrink);
	}
	
	public ActionResult executeCommand(Game game,Player player,ParsedCommand command) {
		
		ActionResult result = new ActionResult(game,player);
		
		if (command.checkEat()) {
			result = executeEat(game,player,command);
		} else if (command.checkDrink()) {
			result = executeDrink(game,player,command);
		} else if (command.checkRest()) {
			result = executeRest(game,player,command);
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
	
	private ActionResult executeRest(Game game, Player player, ParsedCommand command) {
		
		
		int count = determineCount(game);
		player = determineRestPeriod(count,game,player);

		if (isEnoughTime(game,player)) {
			player.setStat("wisdom",(int) player.getStat("wisdom")+2);
			game.getItem(36).setItemFlag(1);
		}
				
		game = setMessage(count,game);
		
		return new ActionResult(game,player);	
	}
	
	private int determineCount(Game game) {
		return Math.abs(game.getItem(GameEntities.ITEM_STORM).getItemFlag()+3);
	}
	
	private Player determineRestPeriod(int count,Game game, Player player) {
		
		for (int i=1;i<count;i++) {
			player.reduceStat("timeRemaining");
			if (((float) player.getStat("strength"))<100 || 
				game.getItem(GameEntities.ITEM_WINE).getItemFlag()==(player.getRoom()*-1)) {
				player.setStat("strength",(float) player.getStat("strength")-8);
			}
		}
		
		return player;
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
	
	private boolean isEnoughTime(Game game, Player player) {
		
		boolean enoughTime = false;
		if ((int) player.getStat("timeRemaining")>100 || 
			game.getItem(GameEntities.ITEM_STORM).getItemFlag()<1) {
			enoughTime = true;
		}
		return enoughTime;
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
			
			int count = determineCount(game);
			game.setMessageGameState();
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
	
	private Game setMessage(int count,Game game) {
		
		game.setMessageGameState();
		game.addPanelMessage("Time passes ...", true);
		for (int i=1;i<count;i++) {
			game.addPanelMessage("Time passes ...", false);
		}
		game.addMessage("Ok",true,true);
		
		return game;
	}
	
}

/* 28 May 2025 - Created File
 * 29 May 2025 - Added the eat function
 * 			   - Added drink function and set up for rest
 * 20 May 2025 - added the rest functionality
 * 11 July 2025 - Fixed display error can't eat food.
 * 12 July 2025 - Fixed problem with eat not executing
 * 13 July 2025 - Fixed problem with drink not executing. Allowed eat apple.
 * 14 July 2025 - Fixed problem where eating and drinking more than you have
 * 26 July 2025 - Added setMessageGameState
 */