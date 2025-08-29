/*
Title: Island of Secrets Command Validator
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.15
Date: 29 August 2025
Source: https://archive.org/details/island-of-secrets_202303

Move the take validators et al to Executor class
Move the Trapdoor to the Move command
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

	private static final Logger logger = Logger.getLogger(Game.class.getName());
	
	//Validations - an invalid command does not count as an action
	public ActionResult validateCommand(ParsedCommand command, Game game, Player player) {

		boolean validCommand = true;
		
		if (neitherExists(command)) {
			validCommand = false;
			game = handleNeitherExistsFails(game);
		} else if (eitherExists(command)) {
			validCommand = false;
			game = handleEitherExistsFails(game,command);
		} else if (missingNoun(command)) {
			validCommand = false;
			game = handleMissingNounFails(game);
		} else if (checkNone(command)) {
			validCommand = false;
			game = handleCheckNoneFails(game);
		} else if (checkNoun(command)) {
			validCommand = false;
			game = handleCheckNounFails(game);
		}
		
		ActionResult result = new ActionResult(game,player,validCommand);
		logger.info("Command Valid: "+validCommand+" Code: "+command.getCodedCommand());
		
		//Special command specfic validations
		if (validCommand) {
			if (command.getCodedCommand().equals(GameEntities.CODE_DOWN_TRAPDOOR) ||
				command.getCodedCommand().equals(GameEntities.CODE_ENTER_TRAPDOOR)) {
				if (game.getItem(GameEntities.ITEM_TRAPDOOR).getItemFlag()==1) {
					result = closedTrapdoor(player,game);
				} else {
					command.updateState(GameEntities.CMD_SWIM);
				}
			} else if (isTrapdoorClosed(command)) {
				result = closedTrapdoor(player,game);
			} else if(command.checkMoveState()) {
				Move moveValidator = new Move();
				result = moveValidator.validateMove(command,game,player.getRoom());
			} else if(command.checkTake()) {
				ItemCommands takeValidator = new ItemCommands();
				result = takeValidator.validateTake(game, player.getRoom(), command);
			} else if (command.checkDrop() || command.checkGive()) {
				ItemCommands carryingValidator = new ItemCommands();
				result = carryingValidator.validateCarrying(game,command);

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
	
	private boolean eitherExists(ParsedCommand command) {
		return (command.getVerbNumber()>Constants.NUMBER_OF_VERBS ||
				command.getNounNumber() == Constants.NUMBER_OF_NOUNS);
	}
	
	private boolean neitherExists(ParsedCommand command) {
		return (command.getVerbNumber()>Constants.NUMBER_OF_VERBS && 
				command.getNounNumber() == Constants.NUMBER_OF_NOUNS);
	}
	
	private boolean missingNoun(ParsedCommand command) {
		return (command.checkMultipleCommandState() && !command.checkNounLength());
	}
	
	private boolean checkNone(ParsedCommand command) {
		return (command.checkNoneCommandType());
	}
	
	private boolean checkNoun(ParsedCommand command) {
		boolean validCommand = false;
		if (command.checkMultipleCommandState()||
			(command.checkMoveState() && command.getSplitTwoCommand()[0].equals("go"))) {
			if(command.getSplitFullCommand().length==1) {
				validCommand = true;
			}
		}
		return validCommand;
	}
	
	private Game handleEitherExistsFails(Game game, ParsedCommand command) {
		game.addMessage("You can't "+command.getCommand(), true, true);
		return game;
	}
	
	private Game handleNeitherExistsFails(Game game) {
		game.addMessage("What!!",true,true);
		return game;
	}
	
	private Game handleMissingNounFails(Game game) {
		game.addMessage("Most commands need two words", true, true);
		return game;
	}

	private Game handleCheckNoneFails(Game game) {
		game.addMessage("I don't understand", true, true);
		return game;
	}

	private Game handleCheckNounFails(Game game) {
		game.addMessage("Most commands need two words",true,true);
		return game;
	}
	
	private boolean isTrapdoorClosed(ParsedCommand command) {
		boolean trapdoorClosed = false;
		if (command.getCodedCommand().equals(GameEntities.CODE_CLOSED_TRAPDOOR)
			&& !command.checkOpen()) {
			trapdoorClosed = true;
		} 
		return trapdoorClosed;
	}
	
	private ActionResult closedTrapdoor(Player player,Game game) {
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
 * 27 August 2025 - Updated valid Command Detector
 * 29 August 2025 - Updated the validators to handle separate concerns and to return modified game object
 */
