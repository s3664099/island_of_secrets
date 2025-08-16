/*
Title: Island of Secrets Map Controller
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 21 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import view.GamePanel;

public class MapController {
	
	public MapController(GameController gameController, GamePanel gamePanel) {}
	
	public MouseAdapter createMouseAdapter(int roomId) {
		return new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				
				JPanel panel = (JPanel) e.getSource();
				panel.setBackground(new Color(240,240,255));
				panel.repaint();
			}
			
			public void mouseExited(MouseEvent e) {
				
				JPanel panel = (JPanel) e.getSource();
				panel.setBackground(null);
				panel.repaint();
			}
		};
	}
}

/* 21 April 2025 - Created File
 * 
 */
