/*
Title: Island of Secrets GameView Interface
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package interfaces;

import javax.swing.JComponent;

/**
 * Defines the contract for UI components that can be managed by a view controller.
 * Provides lifecycle hooks for view activation/deactivation and component access.
 */
public interface GameView {
	
    /**
     * Called when the view becomes active/visible.
     * Implementations should:
     * - Initialize resources
     * - Register listeners
     * - Update displayed content
     */
	default void onViewActivated() {};
	
    /**
     * Called when the view becomes inactive/hidden.
     * Implementations should:
     * - Clean up resources
     * - Unregister listeners
     * - Persist any state
     */
	default void onViewDeactivated() {};
	
    /**
     * Gets the root Swing component for this view.
     * @return The root JComponent that contains this view's UI elements
     */
	JComponent getViewComponent();
}

/* 5 April 2025 - Created File
 * 14 April 2025 - Added JavaDocs
 * 3 December 2025 - Increased version number
 */
