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
	private boolean hasItems = false;
	
	public Miscellaneous(Game game,Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.command = command;
	}
	
	public ActionResult info() {
		Game game = getDetails();
		return new ActionResult(game,player);
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
}

/* 31 May 2025 - Created File
 * 
*/