package logic;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassGraph {
	HashMap<String,NodeClass> graph;
	
	public ClassGraph(){
		graph=new HashMap<>();
	}
	
	public NodeClass searchNodeClass(String className) {
		return graph.get(className);
	}
	
	public void addTypeDeclaration(TypeDeclaration td) {
		
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
			graph.put(td.getName().toString(), new NodeClass(superClass,td));
		}
		else {
			if(res.isShallow()) {
				String superName=td.getSuperclassType().toString();
				NodeClass superClass=searchNodeClass(superName);
				
				if(superClass==null) {//SuperClass doesn't exists either so we add a shallow version
					superClass=new NodeClass(superName);
					graph.put(superName,superClass);
				}//Else it exists, whether it's shallow or not doesn't matter.
				
				//And we replace it
				graph.put(td.getName().toString(), new NodeClass(superClass,td));
				
			}
			else {
				System.out.println("doublon");
			}
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
