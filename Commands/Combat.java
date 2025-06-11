/*
Title: Island of Secrets Combat Commands
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 11 June 2025
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
	private boolean hasItems = false;
	private final Random rand = new Random();
	
	public Combat(Game game,Player player) {
		this.game = game;
		this.player = player;
		this.command = null;
		this.codedCommand = null;
		this.verbNumber = -99;
	}
	
	public Combat(Game game,Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.command = command;
		this.codedCommand = command.getCodedCommand();
		this.verbNumber = command.getVerbNumber();
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
		if (this.code.equals("1258158") || this.code.equals("2758158") && 
			game.getItem(15).getItemLocation()==0) {
				game.getItem(12).setItemFlag(0);
				game.getItem(27).setItemFlag(0);
				game.addMessage("Crack",true,true);
		}
		
		//Break the staff
		if (this.code.substring(0,4).equals("1100") && player.getRoom()==10) {
			player.setStat("wisdom",(int) player.getStat("wisdom")-10);
			game.getItem(noun).setItemLocation(81);
			game.getItem(noun).setItemFlag(-1);
			game.setMessageGameState();
			game.addPanelMessage("It shatters releasing a rainbow of colours!", true);
			
			if (game.getItem(2).getItemLocation() == player.getRoom()) {
								game.addPanelMessage("The egg hatches into a baby dactyl which takes", false);
				game.addPanelMessage("Omegan in its claws and flies away", false);

				game.getItem(39).setItemLocation(81);
				game.getItem(2).setItemLocation(2);
				game.getItem(2).setItemFlag(-1);
				player.setStat("strength",(float) player.getStat("strength")+40);
			}
		
		//Response if player uses the staff without meeting the conditions above
		} else if (this.code.substring(0,4).equals("1100") && verb == 19) {
			game.getItem(noun).setItemLocation(81);
			game.getItem(noun).setItemFlag(-1);
			game.setMessageGameState();
			game.addPanelMessage("It shatters releasing a rainbow of colours!", true);
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
	
	private ActionResult carryingWeapon() {
		game.addMessage("Ok",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult choppingRoots() {
		game.getItem(GameEntities.ITEM_SAP).setItemFlag(0);
		game.getItem(GameEntities.ITEM_SAP).setItemLocation(player.getRoom());
	}
}

/* 11 June 2025 - Create File
 * 
 */
