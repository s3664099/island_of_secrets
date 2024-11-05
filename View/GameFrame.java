/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.3
Date: 5 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/
package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Model.GameEngine;

public class GameFrame extends JFrame {
	
	Color background;

	public GameFrame(GameEngine game) {
		
		super("Island of Secrets");

		//kills the window when the 'x' is clicked at the top
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		//Gets the background colour for the frame
		background = this.getBackground();

		this.setLayout (new BorderLayout()); 
        
		// Top section for status and label panels
		JPanel topPanel = new JPanel(new GridLayout(2, 1)); // Adjust as necessary
		topPanel.add(CreateStatusPanel(game));
		topPanel.add(CreateLabelPanel(game.getRoom(), 1));
		//topPanel.add(CreateLabelPanel("", 0)); // This is for adding the description
											   // Need to consider this when doing it

		//Items Section - May need to update the styling when finished
		this.add(topPanel, BorderLayout.NORTH); // Add to the top of the main layout
		this.add(CreateTextPanel(game.getItems()), BorderLayout.CENTER); // Center the text panel
	    		
		//sets the boundaries of the frame.
		setBounds(100,100, 800,600);
		setVisible(true);
	}
	
	private JPanel CreateStatusPanel(GameEngine game) {
		
        JPanel statusPanel = new JPanel(new GridLayout(2,1));
        statusPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        
        JPanel statPanel = CreatePanel(0);
        JLabel statLabel = new JLabel(game.getStatus());
        statPanel.add(statLabel);
        
        statusPanel.add(CreateLabelPanel(game.getTime(),0));
        statusPanel.add(CreateLabelPanel(game.getStatus(),0)); 
        
        return statusPanel;
	}
	
	private JPanel CreatePanel(int flowType) {

		FlowLayout flow = new FlowLayout(FlowLayout.CENTER);
		
		if (flowType==1) {
			flow = new FlowLayout(FlowLayout.LEFT);
		}
		
        return new JPanel(flow);
		
	}
	
	private JPanel CreateLabelPanel(String labelString,int flowType) {
		
		JPanel panel = CreatePanel(flowType);
        JLabel label = new JLabel(labelString);
        panel.add(label);
        return panel;
	}
	
	private JPanel CreateTextPanel(String text) {
		
		JPanel panel = CreatePanel(1);
		JTextArea textArea = new JTextArea(text,4,70);
		textArea.setEditable(false);  // This makes the JTextArea read-only
		textArea.setLineWrap(true);   // Optional: Wrap lines if text is too long
		textArea.setWrapStyleWord(true);  // Optional: Wrap at word boundaries
		textArea.setBackground(background);
		panel.add(textArea);
		
		return panel;
	}
	
}

/* 2 November 2024 - Created File
 * 3 November 2024 - Added Status Box at the top
 * 4 November 2024 - Moved Label Panel creations to separate box.
 * 				   - Added panel to display location items
 * 5 November 2024 - Updated the display for the items
 */