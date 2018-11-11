package logic;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class CallGraphVisitor extends ASTVisitor {
	private HashMap<String,ClassGraph> packs=new HashMap<>();
	private ClassGraph currentPackage;
	
	public CallGraphVisitor() {
		// TODO Auto-generated constructor stub
		
	}

	public CallGraphVisitor(boolean visitDocTags) {
		super(visitDocTags);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		currentPackage.addTypeDeclaration(node);
		return true;
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
