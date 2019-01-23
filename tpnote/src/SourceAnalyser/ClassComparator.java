package SourceAnalyser;

import java.util.Comparator;



import org.eclipse.jdt.core.dom.*;

public class ClassComparator implements Comparator<TypeDeclaration> {

	public enum comparaisonType{
		METHODS_NUMBER,
		FIELDS_NUMBER
	}
	
	private comparaisonType t;
	
	public ClassComparator(comparaisonType type) {
		this.t=type;
	}
	
	@Override
	public int compare(TypeDeclaration arg0, TypeDeclaration arg1) {
		switch(this.t) {
			case METHODS_NUMBER:
				return Integer.compare(arg0.getMethods().length,arg1.getMethods().length);
				
			//case FIELDS_NUMBER://Default by fields.
			default:
				return Integer.compare(arg0.getFields().length,arg1.getFields().length);

				
		}

	}

	
}
