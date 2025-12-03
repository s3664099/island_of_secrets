/*
Title: Island of Secrets Command Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package command_process;

import java.util.Random;
import java.util.logging.Logger;

import commands.Combat;
import commands.Consume;
import commands.Examine;
import commands.ItemCommands;
import commands.Miscellaneous;
import commands.Move;
import data.GameEntities;
import game.Game;
import game.Player;
import game.PostCommand;
import persistence.Persistence;

/**
 * The {@code CommandExecutor} is responsible for carrying out parsed and validated
 * commands issued by the player. It interprets the {@link ParsedCommand} type and
 * dispatches execution to the appropriate command handler (movement, item handling,
 * combat, persistence, etc.). It acts as the final stage of the command-processing
 * pipeline after parsing and validation.
 */
public class CommandExecutor {
	
	private Random rand = new Random();
	private static final Logger logger = Logger.getLogger(CommandExecutor.class.getName());
	
	/**
	 * Executes a player command within the context of the current {@link Game} state.
	 * <p>
	 * This method determines the category of the {@link ParsedCommand}, then delegates
	 * execution to the relevant command class such as {@link Move}, {@link ItemCommands},
	 * {@link Combat}, {@link Miscellaneous}, or {@link Persistence}.
	 * <p>
	 * Special cases, such as moving through a trapdoor, are also handled here.
	 *
	 * @param game    the current game instance containing rooms, items, and global state
	 * @param player  the player issuing the command
	 * @param command the parsed player command to be executed
	 * @return an {@link ActionResult} describing the outcome of the command execution,
	 *         including updated game state and feedback messages
	 */
	public ActionResult executeCommand(Game game,Player player,ParsedCommand command) {
		
		ActionResult result = new ActionResult(game,player,false);
		
		if (command.checkMoveState()) {
			logger.info("Moving");
			
			if (command.getCodedCommand().equals(GameEntities.CODE_DOWN_STOREROOM) && 
				game.getItem(GameEntities.ITEM_TRAPDOOR).getItemFlag()==0) {
				player.setRoom(rand.nextInt(5)+1);
				result = result.success(game, player);
			
			}	else {
				result = new Move().executeMove(game,player,command);
			}
			
		
		} else if (command.checkTake() || command.checkDrop() || command.checkGive()) {
			logger.info("Take/Drop/Give");
			result = new ItemCommands().executeCommand(game,player,command);
		} else if (command.checkEat() || command.checkDrink() || command.checkRest()) {
			logger.info("Eat/Drink/Rest");
			result = new Consume(command).executeCommand(game,player);
		} else if (command.checkInfo()) {
			logger.info("Info");
			result = new Miscellaneous(game,player).info();
		} else if (command.checkWave()) {
			logger.info("Wave");
			result = new Miscellaneous(game,player,command).wave();
		} else if (command.checkHelp()) {
			logger.info("Help");
			result = new Miscellaneous(game,player,command).help();
		} else if (command.checkPolish()) {
			logger.info("Polish");
			result = new Miscellaneous(game,player,command).polish();
		} else if (command.checkSay()) {
			logger.info("Say");
			result = new Miscellaneous(game,player,command).speak();
		} else if (command.checkExamine()) {
			logger.info("Examine");
			result = new Examine(game,player,command).examine();
		} else if (command.checkFill()) {
			logger.info("Fill");
			result = new Miscellaneous(game,player,command).fill();
		} else if (command.checkRide()) {
			logger.info("Ride");
			result = new Miscellaneous(game,player,command).ride();
		} else if (command.checkOpen()) {
			logger.info("Open");
			result = new Miscellaneous(game,player,command).open();
		} else if (command.checkSwim()) {
			logger.info("Swim");
			result = new Miscellaneous(game,player,command).swim();
		} else if (command.checkShelter()) {
			result = new Miscellaneous(game,player,command).shelter();
		} else if (command.checkChop()) {
			logger.info("Chop");
			result = new Combat(game,player,command).chop();
		} else if (command.checkAttack()) {
			logger.info("Attack");
			result = new Combat(game,player,command).attack();
		} else if (command.checkKill()) {
			logger.info("Kill");
			result = new Combat(game,player,command).kill();
		} else if (command.checkSave()) {
			logger.info("Save");
			result = new Persistence(game,player,command).save();
		} else if (command.checkLoad()) {
			logger.info("Load");
			Persistence load = new Persistence(game,player,command);
			result = load.load();
		} else if (command.checkQuit()) {
			logger.info("Quit");
			result = new Persistence(game,player,command).quit();
		} else if (command.checkRestart()) {
			logger.info("Restart");
			result = new Persistence(game,player,command).restart();
		}
		PostCommand updates = new PostCommand(result);
		return updates.postUpdates();
	}
	
	/**
	 * Executes a special-case "shelter" action, moving the player into a safe location
	 * and updating game state accordingly.
	 * <p>
	 * This method is typically invoked during storm events or scripted sequences.
	 *
	 * @param game     the current game instance
	 * @param player   the player seeking shelter
	 * @param location the identifier of the shelter room/location
	 */
	public void executeShelter(Game game, Player player, int location) {
		
		player.setRoom(location);
		game.getItem(22).setItemFlag(-location);
		game.addMessage("You reach shelter.",true,true);
		game.setMessageGameState();
		game.addPanelMessage("You blindly run through the storm",true);
	}
}

/* 9 November 2024 - Created method
 * 10 November 2024 - Added the verb count method
 * 11 November 2024 - Added the noun count method
 * 					- Got the command splitting working and sending correct errors
 * 					- Added method to process the coded command.
 * 12 November 2024 - Completed the codeCommand method
 * 13 November 2024 - Stored the variables 
 * 14 November 2024 - Added more options for movement
 * 17 November 2024 - Added call to take method
 * 29 November 2024 - moved script to get noun value to separate script.
 * 					- Fixed problem with only verb command not displaying properly
 * 30 November 2024 - Continued building the give functionality
 * 1 December 2024 - Added Eat Functionality. Added Drink Functionality. Changed Median
 * 					 panel to four.
 * 2 December 2024 - Moved drink command to single command and added call to method
 * 3 December 2024 - Added break command
 * 7 December 2024 - Added kill,swim,shelter, examine, fill and others
 * 8 December 2024 - Added say, rest, wave
 * 9 December 2024 - Added save & load, also getter to retrieve loaded game details.
 * 10 December 2024 - Added Quit Method
 * 11 December 2024 - Continued working on post-command processing
 * 12 December 2024 - Continued with the post-command processing
 * 14 December 2024 - Continued with the post-command processing
 * 15 December 2024 - Finished the post-command processing with end game conditions
 *					  Added the logmen response
 * 16 December 2024 - Added code to handle the swimming in poisoned waters section
 * 19 December 2024 - Added command to display list of saved games
 * 20 December 2024 - Added the display games command
 * 23 December 2024 - Added process shelter method
 * 					- Updated to version 2.
 * 30 December 2024 - Added lose game test in case of specific event.
 * 2 January 2025 - Set flag to 0 if it is less than 0 to prevent NumberFormatException.
 * 				  - Skips the swampman move if the player is giving it an item
 * 3 January 2025 - Got the issue with the panel not displaying with a give.
 * 4 January 2025 - added an abs method call for the flag as well. Changed the hardcoded noun numbers to constant.
 * 13 January 2025 - Made sure a 0 isn't selected when selecting a random item to drop
 * 19 January 2025 - Directed rub & polish to correct method.
 * 26 January 2025 - Moved the living storm post command ifs together.
 * 27 January 2025 - Fixed problem with boatman not moving
 * 				   - Fixed the logmen movement to be correct. Added the message that is displayed when they have fun
 * 29 January 2025 - Changed the message for the game finish to display everything after dealing with Omegan
 * 31 January 2025 - Completed Testing and increased version
 * 1 February 2025 - Added extra parameter to the examine function
 * 3 February 2025 - Added description for damage in clone vat room.
 * 4 February 2025 - Updated scavenger and the fill command
 * 8 February 2025 - Updated villager taking water
 * 11 February 2025 - Added string paramater to pass the noun into movement
 * 17 February 2025 - Added code to transform look command to enable looking at room.
 * 20 February 2025 - Fixed Omegan movement
 * 23 February 2025 - Added multi word command so can use give & shelter with one commands
 * 25 February 2025 - Removed display games function call
 * 26 February 2025 - Removed the reset for the counts for load game display
 * 28 February 2025 - Removed Median after giving him the stone
 * 3 March 2025 - Added code to include weight in calculation for dropping items.
 * 5 March 2025 - Increased to v4.0
 * 11 March 2025 - Updated getter for timeRemaining after moving into HashMap for stats
 * 12 March 2025 - Updated wisdom, strength & weight for use with hash map
 * 14 March 2025 - Updated eat & Drink
 * 17 March 2025 - Changed setMessage to addMessage
 * 20 March 2025 - Started updating code with Message builder class
 * 21 March 2025 - Finished updating messages with Message Builder class
 * 22 March 2025 - Fixed up final issue with messages
 * 23 March 2025 - Combined addMessage and addNormalMessage
 * 8 May 2025 - Added ActionResult as return for execution. Added move command
 * 31 May 2025 - Added info and wave commands
 * 1 June 2025 - Added Help & polish commands
 * 2 June 2025 - Added speak commands
 * 8 June 2025 - Added Fill command
 * 9 June 2025 - Added ride & open commands
 * 10 June 2025 - Added swim & shelter commands
 * 11 June 2025 - Added combat commands
 * 13 June 2025 - Added attack and kill commands
 * 17 June 2025 - Added Load Game command
 * 18 June 2025 - Added quit command and Tidied up.
 * 23 June 2025 - Tightened code
 * 24 June 2025 - Moved creation of action result to main code
 * 25 June 2025 - Added logging to the actions to flag what actions are occuring
 * 1 September 2025 - Removed Magic Numbers
 * 2 September 2025 - Updated based on new ActionResult
 * 4 September 2025 - Updated based on changes to Consume
 * 6 November 2025 - Added restart game command
 * 3 December 2025 - Increased version number
 */