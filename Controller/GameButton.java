/*
Title: Island of Secrets Game Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 5 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.GameEngine;
import View.MainGamePanel;

public class GameButton implements ActionListener {

	private GameEngine game;
	private MainGamePanel panel;
	
	public GameButton(GameEngine game, MainGamePanel panel) {
		
		this.panel = panel;
		this.game = game;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//game.getGame().setGameDisplay(false);
		//game.setGamePanel(this.panel);
	}

}

/* 10 February 2025 - Created Class
 * 26 February 2025 - Added code to
 * 5 March 2025 - Increased to v4.0
 * 26 March 2025 - Commented out code to enable to run
 */
