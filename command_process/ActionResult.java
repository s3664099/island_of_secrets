/*
Title: Island of Secrets Action Result
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 1 September 2025
Source: https://archive.org/details/island-of-secrets_202303 

- Update methods calling class
*/

package command_process;

import game.Game;
import game.Player;

public class ActionResult {
	
	private final Game game;
	private final Player player;
	private final boolean valid;
	
	public ActionResult() {
		this(null,null,false);
	}
		
	public ActionResult(Game game, Player player,boolean valid) {
		this.player = player;
		this.game = game;
		this.valid = valid;
	}
	
	public ActionResult success(Game game, Player player) {
		return new ActionResult(game,player,true);
	}
	
	public ActionResult failure(Game game,Player player) {
		return new ActionResult(game,player, false);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Game getGame() {
		return game;
	}
		
	public boolean isValid() {
		return valid;
	}
	
    // --- Debugging helper ---
    @Override
    public String toString() {
        return "ActionResult{" +
               "game=" + game +
               ", player=" + player +
               ", valid=" + valid +
               '}';
    }
}

/* 5 May 2025 - Created File
 * 7 May 2025 - Added null constructor
 * 8 May 2025 - Added constructor for all variables
 * 17 July 2025 - Removed redundant include
 * 1 September 2025 - Updated based on recommendations
*/