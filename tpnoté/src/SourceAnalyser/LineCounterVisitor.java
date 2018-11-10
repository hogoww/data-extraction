package SourceAnalyser;

import org.eclipse.jdt.core.dom.*;

public class LineCounterVisitor extends ASTVisitor {
	private int counter;
	public LineCounterVisitor() {
		super();
		counter=0;
	}

	public LineCounterVisitor(boolean visitDocTags) {
		super(visitDocTags);
		counter=0;
	}
	
	public int getCounter() {
		return this.counter;
	}

	/*
	 * in the following, we either increment the counter and return false, because the statement is atomic (such as break)
	 * Or return true because some nested statement are possibly inside it (such as for)
	 * We also removed empty statements, Not considering them as real statement here.
	 * */
	public boolean visit(BreakStatement node) {
		counter++;
		return false;
	}

	public boolean visit(Assignment node) {
		counter++;
		return false;
	}

	public boolean visit(ContinueStatement node) {
		counter++;
		return true;
	}

	public boolean visit(DoStatement node) {
		counter++;
		return true;
	}

	public boolean visit(EnhancedForStatement node) {
		counter++;
		return true;
	}
	
	public boolean visit(ExpressionStatement node) {
		counter++;
		return true;
	}

	public boolean visit(ForStatement node) {
		counter++;
		return true;
	}

	public boolean visit(IfStatement node) {
		counter++;
		return true;
	}

	public boolean visit(LabeledStatement node) {
		counter++;
		return false;
	}
	
	public boolean visit(ReturnStatement node) {
		counter++;
		return true;
	}
	
	public boolean visit(ThrowStatement node) {
		counter++;
		return true;
	}

	public boolean visit(TryStatement node) {
		counter++;
		return true;
	}
	
	public boolean visit(WhileStatement node) {
		counter++;
		return true;
	}
	
	public boolean visit(VariableDeclarationStatement node) {
		counter++;
		return false;
	}
}
