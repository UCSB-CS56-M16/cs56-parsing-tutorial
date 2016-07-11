package edu.ucsb.cs56.pconrad.parsing.tokenizer;

import java.util.*;

public class InitialTokenizerState implements TokenizerState {
    // begin instance variables
    private final Set<Character> recognizedTokens;
    // end instance variables
    
    public InitialTokenizerState(final Set<Character> recognizedTokens) {
	this.recognizedTokens = recognizedTokens;
    }

    public TokenizerStateResult nextState(final char curChar)
	throws TokenizerException {
	if (recognizedTokens.contains(new Character(curChar))) {
	    final List<Token> retvalTokens = new ArrayList<Token>();
	    retvalTokens.add(new CharToken(curChar));
	    return new TokenizerStateResult(retvalTokens, this);
	} else if (Character.isDigit(curChar)) {
	    return TokenizerStateResult.justState(new IntTokenizerState(curChar, this));
	} else if (Character.isWhitespace(curChar)) {
	    return TokenizerStateResult.justState(this);
	} else {
	    throw new TokenizerException("Bad tokens: " + curChar);
	}
    }

    public List<Token> atInputEnd() throws TokenizerException {
	return TokenizerStateResult.EMPTY_LIST;
    }
} // InitialTokenizerState
