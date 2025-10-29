/*
Title: Island of Secrets Quit Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 9 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.GameController;

public class QuitButton implements ActionListener {
	
	private GameController controller;
	private Boolean restart;
	
	public QuitButton(GameController controller,boolean restart) {
		this.controller = controller;
		this.restart = restart;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (!restart) {
			controller.closeUI();
		} else {
			controller.restart();
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
 */
