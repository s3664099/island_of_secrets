/*
Title: Island of Secrets Command Execution Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.13
Date: 2 September 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package commands;

import java.util.Random;

import command_process.ActionResult;
import command_process.ParsedCommand;
import data.Constants;
import data.GameEntities;
import data.Item;
import data.RawData;
import game.Game;
import game.Player;

public class ItemCommands {
		
	private Random rand = new Random();
	
	public ActionResult validateTake(Game game,Player player, ParsedCommand command) {
		
		int noun = command.getNounNumber();	
		int currentRoom = player.getRoom();
		Item item = game.getItem(noun);
		ActionResult result = new ActionResult(game,player,true);
		
		if (((flaggedUntakeable(item)) || itemNotInRoom(item,currentRoom)) && 
			(noun<=Constants.MAX_CARRIABLE_ITEMS || noun == GameEntities.ITEM_CLOAK)) {
			if (!extraValidTake(currentRoom, noun)) {
				game.addMessage("What "+item.getItemName()+"?",true,true);
				result = result.failure(game, player);
			}
		//Validates Pick and Catch commands
		} else if ((notValidPick(command)) || (notValidCatch(command))){
				game.addMessage("You can't "+command.getCommand(),true,true);
				result = result.failure(game, player);
		} else if (noun>=Constants.MAX_CARRIABLE_ITEMS && noun != GameEntities.ITEM_CLOAK) {
			game.addMessage("I can't take the "+command.getSplitTwoCommand()[1], true, true);
			result = result.failure(game, player);
		}
		
		return result;
	}
	
	private boolean flaggedUntakeable(Item item) {
		return item.getItemFlag()>0 && item.getItemFlag()<9;
	}
	
	private boolean itemNotInRoom(Item item,int currentRoom) {
		return item.getItemLocation()!=currentRoom;
	}
	
	private boolean notValidPick(ParsedCommand command) {
		int noun = command.getNounNumber();
		return command.getVerbNumber() == GameEntities.CMD_PICK && 
				noun != GameEntities.ITEM_APPLE && 
				noun != GameEntities.ITEM_MUSHROOM;
	}
	
	private boolean notValidCatch(ParsedCommand command) {
		int noun = command.getNounNumber();
		return command.getVerbNumber() == GameEntities.CMD_CATCH && noun != GameEntities.ITEM_BEAST;
	}
	
	public ActionResult validateCarrying(Game game,Player player,ParsedCommand command) {
		
		int noun = command.getNounNumber();
		ActionResult result = new ActionResult(game,player,true);
		
		if (game.getItem(noun).getItemLocation()!=GameEntities.ROOM_CARRYING || 
			(noun>=Constants.FOOD_THRESHOLD && noun != 38)) {
			game.addMessage("I don't have that. Sorry.",true,true);
			result = result.failure(game, player);
		}
		
		//Give specific validations
		if (command.checkGive()) {
			if(noun == GameEntities.ITEM_WATER) {
				result = result.failure(game, player);
			}			
		}
		
		return result;
	}
	
	public ActionResult validateGive(Game game, Player player, ParsedCommand command) {
		
		int playerRoom = player.getRoom();
		String[] commands = command.getSplitFullCommand();
		ActionResult result = new ActionResult(game,player,true);
		
		if (commands.length<3) {
			game.addMessage("Give to whom?",true,true);
			game.setGiveState(command.getSplitFullCommand()[1]);
			result = result.failure(game, player);
		} else if (commands[2].equals("to") && commands.length<4) {
			game.addMessage("I don't understand",true,true);
			result = result.failure(game, player);
		
		//Validates the reciever
		} else {
			
			String object = getObject(commands);
			int objectNumber = getNounNumber(object);

			if (objectNumber == -1) {
				game.addMessage("I do not see the "+object+" here!", true, true);
				result = result.failure(game, player);
			} else if (playerRoom != game.getItem(objectNumber).getItemLocation()) {
				game.addMessage("The "+object+" is not here.",true,true);
				result = result.failure(game, player);
			}
		}
		
		return result;
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
	
	private boolean validateCode(String playerCode, String code) {		
		return playerCode.equals(code);
	}
	
	private boolean extraValidTake(int currentRoom,int noun) {		
		return (currentRoom==GameEntities.ROOM_CLEARING && noun==GameEntities.ITEM_APPLE) || 
				(currentRoom==GameEntities.ROOM_ENTRANCE_CHAMBER && noun==GameEntities.ITEM_TORCH);
	}
	
	public ActionResult executeCommand(Game game,Player player, ParsedCommand command) {
		
		ActionResult result = new ActionResult();
		
		if (command.checkTake()) {
			result = executeTake(game,player,command);
		} else if (command.checkDrop()) {
			result = executeDrop(game,player,command);
		} else if (command.checkGive()) {
			result = executeGive(game,player,command);
		}
		
		return result;
	}
		
	private ActionResult executeTake(Game game,Player player,ParsedCommand command) {
		return new TakeHandler(game,player,command).execute();
	}
	
	private ActionResult executeDrop(Game game, Player player, ParsedCommand command) {
		return new DropHandler(game,player,command).execute();
	}
	
	private ActionResult executeGive(Game game,Player player, ParsedCommand command) {
		return new GiveHandler(game,player,command).execute();
	}
	
	private class TakeHandler {
		private final Game game;
		private final Player player;
		private final int nounNumber;
		private final int playerRoom;
		private final String codedCommand;
		
		public TakeHandler(Game game, Player player, ParsedCommand command) {
			this.game = game;
			this.player = player;
			nounNumber = command.getNounNumber();
			playerRoom = player.getRoom();
			codedCommand = command.getCodedCommand();
		}
		
		public ActionResult execute() {
			
			ActionResult result = new ActionResult();
			if (areApples()) {
				result = handleApple();
			} else if (areNoApples()) {
				game.addMessage("There are no more apples within reach",true,true);
				result = new ActionResult(game,player,true);
			} else if (isNoTorch()) {
				game.addMessage("There are no more within reach",true,true);
				result = new ActionResult(game,player,true);
			} else if (isFood()) {
				result = takeSustanence("food") ;
			} else if (isDrink()) {
				result = takeSustanence("drink");
			} else if (isTakingCloak()) {
				result = takeCloak();
			} else if (isTakingEgg()) {
				result = takeEgg();
			} else if (isTakingBooks()) {
				result = takeBooks();
			} else if (hasTakeBeastFailed()) {
				result = takeBeastFailed();
			} else {
				result = finaliseTake(game,player,nounNumber,false);
			}
			return result;
		}
		
		private boolean areApples() {
			return playerRoom == GameEntities.ROOM_CLEARING && game.checkApples() &&
					nounNumber == GameEntities.ITEM_APPLE && game.getItem(nounNumber).getItemLocation()
					!= playerRoom;
		}
		
		private boolean areNoApples() {
			return playerRoom == GameEntities.ROOM_CLEARING && nounNumber == GameEntities.ITEM_APPLE &&
					game.getItem(nounNumber).getItemLocation() != playerRoom;
		}
		
		private boolean isNoTorch() {
			return playerRoom==GameEntities.ROOM_ENTRANCE_CHAMBER && nounNumber == GameEntities.ITEM_TORCH && 
					game.getItem(nounNumber).getItemLocation() != playerRoom;
		}
		
		private boolean isFood() {
			return nounNumber>Constants.FOOD_THRESHOLD && nounNumber<Constants.DRINK_THRESHOLD;
		}
		
		private boolean isDrink() {
			return nounNumber>=Constants.DRINK_THRESHOLD && nounNumber<Constants.MAX_CARRIABLE_ITEMS;
		}
		
		private boolean isTakingCloak() {
			return validateCode(codedCommand,GameEntities.CODE_CLOAK);
		}
		
		private boolean isTakingEgg() {
			return validateCode(codedCommand,GameEntities.CODE_EGG);
		}
		
		private boolean isTakingBooks() {
			return validateCode(codedCommand,GameEntities.CODE_EVIL_BOOKS);
		}
		
		private boolean hasTakeBeastFailed() {
			return nounNumber == GameEntities.ITEM_BEAST && 
					game.getItem(GameEntities.ITEM_ROPE).getItemLocation() !=
					GameEntities.ROOM_CARRYING;
		}
		
		private boolean hasStaff() {
			return game.getItem(GameEntities.ITEM_STAFF).getItemLocation() == GameEntities.ROOM_CARRYING;
		}
		
		private boolean hasWisdomAcquired() {
			return game.getItem(nounNumber).hasWisdonAcquired();
		}
				
		private ActionResult handleApple() {
			
			player.setStat("food", ((int) player.getStat("food"))+1);
			game.addMessage("You pick an apple from the tree",true,true);
			player.setStat("weight",((int) player.getStat("weight"))+1);
					
			return new ActionResult(game,player,true);
		}
		
		private ActionResult takeSustanence(String sustanence) {
			
			player.setStat(sustanence,((int) player.getStat(sustanence))+2);
			game.getItem(nounNumber).setItemLocation(-18);
			
			return finaliseTake(game, player, nounNumber, true);
		}
		
		private ActionResult takeCloak() {
			
			game.addMessage("Lightning Flashes",true,true);
			game.getItem(39).setItemLocation(player.getRoom());
			game.getItem(nounNumber).setItemLocation(0);
			player.setStat("wisdom",(int) player.getStat("wisdom")-2);
			player.setStat("strength",(float) player.getStat("strength")-8);
			game.setLightingGameState();
			
			return new ActionResult(game,player,true);
		}
		
		private ActionResult takeEgg() {
			
			Game game = this.game;
			Player player = this.player;
			
			if (hasStaff()) {
				ActionResult result = finaliseTake(game,player,nounNumber,false);
				game = result.getGame();
				player = result.getPlayer();
				game.addMessage("You use the staff to keep the Dactyl away and take the egg",true,true);
			} else {
				game.addMessage("You anger the bird",true,true);
				
				//One in three bird takes you to random spot & replaces wild canyon beast
				//Needed beast to actually get here
				if (rand.nextInt(3)>1) {
					game.addMessage(" which flies you to a remote place.",false,true);
					player.setRoom(63+rand.nextInt(6));
					game.getItem(16).setItemLocation(GameEntities.ROOM_FOREST);
					game.getItem(16).setItemFlag(0);
				}
			}
			return new ActionResult(game,player,true);
		}
		
		private ActionResult takeBooks() {
			
			player.setStat("wisdom",(int) player.getStat("wisdom")-5);
			player.setStat("strength",(float) player.getStat("strength")-8);
			game.addMessage("They are cursed",true,true);

			return new ActionResult(game,player,true);
		}
		
		private ActionResult takeBeastFailed() {
			game.addMessage("It escaped",true,true);
			return new ActionResult(game,player,true);
		}
		
		private ActionResult finaliseTake(Game game, Player player, int nounNumber, boolean taken) {

			if (!taken) {
				game.getItem(nounNumber).setItemLocation(0);
				
				if (game.getItem(nounNumber).getItemFlag()>1) {
					game.getItem(nounNumber).setItemFlag(0);
				}
			}
			
			if (!hasWisdomAcquired()) {
				player.setStat("wisdom",(int) player.getStat("wisdom")+4);
				game.getItem(nounNumber).setWisdomAcquired(true);
			}
			
			player.setStat("weight",((int) player.getStat("weight"))+1);
			game.addMessage("Taken",true,true);
			
			return new ActionResult(game,player,true);
		}
	}
	
	private class DropHandler {
		private final Game game;
		private final Player player;
		private final int nounNumber;
		private final int verbNumber;
		private final int playerRoom;
		private final String codedCommand;
		
		public DropHandler(Game game, Player player, ParsedCommand command) {
			this.game = game;
			this.player = player;
			this.nounNumber = command.getNounNumber();
			this.verbNumber = command.getVerbNumber();
			this.playerRoom = player.getRoom();
			this.codedCommand = command.getCodedCommand();
		}
		
		public ActionResult execute() {
			
			game.getItem(nounNumber).setItemLocation(playerRoom);
			player.setStat("weight",((int) player.getStat("weight"))-1);
			game.addMessage("Done",true,true);
			ActionResult result = new ActionResult(game,player,true);
			
			if(isJug()) {
				result = dropJug();
			} else if (isTorch()) {
				result = dropTorch();
			} else if (isBeast()) {
				result = releaseBeast();
			}
			
			return result;
		}
		
		private boolean isJug() {
			
			boolean isJug = false;
			if (nounNumber == GameEntities.ITEM_JUG && 
				game.getItem(nounNumber).getItemLocation()==GameEntities.ROOM_CARRYING && 
				verbNumber==GameEntities.CMD_DROP) {
				isJug = true;
			}
			return isJug;
		}
		
		private boolean isTorch() {
			
			boolean isTorch = false;
			if (validateCode(codedCommand.substring(0,3),GameEntities.CODE_TORCH_BRIGHT)) {
				isTorch = true;
			}
			return isTorch;
		}
		
		private boolean isBeast() {
			boolean isBeast = false;
			if (nounNumber == GameEntities.ITEM_BEAST) {
				isBeast = true;
			}
			return isBeast;
		}
		
		private ActionResult dropJug() {
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			player.setStat("wisdom",(int) player.getStat("wisdom")-1);
			player.setStat("weight",((int) player.getStat("weight"))-1);
			game.addMessage("It breaks!",true,true);
			return new ActionResult(game,player,true);
		}
				
		private ActionResult dropTorch() {
			game.getItem(nounNumber).setItemLocation(player.getRoom());
			game.addMessage("The torch dims when you drop it.",true,true);	
			game.getItem(GameEntities.ITEM_TORCH).setItemFlag(0);
			game.getItem(GameEntities.ITEM_TORCH).setItemName("a flickering torch");
						
			if (player.getRoom()==GameEntities.ROOM_WITH_HANDS) {
				game.addMessage("Upon dropping the torch the arms reach out and grab you, preventing you from moving.",false,true);
			}
			return new ActionResult(game,player,true);
		}
		
		private ActionResult releaseBeast() {
			game.getItem(nounNumber).setItemFlag(0);
			game.getItem(16).setItemLocation(GameEntities.ROOM_FOREST);
			game.addMessage("The Canyon Beast runs away", true, true);
			return new ActionResult(game,player,true);
		}
	}
	
	private class GiveHandler {
		
		private final Game game;
		private final Player player;
		private final int nounNumber;
		private final int objectNumber;
		private final String codedCommand;
		private final String[] commands;
		private final String object;
		
		
		public GiveHandler(Game game, Player player, ParsedCommand command) {
			this.game = game;
			this.player = player;
			this.nounNumber = command.getNounNumber();
			this.codedCommand = command.getCodedCommand();
			this.commands = command.getSplitFullCommand();
			this.object = getObject(commands);
			this.objectNumber = getNounNumber(object);
			
		}
		
		private ActionResult execute() {
			
			game.addMessage("It is refused.",true,true);
			ActionResult result = new ActionResult(game,player,true);
			
			//Removes the snake from the hut by giving it an apple
			if(isSnake()) {
				result = giveToSnake();
			
			//Giving water to a villager (but must have some drink)
			} else if (isVillager()) {
				result = giveToVillager();
			
			//Give jug to swampman
			} else if (isSwampman()) {
				result = giveToSwampman();
			
			//Giving to logmen
			} else if (isLogmen()) {
				result = giveToLogmen();
			
			//Giving items to the ancient scavenger
			} else if (isScavenger()) {
				result = giveToScavenger();

			//Give pebble to Median
			} else if (isMedian()) {
				System.out.println("Dude");
				result = giveToMedian();
			}
			
			return result;
		}
		
		private boolean isSnake() {
			boolean isSnake = false;
			if (validateCode(codedCommand,GameEntities.CODE_SNAKE) && objectNumber==GameEntities.ITEM_SNAKE) {
				isSnake = true;
			}
			return isSnake;
		}
		
		private boolean isVillager() {
			boolean isVillager = false;
			if (validateCode(codedCommand,GameEntities.CODE_VILLAGER) && objectNumber==GameEntities.ITEM_VILLAGER && 
				((int) player.getStat("drink"))>1) {
				isVillager = true;
			}
			return isVillager;
		}
		
		private boolean isSwampman() {
			boolean isSwampman = false;
			if (validateCode(codedCommand.substring(0,2),GameEntities.CODE_JUG) &&
				game.getItem(GameEntities.ITEM_JUG).getItemFlag()<0 &&
				objectNumber == GameEntities.ITEM_SWAMPMAN) {
				isSwampman = true;
			}
			return isSwampman;
		}
				
		private boolean isLogmen() {
			boolean isLogmen = false;
			if(objectNumber == GameEntities.ITEM_LOGMEN) {
				isLogmen = true;
			}
			return isLogmen;
		}
		
		private boolean isScavenger() {
			boolean isScavenger = false;
			if ((validateCode(codedCommand.substring(0,3),GameEntities.CODE_LILY) || 
				validateCode(codedCommand.substring(0,3),GameEntities.CODE_CHIP)) &&
					objectNumber == GameEntities.ITEM_SCAVENGER) {
				isScavenger = true;
			}
			return isScavenger;
		}
		
		private boolean isMedian() {
			boolean isMedian = false;
			if(validateCode(codedCommand.substring(0,2),GameEntities.CODE_PEBBLE) &&
			   objectNumber == GameEntities.ITEM_MEDIAN) {
				isMedian = true;
			}
			System.out.println("Result "+isMedian);
			return isMedian;
		}
		
		private ActionResult giveToSnake() {
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.getItem(objectNumber).setItemFlag(1);
			game.addMessage("The snake uncoils",true,true);
			return new ActionResult(game,player,true);
		}
		
		private ActionResult giveToVillager() {
			if (game.getItem(11).getItemFlag() != 0) {
				game.addMessage("He drinks the water and offers his staff",true,true);
				game.getItem(30).setItemName("A villager");
			} else {
				game.addMessage("He drinks the water",true,true);
			}
			game.getItem(11).setItemFlag(0);
			player.setStat("drink",((int) player.getStat("drink"))-1);
			return new ActionResult(game,player,true);
		}
		
		
		private ActionResult giveToSwampman() {
			game.getItem(objectNumber).setItemFlag(1);
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.addMessage("The Swampman takes the jug and leaves",true,true);
			return new ActionResult(game,player,true);
		}
				
		private ActionResult giveToLogmen() {
			game.addMessage("It is taken",true,true);
			game.getItem(nounNumber).setItemLocation(51);
			return new ActionResult(game,player,true);
		}
		
		private ActionResult giveToScavenger() {
			player.setStat("wisdom",(int) player.getStat("wisdom")+10);
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.addMessage("It is accepted",true,true);
			return new ActionResult(game,player,true);
		}
				
		private ActionResult giveToMedian() {
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.getItem(8).setItemFlag(-1);

			//Removes Median from Game
			game.getItem(43).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.getItem(43).setItemFlag(1);
			
			game.addPanelMessage("He takes it ...", true);
			if (player.getRoom()!=8) {
				game.addPanelMessage("runs down the corridor, ...", false);
			} 
			
			game.addPanelMessage("and casts it into the chemical vats, ", false);
			game.addPanelMessage("purifying them with a clear blue light reaching far into the lakes and rivers", false);
			game.addPanelMessage("reaching far into the lakes and rivers beyond.", false);
			game.addMessage("It is accepted",true,true);
			game.setMessageGameState();
			return new ActionResult(game,player,true);
		}
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
 * 			   - Moved constants to separate file.
 * 			   - Referenced constants in Game Entities file.
 * 22 May 2025 - Created private class to handle take actions
 * 23 May 2025 - Removed validation (not necessary).
 * 			   - Added no apples and no torch responses
 * 24 May 2025 - Added take food & drink. Added finalise take
 * 			   - Completed take command
 * 			   - Added drop command
 * 			   - Started give command
 * 25 May 2025 - Added Give to Snake and Give to Villager.
 * 			   - Completed the give functionality
 * 29 June 2025 - Fixed problem with taking the apple.
 * 10 July 2025 - Fixed problem with invalid object still being flagged as taken
 * 				- Fixed validation for take and catch
 * 15 July 2025 - Enabled cloak to be taken and dropped
 * 26 July 2025 - Added setMessageGameState
 * 2 September 2025 - Updated based on new ActionResult
 */
