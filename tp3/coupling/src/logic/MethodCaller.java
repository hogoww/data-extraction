package logic;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodCaller {
	private MethodDeclaration method;
	private HashSet<MethodCaller> calledMethod;
	private NodeClass container;
	

	public MethodCaller(MethodDeclaration m) {
		this.method=m;
		this.calledMethod=new HashSet<>();
	}
	
	/*private void whodoicall() {
		methodinvocationvisitor visitor=new methodinvocationvisitor();
		method.accept(visitor);
		call=visitor.getmethods();
		supercall=visitor.iscallsuper();
	}*/
	
	public MethodDeclaration getMethod() {
		return method;
	}

	
	public void resolveMethodsLinks(CallGraphVisitor packs) {
		MethodInvocationVisitor visitor=new MethodInvocationVisitor(packs);
		method.accept(visitor);
		calledMethod=visitor.getMethods();
		
		System.out.println(this.method.getName().toString());
		for(MethodCaller mc: this.calledMethod) {
			System.out.println("1 "+mc.getMethod().getName().toString());
		}
	}
	
	public NodeClass getContainer() {
		return container;
	}

	public void setContainer(NodeClass container) {
		this.container = container;
	}
	
	public HashSet<MethodCaller> getCalledMethod() {
		return calledMethod;
	}
}
