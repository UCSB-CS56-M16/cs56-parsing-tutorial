package edu.ucsb.cs56.pconrad.parsing.syntax;

public class UnaryMinus implements AST {
    // begin instance variables
    private final AST nested;
    // end instance variables

    public UnaryMinus(final AST nested) {
	this.nested = nested;
    }

    public boolean equals(final Object other) {
	return (other instanceof UnaryMinus &&
		((UnaryMinus)other).nested.equals(nested));
    }

    public int hashCode() {
	return 1 + nested.hashCode();
    }

    public String toString() {
	return "-" + nested.toString();
    }

    public <A, T extends Throwable> A accept(final ASTVisitor<A, T> visitor) throws T {
	return visitor.visitUnaryMinus(nested);
    }
} // UnaryMinus

