/*
Title: Island of Secrets Shelter Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 5 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.GameEngine;
import View.GamePanel;

public class ShelterButton implements ActionListener {

	private GameEngine game;
	private GamePanel panel;
	private int location;
	
	public ShelterButton(GameEngine game, GamePanel panel, int location) {
		
		this.panel = panel;
		this.game = game;
		this.location = location;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		game.getGame().setResponse(0);
		game.getPlayer().setRoom(location);
		game.setGamePanel(this.panel);
	}
}

/* 24 February 2025 - Created Class
 * 5 March 2025 - Increased to v4.0
 */
