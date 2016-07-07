package edu.ucsb.cs56.pconrad.parsing;

// When getting the next token, we generally want to skip
// over whitespace.  As such, we need to get both the non-whitespace
// token here, along with the new position to look at.
// For example, with:
//
// "  3"
//
// ...we'd want to return the token `3`, but the next position would
// be two positions from the start, because we want to skip over the
// whitespace.
//
public class TokenAtResult {
    // begin instance variables
    private final char token;
    private final int nextPos;
    // end instance variables

    public TokenAtResult(final char token, final int nextPos) {
	this.token = token;
	this.nextPos = nextPos;
    }

    public char getToken() { return token; }
    public int getNextPos() { return nextPos; }
} // TokenAtResult
