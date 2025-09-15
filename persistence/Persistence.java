/*
Title: Island of Secrets Persistence Commands
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 3 September 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import command_process.ActionResult;
import command_process.ParsedCommand;
import data.Constants;
import game.Game;
import game.Player;

public class Persistence {

	private final Game game;
	private final Player player;
	private final String[] splitCommand;
	private static final Logger logger = Logger.getLogger(Game.class.getName());
		
	public Persistence(Game game,Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.splitCommand = command.getSplitFullCommand();
	}
	
	public ActionResult save() {
		
		Game game = this.game;
		
		if (splitCommand.length==1) {
			game.addMessage("Please include the name of your game.",true,true);
		} else {
			game = saveGame();
		}
		return new ActionResult(game,player,true);
	}
	
	public ActionResult load() {		
		return splitCommand.length==1?displayGames():loadGame();
	}
	
	public ActionResult quit() {
		game.addMessage("You relinquish your quest",true,true);
		game.getItem(Constants.NUMBER_OF_NOUNS).setItemFlag(-1);
		player.setStat("timeRemaining",1);
		game.setEndGameState();
		return new ActionResult(game,player,true);
	}
	
	private ActionResult displayGames() {
		
		final int MAX_DISPLAY = 4;
		File saveGameDirectory = new File("savegames");
		String[] gameDisplayed = game.getDisplayedSavedGames();
		
		//Clear previous Entries
		Arrays.fill(gameDisplayed,"");
		
		//Get saved games with safety checks
		File[] savFiles = getSavedGames(saveGameDirectory);
		if (savFiles == null || savFiles.length == 0) {
			game.addMessage("There are no saved games to display", true, true);
		
		//Calculate pagination indicies
		} else {
			int currentOffset = game.getCount();
			int startIndex = currentOffset * MAX_DISPLAY;
			int endIndex = Math.min(startIndex+MAX_DISPLAY,savFiles.length);
			boolean hasMorePrevious = (startIndex>0);
			boolean hasMoreNext = (savFiles.length>endIndex);
			
			//Populate displayed games
			for (int i = startIndex;i<endIndex;i++) {
				String saveGame = savFiles[i].getName();
				gameDisplayed[i-startIndex] = saveGame.substring(0,saveGame.length()-4);
			}
			
			//Update game state
			game.setLowerLimitSavedGames(hasMorePrevious);
			game.setUpperLimitSavedGames(hasMoreNext);
			game.setDisplayedGames(gameDisplayed);
			game.addMessage("Game Saves", true, true);
			game.setSavedGameState();
		}
		
		return new ActionResult(game,player,true);
	}
		
	private File[] getSavedGames(File directory) {
		return !directory.exists() || !directory.isDirectory()?
				new File[0]:files = directory.listFiles((dir,name) ->
				name.toLowerCase().endsWith(".sav"));
	}
	
	public Game saveGame() {
		
		boolean writeFile = false;
		File saveGameDirectory = new File("savegames");
		
		//Checks to see if the directory exists. If it doesn't it creates the directory
		if(!saveGameDirectory.exists()) {
			saveGameDirectory.mkdir();
		}
			
		File saveFile = new File(saveGameDirectory+"/"+splitCommand[1]+".sav");
			
		//Checks to see if the file exists
		if (saveFile.exists() && (splitCommand.length<3 || !splitCommand[2].equals("o"))) {
				
			//If file exists tells user how to overwrite it
			game.addMessage("File already exists. Please add 'o' to the end to overwrite.",true,true);
			writeFile = false;
		
		} else {
			writeFile = true;
		}
	
		//Writes file	
		if (writeFile) {
		
			try {
				FileOutputStream file = new FileOutputStream(saveGameDirectory+"/"+splitCommand[1]+".sav");
				ObjectOutputStream out = new ObjectOutputStream(file);
				out.writeObject(game);
				out.writeObject(player);
				out.close();
				file.close();
				game.addMessage("Save successful",true,true);

			} catch (IOException e) {
				logger.log(Level.SEVERE, "Game Failed to save " + e.toString());
				game.addMessage("Game Failed to save.",true,true);
			}
		} else {
			
			if (splitCommand.length>2 && !splitCommand[2].equals("o")) {
				game.addMessage("Game not saved",true,true);
			}
		}
		return game;
	}
	
	private ActionResult loadGame() {
		
		Game game = this.game;
		Player player = this.player;
		
		boolean loadFile = false;
				
		File saveGameDirectory = new File("savegames");				
		File saveFile = new File(saveGameDirectory+"/"+splitCommand[1]+".sav");		
		//If not available
		if (!saveFile.exists()) {			
			game.addMessage("Sorry, the saved game does not exist. Type 'games' to list games.",true,true);
		} else {
			loadFile = true;
		}
			
		if (loadFile) {
	
			//Attempts to load the file
			try {
				FileInputStream file = new FileInputStream(saveGameDirectory+"/"+splitCommand[1]+".sav");
				ObjectInputStream fileIn = new ObjectInputStream(file);
			
				//Load successful. Update the objects
				game = (Game) fileIn.readObject();
				player = (Player) fileIn.readObject();
			
				fileIn.close();
				file.close();
				game.addMessage("Game successfully loaded",true,true);
				game.resetCount();
						
			//Location failed to load
			} catch (IOException|ClassNotFoundException e) {
				logger.log(Level.SEVERE, "Game Failed to load " + e.toString());
				game.addMessage("Game Failed to load.",true,true);
			}
		}
		return new ActionResult(game,player,true);
	}
}

/* 16 June 2025 - Created File
 * 17 June 2025 - Created Save Game & Load Game File.
 * 18 June 2025 - Added display saved game functions. Added quit function and tidied up.
 * 7 July 2025 - Fixed code so reason for save game fail displays
 * 			   - Added logger for game failed to save & load
 * 			   - Stripped .sav from load game displays
 * 3 September 2025 - Changed for updated ActionResult changes
 */
