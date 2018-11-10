package testGraphDot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
	 * Appeler transitions mais est en fait le graphe. Le choix de l'arraylist est une collection prise par défaut.
	 */
	ArrayList<Transition> transitions;

	/**
	 * Seulement un constructeur par défaut sera créer, les tests seront une fonction initializeTest() 
	 */
	public CallGraphGenerator() {
		super();
		transitions = new ArrayList<Transition>();
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
			res += t.getCaller() + " -> " + t.getCallee() +"\n"; // -> pour unidirectionnel; -- pour bidirectionnel
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
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
			writer.println(graph);writer.flush();
	}
	
	/**
	 * Initialise un graphe circulaire à 2 noeuds qui va servir de test transitiond e t1 vers t2 puis de t2 vers t1
	 */
	public void initializeTest() {
		Transition t1, t2;
		t1 = new Transition("t1","t2");
		t2 =new Transition("t2","t1");		
		transitions.add(t1);
		transitions.add(t2);
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
			e.printStackTrace();
		}
	}
	
	/*
	public static void main(String[] args) {
		System.out.println("Created class call graph");
		CallGraphGenerator g = new CallGraphGenerator ();
		System.out.println("Initializing test");
		g.initializeTest();
		System.out.println("Filling file");
		g.writeGrapheInFile();
		System.out.println("Call to dot");
		g.callDot();
	}*/

}
