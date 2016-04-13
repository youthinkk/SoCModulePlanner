package generator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import constant.Constant;
import object.FocusArea;

public class FocusAreaJson {
	private static ObjectMapper _mapper = new ObjectMapper();
	private final File OUTPUT_FILE = new File ("FocusArea.json");
	
	public void generate() {
		HashMap<String, FocusArea> focusAreaList = new HashMap<String, FocusArea>();
		FocusArea focusArea;
		
		focusArea = new FocusArea(Constant.FOCUS_ALGORITHMS_THEORY, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.AlgorithmTheoryPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.AlgorithmTheoryElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_ARTIFICIAL_INTELLIGENCE, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.ArtificialIntelligencePrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.ArtificialIntelligenceElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_ALGORITHMS_THEORY, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.AlgorithmTheoryPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.AlgorithmTheoryElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_COMPUTER_GRAPHICS_GAMES, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.ComputerGraphicGamesPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.ComputerGraphicGamesElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_ALGORITHMS_THEORY, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.AlgorithmTheoryPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.AlgorithmTheoryElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_COMPUTER_NETWORKS, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.NetworkPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.NetworkElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_NETWORKING_DISTRIBUTED_SYSTEMS, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.NetworkPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.NetworkElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_COMPUTER_SECURITY, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.ComputerSecurityPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.ComputerSecurityElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_DATABASE_SYSTEMS, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.DatabaseSystemPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.DatabaseSystemElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_INFORMATION_RETRIEVAL, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.InformationRetrievalPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.InformationRetrievalElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_MULTIMEDIA_INFORMATION_RETRIEVAL, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.InformationRetrievalPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.InformationRetrievalElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_INTERACTIVE_MEDIA, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.InteractiveMediaPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.InteractiveMediaElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_PARALLEL_COMPUTING, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.ParallelComputingPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.ParallelComputingElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_PROGRAMMING_LANGUAGES, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.ProgrammingLanguagesPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.ProgrammingLanguagesElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_SOFTWARE_ENGINEERING, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.SoftwareEngineeringPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.SoftwareEngineeringElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		focusArea = new FocusArea(Constant.FOCUS_VISUAL_COMPUTING, 
				new ArrayList<String>(Arrays.asList(FocusAreaData.VisualComputingPrimaries)), 
				new ArrayList<String>(Arrays.asList(FocusAreaData.VisualComputingElectives)));
		focusAreaList.put(focusArea.getName(), focusArea);
		
		writeFile(focusAreaList);
	}

	private void writeFile(HashMap<String, FocusArea> moduleList) {
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
			HashMap<String, FocusArea> moduleList = new HashMap<String, FocusArea>();
			
			// write empty module list
			_mapper.writeValue(OUTPUT_FILE, moduleList);
		} catch (Exception e) {
			// do nothing
		}
	}
	
	public static void main(String[] args) throws Exception {
		FocusAreaJson json = new FocusAreaJson();
		json.generate();
	}
}
