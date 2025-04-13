/*
Title: Island of Secrets Game Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 7 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.GameController;
import View.GamePanel;

public class GameButton implements ActionListener {

	private GameController controller;
	private GamePanel panel;
	
	public GameButton(GameController controller, GamePanel panel) {
		
		this.panel = panel;
		this.controller = controller;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		panel.showMainView();
		controller.setSavedGameState(false);
		panel.refreshMainView(controller);
	}

}

/* 10 February 2025 - Created Class
 * 26 February 2025 - Added code to
 * 5 March 2025 - Increased to v4.0
 * 26 March 2025 - Commented out code to enable to run
 * 7 April 2025 - Action now returns player to the main screen.
 */
