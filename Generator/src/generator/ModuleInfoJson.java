package generator;

import java.io.File;
import java.util.Scanner;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import object.ModuleInfo;

public class ModuleInfoJson {
	private static ObjectMapper _mapper = new ObjectMapper();
	private final int TOTAL_NUMBER_OF_TYPE = 7;
	private final File MODULE_INFO_FILE = new File ("ModuleInfo.json");
	
	public void generate(File file) throws Exception {
		TreeMap<String, ModuleInfo> list = new TreeMap<String, ModuleInfo>();
		Scanner scanner = new Scanner(file);
		int count = 0;
		
		String code = "";
		String name = "";
		String department = "";
		String type = "";
		int credit = 0;
		int level = 1000;
		
		while (scanner.hasNext()) {
			String input = scanner.nextLine();
			int inputType = count%TOTAL_NUMBER_OF_TYPE;
			
			switch (inputType) {
				case 0:
					code = input;
					break;
				case 1:
					try {
						level = Integer.parseInt(input);
					} catch (Exception e) {
						// do nothing
					}
					break;
				case 2:
					name = input;
					break;
				case 3:
					department = input;
					break;
				case 4:
					try {
						credit = Integer.parseInt(input);
					} catch (Exception e) {
						// do nothing
					}
					break;
				case 5:
					type = input;
					break;
				case 6:
					ModuleInfo module = new ModuleInfo(code, name, department, type, level, credit);
					list.put(code, module);
					
					code = "";
					name = "";
					department = "";
					type = "";
					credit = 0;
					level = 1000;
					break;
			}
			
			count++;
		}
		scanner.close();
		writeFile(list);
	}
	
	private void writeFile(TreeMap<String, ModuleInfo> moduleList) {
		try {
			if (!MODULE_INFO_FILE.exists()) {
				createFile();
			}
			
			_mapper.writerWithDefaultPrettyPrinter().writeValue(MODULE_INFO_FILE, moduleList);
		} catch (Exception e) {
			// do nothing
		}
	}
	
	private void createFile() {
		try {
			TreeMap<String, ModuleInfo> moduleList = new TreeMap<String, ModuleInfo>();
			
			// write empty module list
			_mapper.writeValue(MODULE_INFO_FILE, moduleList);
		} catch (Exception e) {
			// do nothing
		}
	}
	
	public static void main(String[] args) throws Exception {
		ModuleInfoJson json = new ModuleInfoJson();
		json.generate(new File(args[0]));
	}
}
