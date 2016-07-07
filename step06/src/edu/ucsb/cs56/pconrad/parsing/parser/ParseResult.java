package edu.ucsb.cs56.pconrad.parsing.parser;

public class ParseResult<A> {
    // begin instance variables
    private final A result;
    private final int nextPos;
    // end instance variables

    public ParseResult(final A result, final int nextPos) {
	this.result = result;
	this.nextPos = nextPos;
    }

    public A getResult() { return result; }
    public int getNextPos() { return nextPos; }
} // ParseResult

