package SourceAnalyser;

import java.util.ArrayList;
import javax.swing.*;

public class GUI {

	private String[] outputPhrases= {
			"#1 Nombre de classes de l'applications",//1
			"#2  Nombre de lignes de code de l'applications",//2
			"#3  Nombre	total de méthodes de l’application",//3
			"#4  Nombre	total de packages de l’application",//4
			"#5  Nombre	moyen de méthodes par classe",//5
			"#6  Nombre	moyen de lignes de code par méthode",//6
			"#7  Nombre	moyen d’attributs par classe",//7
			"#8  Les 10% des classes qui possèdent le plus grand nombre de méthodes",//8
			"#9  Les 10% des classes qui possèdent le plus grand nombre d’attributs",//9
			"#10 Les classes qui font partie en	même temps les deux catégories précédentes",//10
			"#11 Les classes qui possèdent plus de X méthodes (la valeur	de X est donnée)",//11
			"#12 Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code (par classe)",//12
			"#13 Le nombre maximal de paramètres par rapport à toute les méthodes de l’application"//13
			};
	
	static final private int basicHeight=10;
	static final private int Margin=20;
	static final private int offSet=35;
	static final private int boxHeight=50;
	static final private int boxWidth=800;
	
	
	private ArrayList<JCheckBox> answers;
	private JTextField pathText;
	
	public GUI() {
		answers=new ArrayList<>();
	}
	
	public void checkboxShow() {
		JFrame f= new JFrame("Statistiques Java Projets");  

		int height=basicHeight;
	    
		//To be changed correctly
		this.pathText=new JTextField("[PathToProject]/src");
	    pathText.setBounds(Margin,height, boxWidth,boxHeight/2);
	    f.add(pathText);
	   
	    height+=offSet;
		
		for(int i=0;i<outputPhrases.length;i++) {
			JCheckBox t=new JCheckBox(outputPhrases[i]+".",true);
			t.setBounds(Margin,height ,boxWidth,boxHeight);
			height+=offSet;
			answers.add(t);
			f.add(t);
		}

		f.setSize(boxWidth+Margin,height);
		
		
		
		//Button that closes the window is enough
	    /*JButton b=new JButton("Order");  
	    b.setBounds(100,250,80,30);  
	    b.addActionListener(J);*/
	    
		//show(f);
	}
	
	public void show(JFrame f) {
		f.setLayout(null);
		f.setVisible(true);
	}
	
	public void resultShow() {
		//todo
	}
	
	public ArrayList<JCheckBox> getBoxes(){
		return answers;
	}

	public boolean at(int index){
		return answers.get(index).isSelected();
	}

}
