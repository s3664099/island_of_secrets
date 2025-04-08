/*
Title: Island of Secrets GameUI interface
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 8 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/


package Interfaces;

import Model.GameController;

public interface GameUI {
	void refreshUI(GameController game);
	void setMapPanel(GameController game);
	void closePanel();
}

/* 30 March 2025 - Created file
 * 31 March 2025 - Updated GameUI
 * 4 April 2025 - Added function to set mapPanel
 * 8 April 2025 - Added closePanel function definition
 */
