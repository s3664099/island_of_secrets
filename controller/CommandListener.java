/*
Title: Island of Secrets Command Listener
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
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

import ui.GameController;

/**
 * Listens for keyboard input in a {@link JTextField} and delegates
 * commands to the {@link GameController}.  
 * <p>
 * Only processes input when the Enter key is pressed and ignores
 * other keystrokes. Ensures that commands are handled one at a time
 * to prevent re-entrant processing.
 */
public class CommandListener implements KeyListener {

    /**
     * The text field in which the player enters commands.
     */
	private JTextField commandField;

    /**
     * The game controller responsible for processing commands.
     */
	private final GameController controller;
	
    /**
     * Flag to indicate whether input is currently being processed
     * to prevent overlapping command handling.
     */
	private boolean processingInput = false;
	
    /**
     * Constructs a new CommandListener attached to a specific text field
     * and controller.
     *
     * @param commandField the text field where commands are entered; must not be null
     * @param controller   the controller that will process commands; must not be null
     * @throws NullPointerException if either parameter is null
     */
	public CommandListener(JTextField commandField, GameController controller) {

		this.commandField = Objects.requireNonNull(commandField,"CommandField cannot be null");
		this.controller = Objects.requireNonNull(controller,"Controller cannot be null");
	}

    /**
     * Invoked when a key has been pressed. If the Enter key is pressed
     * and no input is currently being processed, it triggers command processing.
     *
     * @param event the key event describing the key press
     */
	@Override
	public void keyPressed(KeyEvent event) {
		
		//Checks if user presses enter
		if (event.getKeyCode() == KeyEvent.VK_ENTER && !processingInput) {
			processInput();
		}		
	}
	
    /**
     * Processes the text currently in the command field. Trims whitespace
     * and sends non-empty commands to the controller. Ensures that only
     * one command is processed at a time.
     */
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
	
    /**
     * Delegates a user command to the {@link GameController} for execution.
     * Any {@link IOException} encountered during processing is logged.
     *
     * @param command the command string to process
     */
	private void handleCommand(String command) {

		try {
			controller.processCommand(command);
		} catch (IOException e) {
			Logger.getLogger(CommandListener.class.getName())
	          .log(Level.SEVERE, "Command processing error", e);
		}
	}

    /**
     * Not used but required by the {@link KeyListener} interface.
     *
     * @param arg0 the key event
     */
	@Override
	public void keyReleased(KeyEvent arg0) {}

    /**
     * Not used but required by the {@link KeyListener} interface.
     *
     * @param arg0 the key event
     */
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
 * 17 September 2025 - Added JavaDocs
 * 3 December 2025 - Increased version number
 */