package edu.ucsb.cs56.pconrad.parsing;

// Simplifications:
//
// 1. We don't separate out tokenization
// 2. We only say if the input parses.  We don't produce an AST.
//
// throwing a ParserException indicates that something
// doesn't parse
public class Parser {
    // begin instance variables
    private final String input;
    // end instance varirables

    public Parser(final String input) {
	this.input = stripWhitespace(input);
    }

    // returns next position to parse
    private int parseInteger(final int pos) throws ParserException {
	int curPos = pos;

	while (curPos < input.length() &&
	       Character.isDigit(tokenAt(curPos))) {
	    curPos++;
	}

	// ensure we parsed something
	if (curPos == pos) {
	    throw new ParserException("Expected an integer");
	} else {
	    return curPos;
	}
    }

    private int parsePrimary(final int pos) throws ParserException {
	switch (tokenAt(pos)) {
	case '(':
	    final int newPos = parseExpression(pos + 1);
	    if (tokenAt(newPos) != ')') {
		throw new ParserException("Expected ')'");
	    } else {
		return newPos + 1;
	    }
	case '-':
	    return parsePrimary(pos + 1);
	default:
	    return parseInteger(pos);
	}
    }

    private int parsePlusMinus(final int pos) throws ParserException {
	switch (tokenAt(pos)) {
	case '+':
	case '-':
	    return pos + 1;
	default:
	    throw new ParserException("Expected '+' or '-'");
	}
    }

    private int parseTimesDiv(final int pos) throws ParserException {
	switch (tokenAt(pos)) {
	case '*':
	case '/':
	    return pos + 1;
	default:
	    throw new ParserException("Expected '*' or '/'");
	}
    }
    
    private int parseMultiplicativeExpression(final int pos) throws ParserException {
	int curPos = parsePrimary(pos);
	boolean shouldRun = true;
	while (shouldRun) {
	    try {
		final int afterOp = parseTimesDiv(curPos);
		curPos = parsePrimary(afterOp);
	    } catch (ParserException e) {
		shouldRun = false;
	    }
	}
	return curPos;
    }

    private int parseAdditiveExpression(final int pos) throws ParserException {
	int curPos = parseMultiplicativeExpression(pos);
	boolean shouldRun = true;
	while (shouldRun) {
	    try {
		final int afterOp = parsePlusMinus(curPos);
		curPos = parseMultiplicativeExpression(afterOp);
	    } catch (ParserException e) {
		shouldRun = false;
	    }
	}
	return curPos;
    }

    private int parseExpression(final int pos) throws ParserException {
	return parseAdditiveExpression(pos);
    }
    
    private char tokenAt(final int pos) throws ParserException {
	if (pos < 0 || pos >= input.length()) {
	    throw new ParserException("Attempted to get token out of position");
	} else {
	    return input.charAt(pos);
	}
    }

    public static String stripWhitespace(final String input) {
	return input.replaceAll("\\s+", "");
    }

    public boolean syntacticallyValid() {
	boolean retval = true;
	// we always just skip whitespace, so for simplicity it gets
	// stripped out entirely
	try {
	    final int lastPos = parseExpression(0);
	    // ensure we parsed the whole string
	    retval = lastPos == input.length();
	} catch (ParserException e) {
	    // throwing an exception indicates it didn't parse
	    retval = false;
	}
	return retval;
    }
} // Parser
