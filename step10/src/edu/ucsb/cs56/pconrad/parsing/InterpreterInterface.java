package edu.ucsb.cs56.pconrad.parsing;

import edu.ucsb.cs56.pconrad.parsing.syntax.AST;
import edu.ucsb.cs56.pconrad.parsing.tokenizer.*;
import edu.ucsb.cs56.pconrad.parsing.parser.*;

import java.util.ArrayList;

public abstract class InterpreterInterface {
    public abstract ArrayList<Token> tokenize(String input) throws TokenizerException;
    public abstract AST parse(ArrayList<Token> tokens) throws ParserException;

    public AST tokenizeAndParse(final String input)
	throws TokenizerException, ParserException {
	return parse(tokenize(input));
    }
}
