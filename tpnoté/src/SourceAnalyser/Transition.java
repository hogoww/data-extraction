package SourceAnalyser;


public class Transition {
	
	private String callee;
	private String caller;
	
	public Transition(String callee, String caller) {
		super();
		this.callee = callee;
		this.caller = caller;
	}

	public String getCallee() {
		return this.callee;
	}

	public String getCaller() {
		return this.caller;
	}
	
	
	public boolean equals(Transition t){
		if(t.callee.equals(this.callee) && t.caller.equals(this.caller)){
			return true;
		}
		return false;
	}

}
