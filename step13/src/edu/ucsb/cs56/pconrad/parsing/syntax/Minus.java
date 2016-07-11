package edu.ucsb.cs56.pconrad.parsing.syntax;

public class Minus extends OperatorScaffold {
    // begin constants
    public static final Minus MINUS = new Minus();
    // end constants
    
    public Minus() {
        super('-');
    }

    public <A, T extends Throwable> A accept(final OperatorVisitor<A, T> visitor) throws T {
	return visitor.visitMinus();
    }
} // Minus
