/*
Title: Island of Secrets Move Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package commands;

import command_process.ActionResult;
import command_process.ParsedCommand;
import data.Constants;
import data.GameEntities;
import game.Game;
import game.Player;

/**
 * Handles player consumption-related actions such as eating, drinking, and resting.
 * <p>
 * This class validates and executes consumption commands parsed from player input.
 * It adjusts player stats (e.g., food, drink, strength, wisdom, time) and updates
 * the {@link Game} state with messages and flags depending on the outcome.
 * </p>
 */
public class Consume {
	
	private int nounNumber;
	private String noun;
	private ParsedCommand command;
	
    /**
     * Constructs a new Consume action from the given parsed command.
     *
     * @param command the parsed player command containing verb, noun, and coded data
     */
	public Consume(ParsedCommand command) {
		nounNumber = command.getNounNumber();
		noun = command.getSplitTwoCommand()[1];
		this.command = command;
	}
	
    /**
     * Parses an "eat" command, resolving general words like "food"
     * into specific game entity identifiers (e.g., bread).
     *
     * @return a new {@link ParsedCommand} with updated noun values
     */
	public ParsedCommand parseEat() {
				
		if (noun.equals("food")) {
			nounNumber = GameEntities.ITEM_BREAD;
		}
		
		return new ParsedCommand(command.getVerbNumber(),nounNumber,command.getCodedCommand(),
				command.getSplitTwoCommand(),command.getCommand());
	}
	
    /**
     * Validates whether the player can eat the specified noun.
     * <ul>
     *     <li>Fails if the noun is outside the valid food range.</li>
     *     <li>Fails if the player has no food left.</li>
     *     <li>Reduces wisdom for invalid attempts.</li>
     * </ul>
     *
     * @param game   the current game state
     * @param player the player attempting to eat
     * @return an {@link ActionResult} indicating success or failure
     */
	public ActionResult validateEat(Game game, Player player) {

		boolean validEat = true;
		
		if (noun.equals(GameEntities.NOUN_APPLE)) {
			nounNumber = Constants.FOOD_THRESHOLD+1;
		} else if (noun.equals(GameEntities.NOUN_LILY)) {
			nounNumber = GameEntities.ITEM_LILY;
		}
		
		if ((nounNumber<=Constants.FOOD_THRESHOLD || nounNumber>=Constants.DRINK_THRESHOLD) 
				&& nounNumber != GameEntities.ITEM_LILY && noun.length()>0) {
				
				game.addMessage("You can't "+command.getCommand(),true,true);
				player.setStat("wisdom",(int) player.getStat("wisdom")-1);
				validEat = false;
		} else if (((int) player.getStat("food"))<1) {
			game.addMessage("You have no food",true,true);
			validEat = false;
		}
		
		return new ActionResult(game,player,validEat);
	}
		
    /**
     * Validates whether the player can drink the specified noun.
     * <ul>
     *     <li>Fails if the noun is outside the valid drink range.</li>
     *     <li>Fails if the player has no drink left.</li>
     *     <li>Reduces wisdom for invalid attempts.</li>
     * </ul>
     *
     * @param game   the current game state
     * @param player the player attempting to drink
     * @return an {@link ActionResult} indicating success or failure
     */
	public ActionResult validateDrink(Game game, Player player) {

		boolean validDrink = true;

		if ((nounNumber<Constants.DRINK_THRESHOLD || nounNumber>Constants.MAX_CARRIABLE_ITEMS) 
				&& nounNumber != GameEntities.ITEM_LIQUID && noun.length()>0) {
				game.addMessage("You can't "+command.getCommand(),true,true);
				player.setStat("wisdom",(int) player.getStat("wisdom")-1);
				validDrink = false;
		} else if (((int) player.getStat("drink"))<1) {
			game.addMessage("You have no drink.",true,true);
			validDrink = false;
		}
		return new ActionResult(game,player,validDrink);
	}
	
	public ActionResult validateDrinkWine(Game game,Player player) {
		return new ActionResult(game,player,true);
	}
	
    /**
     * Executes the parsed command by delegating to the appropriate handler
     * (eat, drink, or rest).
     *
     * @param game   the current game state
     * @param player the player performing the action
     * @return an {@link ActionResult} representing the action outcome
     */
	public ActionResult executeCommand(Game game,Player player) {
		
		ActionResult result = new ActionResult(game,player,false);
		
		if (command.checkEat()) {
			result = executeEat(game,player);
		} else if (command.checkDrink()) {
			result = executeDrink(game,player);
		} else if (command.checkRest()) {
			result = executeRest(game,player);
		}
		return result;
	}
	
    /**
     * Executes an "eat" command, handling food and special cases like lilies.
     */
	private ActionResult executeEat(Game game,Player player) {
		
		ActionResult result = new ActionResult(game,player,false);
	
		if (isEatingLillies(nounNumber,game)) {
			result = eatLillies(game,player);
		} else if (isEatingLilliesNotCarrying(nounNumber,game)) {
			result = notCarryingLillies(game,player);
		} else {
			result = eatFood(game,player);
		}
		return result;
	}
	
    /**
     * Executes a "drink" command, handling normal drinks and dangerous liquids.
     */
	private ActionResult executeDrink(Game game,Player player) {

		ActionResult result = new ActionResult(game,player,false);

		if (isDrinkingLiquid(nounNumber)) {
			result = drinkLiquid(game,player,nounNumber);
		} else {
			result = drink(game,player);
		}
		return result;
	}
	
    /**
     * Executes a "rest" command, reducing time and adjusting stats.
     * Grants wisdom if enough time passes and updates storm-related flags.
     */
	private ActionResult executeRest(Game game, Player player) {
		
		int count = determineCount(game);
		player = determineRestPeriod(count,game,player);

		if (isEnoughTime(game,player)) {
			player.setStat("wisdom",(int) player.getStat("wisdom")+2);
			game.getItem(36).setItemFlag(1);
		}
				
		game = setMessage(count,game);
		
		return new ActionResult(game,player,true);	
	}
	
    /**
     * Determines how many iterations of rest should pass based on the storm flag.
     */
	private int determineCount(Game game) {
		return Math.abs(game.getItem(GameEntities.ITEM_STORM).getItemFlag()+3);
	}
	

    /**
     * Reduces the player's time and strength during rest, depending on conditions.
     */
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
	
    /** @return true if the player is eating lilies while carrying them. */
	private boolean isEatingLillies(int nounNumber,Game game) {
		return nounNumber == GameEntities.ITEM_LILY && 
				game.getItem(GameEntities.ITEM_LILY).getItemLocation()==0;
	}
	
    /** @return true if the player is eating lilies while not carrying them. */
	private boolean isEatingLilliesNotCarrying(int nounNumber,Game game) {
		return nounNumber == GameEntities.ITEM_LILY && 
				game.getItem(GameEntities.ITEM_LILY).getItemLocation()!=0;
	}
	
    /** @return true if the noun corresponds to liquid. */
	private boolean isDrinkingLiquid(int nounNumber) {
		return nounNumber == GameEntities.ITEM_LIQUID;
	}
	
	/** @return true if the player has enough time remaining or the storm has cleared. */
	private boolean isEnoughTime(Game game, Player player) {
		return (int) player.getStat("timeRemaining")>100 || 
				game.getItem(GameEntities.ITEM_STORM).getItemFlag()<1;
	}
	
    /**
     * Applies the effects of eating lilies: reduces wisdom and strength,
     * and adds an illness message.
     */
	private ActionResult eatLillies(Game game, Player player) {
		player.setStat("wisdom",(int) player.getStat("wisdom")-5);
		player.setStat("strength",(float) player.getStat("strength")-2);
		game.addMessage("They make you very ill",true,true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Result when the player attempts to eat the lillies while not carrying them.
     */
	private ActionResult notCarryingLillies(Game game, Player player) {
		player.setStat("wisdom",(int) player.getStat("wisdom")-1);
		game.addMessage("You aren't carrying them",true,true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Applies the effects of eating normal food: decreases food supply,
     * increases strength, and confirms success.
     */
	private ActionResult eatFood(Game game, Player player) {
		player.setStat("food",((int) player.getStat("food"))-1);
		player.setStat("strength",(float) player.getStat("strength")+10);
		game.addMessage("Ok",true,true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Applies the effects of drinking a liquid:
     * <ul>
     *     <li>If no jug is available, the action fails.</li>
     *     <li>If consumed, reduces strength and wisdom, displays messages, and simulates time passing.</li>
     * </ul>
     */
	private ActionResult drinkLiquid(Game game,Player player,int nounNumber) {
		
		boolean success = false;
		System.out.println(game.getItemFlagSum(GameEntities.ITEM_JUG));
		if(game.getItemFlagSum(GameEntities.ITEM_JUG)!=-1) {
			game.addMessage("You don't have a jug full of bubbling green liquid",true,true);
		} else {
			game.addMessage("Ouch!",true,true);
			player.setStat("strength",(float) player.getStat("strength")-4);
			player.setStat("wisdom",(int) player.getStat("wisdom")-7);
			success = true;
			
			int count = determineCount(game);
			game.setMessageGameState();
			game.addPanelMessage("You taste a drop and ...",true);
			
			for (int i=0;i<count;i++) {
				game.addPanelMessage("Time passes ...", false);
			}
		}
		return new ActionResult(game,player,success);
	}
	
    /**
     * Applies the effects of drinking normal consumables:
     * decreases drink supply, increases strength, and confirms success.
     */
	private ActionResult drink(Game game,Player player) {
		
		player.setStat("drink",((int) player.getStat("drink"))-1);
		player.setStat("strength",(float) player.getStat("strength")+7);
		game.addMessage("Ok",true,true);
		
		return new ActionResult(game,player,true);
	}
	
    /**
     * Displays rest-related messages to the game panel,
     * simulating time passing.
     */
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
 * 2 September 2025 - Updated based on new ActionResult
 * 4 September 2025 - Removed extraneous code and tightened if statements
 * 4 November 2025 - Fixed problem where unable to eat lilies
 * 				   - Fixed problem with drinking liquid
 * 5 November 2025 - Added check to confirm drinking wine
 * 3 December 2025 - Increased version number
 */