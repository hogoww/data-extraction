package logic;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

public class MethodInvocationVisitor extends ASTVisitor {
	private ArrayList<MethodInvocation> methods = new ArrayList<>();
	private boolean callSuper=false;

	@Override
	public boolean visit(MethodInvocation node) {
		methods.add(node);
		return true;
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node) {
		callSuper=true;
		return true;
	}
	
	/*
	@Override
	public boolean visit(ConstructorInvocation node) {
		methods.add(node);
		return true;
	}
	
	@Override
	public boolean visit(SuperConstructorInvocation node) {
		callSuper=true;
		return true;
	}
	
	public List<ASTNode> getMethods() {
		return methods;
	}
	
	public boolean getCallSuper() {
		return callSuper;
	}
	*/
	public ArrayList<MethodInvocation> getMethods() {
		return methods;
	}

	public boolean isCallSuper() {
		return callSuper;
	}

	
}
