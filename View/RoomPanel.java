/*
Title: Island of Secrets Room Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.5
Date: 3 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Data.Constants;
import Interfaces.GameStateProvider;

public class RoomPanel extends JPanel {

	private static final long serialVersionUID = -7153746273218337269L;
	
	//Components
	private final JLabel roomLabel = new JLabel();
	private final JLabel exitLabel = new JLabel();
	private final JLabel specialExitLabel = new JLabel();
	private List<JLabel> messageLabelList  = new ArrayList<JLabel>();
	private List<JLabel> itemLabelList  = new ArrayList<JLabel>();
	private List<String> itemTextList = new ArrayList<String>();
	
	//State
	private GameStateProvider state;
	
	public RoomPanel(GameStateProvider state) {
		this.state = state;
		initialiseComponents();
		updateDisplay();
	}
	
	private void initialiseComponents() {
		setLayout(new GridLayout(9,1));
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		add(createRoomPanel());
		addItemPanels(state.getItems());
		

		

		//Exit Panels
		JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		exitPanel.add(exitLabel);
		add(exitPanel);
		
		JPanel specialExitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		specialExitPanel.add(specialExitLabel);
		add(specialExitPanel);
		
		//MessageDisplay
		for (int i=0;i<state.getMessage().size();i++) {
			JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			messageLabelList.add(new JLabel());
			messagePanel.add(messageLabelList.get(i));
			add(messagePanel);
		}
	}
	
	private JPanel createRoomPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(roomLabel);
		return panel;
	}
	
	private void addItemPanels(String itemsText) {
		processItemsText(itemsText);
		for (int i=0;i<itemLabelList.size();i++) {
			add(createItemPanel(i));
		}
	}
	
	private JPanel createItemPanel(int index) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(itemLabelList.get(index));
		return panel;
	}
	
	public void refreshUI(GameStateProvider state) {
		
		this.state = state;
		
		//Clears the lists
		messageLabelList.clear();
		itemLabelList.clear();
		itemTextList.clear();
		
		removeAll();
		initialiseComponents();
		updateDisplay();
	}
		
	public void updateDisplay() {
		
		roomLabel.setText(state.getRoom());

		for (int i=0;i<itemLabelList.size();i++) {
			itemLabelList.get(i).setText(itemTextList.get(i));
		}
		
		exitLabel.setText(state.getExits());
		specialExitLabel.setText(state.getSpecialExits());
		
		//Adds message
		for (int i=0;i<messageLabelList.size();i++) {
			messageLabelList.get(i).setText(state.getMessage().get(i));
		}
	}

	private void processItemsText(String displayString) {
		int remainingLength = displayString.length();
		while(remainingLength>0) {
			int lineLength = calculateLineLength(displayString,remainingLength);
			itemTextList.add(displayString.substring(0,lineLength));
			itemLabelList.add(new JLabel());
			displayString = displayString.substring(lineLength);
			remainingLength = displayString.length();
		}
	}
	
	private int calculateLineLength(String line, int remainingLength) {
		int lineLength = Math.min(Constants.LINE_LENGTH, remainingLength);
		if (remainingLength>99) {
			lineLength = findLastWhitespace(line,lineLength);
		}
		return lineLength;
	}
	
	private int findLastWhitespace(String line, int initialLength) {
		int adjustedLength = initialLength;
		while (adjustedLength>0 && !Character.isWhitespace(line.charAt(adjustedLength))) {
			adjustedLength--;
		}
		return adjustedLength>0?adjustedLength:initialLength;
	}
}

/* 25 March 2025 - Created File
 * 26 March 2026 - Added code to produce the contents
 * 28 March 2025 - Complete panel by adding message label
 * 1 April 2025 - Updated refresh to display items
 * 2 April 2025 - Fixed issue with items not displaying
 * 3 April 2025 - Updated code to take the Game State
 */
