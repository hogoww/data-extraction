package logic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

public class MethodInvocationVisitor extends ASTVisitor {
	List<ASTNode> methods = new ArrayList<>();

	
	@Override
	public boolean visit(MethodInvocation node) {
		methods.add(node);
		return true;
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node) {
		methods.add(node);
		return true;
	}
	
	@Override
	public boolean visit(ConstructorInvocation node) {
		methods.add(node);
		return true;
	}
	
	@Override
	public boolean visit(SuperConstructorInvocation node) {
		methods.add(node);
		return true;
	}
	
	public List<ASTNode> getMethods() {
		return methods;
	}

}
