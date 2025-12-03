/*
Title: Island of Secrets Parsed Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package command_process;

import data.GameEntities;

/**
 * Represents a parsed player command in the game.
 * <p>
 * This class encapsulates the raw command string entered by the player,
 * its parsed verb/noun components, and the derived command state and type.
 * Commands are categorized as movement, single-command, or multi-command,
 * and mapped to specific {@link CommandType} values for handling.
 * </p>
 */
public class ParsedCommand {
	
	private final String command;
	private final String[] splitTwoCommand;
	private final String[] splitFullCommand;
	private final String codedCommand;
	private final int verbNumber;
	private final int nounNumber;
	
    /**
     * Represents the general structure/state of a parsed command.
     */
	private enum CommandState {
		
        /** Command does not map to a known state. */
		NONE,
		
		/** Movement command (e.g. north, south, etc.). */
		MOVE,
		
		/** Standalone command with no noun (e.g. WAIT, SAVE, QUIT). */
		SINGLE_COMMAND, 
		
		/** Command requiring an additional noun (e.g. TAKE SWORD). */
		MULTIPLE_COMMAND 
	};
	
    /**
     * Represents the specific type of command identified by the parser.
     */
	private enum CommandType { NONE,TAKE,GIVE,DROP,EAT,DRINK,RIDE,OPEN,CHOP,ATTACK,KILL,SWIM,SHELTER,
								HELP,SCRATCH,CATCH,RUB,READ,EXAMINE,FILL,SAY,WAIT,WAVE,INFO,
								LOAD,SAVE,QUIT,RESTART};

	private CommandState commandState = CommandState.NONE;
	private CommandType commandType = CommandType.NONE;
	
    /**
     * Creates a new parsed command instance.
     *
     * @param verbNumber   numeric identifier for the verb portion of the command
     * @param nounNumber   numeric identifier for the noun portion of the command
     * @param codedCommand encoded representation of the command
     * @param splitCommand split command array (up to two words: verb and noun)
     * @param command      the full raw command string as entered by the player
     */
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
	
    /**
     * Updates the command state based on a new verb number.
     *
     * @param verbNumber numeric identifier for the new verb
     */
	public void updateState(int verbNumber) {
		setState(verbNumber);
	}
	
    /**
     * Determines the {@link CommandState} of the current command based on the given verb number.  
     * <p>
     * - If the verb is within the MOVE range, the command is classified as {@link CommandState#MOVE}.  
     * - If the verb matches one of the recognized single-action commands (e.g., EAT, DRINK, WAIT),  
     *   the command is classified as {@link CommandState#SINGLE_COMMAND} and forwarded to 
     *   {@link #setSingleCommand(int)} for further classification.  
     * - Otherwise, the command is treated as a {@link CommandState#MULTIPLE_COMMAND} and delegated 
     *   to {@link #setMultipleCommand(int)}.  
     *
     * @param verbNumber the numeric identifier of the parsed verb, as defined in {@link GameEntities}
     */
	private void setState(int verbNumber) {
		
		if (verbNumber>GameEntities.MOVE_BOTTOM && verbNumber<GameEntities.MOVE_TOP) {
			commandState = CommandState.MOVE;
		} else if (verbNumber == GameEntities.CMD_EAT || verbNumber == GameEntities.CMD_DRINK || 
					verbNumber == GameEntities.CMD_SWIM || verbNumber == GameEntities.CMD_SHELTER ||
					verbNumber == GameEntities.CMD_WAIT || verbNumber == GameEntities.CMD_WAVE || 
					verbNumber == GameEntities.CMD_INFO || verbNumber == GameEntities.CMD_LOAD || 
					verbNumber == GameEntities.CMD_SAVE || verbNumber == GameEntities.CMD_QUIT ||
					verbNumber == GameEntities.CMD_REST || verbNumber == GameEntities.CMD_NORTH ||
					verbNumber == GameEntities.CMD_SOUTH || verbNumber == GameEntities.CMD_EAST ||
					verbNumber == GameEntities.CMD_WEST || verbNumber == GameEntities.CMD_RESTART) {
			commandState = CommandState.SINGLE_COMMAND;
			setSingleCommand(verbNumber);
		} else {
			commandState = CommandState.MULTIPLE_COMMAND;
			setMultipleCommand(verbNumber);
		}
	}
	
    /**
     * Assigns the {@link CommandType} for commands that operate independently (no target needed).  
     * Examples include simple actions like EAT, DRINK, WAIT, or SAVE.  
     * <p>
     * This method is called when {@link #setState(int)} identifies the command as a 
     * {@link CommandState#SINGLE_COMMAND}.
     *
     * @param verbNumber the numeric identifier of the verb to classify as a single command
     */
	private void setSingleCommand(int verbNumber) {
		
		if (verbNumber == GameEntities.CMD_EAT) {
			commandType = CommandType.EAT;
		} else if (verbNumber == GameEntities.CMD_DRINK) {
			commandType = CommandType.DRINK;
		} else if (verbNumber == GameEntities.CMD_INFO) {
			commandType = CommandType.INFO;
		} else if (verbNumber == GameEntities.CMD_WAVE) {
			commandType = CommandType.WAVE;
		} else if (verbNumber == GameEntities.CMD_LOAD) {
			commandType = CommandType.LOAD;
		} else if (verbNumber == GameEntities.CMD_SAVE) {
			commandType = CommandType.SAVE;
		} else if (verbNumber == GameEntities.CMD_REST || verbNumber == GameEntities.CMD_WAIT) {
			commandType = CommandType.WAIT;
		} else if (verbNumber == GameEntities.CMD_QUIT) {
			commandType = CommandType.QUIT;
		} else if (verbNumber == GameEntities.CMD_SWIM) {
			commandType = CommandType.SWIM;
		} else if (verbNumber == GameEntities.CMD_SHELTER) {
			commandType = CommandType.SHELTER;
		} else if (verbNumber == GameEntities.CMD_RESTART) {
			commandType = CommandType.RESTART;
		}
	}
	
    /**
     * Assigns the {@link CommandType} for commands that usually involve a target object or entity.  
     * Examples include TAKE, GIVE, DROP, ATTACK, or EXAMINE.  
     * <p>
     * This method is called when {@link #setState(int)} identifies the command as a 
     * {@link CommandState#MULTIPLE_COMMAND}.
     *
     * @param verbNumber the numeric identifier of the verb to classify as a multiple command
     */
	private void setMultipleCommand(int verbNumber) {
		
		if (verbNumber == GameEntities.CMD_TAKE || verbNumber == GameEntities.CMD_GET || 
			verbNumber == GameEntities.CMD_PICK || verbNumber == GameEntities.CMD_CATCH) {
			commandType = CommandType.TAKE;
		} else if (verbNumber == GameEntities.CMD_GIVE) {
			commandType = CommandType.GIVE;
		} else if (verbNumber == GameEntities.CMD_DROP||verbNumber == GameEntities.CMD_LEAVE) {
			commandType = CommandType.DROP;
		} else if (verbNumber == GameEntities.CMD_RIDE) {
			commandType = CommandType.RIDE;
		} else if (verbNumber == GameEntities.CMD_OPEN) {
			commandType = CommandType.OPEN;
		} else if (verbNumber>GameEntities.CHOP_BOTTOM && verbNumber <GameEntities.CHOP_TOP) {
			commandType = CommandType.CHOP;
		} else if (verbNumber>GameEntities.ATTACK_BOTTOM && verbNumber <GameEntities.ATTACK_TOP) {
			commandType = CommandType.ATTACK;
		} else if (verbNumber == GameEntities.CMD_KILL) {
			commandType = CommandType.KILL;
		} else if (verbNumber == GameEntities.CMD_HELP || verbNumber == GameEntities.CMD_SCRATCH) {
			commandType = CommandType.HELP;
		} else if (verbNumber == GameEntities.CMD_RUB || verbNumber == GameEntities.CMD_POLISH) {
			commandType = CommandType.RUB;
		} else if (verbNumber == GameEntities.CMD_READ || verbNumber == GameEntities.CMD_EXAMINE) {
			commandType = CommandType.EXAMINE;
		} else if (verbNumber == GameEntities.CMD_FILL) {
			commandType = CommandType.FILL;
		} else if (verbNumber == GameEntities.CMD_SAY) {
			commandType = CommandType.SAY;
		}
	}
	
    // --------------------
    // Getters
    // --------------------

    /**
     * @return the numeric identifier of the verb portion
     */
	public int getVerbNumber() {
		return verbNumber;
	}
	
    /**
     * @return the numeric identifier of the noun portion
     */
	public int getNounNumber() {
		return nounNumber;
	}
	
    /**
     * @return the raw command string entered by the player
     */
	public String getCommand() {
		return command;
	}
	
    /**
     * @return the encoded form of the command
     */
	public String getCodedCommand() {
		return codedCommand;
	}
	
    /**
     * @return an array of up to two elements: verb and noun
     */
	public String[] getSplitTwoCommand() {
		return splitTwoCommand;
	}
	
    /**
     * @return the full command split into tokens
     */
	public String[] getSplitFullCommand() {
		return splitFullCommand;
	}
	
    // --------------------
    // State checks
    // --------------------

    /**
     * Checks whether the noun part of the command is present and non-empty.
     *
     * @return true if a valid noun is supplied, false otherwise
     */
	public boolean checkNounLength() {
		return splitTwoCommand.length > 1 && !splitTwoCommand[1].isEmpty();
	}
	
    /**
     * @return true if this command is a movement command
     */
	public boolean checkMoveState() {
		return commandState == CommandState.MOVE;
	}
	
    /**
     * @return true if this command is a single-command (no noun required)
     */
	public boolean checkSingleCommandState() {
		return commandState == CommandState.SINGLE_COMMAND;
	}
	
    /**
     * @return true if this command is a multi-command (noun required)
     */
	public boolean checkMultipleCommandState() {
		return commandState == CommandState.MULTIPLE_COMMAND;
	}
	
    /**
     * @return true if the command type is {@link CommandType#NONE}
     */
	public boolean checkNoneCommandType() {
		return commandState == CommandState.NONE;
	}
	
    // --------------------
    // Command type checks
    // --------------------

    /** @return true if the command is a TAKE command */
	public boolean checkTake() {
		return commandType == CommandType.TAKE;
	}
	
    /** @return true if the command is a DROP command */
	public boolean checkDrop() {
		return commandType == CommandType.DROP;
	}
	
    /** @return true if the command is a GIVE command */
	public boolean checkGive() {
		return commandType == CommandType.GIVE;
	}
	
    /** @return true if the command is an EAT command */	
	public boolean checkEat() {
		return commandType == CommandType.EAT;
	}
	
    /** @return true if the command is a DRINK command */
	public boolean checkDrink() {
		return commandType == CommandType.DRINK;
	}
	
	/** @return true if the command is a WAIT/REST command */
	public boolean checkRest() {
		return commandType == CommandType.WAIT;
	}
	
	/** @return true if the command is an INFO command */
	public boolean checkInfo() {
		return commandType == CommandType.INFO;
	}
	
	/** @return true if the command is a WAVE command */
	public boolean checkWave() {
		return commandType == CommandType.WAVE;
	}
	
	/** @return true if the command is a HELP command */
	public boolean checkHelp() {
		return commandType == CommandType.HELP;
	}
	
	/** @return true if the command is a RUB/POLISH command */
	public boolean checkPolish() {
		return commandType == CommandType.RUB;
	}
	
    /** @return true if the command is a SAY command */
	public boolean checkSay() {
		return commandType == CommandType.SAY;
	}
	
    /** @return true if the command is a READ/EXAMINE command */
	public boolean checkExamine() {
		return commandType == CommandType.EXAMINE;
	}
	
    /** @return true if the command is a FILL command */
	public boolean checkFill() {
		return commandType == CommandType.FILL;
	}
	
    /** @return true if the command is a RIDE command */
	public boolean checkRide() {
		return commandType == CommandType.RIDE;
	}
	
    /** @return true if the command is an OPEN command */
	public boolean checkOpen() {
		return commandType == CommandType.OPEN;
	}
	
    /** @return true if the command is a SWIM command */
	public boolean checkSwim() {
		return commandType == CommandType.SWIM;
	}
	
    /** @return true if the command is a SHELTER command */
	public boolean checkShelter() {
		return commandType == CommandType.SHELTER;
	}
	
    /** @return true if the command is a CHOP command */
	public boolean checkChop() {
		return commandType == CommandType.CHOP;
	}
	
    /** @return true if the command is a KILL command */
	public boolean checkKill() {
		return commandType == CommandType.KILL;
	}
	
    /** @return true if the command is an ATTACK command */
	public boolean checkAttack() {
		return commandType == CommandType.ATTACK;
	}
	
    /** @return true if the command is a LOAD command */
	public boolean checkLoad() {
		return commandType == CommandType.LOAD;
	}
	
    /** @return true if the command is a SAVE command */
	public boolean checkSave() {
		return commandType == CommandType.SAVE;
	}
	
    /** @return true if the command is a QUIT command */
	public boolean checkQuit() {
		return commandType == CommandType.QUIT;
	}
	
    /** @return true if the command is a RESTART command */
	public boolean checkRestart() {
		return commandType == CommandType.RESTART;
	}
	
	/** @return true if the noun is 'table' */
	public boolean checkNounTable() {
		return splitTwoCommand[1] != null && splitTwoCommand[1].equals(GameEntities.NOUN_TABLE);
	}
	
	/** @return true if the noun is 'room' */
	public boolean checkNounRoom() {
		return splitTwoCommand[1] != null && splitTwoCommand[1].equals(GameEntities.NOUN_ROOM);
	}
	
	/** @return true if the noun is 'map' */
	public boolean checkNounMap() {
		return splitTwoCommand[1] != null && splitTwoCommand[1].equals(GameEntities.NOUN_MAP);
	}
	
	/** @return true if the noun is 'diary' */
	public boolean checkNounDiary() {
		return splitTwoCommand[1] != null && splitTwoCommand[1].equals(GameEntities.NOUN_DIARY);
	}
	
	/** @return true if the noun is 'papers' */
	public boolean checkNounPapers() {
		return splitTwoCommand[1] != null && splitTwoCommand[1].equals(GameEntities.NOUN_PAPERS);
	}
	
	/** @return true if the noun is 'wine' */
	public boolean checkNounWine() {
		return splitTwoCommand[1] != null && splitTwoCommand[1].equals(GameEntities.NOUN_WINE);
	}
	
	/** @return true if the noun is 'food' */
	public boolean checkNounFood() {
		return splitFullCommand[1] != null && splitFullCommand[1].equals(GameEntities.NOUN_FOOD);
	}
	
	/** @return true if the noun is 'drink' */
	public boolean checkNounDrink() {
		return splitFullCommand[1] != null && splitFullCommand[1].equals(GameEntities.NOUN_DRINK);
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
 * 24 August 2025 - Updated remaining commands.
 * 27 August 2025 - Fixed checkNonCommand
 * 13 October 2025 - Added check to confirm that table is a valid command
 * 2 November 2025 - Added check to confirm that room is a valid command
 * 5 November 2025 - Added check to confirm drinking wine
 * 6 November 2025 - Added restart command
 * 8 November 2025 - Added food & drink nouns
 * 3 December 2025 - Increased version number
 */