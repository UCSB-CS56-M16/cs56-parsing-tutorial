package edu.ucsb.cs56.pconrad.parsing;

import edu.ucsb.cs56.pconrad.parsing.parser.*;

import java.util.Scanner;

public class Main {
    public static void handleInput(final String input) {
	try {
	    System.out.println(new Parser(input).parse());
	} catch (ParserException e) {
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
