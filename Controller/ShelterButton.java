/*
Title: Island of Secrets Shelter Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 10 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.GameController;

public class ShelterButton implements ActionListener {

	private GameController controller;
	private int location;
	
	public ShelterButton(GameController controller, int location) {
		
		this.controller = controller;
		this.location = location;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.setRoom(location);
	}
}

/* 24 February 2025 - Created Class
 * 5 March 2025 - Increased to v4.0
 * 10 April 2025 - Updated Listener
 */
