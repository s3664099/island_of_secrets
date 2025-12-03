/*
Title: Island of Secrets MessagePanel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
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

/**
 * MessagePanel is a Swing component that displays a sequence of messages
 * in the game UI, one at a time, for a fixed duration. It switches to
 * the main game view once all messages have been shown.
 *
 * <p>Usage example:
 * <pre>
 *     MessagePanel panel = new MessagePanel(gamePanel);
 *     panel.displayMessages(List.of("Welcome!", "Good luck!"));
 * </pre>
 * This will show each message for {@value #DISPLAY_DURATION_MS} ms and then
 * return to the main view.
 */
public class MessagePanel extends JPanel implements GameView {

	private static final long serialVersionUID = 7193673927279090863L;
	
    /** Parent container that controls view switching. */
	private GamePanel panel;
	
    /** Label used to render the current message text. */
	private JLabel label;

    /** Queue of messages currently being displayed. */
	private List<String> gameMessages;
	
    /** Swing timer that controls the display interval between messages. */
	private Timer messageTimer;
	
    /** Index of the message currently being displayed. */
	private int currentIndex;

    /** Logical font family for the message text. */
	private static final String FONT = "Serif";
	
    /** Font size for the message text. */
	private static final int FONT_SIZE = 24;
	
    /** Duration (in milliseconds) each message remains visible. */	
	private static final int DISPLAY_DURATION_MS = 2000;
	
    /**
     * Constructs a new MessagePanel.
     *
     * @param panel the {@link GamePanel} used to switch views; must not be {@code null}
     * @throws NullPointerException if {@code panel} is {@code null}
     */
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
	
    /**
     * Starts displaying the provided list of messages sequentially.
     * Each message is shown for {@value #DISPLAY_DURATION_MS} milliseconds.
     * When all messages have been displayed, control returns to the main view.
     *
     * @param messages list of messages to display; must not be {@code null}
     * @throws NullPointerException if {@code messages} is {@code null}
     */
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

    /**
     * Shows the next message in the list or returns to the main view
     * if all messages have been displayed. This method is called
     * automatically by the {@link Timer}.
     */
    private void showNextMessage() {

        if (currentIndex < gameMessages.size()) {
        	
            // Show current message
            label.setText("<html><div style='text-align: center;'>" + 
            		gameMessages.get(currentIndex) + "</div></html>");
            currentIndex++;
            messageTimer.restart();
        } else {        	
            messageTimer.stop();
            panel.showMainView();
        }
    }
    
    /**
     * Returns the Swing component representing this view.
     *
     * @return this panel as a {@link JComponent}
     */
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
 * 24 November 2025 - Fixed stalling messages where only one message
 * 3 December 2025 - Increased version number
 */