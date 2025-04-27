/*
Title: Island of Secrets Swimming Handler
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 27 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import Game.Game;
import Game.Player;

public class SwimmingHandler {

	public String getDescription() {
		return "You are swimming in poisoned waters";
	}
	
	public void setMessage(Game game,Player player) {
		
		if (((float) player.getStat("strength"))<15) {
			game.addMessage("You are very weak!",false,false);
		}
		
		game.addMessage("", false,false);
		game.addMessage("", false,false);
		game.addMessage("Which Way?",false,false);
	}
	
}

/* 27 April 2025 - Created File.
 */