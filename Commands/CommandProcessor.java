/*
Title: Island of Secrets Command Processor
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 2 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.io.IOException;

import Data.Constants;
import Data.Item;
import Game.Game;
import Game.Player;
import Model.Swimming;

public class CommandProcessor {

	private final CommandParser parser;
	private final CommandValidator validator;
	private final CommandExecutor executor;
	private ActionResult result;
	
	public CommandProcessor() {
		this.parser = new CommandParser();
		this.validator = new CommandValidator();
		this.executor = new CommandExecutor();
		this.result = new ActionResult();
	}
	
	public ActionResult execute(String rawInput,Game game, Player player) {
		
		ParsedCommand command = parser.parse(rawInput, game,player.getRoom());
		boolean validCommand = validator.validateCommand(command,game);
		
		if(validCommand) {
			result = executor.executeCommand(game,player,command);
		}

		//determinePanel(game);
						
		return result;
	}
	
	public void executeGive(String object) {
		//Checks if the response is 'to xxxx'
		String[] instructions = object.split(" ");
		if (instructions[0].equals("to") && instructions.length==2) {
			object = instructions[1];
		}
		
		//Is the response correct for a give command?
		if (object.split(" ").length==1) {
			CommandExecutor processCommands = new CommandExecutor();
			processCommands.executeGive(this.game,this.player,this.nounNum,object,this.codedCommand);
		} else {
			this.game.addMessage("I'm sorry, I don't understand.",true,true);
		}
		
		this.game.setGiveState();
	}
	
}

/* 23 April 2025 - Create class
 * 27 April 2025 - Moved Swimming code to Swimming. Added swimming state change
 * 28 April 2025 - Started building the command processing components
 * 2 May 2025 - Added command validator
 */
