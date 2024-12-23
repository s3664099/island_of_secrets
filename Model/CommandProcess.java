/*
Title: Island of Secrets Command Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 2.0
Date: 23 December 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.util.Random;

import Data.Constants;
import Data.Item;
import Data.RawData;
import View.GamePanel;

public class CommandProcess {
	
	private String[] splitCommand = {"",""};
	private String[] commands;
	private String originalCommand;
	private int verbNo;
	private int nounNo;
	private String codedCommand;
	private Commands command;
	private int nounNumber;
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
			splitCommand[1] = command.substring(commands[0].length()).trim();
		} else {
			game.setMessage("Most commands need two words");
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
		
		int verbNumber = Constants.noVerbs+1;
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
		
		int nounNumber = 52;
				
		//Only called if more than two words
		if (commands.length>1) {
			nounNumber = getNounNum(splitCommand[1].toLowerCase());
			this.nounNo = nounNumber;
		} else {
			nounNumber = -1;
		}
		
		return nounNumber;
	}
	
	private int getNounNum(String noun) {
		
		int nounCount = 0;
		int nounNumber = 52;
		
		for (String command:RawData.getNouns()) {
			nounCount ++;
							
			if (noun.equals(command)) {
				nounNumber = nounCount;
			}
		}
				
		return nounNumber;
	}
	
	public String codeCommand(int room, int nounNumber, Item item) {
			
		String codedNoun = String.format("%d%d%d%d",nounNumber,Math.abs(item.getLocation()),
										 item.getFlag(),room);
		codedNoun = String.valueOf(Integer.parseInt(codedNoun.trim()));
		this.codedCommand = codedNoun;
				
		return codedNoun;
	}
	
	//Executes the command
	public void executeCommand(Game game,Player player,int nounNumber) {
		
		nounNo = nounNumber;
		this.command = new Commands(verbNo,nounNumber,codedCommand,originalCommand);
		
		if (verbNo != 43) {
			game.resetCount();
		}
		
		//Movement Command (verb only)
		if ((verbNo>0 && verbNo<5)) {
			this.command.move(game,player);//More than one verb
		
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
		
		//Display list of saved games
		} else if (verbNo==43) {
			this.command.displayGames(game);
			
		//More than one verb
		} else if(commands.length>1) {
			
			//Go
			if (verbNo==5) {
				this.command.move(game,player);

			//Take Command (pick & catch included)
			} else if (verbNo == 6 || verbNo == 7 || verbNo == 15 || verbNo == 29) {
				this.command.take(game,player);
			
			//Give
			} else if (verbNo == 8) {
				this.command.give(game, player);

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
			
			//Swim
			} else if (verbNo==25) {
				this.command.swim(player, game);
			
			//Shelter
			} else if (verbNo==26) {
				this.command.shelter(player, game);

			//Help & Scratch
			} else if (verbNo==27||verbNo==28) {
				this.command.help(player, game);

			//Rub & Polish
			} else if (verbNo==30||verbNo==31) {
				this.command.help(player, game);

			//Read & Examine
			} else if (verbNo==32||verbNo==33) {
				this.command.examine(game);
			
			//Fill
			} else if (verbNo==34) {
				this.command.fill(game);

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
			player.adjustWisdom(rand.nextInt(2)+1);
		}
		
		//Thicket of biting bushes
		if (player.getRoom()==14 && rand.nextInt(3)==1) {
			player.adjustStrength(-1);
			game.addMessage("You are bitten.");
		}
		
		//Adjusting flag of living storm
		if (game.getItem(36).getFlag()<1 && game.getItem(22).getFlag() != -player.getRoom()) {
			game.getItem(36).setFlag(game.getItem(36).getFlag()+1);
			game.getItem(36).setLocation(player.getRoom());
			player.adjustStrength(-1);
		}
		
		//Location of the wild canyon beast
		if (!game.getItem(16).checkLocation(player.getRoom()) && 
			game.getItem(16).getLocation()>0) {
			game.getItem(16).setLocation(rand.nextInt(4)+1);
		}
		
		//Location of Omegan
		if (!game.getItem(39).checkLocation(player.getRoom())) {
			int part1 = 10 * rand.nextInt(5)+2;
			int part2 = 7 *rand.nextInt(3)+1;
			int newLocation = Math.min(part1+part2, 80);
			game.getItem(39).setLocation(newLocation);
		
		//Is Omegan with player?
		} else if (game.getItem(39).checkLocation(player.getRoom()) &&
				   !game.getItem(43).checkLocation(player.getRoom()) &&
				   game.getItem(13).getFlag()>-1) {
			player.adjustStrength(-2);
			player.adjustWisdom(-2);
		}
		
		//Swampman's Position
		if (player.getRoom()<78) {
			game.getItem(32).setLocation(76+rand.nextInt(2));
		}
		
		//Does the boatman appear?
		if ((player.getRoom()==33 || player.getRoom()==57 || player.getRoom()==73) &&
			game.getItem(2).getFlag()==1) {
			game.getItem(25).setLocation(player.getRoom());
		}
		
		//Swampman with the player?
		if (game.getItem(32).checkLocation(player.getRoom()) && rand.nextInt(2)==1 &&
			game.getItem(32).getFlag()==0) {
			
			game.getItem(32).setFlag(-1);
			String swampMan = "The swampman tells his tale";
			String message = "Median can disable the equipment";
			int loop = 2;
			
			if (game.getItem(8).checkLocation(0)) {
				message+="|and asks you for the pebble you carry.";
				loop++;
			}
			player.setPanelFlag(3);
			game.setPanelMessages(swampMan, message, loop);
		}
		
		//Check if pushed into well - not sure who
		if (player.getRoom()==19 && player.getStrength()<70 && 
			game.getItem(43).getFlag()==0 && rand.nextInt(4)==1) {
			game.setMessage("Pushed into the pit");
			game.getItem(Constants.noNouns).setFlag(1);
		}
		
		//Movement of the logmen if player not present
		if (!game.getItem(41).checkLocation(player.getRoom())) {
			game.getItem(41).setLocation(21+(rand.nextInt(3)*10)+rand.nextInt(2));
		} else {
			game.getItem(41).setFlag(game.getItem(41).getFlag()-1);
			
			//Upset the logmen
			if (game.getItem(41).getFlag()<-4) {
				
				String message = "The Logmen decide to have a little fun and";
				String messageTwo = "tie you up in a storeroom";
				game.getItem(41).setFlag(0);
				player.adjustStrength(-4);
				player.adjustWisdom(-4);
				
				//Player located determines where end up
				if (player.getRoom()<34) {
					messageTwo = "throw you in the water";
					player.setRoom(32);
				} else {
					player.setRoom(51);
				}
				
				//Do you lose items
				for (int i=3;i<5;i++) {
					if (game.getItem(i).getLocation()==0) {
						game.getItem(i).setLocation(42);
					}
				}
			}
		}
		
		//Move Median to player location is condition correct
		if (game.getItem(43).getFlag()==0) {
			game.getItem(43).setLocation(player.getRoom());
		}
		
		//Replays notice re: Median
		if (game.getItem(43).getLocation()<18 && player.getRoom() != 9 && 
			player.getRoom() != 10 && game.getItem(49).getFlag()<1) {
			
			String messageOne = "Median can disable the equipment";
			player.setPanelFlag(3);
			game.setPanelMessages(messageOne,"",1);
		}
		
		//Player in the room of the Storage Casks?
		if (player.getRoom()==18) {
			player.adjustStrength(-1);
		}
		
		//Too weak to carry something
		if (player.getStrength()<50) {
			int object = rand.nextInt(9);
			if (game.getItem(object).checkLocation(0)) {
				game.getItem(object).setLocation(player.getRoom());
				game.addMessage(" You drop something.");
			}
		}
		
		//Does the living storm appear
		if (player.getTime()<900 && player.getRoom()==23 && 
			game.getItem(36).getFlag()>0 && rand.nextInt(3)==3) {
			game.getItem(36).setFlag(-(rand.nextInt(4)+6));
			game.addMessage(" A storm breaks overhead!");
		}
		
		//Near the clashing stones
		if (player.getRoom()==47 && game.getItem(8).getFlag()>0) {
			game.addMessage(" You can go no further");
		}
		
		//Involving staff, pebble & coal - seems like a win condition
		if (game.getItem(8).getFlag()+game.getItem(11).getFlag()+game.getItem(13).getFlag()==-3) {
			game.getItem(Constants.noNouns).setFlag(1);
			
			//The flags of the above must total -3
			String messageOne = "The world lives with new hope!";
			player.setPanelFlag(3);
			game.setPanelMessages(messageOne,"",1);
			game.addMessage("Your quest is over!");
			game.endGame();
		}
		
		//Fail Quest conditions
		if (player.getTime()<0 || player.getStrength()<0) {
			game.addMessage( "You have failed, the evil one succeeds.");
			game.endGame();
		}
	}
	
	public void executeGive(Game game,Player player,int nounNumber, String subject,
							String codedNoun) {
		
		int objNumber = getNounNum(subject);
		
		if (subject.length()==0) {
			String itemName = game.getItem(objNumber).getItem();
			game.setMessage("Please enter who you will be giving the "+itemName+" to.");
		} else if (player.getRoom() != game.getItem(objNumber).getLocation()) {
			game.setMessage("The "+subject+" is not here.");
		} else {
			
			game.setMessage("It is refused.");
			
			//Removes the snake from the hut by giving it an apple
			if (codedNoun.equals("10045") && objNumber==40) {
				game.getItem(nounNumber).setLocation(81);
				game.getItem(objNumber).setFlag(1);
				game.setMessage("The snake uncoils");
			
			//Giving water to a villager (but must have some drink)
			} else if (codedNoun.equals("2413075") && objNumber==30 && player.getDrink()>1) {
				game.getItem(11).setFlag(0);
				game.setMessage("He offers his staff");
				player.adjustDrink(1);
			
			
			} else {
				
				//Give Lilyflower/Marble Chip to scavenger	
				if ((codedNoun.substring(0,3).equals("300") || 
					 codedNoun.substring(0,3).equals("120")) &&
					 objNumber == 42) {
					player.setWisdom(player.getWisdom()+10);
					game.getItem(nounNumber).setLocation(81);
				
				//Give jug to swampman
				} else if (codedNoun.substring(0,2).equals("40") && 
						   game.getItem(4).getFlag()<0 && objNumber == 32) {
					game.getItem(objNumber).setFlag(1);
					game.getItem(nounNumber).setLocation(81);
				
				//Give pebble to Median
				} else if (codedNoun.substring(0,2).equals("80") &&
						   objNumber == 43) {
					game.getItem(nounNumber).setLocation(81);
					player.setPanelFlag(3);
					game.getItem(8).setFlag(-1);
					
					if (player.getRoom()==8) {
						game.setPanelMessages("He takes it ...", 
											  "and casts it into the chemical vats, purifying them with"
											  + "a clear blue light reaching far into the lakes and rivers "
											  + "beyond.", 2);
					} else {
						game.setPanelMessages("He takes it, runs down the corridor, ...", 
											  "and casts it into the chemical vats, purifying them with"
											  + "a clear blue light reaching far into the lakes and rivers "
										      + "beyond.", 2);
					}
				}
				
				//Successfully given
				if (game.getItem(nounNumber).getLocation() == 81) {
					game.setMessage("It is accepted");
				}
				
				//Giving 
				if (objNumber == 41) {
					game.setMessage("It is taken");
					game.getItem(nounNumber).setLocation(51);
				}
			}
		}
	}
	
	public void executeShelter(Game game, Player player, int location) {
		
		player.setRoom(location);
		game.getItem(22).setFlag(-location);
		game.addMessage("You reach shelter.");
		game.setPanelMessages("You blindly run through the storm","",1);

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
 */