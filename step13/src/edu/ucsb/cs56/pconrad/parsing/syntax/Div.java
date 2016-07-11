package edu.ucsb.cs56.pconrad.parsing.syntax;

public class Div extends OperatorScaffold {
    // begin constants
    public static final Div DIV = new Div();
    // end constants
    
    public Div() {
        super('/');
    }

    public <A, T extends Throwable> A accept(final OperatorVisitor<A, T> visitor) throws T {
	return visitor.visitDiv();
    }
} // Div
