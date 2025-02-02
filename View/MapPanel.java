/*
Title: Island of Secrets Map Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.0
Date: 2 February 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.GridLayout;

import javax.swing.JPanel;

import Model.GameEngine;

public class MapPanel extends JPanel {
	
	private static final long serialVersionUID = -1097043236506747632L;
	private GameEngine engine;
	private GamePanel game;
	
	public MapPanel(GamePanel game,GameEngine engine) {
		this.game = game;
		this.engine = engine;
		
		setLayout(new GridLayout(11,10));
	}

}

/*
 * 2 February 2025 - Created File
 */