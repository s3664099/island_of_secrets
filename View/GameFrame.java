/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.2
Date: 4 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/
package View;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Model.GameEngine;

public class GameFrame extends JFrame {

	public GameFrame(GameEngine game) {
		
		super("Island of Secrets");

		//kills the window when the 'x' is clicked at the top
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setLayout (new GridLayout (12,1)); 
        
	    this.add(CreateStatusPanel(game));
	    		
		//sets the boundaries of the frame.
		setBounds(100,100, 800,600);
		setVisible(true);
	}
	
	private JPanel CreateStatusPanel(GameEngine game) {
		
        JPanel statusPanel = new JPanel(new GridLayout(2,1));
        statusPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        
        JPanel statPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel statLabel = new JLabel(game.getStatus());
        statPanel.add(statLabel);
        
        // Add the inner panels to the main statusPanel
        statusPanel.add(createLabelPanel(game.getTime()));
        statusPanel.add(createLabelPanel(game.getStatus())); 
        
        return statusPanel;
	}
	
	private JPanel createLabelPanel(String labelString) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(labelString);
        panel.add(label);
        return panel;
	}
	
}

/* 2 November 2024 - Created File
 * 3 November 2024 - Added Status Box at the top
 * 4 November 2024 - Moved Label Panel creations to separate box.
 */