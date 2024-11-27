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
import Model.GameEngine;

public class GivePanel extends JPanel {

	Color background;

	public GivePanel() {
		
		JPanel mainPanel = new JPanel(new GridLayout(2,1));
				
		//Adds label
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel label = new JLabel("Give to whom?");
        panel.add(label);
        mainPanel.add(panel);
		
        //Adds text area.
		JTextField commandField = new JTextField(2);
		//commandField.addKeyListener(new CommandListener(commandField,game,this));
		mainPanel.add(commandField);
		
		//Once done, then runs rest of script
		add(mainPanel);
		repaint();
		
		//1400 PRINT"GIVE THE ";X$;" TO WHOM";:INPUT X$
		//We need a special option to get the object of give
	}
	
}

/* 25 November 2024 - Created File
 * 27 November 2024 - Built Panel
 */