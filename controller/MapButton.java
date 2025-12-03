/*
Title: Island of Secrets Map Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import view.GamePanel;


/**
 * An {@link ActionListener} that switches the {@link GamePanel}
 * to its map view when the associated UI control (such as a button)
 * is activated.
 * <p>
 * Typical usage:
 * <pre>{@code
 * JButton mapButton = new JButton("Map");
 * mapButton.addActionListener(new MapButton(gamePanel));
 * }</pre>
 */
public class MapButton implements ActionListener {

    /** The game panel whose map view will be displayed. */
	private final GamePanel panel;
	
    /**
     * Constructs a {@code MapButton} that will show the map view
     * on the specified {@link GamePanel}.
     *
     * @param panel the game panel that provides the map view;
     *              must not be {@code null}
     * @throws NullPointerException if {@code panel} is {@code null}
     */
	public MapButton(GamePanel panel) {
			this.panel = Objects.requireNonNull(panel,"GamePanel cannot be null");
	}
	
    /**
     * Invoked when an action occurs on the associated UI component.
     * This implementation switches the {@link GamePanel} to its map view.
     *
     * @param event the action event triggered by the user interaction
     */
	@Override
	public void actionPerformed(ActionEvent event) {
		panel.showMapView();
	}
}

/* 2 February 2025 - Created Class
 * 5 March 2025 - Increased to v4.0
 * 4 April 2025 - Update controller for new style
 * 6 April 2025 - Updated code to handle new style for view
 * 21 April 2025 - Updated code based on DeepSeek recommendations
 * 27 July 2025 - Removed code not used.
 * 18 September 2025 - Added JavaDocs
 * 3 December 2025 - Increased version number
 */
