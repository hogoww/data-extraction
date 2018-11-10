package SourceAnalyser;

import org.eclipse.jdt.core.dom.SimpleName;

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

	public boolean equals(Transition t){
		if(t.callee.equals(this.callee) && t.caller.equals(this.caller)){
			return true;
		}
		return false;
	}

}
