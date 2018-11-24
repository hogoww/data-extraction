package logic;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;


public class MethodInvocationVisitor extends ASTVisitor {
	private boolean callSuper=false;
	private ArrayList<MethodCaller> calledMethod = new ArrayList<>();
	private CallGraphVisitor packages;
	
	public MethodInvocationVisitor(CallGraphVisitor packs) {
		this.packages=packs;
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		IMethodBinding imb=node.resolveMethodBinding();
		if(imb!=null) {
			calledMethod.addAll(
					packages.lookUpMethodInClass(imb.getDeclaringClass().getName().toString(),
										 imb.getMethodDeclaration().getName().toString(),
										 null)
								);
			
			/*System.out.println(imb.getMethodDeclaration().toString()
					+" | "
					+imb.getDeclaringClass().getName().toString()//+" "+(n==null?"null":n.isClass()));
					+node.isResolvedTypeInferredFromExpectedType()
					);*/
		}
		else {
			System.out.println(node.toString()+" "+imb);
		}

		return true;
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node) {
		System.out.println("3 "+node.toString());
		callSuper=true;
		return true;
	}
	
	
	@Override
	public boolean visit(ConstructorInvocation node) {
		System.out.println("2 "+node.toString());
		return true;
	}
	
	
	@Override
	public boolean visit(SuperConstructorInvocation node) {
		System.out.println("4 "+node.toString());
		callSuper=true;
		return true;
	}
	
	public ArrayList<MethodCaller> getMethods() {
		return this.calledMethod;
	}

	public boolean isCallSuper() {
		return callSuper;
	}
}
