/*
Title: Island of Secrets Persistence Commands
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
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
import java.util.regex.Pattern;

import command_process.ActionResult;
import command_process.ParsedCommand;
import data.Constants;
import game.Game;
import game.Player;

/**
 * Handles persistence operations for the adventure game, including
 * saving, loading, and listing saved games.  
 * <p>
 * This class sanitizes filenames to prevent directory traversal attacks
 * and ensures that all game and player state is safely serialized.
 */
public class Persistence {

    /** The current game instance to save or update. */
	private final Game game;

    /** The current player instance to save or update. */
	private final Player player;
	
    /** Tokenized user command used to determine persistence actions.*/
	private final String[] splitCommand;
	
    /** Logger for recording save/load errors and events. */
	private static final Logger logger = Logger.getLogger(Game.class.getName());
	
    /** Directory in which save files are stored.*/
	private final String SAVE_GAME_DIRECTORY = "savegames";
	
    /** File extension used for saved games.*/
	private final String SAVE_GAME_EXTENSION = ".sav";
	
    /** Overwrite flag expected in the command to overwrite an existing save. */
	private final String OVERWRITE = "o";
	
    /** Pattern that restricts save names to letters, digits, underscores, and hyphens. */
	private static final Pattern SAFE_NAME = Pattern.compile("^[A-Za-z0-9_-]+$");
	
    /**
     * Creates a new {@code Persistence} object to manage saving and loading game data.
     *
     * @param game    the current {@link Game} instance
     * @param player  the current {@link Player} instance
     * @param command the parsed user command containing action and arguments
     */
	public Persistence(Game game,Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.splitCommand = command.getSplitFullCommand();
	}
	
    /**
     * Saves the current game state to disk.  
     * If no filename is provided, prompts the user to supply one.
     *
     * @return an {@link ActionResult} representing the outcome of the save request
     */
	public ActionResult save() {
		
		Game game = this.game;
		
		if (splitCommand.length==1) {
			game.addMessage("Please include the name of your game.",true,true);
		} else {
			game = saveGame();
		}
		return new ActionResult(game,player,true);
	}
	
    /**
     * Loads a saved game.  
     * If no game name is provided, displays a paginated list of available saved games.
     *
     * @return an {@link ActionResult} representing the outcome of the load request
     */
	public ActionResult load() {		
		return splitCommand.length==1?displayGames():loadGame();
	}
	
    /**
     * Ends the current game session, sets end-game state, and signals the player
     * is quitting.
     *
     * @return an {@link ActionResult} indicating the game has been terminated
     */
	public ActionResult quit() {
		game.addMessage("You relinquish your quest",true,true);
		game.getItem(Constants.NUMBER_OF_NOUNS).setItemFlag(-1);
		player.setStat("timeRemaining",1);
		game.setEndGameState();
		return new ActionResult(game,player,true);
	}
	
    /**
     * Ends the current game session, sets end-game state, and signals the player
     * is quitting.
     *
     * @return an {@link ActionResult} indicating the game has been terminated
     */
	public ActionResult restart() {
		game.addMessage("You relinquish your quest",true,true);
		game.getItem(Constants.NUMBER_OF_NOUNS).setItemFlag(-1);
		player.setStat("timeRemaining",1);
		game.setRestartGameState();
		return new ActionResult(game,player,true);
	}
	
    /**
     * Displays a paginated list of saved games to the user.
     * The list shows up to four saves at a time and updates the
     * {@link Game} state for navigation between pages.
     *
     * @return an {@link ActionResult} with the updated game state
     */
	private ActionResult displayGames() {
		
		final int MAX_DISPLAY = 4;
		File saveGameDirectory = new File(SAVE_GAME_DIRECTORY);
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
	
    /**
     * Returns an array of valid saved-game files in the specified directory.
     *
     * @param directory the directory to scan
     * @return an array of save files with {@code .sav} extension, or an empty array if none exist
     */
	private File[] getSavedGames(File directory) {
		return !directory.exists() || !directory.isDirectory()?
				new File[0]:directory.listFiles((dir,name) ->
				name.toLowerCase().endsWith(SAVE_GAME_EXTENSION));
	}
	
    /**
     * Serializes the current game and player objects to a save file.
     * Performs validation to prevent overwriting unless the user includes the
     * overwrite flag and sanitizes the save name to prevent path traversal.
     *
     * @return the updated {@link Game} object after save attempt
     */
	private Game saveGame() {
		
		boolean writeFile = false;
		File saveGameDirectory = new File(SAVE_GAME_DIRECTORY);
		
		//Checks to see if the directory exists. If it doesn't it creates the directory
		if(!saveGameDirectory.exists()) {
			saveGameDirectory.mkdir();
		}
			
		File saveFile = new File(saveGameDirectory+"/"+splitCommand[1]+SAVE_GAME_EXTENSION);
			
		//Checks to see if the file exists
		if (saveFile.exists() && (splitCommand.length<3 || !splitCommand[2].equals(OVERWRITE))) {
				
			//If file exists tells user how to overwrite it
			game.addMessage("File already exists. Please add 'o' to the end to overwrite.",true,true);
			writeFile = false;
		
		} else {
			writeFile = true;
		}
	
		//Writes file	
		if (writeFile) {
		
			try {
				String fileName = sanitiseFileName(splitCommand[1]);
				FileOutputStream file = new FileOutputStream(saveGameDirectory+"/"+fileName+SAVE_GAME_EXTENSION);
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
			
			if (splitCommand.length>2 && !splitCommand[2].equals(OVERWRITE)) {
				game.addMessage("Game not saved",true,true);
			}
		}
		return game;
	}
	
    /**
     * Ensures that a proposed save name contains only allowed characters.
     *
     * @param name the raw save name provided by the user
     * @return a validated and safe save name
     * @throws IllegalArgumentException if the name contains invalid characters
     */
	private String sanitiseFileName(String name) throws IllegalArgumentException {
	    if (!SAFE_NAME.matcher(name).matches()) {
	        throw new IllegalArgumentException("Invalid save name. Use letters, numbers, _ or - only.");
	    }
	    return name;
	}
	
    /**
     * Attempts to load a previously saved game and player state from disk.
     * The save name is sanitized to prevent path traversal.
     *
     * @return an {@link ActionResult} representing success or failure of the load operation
     */
	private ActionResult loadGame() {

		Game game = this.game;
		Player player = this.player;

		boolean loadFile = false;
		
		File saveGameDirectory = new File(SAVE_GAME_DIRECTORY);
		String fileName = sanitiseFileName(splitCommand[1]);
		File saveFile = new File(saveGameDirectory+"/"+fileName+SAVE_GAME_EXTENSION);
		
		if (!saveFile.exists()) {			
			game.addMessage("Sorry, the saved game does not exist. Type 'games' to list games.",true,true);
		} else {
			loadFile = true;
		}
			
		if (loadFile) {
	
			//Attempts to load the file
			try {
				FileInputStream file = new FileInputStream(saveGameDirectory+"/"+fileName+SAVE_GAME_EXTENSION);
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
 * 15 September 2025 - Tightened Code and added JavaDocs
 * 29 September 2025 - Tightened Comments
 * 6 November 2025 - Added restart game action
 * 3 December 2025 - Increased version number
 */
