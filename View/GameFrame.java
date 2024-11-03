/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.0
Date: 3 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/
package View;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Model.Game;
import Model.Player;


public class GameFrame extends JFrame {

	public GameFrame(Game game, Player player) {
		
		super("Island of Secrets");

		//kills the window when the 'x' is clicked at the top
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setLayout (new GridLayout (12,1)); 

        JPanel statusPanel = new JPanel(new GridLayout(2,1));
        statusPanel.setBorder(new LineBorder(Color.black));

        // Panel to hold the time label centered
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel timeLabel = new JLabel(player.getTime());
        timePanel.add(timeLabel);
        
        // Panel to hold the status label centered
        JPanel statPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel statLabel = new JLabel(player.getStatus());
        statPanel.add(statLabel);
        
        // Add the inner panels to the main statusPanel
        statusPanel.add(timePanel);
        statusPanel.add(statPanel);        
        
	    this.add(statusPanel);
	    		
		//sets the boundaries of the frame.
		setBounds(100,100, 800,600);
		setVisible(true);
	}
	
}

/* 2 November 2024 - Created File
 * 3 November 2024 - Added Status Box at the top
 */