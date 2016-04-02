package gui;

import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@SuppressWarnings("serial")
public class PreferencePanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public PreferencePanel() {
		
		JLabel majorLabel = new JLabel("Major: ");
		JLabel matriculationLabel = new JLabel("Matriculation Year: ");
		JLabel preUniLabel = new JLabel("Pre-University: ");
		
		JComboBox<String> majorCombo = new JComboBox<String>();
		JComboBox<String> matriculationCombo = new JComboBox<String>();
		JComboBox<String> preUniCombo = new JComboBox<String>();
		
		this.initMajorCombo(majorCombo);
		this.initMatriculationCombo(matriculationCombo);
		this.initPreUniCombo(preUniCombo);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(19)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(preUniLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(majorLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(matriculationLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(matriculationCombo, 0, 334, Short.MAX_VALUE)
						.addComponent(majorCombo, 0, 334, Short.MAX_VALUE)
						.addComponent(preUniCombo, 0, 334, Short.MAX_VALUE))
					.addGap(29))
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
					.addGap(272))
		);
		setLayout(groupLayout);

	}
	
	private void initMajorCombo(JComboBox<String> combo) {
		combo.addItem("Computer Science");
		combo.addItem("Information System");
		combo.addItem("Computer Engineering");
		combo.addItem("Business Analytics");
		combo.addItem("Information Security");
	}
	
	private void initMatriculationCombo(JComboBox<String> combo) {
		combo.addItem("AY2010/2011");
		combo.addItem("AY2011/2012");
		combo.addItem("AY2012/2013");
		combo.addItem("AY2013/2014");
		combo.addItem("AY2014/2015");
		combo.addItem("AY2015/2016");
	}
	
	private void initPreUniCombo(JComboBox<String> combo) {
		combo.addItem("Junior College");
		combo.addItem("Polytechnic");
		combo.addItem("Others");
	}
}
