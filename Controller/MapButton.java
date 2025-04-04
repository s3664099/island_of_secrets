/*
Title: Island of Secrets Map Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 4 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.GameController;
import Model.GameEngine;
import View.GamePanel;

public class MapButton implements ActionListener {

	private static GameController controller;
	
	public MapButton(GameController controller) {
			this.controller = controller;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.setMap();
	}

}

/* 2 February 2025 - Created Class
 * 5 March 2025 - Increased to v4.0
 * 4 April 2025 - Update controller for new style
 */
