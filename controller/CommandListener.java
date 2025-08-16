/*
Title: Island of Secrets Command Listener
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.7
Date: 30 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextField;

public class CommandListener implements KeyListener {

	private JTextField commandField;
	private final GameController controller;
	private boolean processingInput = false;
	
	public CommandListener(JTextField commandField, GameController controller) {

		this.commandField = Objects.requireNonNull(commandField,"CommandField cannot be null");
		this.controller = Objects.requireNonNull(controller,"Controller cannot be null");
	}

	@Override
	public void keyPressed(KeyEvent event) {
		
		//Checks if user presses enter
		if (event.getKeyCode() == KeyEvent.VK_ENTER && !processingInput) {
			processInput();
		}		
	}
	
	private void processInput() {
		try {
			processingInput = true;
			String command = commandField.getText().trim();
			
			if (!command.isEmpty()) {
				commandField.setText("");
				handleCommand(command);
			}
		} finally {
			processingInput = false;
		}
	}
	
	private void handleCommand(String command) {

		try {
			controller.processCommand(command);
		} catch (IOException e) {
			Logger.getLogger(CommandListener.class.getName())
	          .log(Level.SEVERE, "Command processing error", e);
		}
	}

    // Empty implementations remain for interface compliance
	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

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
 * 21 April 2025 - Updated based on recommendations by deepSeek
 * 23 April 2025 - Removed process shelter and give
 * 30 June 2025 - Removed separate process for give
 */