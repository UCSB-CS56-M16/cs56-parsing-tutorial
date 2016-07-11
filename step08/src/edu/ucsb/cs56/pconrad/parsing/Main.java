package edu.ucsb.cs56.pconrad.parsing;

import edu.ucsb.cs56.pconrad.parsing.syntax.AST;
import edu.ucsb.cs56.pconrad.parsing.tokenizer.*;
import edu.ucsb.cs56.pconrad.parsing.parser.*;

import java.util.Scanner;

public class Main {
    public static AST parse(final String input)
	throws TokenizerException, ParserException {
	return DefaultInterpreterInterface.DEFAULT.tokenizeAndParse(input);
    }
    
    public static void handleInput(final String input) {
	try {
	    System.out.println(parse(input));
	} catch (TokenizerException e) {
	    System.out.println("Failed to tokenize: " + e.getMessage());
	} catch (ParserException e) {
	    System.out.println("Failed to parse: " + e.getMessage());
	}
    }
    
    public static void main(String[] args) {
	final Scanner input = new Scanner(System.in);
	while (input.hasNextLine()) {
	    handleInput(input.nextLine());
	}
    }
}
