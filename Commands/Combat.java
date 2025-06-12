/*
Title: Island of Secrets Combat Commands
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 12 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.util.Random;

import Data.GameEntities;
import Game.Game;
import Game.Player;

public class Combat {

	private final Game game;
	private final Player player;
	private final ParsedCommand command;
	private final String codedCommand;
	private final int verbNumber;
	private final int nounNumber;
	private boolean hasItems = false;
	private final Random rand = new Random();
		
	public Combat(Game game,Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.command = command;
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
			result = carryingWeapon();
		}
		
		//Chopping roots with Axe
		if (isChoppingRoots()) {
			result = choppingRoots();
		}
		
		//Break the column with hammer
		if (isBreakingColumn()) {
			result = breakColumn();
		}
		
		if (isBreakStaff()) {
			result = breakStaff();
		} else if (isWasteStaff()) {
			result = wasteStaff();
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
	
	private boolean ifHaveEgg() {
		boolean haveEgg = false;
		if(game.getItem(GameEntities.ITEM_EGG).getItemLocation() == player.getRoom()) {
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
	
	private ActionResult carryingWeapon() {
		game.addMessage("Ok",true,true);
		return new ActionResult(game,player);
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
		
		if (ifHaveEgg()) {
			result = releaseDactyl(result);
		}
		return result;
	}
	
	private ActionResult breakStaffNormal() {
		player.setStat("wisdom",(int) player.getStat("wisdom")-10);
		game.getItem(nounNumber).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.getItem(nounNumber).setItemFlag(-1);
		game.setMessageGameState();
		game.addPanelMessage("It shatters releasing a rainbow of colours!", true);
		return new ActionResult(game,player);
	}
	
	private ActionResult releaseDactyl(ActionResult result) {
		Game game = result.getGame();
		Player player = result.getPlayer();
		
		game.addPanelMessage("The egg hatches into a baby dactyl which takes", false);
		game.addPanelMessage("Omegan in its claws and flies away", false);
		game.getItem(GameEntities.ITEM_OMEGAN).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.getItem(GameEntities.ITEM_EGG).setItemLocation(GameEntities.ROOM_DESTROYED);
		game.getItem(2).setItemFlag(-1);
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
}

/* 11 June 2025 - Create File
 * 12 June 2025 - Added Break Column & Break Staff
 */
