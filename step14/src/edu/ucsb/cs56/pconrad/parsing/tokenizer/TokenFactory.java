package edu.ucsb.cs56.pconrad.parsing.tokenizer;

/**
 * Decouples token constructors and underlying class hierarchy from
 * how tokens behave.  Only used in the test suite.
 */
public interface TokenFactory {
    public Token makeLeftParenToken();
    public Token makeRightParenToken();
    public Token makePlusToken();
    public Token makeMinusToken();
    public Token makeTimesToken();
    public Token makeDivToken();
    public Token makeIntToken(int value);
}
