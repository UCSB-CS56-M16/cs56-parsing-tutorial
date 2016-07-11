package edu.ucsb.cs56.pconrad.parsing.tokenizer;

import java.util.*;

public class IntTokenizerState implements TokenizerState {
    // begin instance variables
    private final ArrayList<Integer> digits;
    private InitialTokenizerState initialState;
    // end instance variables

    public IntTokenizerState(final char startDigit,
			     final InitialTokenizerState initialState) {
	digits = new ArrayList<Integer>();
	addDigit(startDigit);
	this.initialState = initialState;
    }

    protected void addDigit(final char digit) {
	assert(Character.isDigit(digit));
	digits.add(new Integer(toDigit(digit)));
    }
    
    public static int toDigit(final char c) {
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

    public static int digitsToInt(final ArrayList<Integer> digits) {
	int retval = 0;
	int multiplier = 1;
	for(int x = digits.size() - 1; x >= 0; x--) {
	    retval += digits.get(x) * multiplier;
	    multiplier *= 10;
	}
	return retval;
    }

    private List<Token> makeBaseRetvalTokens() {
	final List<Token> retvalTokens = new ArrayList<Token>();
	retvalTokens.add(new IntToken(digitsToInt(digits)));
	return retvalTokens;
    }
    
    public TokenizerStateResult nextState(final char curChar)
	throws TokenizerException {
	if (Character.isDigit(curChar)) {
	    addDigit(curChar);
	    return TokenizerStateResult.justState(this);
	} else {
	    final List<Token> retvalTokens = makeBaseRetvalTokens();
	    final TokenizerStateResult forward = initialState.nextState(curChar);
	    retvalTokens.addAll(forward.getTokens());
	    return new TokenizerStateResult(retvalTokens, forward.getNextState());
	}
    }

    public List<Token> atInputEnd() throws TokenizerException {
	return makeBaseRetvalTokens();
    }
} // TokenizerState

