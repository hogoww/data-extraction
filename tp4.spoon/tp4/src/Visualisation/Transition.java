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

	@Override
	public boolean equals(Object t){
		if(t instanceof Transition)
			if(((Transition)t).callee.equals(this.callee) && ((Transition)t).caller.equals(this.caller) && ((Transition)t).label==this.label){

				return true;
			}
		return false;
	}

}
