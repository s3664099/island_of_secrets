/*
Title: Island of Secrets Command Validator
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.14
Date: 21 July 2025
Source: https://archive.org/details/island-of-secrets_202303

For the validators - Separate the concerns - so return boolean and then add the message in a separate class
*/

package command_process;

import java.util.logging.Logger;

import commands.Consume;
import commands.ItemCommands;
import commands.Move;
import data.Constants;
import data.GameEntities;
import game.Game;
import game.Player;

public class CommandValidator {
	
	private boolean validCommand;
	private Game game;
	private static final Logger logger = Logger.getLogger(Game.class.getName());
	
	//Validations - an invalid command does not count as an action
	public ActionResult validateCommand(ParsedCommand command, Game game, Player player) {
		
		this.game = game;
		validCommand = eitherExists(command,this.game);
		validCommand = neitherExists(command,this.game);
		validCommand = missingNoun(command,this.game);
		validCommand = checkNone(command,this.game);
		validCommand = checkNoun(command);
		
		ActionResult result = new ActionResult(this.game,player,validCommand);
		logger.info("Command Valid: "+validCommand+" Code: "+command.getCodedCommand());
		
		//Special command specfic validations
		if (validCommand) {
			if (command.getCodedCommand().equals(GameEntities.CODE_DOWN_TRAPDOOR) ||
				command.getCodedCommand().equals(GameEntities.CODE_ENTER_TRAPDOOR)) {
				if (game.getItem(GameEntities.ITEM_TRAPDOOR).getItemFlag()==1) {
					result = closedTrapdoor(player);
				} else {
					command.updateState(GameEntities.CMD_SWIM);
				}
			} else if (isTrapdoorClosed(command)) {
				result = closedTrapdoor(player);
			} else if(command.checkMoveState()) {
				Move moveValidator = new Move();
				result = moveValidator.validateMove(command,this.game,player.getRoom());
			} else if(command.checkTake()) {
				ItemCommands takeValidator = new ItemCommands();
				result = takeValidator.validateTake(this.game, player.getRoom(), command);
			} else if (command.checkDrop() || command.checkGive()) {
				ItemCommands carryingValidator = new ItemCommands();
				result = carryingValidator.validateCarrying(this.game,command);

				if (command.checkGive() && result.getValid()) {
					result = carryingValidator.validateGive(game, player.getRoom(), command);
				}
			} else if (command.checkEat()) {
				Consume consume = new Consume();
				result = consume.validateEat(command, game, player);
			} else if (command.checkDrink()) {
				Consume consume = new Consume();
				result = consume.validateDrink(command,game,player);
			}
			
			if (result.getPlayer()==null && !result.getValid()) {
				result = new ActionResult(result.getGame(),player,result.getValid());
			}
		}
		
		return result;
	}
	
	//Either verb or noun doesn't exist
	private boolean eitherExists(ParsedCommand command, Game game) {
		
		boolean validCommand = true;
		
		if (command.getVerbNumber()>Constants.NUMBER_OF_VERBS ||
			command.getNounNumber() == Constants.NUMBER_OF_NOUNS) {
			game.addMessage("You can't "+command.getCommand(), true, true);
			validCommand = false;
		}
		
		return validCommand;
	}
	
	//Neither exists
	private boolean neitherExists(ParsedCommand command,Game game) {
		
		boolean validCommand = true;
		
		if (command.getVerbNumber()>Constants.NUMBER_OF_VERBS && 
				command.getNounNumber() == 52) {
			game.addMessage("What!!",true,true);
			validCommand = false;
		}
		
		return validCommand;
	}
	
	private boolean missingNoun(ParsedCommand command,Game game) {
		
		boolean validCommand = true;
		
		if (command.checkMultipleCommandState() && !command.checkNounLength()) {
			game.addMessage("Most commands need two words", true, true);
			validCommand = false;
		}
		return validCommand;
	}
	
	private boolean checkNone(ParsedCommand command,Game game) {
		
		boolean validCommand = true;
		
		if (command.checkNoneCommandType()) {
			validCommand = false;
			game.addMessage("I don't understand", true, true);
		}
		return validCommand;
	}
	
	private boolean checkNoun(ParsedCommand command) {
		boolean validCommand = true;
		if (command.checkMultipleCommandState()||
			(command.checkMoveState() && command.getSplitTwoCommand()[0].equals("go"))) {
			if(command.getSplitFullCommand().length==1) {
				game.addMessage("Most commands need two words",true,true);
				validCommand = false;
			}
		}
		return validCommand;
	}
	
	private boolean isTrapdoorClosed(ParsedCommand command) {
		boolean trapdoorClosed = false;
		if (command.getCodedCommand().equals(GameEntities.CODE_CLOSED_TRAPDOOR)
			&& !command.checkOpen()) {
			trapdoorClosed = true;
		} 
		return trapdoorClosed;
	}
	
	private ActionResult closedTrapdoor(Player player) {
		game.addMessage("The trapdoor is closed", true, true);
		return new ActionResult(game,player,false);
	}
}

/* 28 April 2025 - Created File
 * 2 May 2025 - Added validation
 * 3 May 2025 - Added command length validation
 * 4 May 2025 - Added player object to validator
 * 5 May 2025 - Updated validator to return ActionResult
 * 12 May 2025 - Added call to item take validator
 * 16 May 2025 - added call to item drop validator
 * 17 May 2025 - Added specific validator for give
 * 19 May 2025 - Added specific validator for reciever of a give
 * 28 May 2025 - Added validation for carrying food/drink
 * 23 June 2025 - Added check to place game into result object
 * 2 July 2025 - Fixed give validator to reject items not carrying
 * 5 July 2025 - Added validation for multi-word commands
 * 20 July 2025 - Added logging info for command.
 * 				- Added Code to set command to swimming for special commands
 * 21 July 2025 - Added script to respond correctly if trapdoor closed
 */
