package SourceAnalyser;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodLineNumberType {
	private MethodDeclaration m;
	private int lineNb;
	
	public MethodLineNumberType(MethodDeclaration m,int nb) {
		this.m=m;
		this.lineNb=nb;
	}

	public MethodDeclaration getM() {
		return m;
	}

	public int getLineNb() {
		return lineNb;
	}
	
}
