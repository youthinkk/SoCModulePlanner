package main;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.TreeMap;

import common.KeyValue;
import common.ModuleInfo;
import net.sf.clipsrules.jni.Environment;
import net.sf.clipsrules.jni.FactAddressValue;
import net.sf.clipsrules.jni.MultifieldValue;

public class ClipsBuilder {
	private Environment _clips;
	private final String SINGAPORE_STUDIES = "SS";
	private final String UNRESTRICTED_ELECTIVES = "UELECTIVE";
	
	public ClipsBuilder() {
		_clips = new Environment();
	}
	
	public void setCondition(String condition) {
		_clips.clear();
		_clips.loadFromString(condition);
		
		System.out.println("load condition: ");
		System.out.println(condition);
	}
	
	public void reset() {
		_clips.reset();
	}
	
	public void run() {
		_clips.run();
	}
	
	public void execute(String command) {
		System.out.println("CLIPS>> " + command);
		_clips.eval(command);
	}
	
	public void watch() {
		_clips.watch("facts");
		_clips.watch("agenda");
	}
	
	public void assertModule(String code, int level, int credits, 
			String corequisite, ArrayList<String> prerequisites, boolean[] offer) {
		String prereqStr = String.join(" ", prerequisites);
		String offerStr = "";
		
		for (int i = 0; i < offer.length; i++) {
			boolean isOffer = offer[i];
			
			if (isOffer) {
				offerStr += (i + 1);
			}
			
			if (i != offer.length-1) {
				offerStr += " ";
			}
		}
		
		if (code.equals("CP3200") || code.equals("CP3202")) {
			offerStr = "3 4";
		}
		corequisite = getCorequisite(code);
		
		String command = String.format("(assert (module (code %s) (level %d) (credits %d) (coreq %s) (prereq %s) (offer %s)))", 
				code, level, credits, corequisite, prereqStr, offerStr);
		execute(command);
	}
	
	public void assertManagement(int startSemester, int numberOfModulesMustPlan, int accumulativeCredits) {
		String command = String.format("(assert (management (current-semester %d) (must-plan-number-module %d) (accumulative-credits %d)))",
				startSemester, numberOfModulesMustPlan, accumulativeCredits);
		execute(command);
	}
	
	public void showFacts() {
		execute("(facts)");
	}
	
	public TreeMap<Integer, ArrayList<KeyValue>> getPlannedModules(TreeMap<String, ModuleInfo> moduleInfo) {
		TreeMap<Integer, ArrayList<KeyValue>> planner = new TreeMap<Integer, ArrayList<KeyValue>>();
		
		MultifieldValue facts = (MultifieldValue) _clips.eval("(find-all-facts ((?f module)) TRUE)");
		
		@SuppressWarnings("unchecked")
		ListIterator<FactAddressValue> iterator = facts.multifieldValue().listIterator();
		//int size = facts.size();
		String code;
		int semester;
		
		while (iterator.hasNext()) {
			FactAddressValue address = iterator.next();
			
			try {
				if (address.getFactSlot("status").toString().equals("planned")) {
					KeyValue keyValue;
					
					code = address.getFactSlot("code").toString();
					semester = Integer.parseInt(address.getFactSlot("semester").toString());
					
					if (moduleInfo.containsKey(code)) {
						keyValue = new KeyValue(code, moduleInfo.get(code).getName());
					} else {
						keyValue = new KeyValue(getProperName(code), "");
					}
					
					ArrayList<KeyValue> list;
					if (planner.containsKey(semester)) {
						list = planner.get(semester);
					} else {
						list = new ArrayList<KeyValue>();
					}
					list.add(keyValue);
					planner.put(semester, list);
				}
			} catch (Exception e) {
				
			}
		}
		return planner;
	}
	
	private String getProperName(String code) {
		if (code.equals(SINGAPORE_STUDIES)) {
			return "Singapore Studies";
		} else if (code.contains(UNRESTRICTED_ELECTIVES)) {
			return "Unrestricted Elective";
		}
		return code;
	}
	
	private String getCorequisite(String module) {
		switch (module) {
			case "CS3201":
				return "CS3202";
			case "CS3202":
				return "CS3201";
			case "CS3281":
				return "CS3282";
			case "CS3282":
				return "CS3281";
			case "CS2101":
				return "CS2103T";
			case "CS2103T":
				return "CS2101";
			default:
				return "";
		}
	}
}
