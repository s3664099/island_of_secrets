/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.3
Date: 5 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import Model.GameEngine;
import View.GameFrame;

public class CommandListener implements KeyListener {

	JTextField text;
	GameEngine game;
	GameFrame gameFrame;
	
	public CommandListener(JTextField text, GameEngine game, GameFrame gameFrame) {
		this.text = text;
		this.game = game;
		this.gameFrame = gameFrame;
	}
	
	@Override
	public void keyPressed(KeyEvent evt) {
		
		//Checks if user presses enter
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			
			String command = this.text.getText();
			this.text.setText("");
			game.processCommand(command, gameFrame);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}

/* 6 November 2024 - Created File
 * 
 */