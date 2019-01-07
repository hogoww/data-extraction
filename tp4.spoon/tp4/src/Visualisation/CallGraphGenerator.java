package Visualisation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import tp4.TreeNode;

/**
 * 
 * @author rogliano
 *
 * Génération d'un graphe orienté de test sans mise en forme excepté celle par défaut de dot
 * Un fichier .dot et un .png seront normalement générés dans le cas de la non-génération du .png le site http://www.webgraphviz.com/ permet de visualier le contenu du .dot
 * 
 */

public class CallGraphGenerator {

	/**
	 * Appeler transitions mais est en fait le graphe. Le choix de set (arraylist en set) est pour éviter la duplication de transition entre 2 éléments.
	 */
	HashSet<Transition> transitions;

	/**
	 * Seulement un constructeur par défaut sera créer, les tests seront une fonction initializeTest() 
	 */
	public CallGraphGenerator() {
		super();
		transitions = new HashSet<Transition>();
	}
	
	/**
	 * Créer la String que nous mettrons dans un fichier .dot qui serait utiliser pour générer le graphe via dot
	 * @return une String de la forme d'un graphe dot
	 */
	public String createGraphString() {
		if(transitions.isEmpty()) {
			System.err.println("No graph created");
			return null;
		}
		String res = "digraph{\n";
		for(Transition t: transitions) {
			res += t.getCaller() + " -> " + t.getCallee() +" [label=\""+t.getLabel()+"\",arrowhead=\"none\"];\n"; // -> pour unidirectionnel; -- pour bidirectionnel
		}
		res+="\n }";
		return res;
	}
	
	/**
	 * Fonction se chargeant de générer le fichier .dot, ce dernier est réécrit à chaque utilisation (pas de append).
	 * Ne fait rien si aucun graphe est donné
	 */
	public void writeGrapheInFile() {
		String graph = createGraphString();
		if(graph.isEmpty()) {
			System.err.println("No graph given.");
			return;
		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("graphGenerated.dot", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(graph);
		writer.flush();
	}
	
	
	/**
	 * Appel à dot pour générer un .png du graph afin de pouvoir le visualiser après coup (potentiel ajout d'appel à evince ou autre pour visualiser le .png)
	 */
	public void callDot() {
		Runtime run = Runtime.getRuntime();
		// p uniquement si tu veux manier les flux de ton apps
		try {
			run .exec("./instru.sh");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void generate(){
		System.out.println("Filling file");
		writeGrapheInFile();
		System.out.println("Call to dot");
		callDot();
	}
	
	
/*	public static void main(String[] args) {
		System.out.println("Created class call graph");
		CallGraphGenerator g = new CallGraphGenerator ();
		System.out.println("Initializing test");
		//g.initializeTest();
		System.out.println("Filling file");
		g.writeGrapheInFile();
		System.out.println("Call to dot");
		g.callDot();
	}*/

	public HashSet<Transition> getTransitions() {
		return transitions;
	}

	public void add(Transition t){
		if(!transitions.contains(t)){
			transitions.add(t);
		}
	}
	
	public void buildGraphFromHashMap(HashMap<HashSet<String>,Double> hashmapOfTransition) {
		for(HashSet<String> trkey : hashmapOfTransition.keySet()) {
			ArrayList<String> tempString=new ArrayList<>();
			for(String ctemp : trkey ) {
				tempString.add(ctemp);
			}
			if(tempString.size()==2) {
				this.transitions.add(new Transition("\""+tempString.get(0)+"\"","\""+tempString.get(1)+"\"",hashmapOfTransition.get(trkey)));
			}
			else {
				if(tempString.size()==0) {
					System.out.println(trkey);
				}
				else {
					this.transitions.add(new Transition("\""+tempString.get(0)+"\"","\""+tempString.get(0)+"\"",hashmapOfTransition.get(trkey)));
				}
			}
		}
	}
	
	public String buildGraphFromTreeNode(TreeNode root) {
		String caller="\"";
		boolean first=true;
		for(String ctemp : root.getClassNames() ) {
			if(!first) {
				caller+=" + ";
			}
			else {first=false;}
			caller+=ctemp;
		}
		caller+="\"";
		
		
		
		if(root.getLeft()!=null) {
			String callee=buildGraphFromTreeNode(root.getLeft());
			this.add(new Transition(caller,callee,root.getValue()));
		}
		
		
		if(root.getRight()!=null) {
			String callee=buildGraphFromTreeNode(root.getRight());
			this.add(new Transition(caller,callee,root.getValue()));
		}
		
		return caller;
	}
}
