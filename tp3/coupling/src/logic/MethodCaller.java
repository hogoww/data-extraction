package logic;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodCaller {
	private MethodDeclaration method;
	private ArrayList<MethodInvocation> call;
	private boolean superCall;
	
	public MethodCaller(MethodDeclaration m) {
		this.method=m;
		this.call=new ArrayList<>();
	}
	
	private void WhoDoICall() {
		MethodInvocationVisitor visitor=new MethodInvocationVisitor();
		method.accept(visitor);
		call=visitor.getMethods();
		superCall=visitor.isCallSuper();
	}
	
}
