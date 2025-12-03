/*
Title: Island of Secrets Command Execution Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
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

/**
 * Handles validation and execution of item-related player commands
 * such as take, drop, and give. This class is responsible for ensuring
 * that commands are legal within the current game state, and then
 * applying the appropriate changes to the {@link Game} and {@link Player}.
 */
public class ItemCommands {
		
	private Random rand = new Random();
	
	/**
	 * Validates whether a "take" command is allowed in the given context.
	 *
	 * @param game   the current game state
	 * @param player the player attempting the action
	 * @param command the parsed command to validate
	 * @return an {@link ActionResult} indicating whether the action is valid
	 */
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
		
		} else if ((notValidPick(command)) || (notValidCatch(command))){
				game.addMessage("You can't "+command.getCommand(),true,true);
				result = result.failure(game, player);
		} else if (noun>=Constants.MAX_CARRIABLE_ITEMS && noun != GameEntities.ITEM_CLOAK && noun != GameEntities.ITEM_BOOKS) {
			game.addMessage("I can't take the "+command.getSplitTwoCommand()[1], true, true);
			result = result.failure(game, player);
		}
		
		return result;
	}
	
	/**
	 * Checks if the given item is flagged as untakeable.
	 *
	 * @param item the item to check
	 * @return true if the item cannot be taken, false otherwise
	 */
	private boolean flaggedUntakeable(Item item) {
		return item.getItemFlag()>0 && item.getItemFlag()<9;
	}
	
	/**
	 * Determines if the item is not located in the current room.
	 *
	 * @param item        the item being checked
	 * @param currentRoom the room the player is currently in
	 * @return true if the item is not in the current room
	 */
	private boolean itemNotInRoom(Item item,int currentRoom) {
		return item.getItemLocation()!=currentRoom;
	}
	
	/**
	 * Checks if a PICK command was used on an invalid item.
	 *
	 * @param command the parsed command
	 * @return true if the pick is invalid
	 */
	private boolean notValidPick(ParsedCommand command) {
		int noun = command.getNounNumber();
		return command.getVerbNumber() == GameEntities.CMD_PICK && 
				noun != GameEntities.ITEM_APPLE && 
				noun != GameEntities.ITEM_MUSHROOM;
	}
	
	/**
	 * Checks if a CATCH command was used on an invalid item.
	 *
	 * @param command the parsed command
	 * @return true if the catch is invalid
	 */
	private boolean notValidCatch(ParsedCommand command) {
		int noun = command.getNounNumber();
		return command.getVerbNumber() == GameEntities.CMD_CATCH && noun != GameEntities.ITEM_BEAST;
	}
	
	/**
	 * Validates whether the player is currently carrying the given item.
	 *
	 * @param game    the current game state
	 * @param player  the player attempting the action
	 * @param command the parsed command to validate
	 * @return an {@link ActionResult} indicating whether the action is valid
	 */
	public ActionResult validateCarrying(Game game,Player player,ParsedCommand command) {
		
		int noun = command.getNounNumber();
		ActionResult result = new ActionResult(game,player,true);
				
		if ((noun>Constants.FOOD_THRESHOLD && noun<=Constants.DRINK_THRESHOLD && 
				   (int) player.getStat("food")<1)||(noun>=Constants.DRINK_THRESHOLD &&
				   noun<=Constants.MAX_CARRIABLE_ITEMS && (int) player.getStat("drink")<1)) {
			game.addMessage("I don't have any. Sorry.",true,true);
			result = result.failure(game, player);
		} else if ((game.getItem(noun).getItemLocation()!=GameEntities.ROOM_CARRYING && 
					noun<=Constants.FOOD_THRESHOLD) || (noun>Constants.MAX_CARRIABLE_ITEMS && 
					noun != GameEntities.ITEM_CLOAK)) {
			game.addMessage("I don't have that. Sorry.",true,true);
			result = result.failure(game, player);
		} 		
		return result;
	}
	
	/**
	 * Validates whether a "give" command is allowed in the given context.
	 *
	 * @param game    the current game state
	 * @param player  the player attempting the action
	 * @param command the parsed command to validate
	 * @return an {@link ActionResult} indicating whether the action is valid
	 */
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
		
		//Validates the receiver
		} else {

			String object = getObject(commands);
			int objectNumber = getNounNumber(object);
			if (objectNumber == -1) {
				game.addMessage("I do not see the "+object+" here!", true, true);
				result = result.failure(game, player);
			} else if (playerRoom != game.getItem(objectNumber).getItemLocation()) {
				game.addMessage("The "+object+" is not here.",true,true);
				result = result.failure(game, player);
			} else if (hasNoFood(player,command)) {
				game.addMessage("You have no food", true, true);
				result = result.failure(game, player);
			} else if (hasNoDrink(player,command)) {
				game.addMessage("You have no drink", true, true);
				result = result.failure(game, player);
			}
		}
		
		return result;
	}
	
	/**
	 * Extracts the object of a "give" command (the receiver).
	 *
	 * @param commands the tokenized command array
	 * @return the object name as a string
	 */
	private String getObject(String[] commands) {
		String object = commands[3];
		if (object.equals("to")) {
			object = commands[4];
		}
		return object;
	}
	
	/**
	 * Extracts the subject of a "give" command (the gift).
	 *
	 * @param commands the tokenized command array
	 * @return the subject name as a string
	 */
	private String getNoun(String[] commands) {
		return commands[1];
	}
	
	/**
	 * Looks up the numeric ID of a noun based on its string form.
	 *
	 * @param object the noun string
	 * @return the item ID, or -1 if not found
	 */
	private int getNounNumber(String object) {
		
		int nounCount = 0;
		int nounNumber = -1;
		
		if (isBoatman(object)) {
			object = setBoat();
		}
		
		if (object != null) {
			for (String command:RawData.getNouns()) {
				nounCount ++;
							
				if (object.equals(command)) {
					nounNumber = nounCount;
				}
			}
		}
		return nounNumber;
	}
	
	/**
	 * Compares two codes for equality.
	 *
	 * @param playerCode the player's provided code
	 * @param code       the expected code
	 * @return true if the codes match
	 */
	private boolean validateCode(String playerCode, String code) {		
		return playerCode.equals(code);
	}
	
	/**
	 * Checks for exceptions where items may be validly taken
	 * despite appearing unavailable.
	 *
	 * @param currentRoom the current room of the player
	 * @param noun        the item number
	 * @return true if this is a valid exception
	 */
	private boolean extraValidTake(int currentRoom,int noun) {		
		return (currentRoom==GameEntities.ROOM_CLEARING && noun==GameEntities.ITEM_APPLE) || 
				(currentRoom==GameEntities.ROOM_ENTRANCE_CHAMBER && noun==GameEntities.ITEM_TORCH);
	}
	
	/**
	 * Executes the appropriate command (take, drop, give).
	 *
	 * @param game    the current game state
	 * @param player  the player executing the command
	 * @param command the parsed command
	 * @return an {@link ActionResult} with the result of execution
	 */
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
	
	/**
	 * Executes a take command via the {@link TakeHandler}.
	 */
	private ActionResult executeTake(Game game,Player player,ParsedCommand command) {
		return new TakeHandler(game,player,command).execute();
	}
	
	/**
	 * Executes a drop command via the {@link DropHandler}.
	 */
	private ActionResult executeDrop(Game game, Player player, ParsedCommand command) {
		return new DropHandler(game,player,command).execute();
	}
	
	/**
	 * Executes a give command via the {@link GiveHandler}.
	 */
	private ActionResult executeGive(Game game,Player player, ParsedCommand command) {
		return new GiveHandler(game,player,command).execute();
	}
	
	/**
	 * returns true if player has food, false if not
	 */
	private boolean hasNoFood(Player player, ParsedCommand command) {
		return command.checkNounFood() && (int) player.getStat("food")<1;
	}
	
	/**
	 * returns true if player has drink, false if not
	 */
	private boolean hasNoDrink(Player player, ParsedCommand command) {
		return command.checkNounDrink() && (int) player.getStat("drink")<1;
	}
	
	/**
	 * returns true if player giving to the boatman
	 */
	private boolean isBoatman(String noun) {
		return noun.equals(GameEntities.NOUN_BOATMAN);
	}
	
	/**
	 * returns the string 'boat'
	 */
	private String setBoat() {
		return GameEntities.NOUN_BOAT;
	}
	
	/**
	 * Handles execution of "take" commands. Encapsulates
	 * all logic for picking up items and applying effects.
	 */
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
		
		/**
		 * Executes the "take" command based on current context.
		 *
		 * @return an {@link ActionResult} with the result
		 */
		public ActionResult execute() {

			ActionResult result = new ActionResult();
			if (areApples()) {
				result = takeApple();
			} else if (areNoApples()) {
				game.addMessage("There are no more apples within reach",true,true);
				result = new ActionResult(game,player,true);
			} else if (isNoTorch()) {
				game.addMessage("There are no more within reach",true,true);
				result = new ActionResult(game,player,true);
			} else if (isTakingFood()) {
				result = takeSustanence("food") ;
			} else if (isTakingDrink()) {
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
		
		/** @return true if the player is taking apples under valid conditions */
		private boolean areApples() {
			return playerRoom == GameEntities.ROOM_CLEARING && game.checkApples() &&
					nounNumber == GameEntities.ITEM_APPLE && game.getItem(nounNumber).getItemLocation()
					!= playerRoom;
		}
		
		/** @return true if the player attempts apples but none are present */
		private boolean areNoApples() {
			return playerRoom == GameEntities.ROOM_CLEARING && nounNumber == GameEntities.ITEM_APPLE &&
					game.getItem(nounNumber).getItemLocation() != playerRoom;
		}
		
		/** @return true if no torch is available in the entrance chamber */
		private boolean isNoTorch() {
			return playerRoom==GameEntities.ROOM_ENTRANCE_CHAMBER && nounNumber == GameEntities.ITEM_TORCH && 
					game.getItem(nounNumber).getItemLocation() != playerRoom;
		}
		
		/** @return true if user taking food */
		private boolean isTakingFood() {
			return nounNumber>Constants.FOOD_THRESHOLD && nounNumber<Constants.DRINK_THRESHOLD;
		}
		
		/** @return true if user taking drink */
		private boolean isTakingDrink() {
			return nounNumber>=Constants.DRINK_THRESHOLD && nounNumber<Constants.MAX_CARRIABLE_ITEMS;
		}
		
		/** @return true if user taking cloak */
		private boolean isTakingCloak() {
			return validateCode(codedCommand,GameEntities.CODE_CLOAK);
		}
		
		/** @return true if user taking egg */
		private boolean isTakingEgg() {
			return validateCode(codedCommand,GameEntities.CODE_EGG);
		}
		
		/** @return true if user taking book */
		private boolean isTakingBooks() {
			return validateCode(codedCommand,GameEntities.CODE_EVIL_BOOKS);
		}
		
		/** @return true if user failed to take beast */
		private boolean hasTakeBeastFailed() {
			return nounNumber == GameEntities.ITEM_BEAST && 
					game.getItem(GameEntities.ITEM_ROPE).getItemLocation() !=
					GameEntities.ROOM_CARRYING;
		}
		
		/** @return true if user has the staff */
		private boolean hasStaff() {
			return game.getItem(GameEntities.ITEM_STAFF).getItemLocation() == GameEntities.ROOM_CARRYING;
		}
		
		/** @return true if user has acquired wisdom */
		private boolean hasWisdomAcquired() {
			return game.getItem(nounNumber).hasWisdonAcquired();
		}
		
		/** 
		 * Takes an apple from the tree and adds to the player's food
		 * 
		 * @return an {@link ActionResult} with the result
		 */
		private ActionResult takeApple() {
			
			player.setStat("food", ((int) player.getStat("food"))+1);
			game.addMessage("You pick an apple from the tree",true,true);
			player.setStat("weight",((int) player.getStat("weight"))+1);
					
			return new ActionResult(game,player,true);
		}
		
		/** 
		 * Adds to the players food or drink and removes item from play
		 * 
		 * @return an {@link ActionResult} with the result
		 */
		private ActionResult takeSustanence(String sustanence) {
			
			player.setStat(sustanence,((int) player.getStat(sustanence))+2);
			game.getItem(nounNumber).setItemLocation(-18);
			
			return finaliseTake(game, player, nounNumber, true);
		}
		
		/** 
		 * Takes the cloak and executes the result
		 * 
		 * @return an {@link ActionResult} with the result
		 */
		private ActionResult takeCloak() {
			
			game.addMessage("Lightning Flashes",true,true);
			game.getItem(39).setItemLocation(player.getRoom());
			game.getItem(nounNumber).setItemLocation(0);
			player.setStat("wisdom",(int) player.getStat("wisdom")-2);
			player.setStat("strength",(float) player.getStat("strength")-8);
			game.addPanelMessage("⚡⚡ Lightning Flashes ⚡⚡", true);
			game.setMessageGameState();
			
			return new ActionResult(game,player,true);
		}
		
		/** 
		 * Determines the result of the player taking the Dactyl's Egg
		 * 
		 * @return an {@link ActionResult} with the result
		 */
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
				
				if (rand.nextInt(3)>1) {
					game.addMessage(" which flies you to a remote place.",false,true);
					player.setRoom(GameEntities.ROOM_LOG_BRIDGE+rand.nextInt(6));
					game.getItem(GameEntities.ITEM_BEAST).setItemLocation(GameEntities.ROOM_FOREST);
					game.getItem(GameEntities.ITEM_BEAST).setItemFlag(0);
				}
			}
			return new ActionResult(game,player,true);
		}
		
		/** 
		 * Determines the result of the player taking the books
		 * 
		 * @return an {@link ActionResult} with the result
		 */
		private ActionResult takeBooks() {
			
			player.setStat("wisdom",(int) player.getStat("wisdom")-5);
			player.setStat("strength",(float) player.getStat("strength")-8);
			game.addMessage("They are cursed",true,true);

			return new ActionResult(game,player,true);
		}
		
		/** 
		 * generates the result of the player failing to take the beast
		 * 
		 * @return an {@link ActionResult} with the result
		 */
		private ActionResult takeBeastFailed() {
			game.addMessage("It escaped",true,true);
			return new ActionResult(game,player,true);
		}
		
		/**
		 * Finalizes the take action: updates item state, adjusts player stats,
		 * and posts a confirmation message.
		 *
		 * @param game       the current game
		 * @param player     the current player
		 * @param nounNumber the item being taken
		 * @param taken      whether the item was already marked as taken
		 * @return the resulting {@link ActionResult}
		 */
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
	
	/**
	 * Handles execution of "drop" commands. Encapsulates logic
	 * for placing items into rooms and applying effects.
	 */
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
		/**
		 * Executes the "drop" command.
		 *
		 * @return an {@link ActionResult} with the result
		 */
		public ActionResult execute() {
			
			game.getItem(nounNumber).setItemLocation(playerRoom);
			player.setStat("weight",((int) player.getStat("weight"))-1);
			game.addMessage("Done",true,true);
			ActionResult result = new ActionResult(game,player,true);
			
			if(isJug()) {
				result = dropJug();
			} else if (isTorch()) {
				result = dropTorch();
			} else if (isBeast() || isRopeWithBeast()) {
				result = releaseBeast();
			} 
			
			return result;
		}
		
		/** @return true if the item is a jug being dropped */
		private boolean isJug() {
			return nounNumber == GameEntities.ITEM_JUG && 
					game.getItem(nounNumber).getItemLocation()==GameEntities.ROOM_CARRYING && 
					verbNumber==GameEntities.CMD_DROP;
		}
		
		/** @return true if the item is a torch being dropped */
		private boolean isTorch() {
			return validateCode(codedCommand.substring(0,3),GameEntities.CODE_TORCH_BRIGHT);
		}
		
		/** @return true if the item is the beast being dropped */
		private boolean isBeast() {
			return nounNumber == GameEntities.ITEM_BEAST;
		}
		
		/** @return true if the item is the rope being dropped and the player has the beast*/
		private boolean isRopeWithBeast() {
			return nounNumber == GameEntities.ITEM_ROPE && 
				   game.getItem(GameEntities.ITEM_BEAST).getItemLocation() == GameEntities.ROOM_CARRYING;
		}
		
		/** 
		 * generates the result of the player dropping the torch
		 * 
		 * @return an {@link ActionResult} with the result
		 */
		private ActionResult dropJug() {
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			player.setStat("wisdom",(int) player.getStat("wisdom")-1);
			player.setStat("weight",((int) player.getStat("weight"))-1);
			game.addMessage("It breaks!",true,true);
			return new ActionResult(game,player,true);
		}
		
		/** 
		 * generates the result of the player dropping the torch
		 * 
		 * @return an {@link ActionResult} with the result
		 */
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
		
		/** 
		 * generates the result of the player dropping the beast or dropping the rope while carrying the beast
		 * 
		 * @return an {@link ActionResult} with the result
		 */
		private ActionResult releaseBeast() {
			game.getItem(GameEntities.ITEM_BEAST).setItemFlag(0);
			game.getItem(GameEntities.ITEM_BEAST).setItemLocation(GameEntities.ROOM_FOREST);
			game.addMessage("The Canyon Beast runs away", true, true);
			return new ActionResult(game,player,true);
		}
	}
	
	/**
	 * Handles execution of "give" commands. Encapsulates
	 * all logic for transferring items to NPCs or entities.
	 */
	private class GiveHandler {
		
		private final Game game;
		private final Player player;
		private final int nounNumber;
		private final int objectNumber;
		private final String codedCommand;
		private final String[] commands;
		private final String object;
		private final String noun;
		
		
		public GiveHandler(Game game, Player player, ParsedCommand command) {
			this.game = game;
			this.player = player;
			this.commands = command.getSplitFullCommand();
			this.nounNumber = command.getNounNumber();
			this.noun = getNoun(commands);
			this.codedCommand = command.getCodedCommand();			
			this.object = getObject(commands);
			this.objectNumber = getNounNumber(object);
			
		}
		
		/**
		 * Executes the "give" command.
		 *
		 * @return an {@link ActionResult} with the result
		 */
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
				result = giveToMedian();
			}
			
			return result;
		}
		
		/** @return true if the recipient is a snake */
		private boolean isSnake() {
			return validateCode(codedCommand,GameEntities.CODE_SNAKE) && objectNumber==GameEntities.ITEM_SNAKE;
		}
		
		/** @return true if the recipient is a villager */
		private boolean isVillager() {
			return (validateCode(codedCommand,GameEntities.CODE_VILLAGER) || noun.equals(GameEntities.NOUN_DRINK)) && 
					objectNumber==GameEntities.ITEM_VILLAGER && ((int) player.getStat("drink"))>1;
		}
		
		/** @return true if the recipient is the swampman */
		private boolean isSwampman() {
			return validateCode(codedCommand.substring(0,2),GameEntities.CODE_JUG) &&
					game.getItem(GameEntities.ITEM_JUG).getItemFlag()<0 &&
					objectNumber == GameEntities.ITEM_SWAMPMAN;
		}
			
		/** @return true if the recipient is logmen */
		private boolean isLogmen() {
			return objectNumber == GameEntities.ITEM_LOGMEN;
		}
		
		/** @return true if the recipient is the scavenger */
		private boolean isScavenger() {
			return (validateCode(codedCommand.substring(0,3),GameEntities.CODE_LILY) || 
					validateCode(codedCommand.substring(0,3),GameEntities.CODE_CHIP)) &&
					objectNumber == GameEntities.ITEM_SCAVENGER;
		}
		
		/** @return true if the recipient is Median */
		private boolean isMedian() {
			return validateCode(codedCommand.substring(0,2),GameEntities.CODE_PEBBLE) &&
					objectNumber == GameEntities.ITEM_MEDIAN;
		}
		
		/** 
		 * generates the result of the player giving to the snake
		 * 
		 * @return an {@link ActionResult} with the result
		 */
		private ActionResult giveToSnake() {
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.getItem(objectNumber).setItemFlag(1);
			game.addMessage("The snake uncoils",true,true);
			return new ActionResult(game,player,true);
		}
		
		/** 
		 * generates the result of the player giving to the villager
		 * 
		 * @return an {@link ActionResult} with the result
		 */
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
		
		
		/** 
		 * generates the result of the player giving to the swampman
		 * 
		 * @return an {@link ActionResult} with the result
		 */
		private ActionResult giveToSwampman() {
			game.getItem(objectNumber).setItemFlag(1);
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.addMessage("The Swampman takes the jug and leaves",true,true);
			return new ActionResult(game,player,true);
		}
		
		/** 
		 * generates the result of the player giving to the logman
		 * 
		 * @return an {@link ActionResult} with the result
		 */
		private ActionResult giveToLogmen() {
			game.addMessage("It is taken",true,true);
			if (noun.equals(GameEntities.NOUN_FOOD)) {
				player.setStat("food", (int) player.getStat("food")-1);
			} else if (noun.equals(GameEntities.NOUN_DRINK)) {
				player.setStat("drink",(int) player.getStat("drink")-1);
			} else {
				game.addMessage("It is taken",true,true);
				game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_STOREROOM);
			}
			return new ActionResult(game,player,true);
		}
		
		/** 
		 * generates the result of the player giving to the Scavenger
		 * 
		 * @return an {@link ActionResult} with the result
		 */
		private ActionResult giveToScavenger() {
			player.setStat("wisdom",(int) player.getStat("wisdom")+10);
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.addMessage("It is accepted",true,true);
			return new ActionResult(game,player,true);
		}
		
		/** 
		 * generates the result of the player giving to Median
		 * 
		 * @return an {@link ActionResult} with the result
		 */
		private ActionResult giveToMedian() {
			game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.getItem(GameEntities.ITEM_PEBBLE).setItemFlag(-1);

			//Removes Median from Game
			game.getItem(GameEntities.ITEM_MEDIAN).setItemLocation(GameEntities.ROOM_DESTROYED);
			game.getItem(GameEntities.ITEM_MEDIAN).setItemFlag(1);
			
			game.addPanelMessage("He takes it ...", true);
			if (player.getRoom()!=GameEntities.ROOM_VATS) {
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
 * 10 September 2025 - Tightened code
 * 11 September 2025 - Added JavaDocs for Take and main section of the class
 * 27 October 2025 - Fixed problem where beast not being dropped.
 * 				   - If drop rope while carrying beast, release beast as well.
 * 10 November 2025 - Added validator to set the noun boatman to boat
 * 12 November 2025 - Remove food & drink when give
 * 16 November 2025 - Giving water to villager now works.
 * 23 November 2025 - Changed lightning flashes to normal panel message
 * 26 November 2025 - Added exclusion for books
 * 3 December 2025 - Increased version number
 */
