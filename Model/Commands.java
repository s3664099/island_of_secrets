/*
Title: Island of Secrets Command Execution Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.15
Date: 3 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

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
	
	public void move(Game game,Player player,String noun) {
		
		int direction = 0;
		boolean haveMoved=false;
		
		//Verb only
		if (this.noun == 52) {
			direction = this.verb;
		} else if (this.noun>Constants.noItems && this.noun<Constants.noNouns) {
			direction = this.noun-Constants.noItems;
		}
		
		if (player.getRoom()==12 && noun.equals("cave")) {
			direction=4;
		} else if (player.getRoom()==45 && noun.equals("hut")) {
			direction=4;
		} else if (player.getRoom()==53 && noun.equals("hut")) {
			direction=4;
		} else if (player.getRoom()==70 && noun.equals("hut") ) {
			direction=1;
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
		}
			
		//Poisoned waters
		if (code.equals("490051") && game.getItem(29).getFlag()==0) {
			player.setPanelFlag(4);
		}
				
		//Checks if player able to move
		//Prevents Player from leaving is Omegan present and strength/wisdom too little, or in lair
		if (game.getItem(39).checkLocation(player.getRoom()) && 
			(player.getStrengthWisdon()<180 || player.getRoom()==10)) {
			game.setMessage("Omegan's presence prevents you from leaving!");
		
		//Swampman blocks
		} else if (player.getRoom() == game.getItem(32).getLocation() && 
					game.getItem(32).getFlag()<1 && direction == 3) {
			game.setMessage("He will not let you pass.");
		
		//The Rocks
		} else if (player.getRoom() == 47 && game.getItem(44).getFlag()==0) {
			game.setMessage("The rocks move to prevent you");
		
		//Room with Arms
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
			
			//Sets direction to 0 if not cardinal
			if (direction>4) {
				direction = 0;
			}
					
			//Is direction a cardinal?
			if (direction>0) {
				if (game.checkExit(player.getRoom(),direction-1)) {
					int newRoom = player.getRoom() + Integer.parseInt(
						"-10+10+01-01".substring((direction-1)*3, ((direction-1)*3)+3));
					player.setRoom(newRoom);
					game.setMessage("Ok");
					haveMoved = true;
					game.getRoom(newRoom).setVisited();
				}
			}
			
			//Otherwise can't move
			if (direction<1 || !haveMoved) {
				game.setMessage("You can't go that way");
			}
			
			//Room with the hands
			if (player.getRoom()==28 && game.getItem(7).getFlag()!=1) {
				game.setMessage("You enter the room and giant hands grab you and hold you fast");
			} else if (player.getRoom()==28) {
				game.setMessage("You enter the room and brightly shining torch force the arms to retreat to the walls");
			} else if (player.getRoom()==27 && direction==1) {
				game.setMessage("The doors slam shut behind you preventing you from leaving");
			}
			
			
			//Does the player have the beast and is on the jetty
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
					game.getItem(Constants.noNouns).setFlag(1);
				} else {
					
					game.setPanelMessages("You board the craft ...",
							  "and are taken to the Island of Secrets.",2);
					player.setRoom(57);
				}
				game.setMessage("The boat skims the dark and silent waters.");
				player.setPanelFlag(3);
			}
		}
	}
	
	public void take(Game game,Player player) {
				
		//Is the item present, and can it be taken?
		if (((game.getItem(noun).getFlag()>0 && game.getItem(noun).getFlag()<9) ||
			game.getItem(noun).getLocation()!=player.getRoom()) && this.noun<=Constants.carriableItems) {
			
			//Pick more apples and add to food.
			if (player.getRoom()==45 && game.checkApples() && noun==1) {
				player.adjustFood(1);
				game.setMessage("You pick an apple from the tree");
				player.setWeight(player.getWeight()+1);
			} else if (player.getRoom()==45 && noun ==1) {
				game.setMessage("There are no more apples within reach");
			} else if (player.getRoom()==27 && noun == 7) {
				game.setMessage("There are no more within reach");
			} else {
				game.setMessage("What "+game.getItem(noun).getItem()+"?");
			}
			
		} else {
			
			//Evil books in library
			if (this.code.equals("3450050")) {
				player.setWisdom(player.getWisdom()-5);
				player.setStrength(player.getStrength()-8);
				game.setMessage("They are cursed");
			} else {
				
				//Omegan's Cloak
				if (this.code.equals("3810010")) {
					
					//Add special lightning Flashes screen
					game.setMessage("Lightning Flashes");
					
					game.getItem(39).setLocation(player.getRoom());
					player.setWisdom(player.getWisdom()-2);
					player.setStrength(player.getStrength()-8);
					player.setPanelFlag(2);
				}
				
				//1st - pick mushrooms or apple
				//2nd - catch canyon beast
				//3rd - noun not an item
				if ((verb == 15 && noun != 20 && noun != 1) || (verb == 29 && noun !=16) ||
					noun > Constants.carriableItems) {
					
					//Makes sure that the cloak section is not overwritten
					if (!this.code.equals("3810010")) {
						game.setMessage("You can't "+command);
					}
					
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
						
						//Makes sure that wisdom increase only happens once
						if (!game.getItem(noun).checkWisdomGain()) {
							player.setWisdom(player.getWisdom()+4);
							game.getItem(noun).setWisdomGain();
						}
						
						player.setWeight(player.getWeight()+1);
						
						if (game.getItem(noun).getFlag()>1) {
							game.getItem(noun).setFlag(0);
						}
					}
																				
					//Handles the bird when attempting to take the egg without the staff
					if (code.equals("246046") && game.getItem(11).getLocation() != 0) {

						game.setMessage("You anger the bird");
						game.getItem(noun).setLocation(player.getRoom());
						
						//One in three bird takes you to random spot & replaces wild canyon beast
						//Needed beast to actually get here
						if (rand.nextInt(3)>1) {
							game.addMessage(" which flies you to a remote place.");
							player.setRoom(63+rand.nextInt(6));
							game.getItem(16).setLocation(1);
							game.getItem(16).setFlag(0);
						}
					} else if (code.equals("246046") && game.getItem(11).getLocation() == 0) {
						game.setMessage("You use the staff to keep the Dactyl away and take the egg");
					}
				}	
			}
		}
	}

	public String give(Game game,Player player,String[] commands) {
		
		String object = "";
				
		if ((noun != 24 && game.getItem(noun).getLocation()>0) || noun == 52) {
			
			String itemName = game.getItem(noun).getItem();
			
			if (itemName.length()==0) {
				itemName = "that";
			} 
			
			game.setMessage("You don't have "+itemName);
			
		} else {
			
			if (commands.length<3) {
				game.setMessage("Give to whom?");
				game.setResponse(1);
			} else {
				
				if (commands[2].equals("to") && commands.length>3) {
					object = commands[3];
				} else {
					game.setMessage("I don't understand");
				}
			}
		}
		
		return object;
	}
	
	public void drop(Game game, Player player) {
		
		//Dropping the Earthenware Jug
		if (noun == 4 && game.getItem(noun).getLocation()==0 && verb==9) {
			game.getItem(noun).setLocation(81);
			player.setWisdom(player.getWisdom()-1);
			player.setWeight(player.getWeight()-1);
			game.setMessage("It breaks!");
		
		//Dropping a brightly glowing torch
		} else if (code.substring(0,3).equals("701")) {
			game.getItem(noun).setLocation(player.getRoom());
			game.setMessage("The torch dims when you drop it.");	
			game.getItem(7).setFlag(0);
			game.getItem(7).setDescription("a flickering torch");
			
			if (player.getRoom()==28) {
				game.addMessage("|Upon dropping the torch the arms reach out and grab you, preventing you from moving.");
			}
			
		//Dropping other items
		} else {
			if (game.getItem(noun).getLocation()==0 && noun<Constants.foodLine) {
				game.getItem(noun).setLocation(player.getRoom());
				player.setWeight(player.getWeight()-1);
				game.setMessage("Done");
				
				//Dropping the beast
				if (noun == 16) {
					game.getItem(noun).setFlag(0);
				}
				
			} else {
				game.setMessage("I can't. Sorry.");
			}
		}
	}
	
	public void eat(Game game, Player player,String nounStr) {
		
		//Allows 'Eat Food' to work
		if (nounStr.equals("food")) {
			noun = 17;
		}
		
		//Eating lillies (moved here since in original game code wouldn't reach)
		if (noun == 3 && game.getItem(3).getLocation()==0) {
			player.adjustWisdom(-5);
			player.adjustStrength(-2);
			game.setMessage("They make you very ill");
		
		//Item unedible
		} else if ((noun<=Constants.foodLine || noun>=Constants.drinkLine) 
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
				game.setMessage("You don't have "+game.getItem(noun).getItem());
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
		} else if (this.code.substring(0,4).equals("1601")) {
			game.setMessage("You are already riding the beast.");
		} else {
			game.setMessage("How?");
		}
	}
	
	public void open(Game game,Player player) {
		
		game.setMessage("I'm unable to do that");
		
		//Open chest in grandpa's shack
		if (this.code.equals("2644044")) {
			game.setMessage("The chest opens. There is something inside");
			game.getItem(6).setFlag(9);
			game.getItem(5).setFlag(9);
			game.getItem(15).setFlag(9);
			game.getItem(26).setFlag(1);
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
		game.setMessage("Nothing happens");

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
		
		//Response if player uses the staff without meeting the conditions above
		} else if (this.code.substring(0,4).equals("1100") && verb == 19) {
			game.getItem(noun).setLocation(81);
			game.getItem(noun).setFlag(-1);
			player.setPanelFlag(3);
			game.setPanelMessages("It shatters releasing a rainbow of colours!", "", 1);
		}
		
		//Tap a person (and the still for some odd reason)
		if (this.verb==18 && (this.noun>29 && this.noun<34) || 
			(this.noun>38 && this.noun<44) || this.noun==16) {
			
			//Carrying the axe?
			if (game.getItem(9).getLocation()<1) {
				kill(player,game);
			} else {
				game.setMessage("You annoy the "+game.getItem(noun).getItem());
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
			game.endGame();	
		}
	}
	
	public void attack(Game game, Player player) {
		
		game.setMessage("That would be unwise");
		
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
				game.getItem(3).setLocation(81);
			
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
				game.getItem(16).setFlag(0);
				game.setMessage("");
			
			//Strike Flint
			} else if (code.substring(0,4).equals("1400")) {

				game.setMessage("Sparks fly");
				
				//Coal in room
				if (player.getRoom()==game.getItem(13).getLocation()) {
					
					game.getItem(noun).setFlag(-1);
					game.getItem(13).setLocation(81);
					player.setPanelFlag(3);
					
					//Omegan's present in his sanctum
					if (player.getRoom()==game.getItem(38).getLocation() && player.getRoom()==10) {
						game.setPanelMessages("The coal burns with a red flame",
								"Which dissolves Omegan's Cloak",2);
						player.adjustWisdom(20);
						game.getItem(13).setFlag(-1);
						game.getItem(38).setLocation(81);
					} else {
						game.setPanelMessages("The coal burns with a red flame","",1);					
					}
				}
			}
			player.adjustStrength(-8);
			player.adjustWisdom(-5);
		}		
	}
	
	//Works
	public void swim(Player player,Game game) {

		if (player.getRoom()!=51 || game.getItem(29).getFlag()>0) {
			game.setMessage("You can't swim here!");
			player.adjustWisdom(1);
		} else {
			game.setMessage("You dive into the water");
			player.setPanelFlag(4);
			player.setRoom(rand.nextInt(5)+1);
			player.setSwimming();
		}
	}
	
	public int shelter(Player player,Game game, String[] commands) {
		
		int location = -1;
		
		if (game.getItem(36).getFlag()<0) {
			
			if (commands.length>1) {
				
				String shelterLocation = commands[1];
				game.setMessage("");
				
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
					game.setMessage("I'm sorry, I do not know that place");
				}
				
			} else {
				game.setMessage("You can shelter in:");
				game.setResponse(2);
			}
		} else {
			game.setMessage("Not possible at the moment.");
		}
		
		return location;
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
	
	public void polish(Player player, Game game,String noun) {
		
		game.setMessage("A-dub-dub");
				
		//Rub the stone
		if (noun.equals("stone") && player.getRoom()==15 && game.getItem(28).getFlag()==1
			&& game.getItem(5).getLocation()==0) {
			game.getItem(28).setFlag(0);
			game.setMessage("Reflections stir within.");			
		} else if (code.substring(0,4).equals("2815") && player.getRoom()==15
				&& game.getItem(28).getFlag()==0) {
			game.getItem(8).setFlag(0);
			take(game,player);
			game.setMessage("The stone utters 'Stony Words'");
		}
	}
	
	public void examine(Player player, Game game, String[] command) {
		
		game.setMessage("Examine the book for clues");
		
		if (command[1].equals("well") && player.getRoom()==19) {
			command[1]="room";
		}
				
		//Read the parchment
		if (code.substring(0,3).equals("600")) {
			game.setMessage("Remember Aladin. It Worked for him.");
			
		//Examining the chest
		} else if (code.equals("2644044") && command[0].equals("examine")) {
			game.setMessage("The chest is closed");
		} else if (code.equals("2644144") && command[0].equals("examine")) {
			game.setMessage("The chest if full of Grandpa's old stuff. On the lid is parchment that says |"
					+ "'Use the rag if it looks a bit dim'");
			
			boolean ragSeen = false;
			
			if (game.getItem(5).getLocation()==44 && game.getItem(5).getFlag()==9) {
				game.addMessage("|The chest contains a dirty old rag");
				game.getItem(5).setFlag(0);
				ragSeen = true;
			}
			
			if (game.getItem(15).getLocation()==44 && game.getItem(15).getFlag()==9) {
				
				String hammer = "";

				if (!ragSeen) {
					hammer = "|On the table is";
				} else {
					hammer = " and";
				}	
				
				game.addMessage(hammer+" a geologist's hammer");
				game.getItem(15).setFlag(0);
			}
		} else if (command[1].equals("table") && player.getRoom()==44 && command[0].equals("examine")) {
			
			game.setMessage("The coffee table looks like it has been better days.");
			boolean breadSeen = false;
			
			if (game.getItem(17).getLocation()==44 && game.getItem(17).getFlag()==9) {
				game.addMessage("|On the table is a loaf of bread");
				game.getItem(17).setFlag(0);
				breadSeen = true;
			}
			
			if (game.getItem(21).getLocation()==44 && game.getItem(21).getFlag()==9) {
				
				String water = "";
				
				if (!breadSeen) {
					water = "|On the table is";
				} else {
					water = " and";
				}
				
				game.addMessage(water+" a bottle of water");
				game.getItem(21).setFlag(0);
			}
		} else if (command[1].equals("column") && player.getRoom()==58 && command[0].equals("examine")) {
			game.setMessage("At the bottom of the column are the words 'remember old times'");
		} else if (command[0].equals("examine") && command[1].equals("room")) {
			game.setMessage("There doesn't seem anything out of the ordinary here");
			
			if (player.getRoom()==65 || player.getRoom()==66 || player.getRoom()==67) {
				game.setMessage("You can see quite a distance from here. To the north a forest rises into ragged peaks|");
				game.addMessage("while to the west you can see a log village on a lake. The the south is a swamp, while|");
				game.addMessage("blasted lands disappear to the east. In the middle of a lake, shrouded in mist, appears|");
				game.addMessage("to be an ancient castle.");
			} else if (player.getRoom()==19) {
				game.setMessage("The well emits deathly energy. Surrounding the well are incorporeal creatures attempting|");
				game.addMessage("to add you to their number");
			} else if (player.getRoom()==74 || player.getRoom()==75||player.getRoom()==76) {
				game.setMessage("You see a village that appears to have been frozen in time, with buildings and|");
				game.addMessage("inhabitants having been turned to stone. The silence is eerie, and the swamp|");
				game.addMessage("seems to be ever so slowly enveloping it.");
			} else if (player.getRoom()==17) {
				game.setMessage("This room has rows and rows of pods with glass lids containing what appears|");
				game.addMessage("appears to be identical people fast asleep, or even in a coma. However a number|");
				game.addMessage("appear to be cracked, or even broken, and the bodies inside are either corposes or|");
				game.addMessage("have rotted away. A foul, almost toxic, smell seems to be present.");
			} else if (player.getRoom()==10) {
				game.setMessage("This room has an evil presence in it, with strange symbols on the floor and wall|");
				game.addMessage("Shadows seem to flicker across the wall, and the floor is covered in a crest, from|");
				game.addMessage("long forgotten family. A crystaline glass window looks out over the island.");
			} else if (player.getRoom()==58) {
				game.setMessage("The column looks like it has seen better days. It is crumbling and appears that a|");
				game.addMessage("peice could easily be removed if you had the right equipment. There is a message|");
				game.addMessage("inscribed at the base of the column.");
			} else if (player.getRoom()==60) {
				game.setMessage("This hut looks like it has been well used, but hasn't been occupied for a long time.|");
				game.addMessage("Whoever lived here, or worked from here, must have been some sort of scholar,|");
				game.addMessage("considering the contents. There is a desk that is covered in papers, which includes|"); 
				game.addMessage("what looks like a map.");
				game.getRoom(player.getRoom()).setViewed();
			}
		} else if (command[1].equals("map") && player.getRoom()==60 && game.getRoom(player.getRoom()).getViewed()) {
				
			game.setMessage("The map looks like it is of a castle located on an island");
			game.getRoom(57).setVisited();
			
			for (int x=0;x<5;x++) {
				for (int y=7;y<11;y++) {
					game.getRoom((x*10)+y).setVisited();
				}
			}
				
		} else if ((command[1].equals("papers") || command[1].equals("diary")) && player.getRoom()==60 && game.getRoom(player.getRoom()).getViewed()) {
		
			game.setMessage("The papers look like the belong to somebody by the name Median. It chronicles his search|");
			game.addMessage("for somebody name Omegan who has poisoned the land. It looks like he is also seeing a cure|");
			game.addMessage("for this poison. In addition, you notice the following|");
			
			int x=rand.nextInt(10);			

			switch (x) {
				case 0:
					game.addMessage("'Only those of strong will can resist the boatman'");
					break;
				case 1:
					game.addMessage("'The Sagemaster rewards those who help her'");
					break;
				case 2:
					game.addMessage("'The stone's mouth needs some polishing'");
					break;
				case 3:
					game.addMessage("'The hands fear bright light'");
					break;
				case 4:
					game.addMessage("'Words will stop the stone'");
					break;
				case 5:
					game.addMessage("'Omegan's Cloak is his strength'");
					break;
				case 6:
					game.addMessage("'The Dactyl could be useful'");
					break;
				default:
					game.addMessage("'I think it is time to head to the island. I hope I remember.'");
			}	
		}
	}
	
	public void fill(Game game,Player player) {
		
		game.setMessage("Not sure that can be done.");
		
		//Fill Earthenware Jug
		if (code.equals("40041")) {
			game.getItem(4).setFlag(-1);
			game.setMessage("Filled");
			game.getItem(4).setDescription("A jug full of bubbling green liquid");
		} else if (code.equals("40013")) {
			game.setMessage("The water streams out of the jug");
		} else if (code.substring(0,2).equals("40")) {
			
			if (game.getItem(4).getFlag()==-1)  {
				if (player.getRoom()==41 || player.getRoom()==13) {
					game.setMessage("The jug is already full");
				}
			}
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
		
		//Bases time to wait based on Living Storm flag
		int count = Math.abs(game.getItem(36).getFlag()+3);
						
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
			player.setPanelFlag(3);
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
			
			if (player.getRoom()==28) {
				game.addMessage("The hands release you and retreat into the wall.");
			}
			
			game.getItem(7).setDescription("a brightly glowing torch");
			player.adjustWisdom(8);
		}
	}
	
	public void info (Game game, Player player) {
		
		boolean hasItem = false;
		int lineLength = Constants.lineLength;
		int itemLength = 0;
		String items = "";
		
		game.setMessage("Info - Items carried|");
		game.addMessage("Food: "+player.getFood());
		game.addMessage("      Drink: "+player.getDrink()+"|");
				
		for (int i=1;i<Constants.carriableItems+1;i++) {
			
			if (game.getItem(i).checkLocation(0)) {
				
				//Checks the length of the string and adds a break if too long
				if (itemLength+game.getItem(i).getItem().length()+1>lineLength) {
					items = items +"|";
					itemLength = 0;
				}
				
				//First item recorded
				if (!hasItem) {
					items = "|Items: "+game.getItem(i).getItem();
					hasItem = true;
					itemLength = items.length()-1;
				
				//Subsequent items
				} else {
					
					//Adds the item and counts the length of the string
					int extraLength = 1;
					if (itemLength > 0) {
						items = items +", ";
						extraLength = 2;
					}
					items = items+game.getItem(i).getItem();
					itemLength += game.getItem(i).getItem().length()+extraLength;
				}
			}
		}
		game.addMessage(items);
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
					//e.printStackTrace();
				}
			} else {
				game.addMessage("Game not saved");
			}
		}
	}
	
	public boolean load(Game game, Player player) {
		
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
					game.resetCount();
							
					//Location failed to load
				} catch (IOException|ClassNotFoundException e) {
					this.game.setMessage("Game failed to load");
					//e.printStackTrace();
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
		String[] gameDisplayed = game.getDisplayedGames();
		int noGames=savFiles.length;
		int gameStart = 0;
		int totalDisplayed = 5;
		int maxDisplay = 5;
		
		game.setMoreGames(false);
		game.setLessGames(false);
		
		if (noGames==0) {
			game.setMessage("There are no saved games to display");
		} else {

			//Check with number of games and determine which games are displayed
			if (noGames>5) {
				gameStart = game.getCount()*maxDisplay;
				if (noGames-gameStart>maxDisplay) {
					totalDisplayed = gameStart+maxDisplay;
					game.setMoreGames(true);
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
				game.setLessGames(true);
			}
		
			game.setGameDisplay(true);
			game.setDisplayedGames(gameDisplayed);
			game.setMessage(gameMessage);
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
 * 
 */