package edu.ucsb.cs56.pconrad.parsing.tokenizer;

import java.util.*;

public class ArithmeticTokenizer extends Tokenizer {
    // begin constants
    public static final Set<Character> ARITHMETIC_TOKENS =
	setOfChar('(', ')', '+', '-', '*', '/');
    // end constants

    public ArithmeticTokenizer(final String input) {
	super(input, ARITHMETIC_TOKENS);
    }
    
    public static Set<Character> setOfChar(char... chars) {
	final Set<Character> retval = new HashSet<Character>();
	for (final char c : chars) {
	    retval.add(new Character(c));
	}
	return retval;
    }
}
