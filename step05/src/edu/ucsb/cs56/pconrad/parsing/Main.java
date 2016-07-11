package edu.ucsb.cs56.pconrad.parsing;

import edu.ucsb.cs56.pconrad.parsing.parser.*;

import java.util.Scanner;

public class Main {
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
                System.out.println(new Parser(input).parse());
            } catch (ParserException e) {
                System.out.println("Does not parse");
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
