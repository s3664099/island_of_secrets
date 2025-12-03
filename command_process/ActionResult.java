/*
Title: Island of Secrets Action Result
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303 
*/

package command_process;

import game.Game;
import game.Player;

/**
 * Represents the result of executing a game action. 
 * <p>
 * An {@code ActionResult} contains references to the {@link Game}, 
 * an optional {@link Player}, and a flag indicating whether the 
 * action was valid (successful).
 * </p>
 */
public class ActionResult {
	
	private final Game game;
	private final Player player;
	private final boolean valid;
	
    /**
     * Creates a default {@code ActionResult} that is invalid
     * and has no associated {@link Game} or {@link Player}.
     */
	public ActionResult() {
		this(null,null,false);
	}
		
    /**
     * Creates a new {@code ActionResult}.
     *
     * @param game   the {@link Game} in which the action was performed (may be {@code null})
     * @param player the {@link Player} associated with the action (may be {@code null})
     * @param valid  {@code true} if the action was valid, {@code false} otherwise
     */
	public ActionResult(Game game, Player player,boolean valid) {
		this.player = player;
		this.game = game;
		this.valid = valid;
	}
	
    /**
     * Creates a successful {@code ActionResult}.
     *
     * @param game   the {@link Game} in which the action was performed
     * @param player the {@link Player} associated with the action
     * @return a new {@code ActionResult} marked as valid
     */
	public ActionResult success(Game game, Player player) {
		return new ActionResult(game,player,true);
	}
	
    /**
     * Creates a failed {@code ActionResult}.
     *
     * @param game   the {@link Game} in which the action was performed
     * @param player the {@link Player} associated with the action
     * @return a new {@code ActionResult} marked as invalid
     */
	public ActionResult failure(Game game,Player player) {
		return new ActionResult(game,player, false);
	}
	
    /**
     * Returns the {@link Player} associated with this result.
     *
     * @return the player, or {@code null} if none was provided
     */
	public Player getPlayer() {
		return player;
	}
	
    /**
     * Returns the {@link Game} associated with this result.
     *
     * @return the game, or {@code null} if none was provided
     */
	public Game getGame() {
		return game;
	}
	
    /**
     * Indicates whether the action was valid.
     *
     * @return {@code true} if the action was valid, {@code false} otherwise
     */
	public boolean isValid() {
		return valid;
	}
	
    /**
     * Returns a string representation of this {@code ActionResult},
     * useful for debugging and logging.
     *
     * @return a string containing game, player, and validity info
     */
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
 * 1 September 2025 - Updated based on recommendations. Added JavaDocs
 * 3 December 2025 - Increased version number
*/