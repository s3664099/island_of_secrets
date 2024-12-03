/*
Title: Island of Secrets Command Execution Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.6
Date: 3 December 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

//880 IF B$="490051" AND F(29)=0 THEN GOSUB 2110:RETURN - Poisonous Waters Minigame
//Add method to create a Frame that is designed to just display messages

package Model;

import Data.Constants;
import java.util.Random;

public class Commands {
	
	private int verb;
	private int noun;
	private String code;
	private Random rand = new Random();
	private String command;
	
	public Commands(int verb,int noun, String code, String command) {
		this.verb = verb;
		this.noun = noun;
		this.code = code;
		this.command = command;
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
			
			//Handling the ferry man (this would be go boat)
			if (player.getRoom()==game.getItem(25).getLocation() && this.noun == 25) {
				
				//Probably need to move this out to a separate method
				game.setMessage("You board the craft ");
				
				if (player.getWisdom()<60) {
					game.addMessage("falling under the spell of the boatman ");
				}
				
				game.addMessage("and are taken to the Island of Secrets.");
				
				//Displays message on screen, and pauses for 10 seconds
				
				game.setMessage("");
				
				if (player.getWisdom()<60) {
					game.addMessage("to serve Omega forever!");
					game.getItem(direction).setFlag(Constants.noNouns-1);
				} else {
					game.addMessage("the boat skims the dark and silent waters.");
					player.setRoom(57);
				}
				
				//Do the same as the above
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
							System.out.println("Hello");
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
				player.setPanelFlag(4);
				int count = game.getItem(36).getFlag()+3;
				
				for (int i=1;i<count;i++) {
					player.reduceTime();
					if (player.getStrength()<100 || game.getItem(22).getFlag()==(player.getRoom()*-1)) {
						player.adjustStrength(1);
					}
				}
				
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
		
		//Break the column
		if (this.code.equals("1258158") || this.code.equals("2758158") && 
			game.getItem(15).getLocation()==0) {
				game.getItem(12).setFlag(0);
				game.getItem(27).setFlag(0);
				game.setMessage("Crack");
		}
		
		//Break the staff
		if (this.code.substring(0,4).equals("1100") && player.getRoom()==10) {
			//GOSUB 1980 - have it occur here
		}
		
		//Tap a person (and the still for some odd reason)
		if (this.verb==18 && (this.noun>29 && this.noun<34) || 
			(this.noun>38 && this.noun<44) || this.noun==16) {
			//GOSUB 1900 - this is game ending - need to change it a bit though
		}
	}
	/*
	 * 
	 */
}

/* 13 November 2024 - Created File. Added code to move player
 * 14 November 2024 - Added code to handle special movement commands
 * 17 November 2024 - completed the movement method
 * 					- Started working on the take method
 * 19 November 2024 - Added the code to increase food & drink
 * 30 November 2024 - Added drop command
 * 1 December 2024 - Added eat & drink functionality
 * 3 December 2024 - Started on the break functionality
 */