package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import object.ModuleInfo;
import storage.ModuleInfoStorage;

public class Parser {
	private static final String SPLIT_DELIMITER = "\\s+";
	private static final String JOIN_DELIMITER = " ";
	private static TreeMap<String, ModuleInfo> _moduleList;
	
	static {
		_moduleList = ModuleInfoStorage.readFile();
	}
	
	/*public static void SaveModuleInfo(String input) {
		String[] splitInput = input.split(SPLIT_DELIMITER);
		ArrayList<String> words = new ArrayList<>(Arrays.asList(splitInput));
		
		try {
			String code = words.get(0).toUpperCase();
			String name = String.join(JOIN_DELIMITER, words.subList(2, words.size()));
			int modularCredits = 0;

			modularCredits = Integer.parseInt(words.get(1));
			
			ModuleInfo moduleInfo = new ModuleInfo(code, name, modularCredits);
			_moduleList.put(code, moduleInfo);

			ModuleInfoStorage.writeFile(_moduleList);
		} catch (Exception e) {
			// do nothing
			System.out.println(e.getMessage());
		} 
	}*/
	
	/*public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			String input = scanner.nextLine();
			SaveModuleInfo(input);
			
			System.out.println("Successful");
			System.out.println();
		}
	}*/
}
