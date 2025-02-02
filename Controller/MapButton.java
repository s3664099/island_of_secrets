/*
Title: Island of Secrets Map Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.0
Date: 2 February 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.GameEngine;
import View.GamePanel;

public class MapButton implements ActionListener {

	private GameEngine game;
	private GamePanel panel;
	
	public MapButton(GameEngine game, GamePanel panel) {
		
		this.panel = panel;
		this.game = game;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		game.setMapPanel(this.panel);
	}

}

/* 2 February 2025 - Created Class
 * 
 */
