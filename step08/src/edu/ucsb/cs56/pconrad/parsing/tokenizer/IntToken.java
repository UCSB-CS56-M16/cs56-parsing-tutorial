package edu.ucsb.cs56.pconrad.parsing.tokenizer;

public class IntToken implements Token {
    // begin instance variables
    private final int value;
    // end instance variables

    public IntToken(final int value) {
        this.value = value;
    }

    public boolean equals(final Object other) {
        // intentionally don't go through intTokenValue to avoid
        // spurious checked exception error
        return (other instanceof IntToken &&
                ((IntToken)other).value == value);
    }

    public int hashCode() {
        return value;
    }

    public String toString() {
        return Integer.toString(value);
    }

    public int getValue() {
	return value;
    }
} // IntToken
