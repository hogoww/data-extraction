package logic;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;


public class NodeClass{
	NodeClass daddy;
	ArrayList<NodeClass> sons;
	String name;
	TypeDeclaration ClassDecl;
	
	public NodeClass getDaddy() {
		return daddy;
	}

	public TypeDeclaration getData() {
		return ClassDecl;
	}
	
	public NodeClass(String myName){//For classes we might not know (yet or at all)
		this.daddy=null;
		this.sons=new ArrayList<>();
		this.ClassDecl=null;
		this.name=myName;
	}
	
	public NodeClass(NodeClass daddy, TypeDeclaration data){
		this.daddy=daddy;
		this.sons=new ArrayList<>();
		this.name=data.getName().toString();
		this.ClassDecl=data;
	}
	
	public boolean isRoot() {
		return ((ClassDecl!=null) && (daddy==null));
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
		this.ClassDecl=ID;
	}
	
	public boolean isShallow() {
		return ClassDecl==null;
	}
	
	@Override
	public String toString() {
		if(this.isShallow()) {
			return this.name+" is a shallow class"+(sons.size()==0?".":" | His direct offspring are:"+this.printSons());
		}
		else{
			return this.name+" is a parsed class, with "+this.daddy.getName()+" as a daddy"+(sons.size()==0?".":" | His direct offspring are:"+printSons());
		}
	}
	
	public String printSons() {
		String acc="";
		for(NodeClass son : sons) {
			acc+=" "+son.getName();
		}
		return acc;
	}
	
	public String getName() {
		return this.name;
	}
	
	public MethodDeclaration lookUp() {
		MethodDeclaration[] md=this.ClassDecl.getMethods();
		
		return md[1];
	}
}
