package logic;

import java.util.ArrayList;
import java.lang.Object;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassGraph {
	HashMap<String,NodeClass> graph;
	
	public HashMap<String, NodeClass> getGraph() {
		return graph;
	}

	public ClassGraph(){
		graph=new HashMap<>();
	}
	
	public NodeClass searchNodeClass(String className) {
		return graph.get(className);
	}
	
	
	public void addTypeDeclaration(TypeDeclaration td,ArrayList<MethodCaller> meths) {
		
		NodeClass res=searchNodeClass(td.getName().toString());
		if(res==null) {//class doesn't exists yet.
			String superName;
			if(td.getSuperclassType()==null) {
				superName="Object";
			}	
			else {
				superName=td.getSuperclassType().toString();
			}	
			
			NodeClass superClass=searchNodeClass(superName);
			
			if(superClass==null) {//SuperClass doesn't exists either so we add a shallow version
				superClass=new NodeClass(superName);
				graph.put(superName,superClass);
			}//Else it exists, whether it's shallow or not doesn't matter.
			
			NodeClass newSon=new NodeClass(superClass,td,meths);
			superClass.addSon(newSon);
			graph.put(td.getName().toString(), new NodeClass(superClass,td,meths));
		}
		else {
			if(res.isShallow()) {
				String superName=td.getSuperclassType().toString();
				NodeClass superClass=searchNodeClass(superName);
				
				if(superClass==null) {//SuperClass doesn't exists either so we add a shallow version
					superClass=new NodeClass(superName);
					graph.put(superName,superClass);
				}//Else it exists, whether it's shallow or not doesn't matter.
				
				//we then update the shallow class to become complete
				res.GotMyID(td,superClass,meths);
				superClass.addSon(res);
			}
			else {
				System.out.println("doublon");
			}
		}
	}
	
	public NodeClass getClassByName(String className) {
		return this.graph.get(className);
	}
	
	public void resolveMethodsLinks(CallGraphVisitor packs) {
		for(NodeClass nc : this.graph.values()) {
			nc.resolveMethodsLinks(packs);
		}
	}
	
		
	
	public String toString() {
		String acc="";
		for(NodeClass nc: graph.values()){
			acc+=nc.toString();
			acc+='\n';
		}
		return acc;
	}
}
