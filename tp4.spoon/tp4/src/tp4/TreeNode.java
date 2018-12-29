package tp4;

public class TreeNode {
	private TreeNode left=null;
	private TreeNode right=null;
	private String className=null;
	private double value=0;
	private TreeNode father;
	
	
	
	public TreeNode(){
		father=null;
	}
		
	public TreeNode(TreeNode daddy,double couplageValue) {
		father=daddy;
		value=couplageValue;
	}
	
	public TreeNode(TreeNode daddy,String className_) {
		father=daddy;
		className=className_;
	}
	
	public boolean isRoot() {
		return father==null;
	}
	
	public boolean isLeaf() {
		return className==null;
	}
	
	public TreeNode getLeft() {
		return left;
	}

	public void setLeft(TreeNode left) {
		this.left = left;
	}

	public TreeNode getRight() {
		return right;
	}

	public void setRight(TreeNode right) {
		this.right = right;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
