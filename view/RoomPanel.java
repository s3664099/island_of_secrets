/*
Title: Island of Secrets Room Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.6
Date: 18 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import data.Constants;
import interfaces.GameStateProvider;

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
				
		add(createPanel(roomLabel));
		addItemPanels(state.getItems());
		add(createPanel(exitLabel));
		add(createPanel(specialExitLabel));
		addMessagePanels(state.getMessage());
	}
	
	private JPanel createPanel(JLabel label) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(label);
		return panel;
	}
	
	private void addItemPanels(String itemsText) {
		processItemsText(itemsText);
		for (int index=0;index<itemLabelList.size();index++) {
			add(createPanel(itemLabelList.get(index)));
		}
	}
	
	private void addMessagePanels(List<String> messages) {
		for (int index = 0;index<messages.size();index++) {
			messageLabelList.add(new JLabel());
			add(createPanel(messageLabelList.get(index)));
		}
	}
	
	public void refreshUI(GameStateProvider state) {
		
		this.state = Objects.requireNonNull(state);
		
		resetComponents();
		updateDisplay();
	}
	
	private void resetComponents() {
		SwingUtilities.invokeLater(() -> {
			messageLabelList.clear();
			itemLabelList.clear();
			itemTextList.clear();
			removeAll();
			initialiseComponents();
		});
	}
		
	public void updateDisplay() {
		
		SwingUtilities.invokeLater(() -> {
			roomLabel.setText(state.getRoom());
			updateLabels(itemTextList,itemLabelList);
			exitLabel.setText(state.getExits());
			specialExitLabel.setText(state.getSpecialExits());
			updateLabels(state.getMessage(),messageLabelList);
			revalidate();
			repaint();
		});
	}
	
	private void updateLabels(List<String> labelText,List<JLabel> labels) {
		for (int i = 0;i<labelText.size() && i<labels.size();i++) {
			labels.get(i).setText(labelText.get(i));
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
 * 18 April 2025 - Removed redundant functions, and updated rest as per DeepSeek.
 */
