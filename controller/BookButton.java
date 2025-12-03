/*
Title: Island of Secrets Book Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package controller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import ui.GameController;
import view.GamePanel;

/**
 * A button that opens the official "Island of Secrets" book link in the user's default browser
 * and refreshes the main game view when clicked.
 * <p>
 * This class implements {@link ActionListener} to respond to GUI button clicks.
 * It uses {@link Desktop#browse(URI)} to open the book URL and logs any errors encountered.
 * </p>
 */
public class BookButton implements ActionListener {

    /**
     * Logger for recording errors or informational messages.
     */
	private static final Logger logger = Logger.getLogger(ShowMainViewButton.class.getName());
	
    /**
     * The main game panel used to refresh the UI after the button is clicked.
     */
	private final GamePanel panel;
	
    /**
     * The game controller used to update or interact with the game state.
     */
	private final GameController controller;
	
    /**
     * URL of the "Island of Secrets" book to open in the browser.
     */
	private static final String BOOK_URL = "https://archive.org/details/island-of-secrets_202303";
	
    /**
     * Constructs a new {@code BookButton} with the specified game panel and controller.
     *
     * @param panel      the {@link GamePanel} to refresh after the action; must not be {@code null}
     * @param controller the {@link GameController} used to interact with the game; must not be {@code null}
     * @throws NullPointerException if either {@code panel} or {@code controller} is {@code null}
     */
	public BookButton(GamePanel panel,GameController controller) {		
		this.panel = Objects.requireNonNull(panel, "GamePanel cannot be null");
		this.controller = Objects.requireNonNull(controller, "GameController cannot be null");
	}
	
    /**
     * Called when the button is clicked.
     * Opens the book URL in the user's default browser and refreshes the main game view.
     *
     * @param event the {@link ActionEvent} triggered by the button click
     */
	@Override
	public void actionPerformed(ActionEvent event) {
		openBookLink();
		refreshGameView();
	}
	
    /**
     * Attempts to open the {@link #BOOK_URL} in the user's default browser.
     * Logs an error if the operation fails or if the desktop is not supported.
     */
	private void openBookLink() {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(Objects.requireNonNull(BOOK_URL,"BOOK_URL cannot be null")));
			} catch (IOException|URISyntaxException e) {
				handleLinkError(e);
			}
		} else {
			logger.log(Level.SEVERE, "Desktop not supported.");
		}
	}
	
    /**
     * Refreshes the main view of the game using the {@link #panel} and {@link #controller}.
     */
	private void refreshGameView() {
		panel.refreshMainView(controller);
	}
	
    /**
     * Handles exceptions thrown when attempting to open the book link.
     * Logs the exception at the SEVERE level.
     *
     * @param e the exception that occurred
     */
	private void handleLinkError(Exception e) {
		logger.log(Level.SEVERE, "Failed to open book link.", e);
	}
}

/* 2 March 2025 - Created Class
 * 5 March 2025 - Increased to v4.0
 * 26 March 2025 - Commented out code to enable to run
 * 7 April 2025 - Button to open webpage now works.
 * 21 April 2025 - Updated based on DeepSeek recommendations
 * 21 September 2025 - Updated code based on recommendations
 * 					 - Added JavaDocs
 * 3 December 2025 - Increased version number
 */
