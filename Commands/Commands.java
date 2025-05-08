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
	
	public void take(Game game,Player player) {
				
		//Is the item present, and can it be taken?
		if (((game.getItem(noun).getItemFlag()>0 && game.getItem(noun).getItemFlag()<9) ||
			game.getItem(noun).getItemLocation()!=player.getRoom()) && this.noun<=Constants.MAX_CARRIABLE_ITEMS) {
			
			//Pick more apples and add to food.
			if (player.getRoom()==45 && game.checkApples() && noun==1) {
				player.setStat("food",((int) player.getStat("food"))+1);
				game.addMessage("You pick an apple from the tree",true,true);
				player.setStat("weight",((int) player.getStat("weight"))+1);
			} else if (player.getRoom()==45 && noun ==1) {
				game.addMessage("There are no more apples within reach",true,true);
			} else if (player.getRoom()==27 && noun == 7) {
				game.addMessage("There are no more within reach",true,true);
			} else {
				game.addMessage("What "+game.getItem(noun).getItemName()+"?",true,true);
			}
			
		} else {
			
			//Evil books in library
			if (this.code.equals("3450050")) {
				player.setStat("wisdom",(int) player.getStat("wisdom")-5);
				player.setStat("strength",(float) player.getStat("strength")-8);
				game.addMessage("They are cursed",true,true);
			} else {
				
				//Omegan's Cloak
				if (this.code.equals("3810010")) {
					
					//Add special lightning Flashes screen
					game.addMessage("Lightning Flashes",true,true);
					
					game.getItem(39).setItemLocation(player.getRoom());
					player.setStat("wisdom",(int) player.getStat("wisdom")-2);
					player.setStat("strength",(float) player.getStat("strength")-8);
					game.setLightingGameState();
				}
				
				//1st - pick mushrooms or apple
				//2nd - catch canyon beast
				//3rd - noun not an item
				if ((verb == 15 && noun != 20 && noun != 1) || (verb == 29 && noun !=16) ||
					noun > Constants.MAX_CARRIABLE_ITEMS) {
					
					//Makes sure that the cloak section is not overwritten
					if (!this.code.equals("3810010")) {
						game.addMessage("You can't "+command,true,true);
					}
					
				} else {
					
					int weight = 0;
										
					//picks up item
					if (game.getItem(noun).getItemLocation()== player.getRoom() && (
						game.getItem(noun).getItemFlag()<1 || game.getItem(noun).getItemFlag()==9)
						&& noun<Constants.MAX_CARRIABLE_ITEMS) {
							game.getItem(noun).setItemLocation(0);
							weight = -1;
					}
					
					if (noun == 16 && game.getItem(10).getItemLocation()!=0) {
						game.getItem(noun).setItemLocation(player.getRoom());
						game.addMessage("It escaped",true,true);
						weight = 0;
					}
					
					if (noun>Constants.FOOD_THRESHOLD && noun<Constants.DRINK_THRESHOLD) {
						weight = -1;
						player.setStat("food",((int) player.getStat("food"))+2);
						game.getItem(noun).setItemLocation(-18);
					}
					
					if (noun>=Constants.DRINK_THRESHOLD && noun<Constants.MAX_CARRIABLE_ITEMS) {
						weight = -1;
						player.setStat("drink",((int) player.getStat("drink"))+2);
						game.getItem(noun).setItemLocation(-18);
					}
										
					if (weight == -1) {
						game.addMessage("Taken",true,true);
						
						//Makes sure that wisdom increase only happens once
						if (!game.getItem(noun).hasWisdonAcquired()) {
							player.setStat("wisdom",(int) player.getStat("wisdom")+4);
							game.getItem(noun).setWisdomAcquired(true);
						}
						
						player.setStat("weight",((int) player.getStat("weight"))+1);
						
						if (game.getItem(noun).getItemFlag()>1) {
							game.getItem(noun).setItemFlag(0);
						}
					}
																				
					//Handles the bird when attempting to take the egg without the staff
					if (code.equals("246046") && game.getItem(11).getItemLocation() != 0) {

						game.addMessage("You anger the bird",true,true);
						game.getItem(noun).setItemLocation(player.getRoom());
						
						//One in three bird takes you to random spot & replaces wild canyon beast
						//Needed beast to actually get here
						if (rand.nextInt(3)>1) {
							game.addMessage(" which flies you to a remote place.",false,true);
							player.setRoom(63+rand.nextInt(6));
							game.getItem(16).setItemLocation(1);
							game.getItem(16).setItemFlag(0);
						}
					} else if (code.equals("246046") && game.getItem(11).getItemLocation() == 0) {
						game.addMessage("You use the staff to keep the Dactyl away and take the egg",true,true);
					}
				}	
			}
		}
	}

	public String give(Game game,Player player,String[] commands) {
		
		String object = "";
				
		if ((noun != 24 && game.getItem(noun).getItemLocation()>0) || noun == 52) {
			
			String itemName = game.getItem(noun).getItemName();
			
			if (itemName.length()==0) {
				itemName = "that";
			} 
			
			game.addMessage("You don't have "+itemName,true,true);
			
		} else {
			
			if (commands.length<3) {
				game.addMessage("Give to whom?",true,true);
				game.setGiveState();
			} else {
				
				if (commands[2].equals("to") && commands.length>3) {
					object = commands[3];
				} else {
					game.addMessage("I don't understand",true,true);
				}
			}
		}
		
		return object;
	}
	
	public void drop(Game game, Player player) {
		
		//Dropping the Earthenware Jug
		if (noun == 4 && game.getItem(noun).getItemLocation()==0 && verb==9) {
			game.getItem(noun).setItemLocation(81);
			player.setStat("wisdom",(int) player.getStat("wisdom")-1);
			player.setStat("weight",((int) player.getStat("weight"))-1);
			game.addMessage("It breaks!",true,true);
		
		//Dropping a brightly glowing torch
		} else if (code.substring(0,3).equals("701")) {
			game.getItem(noun).setItemLocation(player.getRoom());
			game.addMessage("The torch dims when you drop it.",true,true);	
			game.getItem(7).setItemFlag(0);
			game.getItem(7).setItemName("a flickering torch");
			
			if (player.getRoom()==28) {
				game.addMessage("Upon dropping the torch the arms reach out and grab you, preventing you from moving.",false,true);
			}
			
		//Dropping other items
		} else {
			if (game.getItem(noun).getItemLocation()==0 && noun<Constants.FOOD_THRESHOLD) {
				game.getItem(noun).setItemLocation(player.getRoom());
				player.setStat("weight",((int) player.getStat("weight"))-1);
				game.addMessage("Done",true,true);
				
				//Dropping the beast
				if (noun == 16) {
					game.getItem(noun).setItemFlag(0);
				}
				
			} else {
				game.addMessage("I can't. Sorry.",true,true);
			}
		}
	}
	
	public void eat(Game game, Player player,String nounStr) {
		
		//Allows 'Eat Food' to work
		if (nounStr.equals("food")) {
			noun = 17;
		}
		
		//Eating lillies (moved here since in original game code wouldn't reach)
		if (noun == 3 && game.getItem(3).getItemLocation()==0) {
			player.setStat("wisdom",(int) player.getStat("wisdom")-5);
			player.setStat("strength",(float) player.getStat("strength")-2);
			game.addMessage("They make you very ill",true,true);
		
		//Item unedible
		} else if ((noun<=Constants.FOOD_THRESHOLD || noun>=Constants.DRINK_THRESHOLD) 
			&& nounStr.length()>0) {
			game.addMessage("You can't "+command,true,true);
			player.setStat("wisdom",(int) player.getStat("wisdom")-1);
		
		//Eat
		} else {
			game.addMessage("You have no food",true,true);
			
			if (((int) player.getStat("food")+1)>0) {
				player.setStat("food",((int) player.getStat("food"))-1);
				player.setStat("strength",(float) player.getStat("strength")+10);
				game.addMessage("Ok",true,true);
			}
		}
	}
	
	public void drink(Game game, Player player,String nounStr) {
				
		//Drinking green liquid
		if (noun==31) {
			
			if (game.getItemFlagSum(4)!=-1) {
				game.addMessage("You don't have "+game.getItem(noun).getItemName(),true,true);
			} else {
				game.addMessage("Ouch!",true,true);
				player.setStat("strength",(float) player.getStat("strength")-4);
				player.setStat("wisdom",(int) player.getStat("wisdom")-7);
				game.setMessageGameState();
				
				int count = rest(game,player,true);
				
				//Sets messages
				game.addPanelMessage("You taste a drop and ...",true);
				
				for (int i=0;i<count;i++) {
					game.addPanelMessage("Time passes ...", false);
				}
			}
			
		//Item undrinkable
		} else if ((noun<Constants.DRINK_THRESHOLD || noun>Constants.MAX_CARRIABLE_ITEMS) 
				&& nounStr.length()>0) {
				game.addMessage("You can't "+command,true,true);
				player.setStat("wisdom",(int) player.getStat("wisdom")-1);
		} else {
			
			game.addMessage("You have no drink.",true,true);
			if (((int) player.getStat("drink"))>0) {
				player.setStat("drink",((int) player.getStat("drink"))-1);
				player.setStat("strength",(float) player.getStat("strength")+7);
				game.addMessage("Ok",true,true);
			}
		}
	}
	
	public void ride(Game game) {
		
		//Riding the canyon beast
		if (this.code.substring(0,4).equals("1600")) {
			game.getItem(noun).setItemFlag(-1);
			game.addMessage("It allows you to ride.",true,true);
		} else if (this.code.substring(0,4).equals("1601")) {
			game.addMessage("You are already riding the beast.",true,true);
		} else {
			game.addMessage("How?",true,true);
		}
	}
	
	public void open(Game game,Player player) {
		
		game.addMessage("I'm unable to do that",true,true);
		
		//Open chest in grandpa's shack
		if (this.code.equals("2644044")) {
			game.addMessage("The chest opens. There is something inside",true,true);
			game.getItem(6).setItemFlag(9);
			game.getItem(5).setItemFlag(9);
			game.getItem(15).setItemFlag(9);
			game.getItem(26).setItemFlag(1);
		}
		
		//Open trapdoor in refuse filled room
		if (this.code.equals("2951151")) {
			game.addMessage("The trapdoor creaks",true,true);
			game.getItem(29).setItemFlag(0);
			player.setStat("wisdom",(int) player.getStat("wisdom")+3);
		}
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

		if (player.getRoom()!=51 || game.getItem(29).getItemFlag()>0) {
			game.addMessage("You can't swim here!",true,true);
			player.setStat("wisdom",(int) player.getStat("wisdom")-1);
		} else {
			game.addMessage("You dive into the water",true,true);
			player.setPlayerStateStartSwimming();
			player.setRoom(rand.nextInt(5)+1);
		}
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
		
		game.addMessage("?!?",true,true);
		
		//Help Villager or Sage
		if (code.equals("3075075") || code.equals("3371071")) {
			game.addMessage("How will you do that?",true,true);
			
			//Scratch the Sage
			if (code.equals("3371071") && verb == 28) {
				game.getItem(3).setItemFlag(0);
				game.addMessage("She nods slowly.",true,true);
				player.setStat("wisdom",(int) player.getStat("wisdom")+5);
			}
		} 
	}
	
	public void polish(Player player, Game game,String noun) {
		
		game.addMessage("A-dub-dub",true,true);
				
		//Rub the stone
		if (noun.equals("stone") && player.getRoom()==15 && game.getItem(28).getItemFlag()==1
			&& game.getItem(5).getItemLocation()==0) {
			game.getItem(28).setItemFlag(0);
			game.addMessage("Reflections stir within.",true,true);			
		} else if (code.substring(0,4).equals("2815") && player.getRoom()==15
				&& game.getItem(28).getItemFlag()==0) {
			game.getItem(8).setItemFlag(0);
			take(game,player);
			game.addMessage("The stone utters 'Stony Words'",true,true);
		}
	}
	
	public void examine(Player player, Game game, String[] command) {
		
		game.addMessage("Examine the book for clues",true,true);
		
		if (command[1].equals("well") && player.getRoom()==19) {
			command[1]="room";
		}
				
		//Read the parchment
		if (code.substring(0,3).equals("600")) {
			game.addMessage("Remember Aladin. It Worked for him.",true,true);
			
		//Examining the chest
		} else if (code.equals("2644044") && command[0].equals("examine")) {
			game.addMessage("The chest is closed",true,true);
		} else if (code.equals("2644144") && command[0].equals("examine")) {
			game.addMessage("The chest if full of Grandpa's old stuff. On the lid is parchment that says |"
					+ "'Use the rag if it looks a bit dim'",true,true);
			
			boolean ragSeen = false;
			
			if (game.getItem(5).getItemLocation()==44 && game.getItem(5).getItemFlag()==9) {
				game.addMessage(" The chest contains a dirty old rag",false,true);
				game.getItem(5).setItemFlag(0);
				ragSeen = true;
			}
			
			if (game.getItem(15).getItemLocation()==44 && game.getItem(15).getItemFlag()==9) {
				
				String hammer = "";

				if (!ragSeen) {
					hammer = "On the table is";
				} else {
					hammer = " and";
				}	
				
				game.addMessage(hammer+" a geologist's hammer",false,true);
				game.getItem(15).setItemFlag(0);
			}
		} else if (command[1].equals("table") && player.getRoom()==44 && command[0].equals("examine")) {
			
			game.addMessage("The coffee table looks like it has been better days.",true,true);
			boolean breadSeen = false;
			
			if (game.getItem(17).getItemLocation()==44 && game.getItem(17).getItemFlag()==9) {
				game.addMessage("On the table is a loaf of bread",false,true);
				game.getItem(17).setItemFlag(0);
				breadSeen = true;
			}
			
			if (game.getItem(21).getItemLocation()==44 && game.getItem(21).getItemFlag()==9) {
				
				String water = "";
				
				if (!breadSeen) {
					water = "On the table is";
				} else {
					water = " and";
				}
				
				game.addMessage(water+" a bottle of water",false,true);
				game.getItem(21).setItemFlag(0);
			}
		} else if (command[1].equals("column") && player.getRoom()==58 && command[0].equals("examine")) {
			game.addMessage("At the bottom of the column are the words 'remember old times'",true,true);
		} else if (command[0].equals("examine") && command[1].equals("room")) {
			game.addMessage("There doesn't seem anything out of the ordinary here",true,true);
			
			if (player.getRoom()==65 || player.getRoom()==66 || player.getRoom()==67) {
				game.addMessage("You can see quite a distance from here. To the north a forest rises into ragged peaks",true,true);
				game.addMessage("while to the west you can see a log village on a lake. The the south is a swamp, while",false,true);
				game.addMessage("blasted lands disappear to the east. In the middle of a lake, shrouded in mist, appears",false,true);
				game.addMessage("to be an ancient castle.",false,true);
			} else if (player.getRoom()==19) {
				game.addMessage("The well emits deathly energy. Surrounding the well are incorporeal creatures attempting",true,true);
				game.addMessage("to add you to their number",false,true);
			} else if (player.getRoom()==74 || player.getRoom()==75||player.getRoom()==76) {
				game.addMessage("You see a village that appears to have been frozen in time, with buildings and",true,true);
				game.addMessage("inhabitants having been turned to stone. The silence is eerie, and the swamp",false,true);
				game.addMessage("seems to be ever so slowly enveloping it.",false,true);
			} else if (player.getRoom()==17) {
				game.addMessage("This room has rows and rows of pods with glass lids containing what appears",true,true);
				game.addMessage("appears to be identical people fast asleep, or even in a coma. However a number",false,true);
				game.addMessage("appear to be cracked, or even broken, and the bodies inside are either corposes or",false,true);
				game.addMessage("have rotted away. A foul, almost toxic, smell seems to be present.",false,true);
			} else if (player.getRoom()==10) {
				game.addMessage("This room has an evil presence in it, with strange symbols on the floor and wall",true,true);
				game.addMessage("Shadows seem to flicker across the wall, and the floor is covered in a crest, from",false,true);
				game.addMessage("long forgotten family. A crystaline glass window looks out over the island.",false,true);
			} else if (player.getRoom()==58) {
				game.addMessage("The column looks like it has seen better days. It is crumbling and appears that a",true,true);
				game.addMessage("peice could easily be removed if you had the right equipment. There is a message",false,true);
				game.addMessage("inscribed at the base of the column.",false,true);
			} else if (player.getRoom()==60) {
				game.addMessage("This hut looks like it has been well used, but hasn't been occupied for a long time.",true,true);
				game.addMessage("Whoever lived here, or worked from here, must have been some sort of scholar,",false,true);
				game.addMessage("considering the contents. There is a desk that is covered in papers, which includes",false,true); 
				game.addMessage("what looks like a map.",false,true);
				game.getRoom(player.getRoom()).setViewed();
			}
		} else if (command[1].equals("map") && player.getRoom()==60 && game.getRoom(player.getRoom()).getViewed()) {
				
			game.addMessage("The map looks like it is of a castle located on an island",true,true);
			game.getRoom(57).setVisited();
			
			for (int x=0;x<5;x++) {
				for (int y=7;y<11;y++) {
					game.getRoom((x*10)+y).setVisited();
				}
			}
				
		} else if ((command[1].equals("papers") || command[1].equals("diary")) && player.getRoom()==60 && game.getRoom(player.getRoom()).getViewed()) {
		
			game.addMessage("The papers look like the belong to somebody by the name Median. It chronicles his search",true,true);
			game.addMessage("for somebody name Omegan who has poisoned the land. It looks like he is also seeing a cure",false,true);
			game.addMessage("for this poison. In addition, you notice the following",false,true);
			
			int x=rand.nextInt(10);			

			switch (x) {
				case 0:
					game.addMessage("'Only those of strong will can resist the boatman'",false,true);
					break;
				case 1:
					game.addMessage("'The Sagemaster rewards those who help her'",false,true);
					break;
				case 2:
					game.addMessage("'The stone's mouth needs some polishing'",false,true);
					break;
				case 3:
					game.addMessage("'The hands fear bright light'",false,true);
					break;
				case 4:
					game.addMessage("'Words will stop the stone'",false,true);
					break;
				case 5:
					game.addMessage("'Omegan's Cloak is his strength'",false,true);
					break;
				case 6:
					game.addMessage("'The Dactyl could be useful'",false,true);
					break;
				default:
					game.addMessage("'I think it is time to head to the island. I hope I remember.'",false,true);
			}	
		}
	}
	
	public void fill(Game game,Player player) {
		
		game.addMessage("Not sure that can be done.",true,true);
		
		//Fill Earthenware Jug
		if (code.equals("40041")) {
			game.getItem(4).setItemFlag(-1);
			game.addMessage("Filled",true,true);
			game.getItem(4).setItemName("A jug full of bubbling green liquid");
		} else if (code.equals("40013")) {
			game.addMessage("The water streams out of the jug",true,true);
		} else if (code.substring(0,2).equals("40")) {
			
			if (game.getItem(4).getItemFlag()==-1)  {
				if (player.getRoom()==41 || player.getRoom()==13) {
					game.addMessage("The jug is already full",true,true);
				}
			}
		}
	}
	
	public void say(Game game, String noun,Player player) {
		game.addMessage(noun,true,true);
		
		//Speaking to the clashing rocks
		if (noun.toLowerCase().equals("stony words") && player.getRoom()==47 &&
			game.getItem(8).getItemFlag()==0) {
			game.addMessage("The stones are fixed.",false,false);
			game.getItem(44).setItemFlag(1);
		}
		
		//Speaking to the scavenger -has flowers and pebble
		if (noun.toLowerCase().equals("remember old times") && 
			player.getRoom()==game.getItem(42).getItemLocation() && 
			game.getItem(3).getItemLocation()==81 &&
			game.getItem(12).getItemLocation()==81) {
			game.addMessage("He eats the flowers - and changes",false,false);
			game.getItem(42).setItemFlag(1);
			game.getItem(43).setItemFlag(0);
		}
	}
	
	public int rest(Game game, Player player, boolean msgSet) {
		
		//Bases time to wait based on Living Storm flag
		int count = Math.abs(game.getItem(36).getItemFlag()+3);
						
		//Waits and increases strength
		for (int i=1;i<count;i++) {
			player.reduceStat("timeRemaining");
			if (((float) player.getStat("strength"))<100 || game.getItem(22).getItemFlag()==(player.getRoom()*-1)) {
				player.setStat("strength",(float) player.getStat("strength")-8);
			}
		}
		
		if ((int) player.getStat("timeRemaining")>100 || game.getItem(36).getItemFlag()<1) {
			player.setStat("wisdom",(int) player.getStat("wisdom")+2);
			game.getItem(36).setItemFlag(1);
		}
				
		if (!msgSet) {
			
			game.addPanelMessage("Time passes ...", true);
			for (int i=1;i<count;i++) {
				game.addPanelMessage("Time passes ...", false);
			}
			game.addMessage("Ok",true,true);
			game.setMessageGameState();
		}
		
		return count;		
	}
	
	public void wave(Game game,Player player) {
		
		//Wave to boatman
		if (game.getItem(25).isAtLocation(player.getRoom())) {
			game.addMessage("The boatman waves back.",true,true);
		}
		
		//Wave torch
		if (code.substring(0,3).equals("700")) {
			game.getItem(7).setItemFlag(1);
			game.addMessage("The torch brightens.",true,true);
			
			if (player.getRoom()==28) {
				game.addMessage("The hands release you and retreat into the wall.",false,true);
			}
			
			game.getItem(7).setItemName("a brightly glowing torch");
			player.setStat("wisdom",(int) player.getStat("wisdom")+8);
		}
	}
	
	public void info (Game game, Player player) {
		
		boolean hasItem = false;
		int itemLength = 0;
		String items = "";
		
		game.addMessage("Info - Items carried",true,false);
		game.addMessage("Food: "+((int) player.getStat("food")),false,false);
		game.addMessage("Drink: "+((int) player.getStat("drink")),false,false);
				
		for (int i=1;i<Constants.MAX_CARRIABLE_ITEMS+1;i++) {
			
			if (game.getItem(i).isAtLocation(0)) {
				
				//First item recorded
				if (!hasItem) {
					items = "Items: "+game.getItem(i).getItemName();
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
					items = items+game.getItem(i).getItemName();
					itemLength += game.getItem(i).getItemName().length()+extraLength;
				}
			}
		}
		game.addMessage(items,false,false);
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