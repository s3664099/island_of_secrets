/*
Title: Island of Secrets Command Processor
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 23 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.io.IOException;
import java.util.Random;

import Data.Constants;
import Data.Item;
import Game.Game;
import Game.Player;

public class CommandProcessor {

	private Game game;
	private Player player;
	private String codedCommand;
	private int nounNum;
	private Swimming swim;

	public CommandProcessor(Game game, Player player) {
		this.game = game;
		this.player = player;
	}
	
	public void execute(String command) throws IOException {
		
		//Saves the commands into the previous command list
		if (this.commandHistory[0].equals("")) {
			this.commandHistory[0] = command;
		} else if (this.commandHistory[1].equals("")) {
			this.commandHistory[1] = command;
		} else if (this.commandHistory[2].equals("")) {
			this.commandHistory[2] = command;
		} else {
			this.commandHistory[0] = this.commandHistory[1];
			this.commandHistory[1] = this.commandHistory[2];
			this.commandHistory[2] = command;
		}
		
		//Checks if player 'Swimming in Poisoned Waters'
		if (player.getPanelFlag()!=4) {
			
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
				this.game = processCommands.getGame();
				this.player = processCommands.getPlayer();
			}
			
			//Is the player now swimming - creates new swimming object
			if (player.getSwimming()) {
				this.swim = new Swimming(player.getRoom());
				player.setSwimming(false);
			}
			
			test.displayValue(this.game, this.player);
			
			//determinePanel(game);
		} else {
			
			this.game.addMessage("Ok",true,true);
			
			if (command.substring(0,1).equals("n")) {
				this.swim.swim();
			} else if (!command.substring(0,1).equals("s") &&
					   !command.substring(0,1).equals("e") &&
					   !command.substring(0,1).equals("w")) {
				this.game.addMessage("I do not understand",true,true);
			}
			
			float strengthAdj = (float) ((((int) player.getStat("weight"))/Constants.NUMBER_OF_NOUNS+0.1)-3);
			float strength = ((float) player.getStat("strength")) + strengthAdj;
			player.setStat("strength",strength);
			
			if (this.swim.checkPosition((float) player.getStat("strength"))) {
				player.setPanelFlag(0);
				this.game.addMessage("You surface",true,true);
				Random rand = new Random();
				player.setRoom(rand.nextInt(3)+31);
				
			} else if (strength<1) {
				this.game.addMessage("You get lost and drown",true,true);
				player.setPanelFlag(0);
				this.game.setEndGameState();
			}
		}
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
		
		this.game.setGiveState(false);
	}
	
}

/* 23 April 2025 - Create class
 */
