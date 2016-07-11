package edu.ucsb.cs56.pconrad.parsing.evaluator;

import edu.ucsb.cs56.pconrad.parsing.syntax.*;

public class EvaluatorOperatorVisitor implements OperatorVisitor<Integer, EvaluatorException> {
    // begin instance variables
    private final int left;
    private final int right;
    // end instance variables

    public EvaluatorOperatorVisitor(final int left, final int right) {
	this.left = left;
	this.right = right;
    }
    
    public Integer visitPlus() throws EvaluatorException {
	return new Integer(left + right);
    }

    public Integer visitMinus() throws EvaluatorException {
	return new Integer(left - right);
    }

    public Integer visitTimes() throws EvaluatorException {
	return new Integer(left * right);
    }

    public Integer visitDiv() throws EvaluatorException {
	if (right == 0) {
	    throw new EvaluatorException("Division by zero");
	} else {
	    return new Integer(left / right);
	}
    }
} // EvaluatorOperatorVisitor
