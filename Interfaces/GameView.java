/*
Title: Island of Secrets GameView Interface
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 5 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Interfaces;

import javax.swing.JComponent;

public interface GameView {
	void onViewActivated();
	void onViewDeactivated();
	JComponent getViewComponent();
}

/* 5 April 2025 - Created File
 */
