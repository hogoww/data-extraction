package testGraphDot;

public class Transition {
	
	String callee;
	String caller;
	
	public Transition(String callee, String caller) {
		super();
		this.callee = callee;
		this.caller = caller;
	}
	
	public String getCallee() {
		return callee;
	}

	public String getCaller() {
		return caller;
	}


}
