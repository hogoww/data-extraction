package logic;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class CallGraphVisitor extends ASTVisitor {
	private HashMap<String,ClassGraph> packs=new HashMap<>();
	private ClassGraph currentPackage;

	private ArrayList<MethodDeclaration> currentMethods;
	
	public CallGraphVisitor() {
		// TODO Auto-generated constructor stub
	}

	public CallGraphVisitor(boolean visitDocTags) {
		super(visitDocTags);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		currentMethods=new ArrayList<>();
		return true;
	}
	
	@Override
	public void endVisit(TypeDeclaration node) {
		currentPackage.addTypeDeclaration(node,currentMethods);
	}
	
	public boolean visit(MethodDeclaration node) {
		currentMethods.add(node);
		return false;//No need to keep going. We'll do it in another
	}

	
	@Override
	public boolean visit(PackageDeclaration node) {
		this.currentPackage=packs.get(node.getName().toString());
		if(this.currentPackage==null) {//if we don't find it, we create a new one.
			this.currentPackage=new ClassGraph();
		}
		packs.put(node.getName().toString(),this.currentPackage);
		
		return true;
	}
	
		
	@Override
	public String toString() {
		String acc="";
		for(String cg: packs.keySet()){
			acc+=cg+" contains :\n";
			acc+=packs.get(cg).toString();
			acc+='\n';
		}
		return acc;
	}
}
