/*
Title: Island of Secrets Combat Commands
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.8
Date: 1 August 2025
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

public class Combat {

	private final Game game;
	private final Player player;
	private final String codedCommand;
	private final int verbNumber;
	private final int nounNumber;
	private final Random rand = new Random();
		
	public Combat(Game game,Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.codedCommand = command.getCodedCommand();
		this.verbNumber = command.getVerbNumber();
		this.nounNumber = command.getNounNumber();
	}
	
	public ActionResult chop() {
		
		player.setStat("strength",(float) player.getStat("strength")-2);
		game.addMessage("Nothing happens",true,true);
		ActionResult result = new ActionResult(game,player);

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
		}
		
		return result;
	}
		
	public ActionResult kill() {
		
		ActionResult result = defaultKillRespond();
		if (isFatalResponse()) {
			result = fatalResponse();
		}
		return result;
	}
	
	private boolean isCarryingWeapon() {
		boolean carryingWeapon = false;
		if(game.getItem(GameEntities.ITEM_AXE).getItemLocation()==GameEntities.ROOM_CARRYING || 
		   game.getItem(GameEntities.ITEM_HAMMER).getItemLocation()==GameEntities.ROOM_CARRYING) {
			carryingWeapon = true;
		}
		return carryingWeapon;
	}
	
	private boolean isChoppingRoots() {
		boolean choppingRoots = false;
		if(codedCommand.equals(GameEntities.CODE_CHOPPING_ROOTS) && 
		   game.getItem(GameEntities.ITEM_AXE).getItemLocation()==GameEntities.ROOM_CARRYING) {
			choppingRoots = true;
		}
		return choppingRoots;
	}
	
	private boolean isBreakingColumn() {
		boolean breakingColumn = false;
		if(codedCommand.equals(GameEntities.CODE_BREAK_COLUMN_ONE) || 
		   codedCommand.equals(GameEntities.CODE_BREAK_COLUMN_TWO) && 
		   game.getItem(GameEntities.ITEM_HAMMER).getItemLocation()==GameEntities.ROOM_CARRYING) {
			breakingColumn=true;
		}
		return breakingColumn;
	}
	
	private boolean isBreakStaff() {
		boolean breakStaff = false;
		if(codedCommand.substring(0,4).equals(GameEntities.CODE_HAS_STAFF) && 
		   player.getRoom()==GameEntities.ROOM_SANCTUM) {
			breakStaff = true;
		}
		return breakStaff;
	}
	
	private boolean ifHaveEggAndOmegan() {
		boolean haveEgg = false;
		
		if(game.getItem(GameEntities.ITEM_EGG).getItemLocation() == player.getRoom() &&
			game.getItem(GameEntities.ITEM_OMEGAN).getItemLocation() == player.getRoom()) {
			haveEgg = true;
		}
		return haveEgg;
	}
	
	private boolean isWasteStaff() {
		boolean wasteStaff = false;
		if(codedCommand.substring(0,4).equals(GameEntities.CODE_HAS_STAFF) && 
		  verbNumber == GameEntities.CMD_BREAK) {
			wasteStaff = true;
		}
		return wasteStaff;
	}
	
	private boolean isTapPerson() {
		boolean tapPerson = false;
		if((verbNumber==GameEntities.CMD_TAP && 
		   (nounNumber>GameEntities.ITEM_TRAPDOOR && nounNumber<GameEntities.ITEM_BOOKS) || 
		   (nounNumber>GameEntities.ITEM_CLOAK && nounNumber<GameEntities.ITEM_ROCKS) || 
		   nounNumber==GameEntities.ITEM_BEAST)) {
			tapPerson = true;
		}
		return tapPerson;
	}
	
	private boolean hasAxe() {
		boolean hasAxe = false;
		if(game.getItem(GameEntities.ITEM_AXE).getItemLocation()==GameEntities.ROOM_CARRYING) {
			hasAxe = true;
		}
		return hasAxe;
	}
	
	private boolean isFatalResponse() {
		boolean isFatal = false;
		if(game.getItem(nounNumber).getItemLocation() == player.getRoom()) {
			isFatal = true;
		}
		return isFatal;
	}
	
	private boolean isPresent() {
		boolean present = false;
		if(game.getItem(nounNumber).getItemLocation() == player.getRoom() || 
		   game.getItem(nounNumber).getItemLocation() ==GameEntities.ROOM_CARRYING) {
			present = true;
		}
		return present;
	}
	
	private boolean isHitOmegan() {
		boolean hitOmegan = false;
		if(nounNumber == GameEntities.ITEM_OMEGAN) {
			hitOmegan = true;
		}
		return hitOmegan;
	}
	
	private boolean isHitSage() {
		boolean hitSage = false;
		if(nounNumber == GameEntities.ITEM_LILY) {
			hitSage = true;
		}
		return hitSage;
	}
	
	private boolean isHitDactyl() {
		boolean hitDactyl = false;
		if(nounNumber == GameEntities.ITEM_DACTYL) {
			hitDactyl = true;
		}
		return hitDactyl;
	}
	
	private boolean isLogmen() {
		boolean isLogmen = false;
		if(nounNumber==GameEntities.ITEM_LOGMEN) {
			isLogmen = true;
		}
		return isLogmen;
	}
	
	private boolean isSwampman() {
		boolean isSwampman = false;
		if(nounNumber == GameEntities.ITEM_SWAMPMAN) {
			isSwampman = true;
		}
		return isSwampman;
	}
	
	private boolean isStrikeFlint() {
		boolean strikeFlint = false;
		
		if(codedCommand.substring(0,4).equals(GameEntities.CODE_HAS_FLINT)) {
			strikeFlint = true;
		}
		return strikeFlint;
	}
	
	private boolean isCoalPresent() {
		boolean coalPresent = false;
		if(player.getRoom()==game.getItem(GameEntities.ITEM_COAL).getItemLocation()) {
			coalPresent = true;
		}
		return coalPresent;
	}
	
	private boolean isOmeganCloakPresent() {
		boolean omeganCloakPresent = false;
		if(player.getRoom()==game.getItem(GameEntities.ITEM_CLOAK).getItemLocation() && 
		   player.getRoom()==GameEntities.ROOM_SANCTUM) {
			omeganCloakPresent = true;
		}
		return omeganCloakPresent;
	}
		
	private ActionResult choppingRoots() {
		game.getItem(GameEntities.ITEM_SAP).setItemFlag(0);
		game.getItem(GameEntities.ITEM_SAP).setItemLocation(player.getRoom());
		return new ActionResult(game,player);
	}
	
	private ActionResult breakColumn() {
		game.getItem(GameEntities.ITEM_CHIP).setItemFlag(0);
		game.getItem(GameEntities.ITEM_FRACTURE).setItemFlag(0);
		game.addMessage("Crack",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult breakStaff() {
		ActionResult result = breakStaffNormal();
		
		if (ifHaveEggAndOmegan()) {
			result = releaseDactyl(result);
		}
		return result;
	}
	
	private ActionResult breakStaffNormal() {
		player.setStat("wisdom",(int) player.getStat("wisdom")-10);
		game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.getItem(nounNumber).setItemFlag(-9);
		game.setMessageGameState();
		game.addPanelMessage("It shatters releasing a rainbow of colours!", true);
		return new ActionResult(game,player);
	}
	
	private ActionResult releaseDactyl(ActionResult result) {
		Game game = result.getGame();
		Player player = result.getPlayer();
		
		game.setMessageGameState();
		game.addPanelMessage("The egg hatches into a baby dactyl which takes", false);
		game.addPanelMessage("Omegan in its claws and flies away", false);
		game.getItem(GameEntities.ITEM_OMEGAN).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.getItem(GameEntities.ITEM_EGG).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.getItem(nounNumber).setItemFlag(-1);
		player.setStat("strength",(float) player.getStat("strength")+40);
		
		return new ActionResult(game,player);
	}
	
	private ActionResult wasteStaff() {
		game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.getItem(nounNumber).setItemFlag(-1);
		game.setMessageGameState();
		game.addPanelMessage("It shatters releasing a rainbow of colours!", true);
		return new ActionResult(game,player);
	}
	
	private ActionResult annoyPerson() {
		game.addMessage("You annoy the "+game.getItem(nounNumber).getItemName(),true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult defaultAttackRespond() {
		player.setStat("strength",(float) player.getStat("strength")-2);
		player.setStat("wisdom",(int) player.getStat("wisdom")-2);
		game.addMessage("That would be unwise",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult defaultKillRespond() {
		player.setStat("strength",(float) player.getStat("strength")-12);
		player.setStat("wisdom",(int) player.getStat("wisdom")-10);
		game.addMessage("That would be unwise",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult fatalResponse() {
		game.getItem(Constants.NUMBER_OF_ITEMS).setItemFlag(1);
		
		game.setMessageGameState();
		game.setEndGameState();
		game.addPanelMessage("Thunder splits the sky!",true);
		game.addPanelMessage("It is the triumphant voice of Omegan.",false);
		game.addPanelMessage("Well done Alphan!",false);
		game.addPanelMessage("The means becomes the end.",false);
		game.addPanelMessage("I claim you as my own!",false);
		game.addPanelMessage("Ha Ha Hah!",false);

		player.setStat("strength",(float) 0);
		player.setStat("wisdom",0);
		player.setStat("timeRemaining",0);
		game.getItem(Constants.NUMBER_OF_NOUNS).setItemFlag(1);
		
		return new ActionResult(game,player);
	}
	
	private ActionResult hitOmegan() {
		game.addMessage("He laughs dangerously.",true,true);
		player.setStat("strength",(float) player.getStat("strength")-8);
		player.setStat("wisdom",(int) player.getStat("wisdom")-5);
		return new ActionResult(game,player);
	}
	
	private ActionResult hitSage() {
		game.addMessage("You can't touch her",true,true);
		game.getItem(3).setItemLocation(81);
		player.setStat("strength",(float) player.getStat("strength")-8);
		player.setStat("wisdom",(int) player.getStat("wisdom")-5);
		return new ActionResult(game,player);
	}
	
	private ActionResult hitDactyl() {
		game.setMessageGameState();
		game.addPanelMessage("You anger the bird!",true);
		game.addPanelMessage("Which flies you to a remote place", false);

		player.setRoom(rand.nextInt(6)+63);
		game.getItem(16).setItemLocation(1);
		game.getItem(16).setItemFlag(0);
		game.addMessage("",true,true);
		player.setStat("strength",(float) player.getStat("strength")-8);
		player.setStat("wisdom",(int) player.getStat("wisdom")-5);
		return new ActionResult(game,player);
	}
	
	private ActionResult hitLogmen() {
		game.addMessage("They think that's funny!",true,true);
		player.setStat("strength",(float) player.getStat("strength")-8);
		player.setStat("wisdom",(int) player.getStat("wisdom")-5);
		return new ActionResult(game,player);
	}
	
	private ActionResult hitSwampman() {
		game.addMessage("The swampman is unmoved.",true,true);
		player.setStat("strength",(float) player.getStat("strength")-8);
		player.setStat("wisdom",(int) player.getStat("wisdom")-5);
		return new ActionResult(game,player);
	}
	
	private ActionResult strikeFlint() {
		game.addMessage("Sparks fly",true,true);
		ActionResult result = new ActionResult(game,player);
		
		//Coal in room
		if (isCoalPresent()) {
			result = coalPresent();
		}
		return result;
	}
	
	private ActionResult coalPresent() {
		game.getItem(nounNumber).setItemFlag(-1);
		game.getItem(GameEntities.ITEM_COAL).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.setMessageGameState();
		game.addPanelMessage("The coal burns with a red flame",true);
		ActionResult result = new ActionResult(game,player);
		
		if (isOmeganCloakPresent()) {
			result = omeganCloakPresent();
		} 
		return result;
	}
	
	private ActionResult omeganCloakPresent() {

		game.setMessageGameState();
		game.addPanelMessage("The coal burns with a red flame",true);
		game.addPanelMessage("Which dissolves Omegan's Cloak", false);						
		player.setStat("wisdom",(int) player.getStat("wisdom")+20);
		game.getItem(GameEntities.ITEM_COAL).setItemFlag(-1);
		game.getItem(GameEntities.ITEM_CLOAK).setItemLocation(GameEntities.ROOM_DESTROYED);
		ActionResult result = new ActionResult(game,player);
		
		return result;
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
 */
