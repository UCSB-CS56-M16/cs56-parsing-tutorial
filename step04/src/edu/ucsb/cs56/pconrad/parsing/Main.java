package edu.ucsb.cs56.pconrad.parsing;

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
            if (new Parser(input).syntacticallyValid()) {
                System.out.println("Parses");
            } else {
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
