package Visualisation;


public class Transition {
	
	private String callee;
	private String caller;
	private double label;
	
	public Transition(String callee, String caller,double label) {
		super();
		this.callee = callee;
		this.caller = caller;
		this.label = label;
	}

	public String getCallee() {
		return this.callee;
	}

	public String getCaller() {
		return this.caller;
	}
	
	public double getLabel() {
		return this.label;
	}

/*	@Override
	public boolean equals(Object o){
		if(t.callee.equals(this.callee) && t.caller.equals(this.caller) && t.label==this.label){

			return true;
		}
		return false;
	}*/

}
