package tp4;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import org.apache.commons.lang3.concurrent.CallableBackgroundInitializer;

import Visualisation.CallGraphGenerator;

public class Main {
	static final int precision=1000;
	public static void main(String[] args) {
		Launcher launcher = new Launcher();//Creation of the model of the project we want to check out
		launcher.addInputResource("/home/ariale/data-extraction/tp3"); 
		launcher.buildModel();
		CtModel model = launcher.getModel();
		
		
		HashMap<HashSet<String>, Double> valueGraph=getCouplingGraph(model);
		
		
		TreeNode t=Clustering(valueGraph,model);
		if(t!=null) {
			t.display();
		}
		
		for(TreeNode tr: t.clusterToModule()) {
			System.out.println(tr);
		}
		//CallGraphGenerator graph=new CallGraphGenerator();//Call graph generation
		//convertToPercent(valueGraph);
		//graph.buildGraphFromHashMap(valueGraph);
		//graph.generate();
		
	}
	
	
	//convertion to %, precision available as a static member. 
	static public void convertToPercent(HashMap<HashSet<String>,Double> coupling) {
		double div=getCouplingSum(coupling);
		
		for(HashSet<String> key : coupling.keySet()) {
			coupling.put(key, Double.valueOf(Math.floor((coupling.get(key)/div)*precision)/precision));
		}
		System.out.println(getCouplingSum(coupling));
	}
	
	//Sum the entire map
	static public double getCouplingSum(HashMap<HashSet<String>,Double> coupling) {
		double res=0;
		for(Double i:coupling.values()) {
			res+=i.doubleValue();
		}
		return res;
	}
	
	//exercice 4 (1 bis)
	//Create a map of the number of method called between two classes.
	//example (String,Object) 6 would mean that String and Object call 6 method of each other
	static public HashMap<HashSet<String>,Double> getCouplingGraph(CtModel model) {
		HashMap<HashSet<String>,Double> couplage=new HashMap<>();

		for(CtType<?> t : model.getAllTypes()) {//Class definitions
			//System.out.println(t.getReference());
			for(CtMethod<?> m : t.getMethods()) {//Their methods
				//System.out.println("  "+m.getSimpleName());
				for(CtInvocation<?> i : m.getElements(new TypeFilter<>(CtInvocation.class))) {//get all method called (invocation)
					CtExecutable<?> ex=i.getExecutable().getExecutableDeclaration();//get the declaration of the called method
					
					if(ex==null) {
						System.out.println(i);
						continue;
					}
					//We don't consider internal calls, since they are useless for coupling informations
					if(t.getReference().toString().compareTo(ex.getReference().getDeclaringType().toString())!=0) {

						HashSet<String> key=new HashSet<>();//And create or add it to the corresponding value in the map
						key.add(t.getReference().toString());
						key.add(ex.getReference().getDeclaringType().toString());
						Double value=couplage.get(key);
						if(value==null) {
							value=Double.valueOf(1);
							couplage.put(key,value);
						}
						couplage.put(key,Double.valueOf(value.intValue()+1));
					}
				}
			}
		}
		return couplage;
	}
	
	//Exercice 2
	static public TreeNode Clustering(HashMap<HashSet<String>,Double> couplingGraph,CtModel model) {
		TreeNode root=null;
		HashSet<TreeNode> clusters=new HashSet<>();
		
		
		for(CtType type : model.getAllTypes()) {//Creation of all basic clusters
			TreeNode temp=new TreeNode(type.getReference().toString());//value doesn't matter. It'll never be considered on leafs.
			clusters.add(temp);
		}
		
		
		if(clusters.size()==0) {//No classes parsed, no result.
			System.out.println("Pas de classes trouvées");
			return null;
		}
		
		
		
		while(clusters.size()>1) {
			//System.out.println(clusters.size());
			dendroCreation(clusters,couplingGraph);
		}
		
		for(TreeNode t : clusters) {//Since there's only one... But it's a way to access it.
			root=t;
		}
		
		System.out.println("clustering's done");
		return root;
	}
	
	static private void dendroCreation(HashSet<TreeNode> clusters,HashMap<HashSet<String>,Double> couplingGraph) {
		double maxCoupling=-1;
		TreeNode winner1=null;
		TreeNode winner2=null;
		
		//System.out.println("###########");
		for(TreeNode t1 : clusters) {
			//System.out.println("----------------");
			for(TreeNode t2 : clusters) {
				if(t1!=t2) {
					double contestorValue=t1.clusterCoupling(t2, couplingGraph);
					//System.out.println(t1+" "+t2+" = "+contestorValue);
					
					if(contestorValue > maxCoupling) {
		
						maxCoupling=contestorValue;
						winner1=t1;
						winner2=t2;
						
					}
				}
			}
		}
		
		
		TreeNode res=winner1.mergeWith(winner2,couplingGraph);
		clusters.add(res);
		clusters.remove(winner1);
		clusters.remove(winner2);
	}
	
	

}
