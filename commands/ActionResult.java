/*
Title: Island of Secrets Action Result
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 17 July 2025
Source: https://archive.org/details/island-of-secrets_202303

- Change validation to return Action Result
 
*/

package commands;

import game.Game;
import game.Player;

public class ActionResult {
	
	private final Game game;
	private final Player player;
	private final boolean valid;
	
	public ActionResult() {
		this.valid = false;
		player = null;
		game = null;
	}
	
	public ActionResult(Game game, Player player) {
		this.player = player;
		this.game = game;
		this.valid = false;
	}
	
	public ActionResult(Game game, Player player,boolean valid) {
		this.player = player;
		this.game = game;
		this.valid = valid;
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
 * 7 May 2025 - Added null constructor
 * 8 May 2025 - Added constructor for all variables
 * 17 July 2025 - Removed redundant include
*/