package edu.ucsb.cs56.pconrad.parsing;

import edu.ucsb.cs56.pconrad.parsing.syntax.AST;
import edu.ucsb.cs56.pconrad.parsing.tokenizer.*;
import edu.ucsb.cs56.pconrad.parsing.parser.*;

import java.util.ArrayList;

public interface InterpreterInterface {
    public ArrayList<Token> tokenize(String input) throws TokenizerException;
    public AST parse(ArrayList<Token> tokens) throws ParserException;
}
