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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
        
		//Need to do what they did in the game, namely go through each of the lines and
		//work out the size, and then add to the grid.
		
		
		// Top section for status and label panels
		JPanel topPanel = new JPanel(new GridLayout(1, 1)); // Adjust as necessary
		topPanel.add(CreateStatusPanel(game));
		topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		JPanel middlePanel = new JPanel(new GridLayout(20,1));
		middlePanel.add(CreateLabelPanel(game.getRoom(), 1));
		middlePanel.add(CreateLabelPanel("", 1));
		
		//Add the the description
		
		//Add the items to the room panel
		String itemString = game.getItems();
		
		while (itemString.length()>0) {
			
			int lineLength = getLineLength(itemString);
			String itemLine = itemString.substring(0,lineLength);
			itemString = itemString.substring(lineLength);
			middlePanel.add(CreateLabelPanel(itemLine, 1));
		}
		
		//Adds space if there are items.
		if (game.getItems().length()>0) {
			middlePanel.add(CreateLabelPanel("", 1));
		}
		
		//Add exits
		middlePanel.add(CreateLabelPanel(game.getExits(),1));
		middlePanel.add(CreateLabelPanel("",2));
		
		//Display message
		middlePanel.add(CreateLabelPanel("", 1));
		middlePanel.add(CreateLabelPanel(game.getMessage(), 1));
		middlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		JPanel bottomPanel = new JPanel(new GridLayout(3,1));
		bottomPanel.add(CreateLabelPanel("Bottom Panel",1));		
		
		this.add(topPanel, BorderLayout.NORTH); // Add to the top of the main layout
		this.add(middlePanel,BorderLayout.CENTER);//String position = BorderLayout.CENTER;
		this.add(bottomPanel,BorderLayout.SOUTH);
		
		//sets the boundaries of the frame.
		setBounds(100,100, 800,600);
		setVisible(true);
	}
	
	private JPanel CreateStatusPanel(GameEngine game) {
		
        JPanel statusPanel = new JPanel(new GridLayout(2,1));
        
        JPanel statPanel = CreatePanel(0);
        JLabel statLabel = new JLabel(game.getStatus());
        statPanel.add(statLabel);
        
        statusPanel.add(CreateLabelPanel(game.getTime(),0));
        statusPanel.add(CreateLabelPanel(game.getStatus(),0)); 
        
        return statusPanel;
	}
	
	private JPanel CreatePanel(int flowType) {

		FlowLayout flow = new FlowLayout(FlowLayout.CENTER);
		
		if (flowType==1 || flowType==2) {
			flow = new FlowLayout(FlowLayout.LEFT);
		}
		
		JPanel panel = new JPanel(flow);
		
		if (flowType == 2) {
			panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		}
		
        return panel;
		
	}
	
	private JPanel CreateLabelPanel(String labelString,int flowType) {
		
		JPanel panel = CreatePanel(flowType);
        JLabel label = new JLabel(labelString);
        panel.add(label);
        return panel;
	}
	
	private JPanel CreateTextPanel(String text) {
		
		JPanel panel = CreatePanel(1);
		JTextArea textArea = new JTextArea(text);
		textArea.setColumns(70);
		textArea.setRows(7);
		textArea.setEditable(false);  // This makes the JTextArea read-only
		textArea.setLineWrap(true);   // Optional: Wrap lines if text is too long
		textArea.setWrapStyleWord(true);  // Optional: Wrap at word boundaries
		textArea.setBackground(background);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(textArea);
		
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		textArea.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.red));
		return panel;
	}
	
	private int getLineLength(String line) {
		
		int lineLength = 100;
		
		if (lineLength>line.length()) {
			lineLength = line.length();
		}
		
		if (line.length()>99) {
			while(!Character.isWhitespace(line.charAt(lineLength))) {
				lineLength --;
			}
		}
		
		return lineLength;
	}
	
}

/* 2 November 2024 - Created File
 * 3 November 2024 - Added Status Box at the top
 * 4 November 2024 - Moved Label Panel creations to separate box.
 * 				   - Added panel to display location items
 * 5 November 2024 - Updated the display for the items
 * 				   - Completed the display for the place location
 */