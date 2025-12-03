/*
Title: Island of Secrets Game Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import view.GamePanel;

/**
 * An ActionListener for switching the UI to the main game view.
 * Encapsulates the action of showing the main view in {@link GamePanel}.
 */
public class ShowMainViewButton implements ActionListener {

	private static final Logger logger = Logger.getLogger(ShowMainViewButton.class.getName());
	private final GamePanel panel;
	
    /**
     * Constructs a GameButton that switches to the main game view when clicked.
     *
     * @param panel the GamePanel to display the main view; must not be null
     * @throws NullPointerException if panel is null
     */
	public ShowMainViewButton(GamePanel panel) {
		this.panel = Objects.requireNonNull(panel, "GamePanel cannot be null");		
	}
	
    /**
     * Invoked when the button is pressed. Attempts to show the main view,
     * logging any unexpected errors to prevent UI crashes.
     *
     * @param event the ActionEvent triggered by the button press
     */
	@Override
	public void actionPerformed(ActionEvent event) {
        try {
            panel.showMainView();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to show main game view", e);
        }
	}
}

/* 10 February 2025 - Created Class
 * 26 February 2025 - Added code to
 * 5 March 2025 - Increased to v4.0
 * 26 March 2025 - Commented out code to enable to run
 * 7 April 2025 - Action now returns player to the main screen.
 * 13 April 2025 - Updated button for changing the savedGameState
 * 21 April 2025 - Updated based on deepseek recommendations
 * 25 April 2025 - Changed based on updated to Enums
 * 27 July 2025 - Removed functions not used
 * 21 September 2025 - 
 * 3 December 2025 - Increased version number
 */
