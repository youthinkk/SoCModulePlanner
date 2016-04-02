package main;

import gui.MainFrame;

public class Main {
	public static void main(String[] args) {
		Logic logic = new Logic();
		MainFrame mainFrame = new MainFrame(logic);
		
		mainFrame.setVisible(true);
	}
}
