/*
Title: Island of Secrets Game Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.1
Date: 26 February 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.GameEngine;
import View.GamePanel;

public class GameButton implements ActionListener {

	private GameEngine game;
	private GamePanel panel;
	
	public GameButton(GameEngine game, GamePanel panel) {
		
		this.panel = panel;
		this.game = game;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		game.getGame().setGameDisplay(false);
		game.setGamePanel(this.panel);
	}

}

/* 10 February 2025 - Created Class
 * 26 February 2025 - Added code to 
 */
