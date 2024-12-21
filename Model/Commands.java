/*
Title: Island of Secrets Command Execution Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.15
Date: 21 December 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

//880 IF B$="490051" AND F(29)=0 THEN GOSUB 2110:RETURN - Poisonous Waters Minigame
//Add method to create a Frame that is designed to just display messages

package Model;

import Data.Constants;

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
	
	public void move(Game game,Player player) {
		
		int direction = 0;
		boolean haveMoved=false;
		
		//Verb only
		if (this.noun == 52) {
			direction = this.verb;
		} else if (this.noun>Constants.noItems && this.noun<Constants.noNouns) {
			direction = this.noun-Constants.noItems;
		}
		
		//Sets direction for specific movement command
		if (code.equals("500012") || code.equals("500053") || code.equals("500045")) {
			direction = 4;
		} else if (code.equals("500070")||code.equals("500037")||code.equals("510011")||
				   code.equals("510041") ||code.equals("510043")||code.equals("490066")||
				   code.equals("490051")) {
			direction = 1;
		} else if (code.equals("510060")||code.equals("480056")) {
			direction = 2;
		} else if (code.equals("510044")||code.equals("510052")) {
			direction = 3;
		
		//Poisoned waters
		} else if (code.equals("490051") && game.getItem(29).getFlag()==0) {
			player.setPanelFlag(4);
		}
		
		//Checks if player able to move
		//Prevents Player from leaving is Omegan present
		if (player.getRoom() == game.getItem(39).getLocation() && 
			(player.getStrengthWisdon()<180 || player.getRoom()==10)) {
			game.setMessage("You can't leave!");
		
		//Swampman blocks
		} else if (player.getRoom() == game.getItem(32).getLocation() && 
					game.getItem(32).getFlag()<1 && direction == 3) {
			game.setMessage("He will not let you pass.");
		
		//The Rocks
		} else if (player.getRoom() == 47 && game.getItem(44).getFlag()==1) {
			game.setMessage("The rocks move to prevent you");
		} else if (player.getRoom() == 28 && game.getItem(7).getFlag()!=1) {
			game.setMessage("The arms hold you fast");
		
		//Snake at grandpa's Shack
		} else if (player.getRoom()==45 && game.getItem(40).getFlag()==0 && direction == 4) {
			game.setMessage("Hisss!");
		
		//Looks like need canyon beast to climb the path	
		} else if (player.getRoom() == 25 && game.getItemFlagSum(16) != -1 && direction ==3) {
			game.setMessage("Too steep to climb");
		
		
		} else if (player.getRoom() == 51 && direction == 3) {
			game.setMessage("The door is barred!");
		
		//Can move
		} else {
			
			if (direction>4) {
				direction = 0;
			}
		
			if (direction>0) {
				if (game.checkExit(player.getRoom(),direction-1)) {
					int newRoom = player.getRoom() + Integer.parseInt(
						"-10+10+01-01".substring((direction-1)*3, ((direction-1)*3)+3));
					player.setRoom(newRoom);
					game.setMessage("Ok");
					haveMoved = true;
				}
			}
		
			if (direction<1 || !haveMoved) {
				game.setMessage("You can't go that way");
			}
			
			if (player.getRoom() == 33 && game.getItem(16).getLocation()==0) {
				game.getItem(16).setLocation(rand.nextInt(4)+1);
				game.getItem(16).setFlag(0);
				game.setMessage("The beast runs away");
			}
			
			//Handling the ferry man
			if (player.getRoom()==game.getItem(25).getLocation() && this.noun == 25) {
				
				if (player.getWisdom()<60) {
					game.setPanelMessages("You board the craft ...",
										  "falling under the spell of the boatman|"
										  + "and are taken to the Island of Secrets ...|"
										  + "to serve Omegan forever.",4);
					game.getItem(direction).setFlag(Constants.noNouns-1);
				} else {
					game.setPanelMessages("You board the craft ...",
							  "and are taken to the Island of Secrets.",2);
					player.setRoom(57);
				}
				game.setMessage("The boat skims the dark and silent waters.");
			}
		}
	}
	
	public void take(Game game,Player player) {
				
		//Is the item present, and can it be taken?
		if (((game.getItem(noun).getFlag()>0 && game.getItem(noun).getFlag()<9) ||
			game.getItem(noun).getLocation()!=player.getRoom()) && this.noun<=Constants.carriableItems) {
			game.setMessage("What "+game.getItem(noun).getItem()+"?");
		} else {
			
			//Evil books in library
			if (this.code.equals("3450050")) {
			
				player.setWisdom(player.getWisdom()-5);
				player.setStrength(player.getStrength()-8);
				game.setMessage("They are cursed");
			} else {

				//Omegan's Cloak
				if (this.code.equals("3450050")) {
					
					//Add special lightning Flashes screen
					game.setMessage("Lightning Flashes");
					//1370 FOR K=1 TO 30:GOSUB2770 :PRINT"///LIGHTNING FLASHES!":NEXT K
					
					game.getItem(39).setLocation(player.getRoom());
					player.setWisdom(player.getWisdom()-2);
					player.setStrength(player.getStrength()-8);	
				}
				
				//1st - pick mushrooms or apple
				//2nd - catch canyon beast
				//3rd - noun not an item
				if ((verb == 15 && noun != 20 && noun != 1) || (verb == 29 && noun !=16) ||
					noun > Constants.carriableItems) {
					game.setMessage("You can't "+command);
				} else {
					
					int weight = 0;
										
					//picks up item
					if (game.getItem(noun).getLocation()== player.getRoom() && (
						game.getItem(noun).getFlag()<1 || game.getItem(noun).getFlag()==9)
						&& noun<Constants.carriableItems) {
							game.getItem(noun).setLocation(0);
							weight = -1;
					}
					
					if (noun == 16 && game.getItem(10).getLocation()!=0) {
						game.getItem(noun).setLocation(player.getRoom());
						game.setMessage("It escaped");
						weight = 0;
					}
					
					if (noun>Constants.foodLine && noun<Constants.drinkLine) {
						weight = -1;
						player.adjustFood(2);
						game.getItem(noun).setLocation(-18);
					}
					
					if (noun>=Constants.drinkLine && noun<Constants.carriableItems) {
						weight = -1;
						player.adjustDrink(2);
						game.getItem(noun).setLocation(-18);
					}
					
					if (weight == -1) {
						game.setMessage("Taken");
						player.setWisdom(player.getWisdom()+4);
						player.setWeight(player.getWeight()+1);
						
						if (game.getItem(noun).getFlag()>1) {
							game.getItem(noun).setFlag(0);
						}
					}
					
					//Handles the bird (though the coded noun is odd)
					if (code.equals("246046") && game.getItem(11).getLocation() != 0) {
						game.setMessage("You anger the bird");
						game.getItem(noun).setLocation(player.getRoom());
						
						if (rand.nextInt(3)>2) {
							game.addMessage(" which flies you to a remote place.");
							player.setRoom(63+rand.nextInt(6));
							game.getItem(16).setLocation(1);
						}
					}
				}	
			}
		}
	}

	public void give(Game game,Player player) {
				
		if ((noun != 24 && game.getItem(noun).getLocation()>0) || noun == 52) {
			
			String itemName = game.getItem(noun).getItem();
			
			if (itemName.length()==0) {
				itemName = "that";
			}
			
			game.setMessage("You don't have the "+itemName);
			
		} else {
			player.setPanelFlag(1);			
		}
	}
	
	public void drop(Game game, Player player) {
		
		//Dropping the Earthenware Jug
		if (noun == 4 && game.getItem(noun).getLocation()==0 && verb==9) {
			game.getItem(noun).setLocation(81);
			player.setWisdom(player.getWisdom()-1);
			game.setMessage("It breaks!");
		
		//Dropping other items
		} else {
			if (game.getItem(noun).getLocation()==0 && noun<Constants.foodLine) {
				game.getItem(noun).setLocation(player.getRoom());
				player.setWeight(player.getWeight()-1);
				game.setMessage("Done");
			} else {
				game.setMessage("I can't. Sorry.");
			}
		}
	}
	
	public void eat(Game game, Player player,String nounStr) {

		//Eating lillies (moved here since in original game code wouldn't reach)
		if (noun == 3 && game.getItem(3).getLocation()==0) {
			player.adjustWisdom(-5);
			player.adjustStrength(-2);
			game.setMessage("They make you very ill");
		
		//Item unedible
		} else if ((noun<Constants.foodLine || noun>Constants.carriableItems) 
			&& nounStr.length()>0) {
			game.setMessage("You can't "+command);
			player.setWisdom(player.getWisdom()-1);
		
		//Eat
		} else {
			game.setMessage("You have no food");
			
			if (player.getFood()>0) {
				player.adjustFood(-1);
				player.adjustStrength(10);
				game.setMessage("Ok");
			}
		}
	}
	
	public void drink(Game game, Player player,String nounStr) {
		
		//Drinking green liquid
		if (noun==31) {
			
			if (game.getItemFlagSum(4)!=-1) {
				game.setMessage("You don't have the "+game.getItem(noun).getItem());
			} else {
				game.setMessage("Ouch!");
				player.adjustStrength(-4);
				player.adjustWisdom(-7);
				player.setPanelFlag(3);
				
				int count = rest(game,player,true);
				
				//Sets messages
				game.setPanelMessages("You taste a drop and ...", "Time passes ...", count);
			}
			
		//Item undrinkable
		} else if ((noun<Constants.drinkLine || noun>Constants.carriableItems) 
				&& nounStr.length()>0) {
				game.setMessage("You can't "+command);
				player.setWisdom(player.getWisdom()-1);
		} else {
			
			game.setMessage("You have no drink.");
			if (player.getDrink()>0) {
				player.adjustDrink(-1);
				player.adjustStrength(7);
				game.setMessage("Ok");
			}
		}
	}
	
	public void ride(Game game) {
		
		//Riding the canyon beast
		if (this.code.substring(0,4).equals("1600")) {
			game.getItem(noun).setFlag(-1);
			game.setMessage("It allows you to ride.");
		}
	}
	
	public void open(Game game,Player player) {
		
		//Open chest in grandpa's shack
		if (this.code.equals("2644044")) {
			game.setMessage("The chest opens");
			game.getItem(6).setFlag(9);
			game.getItem(5).setFlag(9);
			game.getItem(15).setFlag(9);
		}
		
		//Open trapdoor in refuse filled room
		if (this.code.equals("2951151")) {
			game.setMessage("The trapdoor creaks");
			game.getItem(29).setFlag(0);
			player.adjustWisdom(3);
		}
	}
	
	public void chip(Game game,Player player) {
		
		player.adjustStrength(-2);

		//Carrying Hammer or Axe
		if (game.getItem(9).getLocation()==0 || game.getItem(15).getLocation()==0) {
			game.setMessage("Ok");
		}
		
		//Chopping roots with Axe
		if (this.code.equals("3577077") && game.getItem(9).getLocation()==0) {
			game.getItem(23).setFlag(0);
			game.getItem(23).setLocation(player.getRoom());
		}
		
		//Break the column with hammer
		if (this.code.equals("1258158") || this.code.equals("2758158") && 
			game.getItem(15).getLocation()==0) {
				game.getItem(12).setFlag(0);
				game.getItem(27).setFlag(0);
				game.setMessage("Crack");
		}
		
		//Break the staff
		if (this.code.substring(0,4).equals("1100") && player.getRoom()==10) {
			player.adjustWisdom(10);
			game.getItem(noun).setLocation(81);
			game.getItem(noun).setFlag(-1);
			player.setPanelFlag(3);
			
			if (game.getItem(2).getLocation() != player.getRoom()) {
				game.setPanelMessages("It shatters releasing a rainbow of colours!", "", 1);
			} else {
				game.setPanelMessages("It shatters releasing a rainbow of colours!", 
									  "The egg hatches into a baby dactyl which takes"+
									  " Omegan in its claws and flies away", 2);
				game.getItem(39).setLocation(81);
				game.getItem(2).setLocation(2);
				game.getItem(2).setFlag(-1);
				player.adjustStrength(40);
			}
		}
		
		//Tap a person (and the still for some odd reason)
		if (this.verb==18 && (this.noun>29 && this.noun<34) || 
			(this.noun>38 && this.noun<44) || this.noun==16) {
			
			//Carrying the axe?
			if (game.getItem(9).getLocation()<1) {
				kill(player,game);
			}
		}
	}
	
	public void kill(Player player, Game game) {
		
		//Take a hit even if the object isn't present
		player.adjustStrength(-12);
		player.adjustWisdom(-10);
		game.setMessage("That would be unwise");
		
		//Is object present - ends game
		if (game.getItem(noun).getLocation() == player.getRoom()) {
			game.getItem(Constants.noItems).setFlag(1);
			player.setPanelFlag(3);
			game.setPanelMessages("Thunder splits the sky!","It is the triumphant"
					+ " voice of Omegan.|Well done Alphan!|The means becomes the"
							+ " end.|I claim you as my own!|Ha Ha Hah!",6);
			player.setStrength(0);
			player.setWisdom(0);
			player.setTime(0);
		}
	}
	
	public void attack(Game game, Player player) {
		
		player.adjustStrength(-2);
		player.adjustWisdom(-2);
		
		if (game.getItem(noun).getLocation() == player.getRoom() || 
			game.getItem(noun).getLocation() ==0) {
			
			//Omegan the evil one
			if (noun==39) {
				game.setMessage("He laughs dangerously.");
			
			//Swampman
			} else if (noun==32) {
				game.setMessage("The swampman is unmoved.");
			
			//Sage of the Lilies
			} else if (noun==33) {
				game.setMessage("You can't touch her");
			
			//Logmen
			} else if (noun==41) {
				game.setMessage("They think that's funny!");

			//In the Dactyl's Nest
			} else if (player.getRoom()==46) {
				
				player.setPanelFlag(3);
				game.setPanelMessages("You anger the bird!",
									  "Which flies you to a remote place",2);
				player.setRoom(rand.nextInt(6)+63);
				game.getItem(16).setLocation(1);
				game.setMessage("");
			
			//Strike Flint
			} else if (code.substring(0,4).equals("1400")) {

				//Coal in room
				if (player.getRoom()==game.getItem(13).getLocation()) {
					game.getItem(13).setFlag(-1);
					game.getItem(noun).setFlag(-1);
					game.getItem(noun).setLocation(81);
					
					//Omegan's Cloak present
					if (player.getRoom()==game.getItem(39).getLocation()) {
						game.setPanelMessages("The coal burns with a red flame",
								"Which dissolves Omegan's Cloak",2);
						player.adjustWisdom(20);
					} else {
						game.setPanelMessages("The coal burns with a red flame","",1);					
					}
				}
			}
			player.adjustStrength(-8);
			player.adjustWisdom(-5);
		}		
	}
	
	public void swim(Player player,Game game) {

		if (player.getRoom()!=51 || game.getItem(29).getFlag()>0) {
			game.setMessage("You can't swim here!");
			player.adjustWisdom(1);
		} else {
			player.setPanelFlag(4);
			player.setSwimming();
		}
	}
	
	public void shelter(Player player,Game game) {
		
		if (game.getItem(36).getFlag()<0) {
			player.setPanelFlag(5);
		}
	}
	
	public void help(Player player, Game game) {
		
		game.setMessage("?!?");
		
		//Help Villager or Sage
		if (code.equals("3075075") || code.equals("3371071")) {
			game.setMessage("How will you do that?");
			
			//Scratch the Sage
			if (code.equals("3371071") && verb == 28) {
				game.getItem(3).setFlag(0);
				game.setMessage("She nods slowly.");
				player.adjustWisdom(5);
			}
		} 
	}
	
	public void polish(Player player, Game game) {
		
		game.setMessage("A-dub-dub");
		
		//Rub the mouth at the crystal stone
		if (code.substring(0,4).equals("2815") && player.getRoom()==15) {
			if (game.getItem(noun).getFlag()==1) {
				game.getItem(noun).setFlag(1);
				game.setMessage("Reflections stir within.");
			} else if (game.getItem(5).getLocation()==0) {
				game.getItem(8).setFlag(0);
				take(game,player); //Not sure why this is here
				game.setMessage("The stone utters 'Stony Words'");
			}
		}
	}
	
	public void examine(Game game) {
		game.setMessage("Examine the book for clues");
		
		//Read the parchment
		if (code.substring(0,3).equals("600")) {
			game.setMessage("Remember Aladin. It Worked for him.");
		}
	}
	
	public void fill(Game game) {
		
		game.setMessage("Not sure that can be done.");
		
		//Fill Earthenware Jug
		if (code.equals("40041")) {
			game.getItem(4).setFlag(-1);
			game.setMessage("Filled");
		}
	}
	
	public void say(Game game, String noun,Player player) {
		game.setMessage(noun);
		
		//Speaking to the clashing rocks
		if (noun.toLowerCase().equals("stony words") && player.getRoom()==47 &&
			game.getItem(8).getFlag()==0) {
			game.setMessage("The stones are fixed.");
			game.getItem(44).setFlag(1);
		}
		
		//Speaking to the scavenger -has flowers and pebble
		if (noun.toLowerCase().equals("remember old times") && 
			player.getRoom()==game.getItem(42).getLocation() && 
			game.getItem(3).getLocation()==81 &&
			game.getItem(12).getLocation()==81) {
			game.setMessage("He eats the flowers - and changes");
			game.getItem(42).setFlag(1);
			game.getItem(43).setFlag(0);
		}
	}
	
	public int rest(Game game, Player player, boolean msgSet) {
		
		//Randomly selects time to wait based on Living Storm
		int count = game.getItem(36).getFlag()+3;
		
		//Waits and increases strength
		for (int i=1;i<count;i++) {
			player.reduceTime();
			if (player.getStrength()<100 || game.getItem(22).getFlag()==(player.getRoom()*-1)) {
				player.adjustStrength(1);
			}
		}
		
		if (player.getTime()>100 || game.getItem(36).getFlag()<1) {
			player.adjustWisdom(2);
			game.getItem(36).setFlag(1);
		}
		
		if (!msgSet) {
			game.setPanelMessages("Time passes ...", "Time passes ...", count);
			game.setMessage("Ok");
		}
		
		return count;		
	}
	
	public void wave(Game game,Player player) {
		
		//Wave to boatman
		if (game.getItem(25).checkLocation(player.getRoom())) {
			game.setMessage("The boatman waves back.");
		}
		
		//Wave torch
		if (code.substring(0,3).equals("700")) {
			game.getItem(7).setFlag(1);
			game.setMessage("The torch brightens.");
			player.adjustWisdom(8);
		}
	}
	
	public void info (Game game, Player player) {
		
		boolean hasItem = false;
		
		game.setMessage("Info - Items carried|");
		game.addMessage("Food: "+player.getFood());
		game.addMessage("      Drink: "+player.getDrink()+"|");
		
		int msglen = 0;
		
		for (int i=1;i<Constants.carriableItems+1;i++) {
			
			if (game.getItem(i).checkLocation(0)) {
				
				if (!hasItem) {
					game.addMessage("|Items: ");
					hasItem = true;
					msglen += 7;
				}
				
				msglen += game.getItem(i).getItem().length();
				game.addMessage(" "+game.getItem(i).getItem());
			}
		}	
	}
	
	public void save(Game game, Player player) {
		
		boolean writeFile = false;
		String[] commands = command.split(" ");
		
		if (commands.length==1) {
			game.setMessage("Please include the name of your game.");
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
				game.setMessage("File already exists. Please add 'o' to the end to overwrite.|");
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
					game.setMessage("Save successful");

				} catch (IOException e) {
					game.setMessage("Game failed to save");
					e.printStackTrace();
				}
			} else {
				game.addMessage("Game not saved");
			}
		}
	}
	
	public boolean load(Game game, Player player) {
		
		boolean loadFile = false;
		String[] commands = command.split(" ");
		
		if (commands.length==1) {
			game.setMessage("Please include the name of your game.");
		} else {
		
			//Checks to see if the file exists
			File saveGameDirectory = new File("savegames");				
			File saveFile = new File(saveGameDirectory+"/"+commands[1]+".sav");		
		
			//If not available
			if (!saveFile.exists()) {			
				game.setMessage("Sorry, the saved game does not exist. Type 'games' to list games.|");
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
					this.game.setMessage("Game successfully loaded");
							
					//Location failed to load
				} catch (IOException|ClassNotFoundException e) {
					this.game.setMessage("Game failed to load");
					e.printStackTrace();
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
		game.setMessage("Games Saves");
		int noGames=savFiles.length;
		int gameStart = 0;
		int totalDisplayed = 9;
		boolean moreGames = false;
				
		//Check with number of games and determine which games are displayed
		if (noGames>9) {
			gameStart = game.getCount()*9;
			if (noGames-gameStart>9) {
				game.setCount();
				totalDisplayed = gameStart+9;
				moreGames = true;
			} else {
				totalDisplayed += noGames-gameStart;
				game.resetCount();
			}
		} else {
			totalDisplayed  = noGames;
		}
				
		//Display the games selected
		for (int i = gameStart; i<totalDisplayed;i++ ) {
			game.addMessage(String.format("|%s",savFiles[i].getName()));
		}
		
		if (moreGames) {
			game.addMessage("|Type 'games' for more");
		}
	}
	
	public void quit(Player player, Game game) {
		
		game.setMessage("You relinquish your quest");
		game.getItem(Constants.noNouns).setFlag(-1);
		player.setTime(1);
		game.endGame();
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
 */