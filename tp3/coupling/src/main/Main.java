package main;



import java.io.File;
import logic.CallGraphVisitor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import logic.CallGraphVisitor;


public class Main {


	public static String projectPath = "./";
	public static String projectSourcePath = projectPath + "src";
	public static String jrePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre";
	public static String ASTPath = "/home/ariale/data-extraction/ast libs";
	
	public static void main(String[] args) throws IOException {
	// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		
		
		CallGraphVisitor visitor=new CallGraphVisitor();
		
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			
			CompilationUnit parse = parse(content.toCharArray());
			GenericVisit(parse, visitor);
		}
		//System.out.println(visitor);
		
		visitor.resolveMethodsLink();
		System.out.println("pouet");
		visitor.callGraphGeneration();
		
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
		String[] classpath = {jrePath, ASTPath};
 
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(classSource);
		
		return (CompilationUnit) parser.createAST(null); // create and parse
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

	public static void GenericVisit(CompilationUnit parse,ASTVisitor visitor) {
		parse.accept(visitor);
	}
}

