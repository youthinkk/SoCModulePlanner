package main;

import net.sf.clipsrules.jni.Environment;

public class ClipsBuilder {
	private Environment _clips;
	
	public ClipsBuilder() {
		_clips = new Environment();
	}
	
	public void setCondition(String condition) {
		_clips.clear();
		_clips.loadFromString(condition);
	}
	
	public void reset() {
		_clips.reset();
	}
	
	public void run() {
		_clips.run();
	}
	
	public void execute(String command) {
		_clips.eval(command);
	}
}
