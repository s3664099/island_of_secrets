/*
Title: Island of Secrets Random Exit Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

import data.Constants;

/**
 * Handles the random generation of exits for a location in the game.
 * 
 * <p>This class selects a predefined exit pattern from a hard-coded array,
 * ensuring controlled randomness rather than completely unpredictable exits.</p>
 * 
 * <p>The implementation depends on {@code Constants.NUMBER_EXITS} and 
 * {@code Constants.RANDOM_EXIT_COMBO}, which must align with the defined exit array.</p>
 */
public class RandomExitHandler implements Serializable {

	private static final long serialVersionUID = 3367548012798466733L;
	
	 /** Random instance used for exit generation (shared across all instances). */
	private static final Random rand = new Random();
	
    /**
     * Generates a random exit configuration for a location.
     * 
     * <p>The exits are selected from a predefined exit array. The method ensures 
     * that the number of exits returned matches {@code Constants.NUMBER_EXITS}.</p>
     * 
     * @return a boolean array of exits, where {@code true} indicates the presence of an exit.
     */
	public boolean[] generateRandomExits() {
		
		boolean[] exitArray = {false,true,false,false,false,true,false,true,true};
		boolean[] randomExits = new boolean[Constants.NUMBER_EXITS];
		int randExit = rand.nextInt(Constants.RANDOM_EXIT_COMBO);
		
		for (int i=0;i<Constants.NUMBER_EXITS;i++) {
			randomExits[i]=exitArray[randExit+i];
		}
		return Arrays.copyOf(randomExits, randomExits.length);
	}
}

/*
 * 17 March 2025 - Created new file
 * 20 August 2025 - Updated class and added JavaDocs
 * 3 December 2025 - Increased version number
 */
