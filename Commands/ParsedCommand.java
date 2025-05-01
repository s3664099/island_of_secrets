/*
Title: Island of Secrets Parsed Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 1 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

public class ParsedCommand {
	
	private final String[] splitCommand;
	private final String codedCommand;
	private final int verbNumber;
	private final int nounNumber;
	private boolean verified;
	
	public ParsedCommand(int verbNumber, int nounNumber, String codedCommand, String[] splitCommand) {
	
		this.splitCommand = splitCommand;
		this.codedCommand = codedCommand;
		this.verbNumber = verbNumber;
		this.nounNumber = nounNumber;
	
	}
}

/* 24 April 2025 - Created File
 * 1 May 2025 - Added varables and built constructor
 */
*/