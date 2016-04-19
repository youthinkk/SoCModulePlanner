package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import common.KeyValue;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class PlannerPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public PlannerPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0};
		gridBagLayout.rowHeights = new int[]{0};
		gridBagLayout.columnWeights = new double[]{Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{Double.MIN_VALUE};
		setLayout(gridBagLayout);
	}
	
	public void setContent(TreeMap<Integer, ArrayList<KeyValue>> planner) {
		removeAll();
		Set<Entry<Integer, ArrayList<KeyValue>>> entries = planner.entrySet();
		List<JLabel> labelList = new ArrayList<JLabel>();
		List<JScrollPane> scrollList = new ArrayList<JScrollPane>();
		
		for(Entry<Integer, ArrayList<KeyValue>> entry: entries) {
			Integer semester = entry.getKey();
			ArrayList<KeyValue> modules = entry.getValue();
			
			Collections.sort(modules, new Comparator<KeyValue>() {

				@Override
				public int compare(KeyValue o1, KeyValue o2) {
					return o1.getKey().compareTo(o2.getKey());
				}
				
			});
			
			JTextArea textArea = new JTextArea(this.getPreferredSize().width, this.getPreferredSize().height);
			JScrollPane scrollPane = new JScrollPane(textArea);
			JLabel label = new JLabel();
			
			Integer year = semester/4 + 1;
			Integer sem = semester%4 == 0 ? 1: semester%4;
			String semStr = sem == 3 ? "Special Term" : sem.toString();
			String text = "Semester " + year + "-" + semStr + "\n";
			
			for (int i = 0; i < modules.size(); i++) {
				KeyValue module = modules.get(i);
				String display = String.format("%-10s %s", module.getKey(), module.getValue());
				text += display + "\n";
			}
			
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setEnabled(true);
			
			textArea.setEnabled(false);
			textArea.setText(text);
			label.setText(semester.toString());
			
			scrollList.add(scrollPane);
			labelList.add(label);
		}
		
		GridBagConstraints scrollConstraint = new GridBagConstraints();
		
		for (int i = 0; i < scrollList.size(); i++) {
            scrollConstraint.gridx = 0;
            scrollConstraint.gridy = i;
            scrollConstraint.fill = GridBagConstraints.HORIZONTAL;
            scrollConstraint.insets = new Insets(10, 10, 10, 10);

            add(scrollList.get(i), scrollConstraint);
		}
		
		GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridx = 0;
        constraint.gridy = scrollList.size();
        constraint.weighty = 1;
        this.add(new JLabel(), constraint);
	}

}
