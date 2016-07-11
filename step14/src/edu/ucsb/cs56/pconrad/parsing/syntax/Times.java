package edu.ucsb.cs56.pconrad.parsing.syntax;

public class Times extends OperatorScaffold {
    // begin constants
    public static final Times TIMES = new Times();
    // end constants
    
    public Times() {
        super('*');
    }

    public <A, T extends Throwable> A accept(final OperatorVisitor<A, T> visitor) throws T {
	return visitor.visitTimes();
    }
} // Times
