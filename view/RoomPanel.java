/*
Title: Island of Secrets Room Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
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

/**
 * Panel responsible for displaying the current room's information,
 * including room name, items, exits, special exits, and messages.
 * <p>
 * Uses {@link JLabel} components arranged in a {@link GridLayout} to
 * present game state text to the player. Supports dynamic updates by
 * refreshing labels when the {@link GameStateProvider} changes.
 */
public class RoomPanel extends JPanel {

	private static final long serialVersionUID = -7153746273218337269L;
	
    // --- UI Components ---

    /** Label displaying the current room name or description. */
	private final JLabel roomLabel = new JLabel();
	
    /** Label listing standard exits from the current room. */
	private final JLabel exitLabel = new JLabel();
	
    /** Label listing any special or hidden exits. */
	private final JLabel specialExitLabel = new JLabel();
	
    /** Dynamic labels used to display in-room messages. */
	private List<JLabel> messageLabelList  = new ArrayList<JLabel>();
	
    /** Dynamic labels used to display items present in the room. */
	private List<JLabel> itemLabelList  = new ArrayList<JLabel>();
	
    /** Text segments representing the items in the room. */
	private List<String> itemTextList = new ArrayList<String>();
	
    // --- State ---

    /** Current snapshot of the game state used to populate the panel. */
	private GameStateProvider state;
	
    /**
     * Creates a panel bound to the given game state.
     *
     * @param state the current game state provider
     */
	public RoomPanel(GameStateProvider state) {
		this.state = state;
		initialiseComponents();
		updateDisplay();
	}
	
    /**
     * Builds and lays out the panel components,
     * including room, item, exit, and message labels.
     */
	private void initialiseComponents() {
		setLayout(new GridLayout(9,1));
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
				
		add(createPanel(roomLabel));
		addItemPanels(state.getItems());
		add(createPanel(exitLabel));
		add(createPanel(specialExitLabel));
		addMessagePanels(state.getMessage());
	}
	
    /**
     * Wraps a {@link JLabel} in a left-aligned {@link JPanel}.
     *
     * @param label the label to wrap
     * @return a panel containing the label
     */
	private JPanel createPanel(JLabel label) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(label);
		return panel;
	}
	
    /**
     * Creates and adds panels for displaying items in the current room.
     *
     * @param itemsText the raw items string from the state
     */
	private void addItemPanels(String itemsText) {
		processItemsText(itemsText);
		for (int index=0;index<itemLabelList.size();index++) {
			add(createPanel(itemLabelList.get(index)));
		}
	}
	
    /**
     * Creates and adds panels for displaying messages in the current room.
     *
     * @param messages the list of messages from the state
     */
	private void addMessagePanels(List<String> messages) {
		for (int index = 0;index<messages.size();index++) {
			messageLabelList.add(new JLabel());
			add(createPanel(messageLabelList.get(index)));
		}
	}
	
    /**
     * Refreshes the panel with a new {@link GameStateProvider}.
     * Clears existing labels and rebuilds the display.
     *
     * @param state the new game state provider
     */
	public void refreshUI(GameStateProvider state) {
		
		this.state = Objects.requireNonNull(state);
		resetComponents();
		updateDisplay();
	}
	
    /**
     * Clears all labels and reinitializes components.
     * Executed on the Swing event dispatch thread.
     */
	private void resetComponents() {
		SwingUtilities.invokeLater(() -> {
			messageLabelList.clear();
			itemLabelList.clear();
			itemTextList.clear();
			removeAll();
			initialiseComponents();
		});
	}
	
    /**
     * Updates label text based on the current {@link GameStateProvider}.
     * Executed on the Swing event dispatch thread.
     */
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
	
    /**
     * Updates the given list of labels with new text values.
     *
     * @param labelText the text values to set
     * @param labels the labels to update
     */
	private void updateLabels(List<String> labelText,List<JLabel> labels) {
		for (int i = 0;i<labelText.size() && i<labels.size();i++) {
			labels.get(i).setText(labelText.get(i));
		}
	}

    /**
     * Splits an item text string into wrapped lines and
     * creates labels for each segment.
     *
     * @param displayString the raw item string to process
     */
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
	
    /**
     * Calculates the appropriate line length for wrapping.
     *
     * @param line the current line of text
     * @param remainingLength the characters left to process
     * @return the adjusted line length
     */
	private int calculateLineLength(String line, int remainingLength) {
		int lineLength = Math.min(Constants.LINE_LENGTH, remainingLength);
		if (remainingLength>99) {
			lineLength = findLastWhitespace(line,lineLength);
		}
		return lineLength;
	}
	
    /**
     * Finds the last whitespace before a given index to avoid splitting words.
     *
     * @param line the text being processed
     * @param initialLength the initial maximum length
     * @return the adjusted length up to the last whitespace, or the original length
     */
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
 * 24 September 2025 - Added JavaDocs
 * 3 December 2025 - Increased version number
 */
