package edu.ucsb.cs56.pconrad.parsing.tokenizer;

import java.util.*;

public class Tokenizer {
    // begin instance variables
    private final String input;
    private final Set<Character> recognizedTokens;
    // end instance variables

    public Tokenizer(final String input,
		     final Set<Character> recognizedTokens) {
	this.input = input;
	this.recognizedTokens = recognizedTokens;
    }
    
    public ArrayList<Token> tokenize() throws TokenizerException {
	final ArrayList<Token> tokens = new ArrayList<Token>();
	TokenizerState state = new InitialTokenizerState(recognizedTokens);
	for(int pos = 0; pos < input.length(); pos++) {
	    final TokenizerStateResult cur = state.nextState(input.charAt(pos));
	    tokens.addAll(cur.getTokens());
	    state = cur.getNextState();
	}
	tokens.addAll(state.atInputEnd());
	return tokens;
    }
} // Tokenizer
