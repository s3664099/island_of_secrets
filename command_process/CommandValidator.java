/*
Title: Island of Secrets Command Validator
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
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

/**
 * Validates parsed player commands and ensures they are executable within the game rules. 
 * 
 * <p>This class checks for invalid or incomplete commands, provides feedback to the player,
 * and delegates valid commands to the appropriate handlers (e.g., movement, item actions, consumption). 
 * Special handling is included for game-specific entities such as trapdoors.</p>
 */
public class CommandValidator {

	private static final Logger logger = Logger.getLogger(Game.class.getName());
	
    /**
     * Validates a parsed command against the current game and player state.
     *
     * @param command the parsed player command
     * @param game the current game instance
     * @param player the player issuing the command
     * @return an {@link ActionResult} representing the outcome of the validation
     */
	public ActionResult validateCommand(ParsedCommand command, Game game, Player player) {

		boolean validCommand = true;
		
		if (checkVerbAndNounInvalid(command)) {
			logger.info("Validation Failed - Verb & Noun Invalid");
			validCommand = false;
			game = handleCheckVerbAndNounInvalidFails(game);
		} else if (checkVerbOrNounInvalid(command)) {
			logger.info("Validation Failed - Verb or Noun Invalid");
			validCommand = false;
			game = handleVerbOrNounInvalidFails(game,command);
		} else if (checkMissingNoun(command)) {
			logger.info("Validation Failed - Noun Missing");
			validCommand = false;
			game = handleMissingNounFails(game);
		} else if (checkNone(command)) {
			logger.info("Validation Failed - Missing Noun Failed");
			validCommand = false;
			game = handleCheckNoneFails(game);
		} else if (isNounMissing(command)) {
			logger.info("Validation Failed - Is Noun Missing True");
			validCommand = false;
			game = handleCheckNounFails(game);
		}
		
		ActionResult result = new ActionResult(game,player,validCommand);
		logger.info("Command Valid: "+validCommand+" Code: "+command.getCodedCommand());
		
		if (validCommand) {
			result = specialCommandValidator(command,result);
		} else {
			result = specialNounValidator(command,result);
		}
		
		if (checkResultNull(result)) {
			result = new ActionResult(result.getGame(),player,result.isValid());
		}
		
		return result;
	}
	
    /**
     * Applies specialized command checks for movement, item interactions, 
     * and unique entities like trapdoors.
     *
     * @param command the parsed player command
     * @param result the current validation result
     * @return the updated {@link ActionResult} after applying special rules
     */
	private ActionResult specialCommandValidator(ParsedCommand command,ActionResult result) {
		
		Game game = result.getGame();
		Player player = result.getPlayer();
		
		if (isTrapdoorClosed(command,game)) {
			result = handleTrapdoorClosed(player,game);
		} else if (isTrapdoorOpen(command,game)) {
			command = handleGoTrapdoorOpen(command);
		} else if (checkTrapdoorClosed(command)) {
			result = handleTrapdoorClosed(player,game);
		} else if(checkMoveState(command)) {
			result = validateMoveCommand(command,game,player);
		} else if(checkTakeState(command)) {
			result = validateTakeCommand(command,game,player);
		} else if (checkDropOrGive(command)) {
			result = validateDropOrGive(command,game,player);
		} else if (checkEat(command)) {
			result = validateEat(command,game,player);
		} else if (checkDrink(command)) {
			result = validateDrink(command,game,player);
		} 		
		return result;
	}
	
	/**
     * Applies specialized command checks for nouns that are not listed as the main nouns, 
     *
     * @param command the parsed player command
     * @param result the current validation result
     * @return the updated {@link ActionResult} after applying special rules
     */
	private ActionResult specialNounValidator(ParsedCommand command,ActionResult result) {
		Game game = result.getGame();
		Player player = result.getPlayer();
		
		if (checkExamineNoun(command)) {
			logger.info("Special Examine command");
			result = validateExamineNoun(game,player);
		} else if (checkDrinkWine(command)) {
			logger.info("Special Drink Wine");
			result = validateDrinkWine(command,game,player);
		} else if (checkGiveNoun(command)) {
			logger.info("Special Give Food Drink");
			result = new ItemCommands().validateGive(game, player, command);
		}
		
		return result;
	}
	
    // ===== Command checks =====

    /**
     * @return true if the command contains an invalid verb or noun.
     */
	private boolean checkVerbOrNounInvalid(ParsedCommand command) {
		return ((command.getVerbNumber()>Constants.NUMBER_OF_VERBS ||
				command.getVerbNumber() != GameEntities.CMD_SAY &&
				command.getVerbNumber() != GameEntities.CMD_SHELTER &&
				(command.getNounNumber() == Constants.NUMBER_OF_NOUNS)
				&& command.getVerbNumber() != GameEntities.CMD_SAVE 
				&& command.getVerbNumber() != GameEntities.CMD_LOAD
				&& !command.getSplitTwoCommand()[1].equals(GameEntities.NOUN_STONE)));
	}
	
    /**
     * @return true if both the verb and noun are invalid.
     */
	private boolean checkVerbAndNounInvalid(ParsedCommand command) {

		return (command.getVerbNumber()>Constants.NUMBER_OF_VERBS && 
				command.getNounNumber() == Constants.NUMBER_OF_NOUNS);
	}
	
    /**
     * @return true if a noun is required but missing.
     */
	private boolean checkMissingNoun(ParsedCommand command) {
		return (command.checkMultipleCommandState() && !command.checkNounLength());
	}
	
    /**
     * @return true if the command is unrecognized.
     */
	private boolean checkNone(ParsedCommand command) {
		return (command.checkNoneCommandType());
	}
	
    /**
     * @return true if the noun is missing in a multi-word or movement command.
     */
	private boolean isNounMissing(ParsedCommand command) {
		boolean validCommand = false;
		if (command.checkMultipleCommandState()||
			(command.checkMoveState() && command.getSplitTwoCommand()[0].equals("go"))) {
			if(command.getSplitFullCommand().length==1) {
				validCommand = true;
			}
		}
		return validCommand;
	}
	
    /**
     * @return true if the trapdoor command is issued and the trapdoor is closed.
     */
	private boolean isTrapdoorClosed(ParsedCommand command,Game game) {
		return (command.getCodedCommand().equals(GameEntities.CODE_DOWN_TRAPDOOR) ||
				command.getCodedCommand().equals(GameEntities.CODE_ENTER_TRAPDOOR) &&
				(game.getItem(GameEntities.ITEM_TRAPDOOR).getItemFlag()==1));
	}
	
    /**
     * @return true if the trapdoor command is issued and the trapdoor is open.
     */
	private boolean isTrapdoorOpen(ParsedCommand command, Game game) {
		return (command.getCodedCommand().equals(GameEntities.CODE_DOWN_TRAPDOOR) ||
				command.getCodedCommand().equals(GameEntities.CODE_ENTER_TRAPDOOR) &&
				(game.getItem(GameEntities.ITEM_TRAPDOOR).getItemFlag()!=1));		
	}
	
    /**
     * @return true if the result is invalid and lacks a player reference.
     */
	private boolean checkResultNull(ActionResult result) {
		return result.getPlayer()==null && !result.isValid();
	}
	
    /**
     * @return true if the command targets a closed trapdoor without an open action.
     */
	private boolean checkTrapdoorClosed(ParsedCommand command) {
		return command.getCodedCommand().equals(GameEntities.CODE_CLOSED_TRAPDOOR) && !command.checkOpen();
	}
	
    // ===== State checks =====
	
	private boolean checkMoveState(ParsedCommand command) {
		return command.checkMoveState();
	}
	
	private boolean checkTakeState(ParsedCommand command) {
		return command.checkTake();
	}
	
	private boolean checkDropOrGive(ParsedCommand command) {
		return command.checkDrop() || command.checkGive();
	}
	
	private boolean checkGive(ParsedCommand command,ActionResult result) {
		return command.checkGive() && result.isValid();
	}
	
	private boolean checkEat(ParsedCommand command) {
		return command.checkEat();
	}
	
	private boolean checkDrink(ParsedCommand command) {
		return command.checkDrink();
	}
	
	private boolean checkDrinkWine(ParsedCommand command) {
		return command.checkDrink() && command.checkNounWine();
	}
	
	private boolean checkExamineNoun(ParsedCommand command) {
		return command.checkExamine() && (command.checkNounTable() ||
										  command.checkNounRoom() ||
										  command.checkNounMap() ||
										  command.checkNounPapers() ||
										  command.checkNounDiary());
	}
	
	private boolean checkGiveNoun(ParsedCommand command) {
		return command.checkGive() && (command.checkNounFood() ||
										  command.checkNounDrink());
	}
	
    // ===== Error handling =====

	/** Adds a "You can't do that" message when verb/noun invalid. */
	private Game handleVerbOrNounInvalidFails(Game game, ParsedCommand command) {
		game.addMessage("You can't "+command.getCommand(), true, true);
		return game;
	}

    /** Adds a generic "What!!" message for both verb and noun invalid. */
	private Game handleCheckVerbAndNounInvalidFails(Game game) {
		game.addMessage("What!!",true,true);
		return game;
	}
	
    /** Adds a missing noun message. */
	private Game handleMissingNounFails(Game game) {
		game.addMessage("Most commands need two words", true, true);
		return game;
	}

	/** Adds an "I don't understand" message. */
	private Game handleCheckNoneFails(Game game) {
		game.addMessage("I don't understand", true, true);
		return game;
	}

    /** Adds a two-word requirement message. */
	private Game handleCheckNounFails(Game game) {
		game.addMessage("Most commands need two words",true,true);
		return game;
	}
	
    // ===== Trapdoor handling =====

    /** Returns an invalid result with a trapdoor closed message. */
	private ActionResult handleTrapdoorClosed(Player player,Game game) {
		game.addMessage("The trapdoor is closed", true, true);
		return new ActionResult(game,player,false);
	}
	
    /** Updates command state when trapdoor is open. */
	private ParsedCommand handleGoTrapdoorOpen(ParsedCommand command) {
		command.updateState(GameEntities.CMD_SWIM);
		return command;
	}
	
	   // ===== Validators for specific actions =====
	
	private ActionResult validateMoveCommand(ParsedCommand command, Game game, Player player) {
		Move moveValidator = new Move();
		return moveValidator.validateMove(command,game,player);
	}
	
	private ActionResult validateTakeCommand(ParsedCommand command, Game game, Player player) {
		ItemCommands takeValidator = new ItemCommands();
		return takeValidator.validateTake(game, player, command);
	}
	
	private ActionResult validateDropOrGive(ParsedCommand command, Game game, Player player) {
		ItemCommands carryingValidator = new ItemCommands();
		ActionResult result = carryingValidator.validateCarrying(game,player,command);
		if (checkGive(command,result)) {
			result = validateGive(command,game,player,carryingValidator);
		}
		return result;
	}
	
	private ActionResult validateGive(ParsedCommand command, Game game, Player player,ItemCommands carryingValidator) {
		return carryingValidator.validateGive(game, player, command);
	}
	
	private ActionResult validateEat(ParsedCommand command, Game game, Player player) {
		return new Consume(command).validateEat(game, player);
	}
	
	private ActionResult validateDrink(ParsedCommand command, Game game, Player player) {
		return new Consume(command).validateDrink(game,player);
	}
	
	private ActionResult validateDrinkWine(ParsedCommand command, Game game, Player player) {
		return new Consume(command).validateDrinkWine(game,player);
	}
	
	private ActionResult validateExamineNoun(Game game, Player player) {
		return new ActionResult(game,player,true);
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
 * 30 August 2025 - Moved special validators to new validator method, and also created check for null in result
 * 31 August 2025 - Completed special validation
 * 3 September 2025 - Changed parameters for the move function
 * 5 September 2025 - Updated based on changes to Consume
 * 28 September 2025 - Updated validator to allow for load/save games
 * 13 October 2025 - Added special command for examining the table
 * 2 November 2025 - Added validation for noun room
 * 5 November 2025 - Added check to confirm drinking wine
 * 8 November 2025 - Added validation for giving food & drink
 * 17 November 2025 - Fixed problem where stone not validating
 * 23 November 2025 - Added check to exclude say from invalidating noun or verb
 * 29 November 2025 - Removed shelter from the noun/verb check
 * 3 December 2025 - Increased version number
 */
