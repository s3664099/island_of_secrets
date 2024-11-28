/*
Title: Island of Secrets Give Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.1
Date: 27 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controller.CommandListener;
import Controller.GiveListener;
import Model.GameEngine;

public class GivePanel extends GamePanel {

	public GivePanel(GameEngine game) {
		
		super(game);
		
		JPanel mainPanel = new JPanel(new GridLayout(2,1));
				
		//Adds label
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel label = new JLabel("Give to whom?");
        panel.add(label);
        mainPanel.add(panel);
		
        //Adds text area.
		JTextField commandField = new JTextField(2);
		commandField.grabFocus();
		commandField.requestFocusInWindow();
		commandField.addKeyListener(new GiveListener(commandField,game,this));
		mainPanel.add(commandField);
		
		//Once done, then runs rest of script
		add(mainPanel);
		repaint();
	}
	
}

/* 25 November 2024 - Created File
 * 27 November 2024 - Built Panel
 */
