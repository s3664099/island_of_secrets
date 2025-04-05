/*
Title: Island of Secrets Quit Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 21 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.GameEngine;
import Model.GameInitialiser;
import Model.Player;
import View.GameFrame;
import View.MainGamePanel;

public class QuitButton implements ActionListener {
	
	private GameFrame frame;
	private Boolean restart;
	private GameEngine game;
	private MainGamePanel panel;
	
	public QuitButton(GameFrame frame,boolean restart,GameEngine game,MainGamePanel panel) {
		this.frame = frame;
		this.restart = restart;
		this.game = game;
		this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (!restart) {
			this.frame.dispose();
		} else {
			this.game.restart(GameInitialiser.initialiseGame(), new Player());
			//this.game.setGamePanel(panel);
		}
	}

}

/* 23 December 2024 - Create File
 * 31 January 2025 - Completed Testing and increased version
 * 17 February 2025 - Added option to restart the game
 * 5 March 2025 - Increased to v4.0
 * 15 March 2025 - Added the Game Initialiser
 * 21 March 2025 - Removed extraneous include
 */
