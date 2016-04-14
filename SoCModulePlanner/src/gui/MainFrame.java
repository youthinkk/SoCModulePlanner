package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import constant.Constant;
import main.Logic;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private QuestionPanel _questionPanel = new QuestionPanel();
	private ModulePanel _modulePanel = new ModulePanel();
	private Logic _logic;
	
	private String _matriculationYear;
	private String _major;
	private String _focusArea;
	
	private ArrayList<String> _takenModules;
	private ArrayList<String> _likedModules;
	
	private boolean _isMathTaken;
	private boolean _isPhysicsTaken;
	private boolean _isFromPoly;
	
	private final int DEFAULT_SELECTED_INDEX = -1;
	
	private ActionListener _matriculationYearListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (_questionPanel.comboBox.getSelectedIndex() != -1) {
				_matriculationYear = _questionPanel.comboBox.getSelectedItem().toString();
				askMajor();
			} else {
				showErrorDialog("Matriculation year is not selected.");
			}
		}
		
	};
	
	private ActionListener _majorListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (_questionPanel.comboBox.getSelectedIndex() != -1) {
				_major = _questionPanel.comboBox.getSelectedItem().toString();
				
				if (_major.equals(Constant.MAJOR_COMPUTER_SCIENCE)) {
					askFocusArea();
				} else {
					askMath();
				}
			} else {
				showErrorDialog("Major is not selected.");
			}
		}
		
	};
	
	private ActionListener _focusAreaListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (_questionPanel.comboBox.getSelectedIndex() != -1) {
				_focusArea = _questionPanel.comboBox.getSelectedItem().toString();
				askMath();
			} else {
				showErrorDialog("Focus area is not selected.");
			}
		}
		
	};
	
	private ActionListener _askMathListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (_questionPanel.comboBox.getSelectedIndex() != -1) {
				_isMathTaken = _questionPanel.comboBox.getSelectedItem().equals(Constant.REPLY_YES);
				askPhysics();
			} else {
				showErrorDialog("The question is not answered.");
			}
		}
		
	};
	
	private ActionListener _askPhysicsListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (_questionPanel.comboBox.getSelectedIndex() != -1) {
				_isPhysicsTaken = _questionPanel.comboBox.getSelectedItem().equals(Constant.REPLY_YES);
				askPreUni();
			} else {
				showErrorDialog("The question is not answered.");
			}
		}
		
	};
	
	private ActionListener _askPreUniListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (_questionPanel.comboBox.getSelectedIndex() != -1) {
				_isFromPoly = _questionPanel.comboBox.getSelectedItem().equals(Constant.REPLY_YES);
				askTakenModules();
			} else {
				showErrorDialog("The question is not answered.");
			}
		}
		
	};
	
	private ActionListener _takenModulesListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			_takenModules = _modulePanel.getSelectedList();
			askLikedModules();
		}
		
	};
	
	private ActionListener _likedModulesListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			_likedModules = _modulePanel.getSelectedList();
			executePlanner();
		}
		
	};

	public MainFrame() {
		
		setTitle("SoC Module Planner");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 495, 175);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		getContentPane().add(_questionPanel);
		askMatriculationYear();
	}
	
	private void askMatriculationYear() {
		clearActionListener();
		
		_questionPanel.label.setText(Constant.ASK_MATRICULATION_YEAR);
		
		_questionPanel.comboBox.removeAllItems();
		_questionPanel.comboBox.addItem(Constant.ACADEMIC_YEAR_2015_2016);
		_questionPanel.comboBox.addItem(Constant.ACADEMIC_YEAR_2014_2015);
		_questionPanel.comboBox.addItem(Constant.ACADEMIC_YEAR_2013_2014);
		_questionPanel.comboBox.addItem(Constant.ACADEMIC_YEAR_2012_2013);
		_questionPanel.comboBox.addItem(Constant.ACADEMIC_YEAR_2011_2012);
		_questionPanel.comboBox.addItem(Constant.ACADEMIC_YEAR_2010_2011);
		_questionPanel.comboBox.setSelectedIndex(DEFAULT_SELECTED_INDEX);
		
		_questionPanel.nextButton.addActionListener(_matriculationYearListener);
	}
	
	private void askMajor() {
		clearActionListener();
		
		_questionPanel.label.setText(Constant.ASK_MAJOR);
		
		_questionPanel.comboBox.removeAllItems();
		_questionPanel.comboBox.addItem(Constant.MAJOR_BUSINESS_ANALYTICS);
		_questionPanel.comboBox.addItem(Constant.MAJOR_COMPUTER_ENGINEERING);
		_questionPanel.comboBox.addItem(Constant.MAJOR_COMPUTER_SCIENCE);
		_questionPanel.comboBox.addItem(Constant.MAJOR_INFORMATION_SYSTEM);
		
		if (_matriculationYear.equals(Constant.ACADEMIC_YEAR_2015_2016)) {
			_questionPanel.comboBox.addItem(Constant.MAJOR_INFORMATION_SECURITY);
		}
		
		_questionPanel.comboBox.setSelectedIndex(DEFAULT_SELECTED_INDEX);
		
		_questionPanel.nextButton.addActionListener(_majorListener);
	}
	
	private void askFocusArea() {
		clearActionListener();
		
		_questionPanel.label.setText(Constant.ASK_FOCUS_AREA);
		
		_questionPanel.comboBox.removeAllItems();
		if (_matriculationYear.equals(Constant.ACADEMIC_YEAR_2015_2016)) {
			addFocusAreaAfterAY1516(_questionPanel.comboBox);
		} else {
			addFocusAreaBeforeAY1516(_questionPanel.comboBox);
		}
		_questionPanel.comboBox.setSelectedIndex(DEFAULT_SELECTED_INDEX);
		
		_questionPanel.nextButton.addActionListener(_focusAreaListener);
	}
	
	private void askMath() {
		clearActionListener();
		
		_questionPanel.label.setText(Constant.ASK_H2_MATH_TAKEN);
		
		_questionPanel.comboBox.removeAllItems();
		_questionPanel.comboBox.addItem(Constant.REPLY_YES);
		_questionPanel.comboBox.addItem(Constant.REPLY_NO);
		_questionPanel.comboBox.setSelectedIndex(DEFAULT_SELECTED_INDEX);
		
		_questionPanel.nextButton.addActionListener(_askMathListener);
	}
	
	private void askPhysics() {
		clearActionListener();
		
		_questionPanel.label.setText(Constant.ASK_H2_PHYSICS_TAKEN);

		_questionPanel.comboBox.removeAllItems();
		_questionPanel.comboBox.addItem(Constant.REPLY_YES);
		_questionPanel.comboBox.addItem(Constant.REPLY_NO);
		_questionPanel.comboBox.setSelectedIndex(DEFAULT_SELECTED_INDEX);
		
		_questionPanel.nextButton.addActionListener(_askPhysicsListener);
	}
	
	private void askPreUni() {
		clearActionListener();
		
		_questionPanel.label.setText(Constant.ASK_FROM_POLY);

		_questionPanel.comboBox.removeAllItems();
		_questionPanel.comboBox.addItem(Constant.REPLY_YES);
		_questionPanel.comboBox.addItem(Constant.REPLY_NO);
		_questionPanel.comboBox.setSelectedIndex(DEFAULT_SELECTED_INDEX);
		
		_questionPanel.nextButton.addActionListener(_askPreUniListener);
	}
	
	private void askTakenModules() {
		clearActionListener();
		
		_modulePanel.queryLabel.setText(Constant.ASK_TAKEN_MODULES);
		_modulePanel.availableLabel.setText(Constant.LABEL_AVAILABLE_MODULES);
		_modulePanel.selectedLabel.setText(Constant.LABEL_TAKEN_MODULES);
		_modulePanel.clearSelectedList();
		_modulePanel.nextButton.addActionListener(_takenModulesListener);
		
		getContentPane().remove(_questionPanel);
		getContentPane().add(_modulePanel);
		setSize(_modulePanel.getPreferredSize());
	}
	
	private void askLikedModules() {
		clearActionListener();
		
		_modulePanel.queryLabel.setText(Constant.ASK_LIKED_MODULES);
		_modulePanel.availableLabel.setText(Constant.LABEL_AVAILABLE_MODULES);
		_modulePanel.selectedLabel.setText(Constant.LABEL_LIKED_MODULES);
		_modulePanel.clearSelectedList();
		_modulePanel.nextButton.addActionListener(_likedModulesListener);
	}
	
	private void executePlanner() {
		clearActionListener();
	}
	
	private void addFocusAreaAfterAY1516(JComboBox<String> combo) {
		_questionPanel.comboBox.addItem(Constant.FOCUS_ALGORITHMS_THEORY);
		_questionPanel.comboBox.addItem(Constant.FOCUS_ARTIFICIAL_INTELLIGENCE);
		_questionPanel.comboBox.addItem(Constant.FOCUS_COMPUTER_GRAPHICS_GAMES);
		_questionPanel.comboBox.addItem(Constant.FOCUS_COMPUTER_SECURITY);
		_questionPanel.comboBox.addItem(Constant.FOCUS_DATABASE_SYSTEMS);
		_questionPanel.comboBox.addItem(Constant.FOCUS_MULTIMEDIA_INFORMATION_RETRIEVAL);
		_questionPanel.comboBox.addItem(Constant.FOCUS_NETWORKING_DISTRIBUTED_SYSTEMS);
		_questionPanel.comboBox.addItem(Constant.FOCUS_PARALLEL_COMPUTING);
		_questionPanel.comboBox.addItem(Constant.FOCUS_PROGRAMMING_LANGUAGES);
		_questionPanel.comboBox.addItem(Constant.FOCUS_SOFTWARE_ENGINEERING);
	}
	
	private void addFocusAreaBeforeAY1516(JComboBox<String> combo) {
		_questionPanel.comboBox.addItem(Constant.FOCUS_ALGORITHMS_THEORY);
		_questionPanel.comboBox.addItem(Constant.FOCUS_ARTIFICIAL_INTELLIGENCE);
		_questionPanel.comboBox.addItem(Constant.FOCUS_COMPUTER_GRAPHICS_GAMES);
		_questionPanel.comboBox.addItem(Constant.FOCUS_COMPUTER_NETWORKS);
		_questionPanel.comboBox.addItem(Constant.FOCUS_COMPUTER_SECURITY);
		_questionPanel.comboBox.addItem(Constant.FOCUS_DATABASE_SYSTEMS);
		_questionPanel.comboBox.addItem(Constant.FOCUS_INFORMATION_RETRIEVAL);
		_questionPanel.comboBox.addItem(Constant.FOCUS_INTERACTIVE_MEDIA);
		_questionPanel.comboBox.addItem(Constant.FOCUS_PARALLEL_COMPUTING);
		_questionPanel.comboBox.addItem(Constant.FOCUS_PROGRAMMING_LANGUAGES);
		_questionPanel.comboBox.addItem(Constant.FOCUS_SOFTWARE_ENGINEERING);
		_questionPanel.comboBox.addItem(Constant.FOCUS_VISUAL_COMPUTING);
	}
	
	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, 
				message, 
				"Error", 
				JOptionPane.ERROR_MESSAGE);
	}
	
	private void clearActionListener() {
		for (ActionListener listener: _questionPanel.nextButton.getActionListeners()) {
			_questionPanel.nextButton.removeActionListener(listener);
		}
		
		for (ActionListener listener: _modulePanel.nextButton.getActionListeners()) {
			_modulePanel.nextButton.removeActionListener(listener);
		}
	}
	
	public void setLogic(Logic logic) {
		_logic = logic;
		_modulePanel.initModuleList(_logic.getModuleList());
	}
}
