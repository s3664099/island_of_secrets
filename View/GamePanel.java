/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 24 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controller.BookButton;
import Controller.CommandButton;
import Controller.CommandListener;
import Controller.GameButton;
import Controller.LoadGameButton;
import Controller.MapButton;
import Controller.QuitButton;
import Controller.SearchGameButton;
import Controller.ShelterButton;
import Data.Constants;
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
		JPanel topPanel = new JPanel(new GridLayout(1, 1)); // Adjust as necessary
		topPanel.add(CreateStatusPanel(game));
		topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		JPanel middlePanel = new JPanel(new GridLayout(18,1));
		middlePanel.add(CreateLabelPanel(state.getRoom(), 1));
		middlePanel.add(CreateLabelPanel("", 1));
				
		//Add the items to the room panel
		String itemString = state.getItems();
		
		while (itemString.length()>0) {
			
			int lineLength = getLineLength(itemString);
			String itemLine = itemString.substring(0,lineLength).trim();
			itemString = itemString.substring(lineLength);
			middlePanel.add(CreateLabelPanel(itemLine, 1));
		}
		
		//Adds space if there are items.
		if (state.getItems().length()>0) {
			middlePanel.add(CreateLabelPanel("", 1));
		}
		
		//Add exits
		middlePanel.add(CreateLabelPanel(state.getExits(),1));
		middlePanel.add(CreateLabelPanel(state.getSpecialExits(),1));
		middlePanel.add(CreateLabelPanel("",2));
				
		//Display message
		middlePanel.add(CreateLabelPanel("", 1));
		List<String> messages = state.getMessage();
				
		for (String msg:messages) {
			middlePanel.add(CreateLabelPanel(msg, 1));
		}
						
		if (state.isInitialGameState()) {
			JPanel inputPanel = new JPanel(new GridLayout(1,1));
			addButton(inputPanel,"Click for Clues & Hints",new BookButton(game,this),260);
			middlePanel.add(inputPanel);
		}
		
		if (state.getResponseType()==2) {
			
			String[] shelters = {"Grandpa's Shack","Cave of Snelm","Log Cabin"};
			Integer[] shelterLocations = {44,11,41};
			
			for (int i=0;i<3;i++) {
				JPanel inputPanel = new JPanel(new GridLayout(1,1));
				addButton(inputPanel,shelters[i],new ShelterButton(game,this,shelterLocations[i]),320);
				middlePanel.add(inputPanel);
			}
			
		} else if (state.isSavedGameState()) {
			
			for (String gameName:state.getDisplayedSavedGames()) {
				
				//Is there a saved game?
				if (gameName.length()>0) {
					JPanel inputPanel = new JPanel(new GridLayout(1,1));
					addButton(inputPanel,gameName,new LoadGameButton(game,this,gameName),320);
					middlePanel.add(inputPanel);
				}
			}
			
			//Checks if move forward/back and adds buttons for that.
			if (state.getLowerLimitSavedGames()) {
				JPanel inputPanel = new JPanel(new GridLayout(1,1));
				addButton(inputPanel,"Previous",new SearchGameButton(game,this,false),320);
				middlePanel.add(inputPanel);
			}
			
			if (state.getUpperLimitSavedGames()) {
				JPanel inputPanel = new JPanel(new GridLayout(1,1));
				addButton(inputPanel,"Next",new SearchGameButton(game,this,true),320);
				middlePanel.add(inputPanel);
			}
			
			JPanel inputPanel = new JPanel(new GridLayout(1,1));
			addButton(inputPanel,"Back to Game",new GameButton(game,this),320);
			middlePanel.add(inputPanel);
		}

		if (state.isEndGameState()) {
			
			String gameScore = String.format("Your Final Score = %s", state.getFinalScore());
			middlePanel.add(CreateLabelPanel(gameScore, 1));
			middlePanel.add(CreateLabelPanel("Game Over!", 1));
		}
		
		middlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		//Creates the command field
		String[] commands = state.getCommands();
		JPanel bottomPanel = new JPanel(new GridLayout(6,1));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20)); //Top, Left, Bottom, Right
		JPanel inputPanel = new JPanel(new GridLayout(1,1));

		//Checks if displaying load games or shelter locations
		if (!state.isSavedGameState() && state.getResponseType()!=2) {
			
			//Button to display the map
			if (state.getPanelFlag()!=4) {
				addButton(inputPanel,"Map",new MapButton(game,this),320);
				bottomPanel.add(inputPanel);
			}
			
			//Command Field includes four labels above which contain the last three commands.
			//Also one for a blank spot
			for (int i=0;i<commands.length;i++) {
			
				//If blank, adds blank label
				if (commands[i].length()==0 || state.getResponseType() !=0) {
					bottomPanel.add(CreateLabelPanel(commands[i],1));
			
					//Otherwise add button with command
				} else {
					inputPanel = new JPanel(new GridLayout(1,1));
					addButton(inputPanel,commands[i],new CommandButton(game,this,commands[i]),320);
					inputPanel.setBorder(BorderFactory.createEmptyBorder(0,50,0,520));
					bottomPanel.add(inputPanel);
				}
			}
				
			inputPanel = new JPanel(new GridLayout(1,1));
		
			if (!state.isEndGameState()) {
			
				JTextField commandField = new JTextField(2);
				commandField.addKeyListener(new CommandListener(commandField,game,this));
				inputPanel.setBorder(BorderFactory.createEmptyBorder(0,170,0,170));
				inputPanel.add(commandField);
				this.commandField = commandField;
				
			} else {
				addButton(inputPanel,"Exit",new QuitButton(this.frame,false,game,this),280);
				addButton(inputPanel,"Restart",new QuitButton(this.frame,true,game,this),280);
			}
		
			bottomPanel.add(inputPanel);
			
		} else if (state.isSavedGameState()) {
			
			//Add Escape save game button here and hide map
			commander.setSavedGameState(false);

			//Button to escape shelter
		} else {}
				
		this.add(topPanel, BorderLayout.NORTH); // Add to the top of the main layout
		this.add(middlePanel,BorderLayout.CENTER);//String position = BorderLayout.CENTER;
		this.add(bottomPanel,BorderLayout.SOUTH);
	}
	
	public void setCommandField() {
		this.commandField.requestFocusInWindow();
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
	
	private int getLineLength(String line) {
		
		int lineLength = Constants.LINE_LENGTH;
		
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
 */