package SourceAnalyser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class MethodInvocationVisitor extends ASTVisitor {
	List<MethodInvocation> methods = new ArrayList<MethodInvocation>();

	
	@Override
	public boolean visit(MethodInvocation node) {
		methods.add(node);
		return true;
	}

	public List<MethodInvocation> getMethods() {
		return methods;
	}

}
