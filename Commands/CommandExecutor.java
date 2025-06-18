/*
Title: Island of Secrets Command Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.18
Date: 18 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.util.Random;
import Data.Constants;
import Game.Game;
import Game.Player;
import Model.PostCommand;

public class CommandExecutor {
	
	private ActionResult result = new ActionResult();
	private Random rand = new Random();
	
	//Executes the command
	public ActionResult executeCommand(Game game,Player player,ParsedCommand command) {
		
		if (command.checkMoveState()) {
			
			//Poisoned waters
			if (command.getCodedCommand().equals("490051") && game.getItem(29).getItemFlag()==0) {
				player.setRoom(rand.nextInt(5)+1);
				player.setPlayerStateStartSwimming();
				result = new ActionResult(game,player);
			
			//Normal Move
			}	else {
				Move move = new Move();
				result = move.executeMove(game,player,command);
			}
		} else if (command.checkTake() || command.checkDrop() || command.checkGive()) {	
			ItemCommands item = new ItemCommands();
			result = item.executeCommand(game,player,command);
		} else if (command.checkEat() || command.checkDrink() || command.checkRest()) {
			Consume consume = new Consume();
			result = consume.executeCommand(game,player,command);
		} else if (command.checkInfo()) {
			Miscellaneous misc = new Miscellaneous(game,player);
			result = misc.info();
		} else if (command.checkWave()) {
			Miscellaneous misc = new Miscellaneous(game,player,command);
			result = misc.wave();
		} else if (command.checkHelp()) {
			Miscellaneous misc = new Miscellaneous(game,player,command);
			result = misc.help();
		} else if (command.checkPolish()) {
			Miscellaneous misc = new Miscellaneous(game,player,command);
			result = misc.polish();
		} else if (command.checkSay()) {
			Miscellaneous misc = new Miscellaneous(game,player,command);
			result = misc.speak();
		} else if (command.checkExamine()) {
			Examine examine = new Examine(game,player,command);
			result = examine.examine();
		} else if (command.checkFill()) {
			Miscellaneous fill = new Miscellaneous(game,player,command);
			result = fill.fill();
		} else if (command.checkRide()) {
			Miscellaneous ride = new Miscellaneous(game,player,command);
			result = ride.ride();
		} else if (command.checkOpen()) {
			Miscellaneous open = new Miscellaneous(game,player,command);
			result = open.open();
		} else if (command.checkSwim()) {
			Miscellaneous swim = new Miscellaneous(game,player,command);
			result = swim.swim();
		} else if (command.checkShelter()) {
			Miscellaneous shelter = new Miscellaneous(game,player,command);
			result = shelter.shelter();
			/*
			 * 			int location = this.command.shelter(player, game, commands);
						if (location != -1) {
							executeShelter(game,player,location);
						}
			 */
		} else if (command.checkChop()) {
			Combat chop = new Combat(game,player,command);
			result = chop.chop();
		} else if (command.checkAttack()) {
			Combat attack = new Combat(game,player,command);
			result = attack.attack();
		} else if (command.checkKill()) {
			Combat kill = new Combat(game,player,command);
			result = kill.kill();
		} else if (command.checkSave()) {
			Persistence save = new Persistence(game,player,command);
			result = save.save();
		} else if (command.checkLoad()) {
			Persistence load = new Persistence(game,player,command);
			result = load.load();
		} else if (command.checkQuit()) {
			Persistence quit = new Persistence(game,player,command);
			result = quit.quit();
		}
		
		PostCommand updates = new PostCommand(result);
		
		return updates.postUpdates();
	}
		
	public void executeShelter(Game game, Player player, int location) {
		
		player.setRoom(location);
		game.getItem(22).setItemFlag(-location);
		game.addMessage("You reach shelter.",true,true);
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
 */