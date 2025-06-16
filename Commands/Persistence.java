/*
Title: Island of Secrets Persistence Commands
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 16 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

import Game.Game;
import Game.Player;

public class Persistence {

	private final Game game;
	private final Player player;
	private final String[] splitCommand;
	private final String codedCommand;
	private final int verbNumber;
	private final int nounNumber;
	private final Random rand = new Random();
		
	public Persistence(Game game,Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.splitCommand = command.getSplitFullCommand();
		this.codedCommand = command.getCodedCommand();
		this.verbNumber = command.getVerbNumber();
		this.nounNumber = command.getNounNumber();
	}
	
	public ActionResult save() {
		
		boolean writeFile = false;
		
		if (splitCommand.length==1) {
			game.addMessage("Please include the name of your game.",true,true);
		} else {
				
			File saveGameDirectory = new File("savegames");
				
			//Checks to see if the directory exists. If it doesn't it creates the directory
			if(!saveGameDirectory.exists()) {
				saveGameDirectory.mkdir();
			}
				
			File saveFile = new File(saveGameDirectory+"/"+commands[1]+".sav");
				
			//Checks to see if the file exists
			if (saveFile.exists() && (commands.length<3 || !commands[2].equals("o"))) {
					
				//If file exists tells user how to overwrite it
				game.addMessage("File already exists. Please add 'o' to the end to overwrite.",true,true);
				writeFile = false;
			
			} else {
				writeFile = true;
			}
		
			//Writes file	
			if (writeFile) {
			
				try {
					FileOutputStream file = new FileOutputStream(saveGameDirectory+"/"+commands[1]+".sav");
					ObjectOutputStream out = new ObjectOutputStream(file);
					out.writeObject(game);
					out.writeObject(player);
					out.close();
					file.close();
					game.addMessage("Save successful",true,true);

				} catch (IOException e) {
			        throw new IOException("Game Failed to save " + e.toString());
				}
			} else {
				game.addMessage("Game not saved",true,true);
			}
		}
	}
	
}

/* 16 June 2025 - Created File
*/