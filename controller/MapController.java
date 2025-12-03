/*
Title: Island of Secrets Map Controller
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * Handles creation of mouse-hover behavior for map panels in the game UI.
 * <p>
 * The {@code MapController} supplies a {@link MouseAdapter} that highlights
 * a room's panel when the mouse enters and restores it when the mouse exits.
 * </p>
 */
public class MapController {
	
    /** Highlight color applied when the mouse pointer hovers over a room panel. */
	private static final Color HOVER_COLOUR = new Color(240, 240, 255);
	
    /** 
     * Creates a new {@code MapController}. 
     * Currently stateless; no initialization is required.
     */
	public MapController() {}
	
    /**
     * Builds a {@link MouseAdapter} that changes a {@link JPanel}'s background
     * color when the mouse enters or exits the panel.
     *
     * @param roomId the unique identifier of the room associated with the panel.
     *               Although not currently used in the hover logic, it can be
     *               leveraged by future features such as room selection or tooltips.
     * @return a {@code MouseAdapter} to attach to the specified room panel.
     *
     * @implNote
     * The adapter sets the panel's background to {@link #HOVER_COLOUR} on
     * {@link MouseAdapter#mouseEntered(MouseEvent)} and restores it to the
     * panel's default background (by setting {@code null}) on
     * {@link MouseAdapter#mouseExited(MouseEvent)}.
     * Ensure that restoring to {@code null} is appropriate for your look-and-feel.
     */
	public MouseAdapter createMouseAdapter(int roomId) {
		return new MouseAdapter() {
			
            /** {@inheritDoc} */
			public void mouseEntered(MouseEvent e) {
				
				JPanel panel = (JPanel) e.getSource();
				panel.setBackground(HOVER_COLOUR);
				panel.repaint();
			}
			
            /** {@inheritDoc} */
			public void mouseExited(MouseEvent e) {
				
				JPanel panel = (JPanel) e.getSource();
				panel.setBackground(null);
				panel.repaint();
			}
		};
	}
}

/* 21 April 2025 - Created File
 * 18 September 2025 - Update file by adding HOVER_COLOUR and removing unused variables
 * 					 - Added JavaDocs
 * 3 December 2025 - Increased version number
 */
