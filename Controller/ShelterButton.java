/*
Title: Island of Secrets Shelter Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 25 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import UISupport.GameController;

public class ShelterButton implements ActionListener {

	private final GameController controller;
	private final int shelterLocationId;
	
	public ShelterButton(GameController controller, int shelterLocationId) {
		
		this.controller = Objects.requireNonNull(controller, "GameController cannot be null");
		this.shelterLocationId = validateLocation(shelterLocationId);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		navigateToShelter();
	}
	
	private void navigateToShelter() {
		controller.setRunningGameState();
		controller.addMessage("You reach shelter",true,false);
		controller.setRoom(shelterLocationId);
	}
	
	private int validateLocation(int locationId) {
		if (locationId<0) {
			throw new IllegalArgumentException("Shelter Location ID cannot be negative");
		}
		return locationId;
	}
}

/* 24 February 2025 - Created Class
 * 5 March 2025 - Increased to v4.0
 * 10 April 2025 - Updated Listener
 * 21 April 2025 - Update class based on DeepSeek recommendations
 * 25 April 2025 - Changed based on changes to Enums
 */
