package edu.ucsb.cs56.pconrad.parsing.parser;

import edu.ucsb.cs56.pconrad.parsing.syntax.*;

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
    public static final Map<Character, Operator> PLUS_MINUS =
	new HashMap<Character, Operator>() {{
	    put(new Character('+'), Plus.PLUS);
	    put(new Character('-'), Minus.MINUS);
	}};
    public static final Map<Character, Operator> TIMES_DIV =
	new HashMap<Character, Operator>() {{
	    put(new Character('*'), Times.TIMES);
	    put(new Character('/'), Div.DIV);
	}};
    // end constants
    
    // begin instance variables
    private final String input;
    // end instance varirables

    public Parser(final String input) {
	this.input = input;
    }

    public static int digitsToInt(ArrayList<Integer> digits) {
	int retval = 0;
	int multiplier = 1;
	for(int x = digits.size() - 1; x >= 0; x--) {
	    retval += digits.get(x) * multiplier;
	    multiplier *= 10;
	}
	return retval;
    }

    public static int toDigit(char c) {
	assert(Character.isDigit(c));
	switch (c) {
	case '0':
	    return 0;
	case '1':
	    return 1;
	case '2':
	    return 2;
	case '3':
	    return 3;
	case '4':
	    return 4;
	case '5':
	    return 5;
	case '6':
	    return 6;
	case '7':
	    return 7;
	case '8':
	    return 8;
	case '9':
	    return 9;
	default:
	    assert(false);
	    return -1;
	}
    }
    
    // returns next position to parse
    private ParseResult<Integer> parseInteger(final int pos) throws ParserException {
	final ArrayList<Integer> digits = new ArrayList<Integer>();
	
	// integers can be preceeded by whitespace, but they cannot
	// be interrupted by whitespace
	final ParseResult<Character> firstTokenRes = tokenAtSkipWhitespace(pos);
	final char firstDigit = firstTokenRes.getResult().charValue();
	if (!Character.isDigit(firstDigit)) {
	    throw new ParserException("Expected an integer");
	} else {
	    digits.add(new Integer(toDigit(firstDigit)));
	    int curPos = firstTokenRes.getNextPos();
	    boolean shouldRun = true;
	    
	    // whitespace cannot be present in between digits,
	    // so we do intend to use tokenAt
	    while (curPos < input.length() && shouldRun) {
		final char curToken = tokenAt(curPos);
		if (Character.isDigit(curToken)) {
		    digits.add(new Integer(toDigit(curToken)));
		    curPos++;
		} else {
		    shouldRun = false;
		}
	    }

	    return new ParseResult<Integer>(new Integer(digitsToInt(digits)),
					    curPos);
	}
    } // parseInteger

    private ParseResult<AST> parsePrimary(final int pos) throws ParserException {
	final ParseResult<Character> firstTokenRes =
	    tokenAtSkipWhitespace(pos);
	switch (firstTokenRes.getResult().charValue()) {
	case '(':
	    final ParseResult<AST> nestedExp = parseExpression(firstTokenRes.getNextPos());
	    final ParseResult<Character> secondTokenRes = tokenAtSkipWhitespace(nestedExp.getNextPos());
	    if (secondTokenRes.getResult().charValue() != ')') {
		throw new ParserException("Expected ')'");
	    } else {
		return new ParseResult<AST>(nestedExp.getResult(),
					    secondTokenRes.getNextPos());
	    }
	case '-':
	    final ParseResult<AST> negated = parsePrimary(firstTokenRes.getNextPos());
	    return new ParseResult<AST>(new UnaryMinus(negated.getResult()),
					negated.getNextPos());
	default:
	    final ParseResult<Integer> theInteger = parseInteger(pos);
	    return new ParseResult<AST>(new Literal(theInteger.getResult().intValue()),
					theInteger.getNextPos());
	}
    }

    private ParseResult<Operator> parseOperator(final int pos,
						final Map<Character, Operator> operators)
	throws ParserException {
	final ParseResult<Character> tokenRes = tokenAtSkipWhitespace(pos);
	final Character asChar = new Character(tokenRes.getResult().charValue());
	if (operators.containsKey(asChar)) {
	    return new ParseResult<Operator>(operators.get(asChar),
					     tokenRes.getNextPos());
	} else {
	    throw new ParserException("Expected operator in set: " +
				      operators.keySet().toString());
	}
    }

    private ParseResult<Operator> parsePlusMinus(final int pos) throws ParserException {
	return parseOperator(pos, PLUS_MINUS);
    }

    private ParseResult<Operator> parseTimesDiv(final int pos) throws ParserException {
	return parseOperator(pos, TIMES_DIV);
    }

    // BEGIN CODE FOR MULTIPLICATIVE AND ADDITIVE EXPRESSIONS
    // these are defined as inner classes in order to get access
    // to methods on the parser itself (e.g., parsePlusMinus, etc.)
    private class ParseAdditive extends ParseAdditiveOrMultiplicative {
	public ParseResult<AST> parseBase(final int pos) throws ParserException {
	    return parseMultiplicativeExpression(pos);
	}
	public ParseResult<Operator> parseOp(final int pos) throws ParserException {
	    return parsePlusMinus(pos);
	}
    }

    private class ParseMultiplicative extends ParseAdditiveOrMultiplicative {
	public ParseResult<AST> parseBase(final int pos) throws ParserException {
	    return parsePrimary(pos);
	}
	public ParseResult<Operator> parseOp(final int pos) throws ParserException {
	    return parseTimesDiv(pos);
	}
    }

    // because the above two classes hold no state and don't have useful
    // constructors, we can simply allocate these ahead of time and use
    // them throughout
    private final ParseAdditive PARSE_ADDITIVE = new ParseAdditive();
    private final ParseMultiplicative PARSE_MULTIPLICATIVE = new ParseMultiplicative();
    // END CODE FOR MULIPLICATIVE AND ADDITIVE EXPRESSIONS
    
    private ParseResult<AST> parseMultiplicativeExpression(final int pos)
	throws ParserException {
	return PARSE_MULTIPLICATIVE.parseExp(pos);
    }

    private ParseResult<AST> parseAdditiveExpression(final int pos)
	throws ParserException {
	return PARSE_ADDITIVE.parseExp(pos);
    }

    private ParseResult<AST> parseExpression(final int pos) throws ParserException {
	return parseAdditiveExpression(pos);
    }

    private ParseResult<Character> tokenAtSkipWhitespace(final int pos) throws ParserException {
	int curPos = pos;
	char candidate;

	do {
	    candidate = tokenAt(curPos);
	    curPos++;
	} while (Character.isWhitespace(candidate));

	return new ParseResult<Character>(new Character(candidate),
					  curPos);
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

    public AST parse() throws ParserException {
	final ParseResult<AST> rawResult = parseExpression(0);
	if (!allTrailingWhitespaceFrom(rawResult.getNextPos())) {
	    throw new ParserException("Extra tokens at the end");
	} else {
	    return rawResult.getResult();
	}
    }
} // Parser
