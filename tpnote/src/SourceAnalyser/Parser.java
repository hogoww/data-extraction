package SourceAnalyser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
/*import org.eclipse.core.internal.localstore.Bucket.Visitor;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;*/
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import gui.GUI;
import gui.GUIRes;

/*import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;*/
import org.eclipse.jdt.core.dom.*;

public class Parser {
	
	public static String projectPath = "./";
	public static String projectSourcePath = projectPath + "src";
	public static String jrePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre";
	//public static final String jrePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java";

/*	public static final String projectPath = "C:\\Users\\zakarea.alshara\\osgi_workspace\\projectToParse";
	public static final String projectSourcePath = projectPath + "\\src";
	public static final String jrePath = "C:\\Program Files\\Java\\jre1.8.0_51\\lib\\rt.jar";*/

	
	public static void main(String[] args) throws IOException {

		// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		CodeStatistiqueVisitor visitor=new CodeStatistiqueVisitor();
		GUI g=new GUI(projectSourcePath);
		g.checkboxShow();
		
	

		while(g.isNotDone()) {//Horrific but hey...	 GUI should stop the execution
			try{
				Thread.sleep(1000);
			}
			catch(InterruptedException e){
				System.out.println("waiting for input");
			}
		}
		
		
		if(!g.followUp()) {
			System.out.println("Pas de calculs à faire!");
			return;
		}
		
		GUIRes windowRes=new GUIRes();
		if(g.everythingUnchecked()) {
			windowRes.showEmptyRes(g.getBoxDimension());
			return;
		}
		//else
		
		System.out.println("Calcul en cours...");
		
		for (File fileEntry : javaFiles) { 
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);

			CompilationUnit parse = parse(content.toCharArray());
			GenericVisit(parse, visitor);
		}
	
		//#1 isn't in the questions, so we have twice #12
		
		if(true)//check the correspondance with numbers. Should be in gui anyway.
			System.out.println("Classes:"+visitor.toString());
		if(g.at(0))
			windowRes.addResult("#1 "+visitor.nbClass()+" classes différentes.");
		if(g.at(1)) {
			windowRes.addResult("#2 Le programme contient "+ visitor.programLineNumber()+" lignes.");}
		if(g.at(2))	
			windowRes.addResult("#3 Ce programme est composé de "+visitor.nbMethodTotal()+" méthodes.");
		if(g.at(3))
			windowRes.addResult("#4 Ce programmes importe "+visitor.getNbOfUniqueImport()+" packages différents.");
		if(g.at(4))	
			windowRes.addResult("#5 Il y a en moyenne "+visitor.AverageNbMethodByClass()+" méthodes par classes.");
		if(g.at(5)) {
			windowRes.addResult("#6 Le programme contient en moyenne "+ visitor.AverageLineNumberPerMethods()+" lignes par méthodes.");}
		if(g.at(6))
			windowRes.addResult("#7 Il y a une moyenne de "+visitor.AverageFieldsByClass()+" attributs par classes.");
		if(g.at(7)) 
			windowRes.addResult("#8 Les 20% de classes aillant le plus de méthodes sont : "+visitor.getPercentileSortByMethodsAsString(20)+".");
		if(g.at(8))
			windowRes.addResult("#9 Les 20% de classes aillant le plus d'attributs sont : "+visitor.getPercentileSortByFieldsAsString(20)+".");	
		if(g.at(9))
			windowRes.addResult("#10 Les 20% de classe aillant le plus de méthode ET d'attributs sont : "+visitor.GetPercentileSortByFieldsAndMethodsAsString(20)+".");
		if(g.at(10))
			windowRes.addResult("#11 Les classes aillant plus de "+5+" methodes sont : "+visitor.getClassesByNbMethodMinAsString(5)+".");
		if(g.at(11)) {
			windowRes.addResult("#12 Les 10% de methodes aillant le plus de lignes de codes sont : "+visitor.getMethodPercentileSortByLineNumberAsString(10)+".");
		}
		if(g.at(12)) {
			windowRes.addResult(visitor.getMaxParameterMethodAsString());
		}
		if(g.at(13)) {
			windowRes.addCallGraph();
		}
		
		windowRes.showRes(g.getBoxDimension());
		
		if(g.at(13)) {//done twice, for performances reasons
			visitor.fullCallGraph();		
		}
		
	}

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		parser.setBindingsRecovery(true);
 
		@SuppressWarnings("rawtypes")
		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
 
		parser.setUnitName("");
 
		String[] sources = { projectSourcePath }; 
		String[] classpath = {jrePath};
 
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(classSource);
		
		return (CompilationUnit) parser.createAST(null); // create and parse
	}
	public static void GenericVisit(CompilationUnit parse,ASTVisitor visitor) {
		parse.accept(visitor);
	}
}
