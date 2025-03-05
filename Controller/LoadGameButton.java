/*
Title: Island of Secrets Load Game Button
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
import View.GamePanel;

public class LoadGameButton implements ActionListener {

	private GameEngine game;
	private GamePanel panel;
	private String gameName;
	
	public LoadGameButton(GameEngine game, GamePanel panel,String gameName) {
		this.panel = panel;
		this.game = game;
		this.gameName = gameName.substring(0,gameName.length()-4);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		game.processCommand("load "+gameName, panel);
	}

}

/* 26 February 2025 - Created Class
 * 5 March 2025 - Increased to v4.0
 */
