/*
Title: Island of Secrets Shelter Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import data.GameEntities;
import ui.GameController;

/**
 * A button that navigates the player to a designated shelter location in the game.
 * <p>
 * When triggered, this button sets the game to the running state, moves the player
 * to the shelter location, and displays a message indicating that the player has reached the shelter.
 * </p>
 * <p>
 * Intended to be used as an {@link ActionListener} for UI buttons in the game.
 * </p>
 */
public class ShelterButton implements ActionListener {

    /** The controller that manages game state and UI interactions. */
	private final GameController controller;

    /** The ID of the shelter location the player will be moved to. */
	private final int shelterLocationId;
		
    /**
     * Constructs a ShelterButton for a given game controller and shelter location.
     *
     * @param controller The game controller to use; must not be {@code null}.
     * @param shelterLocationId The ID of the shelter location; must be non-negative.
     * @throws NullPointerException if {@code controller} is {@code null}.
     * @throws IllegalArgumentException if {@code shelterLocationId} is negative.
     */
	public ShelterButton(GameController controller, int shelterLocationId) {
		this.controller = Objects.requireNonNull(controller, "GameController cannot be null");
		this.shelterLocationId = validateLocation(shelterLocationId);
	}
	
    /**
     * Invoked when the button is clicked.
     * <p>
     * Navigates the player to the shelter location, sets the game to running state,
     * and displays the shelter message.
     * </p>
     *
     * @param event The action event triggered by the button click.
     */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		navigateToShelter();
	}
	
    /**
     * Executes the navigation logic to the shelter location.
     * <p>
     * Sets the game state to running, updates the player's room, and adds a message.
     * </p>
     */
	private void navigateToShelter() {
		controller.setRunningGameState();
		controller.addMessage(GameEntities.SHELTER_MESSAGE,true,false);
		controller.setRoom(shelterLocationId);
	}
	
    /**
     * Validates the shelter location ID.
     *
     * @param locationId The location ID to validate.
     * @return The validated location ID.
     * @throws IllegalArgumentException if the location ID is negative.
     */
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
 * 3 December 2025 - Increased version number
 */
