/*
Title: Island of Secrets Swimming Handler
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 7 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import java.util.Random;

import command_process.ActionResult;
import data.Constants;

public class SwimmingHandler {
	
	public ActionResult execute(String command, Player player, Game game) {
				
		game.addMessage("Ok",true,true);
		Swimming swim = player.getSwimming();
		
		if (command.substring(0,1).equals("n")) {
			swim.swim();
		} else if (!command.substring(0,1).equals("s") &&
				   !command.substring(0,1).equals("e") &&
				   !command.substring(0,1).equals("w")) {
			game.addMessage("I do not understand",true,true);
		}
		
		float strengthAdj = (float) ((((int) player.getStat("weight"))/Constants.NUMBER_OF_NOUNS+0.1)-3);
		float strength = ((float) player.getStat("strength")) + strengthAdj;
		player.setStat("strength",strength);
		
		if (swim.checkPosition((float) player.getStat("strength"))) {
			player.setPlayerStateNormal();
			game.addMessage("You surface",true,true);
			Random rand = new Random();
			player.setRoom(rand.nextInt(3)+31);
			
		} else if (strength<1) {
			game.addMessage("You get lost and drown",true,true);
			player.setPlayerStateNormal();
			game.setEndGameState();
		} else {
			player.setSwimming(swim);
		}
		
		return new ActionResult(game,player);
	}
	
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
 * 7 May 2025 - Changed return to ActionResult
 */
 