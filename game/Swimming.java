/*
Title: Island of Secrets Initialise Swimming Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import java.util.Random;

/**
 * Represents a swimming challenge or obstacle within the game.
 *
 * The {@code Swimming} class generates a random swim distance that
 * the player must reach, tracks the player's swim progress, and
 * checks whether the required distance has been achieved.
 */
public class Swimming {
	
	private int targetDistance = 0;
	private int currentProgress = 0;
	private Random rand = new Random();

    /**
     * Constructs a new {@code Swimming} instance with a random
     * target swim distance between 1 and 5.
     */
	public Swimming() {
		this.targetDistance = rand.nextInt(5)+1;
	}
	
    /**
     * Advances the player's swimming position by one step.
     */
	public void swim() {
		this.currentProgress++;
	}
	
    /**
     * Checks whether the player has successfully swum far enough.
     *
     * Success occurs if the player's current swim position exceeds
     * half the required distance and the player's strength is greater than zero.
     *
     * @param strength the player's current strength level
     * @return {@code true} if the swimming attempt succeeds, {@code false} otherwise
     */
	public boolean checkPosition(float strength) {
		return (this.targetDistance / 2) < this.currentProgress && strength > 0;
	}
}

/*15 March 2025 - Created File
 * 20 July 2025 - Moved random position here.
 * 18 August 2025 - Added JavaDocs and cleaned up and fixed up class
 * 3 December 2025 - Increased version number
 */
