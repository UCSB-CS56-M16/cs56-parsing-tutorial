package edu.ucsb.cs56.pconrad.parsing;

import edu.ucsb.cs56.pconrad.parsing.syntax.AST;
import edu.ucsb.cs56.pconrad.parsing.tokenizer.*;
import edu.ucsb.cs56.pconrad.parsing.parser.*;

import java.util.Scanner;

public class Main {
    public static AST parse(final String input)
	throws TokenizerException, ParserException {
	return new Parser(new Tokenizer(input).tokenize()).parse();
    }

    /**
     * Returns true if it should exit
     */
    public static boolean shouldExit(final String input) {
        final String trimmed = input.trim();
        return trimmed.equals("q") || trimmed.equals("quit");
    }
    
    /**
     * Returns true if it should exit
     */
    public static boolean handleInput(final String input) {
        if (shouldExit(input)) {
            return true;
        } else {
            try {
                System.out.println(parse(input));
            } catch (TokenizerException e) {
                System.out.println("Failed to tokenize: " + e.getMessage());
            } catch (ParserException e) {
                System.out.println("Failed to parse: " + e.getMessage());
            }
            return false;
        }
    }
    
    public static void main(String[] args) {
	final Scanner input = new Scanner(System.in);
	while (input.hasNextLine() &&
               !handleInput(input.nextLine())) {}
    }
}
