/*
Title: Island of Secrets Game State
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 4 April 2025
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
	private final int responseType;
	private final int panelFlag;
	private final int currentRoom;
	private final boolean lowerLimit;
	private final boolean upperLimit;
	private final boolean initialGameState;
	private final boolean saveGameState;
	private final boolean endGameState;
	private final GameStateProvider state;
	
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
		this.lowerLimit = state.getLowerLimitSavedGames();
		this.upperLimit = state.getUpperLimitSavedGames();
		this.responseType = state.getResponseType();
		this.panelFlag = state.getPanelFlag();
		this.currentRoom = state.getCurrentRoom();
		this.state = state;
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
		return this.lowerLimit;
	}

	@Override
	public boolean getUpperLimitSavedGames() {
		return this.upperLimit;
	}

	@Override
	public int getResponseType() {
		return this.responseType;
	}

	@Override
	public int getPanelFlag() {
		return this.panelFlag;
	}

	@Override
	public boolean getRoomVisited(int roomNumber) {
		return state.getRoomVisited(roomNumber);
	}

	@Override
	public boolean[] getRoomExits(int roomNumber) {
		return state.getRoomExits(roomNumber);
	}

	@Override
	public String getRoomImageType(int roomNumber) {
		return state.getRoomImageType(roomNumber);
	}

	@Override
	public int getCurrentRoom() {
		return this.currentRoom;
	}

}

/* 1 April 2025 - Created File
 * 2 April 2025 - Added up to endGameState
 * 3 April 2025 - Completed Game State
 * 4 April 2025 - Updated for new functions added to interface
 */
