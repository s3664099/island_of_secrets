/*
Title: Island of Secrets Map Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 21 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import UISupport.GameController;
import View.GamePanel;

public class MapButton implements ActionListener {

	private final GameController controller;
	private final GamePanel panel;
	
	public MapButton(GameController controller, GamePanel panel) {
			this.controller = Objects.requireNonNull(controller,"GameController cannot be null");
			this.panel = Objects.requireNonNull(panel,"GamePanel cannot be null");
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		showGameMap();
	}
	
	private void showGameMap() {
		panel.showMapView();
		panel.refreshMapView(controller);
	}
}

/* 2 February 2025 - Created Class
 * 5 March 2025 - Increased to v4.0
 * 4 April 2025 - Update controller for new style
 * 6 April 2025 - Updated code to handle new style for view
 * 21 April 2025 - Updated code based on DeepSeek recommendations
 */
