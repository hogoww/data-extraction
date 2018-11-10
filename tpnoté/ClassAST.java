



public class ClassAST {
	private ArrayList<MethodAST> methodAST;
	private ArrayList<FieldDeclaration> attributes;
	private TypeDeclaration me;
	
	public boolean visit(TypeDeclaration node) {
		classAST.add(new ClassAST(node));
		return true;
	}	
	
	public ArrayList<MethodAST> getMethodAST() {
		return methodAST;
	}
	
	public ArrayList<FieldDeclaration> getAttributes() {
		return attributes;
	}

	public TypeDeclaration getMe() {
		return me;
	}

	public ClassAST(TypeDeclaration me) {
		this.methodAST=new ArrayList<>();
		this.methodAST=new ArrayList<>();
		this.me=me;
	}

}