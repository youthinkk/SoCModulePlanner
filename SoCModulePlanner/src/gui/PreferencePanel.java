package gui;

import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;

import java.util.Map.Entry;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import main.Logic;
import object.ModuleInfo;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class PreferencePanel extends JPanel {
	private Logic _logic;

	/**
	 * Create the panel.
	 */
	public PreferencePanel(Logic logic) {
		_logic = logic;
		
		JLabel majorLabel = new JLabel("Major: ");
		JLabel matriculationLabel = new JLabel("Matriculation Year: ");
		JLabel preUniLabel = new JLabel("Pre-University: ");
		JLabel availableModuleLabel = new JLabel("Available Modules: ");
		JLabel takeModuleLabel = new JLabel("Modules Taken/Exempted: ");
		
		JComboBox<String> majorCombo = new JComboBox<String>();
		JComboBox<String> matriculationCombo = new JComboBox<String>();
		JComboBox<String> preUniCombo = new JComboBox<String>();
		
		DefaultListModel<String> availableModuleModel = new DefaultListModel<String>();
		JList<String> availableModuleList = new JList<String>(availableModuleModel);
		JScrollPane availableModuleScrollerPane = new JScrollPane(availableModuleList);
		
		DefaultListModel<String> moduleTakenModel = new DefaultListModel<String>();
		JList<String> moduleTakenList = new JList<String>(moduleTakenModel);
		JScrollPane takeModuleScrollerPane = new JScrollPane(moduleTakenList);
		
		JButton addButton = new JButton("Add >>");
		JButton removeButton = new JButton("<< Remove");
		JButton plannerButton = new JButton("Get Planner");
		
		this.initMajorCombo(majorCombo);
		this.initMatriculationCombo(matriculationCombo);
		this.initPreUniCombo(preUniCombo);
		this.initAvailableModuleModel(availableModuleModel);
		this.initPlannerButton(plannerButton, majorCombo, matriculationCombo, preUniCombo);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(19)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(availableModuleLabel, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
							.addGap(639))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(preUniLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(majorLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(matriculationLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(matriculationCombo, 0, 604, Short.MAX_VALUE)
										.addComponent(majorCombo, 0, 604, Short.MAX_VALUE)
										.addComponent(preUniCombo, 0, 604, Short.MAX_VALUE)))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(availableModuleScrollerPane, GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(removeButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(addButton, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
									.addGap(11)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(takeModuleLabel, GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
										.addComponent(takeModuleScrollerPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
										.addComponent(plannerButton, Alignment.TRAILING))))
							.addGap(29))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(6)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(majorLabel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(majorCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(matriculationLabel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(matriculationCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(preUniLabel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(preUniCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(101)
							.addComponent(addButton)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(removeButton))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(availableModuleLabel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(takeModuleLabel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(takeModuleScrollerPane, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
								.addComponent(availableModuleScrollerPane, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(plannerButton)))
					.addGap(7))
		);
		setLayout(groupLayout);

	}
	
	private void initMajorCombo(JComboBox<String> combo) {
		combo.addItem("Computer Science");
		combo.addItem("Information System");
		combo.addItem("Computer Engineering");
		combo.addItem("Business Analytics");
		combo.addItem("Information Security");
		combo.setSelectedIndex(-1);
	}
	
	private void initMatriculationCombo(JComboBox<String> combo) {
		combo.addItem("AY2015/2016");
		combo.addItem("AY2014/2015");
		combo.addItem("AY2013/2014");
		combo.addItem("AY2012/2013");
		combo.addItem("AY2011/2012");
		combo.addItem("AY2010/2011");
		combo.setSelectedIndex(-1);
	}
	
	private void initPreUniCombo(JComboBox<String> combo) {
		combo.addItem("Junior College");
		combo.addItem("Polytechnic");
		combo.addItem("Others");
		combo.setSelectedIndex(-1);
	}
	
	private void initAvailableModuleModel(DefaultListModel<String> model) {
		Set<Entry<String, ModuleInfo>> entries = _logic.getModuleList().entrySet();
		
		for(Entry<String, ModuleInfo> entry: entries) {
			String moduleStr = String.format("%-10s %s", entry.getKey(), entry.getValue().getName());
			model.addElement(moduleStr);
		}
	}
	
	private void initPlannerButton(JButton button, JComboBox<String> majorCombo, JComboBox<String> matriculationCombo, JComboBox<String> preUniCombo) {
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "The following component(s) is/are not selected: \n";
				boolean isValid = true;
				
				if (majorCombo.getSelectedIndex() == -1) {
					message += "- Major \n";
					isValid = false;
				}
				
				if (matriculationCombo.getSelectedIndex() == -1) {
					message += "- Matriculation Year \n";
					isValid = false;
				}
				
				if (preUniCombo.getSelectedIndex() == -1) {
					message += "- Pre-University \n";
					isValid = false;
				}
				
				if (!isValid) {
					JOptionPane.showMessageDialog(null, 
							message, 
							"Error", 
							JOptionPane.ERROR_MESSAGE);
				} else {
					// Execute CLIPS
				}
			}
		});
	}
}
