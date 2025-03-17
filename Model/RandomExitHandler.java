/*
Title: Island of Secrets Random Exit Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 17 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.util.Random;

import Data.Constants;

public class RandomExitHandler {
	private Random rand = new Random();
	
	//Generates location's exits from a array randomly
	public boolean[] generateRandomExits() {
		
		boolean[] exitArray = {false,true,false,false,false,true,false,true,true};
		boolean[] randomExits = new boolean[Constants.NUMBER_EXITS];
		int randExit = rand.nextInt(Constants.RANDOM_EXIT_COMBO);
		
		for (int i=0;i<Constants.NUMBER_EXITS;i++) {
			randomExits[i]=exitArray[randExit+i];
		}
		return randomExits;
	}
}

/*
 * 17 March 2025 - Created new file 
 */
