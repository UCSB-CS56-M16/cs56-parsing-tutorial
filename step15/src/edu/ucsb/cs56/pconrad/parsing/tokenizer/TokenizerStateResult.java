package edu.ucsb.cs56.pconrad.parsing.tokenizer;

import java.util.*;

public class TokenizerStateResult {
    // begin constants
    public static final List<Token> EMPTY_LIST =
	Collections.<Token>emptyList();
    // end constants

    // begin instance variables
    private final List<Token> tokens;
    private final TokenizerState nextState;
    // end instance variables

    public TokenizerStateResult(final List<Token> tokens,
				final TokenizerState nextState) {
	this.tokens = tokens;
	this.nextState = nextState;
    }

    public List<Token> getTokens() { return tokens; }
    public TokenizerState getNextState() { return nextState; }

    public static TokenizerStateResult justState(final TokenizerState nextState) {
	return new TokenizerStateResult(EMPTY_LIST, nextState);
    }
}
