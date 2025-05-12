/*
Title: Island of Secrets Command Validator
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.5
Date: 12 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Game.Game;
import Game.Player;

public class CommandValidator {
	
	private boolean validCommand;
	private Game game;
	
	public ActionResult validateCommand(ParsedCommand command, Game game, Player player) {
		
		this.game = game;
		validCommand = eitherExists(command,this.game);
		validCommand = neitherExists(command,this.game);
		validCommand = missingNoun(command,this.game);
		validCommand = checkNone(command,this.game);
		
		ActionResult result = new ActionResult(this.game,validCommand);
		
		if (validCommand) {
			if(command.checkMoveState()) {
				Move moveValidator = new Move();
				result = moveValidator.validateMove(command,this.game,player.getRoom());
			} else if(command.checkTake()) {
				ItemCommands takeValidator = new ItemCommands();
				result = takeValidator.validateTake(this.game, player.getRoom(), command);
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
			game.addMessage("I don't understand", true, true);
		}
		return validCommand;
	}
}

/* 28 April 2025 - Created File
 * 2 May 2025 - Added validation
 * 3 May 2025 - Added command length validation
 * 4 May 2025 - Added player object to validator
 * 5 May 2025 - Updated validator to return ActionResult
 * 12 May 2025 - Added call to item take validator
 */
