package logic;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.TypeDeclaration;


public class NodeClass{
	NodeClass daddy;
	ArrayList<NodeClass> sons;
	String name;
	TypeDeclaration ClassDecl;
	ArrayList<MethodCaller> myMethods;
	


	public NodeClass(String myName){//For classes we might not know (yet or at all)
		this.daddy=null;
		this.sons=new ArrayList<>();
		this.ClassDecl=null;
		this.name=myName;
		this.myMethods=new ArrayList<>();
	}
	
	public NodeClass(NodeClass daddy, TypeDeclaration data,ArrayList<MethodCaller> meths){
		this.daddy=daddy;
		this.sons=new ArrayList<>();
		this.name=data.getName().toString();
		this.ClassDecl=data;
		this.myMethods=meths;
		setContainerForMyMethods();
	}
	
	public void setContainerForMyMethods() {
		for(MethodCaller mc: myMethods) {
			mc.setContainer(this);
		}
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
	
	public void GotMyID(TypeDeclaration ID,NodeClass superType,ArrayList<MethodCaller> meths) {
		this.daddy=superType;
		this.ClassDecl=ID;
		this.myMethods=meths;

		for(MethodCaller mc: this.myMethods) {
			mc.setContainer(this);
		}
	}
	
	public boolean isShallow() {
		return ClassDecl==null;
	}
	
	
	public String getName() {
		return this.name;
	}

	public NodeClass getDaddy() {
		return daddy;
	}

	public TypeDeclaration getData() {
		return ClassDecl;
	}
	

	public void resolveMethodsLinks(CallGraphVisitor packs) {
		if( ! this.isShallow()) {
			for(MethodCaller mc : this.myMethods) {
				mc.resolveMethodsLinks(packs);
			}
		}
	}
	
	public ArrayList<MethodCaller> lookUpMethod(String methodName){
		
		ArrayList<MethodCaller> res=new ArrayList<>();
		for(MethodCaller mc: this.myMethods) {
			if(mc.getMethod().getName().toString().equals(methodName)) {
				res.add(mc);
			}
		}
		
		return res;
	}
	
	@Override
	public String toString() {
		String res=this.name;
		if(this.isShallow()) {
			res+=" is a shallow class";
		}
		else{
			res+=" is a parsed class, with "+this.daddy.getName()+" as a daddy";
		}
		res+=(sons.size()==0?".":" | His direct offspring are:"+this.printSons());
		res+=(this.myMethods==null?"":"\n my methods are :"+this.printMethods());
		return res;
	}
	
	public String printSons() {
		String acc="";
		for(NodeClass son : sons) {
			acc+=" "+son.getName();
		}
		return acc;
	}
	
	public String printMethods() {
		String acc="";
		for(MethodCaller m: this.myMethods) {
			acc+=" "+m.getMethod().getName();
		}
		return acc;
	}
	
	public int getCouplingWith(NodeClass anotherClass) {
		if(this.myMethods==null) {
			return 0;
		}
		
		int res=0;
		String otherClassName=anotherClass.getName();
		System.out.println(otherClassName);
		
		for(MethodCaller mc : this.myMethods) {
			
			System.out.println(" "+mc.getMethod().getName().toString());
			for(MethodCaller called : mc.getCalledMethod()) {
				System.out.println("  "+mc.getMethod().getName().toString());
				if(otherClassName.equals(called.getContainer().getName().toString())) {
					res++;
				}		
			}
			
		}
		return res;
	}
}
