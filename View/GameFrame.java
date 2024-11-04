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
	    this.add(CreateLabelPanel(game.getRoom(),1));
	    this.add(CreateLabelPanel("",0)); //Possibily do it as a text panel
	    this.add(CreateLabelPanel(game.getItems(),1));
	    		
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
        
        statusPanel.add(CreateLabelPanel(game.getTime(),0));
        statusPanel.add(CreateLabelPanel(game.getStatus(),0)); 
        
        return statusPanel;
	}
	
	private JPanel CreateLabelPanel(String labelString,int flowType) {
		
		FlowLayout flow = new FlowLayout(FlowLayout.CENTER);
		
		if (flowType==1) {
			flow = new FlowLayout(FlowLayout.LEFT);
		}
		
        JPanel panel = new JPanel(flow);
        JLabel label = new JLabel(labelString);
        panel.add(label);
        return panel;
	}
	
}

/* 2 November 2024 - Created File
 * 3 November 2024 - Added Status Box at the top
 * 4 November 2024 - Moved Label Panel creations to separate box.
 */