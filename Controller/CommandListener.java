/*
Title: Island of Secrets Command Listener
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.0
Date: 31 January 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import Model.GameEngine;
import View.GamePanel;

public class CommandListener implements KeyListener {

	JTextField text;
	GameEngine game;
	GamePanel gamePanel;
	
	public CommandListener(JTextField text, GameEngine game, GamePanel gameFrame) {
		this.text = text;
		this.game = game;
		this.gamePanel = gameFrame;
	}
	
	@Override
	public void keyPressed(KeyEvent evt) {
		
		//Checks if user presses enter
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			
			String command = this.text.getText();
			this.text.setText("");
			
			if (game.getResponseType()==1) {
				game.processGive(command, gamePanel);
			} else if (game.getResponseType()==2) {
				game.processShelter(command,gamePanel);
			} else {
				game.processCommand(command, gamePanel);
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	    // Method required by KeyListener but not used
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	    // Method required by KeyListener but not used		
	}

}

/* 6 November 2024 - Created File
 * 22 December 2024 - Added check for special response requests
 * 23 December 2024 - Added process for sheltering
 * 					  Updated to version 2.
 * 19 January 2025 - Added notes for unused methods.
 * 31 January 2025 - Completed Testing and increased version
 */