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
