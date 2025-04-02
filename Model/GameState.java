/*
Title: Island of Secrets Game State
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 2 April 2025
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
	private final String[] previousCommands;
	private final List<String> message;
	private final int finalScore;
	private final boolean initialGameState;
	private final boolean saveGameState;
	private final boolean endGameState;
	
	public GameState(GameStateProvider state) {
		this.room = state.getRoom();
		this.items = state.getItems();
		this.exits = state.getExits();
		this.time = state.getTime();
		this.status = state.getStatus();
		this.specialExits = state.getSpecialExits(); 
		this.displayedSavedGames = state.getDisplayedSavedGames();
		this.previousCommands = state.getCommands();
		this.message = state.getMessage();
		this.finalScore = state.getFinalScore();
		this.initialGameState = state.isInitialGameState();
		this.saveGameState = state.isSavedGameState();
		this.endGameState = state.isEndGameState();
		
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
		return this.previousCommands;
	}

	@Override
	public List<String> getMessage() {
		return this.message;
	}
	
	@Override
	public int getFinalScore() {
		return this.finalScore;
	}

	@Override
	public boolean isInitialGameState() {
		return this.initialGameState;
	}

	@Override
	public boolean isSavedGameState() {
		return this.saveGameState;
	}

	@Override
	public boolean isEndGameState() {
		return this.endGameState;
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
 * 2 April 2025 - Added up to endGameState
 */
