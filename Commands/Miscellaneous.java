/*
Title: Island of Secrets Miscellaneous Commands
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 31 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Data.GameEntities;
import Game.Game;
import Game.Player;

public class Miscellaneous {
	
	private final Game game;
	private final Player player;
	private final ParsedCommand command;
	private final String commandCode;
	private boolean hasItems = false;
	
	public Miscellaneous(Game game,Player player) {
		this.game = game;
		this.player = player;
		this.command = null;
		this.commandCode = null;
	}
	
	public Miscellaneous(Game game,Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.command = command;
		this.commandCode = command.getCodedCommand();
	}
	
	public ActionResult info() {
		Game game = getDetails();
		return new ActionResult(game,player);
	}
	
	public ActionResult wave() {
		
		ActionResult result = new ActionResult();
		if (isBoatmanPresent()) {
			game.addMessage("The boatman waves back.",true,true);
			result = new ActionResult(game,player);
		} else if (isWavingTorch()) {
			result = waveTorch();
		}
		
		return result;
	}
	
	private boolean isBoatmanPresent() {
		
		boolean boatmanPresent = false;
		if(game.getItem(GameEntities.ITEM_BOAT).isAtLocation(player.getRoom()) &&
		   !isWavingTorch()) {
			boatmanPresent = true;
		}
		return boatmanPresent;
	}
	
	private boolean isWavingTorch() {
		boolean wavingTorch = false;
		if(commandCode.substring(0,3).equals(GameEntities.CODE_TORCH_DIM)) {
			wavingTorch = true;
		}
		return wavingTorch;
	}
	
	private Game getDetails() {
		
		String items = getItemDetails();
		game.addMessage("Info - Items carried",true,false);
		game.addMessage("Food: "+((int) player.getStat("food")),false,false);
		game.addMessage("Drink: "+((int) player.getStat("drink")),false,false);
		
		if(hasItems) {
			game.addMessage("Items: "+items,false,false);
		}
		return game;
	}
	
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
	
	private ActionResult waveTorch() {
		game.getItem(GameEntities.ITEM_TORCH).setItemFlag(1);
		game.addMessage("The torch brightens.",true,true);
		
		if (player.getRoom()==GameEntities.ROOM_WITH_HANDS) {
			game.addMessage("The hands release you and retreat into the wall.",false,true);
		}
		
		game.getItem(GameEntities.ITEM_TORCH).setItemName("a brightly glowing torch");
		player.setStat("wisdom",(int) player.getStat("wisdom")+8);
		return new ActionResult(game,player);
	}
}

/* 31 May 2025 - Created File
 * 
*/