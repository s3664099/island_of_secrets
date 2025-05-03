/*
Title: Island of Secrets Parsed Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 3 May 2025
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
	private enum CommandType { NONE,TAKE,DROP};
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
		} else {
			commandState = CommandState.MULTIPLE_COMMAND;
		}
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
 */