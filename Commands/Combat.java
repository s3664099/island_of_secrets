/*
Title: Island of Secrets Combat Commands
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 11 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.util.Random;

import Game.Game;
import Game.Player;

public class Combat {

	private final Game game;
	private final Player player;
	private final ParsedCommand command;
	private final String codedCommand;
	private final int verbNumber;
	private boolean hasItems = false;
	private final Random rand = new Random();
	
	public Combat(Game game,Player player) {
		this.game = game;
		this.player = player;
		this.command = null;
		this.codedCommand = null;
		this.verbNumber = -99;
	}
	
	public Combat(Game game,Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.command = command;
		this.codedCommand = command.getCodedCommand();
		this.verbNumber = command.getVerbNumber();
	}
}

/* 11 June 2025 - Create File
 * 
 */
