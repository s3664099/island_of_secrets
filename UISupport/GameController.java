/*
Title: Island of Secrets GameController
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.12
Date: 30 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package UISupport;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import Game.Player;
import Interfaces.GameCommandHandler;
import Interfaces.GameUI;
import Model.GameEngine;
import Model.GameInitialiser;

/**
 * Mediates between GameEngine and GameUI, handling all user interactions
 * and translating them into game commands.
 */
public final class GameController implements GameCommandHandler {

	private static final Logger logger = Logger.getLogger(GameController.class.getName());
	
	private GameEngine engine;
	private final GameUI ui;
	
	public GameController(GameEngine engine, GameUI ui) {
		this.engine = Objects.requireNonNull(engine, "GameEngine cannot be null");
        this.ui = Objects.requireNonNull(ui, "GameUI cannot be null");
	}
	
	public GameState getState() {
		return new GameState(engine);
	}
	
	public GameEngine getEngine() {
		return engine;
	}
		
	public void addMessage(String message, boolean clear, boolean isLong) {
		engine.addMessage(message,clear,isLong);
	}

	@Override
	public void processCommand(String input) throws IOException {
		logger.log(Level.FINE, "Processing command: {0}", input);
		engine.processCommand(input);
		refreshUI();
	}
	
	@Override
	public void setSavedGameState() {
		engine.setSavedGameState();
		refreshUI();
	}
	
	@Override
	public void setRunningGameState() {
		engine.setRunningGameState();
		refreshUI();
	}
	
	public boolean isGiveState() {
		return engine.isGiveState();
	}
	
	@Override
	public void setShelterGameState() {
		engine.setShelterGameState();
	}
	
	@Override
	public void setRoom(int locationID) {
		logger.log(Level.FINE, "Setting room: {0}", locationID);
		engine.setRoom(locationID);		
		refreshUI();
	}
	
	public void refreshUI() {
		
		/* - This is where it is refreshed, so the refresh UI should check if the 
		 * message state is set, and if so, display the message panel.
		 * Then it should reset it back to normal, unless the end game is triggered
		 * then it will set it to end game state.
		 * Check if messages come from success completion.
		 */
		
		ui.refreshUI(this);
	}
	
	public void closeUI() {
		ui.closeUI();
	}
	
	public void showMap() {
		ui.showMapView(this);
	}
	
	public void restart() {
		logger.log(Level.FINE, "Restarting game");
		this.engine = new GameEngine(
			GameInitialiser.initialiseGame(),
			new Player()
		);
		refreshUI();
	}
	
	@Override
	public void increaseLoadPosition() throws IOException {
		engine.increaseLoadPosition();
		refreshUI();
	}

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
 */
