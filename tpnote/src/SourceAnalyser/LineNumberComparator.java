package SourceAnalyser;

import java.util.Comparator;


public class LineNumberComparator implements Comparator<MethodLineNumberType> {

	@Override
	public int compare(MethodLineNumberType arg0, MethodLineNumberType arg1) {
		return Integer.compare(arg0.getLineNb(),arg1.getLineNb());
		
	}
}
