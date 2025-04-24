/*
Title: Island of Secrets Command Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.8
Date: 23 March 2025
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

public class CommandProcess {
	
	private String[] splitCommand = {"",""};
	private String[] commands;
	private String originalCommand;
	private int verbNo;
	private String codedCommand;
	private Commands command;
	private Game game;
	private Player player;
	private boolean loadedGame = false;
	private Random rand = new Random();
	
	public CommandProcess(String command,Game game) {
		
		command = command.toLowerCase();
		command = fixCommand(command);
		commands = command.split(" ");
		splitCommand[0] = commands[0];
		this.originalCommand = command;
		
		if (commands.length>1) {
			
			if (splitCommand[0].equals("give")) {
				splitCommand[1] = commands[1];
			} else {
				splitCommand[1] = command.substring(commands[0].length()).trim();
			}
		} else {
			game.addMessage("Most commands need two words",true,true);
		}
	}
	
	public CommandProcess() {}
	
	public Game getGame() {
		return this.game;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public boolean checkLoadedGame() {
		return loadedGame;
	}
	
	private String fixCommand(String command) {
		
		if (command.equals("u") || command.equals("up")) {
			command = "go up";
		} else if (command.equals("d") || command.equals("down")) {
			command = "go down";
		} else if (command.equals("i") || command.equals("enter") ||
				command.equals("inside") || command.equals("go inside")) {
			command = "go in";
		} else if (command.equals("o") || command.equals("exit") ||				
				command.equals("outside") || command.equals("go outside")) {
			command = "go out";
		} else if (command.equals("north")) {
			command = "n";
		} else if (command.equals("south")) {
			command = "s";
		} else if (command.equals("east")) {
			command = "e";
		} else if (command.equals("west")) {
			command = "w";
		}
				
		return command;	
	}
	
	public int getVerbNumber() {
		
		int verbNumber = Constants.NUMBER_OF_VERBS+1;
		int verbCount = 0;
		
		for (String command:RawData.getVerbs()) {
			verbCount ++;
			
			if (splitCommand[0].toLowerCase().equals(command)) {
				verbNumber = verbCount;
				this.verbNo = verbCount;
			}
		}
						
		return verbNumber;
	}
	
	public int getNounNumber() {
		
		int nounNumber = Constants.NUMBER_OF_NOUNS;
				
		//Only called if more than two words
		if (commands.length>1) {
			nounNumber = getNounNum(splitCommand[1].toLowerCase());
		} else {
			nounNumber = -1;
		}
		
		return nounNumber;
	}
	
	private int getNounNum(String noun) {
		
		int nounCount = 0;
		int nounNumber = Constants.NUMBER_OF_NOUNS;
		
		for (String command:RawData.getNouns()) {
			nounCount ++;
							
			if (noun.equals(command)) {
				nounNumber = nounCount;
			}
		}
				
		return nounNumber;
	}
	
	public String codeCommand(int room, int nounNumber, Item item) {
		
		String codedNoun = String.format("%d%d%d%d",nounNumber,Math.abs(item.getItemLocation()),
										 Math.abs(item.getItemFlag()),room);
		codedNoun = String.valueOf(Integer.parseInt(codedNoun.trim()));
		this.codedCommand = codedNoun;
				
		return codedNoun;
	}
	
	//Executes the command
	public void executeCommand(Game game,Player player,int nounNumber) throws IOException {
		
		this.command = new Commands(verbNo,nounNumber,codedCommand,originalCommand);
		int commandLength = this.commands.length;
		
		//Sets look to examine
		if (this.commands[0].equals("look") && commandLength==1) {
			verbNo = 33;
			this.splitCommand[0] = "examine";
			this.splitCommand[1] = "room";
			commandLength = 2;
		} else if (this.commands[0].equals("look")) {
			this.splitCommand[0] = "examine";
			verbNo = 33;
		}
		
		//Movement Command (verb only)
		if ((verbNo>0 && verbNo<5)) {
			this.command.move(game,player,splitCommand[1]);//More than one verb
		
		//Eat
		} else if (verbNo==11) {
			this.command.eat(game,player,splitCommand[1]);
		
		//Drink
		} else if (verbNo==12) {
			this.command.drink(game,player,splitCommand[1]);
		
		//Info
		} else if (verbNo==39) {
			this.command.info(game, player);
		
		//Wave
		} else if (verbNo==38) {
			this.command.wave(game, player);
		
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
		
		//Wait/Rest
		} else if (verbNo==36 || verbNo==37) {
			this.command.rest(game, player, false);
		
		//Quit
		} else if (verbNo==42) {
			this.command.quit(player,game);
		
		//Swim
		} else if (verbNo==25) {
			this.command.swim(player, game);
		

		//Shelter
		} else if (verbNo==26) {
			int location = this.command.shelter(player, game, commands);
			
			if (location != -1) {
				executeShelter(game,player,location);
			}
			
		//More than one verb
		} else if(commandLength>1) {
			
			//Go
			if (verbNo==5) {
				this.command.move(game,player,splitCommand[1]);

			//Take Command (pick & catch included)
			} else if (verbNo == 6 || verbNo == 7 || verbNo == 15 || verbNo == 29) {
				this.command.take(game,player);
			
			//Give
			} else if (verbNo == 8) {
				String object = this.command.give(game, player,this.commands);
				
				if (object.length()>0) {
					executeGive(game,player,nounNumber,object,codedCommand);
				}

			//Drop
			} else if (verbNo == 9||verbNo ==10) {
				this.command.drop(game,player);
				
			//Ride
			} else if (verbNo==13) {
				this.command.ride(game);
			
			//Open
			} else if (verbNo==14) {
				this.command.open(game, player);
			
			//Break
			} else if (verbNo>15 && verbNo<20) {
				this.command.chip(game, player);
			
			//Attack
			} else if (verbNo>19 && verbNo<24) {
				this.command.attack(game, player);
			
			//Kill
			} else if (verbNo==24) {
				this.command.kill(player, game);
			
			//Help & Scratch
			} else if (verbNo==27||verbNo==28) {
				this.command.help(player, game);

			//Rub & Polish
			} else if (verbNo==30||verbNo==31) {
				this.command.polish(player, game,splitCommand[1]);

			//Read & Examine
			} else if (verbNo==32||verbNo==33) {
				this.command.examine(player,game,splitCommand);
			
			//Fill
			} else if (verbNo==34) {
				this.command.fill(game,player);

			//Say
			} else if (verbNo==35) {
				this.command.say(game, splitCommand[1], player);
			}
		}
		
		postUpdates(game,player);
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
			player.setPanelFlag(3);
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
				
				player.setPanelFlag(3);
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
			player.setPanelFlag(3);
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
			player.setPanelFlag(3);
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
	
	public void executeGive(Game game,Player player,int nounNumber, String subject,
							String codedNoun) {
				
		int objNumber = getNounNum(subject);
		boolean alreadyMessage = false;
		
		if (subject.length()==0) {
			String itemName = game.getItem(objNumber).getItemName();
			game.addMessage("Please enter who you will be giving the "+itemName+" to.",true,true);
		} else if (player.getRoom() != game.getItem(objNumber).getItemLocation()) {
			game.addMessage("The "+subject+" is not here.",true,true);
		} else {
			
			game.addMessage("It is refused.",true,true);
			
			//Removes the snake from the hut by giving it an apple
			if (codedNoun.equals("10045") && objNumber==40) {
				game.getItem(nounNumber).setItemLocation(81);
				game.getItem(objNumber).setItemFlag(1);
				game.addMessage("The snake uncoils",true,true);
				
			//Giving water to a villager (but must have some drink)
			} else if (codedNoun.equals("2413075") && objNumber==30 && ((int) player.getStat("drink"))>1) {

				if (game.getItem(11).getItemFlag() != 0) {
					game.addMessage("He drinks the water and offers his staff",true,true);
					game.getItem(30).setItemName("A villager");
				} else {
					game.addMessage("He drinks the water",true,true);
				}
				
				game.getItem(11).setItemFlag(0);
				player.setStat("drink",((int) player.getStat("drink"))-1);
			} else {
				
				//Giving items to the ancient scavenger
				if ((codedNoun.substring(0,3).equals("300") || 
					 codedNoun.substring(0,3).equals("120")) &&
					 objNumber == 42) {
					player.setStat("wisdom",(int) player.getStat("wisdom")+10);
					game.getItem(nounNumber).setItemLocation(81);
				
				//Give jug to swampman
				} else if (codedNoun.substring(0,2).equals("40") && 
						   game.getItem(4).getItemFlag()<0 && objNumber == 32) {
					game.getItem(objNumber).setItemFlag(1);
					game.getItem(nounNumber).setItemLocation(81);
					game.addMessage("The Swampman takes the jug and leaves",true,true);
					alreadyMessage = true;
				
				//Give pebble to Median
				} else if (codedNoun.substring(0,2).equals("80") &&
						   objNumber == 43) {
					game.getItem(nounNumber).setItemLocation(81);
					player.setPanelFlag(3);
					game.getItem(8).setItemFlag(-1);

					//Removes Median from Game
					game.getItem(43).setItemLocation(81);
					game.getItem(43).setItemFlag(1);
					
					game.addPanelMessage("He takes it ...", true);
					if (player.getRoom()!=8) {
						game.addPanelMessage("runs down the corridor, ...", false);
					} 
					game.addPanelMessage("and casts it into the chemical vats, ", false);
					game.addPanelMessage("purifying them with a clear blue light reaching far into the lakes and rivers", false);
					game.addPanelMessage("reaching far into the lakes and rivers beyond.", false);
				}
				
				//Successfully given
				if (game.getItem(nounNumber).getItemLocation() == 81 && !alreadyMessage) {
					game.addMessage("It is accepted",true,true);
				}
				
				//Giving to logmen
				if (objNumber == 41) {
					game.addMessage("It is taken",true,true);
					game.getItem(nounNumber).setItemLocation(51);
				}
			}
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
 */