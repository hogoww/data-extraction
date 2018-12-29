package tp4;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.concurrent.CallableBackgroundInitializer;

import Visualisation.CallGraphGenerator;

public class Main {
	static final int precision=1000;
	public static void main(String[] args) {
		Launcher launcher = new Launcher();//Creation of the model of the project we want to check out
		launcher.addInputResource("/home/ariale/data-extraction/tp4.spoon/tp4"); 
		launcher.buildModel();
		CtModel model = launcher.getModel();
		
		
		HashMap<HashSet<String>, Double> valueGraph=getCouplingGraph(model);
		convertToPercent(valueGraph);
		CallGraphGenerator graph=new CallGraphGenerator();//Call graph generation
		
		graph.buildGraphFromHashMap(valueGraph);
		graph.generate();
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
		return couplage;
	}
	
	//Exercice 2
}
