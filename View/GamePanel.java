/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 26 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Interfaces.GameCommandHandler;
import Interfaces.GameStateProvider;
import Model.GameEngine;

public class GamePanel extends JPanel {
	
	private final GameStateProvider state;
	private final GameCommandHandler commander;
		
	private static final long serialVersionUID = 1L;
	Color background;
	JTextField commandField;


	public GamePanel(GameEngine game) {
		this.state = game;
		this.commander = game;
		initialiseUI();
	}
	
	public void initialiseUI() {
				
		//Gets the background colour for the frame
		background = this.getBackground();
		this.setLayout (new BorderLayout()); 
		
		// Top section for status and label panels
		StatusPanel statusPanel = new StatusPanel(state);
		RoomPanel roomPanel = new RoomPanel(state);
		CommandPanel commandPanel = new CommandPanel(state);
				
		this.add(statusPanel, BorderLayout.NORTH); 
		this.add(roomPanel,BorderLayout.CENTER);
		this.add(commandPanel,BorderLayout.SOUTH);
	}
	
	public void setCommandField() {
		//this.commandField.requestFocusInWindow();
	}
	
	//Creates a button and adds it to the panel.
	private void addButton(JPanel panel,String buttonName,ActionListener action,int size) {
		
		//Create Exit Button
		JButton button = new JButton(buttonName);
		panel.add(button);
		
		//Closes frame when clicked
		panel.setBorder(BorderFactory.createEmptyBorder(0,size,0,size));
	    button.addActionListener(action);
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
 * 15 January 2025 - Removed extraneous methods and moved the lineLength to a constant.
 * 31 January 2025 - Completed Testing and increased version
 * 				   - Fixed the styling for the input section.
 * 1 February 2025 - Added the map display button
 * 17 February 2025 - Added restart button when game ends
 * 23 February 2025 - Hid map button if swimming in poisoned waters.
 * 24 February 2025 - Added buttons for Shelter
 * 25 February 2025 - Changed previous commands to buttons
 * 26 February 2025 - Added the load game buttons
 * 2 March 2025 - Added button to open browser to book
 * 3 March 2025 - Added code to focus on the command line.
 * 5 March 2025 - Increased to v4.0
 * 21 March 2025 - Updated for messageBuilder
 * 24 March 2025 - Removed GameFrame
 * 26 March 2025 - Moved code to separate classes
 */