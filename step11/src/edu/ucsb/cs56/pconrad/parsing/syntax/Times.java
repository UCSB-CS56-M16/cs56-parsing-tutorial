package edu.ucsb.cs56.pconrad.parsing.syntax;

public class Times extends OperatorScaffold {
    // begin constants
    public static final Times TIMES = new Times();
    // end constants
    
    public Times() {
        super('*');
    }

    public int evaluate(final int left, final int right)
	throws EvaluatorException {
	return left * right;
    }
} // Times
