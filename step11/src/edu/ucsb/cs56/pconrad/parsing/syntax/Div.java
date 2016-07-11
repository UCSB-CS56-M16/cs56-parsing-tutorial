package edu.ucsb.cs56.pconrad.parsing.syntax;

public class Div extends OperatorScaffold {
    // begin constants
    public static final Div DIV = new Div();
    // end constants
    
    public Div() {
        super('/');
    }

    public int evaluate(final int left, final int right)
	throws EvaluatorException {
	if (right == 0) {
	    throw new EvaluatorException("Division by zero");
	} else {
	    return left / right;
	}
    }
} // Div
