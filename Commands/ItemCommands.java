/*
Title: Island of Secrets Command Execution Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 8 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.util.Random;

import Data.Constants;
import Data.Item;
import Game.Game;
import Game.Player;

public class ItemCommands {
	
	private static final int CARRYING = 0;
	private static final int FOREST = 1;
	private static final int CLEARING = 45;
	private static final int ENTRANCE_CHAMBER = 27;
	
	private static final int APPLE = 1;
	private static final int TORCH = 7;
	private static final int ROPE = 10;
	private static final int STAFF = 20;
	private static final int BEAST = 16;
	private static final int MUSHROOM = 20;
	
	private static final int PICK = 15;
	private static final int CATCH = 29;
	
	private Random rand = new Random();
	
	public ActionResult validateTake(Game game,int currentRoom, ParsedCommand command) {
		
		int noun = command.getNounNumber();	
		Item item = game.getItem(noun);
		ActionResult result = new ActionResult();
		
		if (((item.getItemFlag()>0 && item.getItemFlag()<9) ||
				item.getItemLocation()!=currentRoom) && noun<=Constants.MAX_CARRIABLE_ITEMS) {
			
			if (!extraValidTake(currentRoom, noun)) {
				game.addMessage("What "+item.getItemName()+"?",true,true);
				result = new ActionResult(game,false);
			}
		
		//Validates Pick and Catch commands
		} else if ((command.getVerbNumber() == PICK && noun != APPLE && noun != MUSHROOM) || 
				   (command.getVerbNumber() == CATCH && noun != BEAST)){
			game.addMessage("You can't "+command.getCommand(),true,true);
		}
		
		return result;
	}
	
	private boolean extraValidTake(int currentRoom,int noun) {
		
		boolean valid = false;
		
		if ((currentRoom==CLEARING && noun==APPLE) || (currentRoom==ENTRANCE_CHAMBER && noun==TORCH)) {
			valid=true;
		}
		
		return valid;
	}
		
	public ActionResult executeCommand(Game game,Player player,ParsedCommand command) {
		
		ActionResult result = specialItemsTakeResponse(game,player,command);
		
		if (!result.getValid()) {
			result = specialResponseValidTake(game,player,command);
			
			if (!result.getValid()) {
				
			}
		}
		
		return new ActionResult();
	}
	
	public ActionResult specialItemsTakeResponse(Game game,Player player, ParsedCommand command) {
		
		boolean commandActioned = false;
		int currentRoom = player.getRoom();
		int noun = command.getNounNumber();
		
		if (currentRoom == CLEARING && game.checkApples() && noun == APPLE && game.getItem(noun).getItemLocation() != currentRoom) {
			player.setStat("food", ((int) player.getStat("food"))+1);
			game.addMessage("You pick an apple from the tree",true,true);
			player.setStat("weight",((int) player.getStat("weight"))+1);
			commandActioned = true;
		} else if (currentRoom == CLEARING && noun == APPLE && game.getItem(noun).getItemLocation() != currentRoom) {
			game.addMessage("There are no more apples within reach",true,true);
			commandActioned = true;
		} else if (currentRoom==ENTRANCE_CHAMBER && noun == TORCH && game.getItem(noun).getItemLocation() != currentRoom) {
			game.addMessage("There are no more within reach",true,true);
			commandActioned = true;
		}
		
		return new ActionResult(game,player,commandActioned);
	}
	
	public ActionResult specialResponseValidTake(Game game, Player player, ParsedCommand command) {
		
		boolean commandExecuted = false;
		String commandCode = command.getCodedCommand();
		int noun = command.getNounNumber();
		
		//Evil books in library
		if (commandCode.equals("3450050")) {
			player.setStat("wisdom",(int) player.getStat("wisdom")-5);
			player.setStat("strength",(float) player.getStat("strength")-8);
			game.addMessage("They are cursed",true,true);
			commandExecuted = true;
		
		//Attempting to take the beast
		} else if (noun == BEAST && game.getItem(ROPE).getItemLocation()!=CARRYING) {
			game.getItem(noun).setItemLocation(player.getRoom());
			game.addMessage("It escaped",true,true);
			commandExecuted = true;
			
			//Handles the bird when attempting to take the egg without the staff
		} else if (commandCode.equals("246046") && game.getItem(STAFF).getItemLocation() != CARRYING) {

				game.addMessage("You anger the bird",true,true);
				game.getItem(noun).setItemLocation(player.getRoom());
				
				//One in three bird takes you to random spot & replaces wild canyon beast
				//Needed beast to actually get here
				if (rand.nextInt(3)>1) {
					game.addMessage(" which flies you to a remote place.",false,true);
					player.setRoom(63+rand.nextInt(6));
					game.getItem(16).setItemLocation(FOREST);
					game.getItem(16).setItemFlag(0);
				}
				commandExecuted = true;
		}
		
		return new ActionResult(game,player,commandExecuted);
	}

	/*
	 * Give
	 * if three words - executeGive
	 * 
	 * 
	 * 	public void take(Game game,Player player) {
				

		} else {
			

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
				

				//2nd - catch canyon beast
				//3rd - noun not an item

					
					int weight = 0;
										
					//picks up item
					if (game.getItem(noun).getItemLocation()== player.getRoom() && (
						game.getItem(noun).getItemFlag()<1 || game.getItem(noun).getItemFlag()==9)
						&& noun<Constants.MAX_CARRIABLE_ITEMS) {
							game.getItem(noun).setItemLocation(0);
							weight = -1;
					}
					

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
					game.setMessageGameState();
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
	 */
	
}

/* 8 May 2025 - Created File
 * 12 May 2025 - Added item take validator
 */
