/*
Title: Island of Secrets Persistence Commands
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 17 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
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
		
		Game game = this.game;
		
		if (splitCommand.length==1) {
			game.addMessage("Please include the name of your game.",true,true);
		} else {
			game = saveGame();
		}
		return new ActionResult(game,player);
	}
	
	public ActionResult load() {
		
		ActionResult result = new ActionResult(game,player);
		
		if (splitCommand.length==1) {
			result = displayGames();
		} else {
			result = loadGame();
		}
		
		return result;
	}
	
	private ActionResult displayGames() {
		
		//Checks to see if the file exists
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
				gameDisplayed[i-startIndex] = savFiles[i].getName();
			}
			
			//Update game state
			game.setLowerLimitSavedGames(hasMorePrevious);
			game.setUpperLimitSavedGames(hasMoreNext);
			game.setDisplayedGames(gameDisplayed);
			game.addMessage("Game Saves", true, true);
			game.setSavedGameState();
		}
		
		return new ActionResult(game,player);
	}
		
	private File[] getSavedGames(File directory) {
		
		File[] files = new File[0];
		
		if(!directory.exists() || !directory.isDirectory()) {
			files = new File[0];
		} else {
			files = directory.listFiles((dir,name) ->
			name.toLowerCase().endsWith(".sav"));
		}
		return files;
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
				game.addMessage("Game Failed to save " + e.toString(),true,true);
			}
		} else {
			game.addMessage("Game not saved",true,true);
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
				game.addMessage("Game Failed to save " + e.toString(),true,true);
			}
		}
		return new ActionResult(game,player);
	}
}

/* 16 June 2025 - Created File
 * 17 June 2025 - Created Save Game & Load Game File.
 */
