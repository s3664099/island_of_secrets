/*
Title: Island of Secrets Command Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 21 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.GameController;

public class CommandButton implements ActionListener {

	private final String command;
	private final GameController controller;
	
	public CommandButton(GameController controller, String command) {
		
		this.command = Objects.requireNonNull(command,"Command cannot be null");
		this.controller = Objects.requireNonNull(controller,"Controller cannot be null");	
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(shouldProcessCommand()) {
			processCommand();
		}
	}
	
	private boolean shouldProcessCommand() {
		return !command.trim().isEmpty();
	}
	
	private void processCommand() {
		try {
			controller.processCommand(command);
		} catch (IOException e) {
			handleCommandError(e);
		}
	}
	
	private void handleCommandError(IOException e) {
		Logger.getLogger(CommandListener.class.getName())
        .log(Level.SEVERE, "Command processing error", e);		
	}

}

/* 25 February 2025 - Created Class
 * 5 March 2025 - Increased version to v4.0
 * 22 March 2025 - Added Error Handling
 * 31 March 2025 - Removed panel from process command
 * 1 April 2024 - Updated listener
 * 21 April 2025 - Updated class based on recommendations by DeepSeek
 */
