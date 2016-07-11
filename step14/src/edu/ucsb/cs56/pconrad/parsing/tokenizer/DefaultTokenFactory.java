package edu.ucsb.cs56.pconrad.parsing.tokenizer;

public class DefaultTokenFactory implements TokenFactory {
    // begin constants
    public static final DefaultTokenFactory DEFAULT = new DefaultTokenFactory();
    // end constants
    
    public Token makeLeftParenToken() {
        return new CharToken('(');
    }
    
    public Token makeRightParenToken() {
        return new CharToken(')');
    }
    
    public Token makePlusToken() {
        return new CharToken('+');
    }
    
    public Token makeMinusToken() {
        return new CharToken('-');
    }
    
    public Token makeTimesToken() {
        return new CharToken('*');
    }
    
    public Token makeDivToken() {
        return new CharToken('/');
    }
    
    public Token makeIntToken(final int value) {
        return new IntToken(value);
    }
}
