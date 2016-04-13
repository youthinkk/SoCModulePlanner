package generator;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ModuleHistoryJson {
	private static ObjectMapper _mapper = new ObjectMapper();
	private final File OUTPUT_FILE = new File ("ModuleHistory.json");
	private final int TOTAL_NUMBER_OF_SEMESTER = 4;
	
	public void generate(File file) throws Exception {
		HashMap<String, boolean[]> list = new HashMap<String, boolean[]>();
		Scanner scanner = new Scanner(file);
		
		String code = new String();
		boolean[] offerSemester = new boolean[TOTAL_NUMBER_OF_SEMESTER];
		
		while (scanner.hasNextLine()) {
			String input = scanner.nextLine();
			if (input.trim().isEmpty()) {
				if (code.isEmpty()) continue;
				
				list.put(code, offerSemester);
				code = new String();
				offerSemester = new boolean[TOTAL_NUMBER_OF_SEMESTER];
				
				continue;
			}
			
			if (code.trim().isEmpty()) {
				code = input;
			} else {
				int semester = parseInt(input)-1;
				
				if (semester >= 0) {
					offerSemester[semester] = true;
				}
			}
		}
		
		scanner.close();
		writeFile(list);
	}
	
	private int parseInt(String input) {
		try{
			return Integer.parseInt(input);
		} catch (Exception e) {
			return -1;
		}
	}
	
	private void writeFile(HashMap<String, boolean[]> moduleList) {
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
			HashMap<String, boolean[]> list = new HashMap<String, boolean[]>();
			
			// write empty module list
			_mapper.writeValue(OUTPUT_FILE, list);
		} catch (Exception e) {
			// do nothing
		}
	}
	
	public static void main(String[] args) throws Exception {
		ModuleHistoryJson json = new ModuleHistoryJson();
		json.generate(new File(args[0]));
	}
}
