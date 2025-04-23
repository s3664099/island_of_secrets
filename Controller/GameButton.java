/*
Title: Island of Secrets Game Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 21 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import UISupport.GameController;
import View.GamePanel;

public class GameButton implements ActionListener {

	private final GameController controller;
	private final GamePanel panel;
	
	public GameButton(GameController controller, GamePanel panel) {
		
		this.panel = Objects.requireNonNull(panel, "GamePanel cannot be null");
		this.controller = Objects.requireNonNull(controller, "GameController cannot be null");
		
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		returnToMainGameView();
	}
	
	private void returnToMainGameView() {
		showMainView();
		updateGameState();
		refreshView();
	}
	
	private void showMainView() {
		panel.showMainView();
	}
	
	private void updateGameState() {
		controller.setSavedGameState(false);
	}
	
	private void refreshView() {
		panel.refreshMainView(controller);
	}
}

/* 10 February 2025 - Created Class
 * 26 February 2025 - Added code to
 * 5 March 2025 - Increased to v4.0
 * 26 March 2025 - Commented out code to enable to run
 * 7 April 2025 - Action now returns player to the main screen.
 * 13 April 2025 - Updated button for changing the savedGameState
 * 21 April 2025 - Updated based on deepseek recommendations
 */
