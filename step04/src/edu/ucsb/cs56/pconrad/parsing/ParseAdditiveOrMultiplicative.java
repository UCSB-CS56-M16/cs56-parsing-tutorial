package edu.ucsb.cs56.pconrad.parsing;

// Observation: the only differences between parseMultiplicateExpression
// and parseAdditiveExpression are:
// 1. which method is called internally to parse the nested expressions
// 2. which method is called internally to parse the operator in play
//
// As such, we factor out this commonality into a class that
// treats these above two methods as abstract, and implements
// the functionality in terms of these abstract methods.
public abstract class ParseAdditiveOrMultiplicative {
    public abstract int parseBase(int pos) throws ParserException;
    public abstract int parseOp(int pos) throws ParserException;
    public int parseExp(final int pos) throws ParserException {
	int curPos = parseBase(pos);
	boolean shouldRun = true;
	while (shouldRun) {
	    try {
		final int afterOp = parseOp(curPos);
		curPos = parseBase(afterOp);
	    } catch (ParserException e) {
		shouldRun = false;
	    }
	}
	return curPos;
    }
}
