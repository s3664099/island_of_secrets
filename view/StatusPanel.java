/*
Title: Island of Secrets Status Panel
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
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import interfaces.GameStateProvider;

/**
 * A Swing panel that displays the current game time and player status.
 * <p>
 * The panel consists of two centered labels arranged in a vertical
 * {@link GridLayout}. It observes a {@link GameStateProvider} and updates
 * its labels whenever the game state changes, but only if the displayed
 * values differ from the previous state to avoid unnecessary repaints.
 * </p>
 */
public class StatusPanel extends JPanel {
	
	private static final long serialVersionUID = 582607980142319020L;
	
    /** Label showing the current game time. */
	private final JLabel timeLabel = new JLabel();
	
    /** Label showing the current player status. */
	private final JLabel statusLabel = new JLabel();
	
    /** Provides the latest game time and status values. */
	private GameStateProvider state;
	
    /**
     * Constructs a new {@code StatusPanel} bound to the given
     * {@link GameStateProvider}.
     *
     * @param state the non-null game state provider supplying time and status
     * @throws NullPointerException if {@code state} is {@code null}
     */
	public StatusPanel(GameStateProvider state) {
		this.state = Objects.requireNonNull(state, "GameStateProvider cannot be null");
		initialiseComponents();
		updateDisplay();
	}
	
    /**
     * Initializes the panel’s layout and child components.
     * Creates two sub-panels, each centering a single label.
     */
	private void initialiseComponents() {
		setLayout(new GridLayout(2, 1));
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		
		JPanel timePanel = createLabelPanel(timeLabel);
		JPanel statusPanel = createLabelPanel(statusLabel);
		
		add(timePanel);
		add(statusPanel);
	}
	
    /**
     * Wraps a {@link JLabel} in a panel with centered {@link FlowLayout}.
     *
     * @param label the label to be placed in the panel
     * @return a new panel containing the provided label
     */
	private JPanel createLabelPanel(JLabel label) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.add(label);
		return panel;
	}
	
    /**
     * Refreshes the UI with a new game state if the displayed values have changed.
     * <p>
     * If the supplied provider reports the same time and status as the current
     * provider, no update occurs.
     * </p>
     *
     * @param state a non-null provider of the latest game state
     * @throws NullPointerException if {@code state} is {@code null}
     */
	public void refreshUI(GameStateProvider updatedState) {
		GameStateProvider newState = 
				Objects.requireNonNull(updatedState, "GameStateProvider cannot be null");
		
		if(checkChange(newState.getTime(),state.getTime()) || 
				checkChange(newState.getStatus(),state.getStatus())) {
			this.state = newState;
			updateDisplay();
		}
	}
	
    /**
     * Determines whether two string values differ.
     *
     * @param newState the new string value
     * @param oldState the previous string value
     * @return {@code true} if the values differ, {@code false} otherwise
     */
	private boolean checkChange(String newState, String oldState) {
		return !oldState.equals(newState);
	}
	
    /**
     * Schedules a UI refresh. Ensures updates run on the Event Dispatch Thread.
     */
	private void updateDisplay() {
		if (SwingUtilities.isEventDispatchThread()) {
		    applyDisplay();
		} else {
		    SwingUtilities.invokeLater(this::applyDisplay);
		}
	}
	
    /**
     * Applies the latest time and status values to their respective labels.
     * Must be called on the Event Dispatch Thread.
     */
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
 * 					 - Added JavaDocs
 * 18 October 2025 - Fixed issue where stats not updating
 * 3 December 2025 - Increased version number
 */
