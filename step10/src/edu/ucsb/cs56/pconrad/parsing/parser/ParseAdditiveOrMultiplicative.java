package edu.ucsb.cs56.pconrad.parsing.parser;

import edu.ucsb.cs56.pconrad.parsing.syntax.*;

// Observation: the only differences between parseMultiplicateExpression
// and parseAdditiveExpression are:
// 1. which method is called internally to parse the nested expressions
// 2. which method is called internally to parse the operator in play
//
// As such, we factor out this commonality into a class that
// treats these above two methods as abstract, and implements
// the functionality in terms of these abstract methods.
public abstract class ParseAdditiveOrMultiplicative {
    public abstract ParseResult<AST> parseBase(int pos) throws ParserException;
    public abstract ParseResult<Operator> parseOp(int pos) throws ParserException;
    public ParseResult<AST> parseExp(final int pos) throws ParserException {
	ParseResult<AST> curResult = parseBase(pos);
	boolean shouldRun = true;

	while (shouldRun) {
	    try {
		final ParseResult<Operator> opResult = parseOp(curResult.getNextPos());
		final ParseResult<AST> nextBaseResult = parseBase(opResult.getNextPos());
		curResult = new ParseResult<AST>(new Binop(curResult.getResult(),
							   opResult.getResult(),
							   nextBaseResult.getResult()),
						 nextBaseResult.getNextPos());
	    } catch (ParserException e) {
		shouldRun = false;
	    }
	}

	return curResult;
    }
}
