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
	private ArrayList<MethodCaller> currentMethods;
	

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
		currentMethods.add(new MethodCaller(node));
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
	
	//If ClassName is null, we just propagate the null pointer exception, caller's responsibility
	public ArrayList<NodeClass> lookUpClass(String ClassName,String Package) {
		ArrayList<NodeClass> res=new ArrayList<>();
		NodeClass found;
		
		if(Package==null) {
			
			//We don't know what packages the class we're looking for is in, so we have to iterate over them
			for(ClassGraph cg: this.packs.values()) {
				found=cg.getClassByName(ClassName);
				if(found!=null) {
					res.add(found);
					break;
				}
			}
		}
		else {//We just return the class from the indicated package. If it doesn't exist, it's the caller's problem.
			found=this.packs.get(Package).getClassByName(ClassName);
			res.add(found);
		}
		
		return res;
	}
	
	public ArrayList<MethodCaller> lookUpMethodInClass(String ClassName,String MethodName,String Package) {
		ArrayList<MethodCaller> res=new ArrayList<>();
		
		for(NodeClass nc : this.lookUpClass(ClassName,null)) {
			res.addAll(nc.lookUpMethod(MethodName));
		}
		
		return res;
	}
	
	public void resolveMethodsLink() {
		for(ClassGraph cg: this.packs.values()) {
			cg.resolveMethodsLinks(this);
		}
	}
	
	@Override
	public String toString() {
		String acc="Package ";
		for(String cg: packs.keySet()){
			acc+=cg+" contains :\n";
			acc+=packs.get(cg).toString();
			acc+='\n';
		}
		return acc;
	}
}
