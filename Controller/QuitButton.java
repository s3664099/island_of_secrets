/*
Title: Island of Secrets Quit Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.1
Date: 17 February 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.Game;
import Model.GameEngine;
import Model.Player;
import View.GameFrame;
import View.GamePanel;

public class QuitButton implements ActionListener {
	
	private GameFrame frame;
	private Boolean restart;
	private GameEngine game;
	private GamePanel panel;
	
	public QuitButton(GameFrame frame,boolean restart,GameEngine game,GamePanel panel) {
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
			this.game.restart(new Game(), new Player());
			this.game.setGamePanel(panel);
		}
	}

}

/* 23 December 2024 - Create File
 * 31 January 2025 - Completed Testing and increased version
 * 17 February 2025 - Added option to restart the game
 */
