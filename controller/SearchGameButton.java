/*
Title: Island of Secrets Search Game Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 21 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchGameButton implements ActionListener {

	private final GameController controller;
	private final boolean shouldMoveNext;
	
	public SearchGameButton(GameController controller, boolean shouldMoveNext) {
		this.controller = Objects.requireNonNull(controller,"controller cannot be null");
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
	        handleNavigationError(e, "I/O error");
	    } catch (Exception e) {
	        handleNavigationError(e, "Unexpected error");
	    }
	}
	
	private void executeNavigation() throws IOException {
		if (shouldMoveNext) {
			controller.increaseLoadPosition();
		} else {
			controller.decreaseLoadPosition();
		}
	}
	
	private void handleNavigationError(Exception e, String type) {
	    Logger.getLogger(SearchGameButton.class.getName())
	          .log(Level.SEVERE,
	               String.format("%s while navigating saved games. Direction: %s",
	                             type, shouldMoveNext ? "NEXT" : "PREVIOUS"),
	               e);
	}
}

/* 26 February 2025 - Created Class
 * 13 April 2025 - Updated class to handle new architecture
 * 21 April 2025 - Updated based on DeepSeek recommendations
 */
