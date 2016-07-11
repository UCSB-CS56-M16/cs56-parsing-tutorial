package edu.ucsb.cs56.pconrad.parsing.syntax;

public class Literal implements AST {
    // begin instance variables
    private final int value;
    // end instance variables

    public Literal(final int value) {
        this.value = value;
    }

    public boolean equals(final Object other) {
        return (other instanceof Literal &&
                ((Literal)other).value == value);
    }

    public int hashCode() {
	return value;
    }

    public String toString() {
        return Integer.toString(value);
    }

    public <A, T extends Throwable> A accept(final ASTVisitor<A, T> visitor) throws T {
	return visitor.visitLiteral(value);
    }
} // Literal
