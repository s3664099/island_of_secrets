/*
Title: Island of Secrets Read Operation Interface
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 25 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Interfaces;

import java.util.List;

//Read Only Operations
public interface GameStateProvider {

	//Game Status Methods
	public int getFinalScore();
	public String getRoom();
	public String getItems();
	public String getExits();
	public String getTime();
	public String getStatus();
	public String getSpecialExits();
	public String[] getDisplayedSavedGames();
	public String[] getCommands();
	public List<String> getMessage();
	
	//GameUI Methods
	public boolean isInitialGameState();
	public boolean isSavedGameState();
	public boolean isEndGameState();
	public boolean getLowerLimitSavedGames();
	public boolean getUpperLimitSavedGames();
	public int getResponseType();
	public int getPanelFlag();
}

/* 24 March 2025 - Created File
 * 25 March 2025 - Added further required methods
 */
