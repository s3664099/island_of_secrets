/*
Title: Island of Secrets Parsed Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 7 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

public class ParsedCommand {
	
	private final String command;
	private final String[] splitCommand;
	private final String codedCommand;
	private final int verbNumber;
	private final int nounNumber;
	private enum CommandState { NONE, MOVE, SINGLE_COMMAND, MULTIPLE_COMMAND };
	private enum CommandType { NONE,TAKE,GIVE,DROP,EAT,DRINK,RIDE,OPEN,CHOP,KILL,SWIM,SHELTER,
								HELP,SCRATCH,CATCH,RUB,READ,EXAMINE,FILL,SAY,WAIT,WAVE,INFO,
								LOAD,SAVE,QUIT}; //(-5)
	private CommandState commandState = CommandState.NONE;
	private CommandType commandType = CommandType.NONE;
	
	public ParsedCommand(int verbNumber, int nounNumber, String codedCommand, 
						String[] splitCommand, String command) {
	
		this.splitCommand = splitCommand;
		this.codedCommand = codedCommand;
		this.verbNumber = verbNumber;
		this.nounNumber = nounNumber;
		this.command = command;
		
		setState(verbNumber);
	}
	
	private void setState(int verbNumber) {
		
		if (verbNumber>0 && verbNumber<5) {
			commandState = CommandState.MOVE;
		} else if (verbNumber == 11 || verbNumber == 12 || 
					verbNumber == 25 || verbNumber == 26 ||
					verbNumber == 37 || verbNumber == 37 ||
					verbNumber == 38 || verbNumber == 39 ||
					verbNumber == 40 || verbNumber == 41 ||
					verbNumber == 42) {
			commandState = CommandState.SINGLE_COMMAND;
			setSingleCommand(verbNumber);
		} else {
			commandState = CommandState.MULTIPLE_COMMAND;
		}
	}
	
	public void setSingleCommand(int verbNumber) {
		
		if (verbNumber == 11) {
			commandType = CommandType.EAT;
		} else if (verbNumber == 12) {
			commandType = CommandType.DRINK;
		} else if (verbNumber == 39) {
			commandType = CommandType.INFO;
		} else if (verbNumber == 38) {
			commandType = CommandType.WAVE;
		} else if (verbNumber == 40) {
			commandType = CommandType.LOAD;
		} else if (verbNumber == 41) {
			commandType = CommandType.SAVE;
		} else if (verbNumber == 36 || verbNumber == 37) {
			commandType = CommandType.WAIT;
		} else if (verbNumber == 42) {
			commandType = CommandType.QUIT;
		} else if (verbNumber == 25) {
			commandType = CommandType.SWIM;
		} else if (verbNumber == 26) {
			commandType = CommandType.SHELTER;
		}
	}
	
	public void setMultipleCommand(int verbNumber) {
		
	}
	
	public int getVerbNumber() {
		return verbNumber;
	}
	
	public int getNounNumber() {
		return nounNumber;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getCodedCommand() {
		return codedCommand;
	}
	
	public String[] getSplitCommand() {
		return splitCommand;
	}
	
	public boolean checkNounLength() {
		boolean nounLength = true;
		
		if (splitCommand[1].length()==0) {
			nounLength = false;
		}
		
		return nounLength;
	}
	
	public boolean checkMoveState() {
		
		boolean moveState = false;
		if (commandState == CommandState.MOVE) {
			moveState = true;
		}
		return moveState;
	}
	
	public boolean checkSingleCommandState() {
		
		boolean moveState = false;
		if (commandState == CommandState.SINGLE_COMMAND) {
			moveState = true;
		}
		return moveState;
	}
	
	public boolean checkMultipleCommandState() {
		
		boolean moveState = false;
		if (commandState == CommandState.MULTIPLE_COMMAND) {
			moveState = true;
		}
		return moveState;
	}
}

/* 24 April 2025 - Created File
 * 1 May 2025 - Added varables and built constructor
 * 2 May 2025 - Added getters for command validation
 * 3 May 2025 - Added getter for codedCommand. Added commandState
 * 7 May 2025 - Added command enums
 */