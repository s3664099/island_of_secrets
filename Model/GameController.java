/*
Title: Island of Secrets GameController
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 30 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.io.IOException;

import javax.swing.SwingUtilities;

import Interfaces.GameCommandHandler;
import Interfaces.GameUI;

public class GameController implements GameCommandHandler {

	private final GameEngine engine;
	private final GameUI ui;
	
	public GameController(GameEngine engine, GameUI ui) {
		this.engine = engine;
		this.ui = ui;
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

    private void refreshUI() {
        SwingUtilities.invokeLater(ui::refresh);
    }
	
}

/* 30 March 2025 - Created file
 */
