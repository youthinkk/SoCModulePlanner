package generator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ModulePrereqJson {
	private static ObjectMapper _mapper = new ObjectMapper();
	private final File OUTPUT_FILE = new File ("ModulePrereq.json");
	
	public void generate(File file) throws Exception {
		HashMap<String, ArrayList<ArrayList<String>>> list = new HashMap<String, ArrayList<ArrayList<String>>>();
		Scanner scanner = new Scanner(file);
		
		String code = new String();
		ArrayList<ArrayList<String>> prereqList = new ArrayList<ArrayList<String>>();
		
		while (scanner.hasNextLine()) {
			String input = scanner.nextLine();
			
			if (input.trim().isEmpty()) {
				if (code.isEmpty()) continue;
				
				list.put(code, prereqList);
				code = new String();
				prereqList = new ArrayList<ArrayList<String>>();
				
				continue;
			}
			
			if (code.trim().isEmpty()) {
				code = input;
			} else {
				String[] split = input.split("\\s+");
				
				if (split[0].equals("NIL")) {
					code = new String();
					prereqList = new ArrayList<ArrayList<String>>();
				} else {
					ArrayList<String> prereq = new ArrayList<String>(Arrays.asList(split));
					Collections.sort(prereq);
					prereqList.add(prereq);
				}
			}			
		}
		
		scanner.close();
		writeFile(list);
	}
	
	private void writeFile(HashMap<String, ArrayList<ArrayList<String>>> moduleList) {
		try {
			if (!OUTPUT_FILE.exists()) {
				createFile();
			}
			
			_mapper.writerWithDefaultPrettyPrinter().writeValue(OUTPUT_FILE, moduleList);
		} catch (Exception e) {
			// do nothing
		}
	}
	
	private void createFile() {
		try {
			HashMap<String, ArrayList<ArrayList<String>>> list = new HashMap<String, ArrayList<ArrayList<String>>>();
			
			// write empty module list
			_mapper.writeValue(OUTPUT_FILE, list);
		} catch (Exception e) {
			// do nothing
		}
	}
	
	public static void main(String[] args) throws Exception {
		ModulePrereqJson json = new ModulePrereqJson();
		json.generate(new File(args[0]));
	}
}
