/*
Title: Island of Secrets Load Game Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 31 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import Model.GameEngine;
import View.MainGamePanel;

public class LoadGameButton implements ActionListener {

	private GameEngine game;
	private MainGamePanel panel;
	private String gameName;
	
	public LoadGameButton(GameEngine game, MainGamePanel panel,String gameName) {
		this.panel = panel;
		this.game = game;
		this.gameName = gameName.substring(0,gameName.length()-4);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			game.processCommand("load "+gameName);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}

/* 26 February 2025 - Created Class
 * 5 March 2025 - Increased to v4.0
 * 22 March 2025 - Added error handling
 * 31 March 2025 - Removed panel from process command
 */
