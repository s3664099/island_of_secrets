/*
Title: Island of Secrets Search Game Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 21 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.GameController;

public class SearchGameButton implements ActionListener {

	private final GameController controller;
	private final boolean shouldMoveNext;
	
	public SearchGameButton(GameController controller, boolean shouldMoveNext) {
		this.controller = controller;
		this.shouldMoveNext = shouldMoveNext;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		navigateSavedGames();
	}
	
	private void navigateSavedGames() {
		try {
			executeNavigation();
		} catch (IOException e) {
			handleNavigationError(e);
		}
	}
	
	private void executeNavigation() throws IOException {
		if (shouldMoveNext) {
			controller.increaseLoadPosition();;
		} else {
			controller.decreaseLoadPosition();
		}
	}
	
    private void handleNavigationError(IOException e) {
    	Logger.getLogger(SearchGameButton.class.getName())
        .log(Level.SEVERE, "Game navigation failed", e);
    }	
	
}

/* 26 February 2025 - Created Class
 * 13 April 2025 - Updated class to handle new architecture
 * 21 April 2025 - Updated based on DeepSeek recommendations
 */
