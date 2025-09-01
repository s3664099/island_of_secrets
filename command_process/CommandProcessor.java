/*
Title: Island of Secrets Command Processor
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.8
Date: 1 September 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package command_process;

import game.Game;
import game.Player;
import java.util.logging.Logger;

public class CommandProcessor {

	private final CommandParser parser;
	private final CommandValidator validator;
	private final CommandExecutor executor;
	private static final Logger logger = Logger.getLogger(CommandExecutor.class.getName());
	
	public CommandProcessor() {
		this.parser = new CommandParser();
		this.validator = new CommandValidator();
		this.executor = new CommandExecutor();
	}
	
	public ActionResult execute(String rawInput,Game game, Player player) {
		
		ActionResult result = new ActionResult();
		
		try {
			logger.info("Executing command: " + rawInput);
			ParsedCommand command = parser.parse(rawInput, game,player.getRoom());
			logger.info("Parsed command: " + command.getCommand());
			result = validator.validateCommand(command,game,player);
			logger.info("Validation result: " + result.getValid());
		
			if(result.getValid()) {
				result = executor.executeCommand(game,player,command);
			}
		} catch (Exception e) {
			logger.severe("An error occurred while processing the command.");
	        result = new ActionResult(game, player, false);
		}
		return result;
	}
}

/* 23 April 2025 - Create class
 * 27 April 2025 - Moved Swimming code to Swimming. Added swimming state change
 * 28 April 2025 - Started building the command processing components
 * 2 May 2025 - Added command validator
 * 4 May 2025 - Updated to move player into validator
 * 18 June 2025 - Tidied up code
 * 30 June 2025 - Removed the give section
 */
