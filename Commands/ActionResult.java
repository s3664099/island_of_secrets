/*
Title: Island of Secrets Action Result
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 5 May 2025
Source: https://archive.org/details/island-of-secrets_202303

- Change validation to return Action Result
 
*/

package Commands;

import Game.Game;
import Game.Player;

public class ActionResult {
	
	private final Game game;
	private final Player player;
	private final boolean valid;
	
	public ActionResult(Game game, Player player) {
		this.player = player;
		this.game = game;
		this.valid = false;
	}
	
	public ActionResult(Game game, boolean valid) {
		this.player = null;
		this.game = game;
		this.valid = valid;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Game getGame() {
		return game;
	}
	
	public boolean getValid() {
		return valid;
	}
}

/* 5 May 2025 - Created File
 * 
*/