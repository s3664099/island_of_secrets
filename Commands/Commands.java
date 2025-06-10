/*
Title: Island of Secrets Command Execution Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.13
Date: 28 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Game.Game;
import Game.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

public class Commands {
	
	private int verb;
	private int noun;
	private String code;
	private Random rand = new Random();
	private String command;
	private Game game;
	private Player player;
	
	public Commands(int verb,int noun, String code, String command) {
		this.verb = verb;
		this.noun = noun;
		this.code = code;
		this.command = command;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Game getGame() {
		return this.game;
	}
	
	public void move(Game game,Player player,String noun) {




			

			

			

	}
	

	
	public void eat(Game game, Player player,String nounStr) {
		

	}
	
	public void drink(Game game, Player player,String nounStr) {
				

	}
	
	public void ride(Game game) {
		

	}
	
	public void open(Game game,Player player) {
		

	}
	
	public void chip(Game game,Player player) {
		
		player.setStat("strength",(float) player.getStat("strength")-2);
		game.addMessage("Nothing happens",true,true);

		//Carrying Hammer or Axe
		if (game.getItem(9).getItemLocation()==0 || game.getItem(15).getItemLocation()==0) {
			game.addMessage("Ok",true,true);
		}
		
		//Chopping roots with Axe
		if (this.code.equals("3577077") && game.getItem(9).getItemLocation()==0) {
			game.getItem(23).setItemFlag(0);
			game.getItem(23).setItemLocation(player.getRoom());
		}
		
		//Break the column with hammer
		if (this.code.equals("1258158") || this.code.equals("2758158") && 
			game.getItem(15).getItemLocation()==0) {
				game.getItem(12).setItemFlag(0);
				game.getItem(27).setItemFlag(0);
				game.addMessage("Crack",true,true);
		}
		
		//Break the staff
		if (this.code.substring(0,4).equals("1100") && player.getRoom()==10) {
			player.setStat("wisdom",(int) player.getStat("wisdom")-10);
			game.getItem(noun).setItemLocation(81);
			game.getItem(noun).setItemFlag(-1);
			game.setMessageGameState();
			game.addPanelMessage("It shatters releasing a rainbow of colours!", true);
			
			if (game.getItem(2).getItemLocation() == player.getRoom()) {
								game.addPanelMessage("The egg hatches into a baby dactyl which takes", false);
				game.addPanelMessage("Omegan in its claws and flies away", false);

				game.getItem(39).setItemLocation(81);
				game.getItem(2).setItemLocation(2);
				game.getItem(2).setItemFlag(-1);
				player.setStat("strength",(float) player.getStat("strength")+40);
			}
		
		//Response if player uses the staff without meeting the conditions above
		} else if (this.code.substring(0,4).equals("1100") && verb == 19) {
			game.getItem(noun).setItemLocation(81);
			game.getItem(noun).setItemFlag(-1);
			game.setMessageGameState();
			game.addPanelMessage("It shatters releasing a rainbow of colours!", true);
		}
		
		//Tap a person (and the still for some odd reason)
		if (this.verb==18 && (this.noun>29 && this.noun<34) || 
			(this.noun>38 && this.noun<44) || this.noun==16) {
			
			//Carrying the axe?
			if (game.getItem(9).getItemLocation()<1) {
				kill(player,game);
			} else {
				game.addMessage("You annoy the "+game.getItem(noun).getItemName(),true,true);
			}
		} 
	}
	
	public void kill(Player player, Game game) {
		
		//Take a hit even if the object isn't present
		player.setStat("strength",(float) player.getStat("strength")-12);
		player.setStat("wisdom",(int) player.getStat("wisdom")-10);
		game.addMessage("That would be unwise",true,true);
				
		//Is object present - ends game
		if (game.getItem(noun).getItemLocation() == player.getRoom()) {
			game.getItem(Constants.NUMBER_OF_ITEMS).setItemFlag(1);
			
			game.setMessageGameState();
			game.addPanelMessage("Thunder splits the sky!",true);
			game.addPanelMessage("It is the triumphant voice of Omegan.",false);
			game.addPanelMessage("Well done Alphan!",false);
			game.addPanelMessage("The means becomes the end.",false);
			game.addPanelMessage("I claim you as my own!",false);
			game.addPanelMessage("Ha Ha Hah!",false);

			player.setStat("strength",(float) 0);
			player.setStat("wisdom",0);
			player.setStat("timeRemaining",0);

			game.setEndGameState();	
		}
	}
	
	public void attack(Game game, Player player) {
		
		game.addMessage("That would be unwise",true,true);
		
		player.setStat("strength",(float) player.getStat("strength")-2);
		player.setStat("wisdom",(int) player.getStat("wisdom")-2);
		
		if (game.getItem(noun).getItemLocation() == player.getRoom() || 
			game.getItem(noun).getItemLocation() ==0) {
			
			//Omegan the evil one
			if (noun==39) {
				game.addMessage("He laughs dangerously.",true,true);
			
			//Swampman
			} else if (noun==32) {
				game.addMessage("The swampman is unmoved.",true,true);
			
			//Sage of the Lilies
			} else if (noun==33) {
				game.addMessage("You can't touch her",true,true);
				game.getItem(3).setItemLocation(81);
			
			//Logmen
			} else if (noun==41) {
				game.addMessage("They think that's funny!",true,true);

			//In the Dactyl's Nest
			} else if (player.getRoom()==46) {
				
				game.setMessageGameState();
				game.addPanelMessage("You anger the bird!",true);
				game.addPanelMessage("Which flies you to a remote place", false);

				player.setRoom(rand.nextInt(6)+63);
				game.getItem(16).setItemLocation(1);
				game.getItem(16).setItemFlag(0);
				game.addMessage("",true,true);
			
			//Strike Flint
			} else if (code.substring(0,4).equals("1400")) {

				game.addMessage("Sparks fly",true,true);
				
				//Coal in room
				if (player.getRoom()==game.getItem(13).getItemLocation()) {
					
					game.getItem(noun).setItemFlag(-1);
					game.getItem(13).setItemLocation(81);
					game.setMessageGameState();
					
					//Omegan's present in his sanctum
					if (player.getRoom()==game.getItem(38).getItemLocation() && player.getRoom()==10) {
						
						game.addPanelMessage("The coal burns with a red flame",true);
						game.addPanelMessage("Which dissolves Omegan's Cloak", false);						

						player.setStat("wisdom",(int) player.getStat("wisdom")+20);
						game.getItem(13).setItemFlag(-1);
						game.getItem(38).setItemLocation(81);
					} else {
						game.addPanelMessage("The coal burns with a red flame",true);				
					}
				}
			}
			player.setStat("strength",(float) player.getStat("strength")-8);
			player.setStat("wisdom",(int) player.getStat("wisdom")-5);
		}		
	}
	
	//Works
	public void swim(Player player,Game game) {


	}
	
	public int shelter(Player player,Game game, String[] commands) {
		
		int location = -1;
		
		if (game.getItem(36).getItemFlag()<0) {
			
			if (commands.length>1) {
				
				String shelterLocation = commands[1];
				game.addMessage("",true,true);
				
				if (commands.length>2 && commands[1].equals("in")) {
					shelterLocation = commands[2];
				}
				
				if (shelterLocation.equals("shack")) {
					location = 44;
				} else if (shelterLocation.equals("cave")) {
					location = 11;
				} else if (shelterLocation.equals("cabin")) {
					location = 41;
				} else {
					game.addMessage("I'm sorry, I do not know that place",true,true);
				}
				
			} else {
				game.addMessage("You can shelter in:",true,true);
				game.setShelterGameState();
			}
		} else {
			game.addMessage("Not possible at the moment.",true,true);
		}
		
		return location;
	}
	
	public void help(Player player, Game game) {
		

	}
	
	public void polish(Player player, Game game,String noun) {
		

	}
	
	public void examine(Player player, Game game, String[] command) {

	}
	
	public void fill(Game game,Player player) {
		

	}
	
	public void say(Game game, String noun,Player player) {

	}
	

	
	public void wave(Game game,Player player) {
		

	}
		
	public void save(Game game, Player player) throws IOException {
		
		boolean writeFile = false;
		String[] commands = command.split(" ");
		
		if (commands.length==1) {
			game.addMessage("Please include the name of your game.",true,true);
		} else {
				
			File saveGameDirectory = new File("savegames");
				
			//Checks to see if the directory exists. If it doesn't it creates the directory
			if(!saveGameDirectory.exists()) {
				saveGameDirectory.mkdir();
			}
				
			File saveFile = new File(saveGameDirectory+"/"+commands[1]+".sav");
				
			//Checks to see if the file exists
			if (saveFile.exists() && (commands.length<3 || !commands[2].equals("o"))) {
					
				//If file exists tells user how to overwrite it
				game.addMessage("File already exists. Please add 'o' to the end to overwrite.",true,true);
				writeFile = false;
			
			} else {
				writeFile = true;
			}
		
			//Writes file	
			if (writeFile) {
			
				try {
					FileOutputStream file = new FileOutputStream(saveGameDirectory+"/"+commands[1]+".sav");
					ObjectOutputStream out = new ObjectOutputStream(file);
					out.writeObject(game);
					out.writeObject(player);
					out.close();
					file.close();
					game.addMessage("Save successful",true,true);

				} catch (IOException e) {
			        throw new IOException("Game Failed to save " + e.toString());
				}
			} else {
				game.addMessage("Game not saved",true,true);
			}
		}
	}
	
	public boolean load(Game game, Player player) throws IOException {
		
		//Prevent saves from having more than one word (Same with save)
		boolean loadFile = false;
		String[] commands = command.split(" ");
		
		if (commands.length==1) {
			displayGames(game);
		} else {
		
			//Checks to see if the file exists
			File saveGameDirectory = new File("savegames");				
			File saveFile = new File(saveGameDirectory+"/"+commands[1]+".sav");		
			
			//If not available
			if (!saveFile.exists()) {			
				game.addMessage("Sorry, the saved game does not exist. Type 'games' to list games.",true,true);
			} else {
				loadFile = true;
			}
		
			this.game = game;
			this.player = player;
				
			if (loadFile) {
		
				//Attempts to load the file
				try {
					FileInputStream file = new FileInputStream(saveGameDirectory+"/"+commands[1]+".sav");
					ObjectInputStream fileIn = new ObjectInputStream(file);
				
					//Load successful. Update the objects
					this.game = (Game) fileIn.readObject();
					this.player = (Player) fileIn.readObject();
				
					fileIn.close();
					file.close();
					this.game.addMessage("Game successfully loaded",true,true);
					game.resetCount();
							
					//Location failed to load
				} catch (IOException|ClassNotFoundException e) {
			        throw new IOException("Game Failed to save " + e.toString());
				}
			}
		}
		
		return loadFile;
	}
	
	public void displayGames(Game game) {
		
		//Checks to see if the file exists
		File saveGameDirectory = new File("savegames");
		
		//Retrieves the saved games
		File[] savFiles = saveGameDirectory.listFiles( new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".sav");
			};
		});
		
		//Sets variables to list set number of game names		
		String gameMessage = "Games Saves";
		String[] gameDisplayed = game.getDisplayedSavedGames();
		int noGames=savFiles.length;
		int gameStart = 0;
		int totalDisplayed = 4;
		int maxDisplay = 4;
		
		game.setUpperLimitSavedGames(false);
		game.setLowerLimitSavedGames(false);
		
		if (noGames==0) {
			game.addMessage("There are no saved games to display",true,true);
		} else {

			//Check with number of games and determine which games are displayed
			if (noGames>5) {
				gameStart = game.getCount()*maxDisplay;
				if (noGames-gameStart>maxDisplay) {
					totalDisplayed = gameStart+maxDisplay;
					game.setUpperLimitSavedGames(true);
				} else {
					totalDisplayed += noGames-gameStart;
				}
			} else {
				totalDisplayed  = noGames;
			}
				
			//Display the games selected
			for (int i = gameStart; i<totalDisplayed;i++ ) {
				gameDisplayed[i-gameStart] = savFiles[i].getName();
			}
						
			if (gameStart>0) {
				game.setLowerLimitSavedGames(true);
			}
		
			//game.setGameDisplay(true);
			game.setDisplayedGames(gameDisplayed);
			game.addMessage(gameMessage,true,true);
			game.setSavedGameState();
		}
	}
	
	public void quit(Player player, Game game) {
		
		game.addMessage("You relinquish your quest",true,true);
		game.getItem(Constants.NUMBER_OF_NOUNS).setItemFlag(-1);
		player.setStat("timeRemaining",1);
		game.setEndGameState();
	}
}

/* 13 November 2024 - Created File. Added code to move player
 * 14 November 2024 - Added code to handle special movement commands
 * 17 November 2024 - completed the movement method
 * 					- Started working on the take method
 * 19 November 2024 - Added the code to increase food & drink
 * 30 November 2024 - Added drop command
 * 1 December 2024 - Added eat & drink functionality
 * 3 December 2024 - Started on the break functionality
 * 4 December 2024 - Completed the break method
 * 7 December 2024 - Completed Kill & Swim method. Updated move for poisonous waters subgame
 * 					 Completed the panel message of go boat.
 * 					 Completed shelter,help,scratch,rub,polish,fill
 * 8 December 2024 - Completed say, wait, wave and info
 * 9 December 2024 - Added save & load method. Added getters to retrieve saved details
 * 10 December 2024 - Added quit method
 * 15 December 2024 - Added end game flag
 * 18 December 2024 - Added game name with save command. Added overwrite handling for save game.
 * 20 December 2024 - Started working on the display saved game function
 * 21 December 2024 - The display game function works where there are more than 8 games
 * 22 December 2024 - Change the give command so that a new screen isn't required
 * 23 December 2024 - Updated to version 2.
 * 25 December 2024 - Moved the code that takes player to poisoned waters if trapdoor is open
 * 27 December 2024 - Fixed problem with rocks preventing movement
 * 29 December 2024 - Added further notes for movement allowability
 * 30 December 2024 - Fixed the go boat section so it now works (and ends the game if applicable).
 * 4 January 2025 - Changed so that can only eat food, not drink.
 * 5 January 2025 - tested ride command, and added further responses if the instruction is not strict,
 * 6 January 2025 - Changed the flags for the items in the chest since the book might not be used.
 * 				  - Added error response if unable to open something (ie not trapdoor/chest).
 * 7 January 2025 - Added responses in the break section for when no other responses occur.
 * 11 January 2025 - Added a set end-game for when player attempts to kill something. Added message for when swimming.
 * 13 January 2025 - Added extra responses to attack and made coal disappear instead of flint.
 * 14 January 2025 - Changed room allocation when swimming so not swimming in poisoned waters for too long (as per game).
 * 15 January 2025 - Fixed the shelter options to appear nicer
 * 19 January 2025 - Updated the polish/rub command to make more sense
 * 22 January 2025 - Fixed problem with display not displaying on rest
 * 25 January 2025 - Fixed problem with the comma appearing at beginning of items in inventory.
 * 					 Stylised 
 * 26 January 2025 - Made the count for rest absolute value
 * 28 January 2025 - Reset the best flag if it is no longer in your possession
 * 29 January 2025 - Moved the flag for the coal to where it is only triggered when the cloak is present
 * 30 January 2025 - Added code to pick apples for food.
 * 31 January 2025 - Completed Testing and increased version
 * 					 Added code to display response when torch already taken.
 * 1 February 2025 - Added code to change description of torch when waved, and also when dropped.
 * 				   - Added responses of the arms based on whether the torch is bright or not.
 * 				   - Updated Grandpa's shack to reveal items without needing the book
 * 				   - Added check to make sure wisdom increase for taking items only occurs once
 * 4 February 2025 - Updated the fill command
 * 5 February 2025 - Added code to flag visited when move into room.
 * 8 February 2025 - Added description for when you take the egg
 * 11 February 2025 - Added entry using specific names
 * 17 February 2025 - Added examine column
 * 18 February 2025 - Added look room functions
 * 20 February 2025 - Added further room descriptions
 * 21 February 2025 - Added comment when examining marble column.
 * 					- Started working on the abode hut
 * 22 February 2025 - Finished the diary and the map in the hut
 * 					- Added eat food, and response to entering castle of secrets
 * 23 February 2025 - Made possible for give & shelter to work with single command
 * 24 February 2025 - Removed shelter options in replace for buttons
 * 25 February 2025 - Started working on displaying the saved games as buttons
 * 28 February 2025 - Removed Stack Trace from Load & Save
 * 3 March 2025 - Added section to remove cloak when destroyed
 * 5 March 2025 - Increased to v4.0
 * 9 March 2025 - Refactored constant
 * 10 March 2025 - Updated the setWisdom method by passing boolean
 * 11 March 2025 - Updated code for timeRemaining getter after moving into HashMap for stats\
 * 12 March 2025 - Updated Time Remaining Stats to hashmap. Updated wisdom, strength & weight for hashmap.
 * 14 March 2025 - Updated food and drink stats
 * 17 March 2025 - Changed setMessage to addMessage
 * 20 March 2025 - Started updating code to handle message builder in game class
 * 22 March 2025 - Added cast to strength to fix error with killing people
 * 23 March 2025 - Merged addMessage and addNormalMessage
 * 26 March 2025 - Commented out code to enable to run
 * 11 April 2025 - Updated code to display saved games
 * 23 April 2025 - Fixed info command. Update response to Enums
 * 28 April 2025 - Updated for setting the state of the game for messages and others
 * 				 - Updated code for setting the target room when swimming for move into
 * 				   poisoned waters
 */