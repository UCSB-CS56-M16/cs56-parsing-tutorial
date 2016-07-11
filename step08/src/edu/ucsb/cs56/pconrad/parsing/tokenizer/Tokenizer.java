package edu.ucsb.cs56.pconrad.parsing.tokenizer;

import java.util.*;

public class Tokenizer {
    // begin instance variables
    private final String input;
    private final Set<Character> recognizedTokens;
    private int inputPos;
    // end instance variables

    public Tokenizer(final String input,
		     final Set<Character> recognizedTokens) {
	this.input = input;
	this.recognizedTokens = recognizedTokens;
	inputPos = 0;
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

    public IntToken makeIntToken(final char firstDigit) {
	assert(Character.isDigit(firstDigit));
	final ArrayList<Integer> digits = new ArrayList<Integer>();
	digits.add(new Integer(toDigit(firstDigit)));

	boolean shouldRun = true;

	while (inputPos < input.length() && shouldRun) {
	    final char curChar = input.charAt(inputPos);
	    if (Character.isDigit(curChar)) {
		digits.add(new Integer(toDigit(curChar)));
		inputPos++;
	    } else {
		shouldRun = false;
	    }
	}

	return new IntToken(digitsToInt(digits));
    }

    public ArrayList<Token> tokenize() throws TokenizerException {
	final ArrayList<Token> tokens = new ArrayList<Token>();
	
	while (inputPos < input.length()) {
	    final char charHere = input.charAt(inputPos);
	    if (recognizedTokens.contains(new Character(charHere))) {
		tokens.add(new CharToken(charHere));
		inputPos++;
	    } else if (Character.isDigit(charHere)) {
		inputPos++;
		tokens.add(makeIntToken(charHere));
		// makeIntToken increments inputPos internally
	    } else if (Character.isWhitespace(charHere)) {
		inputPos++;
	    } else {
		throw new TokenizerException("Bad token: " + charHere);
	    }
	}

	return tokens;
    }
} // Tokenizer
