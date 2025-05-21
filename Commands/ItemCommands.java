/*
Title: Island of Secrets Command Execution Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 21 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.util.Random;

import Data.Constants;
import Data.Item;
import Data.RawData;
import Game.Game;
import Game.Player;

public class ItemCommands {
	
	private static final int CARRYING = 0;
	private static final int FOREST = 1;
	private static final int CLEARING = 45;
	private static final int ENTRANCE_CHAMBER = 27;
	private static final int ROOM_WITH_HANDS = 28;
	private static final int DESTROYED = 81;
	
	private static final int APPLE = 1;
	private static final int JUG = 4;
	private static final int TORCH = 7;
	private static final int ROPE = 10;
	private static final int STAFF = 20;
	private static final int BEAST = 16;
	private static final int MUSHROOM = 20;
	private static final int WATER = 24;
	private static final int VILLAGER = 30;
	private static final int SWAMPMAN = 32;
	private static final int SNAKE = 40;
	private static final int LOGMEN = 42;
	private static final int SCAVENGER = 42;
	private static final int MEDIAN = 43;
	
	private static final int DROP = 9;
	private static final int PICK = 15;
	private static final int CATCH = 29;
	
	private Random rand = new Random();
	
	public ActionResult validateTake(Game game,int currentRoom, ParsedCommand command) {
		
		int noun = command.getNounNumber();	
		Item item = game.getItem(noun);
		boolean commandSuccessful = true;
		
		if (((item.getItemFlag()>0 && item.getItemFlag()<9) ||
				item.getItemLocation()!=currentRoom) && noun<=Constants.MAX_CARRIABLE_ITEMS) {
			
			if (!extraValidTake(currentRoom, noun)) {
				game.addMessage("What "+item.getItemName()+"?",true,true);
				commandSuccessful = false;
			
			//Validates Pick and Catch commands
			} else if ((command.getVerbNumber() == PICK && noun != APPLE && noun != MUSHROOM) || 
				   (command.getVerbNumber() == CATCH && noun != BEAST)){
				game.addMessage("You can't "+command.getCommand(),true,true);
			}
		}
		
		return new ActionResult(game,commandSuccessful);
	}
	
	public ActionResult validateCarrying(Game game,ParsedCommand command) {
		
		int noun = command.getNounNumber();
		boolean validCommand = true;
		
		if (game.getItem(noun).getItemLocation()!=CARRYING || noun>=Constants.FOOD_THRESHOLD) {
			game.addMessage("I don't have that. Sorry.",true,true);
			validCommand = false;
		}
		
		//Give specific validations
		if (command.checkGive()) {
			
			if(noun == WATER) {
				validCommand = true;
			}			
		}
		
		return new ActionResult(game,validCommand);
	}
	
	public ActionResult validateGive(Game game, int playerRoom, ParsedCommand command) {
		
		String[] commands = command.getSplitFullCommand();
		boolean validCommand = true;
		
		if (commands.length<3) {
			game.addMessage("Give to whom?",true,true);
			game.setGiveState();
			validCommand = false;
		} else if (commands[2].equals("to") && commands.length<4) {
			game.addMessage("I don't understand",true,true);
			validCommand  = false;
		
		//Validates the reciever
		} else {
			
			String object = getObject(commands);
			int objectNumber = getNounNumber(object);

			if (objectNumber == -1) {
				game.addMessage("I do not see the "+object+" here!", true, true);
				validCommand = false;
			} else if (playerRoom != game.getItem(objectNumber).getItemLocation()) {
				game.addMessage("The "+object+" is not here.",true,true);
			}
		}
		
		return new ActionResult(game,validCommand);
	}
	
	//Determines the receiver of the noun
	private String getObject(String[] commands) {
		
		String object = commands[3];
		
		if (object.equals("to")) {
			object = commands[4];
		}
		return object;
	}
	
	private int getNounNumber(String object) {
		
		int nounCount = 0;
		int nounNumber = -1;
		for (String command:RawData.getNouns()) {
			nounCount ++;
							
			if (object.equals(command)) {
				nounNumber = nounCount;
			}
		}
		return nounNumber;
	}
	
	private boolean extraValidTake(int currentRoom,int noun) {
		
		boolean valid = false;
		
		if ((currentRoom==CLEARING && noun==APPLE) || (currentRoom==ENTRANCE_CHAMBER && noun==TORCH)) {
			valid=true;
		}
		
		return valid;
	}
	
	public ActionResult executeCommand(Game game,Player player, ParsedCommand command) {
		
		ActionResult result = new ActionResult();
		
		if (command.checkTake()) {
			result = executeTakeCommand(game,player,command);
		} else if (command.checkDrop()) {
			result = executeDropCommand(game,player,command);
		} else if (command.checkGive()) {
			
		}
		
		return result;
	}
		
	public ActionResult executeTakeCommand(Game game,Player player,ParsedCommand command) {
		
		ActionResult result = specialItemsTakeResponse(game,player,command);
		int noun = command.getNounNumber();
		String codedCommand = command.getCodedCommand();
		
		if (!result.getValid()) {
			result = specialResponseValidTake(game,player,command);
			
			if (!result.getValid()) {
				game.getItem(noun).setItemLocation(0);
				
				if (noun>Constants.FOOD_THRESHOLD && noun<Constants.DRINK_THRESHOLD) {
					player.setStat("food",((int) player.getStat("food"))+2);
					game.getItem(noun).setItemLocation(-18);
				} else 	if (noun>=Constants.DRINK_THRESHOLD && noun<Constants.MAX_CARRIABLE_ITEMS) {
					player.setStat("drink",((int) player.getStat("drink"))+2);
					game.getItem(noun).setItemLocation(-18);
				}
				
				//Omegan's Cloak
				if (codedCommand.equals("3810010")) {
					
					//Add special lightning Flashes screen
					game.addMessage("Lightning Flashes",true,true);
					
					game.getItem(39).setItemLocation(player.getRoom());
					player.setStat("wisdom",(int) player.getStat("wisdom")-2);
					player.setStat("strength",(float) player.getStat("strength")-8);
					game.setLightingGameState();
				} else if (codedCommand.equals("246046") && game.getItem(STAFF).getItemLocation() == CARRYING) {
					game.addMessage("You use the staff to keep the Dactyl away and take the egg",true,true);	
				} else {
					game.addMessage("Taken",true,true);
				}
				
				//Makes sure that wisdom increase only happens once
				if (!game.getItem(noun).hasWisdonAcquired()) {
					player.setStat("wisdom",(int) player.getStat("wisdom")+4);
					game.getItem(noun).setWisdomAcquired(true);
				}
				
				player.setStat("weight",((int) player.getStat("weight"))+1);
				
				if (game.getItem(noun).getItemFlag()>1) {
					game.getItem(noun).setItemFlag(0);
				}
				
				result = new ActionResult(game,player);
			}
		}
		
		return result;
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
		
		boolean commandActioned = false;
		String commandCode = command.getCodedCommand();
		int noun = command.getNounNumber();
		
		//Evil books in library
		if (commandCode.equals("3450050")) {
			player.setStat("wisdom",(int) player.getStat("wisdom")-5);
			player.setStat("strength",(float) player.getStat("strength")-8);
			game.addMessage("They are cursed",true,true);
			commandActioned = true;
		
		//Attempting to take the beast
		} else if (noun == BEAST && game.getItem(ROPE).getItemLocation()!=CARRYING) {
			game.getItem(noun).setItemLocation(player.getRoom());
			game.addMessage("It escaped",true,true);
			commandActioned = true;
			
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
				commandActioned = true;
		}
		
		return new ActionResult(game,player,commandActioned);
	}
	
	private ActionResult executeDropCommand(Game game, Player player, ParsedCommand command) {
		
		int noun = command.getNounNumber();
		
		game.getItem(noun).setItemLocation(player.getRoom());
		player.setStat("weight",((int) player.getStat("weight"))-1);
		game.addMessage("Done",true,true);
		
		ActionResult result = specialDropResults(game,player,command);
		
		return result;
	}
	
	private ActionResult specialDropResults(Game game,Player player,ParsedCommand command) {
		
		int noun = command.getNounNumber();
		int verb = command.getVerbNumber();
		String codedCommand = command.getCodedCommand();
		
		//Dropping the Earthenware Jug
		if (noun == JUG && game.getItem(noun).getItemLocation()==CARRYING && verb==DROP) {
			game.getItem(noun).setItemLocation(DESTROYED);
			player.setStat("wisdom",(int) player.getStat("wisdom")-1);
			player.setStat("weight",((int) player.getStat("weight"))-1);
			game.addMessage("It breaks!",true,true);
		
		//Dropping a brightly glowing torch
		} else if (codedCommand.substring(0,3).equals("701")) {
						
			game.getItem(noun).setItemLocation(player.getRoom());
			game.addMessage("The torch dims when you drop it.",true,true);	
			game.getItem(TORCH).setItemFlag(0);
			game.getItem(TORCH).setItemName("a flickering torch");
						
			if (player.getRoom()==ROOM_WITH_HANDS) {
				game.addMessage("Upon dropping the torch the arms reach out and grab you, preventing you from moving.",false,true);
			}
		
		//Dropping the beast
		} else if (noun == BEAST) {
			game.getItem(noun).setItemFlag(0);
			game.addMessage("The Canyon Beast runs away", true, true);		}
		
		return new ActionResult(game,player);
	}
	
	public ActionResult executeGive(Game game, Player player, ParsedCommand command) {
		
		String[] commands = command.getSplitFullCommand();
		String object = getObject(commands);
		String codedCommand = command.getCodedCommand();
		int objectNumber = getNounNumber(object);
		int nounNumber = command.getNounNumber();
		game.addMessage("It is refused.",true,true);
		ActionResult result = new ActionResult(game,player);
		
		//Removes the snake from the hut by giving it an apple
		if (codedCommand.equals("10045") && objectNumber==SNAKE) {
			game.getItem(nounNumber).setItemLocation(DESTROYED);
			game.getItem(objectNumber).setItemFlag(1);
			game.addMessage("The snake uncoils",true,true);
			result = new ActionResult(game,player);

		//Giving water to a villager (but must have some drink)
		} else if (codedCommand.equals("2413075") && objectNumber==VILLAGER && ((int) player.getStat("drink"))>1) {
			result = giveWater(game,player);
		} else {

			//Giving items to the ancient scavenger
			if ((codedCommand.substring(0,3).equals("300") || 
					codedCommand.substring(0,3).equals("120")) &&
					objectNumber == SCAVENGER) {
				
				player.setStat("wisdom",(int) player.getStat("wisdom")+10);
				game.getItem(nounNumber).setItemLocation(DESTROYED);
				result = new ActionResult(game,player);
				game.addMessage("It is accepted",true,true);
				
			//Give jug to swampman
			} else if (codedCommand.substring(0,2).equals("40") && 
					   game.getItem(JUG).getItemFlag()<0 && objectNumber == SWAMPMAN) { 
				game.getItem(objectNumber).setItemFlag(1);
				game.getItem(nounNumber).setItemLocation(DESTROYED);
				game.addMessage("The Swampman takes the jug and leaves",true,true);
				result = new ActionResult(game,player);
			
			//Give pebble to Median
			} else if (codedCommand.substring(0,2).equals("80") &&
					   objectNumber == MEDIAN) {
				result = givePebble(game, player,nounNumber);
			
			//Giving to logmen
			} else	if (objectNumber == LOGMEN) {
					game.addMessage("It is taken",true,true);
					game.getItem(nounNumber).setItemLocation(51);
			}
						
		}
		
		return result;
	}
	
	private ActionResult givePebble(Game game, Player player, int nounNumber) {
		
		game.getItem(nounNumber).setItemLocation(DESTROYED);
		game.setMessageGameState();
		game.getItem(8).setItemFlag(-1);

		//Removes Median from Game
		game.getItem(43).setItemLocation(DESTROYED);
		game.getItem(43).setItemFlag(1);
		
		game.addPanelMessage("He takes it ...", true);
		if (player.getRoom()!=8) {
			game.addPanelMessage("runs down the corridor, ...", false);
		} 
		
		game.addPanelMessage("and casts it into the chemical vats, ", false);
		game.addPanelMessage("purifying them with a clear blue light reaching far into the lakes and rivers", false);
		game.addPanelMessage("reaching far into the lakes and rivers beyond.", false);
		game.addMessage("It is accepted",true,true);
		
		return new ActionResult(game,player);
	}


		
	private ActionResult giveWater(Game game, Player player) {
		
		if (game.getItem(11).getItemFlag() != 0) {
			game.addMessage("He drinks the water and offers his staff",true,true);
			game.getItem(30).setItemName("A villager");
		} else {
			game.addMessage("He drinks the water",true,true);
		}
		
		game.getItem(11).setItemFlag(0);
		player.setStat("drink",((int) player.getStat("drink"))-1);
		
		return new ActionResult(game,player);
	}
}

/* 8 May 2025 - Created File
 * 12 May 2025 - Added item take validator
 * 16 May 2025 - Completed the take command
 * 17 May 2025 - Completed the drop command
 * 				 Added validate for give command
 * 19 May 2025 - Validated reciever
 * 			   - Give apple to snake and water to villager
 * 21 May 2025 - Completed the give functions.
 */
