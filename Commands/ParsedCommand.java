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
	
	public ParsedCommand(int verbNumber, int nounNumber, String codedCommand, 
						String[] splitCommand, String command) {
	
		this.splitCommand = splitCommand;
		this.codedCommand = codedCommand;
		this.verbNumber = verbNumber;
		this.nounNumber = nounNumber;
		this.command = command;
	
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
}

/* 24 April 2025 - Created File
 * 1 May 2025 - Added varables and built constructor
 * 2 May 2025 - Added getters for command validation
 * 3 May 2025 - Added getter for codedCommand
 */