/*
Title: Island of Secrets Command Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 1 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import Model.GameController;

public class CommandButton implements ActionListener {

	private String command;
	private GameController controller;
	
	public CommandButton(GameController controller, String command) {
		
		this.command = command;
		this.controller = controller;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			controller.processCommand(command);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}

/* 25 February 2025 - Created Class
 * 5 March 2025 - Increased version to v4.0
 * 22 March 2025 - Added Error Handling
 * 31 March 2025 - Removed panel from process command
 * 1 April 2024 - Updated listener
 */
