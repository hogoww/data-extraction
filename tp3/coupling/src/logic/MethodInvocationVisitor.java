package logic;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;


public class MethodInvocationVisitor extends ASTVisitor {
	private HashSet<MethodCaller> calledMethod = new HashSet<>();
	private CallGraphVisitor packages;
	
	public MethodInvocationVisitor(CallGraphVisitor packs) {
		this.packages=packs;
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		IMethodBinding imb=node.resolveMethodBinding();
		tryToResolveMethodBinding(imb,node);

		return true;
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node) {
		IMethodBinding imb=node.resolveMethodBinding();
		tryToResolveMethodBinding(imb,node);
		return true;
	}


	@Override
	public boolean visit(ConstructorInvocation node) {
		IMethodBinding imb=node.resolveConstructorBinding();
		tryToResolveMethodBinding(imb,node);
		return true;
	}	
	
	@Override
	public boolean visit(SuperConstructorInvocation node) {
		IMethodBinding imb=node.resolveConstructorBinding();
		tryToResolveMethodBinding(imb,node);
		return true;
	}

	public void tryToResolveMethodBinding(IMethodBinding imb,ASTNode node) {

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
			System.out.println("pouet"+node.toString()+" "+imb);
		}
	}
	
	public HashSet<MethodCaller> getMethods() {
		return this.calledMethod;
	}

}
