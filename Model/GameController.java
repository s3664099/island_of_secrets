/*
Title: Island of Secrets GameController
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 4 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.io.IOException;
import Interfaces.GameCommandHandler;
import Interfaces.GameUI;

public class GameController implements GameCommandHandler {

	private final GameEngine engine;
	private final GameUI ui;
	
	public GameController(GameEngine engine, GameUI ui) {
		this.engine = engine;
		this.ui = ui;
	}
	
	public GameState getState() {
		return new GameState(engine);
	}
	
	public GameEngine getEngine() {
		return engine;
	}
	
	public int getResponseType() {
		return this.engine.getResponseType();
	}

	@Override
	public void setSavedGameState(boolean isSavedGame) {
		engine.setSavedGameState(isSavedGame);
	}

	@Override
	public void processCommand(String input) throws IOException {
		engine.processCommand(input);
		refreshUI();
	}

	@Override
	public void processGive(String item) {
		engine.processGive(item);
		refreshUI();
	}

	@Override
	public void processShelter(int locationID) {
		engine.processShelter(locationID);
		refreshUI();
	}
	
	public void refreshUI() {
		ui.refreshUI(this);
	}
	
	public void setMap() {
		ui.setMapPanel(this);
	}
}

/* 30 March 2025 - Created file
 * 31 March 2025 - Made gameUI the GameFrame
 * 1 April 2025 - Added getResponseType
 * 3 April 2025 - Updated Code to take GameState
 * 4 April 2025 - Added function to set the mapPanel
 */
