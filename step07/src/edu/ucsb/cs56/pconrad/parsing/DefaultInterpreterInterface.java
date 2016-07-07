package edu.ucsb.cs56.pconrad.parsing;

import edu.ucsb.cs56.pconrad.parsing.syntax.AST;
import edu.ucsb.cs56.pconrad.parsing.tokenizer.*;
import edu.ucsb.cs56.pconrad.parsing.parser.*;

import java.util.ArrayList;

public class DefaultInterpreterInterface implements InterpreterInterface {
    public static final DefaultInterpreterInterface DEFAULT =
	new DefaultInterpreterInterface();
    
    public ArrayList<Token> tokenize(final String input) throws TokenizerException {
	return new Tokenizer(input).tokenize();
    }
    
    public AST parse(final ArrayList<Token> tokens) throws ParserException {
	return new Parser(tokens).parse();
    }
} // DefaultInterpreterInterface

