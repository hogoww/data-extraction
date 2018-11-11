package logic;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.TypeDeclaration;



//Minimal tree to work with for this task.

public class NodeClass{
	NodeClass daddy;
	ArrayList<NodeClass> sons;
	String name;
	TypeDeclaration data;
	
	public NodeClass getDaddy() {
		return daddy;
	}

	public TypeDeclaration getData() {
		return data;
	}
	
	public NodeClass(String myName){//For classes we might not know (yet or at all)
		this.daddy=null;
		this.sons=new ArrayList<>();
		this.data=null;
		this.name=myName;
	}
	
	public NodeClass(NodeClass daddy, TypeDeclaration data){
		this.daddy=daddy;
		this.sons=new ArrayList<>();
		this.name=data.getName().toString();
		this.data=data;
	}
	
	public boolean isRoot() {
		return ((data!=null) && (daddy==null));
	}
	
	public void addSon(NodeClass s) {
		s.IAmYourDaddy(this);
		sons.add(s);
	}
	
	public void IAmYourDaddy(NodeClass d) {
		daddy=d;
	}
	
	public void GotMyID(TypeDeclaration ID,NodeClass superType) {
		this.daddy=superType;
		this.data=ID;
	}
	
	public boolean isShallow() {
		return data==null;
	}
	
	@Override
	public String toString() {
		if(this.isShallow()) {
			return this.name+" is a shallow class.";
		}
		else{
			return this.name+" is a parsed class, with "+this.daddy.getName()+" as a daddy.";
		}
	}
	
	public String getName() {
		return this.name;
	}
		
}
