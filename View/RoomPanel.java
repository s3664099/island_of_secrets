/*
Title: Island of Secrets Room Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 1 April 2025
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
	private final GameStateProvider state;
	
	private final JLabel roomLabel = new JLabel();
	private final JLabel exitLabel = new JLabel();
	private final JLabel specialExitLabel = new JLabel();
	private List<JLabel> messageLabelList  = new ArrayList<JLabel>();
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
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
				
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
		
		//MessageDisplay
		for (int i=0;i<state.getMessage().size();i++) {
			JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			messageLabelList.add(new JLabel());
			messagePanel.add(messageLabelList.get(i));
			add(messagePanel);
		}
	}
	
	public void refreshUI() {
		removeAll();
		configureLayout();
		refresh();
	}
		
	public void refresh() {
		
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
	
	private void determineLength(String displayString, List<JLabel> labelList) {
		System.out.println(displayString);
		System.out.println("Hello");
		while (displayString.length()>0) {
			System.out.println(displayString);
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
 * 28 March 2025 - Complete panel by adding message label
 * 1 April 2025 - Updated refresh to display items
 */
