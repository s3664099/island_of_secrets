/*
Title: Island of Secrets Command Validator
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 3 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Game.Game;

public class CommandValidator {
	
	boolean validCommand;
	
	public boolean validateCommand(ParsedCommand command, Game game) {
		
		validCommand = eitherExists(command,game);
		validCommand = neitherExists(command,game);
		validCommand = missingNoun(command,game);
		//Add validate move
		
		return validCommand;
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
}

/* 28 April 2025 - Created File
 * 2 May 2025 - Added validation
 * 3 May 2025 - Added command length validation
 */
