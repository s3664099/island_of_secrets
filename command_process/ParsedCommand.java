/*
Title: Island of Secrets Parsed Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.18
Date: 29 July 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package command_process;

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
	
	public void updateState(int verbNumber) {
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
			setMultipleCommand(verbNumber);
		}
	}
	
	private void setSingleCommand(int verbNumber) {
		
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
	
	private void setMultipleCommand(int verbNumber) {
		
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
	
	public boolean checkRest() {
		return commandType == CommandType.WAIT;
	}
	
	public boolean checkInfo() {
		return commandType == CommandType.INFO;
	}
	
	public boolean checkWave() {
		return commandType == CommandType.WAVE;
	}
	
	public boolean checkHelp() {
		return commandType == CommandType.HELP;
	}
	
	public boolean checkPolish() {
		return commandType == CommandType.RUB;
	}
	
	public boolean checkSay() {
		return commandType == CommandType.SAY;
	}
	
	public boolean checkExamine() {
		return commandType == CommandType.EXAMINE;
	}
	
	public boolean checkFill() {
		return commandType == CommandType.FILL;
	}
	
	public boolean checkRide() {
		return commandType == CommandType.RIDE;
	}
	
	public boolean checkOpen() {
		return commandType == CommandType.OPEN;
	}
	
	public boolean checkSwim() {
		return commandType == CommandType.SWIM;
	}
	
	public boolean checkShelter() {
		return commandType == CommandType.SHELTER;
	}
	
	public boolean checkChop() {
		return commandType == CommandType.CHOP;
	}
	
	public boolean checkKill() {
		return commandType == CommandType.KILL;
	}
	
	public boolean checkAttack() {
		return commandType == CommandType.ATTACK;
	}
	
	public boolean checkLoad() {
		return commandType == CommandType.LOAD;
	}
	
	public boolean checkSave() {
		return commandType == CommandType.SAVE;
	}
	
	public boolean checkQuit() {
		return commandType == CommandType.QUIT;
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
 * 29 May 2025 - Added check for rest/wait
 * 31 May 2025 - Added Info & Wave Function
 * 1 June 2025 - Added check for help & polish commands
 * 2 June 2025 - Added check for Speak & Examine
 * 8 June 2025 - Added check for fill
 * 9 June 2025 - Added check for ride & open
 * 10 June 2025 - Added check for swim & shelter
 * 11 June 2025 - Added check for combat actions
 * 16 June 2025 - Added checks for load,save & quit
 * 24 June 2025 - Added script to make single special movement commands to go special command
 * 25 June 2025 - Added function call to set multiple command
 * 20 July 2025 - Added function to change command state
 */