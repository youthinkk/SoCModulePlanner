package main;

import java.util.ArrayList;

import net.sf.clipsrules.jni.Environment;

public class ClipsBuilder {
	private Environment _clips;
	
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
