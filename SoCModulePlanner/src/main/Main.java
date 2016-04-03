package main;

import gui.MainFrame;

public class Main {
	public static void main(String[] args) {
		Logic logic = new Logic();
		MainFrame mainFrame = new MainFrame();
		
		mainFrame.setLogic(logic);
		mainFrame.setVisible(true);
	}
}
