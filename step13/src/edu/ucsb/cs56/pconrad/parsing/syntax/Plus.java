package edu.ucsb.cs56.pconrad.parsing.syntax;

public class Plus extends OperatorScaffold {
    // begin constants
    public static final Plus PLUS = new Plus();
    // end constants
    
    public Plus() {
        super('+');
    }

    public <A, T extends Throwable> A accept(final OperatorVisitor<A, T> visitor) throws T {
	return visitor.visitPlus();
    }
} // Plus
