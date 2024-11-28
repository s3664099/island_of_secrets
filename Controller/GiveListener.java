/*
Title: Island of Secrets Give Listener
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.1
Date: 28 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import Model.GameEngine;
import View.GamePanel;
import View.GivePanel;

public class GiveListener extends CommandListener implements KeyListener {
	
	public GiveListener(JTextField text, GameEngine game, GivePanel gameFrame) {
		super(text,game,gameFrame);
	}
	
	@Override
	public void keyPressed(KeyEvent evt) {
		
		//Checks if user presses enter
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			String command = this.text.getText();
			game.processGive(command, gamePanel);
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

/* 27 November 2024 - Created File
 * 28 November 2024 - Sent text to command processing
 */