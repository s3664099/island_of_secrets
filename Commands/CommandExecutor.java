/*
Title: Island of Secrets Command Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.15
Date: 11 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.io.IOException;
import java.util.Random;

import Data.Constants;
import Data.Item;
import Data.RawData;
import Game.Game;
import Game.Player;

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
		} else if (command.checkKill()) {
			Combat kill = new Combat(game,player,command);
			
		
		

		
		

		//Load
		} else if (verbNo==40) {
			
			boolean loadGame = this.command.load(game, player);
			
			if (loadGame) {
				this.game = command.getGame();
				this.player = command.getPlayer();
				loadedGame = true;
			}
			
		//Save
		} else if (verbNo==41) {
			this.command.save(game, player);
		

		
		//Quit
		} else if (verbNo==42) {
			this.command.quit(player,game);
		

		



			
		//More than one verb
		} else if(commandLength>1) {
			

				


				
			
				
			
			
			

			
			



			



			}
		}
		
		postUpdates(game,player);
		
		return result;
	}
	
	public void postUpdates(Game game, Player player) {
		
		//Orchards
		if (player.getRoom()==61) {
			player.setStat("wisdom",(int) player.getStat("wisdom")+rand.nextInt(2)+1);
		}
		
		//Thicket of biting bushes
		if (player.getRoom()==14 && rand.nextInt(3)==1) {
			player.setStat("strength",(float) player.getStat("strength")-1);
			game.addMessage("You are bitten.",false,true);
		}
			
		//Adjusting flag of living storm
		if (game.getItem(36).getItemFlag()<1 && game.getItem(22).getItemFlag() != -player.getRoom()) {
			game.getItem(36).setItemFlag(game.getItem(36).getItemFlag()+1);
			game.getItem(36).setItemLocation(player.getRoom());
			player.setStat("strength",(float) player.getStat("strength")-1);
		}

		int stormRand = rand.nextInt(3);

		//Does the living storm appear
		if ((int) player.getStat("timeRemaining")<900 && player.getRoom()==23 && 
			game.getItem(36).getItemFlag()>0 && stormRand ==2) {
			game.getItem(36).setItemFlag(-(rand.nextInt(4)+6));
			game.addMessage(" A storm breaks overhead!",false,true);
		}
		
		//Location of the wild canyon beast
		if (!game.getItem(16).isAtLocation(player.getRoom()) && 
			game.getItem(16).getItemLocation()>0) {
			game.getItem(16).setItemLocation(rand.nextInt(4)+1);
		}
		
		//Location of Omegan
		if (!game.getItem(39).isAtLocation(player.getRoom())) {
			int part1 = 10 * (rand.nextInt(5)+1);
			int part2 = 7 * (rand.nextInt(3)+1);
			int newLocation = Math.min(part1+part2, 80);
			game.getItem(39).setItemLocation(newLocation);
		
		//Is Omegan with player?
		} else if (game.getItem(39).isAtLocation(player.getRoom()) &&
				   !game.getItem(43).isAtLocation(player.getRoom()) &&
				   game.getItem(13).getItemFlag()>-1) {
			player.setStat("strength",(float) player.getStat("strength")-2);
			player.setStat("wisdom",(int) player.getStat("wisdom")-2);
		}
			
		//Swampman's Position if not with player
		if (!game.getItem(32).isAtLocation(player.getRoom()) && game.isRunningState()) {
			game.getItem(32).setItemLocation(76+rand.nextInt(2));
		}
		
		//Swampman with the player?
		if (game.getItem(32).isAtLocation(player.getRoom()) && rand.nextInt(2)==1 &&
			game.getItem(32).getItemFlag()==0) {
			
			game.getItem(32).setItemFlag(-1);
			
			game.addPanelMessage("The swampman tells his tale",true);
			game.addPanelMessage("Median can disable the equipment",false);
			
			if (game.getItem(8).isAtLocation(0)) {
				game.addPanelMessage("and asks you for the pebble you carry.",false);
			}
			game.setMessageGameState();
		}
		
		//Does the boatman appear?
		if ((player.getRoom()==33 || player.getRoom()==57 || player.getRoom()==73) &&
			rand.nextInt(2)==1) {
			game.getItem(25).setItemLocation(player.getRoom());
		}
		
		//Check if pushed into well
		if (player.getRoom()==19 && ((float) player.getStat("strength"))<70 && 
			game.getItem(43).getItemFlag()==0 && rand.nextInt(4)==1) {
			game.addMessage("Pushed into the pit",false,true);
			game.getItem(Constants.NUMBER_OF_NOUNS).setItemFlag(1);
		}
		
		//Movement of the logmen if player not present
		if (!game.getItem(41).isAtLocation(player.getRoom())) {
			game.getItem(41).setItemLocation(21+((rand.nextInt(3)+1)*10)+(rand.nextInt(2)+1));
		} else {
			game.getItem(41).setItemFlag(game.getItem(41).getItemFlag()-1);
			
			//Upset the logmen
			if (game.getItem(41).getItemFlag()<-4) {
				
				String message = "The Logmen decide to have a little fun and";
				String messageTwo = "tie you up in a storeroom";
				game.getItem(41).setItemFlag(0);
				player.setStat("strength",(float) player.getStat("strength")-4);
				player.setStat("wisdom",(int) player.getStat("wisdom")-4);
				
				//Player located determines where end up
				if (player.getRoom()<34) {
					messageTwo = "throw you in the water";
					player.setRoom(32);
				} else {
					player.setRoom(51);
				}
				
				game.setMessageGameState();
				game.addPanelMessage(message, true);
				game.addPanelMessage(messageTwo, false);
				
				//Do you lose items
				for (int i=3;i<5;i++) {
					if (game.getItem(i).getItemLocation()==0) {
						game.getItem(i).setItemLocation(42);
					}
				}
			}
		}
		
		//Move Median to player location is condition correct
		if (game.getItem(43).getItemFlag()==0) {
			game.getItem(43).setItemLocation(player.getRoom());
		}
		
		//Replays notice re: Median
		if (game.getItem(43).getItemLocation()<18 && player.getRoom() != 9 && 
			player.getRoom() != 10 && game.getItem(49).getItemFlag()<1) {
			
			String messageOne = "Median can disable the equipment";
			game.setMessageGameState();
			game.addPanelMessage(messageOne,true);
		}
		
		//Player in the clone vat room
		if (player.getRoom()==18) {
			player.setStat("strength",(float) player.getStat("strength")-1);
			game.addMessage("The gas leaking from the vats burns your lungs!",false,true);
		}
				
		//Too weak to carry something
		float str = (float) player.getStat("strength");
		int weight = (int) player.getStat("weight");
		if ((str-weight)<50) {
			int object = rand.nextInt(9)+1;
			
			if (game.getItem(object).isAtLocation(0)) {
				game.getItem(object).setItemLocation(player.getRoom());
				game.addMessage(" You drop something.",false,false);
			}
		}
				
		//Near the clashing stones
		if (player.getRoom()==47 && game.getItem(8).getItemFlag()>0) {
			game.addMessage(" You can go no further",false,false);
		}
		
		//Involving staff, pebble & coal - seems like a win condition
		if (game.getItem(8).getItemFlag()+game.getItem(11).getItemFlag()+game.getItem(13).getItemFlag()==-3) {
			
			//The flags of the above must total -3
			String messageOne = "The world lives with new hope!";
			game.setMessageGameState();
			game.addPanelMessage(messageOne, false);
			game.addMessage("Your quest is over!",true,true);
			game.setEndGameState();
		}
		
		//Fail Quest conditions
		if ((int) player.getStat("timeRemaining")<0 || ((float) player.getStat("strength"))<0 || game.getItem(Constants.NUMBER_OF_NOUNS).getItemFlag()==1) {
			game.addMessage( "You have failed, the evil one succeeds.",true,true);
			game.setEndGameState();
		}
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
 */