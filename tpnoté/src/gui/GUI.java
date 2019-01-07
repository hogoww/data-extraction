package gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;

public class GUI {

	private String[] outputPhrases= {
			"#1  Nombre  de classes de l'applications",//1
			"#2  Nombre  de lignes de code de l'applications",//2
			"#3  Nombre	 total de méthodes de l’application",//3
			"#4  Nombre	 total de packages de l’application",//4
			"#5  Nombre	 moyen de méthodes par classe",//5
			"#6  Nombre	 moyen de lignes de code par méthode",//6
			"#7  Nombre	 moyen d’attributs par classe",//7
			"#8  Les 10% des classes qui possèdent le plus grand nombre de méthodes",//8
			"#9  Les 10% des classes qui possèdent le plus grand nombre d’attributs",//9
			"#10 Les classes qui font partie en	même temps les deux catégories précédentes",//10
			"#11 Les classes qui possèdent plus de X méthodes (la valeur	de X est donnée)",//11
			"#12 Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code (par classe)",//12
			"#13 Le nombre maximal de paramètres par rapport à toute les méthodes de l’application",//13
			" Visualiser le graphe d'appel des méthodes de l'applications"//callGraph
			};
	
	static final private int basicHeight=10;
	static final private int Margin=20;
	static final private int offSet=35;
	static final private int boxHeight=50;
	static final private int boxWidth=800;
	
	
	private ArrayList<JCheckBox> answers=new ArrayList<>();
	private String currentPathProject;
	private JTextField pathText;
	private boolean isDone;
	private boolean followUp=true;
	private Dimension boxDimention=null;
	
	public GUI(String currentPathProject) {
		this.currentPathProject=currentPathProject;
	}
	
	public void checkboxShow() {
		JFrame f= new JFrame("Statistiques Java Projets");  

		int height=basicHeight;
	    
		//To be changed correctly
		this.pathText=new JTextField(currentPathProject);//"[PathToProject]/src");
	    pathText.setBounds(Margin,height, boxWidth-Margin,boxHeight/2);
	    f.add(pathText);
	    f.add(new JFileChooser());
	   
	    height+=offSet;
		
		JCheckBox sa=new JCheckBox("Tout sélectionner.",true);
		sa.setBounds(Margin,height ,boxWidth,boxHeight);
		sa.setSelected(false);
		sa.addActionListener(new ActionListener() {
				boolean isSelected=false;
				
				@Override
				public void actionPerformed(ActionEvent e) {
					isSelected=!isSelected;
					for(JCheckBox cb : getBoxes()) {
						cb.setSelected(isSelected);
					}
					if(isSelected) {
						sa.setText("Tout déselectionner");
					}
					else {
						sa.setText("Tout sélectionner");
					}
				}
			}
			);
		height+=offSet;
		f.add(sa);
	    
		for(int i=0;i<outputPhrases.length;i++) {
			JCheckBox t=new JCheckBox(outputPhrases[i]+".",true);
			t.setBounds(Margin,height ,boxWidth,boxHeight);
			t.setSelected(false);
			height+=offSet;
			answers.add(t);
			f.add(t);
		}

		
		//Button that closes the window is enough
		JButton b=new JButton("Valider");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				f.setVisible(false);
				isDone=true;
			}          
		});
		
		b.setBounds(0,height+offSet ,boxWidth,boxHeight);
		height+=offSet+offSet;
		f.add(b);
		
		boxDimention=new Dimension(boxWidth+Margin, height+offSet);
		
		f.setSize(boxDimention);
		  f.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e)
	            {
	                isDone=true;
	                followUp=false;
	                e.getWindow().dispose();
	            }
	        });
		 
		  
		show(f);
		
	}
	
	
	public void show(JFrame f) {
		f.setLayout(null);
		f.setVisible(true);
		isDone=false;
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
	
	public boolean everythingUnchecked() {
		
		for(JCheckBox h : answers) {
			if(h.isSelected()) {
				return false;
			}
		}
		return true;
	}

	public boolean isNotDone() {
		return !isDone;
	}
	
	public boolean followUp() {
		return followUp;
	}
	
	public Dimension getBoxDimension() {
		return this.boxDimention;
	}
	
}
