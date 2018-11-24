package logic;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodCaller {
	private MethodDeclaration method;
	private ArrayList<MethodCaller> call;
	
	public MethodCaller(MethodDeclaration m) {
		this.method=m;
		this.call=new ArrayList<>();
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
		call=visitor.getMethods();
		
		System.out.println(this.method.getName().toString());
		for(MethodCaller mc: this.call) {
			System.out.println("1 "+mc.getMethod().getName().toString());
		}
	}
}
