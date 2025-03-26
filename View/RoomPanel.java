/*
Title: Island of Secrets Status Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 26 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.GridLayout;
import javax.swing.JPanel;

import Interfaces.GameStateProvider;

public class RoomPanel extends JPanel {

	private static final long serialVersionUID = -7153746273218337269L;
	private final GameStateProvider state;

	public RoomPanel(GameStateProvider state) {
		this.state = state;
		configureLayout();
		refresh();
	}
	
	private void configureLayout() {
		JPanel middlePanel = new JPanel(new GridLayout(9,1));
	}
	
	/*
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
	
			if (state.isEndGameState()) {
			
			String gameScore = String.format("Your Final Score = %s", state.getFinalScore());
			middlePanel.add(CreateLabelPanel(gameScore, 1));
			middlePanel.add(CreateLabelPanel("Game Over!", 1));
		}
				middlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
	*/
	
	public void refresh() {
		//timeLabel.setText(state.getTime());
		//timeLabel.setText(state.getStatus());
	}
	
}

/* 25 March 2025 - Created Fle
 * 26 March 2026 - Added code to produce the contents
 */
