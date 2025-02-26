/*
Title: Island of Secrets Search Game Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.0
Date: 26 February 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.GameEngine;
import View.GamePanel;

public class SearchGameButton implements ActionListener {

	private GameEngine game;
	private GamePanel panel;
	private boolean next;
	
	public SearchGameButton(GameEngine game, GamePanel panel, boolean next) {
		this.panel = panel;
		this.game = game;
		this.next = next;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (next) {
			game.getGame().increaseCount();
		} else {
			game.getGame().descreaseCount();
		}
				
		game.processCommand("load", panel);
	}

}

/* 26 February 2025 - Created Class
 */
