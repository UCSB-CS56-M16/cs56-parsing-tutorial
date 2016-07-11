package edu.ucsb.cs56.pconrad.parsing.parser;

import edu.ucsb.cs56.pconrad.parsing.syntax.*;
import edu.ucsb.cs56.pconrad.parsing.tokenizer.*;

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
    public static final Map<Token, Operator> PLUS_MINUS =
	new HashMap<Token, Operator>() {{
	    put(new CharToken('+'), Plus.PLUS);
	    put(new CharToken('-'), Minus.MINUS);
	}};
    public static final Map<Token, Operator> TIMES_DIV =
	new HashMap<Token, Operator>() {{
	    put(new CharToken('*'), Times.TIMES);
	    put(new CharToken('/'), Div.DIV);
	}};
    public static final Token LEFT_PAREN_TOKEN = new CharToken('(');
    public static final Token RIGHT_PAREN_TOKEN = new CharToken(')');
    public static final Token PLUS_TOKEN = new CharToken('+');
    public static final Token MINUS_TOKEN = new CharToken('-');
    public static final Token TIMES_TOKEN = new CharToken('*');
    public static final Token DIV_TOKEN = new CharToken('/');
    // end constants
    
    // begin instance variables
    private final ArrayList<Token> input;
    // end instance varirables

    // This is specifically an ArrayList for constant-time random access
    public Parser(final ArrayList<Token> input) {
	this.input = input;
    }

    private ParseResult<AST> parsePrimary(final int pos) throws ParserException {
	final Token firstToken = tokenAt(pos);

	if (firstToken.equals(LEFT_PAREN_TOKEN)) {
	    final ParseResult<AST> nestedExp = parseExpression(pos + 1);
	    final int nextPos = nestedExp.getNextPos();
	    if (tokenAt(nextPos).equals(RIGHT_PAREN_TOKEN)) {
		return new ParseResult<AST>(nestedExp.getResult(),
					    nextPos + 1);
	    } else {
		throw new ParserException("Expected ')'");
	    }
	} else if (firstToken.equals(MINUS_TOKEN)) {
	    final ParseResult<AST> nestedExp = parsePrimary(pos + 1);
	    return new ParseResult<AST>(new UnaryMinus(nestedExp.getResult()),
					nestedExp.getNextPos());
	} else if (firstToken instanceof IntToken) {
	    return new ParseResult<AST>(new Literal(((IntToken)firstToken).getValue()),
					pos + 1);
	} else {
	    throw new ParserException("Expected primary expression; got: " +
				      firstToken.toString());
	}
    }

    private ParseResult<Operator> parseOperator(final int pos,
						final Map<Token, Operator> operators)
	throws ParserException {
	final Token tokenHere = tokenAt(pos);
	if (operators.containsKey(tokenHere)) {
	    return new ParseResult<Operator>(operators.get(tokenHere),
					     pos + 1);
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

    private Token tokenAt(final int pos) throws ParserException {
	if (pos < 0 || pos >= input.size()) {
	    throw new ParserException("Attempted to get token out of position");
	} else {
	    return input.get(pos);
	}
    }

    public AST parse() throws ParserException {
	final ParseResult<AST> rawResult = parseExpression(0);
	if (rawResult.getNextPos() != input.size()) {
	    throw new ParserException("Extra tokens at the end");
	} else {
	    return rawResult.getResult();
	}
    }
} // Parser
