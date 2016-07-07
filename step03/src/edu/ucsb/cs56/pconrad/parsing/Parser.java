package edu.ucsb.cs56.pconrad.parsing;

import java.util.*;

// Simplifications:
//
// 1. We don't separate out tokenization
// 2. We only say if the input parses.  We don't produce an AST.
//
// throwing a ParserException indicates that something
// doesn't parse
public class Parser {
    // begin constants
    public static final Set<Character> PLUS_MINUS =
	setOfChars('+', '-');
    public static final Set<Character> TIMES_DIV =
	setOfChars('*', '/');
    // end constants
    
    // begin instance variables
    private final String input;
    // end instance varirables

    public Parser(final String input) {
	this.input = input;
    }

    public static Set<Character> setOfChars(final char... items) {
	final Set<Character> retval = new HashSet<Character>();
	for (final char item : items) {
	    retval.add(new Character(item));
	}
	return retval;
    }
    
    // returns next position to parse
    private int parseInteger(final int pos) throws ParserException {
	// integers can be preceeded by whitespace, but they cannot
	// be interrupted by whitespace
	final TokenAtResult firstTokenRes = tokenAtSkipWhitespace(pos);

	if (!Character.isDigit(firstTokenRes.getToken())) {
	    throw new ParserException("Expected an integer");
	} else {
	    int curPos = firstTokenRes.getNextPos();
	    // whitespace cannot be present in between digits,
	    // so we do intend to use tokenAt
	    while (curPos < input.length() &&
		   Character.isDigit(tokenAt(curPos))) {
		curPos++;
	    }

	    return curPos;
	}
    } // parseInteger

    private int parsePrimary(final int pos) throws ParserException {
	final TokenAtResult firstTokenRes = tokenAtSkipWhitespace(pos);
	switch (firstTokenRes.getToken()) {
	case '(':
	    final int newPos = parseExpression(firstTokenRes.getNextPos());
	    final TokenAtResult secondTokenRes = tokenAtSkipWhitespace(newPos);
	    if (secondTokenRes.getToken() != ')') {
		throw new ParserException("Expected ')'");
	    } else {
		return secondTokenRes.getNextPos();
	    }
	case '-':
	    return parsePrimary(pos + 1);
	default:
	    return parseInteger(pos);
	}
    }

    private int parseOperator(final int pos, final Set<Character> operators) throws ParserException {
	final TokenAtResult tokenRes = tokenAtSkipWhitespace(pos);
	if (operators.contains(new Character(tokenRes.getToken()))) {
	    return tokenRes.getNextPos();
	} else {
	    throw new ParserException("Expected operator in set: " + operators.toString());
	}
    }

    private int parsePlusMinus(final int pos) throws ParserException {
	return parseOperator(pos, PLUS_MINUS);
    }

    private int parseTimesDiv(final int pos) throws ParserException {
	return parseOperator(pos, TIMES_DIV);
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

    private TokenAtResult tokenAtSkipWhitespace(final int pos) throws ParserException {
	int curPos = pos;
	char candidate;

	do {
	    candidate = tokenAt(curPos);
	    curPos++;
	} while (Character.isWhitespace(candidate));

	return new TokenAtResult(candidate, curPos);
    }

    private char tokenAt(final int pos) throws ParserException {
	if (pos < 0 || pos >= input.length()) {
	    throw new ParserException("Attempted to get token out of position");
	} else {
	    return input.charAt(pos);
	}
    }

    private boolean allTrailingWhitespaceFrom(final int pos) {
	for (int curPos = pos; curPos < input.length(); curPos++) {
	    if (!Character.isWhitespace(input.charAt(curPos))) {
		return false;
	    }
	}
	return true;
    }
	       
    public boolean syntacticallyValid() {
	boolean retval = true;
	// we always just skip whitespace, so for simplicity it gets
	// stripped out entirely
	try {
	    final int lastPos = parseExpression(0);
	    // ensure we parsed the whole string
	    retval = allTrailingWhitespaceFrom(lastPos);
	} catch (ParserException e) {
	    // throwing an exception indicates it didn't parse
	    retval = false;
	}
	return retval;
    }
} // Parser
