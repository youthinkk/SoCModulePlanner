package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Logic;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private Logic _logic;
	
	public MainFrame() {
		
	}

	/**
	 * Create the frame.
	 */
	public MainFrame(Logic logic) {
		_logic = logic;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 780, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		PreferencePanel  preferencePanel = new PreferencePanel(_logic);
		getContentPane().add(preferencePanel);
	}
}
