/*
Title: Island of Secrets Quit Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.5
Date: 21 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class QuitButton implements ActionListener {
	
	private final GameController controller;
	private final Boolean shouldRestart;
	
	public QuitButton(GameController controller,boolean shouldRestart) {
		this.controller = Objects.requireNonNull(controller,"GameController cannot be null");
		this.shouldRestart = shouldRestart;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		handleQuitAction();
	}
	
	private void handleQuitAction() {
		if (shouldRestart) {
			controller.restart();
		} else {
			controller.closeUI();
		}
	}
}

/* 23 December 2024 - Create File
 * 31 January 2025 - Completed Testing and increased version
 * 17 February 2025 - Added option to restart the game
 * 5 March 2025 - Increased to v4.0
 * 15 March 2025 - Added the Game Initialiser
 * 21 March 2025 - Removed extraneous include
 * 8 April 2025 - Add the quit functionality
 * 9 April 2025 - Added the restart functionality
 * 21 April 2025 - Updated based on DeepSeek's recommendations.
 */
