/*
Title: Island of Secrets Search Game Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 22 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import Model.GameEngine;
import View.MainGamePanel;

public class SearchGameButton implements ActionListener {

	private GameEngine game;
	private MainGamePanel panel;
	private boolean next;
	
	public SearchGameButton(GameEngine game, MainGamePanel panel, boolean next) {
		this.panel = panel;
		this.game = game;
		this.next = next;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		try {
			if (next) {
				game.increaseLoad(panel);;
			} else {
				game.decreaseLoad(panel);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/* 26 February 2025 - Created Class
 */
