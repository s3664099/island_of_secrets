/*
Title: Island of Secrets Command Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.0
Date: 9 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

public class Commands {
	
	String[] splitCommand = {"",""};
	int verbNo;
	int nounNo;
	Game game;
	Player player;
	
	public Commands(String command,Player player, Game game) {
		
		this.game = game;
		this.player = player;
		
		command = command.toLowerCase();
		String[] commands = command.split(" ");
		splitCommand[0] = commands[0];
		
		if (commands.length>1) {
			splitCommand[1] = command.substring(commands[0].length()).trim();
		}	
	}
}

/* 9 November 2024 - Created method
 *
 */