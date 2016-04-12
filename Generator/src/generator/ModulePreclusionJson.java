package generator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ModulePreclusionJson {
	private static ObjectMapper _mapper = new ObjectMapper();
	private final File MODULE_INFO_FILE = new File ("ModulePreclusion.json");
	
	public void generate(File file) throws Exception {
		HashMap<String, ArrayList<String>> list = new HashMap<String, ArrayList<String>>();
		Scanner scanner = new Scanner(file);
		
		String code = new String();
		ArrayList<String> preclusion = new ArrayList<String>();
		
		while (scanner.hasNextLine()) {
			String input = scanner.nextLine();
			
			if (input.trim().isEmpty()) {
				if (code.isEmpty()) continue;
				
				Collections.sort(preclusion);
				list.put(code, preclusion);
				code = new String();
				preclusion = new ArrayList<String>();
				
				continue;
			}
			
			if (code.trim().isEmpty()) {
				code = input;
			} else {
				String[] split = input.split("\\s+");
				
				if (split[0].equals("NIL")) {
					code = new String();
					preclusion = new ArrayList<String>();
				} else {
					preclusion.add(split[0]);
				}
			}
		}
		
		scanner.close();
		writeFile(list);
	}
	
	private void writeFile(HashMap<String, ArrayList<String>> moduleList) {
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
			HashMap<String, ArrayList<String>> list = new HashMap<String, ArrayList<String>>();
			
			// write empty module list
			_mapper.writeValue(MODULE_INFO_FILE, list);
		} catch (Exception e) {
			// do nothing
		}
	}
	
	public static void main(String[] args) throws Exception {
		ModulePreclusionJson json = new ModulePreclusionJson();
		json.generate(new File(args[0]));
	}
}
