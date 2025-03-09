/*
Title: Island of Secrets Command Button
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

public class CommandButton implements ActionListener {

	private GameEngine game;
	private GamePanel panel;
	private String command;
	
	public CommandButton(GameEngine game, GamePanel panel, String command) {
		
		this.panel = panel;
		this.game = game;
		this.command = command;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		game.processCommand(command, panel);
	}

}

/* 25 February 2025 - Created Class
 * 5 March 2025 - Increased version to v4.0
 */
