/*
Title: Island of Secrets Command Listener
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 27 March 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JTextField;

import Interfaces.GameCommandHandler;
import Interfaces.GameStateProvider;
import Model.GameEngine;
import View.CommandPanel;
import View.GamePanel;

public class CommandListener implements KeyListener {

	private JTextField commandField;
	private final GameCommandHandler commander;
	private final GameStateProvider state;
	private final GamePanel gamePanel;
	
	public CommandListener(JTextField commandField, GameCommandHandler commander, GameStateProvider state,GamePanel GamePanel) {
		this.commandField = commandField;
		this.commander = commander;
		this.state = state;
		this.gamePanel = GamePanel;
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		
		//Checks if user presses enter
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			
			String command = this.commandField.getText();
			this.commandField.setText("");
			
			if (state.getResponseType()==1) {
				commander.processGive(command, gamePanel);
			} else if (state.getResponseType()==2) {
				commander.processShelter(command,gamePanel);
			} else {
				try {
					commander.processCommand(command, gamePanel);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
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
 * 5 March 2025 - Increased to v4.0
 * 22 March 2025 - Added error handling
 * 27 March 2025 - Update file for new stule
 */