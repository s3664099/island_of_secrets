/*
Title: Island of Secrets Write Operation Interface
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 10 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Interfaces;

import java.io.IOException;

//Write Operations
public interface GameCommandHandler {

	public void setSavedGameState(boolean isSavedGame);
	public void processCommand(String input) throws IOException;
	public void processGive(String item);
	public void processShelter(int locationID);
	public void setRoom(int locationID);
	
}

/* 24 March 2025 - Created File
 * 25 March 2025 - Added method for setting saved game state
 * 10 April 2025 - Added setRoom function
 */
