package gui;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class GUIRes {
	private boolean isDone=false;
	private JFrame f=new JFrame("résultats");
	private DefaultListModel<String> l=new DefaultListModel<>();
	
	public void showRes(Dimension d) {
		f.setSize(d);
		f.add(new JList<String>(l));
		f.setVisible(true);
	}
	
	public void showEmptyRes(Dimension d) {
		l.addElement("Rien à afficher, vous n'avez rien sélectionné!");
		
		
		showRes(d);
	}
	
	public void showOnlyCallGraph(Dimension d) {
		if(!l.isEmpty()) {
			l.addElement("");
		}
		l.addElement("Le graphe d'appel vas s'ouvrir dans une nouvelle fenêtre lorsque les calculs serons finis");
		
		showRes(d);
	}
	
	public void addCallGraph() {
		l.addElement("Le graphe d'appel vas s'ouvrir dans une nouvelle fenêtre lorsque les calculs serons finis");
		
	}
	
	public void addResult(String phrase) {
		l.addElement(phrase);
	}
}
