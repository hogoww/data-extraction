package SourceAnalyser;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashSet;

import SourceAnalyser.ClassComparator.comparaisonType;

import org.eclipse.jdt.core.dom.*;



//Exo1
public class CodeStatistiqueVisitor extends ASTVisitor{
	private ArrayList<TypeDeclaration> classAST;
	private ArrayList<TypeDeclaration> interfaceAST;
	private HashSet<String> imports;

	
	public CodeStatistiqueVisitor(){
		super();
		this.classAST = new ArrayList<>();
		this.interfaceAST = new ArrayList<>();
		this.imports = new HashSet<>();
	}
	
	//Needed visitors
	@Override//classes déclarations
	public boolean visit(TypeDeclaration node) {
		if(node.isInterface()) {
			interfaceAST.add(node);
		}	
		else {
			classAST.add(node);
		}
		return true;//We still have to parse the classes, in case we have inner classes/interfaces...
	}
	
	@Override
	public boolean visit(ImportDeclaration node) {
		imports.add(node.getName().toString());
		return true;
	}

	//#1
	public int nbClass() {
		return classAST.size();
	}

	
	public String getClassesNames() {
		
		String result="";
		for(TypeDeclaration c : classAST) {
			result=result+"\n"+c.getName();		
		}
		return result;
	}
	
	//#2 we reduce line number to as statement, which is what will be executed anyway. 
	public int programLineNumber() {
		int res=0;//Not the most efficient, but fonctionnal
			
		for(TypeDeclaration t: classAST) {
			LineCounterVisitor lv=new LineCounterVisitor();
			t.accept(lv);
			res+=lv.getCounter();
		}
		
		return res;
	}
	

	
	//#3
	public int nbMethodTotal(){
		int cmpt=0;
		for(TypeDeclaration c: classAST) {
			cmpt+=c.getMethods().length;
		}
		return cmpt;
	}

	//#4
	public int getNbOfUniqueImport() {
		return imports.size();
	}

	
	//#5
	public int AverageNbMethodByClass() {
		return nbMethodTotal()/nbClass();
	}

	//#6
	public int AverageLineNumberPerMethods() {
		int MethodNumber=0;//Not the most efficient, but fonctional
		
		for(TypeDeclaration c: classAST) {
			MethodNumber+=c.getMethods().length;
		}
		
		if(MethodNumber>0) {
			return this.programLineNumber()/MethodNumber;
		}
		else {
			return -1;//error case, even if it's unlikely we ever get a program without method to analyse.
		}
	}
	
	
	//#7
	public int AverageFieldsByClass() {
		return nbFieldTotal()/nbClass();
	}
	

	private int nbFieldTotal(){
		int cmpt=0;
		for(TypeDeclaration c: classAST) {
			cmpt+=c.getFields().length;
		}
		return cmpt;
	}


	//#8
	public String getPercentileSortByMethodsAsString(int taux){
		assert(taux>0 && taux<100);
		return typeDeclarationToString(getPercentileSortByMethods(taux));
	}

	//#9
	//Taux must be a %
	public String  getPercentileSortByFieldsAsString(int taux){
		assert(taux>0 && taux<100);
		return typeDeclarationToString(getPercentileSortByFields(taux));
	}

	//#8 & #9 support

	private ArrayList<TypeDeclaration> getPercentileSortByMethods(int taux){
		return restrain(taux,ClassComparator.comparaisonType.METHODS_NUMBER);
	}
	
	private ArrayList<TypeDeclaration> getPercentileSortByFields(int taux){
		return restrain(taux,ClassComparator.comparaisonType.FIELDS_NUMBER);
	}

	private ArrayList<TypeDeclaration> restrain(int taux,comparaisonType type){
		ArrayList<TypeDeclaration> res=new ArrayList<>();
		ArrayList<TypeDeclaration> sorted=sortBy(type);
		for(int i=sorted.size()-1;i>=sorted.size()-(sorted.size()*taux)/100;i--) {
			res.add(sorted.get(i));			
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<TypeDeclaration> sortBy(comparaisonType type){
		ArrayList<TypeDeclaration> res=(ArrayList<TypeDeclaration>)classAST.clone();
		res.sort(new ClassComparator(type));
		return res;
	}

	
	private String typeDeclarationToString(AbstractCollection<TypeDeclaration> collection) {
		String res="";
		for(TypeDeclaration i: collection){
			res+=" "+i.getName();
		}
		
		return res;
	}
	
	//#10
	//Taux must be a %	
	public String GetPercentileSortByFieldsAndMethodsAsString(int taux){
		assert(taux>0 && taux<100);
		return typeDeclarationToString(GetPercentileSortByFieldsAndMethods(taux));
	}
	

	private ArrayList<TypeDeclaration> GetPercentileSortByFieldsAndMethods(int taux){
		ArrayList<TypeDeclaration> res=new ArrayList<>();
		
		ArrayList<TypeDeclaration> methods=getPercentileSortByMethods(taux);

		for(TypeDeclaration field: getPercentileSortByFields(taux)) {
			if(methods.contains(field)) {
				res.add(field);
			}
		}
		return res;
	}

	
	
	//#11
	public String getClassesByNbMethodMinAsString(int nbMethodMin){
		return typeDeclarationToString(getClassesByNbMethodMin(nbMethodMin));
	}


	
	private ArrayList<TypeDeclaration> getClassesByNbMethodMin(int nbMethodMin){
		ArrayList<TypeDeclaration> res=new ArrayList<>();
		for(TypeDeclaration t :this.classAST) {
			if(t.getMethods().length>nbMethodMin)
				res.add(t);
		}
		return res;
	}


	//#12
	public String  getMethodPercentileSortByLineNumberAsString(int taux){
		assert(taux>0 && taux<100);
		return methodDeclarationToString((restrainMethods(taux)));
	}
	
	private ArrayList<MethodDeclaration> restrainMethods(int taux){
		ArrayList<MethodDeclaration> res=new ArrayList<>();
		ArrayList<MethodDeclaration> sorted=getMethodSortedByNumberOfLines();
		for(int i=sorted.size()-1;i>=sorted.size()-(sorted.size()*taux)/100;i--) {
			res.add(sorted.get(i));
		}
		
		return res;
	}
	
	private ArrayList<MethodDeclaration> getMethodSortedByNumberOfLines() {
		ArrayList<MethodDeclaration> res=new ArrayList<>();
		ArrayList<MethodLineNumberType> ArrayToSort=new ArrayList<>();
		
		for(TypeDeclaration c: classAST) {
			for(MethodDeclaration m : c.getMethods()) {
				LineCounterVisitor lv=new LineCounterVisitor();
				m.accept(lv);
				ArrayToSort.add(new MethodLineNumberType(m,lv.getCounter()));
				
			}
		}
		ArrayToSort.sort(new LineNumberComparator());
		
		for(MethodLineNumberType m : ArrayToSort) {
			res.add(m.getM());
		}
		
		return res;
	}
	
	private String methodDeclarationToString(AbstractCollection<MethodDeclaration> collection) {
		String res="";
		for(MethodDeclaration i: collection){
			res+=" "+i.getName();
		}
		
		return res;
	}
	
	//#13
	public String getMaxParameterMethodAsString() {//To show both the max & the associated Identifier. showing Identifier's class is a bit bothering, maybe later
		MethodDeclaration res=getMaxParamMethod();
		return  "Le plus grand nombre de paramettre pour une méthode est "+res.parameters().size()+" arguments,\n et la première méthode avec ce nombre d'arguments est \""+ res.getName()+"\"";
	}
	
	public MethodDeclaration getMaxParamMethod() {
		MethodDeclaration res=null; //in case we explore something that doesn't have any methods
		
		int max=0;
		int comparedVal;
		for(TypeDeclaration currentClass : classAST) {
			for(MethodDeclaration currentMethod : currentClass.getMethods()) {
				comparedVal=currentMethod.parameters().size();
				if(comparedVal>max) {
					max=comparedVal;
					res=currentMethod;
				}
			}
		}
		
		return res;
	}
	

	//#Call Graph question
	public void fullCallGraph() {
		CallGraphGenerator cgg = new CallGraphGenerator();
		
		for(TypeDeclaration c : this.classAST) {
			for (MethodDeclaration method : c.getMethods()) {

				MethodInvocationVisitor visitor2= new MethodInvocationVisitor();
				method.accept(visitor2);

				for (MethodInvocation methodInvocation : visitor2.getMethods()) {
					Transition toAdd = new Transition (methodInvocation.getName().toString(),method.getName().toString());
					cgg.add(toAdd);
				}
			}
		}
		cgg.generate();
	}

	//others
	public ArrayList<TypeDeclaration> getClassAST() {
		return classAST;
	}
	
	
	@Override
	public String toString(){
		return this.getClassesNames();
	}
	
}

//Just for testing
abstract class a{
	
}
//Just for testing
interface i{
	
}