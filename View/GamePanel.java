/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 2.1
Date: 29 December 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Controller.CommandListener;
import Controller.QuitButton;
import Model.GameEngine;

public class GamePanel extends JPanel {
	
	Color background;
	GameEngine game;
	GameFrame frame;

	public GamePanel(GameEngine game,GameFrame frame) {
		
		add(game);
		this.frame = frame;
	}
	
	public void add(GameEngine game) {
		
		//Gets the background colour for the frame
		background = this.getBackground();

		this.setLayout (new BorderLayout()); 
        
		//Need to do what they did in the game, namely go through each of the lines and
		//work out the size, and then add to the grid.
		
		// Top section for status and label panels
		JPanel topPanel = new JPanel(new GridLayout(1, 1)); // Adjust as necessary
		topPanel.add(CreateStatusPanel(game));
		topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		JPanel middlePanel = new JPanel(new GridLayout(18,1));
		middlePanel.add(CreateLabelPanel(game.getRoom(), 1));
		middlePanel.add(CreateLabelPanel("", 1));
				
		//Add the items to the room panel
		String itemString = game.getItems();
		
		while (itemString.length()>0) {
			
			int lineLength = getLineLength(itemString);
			String itemLine = itemString.substring(0,lineLength).trim();
			itemString = itemString.substring(lineLength);
			middlePanel.add(CreateLabelPanel(itemLine, 1));
		}
		
		//Adds space if there are items.
		if (game.getItems().length()>0) {
			middlePanel.add(CreateLabelPanel("", 1));
		}
		
		//Add exits
		middlePanel.add(CreateLabelPanel(game.getExits(),1));
		middlePanel.add(CreateLabelPanel(game.getSpecialExits(),1));
		middlePanel.add(CreateLabelPanel("",2));
				
		//Display message
		middlePanel.add(CreateLabelPanel("", 1));
		String message = game.getMessage();
		String[] messages = message.split("\\|");
				
		for (String msg:messages) {
			middlePanel.add(CreateLabelPanel(msg, 1));
		}

		if (game.checkEndGame()) {
			
			String gameScore = String.format("Your Final Score = %s", game.getFinalScore());
			middlePanel.add(CreateLabelPanel(gameScore, 1));
			middlePanel.add(CreateLabelPanel("Game Over!", 1));
		}
		
		middlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		//Creates the command field
		String[] commands = game.getCommands();
		JPanel bottomPanel = new JPanel(new GridLayout(7,1));
		
		//Command Field includes four labels above which contain the last three commands.
		//Also one for a blank spot
		bottomPanel.add(CreateLabelPanel(commands[0],1));
		bottomPanel.add(CreateLabelPanel(commands[1],1));
		bottomPanel.add(CreateLabelPanel(commands[2],1));
		
		if (!game.checkEndGame()) {
			JTextField commandField = new JTextField(2);
			commandField.addKeyListener(new CommandListener(commandField,game,this));
			commandField.requestFocusInWindow();
			bottomPanel.add(commandField);
		} else {
			
			//Create Exit Button
			JButton button = new JButton("Exit");
			bottomPanel.add(button);
			
			//Closes frame when clicked
		    button.addActionListener(new QuitButton(this.frame));
		}
				
		this.add(topPanel, BorderLayout.NORTH); // Add to the top of the main layout
		this.add(middlePanel,BorderLayout.CENTER);//String position = BorderLayout.CENTER;
		this.add(bottomPanel,BorderLayout.SOUTH);
	}
	
	public void addPanel(JPanel panel) {
		this.add(panel);
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
		
		int lineLength = 90;
		
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
 * 8 November 2024 - Change from Frame to Panel and created an add method
 * 8 December 2024 - Updated code so that messages will split with pipe and add
 * 					 to new line.
 * 15 December 2024 - Added final score display
 * 23 December 2024 - Added quit function
 * 					- Updated to version 2.
 * 29 December 2024 - Added entry for special exits
 */