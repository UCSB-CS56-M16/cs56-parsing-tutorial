package edu.ucsb.cs56.pconrad.parsing.syntax;

public class Plus extends OperatorScaffold {
    // begin constants
    public static final Plus PLUS = new Plus();
    // end constants
    
    public Plus() {
        super('+');
    }

    public int evaluate(final int left, final int right)
	throws EvaluatorException {
	return left + right;
    }
} // Plus
