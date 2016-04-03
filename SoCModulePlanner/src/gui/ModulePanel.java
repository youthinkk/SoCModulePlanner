package gui;

import javax.swing.JPanel;

import java.util.Set;
import java.util.TreeMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import object.KeyValue;
import object.ModuleInfo;

import javax.swing.JScrollPane;

public class ModulePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public JLabel availableLabel = new JLabel("Null");
	public JLabel queryLabel = new JLabel("Null");
	public JLabel selectedLabel = new JLabel("Null");
	
	private JButton _addButton = new JButton("Add >>");
	private JButton _removeButton = new JButton("<< Remove");
	public JButton nextButton = new JButton("Next >>");
	
	private DefaultListModel<KeyValue> availableModel = new DefaultListModel<KeyValue>();
	public JList<KeyValue> availableList = new JList<KeyValue>(availableModel);
	private JScrollPane availablePane = new JScrollPane(availableList);
	
	private DefaultListModel<KeyValue> selectedModel = new DefaultListModel<KeyValue>();
	public JList<KeyValue> selectedList = new JList<KeyValue>(selectedModel);
	private JScrollPane selectedPane = new JScrollPane(selectedList);
	
	private TreeMap<String, ModuleInfo> _moduleList = new TreeMap<String, ModuleInfo>();

	public ModulePanel() {
		initAddButton();
		initRemoveButton();
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(nextButton, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(queryLabel, GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(availableLabel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE)
										.addComponent(availablePane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addGroup(groupLayout.createSequentialGroup()
											.addGap(6)
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(_addButton, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
												.addComponent(_removeButton, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))
											.addGap(11)
											.addComponent(selectedPane, GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))
										.addGroup(groupLayout.createSequentialGroup()
											.addGap(146)
											.addComponent(selectedLabel, GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)))))))
					.addGap(20))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(17)
					.addComponent(queryLabel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(availableLabel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(selectedLabel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(availablePane, GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(59)
							.addComponent(_addButton)
							.addGap(12)
							.addComponent(_removeButton))
						.addComponent(selectedPane, GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(nextButton)
					.addGap(17))
		);
		setLayout(groupLayout);

	}
	
	public void clearSelectedList() {
		selectedModel.removeAllElements();
	}
	
	public ArrayList<String> getSelectedList() {
		List<KeyValue> list = Collections.list(selectedModel.elements());
		ArrayList<String> modules = new ArrayList<String>();
		
		for (int i = 0; i < list.size(); i++) {
			modules.add(list.get(i).getKey());
		}
		return modules;
	}
	
	/**
	 * Set available modules in the database.
	 * Only initialize once.
	 * @param moduleList
	 */
	public void initModuleList(TreeMap<String, ModuleInfo> moduleList) {
		_moduleList = moduleList;
		setAvailableModel();
	}

	private void setAvailableModel() {
		Set<Entry<String, ModuleInfo>> entries = _moduleList.entrySet();
		
		for(Entry<String, ModuleInfo> entry: entries) {
			String value = String.format("%-10s %s", entry.getKey(), entry.getValue().getName());
			KeyValue keyValue = new KeyValue(entry.getKey(), value);
			availableModel.addElement(keyValue);
		}
	}
	
	private void initAddButton() {
		_addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<KeyValue> selectedItems = availableList.getSelectedValuesList();
				
				for (int i = 0; i < selectedItems.size(); i++) {
					KeyValue item = selectedItems.get(i);
					selectedModel.addElement(item);
					availableModel.removeElement(item);
				}
				
				sortListModel(selectedModel);
			}
			
		});
	}
	
	private void initRemoveButton() {
		_removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<KeyValue> selectedItems = selectedList.getSelectedValuesList();
				
				for (int i = 0; i < selectedItems.size(); i++) {
					KeyValue item = selectedItems.get(i);
					availableModel.addElement(item);
					selectedModel.removeElement(item);
				}
				
				sortListModel(availableModel);
			}
			
		});
	}
	
	private void sortListModel(DefaultListModel<KeyValue> model) {
		List<KeyValue> list = Collections.list(model.elements());
		Collections.sort(list, new Comparator<KeyValue>() {

			@Override
			public int compare(KeyValue o1, KeyValue o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
			
		});
		
		model.removeAllElements();
		
		for (int i = 0; i < list.size(); i++) {
			model.addElement(list.get(i));
		}
	}
}
