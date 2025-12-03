/*
Title: Island of Secrets Search Game Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import ui.GameController;

/**
 * A Swing {@link ActionListener} that allows the user to navigate through
 * saved-game listings in the UI.
 * <p>
 * This button moves either forward or backward through the available
 * saved games depending on the {@code shouldMoveNext} flag supplied
 * at construction time. It delegates all navigation logic to the
 * {@link GameController}.
 */
public class SearchGameButton implements ActionListener {

	private final GameController controller;
	private final boolean shouldMoveNext;
	
    /**
     * Creates a new SearchGameButton.
     *
     * @param controller     the game controller responsible for updating the saved-game
     *                       list and managing navigation state; must not be {@code null}.
     * @param shouldMoveNext {@code true} to advance to the next group of saved games,
     *                       {@code false} to move to the previous group.
     * @throws NullPointerException if {@code controller} is {@code null}.
     */
	public SearchGameButton(GameController controller, boolean shouldMoveNext) {
		this.controller = Objects.requireNonNull(controller,"controller cannot be null");
		this.shouldMoveNext = shouldMoveNext;
	}
	
    /**
     * Invoked when the user activates the button (e.g., via a mouse click or
     * keyboard shortcut). Attempts to navigate through the saved-game list
     * and logs any errors encountered.
     *
     * @param event the action event that triggered this listener.
     */
	@Override
	public void actionPerformed(ActionEvent event) {
		navigateSavedGames();
	}
	
    /**
     * Performs the navigation, catching and logging both checked and unexpected
     * exceptions so that the UI remains responsive even if an error occurs.
     */
	private void navigateSavedGames() {
		try {
			executeNavigation();
	    } catch (IOException e) {
	        handleNavigationError(e, "I/O error");
	    } catch (Exception e) {
	        handleNavigationError(e, "Unexpected error");
	    }
	}
	
    /**
     * Executes the actual navigation operation by calling the appropriate
     * method on the {@link GameController}.
     *
     * @throws IOException if the controller encounters an I/O problem while
     *                     updating the saved-game list.
     */
	private void executeNavigation() throws IOException {
		if (shouldMoveNext) {
			controller.increaseLoadPosition();
		} else {
			controller.decreaseLoadPosition();
		}
	}
	
    /**
     * Logs navigation errors with severity level {@link java.util.logging.Level#SEVERE}.
     *
     * @param e    the exception that occurred.
     * @param type a short description of the error type (for example,
     *             {@code "I/O error"} or {@code "Unexpected error"}).
     */
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
 * 19 September 2025 - Updated error handling & added JavaDocs
 * 3 December 2025 - Increased version number
 */
