/*
Title: Island of Secrets Status Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 16 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import interfaces.GameStateProvider;

/**
 * Displays game status information including time and player status.
 * Updates automatically when game state changes.
 */
public class StatusPanel extends JPanel {
	
	private static final long serialVersionUID = 582607980142319020L;
	
	//UI Components
	private final JLabel timeLabel = new JLabel();
	private final JLabel statusLabel = new JLabel();
	
	//State
	private GameStateProvider state;
	
	public StatusPanel(GameStateProvider state) {
		this.state = Objects.requireNonNull(state, "GameStateProvider cannot be null");
		initialiseComponents();
		updateDisplay();
	}
	
	private void initialiseComponents() {
		setLayout(new GridLayout(2, 1));
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		
		JPanel timePanel = createLabelPanel(timeLabel);
		JPanel statusPanel = createLabelPanel(statusLabel);
		
		add(timePanel);
		add(statusPanel);
	}
	
	private JPanel createLabelPanel(JLabel label) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.add(label);
		return panel;
	}
	
    /**
     * Updates the panel with new game state
     * @param state The current game state provider
     */
	public void refreshUI(GameStateProvider state) {
		GameStateProvider newState = 
				Objects.requireNonNull(state, "GameStateProvider cannot be null");
		
		if(checkChange(newState.getTime(),state.getTime()) || 
				checkChange(newState.getStatus(),state.getStatus())) {
			this.state = newState;
			updateDisplay();
		}
	}
	
	private boolean checkChange(String newState, String oldState) {
		return oldState.equals(newState);
	}
	
	private void updateDisplay() {
		if (SwingUtilities.isEventDispatchThread()) {
		    applyDisplay();
		} else {
		    SwingUtilities.invokeLater(this::applyDisplay);
		}
	}
	
	private void applyDisplay() {
		timeLabel.setText(state.getTime());
		statusLabel.setText(state.getStatus());
	}
}

/* 25 March 2025 - Created File
 * 26 March 2025 - Fixed error with time not showing
 * 3 April 2025 - Updated code to take Game State
 * 16 April 2025 - Updated code based on DeepSeek
 * 25 September 2025 - Updated code based on recommendations
 */
