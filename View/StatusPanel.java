/*
Title: Island of Secrets Status Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 25 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Interfaces.GameStateProvider;

public class StatusPanel extends JPanel {
	
	private static final long serialVersionUID = 582607980142319020L;
	private final JLabel timeLabel = new JLabel();
	private final JLabel statusLabel = new JLabel();
	private final GameStateProvider state;
	
	public StatusPanel(GameStateProvider state) {
		this.state = state;
		configureLayout();
		refresh();
	}
	
	private void configureLayout() {
		setLayout(new GridLayout(2, 1));
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		//Time Display
		JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		timePanel.add(timeLabel);
		
		//Status Display
		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		statusPanel.add(statusLabel);
		
		add(timePanel);
		add(statusPanel);
	}
	
	public void refresh() {
		timeLabel.setText(state.getTime());
		timeLabel.setText(state.getStatus());
	}

}

/* 25 March 2025 - Created File
 */
