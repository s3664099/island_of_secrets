/*
Title: Island of Secrets Map Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.3
Date: 5 February 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
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
		
		for (int x=1;x<110;x++) {
			
			JPanel roomPanel = new JPanel();
			
			if (x<10 && x>1) {
				roomPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
			} else if (x==1) {
				roomPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 0, 0, Color.BLACK));
			} else if (x==10) {
				roomPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 2, Color.BLACK));
			} else if (x==71) {
				roomPanel.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 0, Color.BLACK));
			} else if (x==80) {
				roomPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.BLACK));
			} else if (x>71 && x<80) {
				roomPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
			} else if (x==11 || x==21 || x==31 || x==41 || x==51 || x==61) {
				roomPanel.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.BLACK));
			} else if (x==20 || x==30 || x==40 || x==50 || x==60 || x==70) {
				roomPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK));
			}
			
			if (x<80 && engine.getGame().getRoom(x).getVisited()) {
				roomPanel.add(new JLabel(""+engine.getGame().getRoom(x).getRoomType()));
				//Gets exits and sets the walls based on where the rooms
			}
			
			add(roomPanel);
		}
	}
}

/*
 * 2 February 2025 - Created File
 * 4 February 2025 - Added Borders to the map panel.
 * 5 February 2025 - Added code to only set label when room entered.
 * 9 February 2025 - Retrieve and add room type to the panel
 */