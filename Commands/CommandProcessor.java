/*
Title: Island of Secrets Command Processor
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 27 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.io.IOException;
import java.util.Random;

import Data.Constants;
import Data.Item;
import Game.Game;
import Game.Player;
import Model.Swimming;

public class CommandProcessor {

	private final Game game;
	private final Player player;
	private String codedCommand;
	private int nounNum;

	public CommandProcessor(Game game, Player player) {
		this.game = game;
		this.player = player;
	}
	
	public CommandResult execute(String command) throws IOException {
			
		CommandProcess processCommands = new CommandProcess(command,this.game);
		int verbNumber = processCommands.getVerbNumber();
		int nounNumber = processCommands.getNounNumber();
		
		//Either verb or noun doesn't exist
		if (verbNumber>Constants.NUMBER_OF_VERBS || nounNumber == Constants.NUMBER_OF_NOUNS) {
			this.game.addMessage("You can't "+command,true,true);
		}

		//Neither exists
		if (verbNumber>Constants.NUMBER_OF_VERBS && nounNumber == 52) {
			this.game.addMessage("What!!",true,true);
		}
		
		//No second word move to end
		if (nounNumber == -1) {
			nounNumber = Constants.NUMBER_OF_NOUNS;
		}
		
		this.player.turnUpdateStats();
		Item item = this.game.getItem(nounNumber);
		String codedCommand = processCommands.codeCommand(this.player.getRoom(),nounNumber,item);
		processCommands.executeCommand(this.game, player, nounNumber);
			
		this.codedCommand = codedCommand;
		this.nounNum = nounNumber;
			
		//Has a game been loaded?
		if (processCommands.checkLoadedGame()) {
			//this.game = processCommands.getGame();
			//this.player = processCommands.getPlayer();
		}
		
		//Is the player now swimming - creates new swimming object
		if (player.isPlayerStateSwimming()) {
			player.setSwimming(new Swimming(player.getRoom()));
		}
			
		//determinePanel(game);
				
		CommandResult result = new CommandResult();
		
		return result;
	}
	
	public void executeGive(String object) {
		//Checks if the response is 'to xxxx'
		String[] instructions = object.split(" ");
		if (instructions[0].equals("to") && instructions.length==2) {
			object = instructions[1];
		}
		
		//Is the response correct for a give command?
		if (object.split(" ").length==1) {
			CommandProcess processCommands = new CommandProcess();
			processCommands.executeGive(this.game,this.player,this.nounNum,object,this.codedCommand);
		} else {
			this.game.addMessage("I'm sorry, I don't understand.",true,true);
		}
		
		this.game.setGiveState();
	}
	
}

/* 23 April 2025 - Create class
 * 27 April 2025 - Moved Swimming code to Swimming. Added swimming state change
 */
