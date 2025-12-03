/*
Title: Island of Secrets Miscellaneous Commands
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
import game.Game;
import game.Player;

/**
 * Handles miscellaneous player actions in the adventure game, such as
 * waving, swimming, polishing stones, and interacting with special NPCs or
 * items. Most methods interpret a parsed player command and update the
 * {@link Game} and {@link Player} state accordingly.
 */
public class Miscellaneous {
	
    /** The active game instance. */
	private final Game game;
	
	/** The player executing the commands. */
	private final Player player;
	
	/** The parsed command from the player (may be null for info-only actions). */
	private final ParsedCommand command;
	
	/** Encoded representation of the player command. */
	private final String codedCommand;
	
	/** Verb index of the player command. */
	private final int verbNumber;
	
	/** Tracks if the player currently holds any items. */
	private boolean hasItems = false;
	
	/** Random number generator for actions like swimming relocation. */
	private final Random rand = new Random();
	
    /**
     * Creates a {@code Miscellaneous} handler for non-command actions
     * (e.g. displaying player info).
     * @param game   current game state
     * @param player active player
     */
	public Miscellaneous(Game game,Player player) {
		this.game = game;
		this.player = player;
		this.command = null;
		this.codedCommand = null;
		this.verbNumber = -99;
	}
	
    /**
     * Creates a {@code Miscellaneous} handler with a parsed command
     * for processing user actions.
     * @param game    current game state
     * @param player  active player
     * @param command parsed player command
     */
	public Miscellaneous(Game game,Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.command = command;
		this.codedCommand = command.getCodedCommand();
		this.verbNumber = command.getVerbNumber();
	}
	
    /**
     * Displays the player’s current food/drink stats and carried items.
     * @return result containing updated game messages
     */
	public ActionResult info() {
		return new ActionResult(displayPlayerDetails(),player,true);
	}
	
    /**
     * Handles the “wave” action—waves to a boatman or brightens a torch.
     * @return action result with updated game state
     */
	public ActionResult wave() {
		
		ActionResult result = new ActionResult();
		if (isBoatmanPresent()) {
			game.addMessage("The boatman waves back.",true,true);
			result = new ActionResult(game,player,true);
		} else if (isWavingTorch()) {
			result = waveTorch();
		}
		
		return result;
	}
	
    /**
     * Handles the “help” action, possibly interacting with sages or villagers.
     * @return action result with feedback or NPC response
     */
	public ActionResult help() {
		
		game.addMessage("?!?",true,true);
		ActionResult result = new ActionResult(game,player,true);
		
		if(isScratchSage()) {
			result = scratchSage();
		} else if (isHelpVillagerSage()) {
			game.addMessage("How will you do that?",true,true);
			result = new ActionResult(game,player,true);
		}
		return result;
	}
	
    /**
     * Handles the “polish” action, polishing or re-polishing the stone if conditions are met.
     * @return action result describing the polishing outcome
     */
	public ActionResult polish() {
		
		game.addMessage("A-dub-dub",true,true);
		ActionResult result = new ActionResult(game,player,true);
		
		//Rub the stone
		if (isPolishStone()) {
			result = polishStone();			
		} else if (isPolishStoneAgain()) {
			result = stonePolished();
		}
		return result;
	}
	
    /**
     * Handles the “speak” action, including special phrases like
     * “stony words” or “remember old times.”
     * @return action result with dialogue outcome
     */
	public ActionResult speak() {
		
		String noun = command.getSplitTwoCommand()[1];
		game.addMessage(noun,true,true);
		ActionResult result = new ActionResult(game,player,false);
		
		if(isSayStonyWords(noun)) {
			result = sayStonyWords();
		} else if (isSpeakScavenger(noun)) {
			result = speakScavenger();
		}
		
		return result;
	}
	
    /**
     * Handles the “fill” action for filling jugs with liquids.
     * @return action result with jug status or message
     */
	public ActionResult fill() {
		game.addMessage("Not sure that can be done.",true,true);
		ActionResult result = new ActionResult(game,player,false);
		
		if (isFillJug()) {
			result = fillJug();
		} else if (isFillJugWater()) {
			result = fillJugWater();
		} else if (isJugFull()) {
			result = jugFull();
		}
		return result;
	}
	
    /**
     * Handles the “ride” action for mounting a beast.
     * @return action result reflecting riding status
     */

	public ActionResult ride() {
		
		game.addMessage("How?",true,true);
		ActionResult result = new ActionResult(game,player,false);
		if (isRideBeast()) {
			result = rideBeast();
		} else if (isAlreadyRidingBeast()) {
			result = alreadyRidingBeast();
		} 
		return result;
	}
	
    /**
     * Handles the “open” action for opening chests or trapdoors.
     * @return action result with updated item states
     */
	public ActionResult open() {
		game.addMessage("I'm unable to do that",true,true);
		ActionResult result = new ActionResult(game,player,false);
		
		if (isOpenChest()) {
			result = openChest();
		}
		
		if (isOpenTrapdoor()) {
			result = openTrapdoor();
		}
		return result;
	}
	
    /**
     * Handles the “swim” action and randomly relocates the player
     * if swimming is allowed.
     * @return action result indicating success or failure
     */
	public ActionResult swim() {		
		return checkCanSwim()?canSwim():cannotSwim();
	}

    /**
     * Handles the “shelter” action, allowing the player to take shelter
     * during a storm if possible.
     * @return action result indicating shelter outcome
     */
	public ActionResult shelter() {
		game.addMessage("Not possible at the moment.",true,true);		
		return isStormBlowing()?shelterFromStorm():new ActionResult(game,player,false); 
	}
	
    // ---------- Private Helpers ----------

    /**
     * Checks if the boatman is present in the player’s current room and
     * the player is not waving the torch.
     * @return true if boatman is present
     */
	private boolean isBoatmanPresent() {
		return game.getItem(GameEntities.ITEM_BOAT).isAtLocation(player.getRoom()) &&
				   !isWavingTorch();
	}
	
    /**
     * Determines if the current command is waving a torch.
     * @return true if waving torch
     */
	private boolean isWavingTorch() {
		return codedCommand.substring(0,3).equals(GameEntities.CODE_TORCH_DIM);
	}
	
    /**
     * Checks if the player is scratching the sage.
     * @return true if scratch-sage command detected
     */
	private boolean isScratchSage() {
		return codedCommand.equals(GameEntities.CODE_SCRATCH_SAGE) 
				&& verbNumber == GameEntities.CMD_SCRATCH;
	}
	
    /**
     * Checks if the command requests helping a villager or sage.
     * @return true if help command matches villager/sage
     */
	private boolean isHelpVillagerSage() {
		return codedCommand.equals(GameEntities.CODE_HELP_VILLAGER) || 
				codedCommand.equals(GameEntities.CODE_HELP_SAGE);
	}
	
    /**
     * Determines if the player can polish the stone.
     * @return true if polishing stone is valid
     */
	private boolean isPolishStone() {
		boolean polishStone = false;
		if (command.getSplitTwoCommand()[1].length()>0) {
			String noun = command.getSplitTwoCommand()[1];
			if(noun.equals(GameEntities.NOUN_STONE) && player.getRoom()==GameEntities.ROOM_STONE 
			   && game.getItem(GameEntities.ITEM_STONE).getItemFlag()==1
			   && game.getItem(GameEntities.ITEM_RAG).getItemLocation()==GameEntities.ROOM_CARRYING) {
				polishStone = true;
			}
		}
		return polishStone;
	}
	
    /**
     * Determines if the stone is ready to be polished again.
     * @return true if second polish is valid
     */
	private boolean isPolishStoneAgain() {
		return codedCommand.substring(0,4).equals("2815") && player.getRoom()==GameEntities.ROOM_STONE
				&& game.getItem(GameEntities.ITEM_STONE).getItemFlag()==0
				&& game.getItem(GameEntities.ITEM_RAG).getItemLocation()==GameEntities.ROOM_CARRYING;
	}
	
    /**
     * Checks if the player is saying “stony words” at the castle entrance.
     * @param noun the second command word
     * @return true if conditions match
     */
	private boolean isSayStonyWords(String noun) {
		return noun.toLowerCase().equals(GameEntities.NOUN_STONEYWORDS) && player.getRoom()==GameEntities.ROOM_CASTLE_ENTRANCE &&
				game.getItem(GameEntities.ITEM_PEBBLE).getItemFlag()==0;
	}
	
    /**
     * Checks if the player is speaking to the scavenger with the phrase
     * “remember old times.”
     * @param noun the spoken phrase
     * @return true if conditions match
     */
	private boolean isSpeakScavenger(String noun) {
		return noun.toLowerCase().equals(GameEntities.NOUN_REMEMBEROLDTIMES) && 
				player.getRoom()==game.getItem(GameEntities.ITEM_SCAVENGER).getItemLocation() && 
				game.getItem(GameEntities.ITEM_LILY).getItemLocation()==GameEntities.ROOM_DESTROYED &&
				game.getItem(GameEntities.ITEM_CHIP).getItemLocation()==GameEntities.ROOM_DESTROYED;
	}
	
    /** @return true if the jug can be filled. */
	private boolean isFillJug() {
		return codedCommand.equals(GameEntities.CODE_FILL_JUG);
	}
	
    /** @return true if filling jug with water is requested. */
	private boolean isFillJugWater() {
		return codedCommand.equals(GameEntities.CODE_FILL_JUG_WATER);
	}
	
    /** @return true if the jug is already full. */
	private boolean isJugFull() {
		return codedCommand.substring(0,2).equals(GameEntities.CODE_JUG_FULL);
	}
	
    /** @return true if riding the beast is requested. */
	private boolean isRideBeast() {
		return codedCommand.substring(0,4).equals(GameEntities.CODE_RIDE_BEAST);
	}
	
    /** @return true if player is already riding the beast. */
	private boolean isAlreadyRidingBeast() {
		return codedCommand.substring(0,4).equals(GameEntities.CODE_RIDING_BEAST);
	}
	
    /** @return true if opening the chest is requested. */
	private boolean isOpenChest() {
		return codedCommand.equals(GameEntities.CODE_OPEN_CHEST);
	}
	
    /** @return true if opening the trapdoor is requested. */
	private boolean isOpenTrapdoor() {
		return codedCommand.equals(GameEntities.CODE_OPEN_TRAPDOOR);
	}
	
    /** @return true if a storm is active. */
	private boolean isStormBlowing() {
		return game.getItem(GameEntities.ITEM_STORM).getItemFlag()<0;
	}
	
	/** @return true if know of shelter.*/
	private boolean knowAnyShelter() {
		return game.getRoomVisited(GameEntities.ROOM_GRANDPAS_SHACK) || 
			   game.getRoomVisited(GameEntities.ROOM_SNELM_LAIR) ||
			   game.getRoomVisited(GameEntities.ROOM_HUT);
	}
	
    /** @return true if swimming is currently possible. */
	private boolean checkCanSwim() {
		return player.getRoom()==GameEntities.ROOM_STOREROOM && game.getItem(GameEntities.ITEM_TRAPDOOR).getItemFlag()==0;
	}
	
    /**
     * Adds player stats and carried items to the game message log.
     * @return updated game instance
     */
	private Game displayPlayerDetails() {
		
		String items = getItemDetails();
		String infoDisplay = "Info: "+"Food: "+((int) player.getStat("food"));
		infoDisplay += "  Drink: "+((int) player.getStat("drink"));
		game.addMessage(infoDisplay,true,false);
		
		if(hasItems) {
			game.addMessage("Items: "+items,false,true);
		}
		return game;
	}
	
    /**
     * Collects the names of all carried items for display.
     * @return comma-separated list of item names
     */
	private String getItemDetails() {
		
		int itemLength = 0;
		String items = "";
		
		for (int i=1;i<Constants.MAX_CARRIABLE_ITEMS+1;i++) {
			
			if (game.getItem(i).isAtLocation(GameEntities.ROOM_CARRYING)) {
				hasItems = true;
				int extraLength = 1;
				
				if (itemLength>0) {
					items = items+", ";
					extraLength = 2;
				}
				items = items+game.getItem(i).getItemName();
				itemLength += game.getItem(i).getItemName().length()+extraLength;
			}
		}		
		return items;
	}
	
    /**
     * Brightens the torch and updates related game state.
     * @return result of waving the torch
     */
	private ActionResult waveTorch() {
		game.getItem(GameEntities.ITEM_TORCH).setItemFlag(1);
		game.addMessage("The torch brightens.",true,true);
		
		if (player.getRoom()==GameEntities.ROOM_WITH_HANDS) {
			game.addMessage("The hands release you and retreat into the wall.",false,true);
		}
		
		game.getItem(GameEntities.ITEM_TORCH).setItemName("a brightly glowing torch");
		player.setStat("wisdom",(int) player.getStat("wisdom")+8);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles scratching the sage, increasing wisdom.
     * @return action result of scratching sage
     */
	private ActionResult scratchSage() {
		game.getItem(3).setItemFlag(0);
		game.addMessage("She nods slowly.",true,true);
		player.setStat("wisdom",(int) player.getStat("wisdom")+5);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles polishing the stone for the first time.
     * @return action result of polishing stone
     */
	private ActionResult polishStone() {
		game.getItem(28).setItemFlag(0);
		game.addMessage("Reflections stir within.",true,true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles second polishing of the stone and reveals the pebble.
     * @return action result of stone polished
     */
	private ActionResult stonePolished() {
		game.getItem(GameEntities.ITEM_PEBBLE).setItemFlag(0);
		game.getItem(GameEntities.ITEM_PEBBLE).setItemFlag(GameEntities.ROOM_CARRYING);
		game.getItem(GameEntities.ITEM_STONE).setItemFlag(99);
		game.addMessage("The stone utters 'Stony Words'",true,true);
		game.addMessage("Something falls out of the mouth",false,true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles saying the “stony words,” unlocking rocks.
     * @return action result of saying stony words
     */
	private ActionResult sayStonyWords() {
		game.addMessage("The stones are fixed.",false,false);
		game.getItem(GameEntities.ITEM_ROCKS).setItemFlag(1);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles speaking with the scavenger and transforming it.
     * @return action result of speaking to scavenger
     */
	private ActionResult speakScavenger() {
		game.addMessage("He eats the flowers - and changes",false,false);
		game.getItem(GameEntities.ITEM_SCAVENGER).setItemFlag(1);
		game.getItem(GameEntities.ITEM_MEDIAN).setItemFlag(0);
		
		return new ActionResult(game,player,true);
	}
	
    /**
     * Fills the jug with special liquid.
     * @return action result of filling jug
     */
	private ActionResult fillJug() {
		game.getItem(GameEntities.ITEM_JUG).setItemFlag(-1);
		game.addMessage("Filled",true,true);
		game.getItem(GameEntities.ITEM_JUG).setItemName("A jug full of bubbling green liquid");
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles filling the jug with water and spilling it.
     * @return action result of filling jug with water
     */
	private ActionResult fillJugWater() {
		game.addMessage("The water streams out of the jug",true,true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Responds when the jug is already full.
     * @return action result of jug full
     */
	private ActionResult jugFull() {
		if (game.getItem(GameEntities.ITEM_JUG).getItemFlag()==-1)  {
			if (player.getRoom()==GameEntities.ROOM_HUT || 
				player.getRoom()==GameEntities.ROOM_BRANCHES) {
				game.addMessage("The jug is already full",true,true);
			}
		}
		return new ActionResult(game,player,true);
	}
	
    /**
     * Allows the player to ride the specified beast.
     * @return action result of riding beast
     */
	private ActionResult rideBeast() {
		int nounNumber = command.getNounNumber();
		game.getItem(nounNumber).setItemFlag(-1);
		game.addMessage("It allows you to ride.",true,true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Notifies that the player is already riding the beast.
     * @return action result of already riding
     */
	private ActionResult alreadyRidingBeast() {
		game.addMessage("You are already riding the beast.",true,true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Opens the chest and updates item states inside it.
     * @return action result of opening chest
     */
	private ActionResult openChest() {
		game.addMessage("The chest opens. There is something inside",true,true);
		game.getItem(GameEntities.ITEM_PARCHMENT).setItemFlag(9);
		game.getItem(GameEntities.ITEM_RAG).setItemFlag(9);
		game.getItem(GameEntities.ITEM_HAMMER).setItemFlag(9);
		game.getItem(GameEntities.ITEM_CHEST).setItemFlag(1);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Opens the trapdoor and grants wisdom.
     * @return action result of opening trapdoor
     */
	private ActionResult openTrapdoor() {
		game.addMessage("The trapdoor creaks",true,true);
		game.getItem(29).setItemFlag(0);
		player.setStat("wisdom",(int) player.getStat("wisdom")+3);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Reports that swimming is not possible and penalizes wisdom.
     * @return action result of failed swim
     */
	private ActionResult cannotSwim() {
		game.addMessage("You can't swim here!",true,true);
		player.setStat("wisdom",(int) player.getStat("wisdom")-1);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Lets the player swim and randomly relocates them.
     * @return action result of successful swim
     */
	private ActionResult canSwim() {
		game.addMessage("You dive into the water",true,true);
		player.setPlayerStateSwimming();
		player.setRoom(rand.nextInt(5)+1);
		return new ActionResult(game,player,true);
	}
	
	 /**
     * Handles taking whether player knows of any shelter or not
     * @return action result of sheltering
     */
	private ActionResult shelterFromStorm() {
		game.addMessage("I don't know of any shelter",true,true);
		ActionResult result = new ActionResult(game,player,true);
		if (knowAnyShelter()) {
			result = selectShelter();
		}
		return result;
	}
	
    /**
     * Handles taking shelter from a storm based on player input.
     * @return action result of sheltering
     */
	private ActionResult selectShelter() {
		int commandLength = command.getCommand().split(" ").length;
		String[] commands = command.getSplitFullCommand();
		
		if (commandLength>1) {
			
			String shelterLocation = commands[1];
			game.addMessage(GameEntities.SHELTER_MESSAGE,true,true);
			
			if (commands.length>2 && commands[1].equals("in")) {
				shelterLocation = commands[2];
			}
			
			if (shelterLocation.equals(GameEntities.NOUN_SHACK) && game.getRoomVisited(GameEntities.ROOM_GRANDPAS_SHACK)) {
				player.setRoom(GameEntities.ROOM_GRANDPAS_SHACK);
			} else if (shelterLocation.equals(GameEntities.NOUN_CAVE) && game.getRoomVisited(GameEntities.ROOM_SNELM_LAIR)) {
				player.setRoom(GameEntities.ROOM_SNELM_LAIR);
			} else if (shelterLocation.equals(GameEntities.NOUN_HUT) && game.getRoomVisited(GameEntities.ROOM_HUT)) {
				player.setRoom(GameEntities.ROOM_HUT);
			} else {
				game.addMessage("I'm sorry, I do not know that place",true,true);
			}
			
		} else {
			game.addMessage("You can shelter in:",true,true);
			game.setShelterGameState();
		}
		return new ActionResult(game,player,true);
	}
}

/* 31 May 2025 - Created File
 * 1 June 2025 - Added help & polish commands
 * 2 June 2025 - Added Speak command
 * 8 June 2025 - Added fill command
 * 9 June 2025 - Added ride and open commands
 * 10 June 2025 - Added swim & shelter commands
 * 14 July 2025 - Updated info section to prevent display from breaking
 * 18 July 2025 - Fixed problem with not setting the swimming flag in the storeroom
 * 19 July 2025 - Changes to setPlayerStateSwimming
 * 12 August 2025 - Changed comment when rub mouth
 * 2 September 2025 - Updated based on new ActionResult
 * 14 September 2025 - Tightened Up Code.
 * 					 - Added JavaDocs
 * 9 October 2025 - Changed lair to Snelm's Lair
 * 17 November 2025 - Added entity for noun stone
 * 30 November 2025 - Changed cabin to hut
 * 3 December 2025 - Added response if no shelter known
 * 				   - Increased version number
 */