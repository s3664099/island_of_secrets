/*
Title: Island of Secrets Quit Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.0
Date: 31 January 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import View.GameFrame;

public class QuitButton implements ActionListener {
	
	private GameFrame frame;
	
	public QuitButton(GameFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.frame.dispose();
	}

}

/* 23 December 2024 - Create File
 * 31 January 2025 - Completed Testing and increased version
 */
