package edu.ucsb.cs56.pconrad.parsing.syntax;

public class Minus extends OperatorScaffold {
    // begin constants
    public static final Minus MINUS = new Minus();
    // end constants
    
    public Minus() {
        super('-');
    }

    public int evaluate(final int left, final int right)
	throws EvaluatorException {
	return left - right;
    }
} // Minus
