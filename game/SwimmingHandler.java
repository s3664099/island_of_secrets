/*
Title: Island of Secrets Swimming Handler
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import java.util.Random;

import command_process.ActionResult;
import data.Constants;

/**
 * Handles the swimming sequence in the game.
 *
 * The {@code SwimmingHandler} processes player commands while swimming,
 * updates the player's swim progress and strength, and determines
 * whether the player surfaces safely or drowns.
 */
public class SwimmingHandler {
	
	private final Random rand = new Random();
	
    /**
     * Executes a swimming command.
     *
     * @param command the player's command (directional input or other)
     * @param player the current {@link Player}
     * @param game the current {@link Game}
     * @return an {@link ActionResult} representing the updated game and player state
     */
	public ActionResult execute(String command, Player player, Game game) {
				
		game.addMessage("Ok",true,true);
		Swimming swim = player.getSwimming();
		
		// Movement command handling
		if (command.startsWith("n")) {
			swim.swim();
		} else if (!command.startsWith("s") &&
				   !command.startsWith("e") &&
				   !command.startsWith("w")) {
			game.addMessage("I do not understand",true,true);
		}
		
		// Adjust player's strength
		float strength = adjustStrength(player);
		player.setStat("strength",strength);
		
		// Check swim success
		if (swim.checkPosition((float) player.getStat("strength"))) {
			player.setPlayerStateNormal();
			game.addMessage("You surface",true,true);
			
			// places player in room 31–33
			player.setRoom(rand.nextInt(3)+31);
			
		} else if (strength<1) {
			game.addMessage("You get lost and drown",true,true);
			player.setPlayerStateNormal();
			game.setEndGameState();
		} else {
			player.setSwimming(swim);
		}
		
		return new ActionResult(game,player,true);
	}
	
    /**
     * Calculates the player's adjusted strength while swimming.
     *
     * The adjustment is based on the player's weight and constants
     * from the game configuration.
     *
     * @param player the current {@link Player}
     * @return the adjusted strength value
     */
	private float adjustStrength(Player player) {
		float strengthAdj = (float) ((((int) player.getStat("weight"))/Constants.NUMBER_OF_NOUNS+0.1)-3);
		return ((float) player.getStat("strength")) + strengthAdj;
	}
	
    /**
     * Returns the description displayed when the player
     * is in the swimming state.
     *
     * @return a short description of the swimming context
     */
	public String getDescription() {
		return "You are swimming in poisoned waters";
	}
	
    /**
     * Sets contextual swimming messages for the player.
     *
     * Displays a warning if the player has low strength,
     * and always prompts for a direction to swim.
     *
     * @param game the current {@link Game}
     * @param player the current {@link Player}
     */
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
 * 7 May 2025 - Changed return to ActionResult
 * 18 August 2025 - Updated with JavaDocs and polished it.
 * 3 September 2025 - Updated for ActionResult changes
 * 3 December 2025 - Increased version number
 */
 