package Test;

import java.util.List;

import Interfaces.GameStateProvider;

public class RoomPanelTest implements GameStateProvider {

	@Override
	public int getFinalScore() {
		
		return 0;
	}

	@Override
	public String getRoom() {
		
		return "A Kitchen";
	}

	@Override
	public String getItems() {
		
		return "A Pig, Pig, pig pig, A Pig, Pig, pig pig, A Pig, Pig, pig pig, A Pig, Pig, pig pig, A Pig, Pig, pig pig,A Pig, Pig, pig pig, A Pig, Pig, pig pig, A Pig, Pig, pig pig, A Pig, Pig, pig pig, A Pig, Pig, pig pig, A Pig, Pig, pig pig, A Pig, Pig, pig pig, A Pig, Pig, pig pig, A Pig, Pig, pig pig,";
	}

	@Override
	public String getExits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSpecialExits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getDisplayedSavedGames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getCommands() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMessage() {
		// TODO Auto-generated method stub
		return null;
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
