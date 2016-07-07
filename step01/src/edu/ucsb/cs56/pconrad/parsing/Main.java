package edu.ucsb.cs56.pconrad.parsing;

import java.util.Scanner;

public class Main {
    public static void handleInput(final String input) {
	if (new Parser(input).syntacticallyValid()) {
	    System.out.println("Parses");
	} else {
	    System.out.println("Does not parse");
	}
    }
    
    public static void main(String[] args) {
	final Scanner input = new Scanner(System.in);
	while (input.hasNextLine()) {
	    handleInput(input.nextLine());
	}
    }
}
