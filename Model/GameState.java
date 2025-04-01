/*
Title: Island of Secrets Game State
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 1 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.util.List;

import Interfaces.GameStateProvider;

public class GameState implements GameStateProvider {

	private final String room;
	private final String items;
	private final String exits;
	private final String time;
	private final String status;
	private final String specialExits;
	private final String[] displayedSavedGames;
	private final String[] getCommands;
	
	public GameState(GameStateProvider state) {
		this.room = state.getRoom();
		this.items = state.getItems();
		this.exits = state.getExits();
		this.time = state.getTime();
		this.status = state.getStatus();
		this.specialExits = state.getSpecialExits(); 
		this.displayedSavedGames = state.getDisplayedSavedGames();
		this.getCommands = state.getCommands();
		
	}
	
	@Override
	public String getRoom() {
		return this.room;
	}

	@Override
	public String getItems() {
		return this.items;
	}

	@Override
	public String getExits() {
		return this.exits;
	}

	@Override
	public String getSpecialExits() {
		return this.specialExits;
	}
	
	@Override
	public String getTime() {
		return this.time;
	}

	@Override
	public String getStatus() {
		return this.status;
	}

	@Override
	public String[] getDisplayedSavedGames() {
		return this.displayedSavedGames;
	}

	@Override
	public String[] getCommands() {
		return this.getCommands;
	}

	@Override
	public List<String> getMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getFinalScore() {
		
		return 0;
	}

	@Override
	public boolean isInitialGameState() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSavedGameState() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEndGameState() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getLowerLimitSavedGames() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getUpperLimitSavedGames() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getResponseType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPanelFlag() {
		// TODO Auto-generated method stub
		return 0;
	}

}

/* 1 April 2025 - Created File
 */
