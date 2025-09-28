/*
Title: Island of Secrets Lightning Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.6
Date: 28 September 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import interfaces.GameView;

public class LightningPanel extends JPanel implements GameView {

	private static final long serialVersionUID = 1370519661015151132L;
	
	private int currentIndex;
	private JLabel label;
	private final GamePanel panel;
	private Timer lightningTimer;
	
	private static final String FONT = "Serif";
	private static final int FONT_SIZE = 36;
	
	/** Duration of the Lightning Effect displayed by the panel in Microseconds */
	private static final int DISPLAY_DURATION_MS = 100;
	private static final int NUMB_FLASHES = 31;
	
    public LightningPanel(GamePanel panel) {
        this.panel = Objects.requireNonNull(panel, "GamePanel cannot be null");;
        
        // Set up label
        label = new JLabel("⚡⚡ Lightning Flashes ⚡⚡", SwingConstants.CENTER);
        label.setFont(new Font(FONT, Font.BOLD, FONT_SIZE));
        label.setForeground(Color.WHITE);
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        
        // Set up timer
        lightningTimer = new Timer(DISPLAY_DURATION_MS, e -> showNextEffect());
    }
	
	
    public void startLightningEffect() {
    	
        // Stop any current display
        lightningTimer.stop();
        this.currentIndex = 0;
        
        if (this.currentIndex<NUMB_FLASHES) {
        	panel.showLightningView();
        	showNextEffect();
        }
    }
    
    private void showNextEffect() {
    	
    	if(this.currentIndex<NUMB_FLASHES) {
    		if ((this.currentIndex&1)==0) {
    			setBackground(Color.YELLOW);
    		} else {
    			setBackground(Color.BLACK);
    		}
    		this.currentIndex++;
    	} else {
    		lightningTimer.stop();
            panel.showMainView();
    	}
    }

	public JComponent getViewComponent() {
		return this;
	}
}
/* 25 November 2024 - Created File
 * 23 December 2024 - Updated to version 2.
 * 18 January 2025 - Removed the unused includes and added a serializable section to get rid of warnings.
 * 31 January 2025 - Completed Testing and increased version
 * 5 March 2025 - Increased to v4.0
 * 21 March 2025 - Removed the notes
 * 26 March 2025 - Commented out code to allow to run
 * 30 March 2025 - Removed unusued code
 * 28 July 2025 - Updated code for new architecture
 * 15 Aug 2025 - Panel now displays correctly
 * 28 September 2025 - Updated code.
*/