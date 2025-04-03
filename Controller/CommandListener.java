/*
Title: Island of Secrets Command Listener
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 1 April 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.JTextField;
import Model.GameController;

public class CommandListener implements KeyListener {

	private JTextField commandField;
	private final GameController controller;
	
	public CommandListener(JTextField commandField, GameController controller) {

		this.commandField = commandField;
		this.controller = controller;
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		
		//Checks if user presses enter
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			
			String command = this.commandField.getText().trim();
			this.commandField.setText("");
			
			
			if (controller.getResponseType()==1) {
				controller.processGive(command);
			} else if (controller.getResponseType()==2) {
				//commander.processShelter(command);
			} else {
				try {
					controller.processCommand(command);
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
 * 31 March 2025 - Updated file to handle Decoupling
 * 1 April 2025 - Updated listener to make it tighter
 */