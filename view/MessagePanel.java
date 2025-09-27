/*
Title: Island of Secrets MessagePanel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.6
Date: 27 September 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import interfaces.GameView;

public class MessagePanel extends JPanel implements GameView {

	private static final long serialVersionUID = 7193673927279090863L;
	private GamePanel panel;
	private JLabel label;

	private List<String> gameMessages;
	private Timer messageTimer;
	private int currentIndex;

	private static final String FONT = "Serif";
	private static final int FONT_SIZE = 24;
	private static final int DISPLAY_DURATION_MS = 2000;
	
    public MessagePanel(GamePanel panel) {
    	
    	this.panel = Objects.requireNonNull(panel, "GamePanel cannot be null");
    	
    	// Set up label
        label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new Font(FONT, Font.BOLD, FONT_SIZE));
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        
        // Set up timer
        messageTimer = new Timer(DISPLAY_DURATION_MS, e -> showNextMessage());
    }
	
    public void displayMessages(List<String> messages) {
        // Stop any current display
        messageTimer.stop();
        
        // Set new messages
        this.gameMessages = Objects.requireNonNull(messages,"Messages can't be null");
        this.currentIndex = 0;
        
        // Start displaying if we have messages
        if (!this.gameMessages.isEmpty()) {
            panel.showMessageView();
            showNextMessage();
        }
    }

    private void showNextMessage() {
        if (currentIndex < gameMessages.size()) {
        	
            // Show current message
            label.setText("<html><div style='text-align: center;'>" + 
            		gameMessages.get(currentIndex) + "</div></html>");
            currentIndex++;
            
            // Start timer for next message or auto-close
            if (currentIndex < gameMessages.size()) {
                messageTimer.restart();
            }
        } else {        	
            messageTimer.stop();
            panel.showMainView();
        }
    }
    
	@Override
	public JComponent getViewComponent() {
		return this;
	}
}
/* 30 November 2024 - Created File
 * 1 December 2024 - Change name of class to specific name
 * 2 December 2024 - Added longer delay for last display
 * 4 December 2024 - Added code to have different messages appear if repeating
 * 23 December 2024 - Updated to version 2.
 * 18 January 2025 - Removed the unused includes and added a serializable section to get rid of warnings.
 * 31 January 2025 - Completed Testing and increased version
 * 5 March 2025 - Increased to v4.0
 * 21 March 2025 - Updated for MessageBuilder class
 * 22 March 2025 - Fixed issue with MessagePanel - works now.
 * 26 March 2025 - Commented out code to allow code to run
 * 24 July 2025 - Started updating message panel to display special messages
 * 26 July 2025 - The class now works without errors, but does not display message
 * 				- Message now displays
 * 				- Message now returns to the main at the end
 * 27 September 2025 - Updated code based on recommendations.
 * 					 - Added JavaDocs
 */