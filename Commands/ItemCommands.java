/*
Title: Island of Secrets Command Execution Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.7
Date: 24 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.util.Random;

import Data.Constants;
import Data.GameEntities;
import Data.Item;
import Data.RawData;
import Game.Game;
import Game.Player;

public class ItemCommands {
		
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
			} else if ((command.getVerbNumber() == GameEntities.CMD_PICK && noun != GameEntities.ITEM_APPLE && noun != GameEntities.ITEM_MUSHROOM) || 
				   (command.getVerbNumber() == GameEntities.CMD_CATCH && noun != GameEntities.ITEM_BEAST)){
				game.addMessage("You can't "+command.getCommand(),true,true);
			}
		}
		
		return new ActionResult(game,commandSuccessful);
	}
	
	public ActionResult validateCarrying(Game game,ParsedCommand command) {
		
		int noun = command.getNounNumber();
		boolean validCommand = true;
		
		if (game.getItem(noun).getItemLocation()!=GameEntities.ROOM_CARRYING || noun>=Constants.FOOD_THRESHOLD) {
			game.addMessage("I don't have that. Sorry.",true,true);
			validCommand = false;
		}
		
		//Give specific validations
		if (command.checkGive()) {
			
			if(noun == GameEntities.ITEM_WATER) {
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
	
	private boolean validateCode(String playerCode, String code) {
		
		boolean codeValid = false;
		
		if (playerCode.equals(code)) {
			codeValid = true;
		}
		
		return codeValid;
	}
	
	private boolean extraValidTake(int currentRoom,int noun) {
		
		boolean valid = false;
		
		if ((currentRoom==GameEntities.ROOM_CLEARING && noun==GameEntities.ITEM_APPLE) || 
			(currentRoom==GameEntities.ROOM_ENTRANCE_CHAMBER && noun==GameEntities.ITEM_TORCH)) {
			valid=true;
		}
		
		return valid;
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
		
	public ActionResult executeTake(Game game,Player player,ParsedCommand command) {
		return new TakeHandler(game,player,command).execute();
	}
	
	public ActionResult executeDrop(Game game, Player player, ParsedCommand command) {
		return new DropHandler(game,player,command).execute();
	}
	
	public ActionResult executeGive(Game game,Player player, ParsedCommand command) {
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
				result = new ActionResult(game,true);
			} else if (isNoTorch()) {
				game.addMessage("There are no more within reach",true,true);
				result = new ActionResult(game,true);
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
			
			boolean isApple = false;
			if (playerRoom == GameEntities.ROOM_CLEARING && game.checkApples() &&
				nounNumber == GameEntities.ITEM_APPLE && game.getItem(nounNumber).getItemLocation()
				!= playerRoom) {
				isApple = true;
			}
			return isApple;
		}
		
		private boolean areNoApples() {
			
			boolean noApples = false;
			
			//checkApples() will be false.
			if (playerRoom == GameEntities.ROOM_CLEARING && nounNumber == GameEntities.ITEM_APPLE &&
				game.getItem(nounNumber).getItemFlag() != playerRoom) {
				noApples = true;
			}
			
			return noApples;
		}
		
		private boolean isNoTorch() {
			
			boolean isNoTorch = false;
			
			if(playerRoom==GameEntities.ROOM_ENTRANCE_CHAMBER && nounNumber == GameEntities.ITEM_TORCH && 
			   game.getItem(nounNumber).getItemLocation() != playerRoom) {
				isNoTorch = true;
			}
			return isNoTorch;
		}
		
		private boolean isFood() {
			
			boolean isFood = false;
			if(nounNumber>Constants.FOOD_THRESHOLD && nounNumber<Constants.DRINK_THRESHOLD) {
				isFood = true;
			}
			return isFood;
		}
		
		private boolean isDrink() {
			
			boolean isDrink = false;
			if(nounNumber>=Constants.DRINK_THRESHOLD && nounNumber<Constants.MAX_CARRIABLE_ITEMS) {
				isDrink = true;
			}
			return isDrink;
		}
		
		private boolean isTakingCloak() {
			
			boolean isTakingCloak = false;
			if(validateCode(codedCommand,GameEntities.CODE_CLOAK)) {
				isTakingCloak = true;
			}
			return isTakingCloak;
		}
		
		private boolean isTakingEgg() {
			boolean isTakingEgg = false;
			if (validateCode(codedCommand,GameEntities.CODE_EGG)) {
				isTakingEgg = true;
			}
			return isTakingEgg;
		}
		
		private boolean isTakingBooks() {
			
			boolean isTakingBooks = false;
			if(validateCode(codedCommand,GameEntities.CODE_EVIL_BOOKS)) {
				isTakingBooks = true;
			}
			return isTakingBooks;
		}
		
		private boolean hasTakeBeastFailed() {
			
			boolean isTakingBeast = false;
			if (nounNumber == GameEntities.ITEM_BEAST && 
				game.getItem(GameEntities.ITEM_ROPE).getItemLocation() !=
				GameEntities.ROOM_CARRYING) {
				isTakingBeast = true;
			}
			return isTakingBeast;
		}
		
		private boolean hasStaff() {
			boolean hasStaff = false;
			if (game.getItem(GameEntities.ITEM_STAFF).getItemLocation() == GameEntities.ROOM_CARRYING){
				hasStaff = true;
			}
			return hasStaff;
		}
		
		private boolean hasWisdomAcquired() {
			
			boolean wisdomAcquired = false;
			if (game.getItem(nounNumber).hasWisdonAcquired()) {
				wisdomAcquired = true;
			}
			return wisdomAcquired;
		}
				
		private ActionResult handleApple() {
			
			player.setStat("food", ((int) player.getStat("food"))+1);
			game.addMessage("You pick an apple from the tree",true,true);
			player.setStat("weight",((int) player.getStat("weight"))+1);
					
			return new ActionResult(game,player);
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
			
			return new ActionResult(game,player);
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
			return new ActionResult(game,player);
		}
		
		private ActionResult takeBooks() {
			
			player.setStat("wisdom",(int) player.getStat("wisdom")-5);
			player.setStat("strength",(float) player.getStat("strength")-8);
			game.addMessage("They are cursed",true,true);

			return new ActionResult(game,player);
		}
		
		private ActionResult takeBeastFailed() {
			game.addMessage("It escaped",true,true);
			return new ActionResult(game,player);
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
			
			return new ActionResult(game,player);
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
			ActionResult result = new ActionResult(game,player);
			
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
			if (validateCode(codedCommand.substring(0,3),GameEntities.CODE_TORCH)) {
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
			return new ActionResult(game,player);
		}
				
		private ActionResult dropTorch() {
			game.getItem(nounNumber).setItemLocation(player.getRoom());
			game.addMessage("The torch dims when you drop it.",true,true);	
			game.getItem(GameEntities.ITEM_TORCH).setItemFlag(0);
			game.getItem(GameEntities.ITEM_TORCH).setItemName("a flickering torch");
						
			if (player.getRoom()==GameEntities.ROOM_WITH_HANDS) {
				game.addMessage("Upon dropping the torch the arms reach out and grab you, preventing you from moving.",false,true);
			}
			return new ActionResult(game,player);
		}
		
		private ActionResult releaseBeast() {
			game.getItem(nounNumber).setItemFlag(0);
			game.getItem(16).setItemLocation(GameEntities.ROOM_FOREST);
			game.addMessage("The Canyon Beast runs away", true, true);
			return new ActionResult(game,player);
		}
	}
	
	private class GiveHandler {
		
		private final Game game;
		private final Player player;
		private final int nounNumber;
		private final int verbNumber;
		private final int playerRoom;
		private final int objectNumber;
		private final String codedCommand;
		private final String[] commands;
		private final String object;
		
		
		public GiveHandler(Game game, Player player, ParsedCommand command) {
			this.game = game;
			this.player = player;
			this.nounNumber = command.getNounNumber();
			this.verbNumber = command.getVerbNumber();
			this.playerRoom = player.getRoom();
			this.codedCommand = command.getCodedCommand();
			this.commands = command.getSplitFullCommand();
			this.object = getObject(commands);
			this.objectNumber = getNounNumber(object);
			
		}
		
		private ActionResult execute() {
			
			game.addMessage("It is refused.",true,true);
			ActionResult result = new ActionResult(game,player);
			
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
			return true;
		}
				
		private boolean isLogmen() {
			return true;
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
			return isMedian;
		}
		
		private ActionResult giveToSnake() {
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.getItem(objectNumber).setItemFlag(1);
			game.addMessage("The snake uncoils",true,true);
			return new ActionResult(game,player);
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
			return new ActionResult(game,player);
		}
		
		
		private ActionResult giveToSwampman() {
			return new ActionResult(game,player);
		}
				
		private ActionResult giveToLogmen() {
			return new ActionResult(game,player);
		}
		
		private ActionResult giveToScavenger() {
			player.setStat("wisdom",(int) player.getStat("wisdom")+10);
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.addMessage("It is accepted",true,true);
			return new ActionResult(game,player);
		}
				
		private ActionResult giveToMedian() {
			
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.setMessageGameState();
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
			
			return new ActionResult(game,player);
		}
	}
		
	public ActionResult executeGiveCommand(Game game, Player player, ParsedCommand command) {
		

		

		
		
		if () {
			

		
		} else if () {
			
		} else {

			
			if () {
				

				
			
			} else if (codedCommand.substring(0,2).equals("40") && 
					   game.getItem(GameEntities.ITEM_JUG).getItemFlag()<0 && objectNumber == GameEntities.ITEM_SWAMPMAN) { 
				game.getItem(objectNumber).setItemFlag(1);
				game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
				game.addMessage("The Swampman takes the jug and leaves",true,true);
				result = new ActionResult(game,player);
			
			
			} else if () {
				result = givePebble(game, player,nounNumber);
			
			//Giving to logmen
			} else	if (objectNumber == GameEntities.ITEM_LOGMEN) {
					game.addMessage("It is taken",true,true);
					game.getItem(nounNumber).setItemLocation(51);
			}
		}
		return result;
	}
	
	private ActionResult givePebble(Game game, Player player, int nounNumber) {
		

		
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
 * 			   - Moved constants to separate file.
 * 			   - Referenced constants in Game Entities file.
 * 22 May 2025 - Created private class to handle take actions
 * 23 May 2025 - Removed validation (not necessary).
 * 			   - Added no apples and no torch responses
 * 24 May 2025 - Added take food & drink. Added finalise take
 * 			   - Completed take command
 * 			   - Added drop command
 * 			   - Started give command
 * 25 May 2025 - Added Give to Snake and Give to Villager
 */
