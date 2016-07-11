package edu.ucsb.cs56.pconrad.parsing.tokenizer;

public class CharToken implements Token {
    // begin instance variables
    private final char c;
    // end instance variables
    
    public CharToken(final char c) {
        this.c = c;
    }

    public boolean equals(final Object other) {
        // intentionally don't go through charTokenValue to avoid
        // spurious checked exception error
        return (other instanceof CharToken &&
                ((CharToken)other).c == c);
    }

    public int hashCode() {
        return (int)c;
    }

    public String toString() {
        return Character.toString(c);
    }
} // CharToken
