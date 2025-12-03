/*
Title: Island of Secrets GameController
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package ui;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.GameEngine;
import game.GameInitialiser;
import game.Player;
import interfaces.GameCommandHandler;
import interfaces.GameUI;

/**
 * Mediates between the {@link GameEngine} and {@link GameUI}, handling
 * all user interactions and translating them into game commands.
 * <p>
 * The controller manages game state, user commands, UI refreshes,
 * and navigation of saved games.
 * </p>
 */
public final class GameController implements GameCommandHandler {

	private static final Logger logger = Logger.getLogger(GameController.class.getName());
	
	private GameEngine engine;
	private final GameUI ui;
	
    /**
     * Constructs a GameController with the specified engine and UI.
     *
     * @param engine the {@link GameEngine} to control; must not be null
     * @param ui     the {@link GameUI} for rendering the game; must not be null
     * @throws NullPointerException if {@code engine} or {@code ui} is null
     */
	public GameController(GameEngine engine, GameUI ui) {
		this.engine = Objects.requireNonNull(engine, "GameEngine cannot be null");
        this.ui = Objects.requireNonNull(ui, "GameUI cannot be null");
	}
	
    /**
     * Returns the current state of the game.
     *
     * @return a {@link GameState} object representing the current game state
     */
	public GameState getState() {
		return new GameState(engine);
	}
	
    /**
     * Returns the underlying {@link GameEngine}.
     *
     * @return the current {@link GameEngine}
     */
	public GameEngine getEngine() {
		return engine;
	}
	
    /**
     * Adds a message to the game engine.
     *
     * @param message the message to add
     * @param clear   whether to clear previous messages
     * @param isLong  whether the message should be treated as long
     */
	public void addMessage(String message, boolean clear, boolean isLong) {
		engine.addMessage(message,clear,isLong);
	}

    /**
     * Processes a command string by passing it to the {@link GameEngine}
     * and refreshing the UI.
     *
     * @param input the command string
     * @throws IOException if processing the command fails
     */
	@Override
	public void processCommand(String input) throws IOException {
		logger.log(Level.FINE, "Processing command: {0}", input);
		engine.processCommand(input);
		refreshUI();
	}
	
    /**
     * Sets the engine into saved game state and refreshes the UI.
     */
	@Override
	public void setSavedGameState() {
		engine.setSavedGameState();
		refreshUI();
	}
	
    /**
     * Sets the engine into running game state and refreshes the UI.
     */
	@Override
	public void setRunningGameState() {
		engine.setRunningGameState();
		refreshUI();
	}
	
    /**
     * Sets the engine into message display state.
     */
	public void setMessageState() {
		engine.setMessageState();
	}
	
    /**
     * Checks if the engine is currently in "give" state.
     *
     * @return true if in give state, false otherwise
     */
	public boolean isGiveState() {
		return engine.isGiveState();
	}
	
    /**
     * Checks if the engine is currently in message display state.
     *
     * @return true if in message state, false otherwise
     */
	public boolean isMessageState() {
		return engine.isMessageState();
	}
		
    /**
     * Sets the engine into shelter game state.
     */
	@Override
	public void setShelterGameState() {
		engine.setShelterGameState();
	}
	
    /**
     * Sets the current room in the engine and refreshes the UI.
     *
     * @param locationID the ID of the room to set
     */
	@Override
	public void setRoom(int locationID) {
		logger.log(Level.FINE, "Setting room: {0}", locationID);
		engine.setRoom(locationID);		
		refreshUI();
	}
	
    /**
     * Refreshes the UI to reflect the current state of the game engine.
     */
	public void refreshUI() {
		ui.refreshUI(this);
	}
	
    /**
     * Closes the user interface.
     */
	public void closeUI() {
		ui.closeUI();
	}
	
    /**
     * Displays the map view in the UI.
     */
	public void showMap() {
		ui.showMapView(this);
	}
	
    /**
     * Restarts the game by creating a new {@link GameEngine} and {@link Player},
     * then refreshes the UI.
     */
	public void restart() {
		logger.log(Level.FINE, "Restarting game");
		this.engine = new GameEngine(
			GameInitialiser.initialiseGame(),
			new Player()
		);
		refreshUI();
	}
	
    /**
     * Moves the load position to the next saved game and refreshes the UI.
     *
     * @throws IOException if navigating to the next saved game fails
     */
	@Override
	public void increaseLoadPosition() throws IOException {
		engine.increaseLoadPosition();
		refreshUI();
	}

    /**
     * Moves the load position to the previous saved game and refreshes the UI.
     *
     * @throws IOException if navigating to the previous saved game fails
     */
	@Override
	public void decreaseLoadPosition() throws IOException {
		engine.decreaseLoadPosition();
		refreshUI();
	}
}

/* 30 March 2025 - Created file
 * 31 March 2025 - Made gameUI the GameFrame
 * 1 April 2025 - Added getResponseType
 * 3 April 2025 - Updated Code to take GameState
 * 4 April 2025 - Added function to set the mapPanel
 * 8 April 2025 - Added closeUI function
 * 9 April 2025 - Added functon to restart game
 * 10 April 2025 - Added set room function
 * 13 April 2025 - Updated code for increasing and descreasing save game positions
 * 				 - Updated based on DeepSeek recommendations.
 * 23 April 2025 - Added setResponseType to change response type and addMessage to add a message
 * 				   from the controller, Removed processShelter
 * 25 April 2025 - Updated based on changes to enums in Game class and Player class
 * 30 June 2025 - Removed separate process for give
 * 28 July 2025 - Updated code so as to set message state
 * 15 August 2025 - Added check Lightning mode
 * 21 September 2025 - Added JavaDocs
 * 6 November 2025 - Added restart game command
 * 23 November 2025 - Removed Lightning State
 * 3 December 2025 - Increased version number
 */
