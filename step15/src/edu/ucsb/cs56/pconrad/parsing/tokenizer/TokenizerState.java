package edu.ucsb.cs56.pconrad.parsing.tokenizer;

import java.util.List;

public interface TokenizerState {
    public TokenizerStateResult nextState(char curChar) throws TokenizerException;
    public List<Token> atInputEnd() throws TokenizerException;
}
