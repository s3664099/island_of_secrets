/*
Title: Island of Secrets Search Game Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 13 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import Model.GameController;

public class SearchGameButton implements ActionListener {

	private GameController controller;
	private boolean next;
	
	public SearchGameButton(GameController controller, boolean next) {
		this.controller = controller;
		this.next = next;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		try {
			if (next) {
				controller.increaseLoadPosition();;
			} else {
				controller.decreaseLoadPosition();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/* 26 February 2025 - Created Class
 * 13 April 2025 - Updated class to handle new architecture
 */
