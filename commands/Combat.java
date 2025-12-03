/*
Title: Island of Secrets Combat Commands
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
 * Handles combat-related actions within the game, including attacking,
 * chopping, killing, and special interactions with items and creatures.
 * <p>
 * This class interprets a parsed command (verb and noun), evaluates the 
 * current game and player state, and returns an {@link ActionResult} 
 * describing the outcome of the action.
 */
public class Combat {

	private final Game game;
	private final Player player;
	private final String codedCommand;
	private final int verbNumber;
	private final int nounNumber;
	private final Random rand = new Random();
		
    /**
     * Constructs a Combat handler for the given game, player, and command.
     *
     * @param game    the current game state
     * @param player  the player performing the action
     * @param command the parsed command containing verb, noun, and code
     */
	public Combat(Game game,Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.codedCommand = command.getCodedCommand();
		this.verbNumber = command.getVerbNumber();
		this.nounNumber = command.getNounNumber();
	}
	
    /**
     * Handles chopping-related actions, such as chopping roots, breaking columns,
     * or breaking staffs, depending on the player's weapon and command.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	public ActionResult chop() {
		
		reduceStats(2,0);
		game.addMessage("Nothing happens",true,true);
		ActionResult result = new ActionResult(game,player,false);

		//Carrying Hammer or Axe
		if (isCarryingWeapon()) {
				
			//Chopping roots with Axe
			if (isChoppingRoots()) {
				result = choppingRoots();
			}
				
			//Break the column with hammer
			if (isBreakingColumn()) {
				result = breakColumn();
			}
			
			if (isTapPerson()) {
				if(hasAxe()) {
					result = kill();
				} else {
					result = annoyPerson();
				}
			}
		} 
				
		if (isBreakStaff()) {
			result = breakStaff();
		} else if (isWasteStaff()) {
			result = wasteStaff();
		}
		
		return result;
	}
	
    /**
     * Handles attack-related actions, including strikes against creatures,
     * objects, or special interactions such as striking flint.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	public ActionResult attack() {
		ActionResult result = defaultAttackRespond();
		
		if(isPresent()) {
			if(isHitOmegan()) {
				result = hitOmegan();
			} else if (isSwampman()) {
				result = hitSwampman();
			} else if (isHitSage()) {
				result = hitSage();
			} else if (isLogmen()) {
				result = hitLogmen();
			} else if (isHitDactyl()) {
				result = hitDactyl();
			} else if (isStrikeFlint()) {
				result = strikeFlint();
			}
		} else {
			result = notPresent();
		}
		return result;
	}

    /**
     * Handles kill commands. If the target is fatal, triggers the end-game sequence.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	public ActionResult kill() {
		return (isFatalResponse()) ? fatalResponse():defaultKillRespond();
	}
	
    // === Private Helper Methods ===
	
    /**
     * Checks if the player is carrying either an axe or hammer.
     *
     * @return true if the player has a weapon, false otherwise
     */
	private boolean isCarryingWeapon() {
		return game.getItem(GameEntities.ITEM_AXE).getItemLocation()==GameEntities.ROOM_CARRYING || 
				game.getItem(GameEntities.ITEM_HAMMER).getItemLocation()==GameEntities.ROOM_CARRYING;
	}
	
    /**
     * Checks if the player is chopping roots with an axe.
     *
     * @return true if chopping roots, false otherwise
     */
	private boolean isChoppingRoots() {
		return codedCommand.equals(GameEntities.CODE_CHOPPING_ROOTS) && 
				game.getItem(GameEntities.ITEM_AXE).getItemLocation()==GameEntities.ROOM_CARRYING;
	}
	
    /**
     * Checks if the player is attempting to break a column with a hammer.
     *
     * @return true if breaking a column, false otherwise
     */
	private boolean isBreakingColumn() {
		return (codedCommand.equals(GameEntities.CODE_BREAK_COLUMN_ONE) || 
				codedCommand.equals(GameEntities.CODE_BREAK_COLUMN_TWO)) && 
				game.getItem(GameEntities.ITEM_HAMMER).getItemLocation()==GameEntities.ROOM_CARRYING;
	}
	
    /**
     * Checks if the player is attempting to break a staff in the sanctum.
     *
     * @return true if breaking a staff, false otherwise
     */
	private boolean isBreakStaff() {
		return codedCommand.length() >= 4 && codedCommand.substring(0,4).equals(GameEntities.CODE_HAS_STAFF) && 
				player.getRoom()==GameEntities.ROOM_SANCTUM;
	}
	
    /**
     * Checks if both the egg and Omegan are present in the player's room.
     *
     * @return true if both are present, false otherwise
     */
	private boolean ifHaveEggAndOmegan() {
		return game.getItem(GameEntities.ITEM_EGG).getItemLocation() == player.getRoom() &&
				game.getItem(GameEntities.ITEM_OMEGAN).getItemLocation() == player.getRoom();
	}
	
    /**
     * Checks if the player is wasting a staff (breaking it with a break command).
     *
     * @return true if wasting a staff, false otherwise
     */
	private boolean isWasteStaff() {
		return codedCommand.length() >= 4 && codedCommand.substring(0,4).equals(GameEntities.CODE_HAS_STAFF) && 
				verbNumber == GameEntities.CMD_BREAK;
	}
	
    /**
     * Checks if the player is tapping a valid character or creature.
     *
     * @return true if tapping a person/creature, false otherwise
     */
	private boolean isTapPerson() {
		return (verbNumber==GameEntities.CMD_TAP && isKillPerson());
	}

    /**
     * Checks if the player is killing a valid character or creature.
     *
     * @return true if killing a person/creature, false otherwise
     */
	private boolean isKillPerson() {
		return (nounNumber==GameEntities.ITEM_SWAMPMAN || nounNumber == GameEntities.ITEM_BOAT ||
				nounNumber>GameEntities.ITEM_CLOAK && nounNumber<GameEntities.ITEM_ROCKS || 
				nounNumber==GameEntities.ITEM_BEAST || nounNumber == GameEntities.ITEM_DACTYL);
	}
	
    /**
     * Checks if the player has an axe.
     *
     * @return true if carrying an axe, false otherwise
     */
	private boolean hasAxe() {
		return game.getItem(GameEntities.ITEM_AXE).getItemLocation()==GameEntities.ROOM_CARRYING;
	}
	
    /**
     * Checks if the current kill attempt will trigger a fatal response.
     *
     * @return true if the kill is fatal, false otherwise
     */
	private boolean isFatalResponse() {
		return isKillPerson() && game.getItem(nounNumber).getItemLocation() == player.getRoom();
	}
	
    /**
     * Checks if the target noun is present either in the room or in inventory.
     *
     * @return true if the item/creature is present, false otherwise
     */
	private boolean isPresent() {
		return game.getItem(nounNumber).getItemLocation() == player.getRoom() || 
				game.getItem(nounNumber).getItemLocation() ==GameEntities.ROOM_CARRYING;
	}
	
    /**
     * Checks if the current attack targets Omegan.
     *
     * @return true if attacking Omegan, false otherwise
     */
	private boolean isHitOmegan() {
		return nounNumber == GameEntities.ITEM_OMEGAN;
	}
	
    /**
     * Checks if the current attack targets the sage.
     *
     * @return true if attacking the sage, false otherwise
     */
	private boolean isHitSage() {
		return nounNumber == GameEntities.ITEM_LILY;
	}
	
    /**
     * Checks if the current attack targets the dactyl.
     *
     * @return true if attacking the dactyl, false otherwise
     */
	private boolean isHitDactyl() {
		return nounNumber == GameEntities.ITEM_DACTYL;
	}
	
	/**
     * Checks if the current attack targets the logmen.
     *
     * @return true if attacking the logmen, false otherwise
     */
	private boolean isLogmen() {
		return nounNumber==GameEntities.ITEM_LOGMEN;
	}
	
    /**
     * Checks if the current attack targets the swampman.
     *
     * @return true if attacking the swampman, false otherwise
     */
	private boolean isSwampman() {
		return nounNumber == GameEntities.ITEM_SWAMPMAN;
	}
	
    /**
     * Checks if the player is striking flint.
     *
     * @return true if striking flint, false otherwise
     */
	private boolean isStrikeFlint() {
		return codedCommand.length() >= 4 && codedCommand.substring(0,4).equals(GameEntities.CODE_HAS_FLINT);
	}
	
    /**
     * Checks if coal is present in the player's room.
     *
     * @return true if coal is present, false otherwise
     */
	private boolean isCoalPresent() {
		return player.getRoom()==game.getItem(GameEntities.ITEM_COAL).getItemLocation();
	}
	
    /**
     * Checks if Omegan's Cloak is present in the sanctum with the player.
     *
     * @return true if present, false otherwise
     */
	private boolean isOmeganCloakPresent() {
		return player.getRoom()==game.getItem(GameEntities.ITEM_CLOAK).getItemLocation() && 
				player.getRoom()==GameEntities.ROOM_SANCTUM;
	}
	
    /**
     * Handles chopping roots with an axe, releasing sap.
     *
     * @return an {@link ActionResult} indicating success
     */
	private ActionResult choppingRoots() {
		game.getItem(GameEntities.ITEM_SAP).setItemFlag(0);
		game.getItem(GameEntities.ITEM_SAP).setItemLocation(player.getRoom());
		game.addMessage("You chop the roots and sap flows out", true, true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles breaking a column with a hammer.
     *
     * @return an {@link ActionResult} indicating success
     */
	private ActionResult breakColumn() {
		game.getItem(GameEntities.ITEM_CHIP).setItemFlag(0);
		game.getItem(GameEntities.ITEM_FRACTURE).setItemFlag(0);
		game.addMessage("Crack",true,true);
		return new ActionResult(game,player,true);
	}

    /**
     * Handles breaking a staff, with a special case if egg and Omegan are present.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult breakStaff() {
		return (ifHaveEggAndOmegan()) ? releaseDactyl():wasteStaff();
	}
	
    /**
     * Handles normal staff breaking without special conditions.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult wasteStaff(){
		reduceStats(0,10);
		game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.getItem(nounNumber).setItemFlag(-9);
		game.setMessageGameState();
		game.addPanelMessage("It shatters releasing a rainbow of colours!", true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles the release of a dactyl when the egg and Omegan are present.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult releaseDactyl() {		
		game.addPanelMessage("The egg hatches into a baby dactyl which takes", true);
		game.addPanelMessage("Omegan in its claws and flies away", false);
		game.getItem(GameEntities.ITEM_OMEGAN).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.getItem(GameEntities.ITEM_EGG).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.getItem(nounNumber).setItemFlag(-1);
		player.setStat(Constants.STAT_STRENGTH,(float) player.getStat(Constants.STAT_STRENGTH)+40);
		
		return new ActionResult(game,player,true);
	}
		
    /**
     * Handles the case where tapping someone annoys them rather than killing.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult annoyPerson() {
		game.addMessage("You annoy the "+game.getItem(nounNumber).getItemName(),true,true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles the default response to an attack command.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult defaultAttackRespond() {
		reduceStats(2,2);
		game.addMessage("That would be unwise",true,true);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles the default response to a kill command.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult defaultKillRespond() {
		reduceStats(12,10);
		game.addMessage("That is not possible",true,true);
		return new ActionResult(game,player,true);
	}
	
	private ActionResult notPresent() {
		game.addMessage("That is not possible",true,true);
		return new ActionResult(game,player,false);
	}
	
    /**
     * Handles a fatal kill response, triggering the end-game sequence.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult fatalResponse() {
		game.setMessageGameState();
		game.setEndGameState();
		game.addPanelMessage("Thunder splits the sky!",true);
		game.addPanelMessage("It is the triumphant voice of Omegan.",false);
		game.addPanelMessage("Well done Alphan!",false);
		game.addPanelMessage("The means becomes the end.",false);
		game.addPanelMessage("I claim you as my own!",false);
		game.addPanelMessage("Ha Ha Hah!",false);

		player.setStat(Constants.STAT_STRENGTH,(float) 0);
		player.setStat(Constants.STAT_WISDOM,0);
		player.setStat(Constants.STAT_TIME,0);
		game.getItem(Constants.NUMBER_OF_NOUNS).setItemFlag(1);
		
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles attacking Omegan.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult hitOmegan() {
		game.addMessage("He laughs dangerously.",true,true);
		reduceStats(8,5);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles attacking the sage.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult hitSage() {
		game.addMessage("You can't touch her",true,true);
		game.getItem(GameEntities.ITEM_LILY).setItemLocation(GameEntities.ROOM_DESTROYED);
		reduceStats(8,5);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles attacking the dactyl.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult hitDactyl() {
		game.setMessageGameState();
		game.addPanelMessage("You anger the bird!",true);
		game.addPanelMessage("Which flies you to a remote place", false);

		player.setRoom(rand.nextInt(6)+63);
		game.getItem(16).setItemLocation(1);
		game.getItem(16).setItemFlag(0);
		game.addMessage("",true,true);
		reduceStats(8,5);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles attacking the logmen.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult hitLogmen() {
		game.addMessage("They think that's funny!",true,true);
		reduceStats(8,5);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles attacking the swampman.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult hitSwampman() {
		game.addMessage("The swampman is unmoved.",true,true);
		reduceStats(8,5);
		return new ActionResult(game,player,true);
	}
	
    /**
     * Handles striking flint, possibly igniting coal.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult strikeFlint() {
		game.addMessage("Sparks fly",true,true);
		return (isCoalPresent()) ? coalPresent():new ActionResult(game,player,true);
	}
	
    /**
     * Handles the case where coal is present when flint is struck.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult coalPresent() {
		game.getItem(nounNumber).setItemFlag(-1);
		game.getItem(GameEntities.ITEM_COAL).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.addPanelMessage("The coal burns with a red flame",true);
		game.setMessageGameState();
		return (isOmeganCloakPresent())? omeganCloakPresent(): new ActionResult(game,player,true);
	}
	
    /**
     * Handles the case where Omegan's Cloak is present and destroyed by flame.
     *
     * @return an {@link ActionResult} indicating the outcome
     */
	private ActionResult omeganCloakPresent() {
		System.out.println("Cloak Destroyed");
		player.setStat(Constants.STAT_WISDOM,(int) player.getStat(Constants.STAT_WISDOM)+20);
		game.getItem(GameEntities.ITEM_COAL).setItemFlag(-1);
		game.getItem(GameEntities.ITEM_CLOAK).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.addPanelMessage("Which dissolves Omegan's Cloak", false);						
		
		return new ActionResult(game,player,true);
	}
	
    /**
     * Reduces the player's strength and wisdom stats by the given amounts.
     *
     * @param strength the strength reduction
     * @param wisdom   the wisdom reduction
     */
	private void reduceStats(int strength, int wisdom) {
	    player.setStat(Constants.STAT_STRENGTH, (float) player.getStat(Constants.STAT_STRENGTH) - strength);
	    player.setStat(Constants.STAT_WISDOM, (int) player.getStat(Constants.STAT_WISDOM) - wisdom);
	}
}

/* 11 June 2025 - Create File
 * 12 June 2025 - Added Break Column & Break Staff
 * 13 June 2025 - Added tap & kill command
 * 14 June 2025 - Started writing the attack function
 * 15 June 2025 - Continued with Attack Function
 * 16 June 2025 - Completed attack function with strike flint. Updated damage for attack responses
 * 15 July 2025 - Updated code so win conditions work, and fails if omegan not present when staff is broken
 * 26 July 2025 - Added set state changes to message states. Changed method of setting end game state
 * 28 July 2025 - Added set endgame state to kill command
 * 1 August 2025 - Removed response for carrying weapon, and moved results into if statement
 * 2 September 2025 - Updated based on new ActionResult
 * 7 September 2025 - Fixed and tightened code
 * 					- Add JavaDocs
 * 31 October 2025 - Changed response when attacking something not present
 * 				   - Added message for when chop roots
 * 2 November 2025 - Fixed problem with killing/tapping inanimate items. And not certain creatures
 * 23 November 2025 - Removed duplicate waste staff function
 * 24 November 2025 - Fixed problem with special messages not displaying
 * 29 November 2025 - Moved stat name constants to constants
 * 3 December 2025 - Increased version number
 */
