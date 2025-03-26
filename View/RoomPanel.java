/*
Title: Island of Secrets Status Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 26 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Data.Constants;
import Interfaces.GameStateProvider;

public class RoomPanel extends JPanel {

	private static final long serialVersionUID = -7153746273218337269L;
	private final GameStateProvider state;
	
	private final JLabel roomLabel = new JLabel();
	private final JLabel exitLabel = new JLabel();
	private final JLabel specialExitLabel = new JLabel();
	private List<JLabel> itemLabelList  = new ArrayList<JLabel>();
	private List<String> itemTextList = new ArrayList<String>();
	
	public RoomPanel(GameStateProvider state) {
		this.state = state;
		configureLayout();
		refresh();
	}
	
	private void configureLayout() {
		setLayout(new GridLayout(9,1));
		determineLength(state.getItems(),itemLabelList);
				
		//Room Display
		JPanel roomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		roomPanel.add(roomLabel);
		add(roomPanel);
		
		//ItemDisplay
		for (int i=0;i<itemLabelList.size();i++) {
			JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			itemPanel.add(itemLabelList.get(i));
			add(itemPanel);
		}
		
		//Exit Panels
		JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		exitPanel.add(exitLabel);
		add(exitPanel);
		
		JPanel specialExitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		specialExitPanel.add(specialExitLabel);
		add(specialExitPanel);
	}
	
	/*
					
	//Display message
	middlePanel.add(CreateLabelPanel("", 1));
	List<String> messages = state.getMessage();
			
	for (String msg:messages) {
		middlePanel.add(CreateLabelPanel(msg, 1));
	}
	
			if (state.isEndGameState()) {
			
			String gameScore = String.format("Your Final Score = %s", state.getFinalScore());
			middlePanel.add(CreateLabelPanel(gameScore, 1));
			middlePanel.add(CreateLabelPanel("Game Over!", 1));
		}
				middlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
	*/
	
	public void refresh() {
		roomLabel.setText(state.getRoom());
		exitLabel.setText(state.getExits());
		specialExitLabel.setText(state.getSpecialExits());
		
		for (int i=0;i<itemLabelList.size();i++) {
			itemLabelList.get(i).setText(itemTextList.get(i));
		}
	}
	
	private void determineLength(String displayString, List<JLabel> labelList) {

		while (displayString.length()>0) {		
			int lineLength = getLineLength(displayString);
			itemTextList.add(displayString.substring(0,lineLength));
			displayString = displayString.substring(lineLength);
			labelList.add(new JLabel());
		}

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

/* 25 March 2025 - Created File
 * 26 March 2026 - Added code to produce the contents
 */
