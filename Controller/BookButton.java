/*
Title: Island of Secrets Book Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 5 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import Model.GameEngine;
import View.GamePanel;

public class BookButton implements ActionListener {

	private GameEngine game;
	private GamePanel panel;
	
	public BookButton(GameEngine game, GamePanel panel) {
		
		this.panel = panel;
		this.game = game;		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				URI uri = new URI("https://archive.org/details/island-of-secrets_202303");
				desktop.browse(uri);
			} catch (IOException excp) {
				excp.printStackTrace();
			} catch (URISyntaxException excp) {
				excp.printStackTrace();
			}
		}
		
		game.getGame().setGameDisplay(false);
		game.setGamePanel(this.panel);
	}

}

/* 2 March 2025 - Created Class
 * 5 March 2025 - Increased to v4.0
 */
