/*
Title: Island of Secrets Parsed Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.7
Date: 28 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

public class ParsedCommand {
	
	private final String command;
	private final String[] splitTwoCommand;
	private final String[] splitFullCommand;
	private final String codedCommand;
	private final int verbNumber;
	private final int nounNumber;
	private enum CommandState { NONE, MOVE, SINGLE_COMMAND, MULTIPLE_COMMAND };
	private enum CommandType { NONE,TAKE,GIVE,DROP,EAT,DRINK,RIDE,OPEN,CHOP,ATTACK,KILL,SWIM,SHELTER,
								HELP,SCRATCH,CATCH,RUB,READ,EXAMINE,FILL,SAY,WAIT,WAVE,INFO,
								LOAD,SAVE,QUIT}; //(-5)
	private CommandState commandState = CommandState.NONE;
	private CommandType commandType = CommandType.NONE;
	
	public ParsedCommand(int verbNumber, int nounNumber, String codedCommand, 
						String[] splitCommand, String command) {
	
		this.splitTwoCommand = splitCommand;
		this.splitFullCommand = command.split(" ");
		this.codedCommand = codedCommand;
		this.verbNumber = verbNumber;
		this.nounNumber = nounNumber;
		this.command = command;
		
		setState(verbNumber);
	}
	
	private void setState(int verbNumber) {
		
		if (verbNumber>0 && verbNumber<6) {
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
		
		if (verbNumber == 6 || verbNumber == 7 || verbNumber == 15 || verbNumber == 29) {
			commandType = CommandType.TAKE;
		} else if (verbNumber == 8) {
			commandType = CommandType.GIVE;
		} else if (verbNumber == 9||verbNumber == 10) {
			commandType = CommandType.DROP;
		} else if (verbNumber == 13) {
			commandType = CommandType.RIDE;
		} else if (verbNumber == 14) {
			commandType = CommandType.OPEN;
		} else if (verbNumber>15 && verbNumber <20) {
			commandType = CommandType.CHOP;
		} else if (verbNumber>19 && verbNumber <24) {
			commandType = CommandType.ATTACK;
		} else if (verbNumber == 24) {
			commandType = CommandType.KILL;
		} else if (verbNumber == 27 || verbNumber == 28) {
			commandType = CommandType.HELP;
		} else if (verbNumber == 30 || verbNumber == 31) {
			commandType = CommandType.RUB;
		} else if (verbNumber == 32 || verbNumber == 33) {
			commandType = CommandType.EXAMINE;
		} else if (verbNumber == 34) {
			commandType = CommandType.FILL;
		} else if (verbNumber == 35) {
			commandType = CommandType.SAY;
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
	
	public String[] getSplitTwoCommand() {
		return splitTwoCommand;
	}
	
	public String[] getSplitFullCommand() {
		return splitFullCommand;
	}
	
	public boolean checkNounLength() {
		boolean nounLength = true;
		
		if (splitTwoCommand[1].length()==0) {
			nounLength = false;
		}
		
		return nounLength;
	}
	
	public boolean checkMoveState() {
		return commandState == CommandState.MOVE;
	}
	
	public boolean checkSingleCommandState() {
		return commandState == CommandState.SINGLE_COMMAND;
	}
	
	public boolean checkMultipleCommandState() {
		return commandState == CommandState.MULTIPLE_COMMAND;
	}
	
	public boolean checkNoneCommandType() {
		return commandType == CommandType.NONE;
	}
	
	public boolean checkTake() {
		return commandType == CommandType.TAKE;
	}
	
	public boolean checkDrop() {
		return commandType == CommandType.DROP;
	}
	
	public boolean checkGive() {
		return commandType == CommandType.GIVE;
	}
	
	public boolean checkEat() {
		return commandType == CommandType.EAT;
	}
	
	public boolean checkDrink() {
		return commandType == CommandType.DRINK;
	}
}

/* 24 April 2025 - Created File
 * 1 May 2025 - Added varables and built constructor
 * 2 May 2025 - Added getters for command validation
 * 3 May 2025 - Added getter for codedCommand. Added commandState
 * 7 May 2025 - Added command enums. Set commandType for single Commands
 * 				Tightened check enum methods
 * 8 May 2025 - Added the multi-word commands. Added check for take/drop/give
 * 16 May 2025 - Added code to split all the command, and one to split into verb/noun.
 * 28 May 2025 - Added check for eat and drink
 */