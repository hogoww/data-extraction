package main;


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

import logic.CallGraphVisitor;

/*import org.eclipse.jdt.core.dom.MethodDeclaration;
	import org.eclipse.jdt.core.dom.MethodInvocation;
	import org.eclipse.jdt.core.dom.VariableDeclarationFragment;*/
import org.eclipse.jdt.core.dom.*;


public class Main {


	public static String projectPath = "./";
	public static String projectSourcePath = projectPath + "src";
	public static String jrePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre";

	public static void main(String[] args) throws IOException {
	// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		
		
		CallGraphVisitor visitor=new CallGraphVisitor();
		
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);
			
			CompilationUnit parse = parse(content.toCharArray());
			GenericVisit(parse, visitor);
		}
		
		System.out.println(visitor);

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

