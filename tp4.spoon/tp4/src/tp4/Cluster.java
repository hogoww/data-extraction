package tp4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import Visualisation.CallGraphGenerator;

public class Cluster {
	private Cluster left=null;
	private Cluster right=null;
	private HashSet<String> classNames=new HashSet<String>();
	private Double value=new Double(0);
	private Cluster father=null;
	
	
	public Cluster(){}
	 
	public Cluster(Cluster daddy,Double couplageValue) {
		father=daddy;
		value=couplageValue;
	}
	
	public Cluster(String className) {
		classNames.add(className);
	}
	
	public Cluster(Cluster daddy,HashSet<String> classNames_) {
		father=daddy;
		classNames=classNames_;
	}
	
	
	public Cluster mergeWith(Cluster t,HashMap<HashSet<String>,Double> couplingGraph) {
		Cluster mergeResult=new Cluster();
		
		mergeResult.addAllClassNames(this);
		mergeResult.addAllClassNames(t);
		
		if(this.isLeaf() && t.isLeaf()) {
			mergeResult.setValue(this.couplingBetweenLeaves(t, couplingGraph));
		}
		else {
			mergeResult.setValue(this.couplingBetweenTwoNotLeaf(t, couplingGraph));
		}
		mergeResult.setLeft(this);
		mergeResult.setRight(t);
		
		return mergeResult;
	}
/*	
	//check if this contains every classes in the other node.
	public boolean contains(TreeNode t) {
		for(String s1 : this.getClassNames()) {
			boolean temp=false;
			for(String s2 : this.getClassNames()) {
				if(s1==s2) {
					temp=true;
					break;
				}
			}
			if(! temp) {
				return false;
			}
		}
		return true;
	}
	*/
	
	private double couplingBetweenLeaves(Cluster anotherTreeNode,HashMap<HashSet<String>,Double> couplingGraph) {
		HashSet<String> key=new HashSet<>();//Key creation is a bit tricky, since we have to get only an element from a set with one value. Have to go through an iterator for that
		for(String c1 : this.getClassNames()) {
			key.add(c1);
		}
		for(String c2 : anotherTreeNode.getClassNames()) {
			key.add(c2);
		}

		return couplingGraph.getOrDefault(key,new Double(0)).doubleValue();
	}

	public double couplingBetweenTwoNotLeaf(Cluster anotherTreeNode,HashMap<HashSet<String>,Double> couplingGraph) {
		double sum=0;
		for(String className: this.getClassNames()){
			
			Cluster temp=new Cluster(className);
			sum+=temp.couplingBetweenLeafAndNode(anotherTreeNode, couplingGraph);
		}
		return sum;
	}
	
	private double couplingBetweenLeafAndNode(Cluster anotherTreeNode,HashMap<HashSet<String>,Double> couplingGraph) {
		//Else, one of them is a leaf
		Cluster cluster;
		String single=null;
		if(this.isLeaf()) {
			cluster=anotherTreeNode;
			for(String s : this.getClassNames()) {//Same trick as earlier 
				single=s;
			}
		}
		
		else {
			cluster=this;
			for(String s : anotherTreeNode.getClassNames()) {//Same trick as earlier 
				single=s;
			}
		}
		
//		System.out.println("cluster");
//		for(String s : cluster.getClassNames()) {
//			System.out.println(s);
//		}
		//System.out.println("single= "+single);
		// div by 2, since we do an average
		//System.out.println(couplingGraph);
		double couplage=0;
		for(String s : cluster.getClassNames()) {
			HashSet<String> key=new HashSet<>();
			key.add(single);
			key.add(s);
			//System.out.println(key);
			couplage+=couplingGraph.getOrDefault(key,new Double(0)).doubleValue();
		}
		//System.out.println("couplage="+couplage);
		
		return couplage;
	}
	
	public Double clusterCoupling(Cluster anotherTreeNode,HashMap<HashSet<String>,Double> couplingGraph) {
		if(this.isLeaf() && anotherTreeNode.isLeaf()) {//If they both are leaves, we just have to get their coupling from the graph
			return new Double(couplingBetweenLeaves(anotherTreeNode,couplingGraph));
		}
		
		if(!(this.isLeaf() || anotherTreeNode.isLeaf())) {
			return new Double(couplingBetweenTwoNotLeaf(anotherTreeNode,couplingGraph));
		}
		
		//		System.out.println(new Double(addingValue+couplingBetweenLeafAndNode(anotherTreeNode,couplingGraph)));
//		System.out.println(couplingBetweenLeafAndNode(anotherTreeNode,couplingGraph));
//		System.out.println("");
		
		
		return new Double(couplingBetweenLeafAndNode(anotherTreeNode,couplingGraph));
	}
	
	
	public boolean isRoot(){
		return father==null;
	}
	
	public boolean isLeaf() {
		return (left==null && right==null);
	}
	
	public Cluster getLeft() {
		return left;
	}

	public void setLeft(Cluster left) {
		this.left = left;
	}

	public Cluster getRight() {
		return right;
	}

	public void setRight(Cluster right) {
		this.right = right;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public HashSet<String> getClassNames() {
		return classNames;
	}

	public void setClassNames(HashSet<String> classNames) {
		this.classNames = classNames;
	}
	
	public void addAllClassNames(Cluster cluster) {
		for(String c : cluster.getClassNames()) {
			this.addClassName(c);
		}
	}


	public void addClassName(String newClass) {
		this.classNames.add(newClass);
	}
	
	public void display() {
		CallGraphGenerator g=new CallGraphGenerator();
		g.buildGraphFromTreeNode(this);
		g.generate(2);
				
	}
	
	public String toString() {
		String res="(";
		for(String s : this.classNames) {
			res+=","+s;
		}
		res+=" = "+this.value+")";
		return res;
	}

	
	public double similarity() {
		return this.value;
	}
	
	public ArrayList<Cluster> clusterToModule() {
		ArrayList<Cluster> partition=new ArrayList<>();
		this.clusterToModuleR(partition);
		return partition;
	}
	
	private void clusterToModuleR(ArrayList<Cluster> partition){
		if(this.isLeaf()) {
			partition.add(this);
			return;
		}
		
		if(this.similarity()>((this.left.similarity()+this.right.similarity())/2)) {
			partition.add(this);
		}
		else {
			left.clusterToModuleR(partition);
			right.clusterToModuleR(partition);
		}
	}
}
