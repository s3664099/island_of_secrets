/*
Title: Island of Secrets Combat Commands
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 14 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.util.Random;

import Data.Constants;
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

		if (isTapPerson()) {
			if(hasAxe()) {
				result = kill();
			} else {
				result = annoyPerson();
			}
		}

		return result;
	}
	
	public ActionResult attack() {
		ActionResult result = defaultAttackRespond();
		
		if(isPresent()) {
			if(isHitOmegan()) {
				result = hitOmegan();
			} else if (nounNumber == GameEntities.ITEM_SWAMPMAN) {
				game.addMessage("The swampman is unmoved.",true,true);
			} else if (isHitSage()) {
				result = hitSage();
			} else if (nounNumber == GameEntities.ITEM_LOGMEN) {
				game.addMessage("They think that's funny!",true,true);
			} else if (isHitDactyl()) {
				result = hitDactyl();
			}
		}
		
		return result;
	}
	
	/*if () {
			
			//Omegan the evil one
			if (noun==39) {
				game.addMessage("He laughs dangerously.",true,true);
			
			//Swampman
			} else if (noun==32) {
				
			
			//Sage of the Lilies
			} else if (noun==33) {
				game.addMessage("You can't touch her",true,true);
				game.getItem(3).setItemLocation(81);
			
			//Logmen
			} else if (noun==41) {
				

			//In the Dactyl's Nest
			} else if (player.getRoom()==46) {
				

			
			//Strike Flint
			} else if (code.substring(0,4).equals("1400")) {

				game.addMessage("Sparks fly",true,true);
				
				//Coal in room
				if (player.getRoom()==game.getItem(13).getItemLocation()) {
					
					game.getItem(noun).setItemFlag(-1);
					game.getItem(13).setItemLocation(81);
					game.setMessageGameState();
					
					//Omegan's present in his sanctum
					if (player.getRoom()==game.getItem(38).getItemLocation() && player.getRoom()==10) {
						
						game.addPanelMessage("The coal burns with a red flame",true);
						game.addPanelMessage("Which dissolves Omegan's Cloak", false);						

						player.setStat("wisdom",(int) player.getStat("wisdom")+20);
						game.getItem(13).setItemFlag(-1);
						game.getItem(38).setItemLocation(81);
					} else {
						game.addPanelMessage("The coal burns with a red flame",true);				
					}
				}
			}
			player.setStat("strength",(float) player.getStat("strength")-8);
			player.setStat("wisdom",(int) player.getStat("wisdom")-5);
		}	
	 */
	
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
		game.addPanelMessage("Thunder splits the sky!",true);
		game.addPanelMessage("It is the triumphant voice of Omegan.",false);
		game.addPanelMessage("Well done Alphan!",false);
		game.addPanelMessage("The means becomes the end.",false);
		game.addPanelMessage("I claim you as my own!",false);
		game.addPanelMessage("Ha Ha Hah!",false);

		player.setStat("strength",(float) 0);
		player.setStat("wisdom",0);
		player.setStat("timeRemaining",0);

		game.setEndGameState();	
		
		return new ActionResult(game,player);
	}
	
	private ActionResult hitOmegan() {
		game.addMessage("He laughs dangerously.",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult hitSage() {
		game.addMessage("You can't touch her",true,true);
		game.getItem(3).setItemLocation(81);
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
		return new ActionResult(game,player);
	}
}

/* 11 June 2025 - Create File
 * 12 June 2025 - Added Break Column & Break Staff
 * 13 June 2025 - Added tap & kill command
 * 14 June 2025 - Started writing the attack section
 */
