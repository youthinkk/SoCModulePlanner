package generator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import constant.Constant;

public class GradRequirementJson {
	private static ObjectMapper _mapper = new ObjectMapper();
	private final File MODULE_INFO_FILE = new File ("GradRequirement.json");
	
	private void generate() {
		HashMap<String, ArrayList<ArrayList<ArrayList<String>>>> list = 
				new HashMap<String, ArrayList<ArrayList<ArrayList<String>>>>();
		ArrayList<ArrayList<ArrayList<String>>> CSRequirements = getComputerScienceRequirements();
		
		list.put(Constant.MAJOR_COMPUTER_SCIENCE, CSRequirements);
		
		writeFile(list);
	}
	
	private ArrayList<ArrayList<ArrayList<String>>> getComputerScienceRequirements() {
		ArrayList<ArrayList<ArrayList<String>>> firstLayer = new ArrayList<ArrayList<ArrayList<String>>>();
		ArrayList<ArrayList<String>> secondLayer = new ArrayList<ArrayList<String>>();
		
		// CS Foundation
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"CS1010"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"CS1020" , "CS2010"}));
		secondLayer.add(getModules(new String[] {"CS2020"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"CS1231"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"CS2100"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"CS2103T"}));
		secondLayer.add(getModules(new String[] {"CS2103"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"CS2105"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"CS2106"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"CS3230"}));
		firstLayer.add(secondLayer);
			
		// CS Depth & Breadth
		/*secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_PRIMARIES1}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_PRIMARIES2}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_PRIMARIES3}));
		firstLayer.add(secondLayer);*/
		
		/*secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_ELECTIVES1}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_ELECTIVES2}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_ELECTIVES3}));
		firstLayer.add(secondLayer);*/
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"CS3201" , "CS3202"}));
		secondLayer.add(getModules(new String[] {"CS3216" , "CS3217"}));
		secondLayer.add(getModules(new String[] {"CS3281" , "CS3282"}));
		secondLayer.add(getModules(new String[] {"CS3283" , "CS3284"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"CP3200" , "CP3202"}));
		secondLayer.add(getModules(new String[] {"CP3880"}));
		secondLayer.add(getModules(new String[] {"CP4101"}));
		secondLayer.add(getModules(new String[] {"CS3200", "CS3101A"}));
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_INDUSTRIAL_NOC}));
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_INDUSTRIAL_ILEAD}));
		firstLayer.add(secondLayer);
		
		// IT Professionalism
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"IS1103"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"CS2101"}));
		firstLayer.add(secondLayer);
		
		// Math
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"MA1301"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"MA1521"}));
		secondLayer.add(getModules(new String[] {"MA1102R"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"MA1101R"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"ST2334"}));
		secondLayer.add(getModules(new String[] {"ST2131", "ST2132"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"PC1221"}));
		secondLayer.add(getModules(new String[] {"PC1222"}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_SCIENCE}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_SCIENCE}));
		firstLayer.add(secondLayer);
		
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_SCIENCE}));
		firstLayer.add(secondLayer);
		
		// University Requirements
		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_GEM}));
		firstLayer.add(secondLayer);

		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {"GEK1901"}));
		firstLayer.add(secondLayer);

		secondLayer = new ArrayList<ArrayList<String>>();
		secondLayer.add(getModules(new String[] {Constant.REQUIREMENT_SS}));
		firstLayer.add(secondLayer);
		
		return firstLayer;
	}
	
	private ArrayList<String> getModules(String[] modules) {
		return new ArrayList<String>(Arrays.asList(modules));
	}
	
	private void writeFile(HashMap<String, ArrayList<ArrayList<ArrayList<String>>>> list) {
		try {
			if (!MODULE_INFO_FILE.exists()) {
				createFile();
			}
			
			_mapper.writerWithDefaultPrettyPrinter().writeValue(MODULE_INFO_FILE, list);
		} catch (Exception e) {
			// do nothing
		}
	}
	
	private void createFile() {
		try {
			HashMap<String, ArrayList<ArrayList<ArrayList<String>>>> list = 
					new HashMap<String, ArrayList<ArrayList<ArrayList<String>>>>();
			
			// write empty module list
			_mapper.writeValue(MODULE_INFO_FILE, list);
		} catch (Exception e) {
			// do nothing
		}
	}
	
	public static void main(String[] args) throws Exception {
		GradRequirementJson json = new GradRequirementJson();
		json.generate();
	}
}
