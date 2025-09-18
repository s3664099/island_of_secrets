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

public class MapController {
	
	private static final Color HOVER_COLOUR = new Color(240, 240, 255);
	
	public MapController() {}
	
	public MouseAdapter createMouseAdapter(int roomId) {
		return new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				
				JPanel panel = (JPanel) e.getSource();
				panel.setBackground(HOVER_COLOUR);
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
 * 18 September 2025 - Update file by adding HOVER_COLOUR and removing unused variables
 * 
 */
