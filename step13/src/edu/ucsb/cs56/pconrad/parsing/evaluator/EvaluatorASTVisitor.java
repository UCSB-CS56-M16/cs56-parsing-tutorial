package edu.ucsb.cs56.pconrad.parsing.evaluator;

import edu.ucsb.cs56.pconrad.parsing.syntax.*;

public class EvaluatorASTVisitor implements ASTVisitor<Integer, EvaluatorException> {
    // begin constants
    // no state, so we can just allocate once
    public static final EvaluatorASTVisitor VISITOR = new EvaluatorASTVisitor();
    // end constants
    
    public static int evaluate(final AST expression) throws EvaluatorException {
	return expression.accept(VISITOR);
    }
    
    public Integer visitLiteral(final int value) throws EvaluatorException {
	return new Integer(value);
    }

    public Integer visitBinop(final AST left,
			      final Operator operator,
			      final AST right)
	throws EvaluatorException  {
	return operator.accept(new EvaluatorOperatorVisitor(evaluate(left),
							    evaluate(right)));
    }

    public Integer visitUnaryMinus(final AST nested)
	throws EvaluatorException {
	return new Integer(-evaluate(nested));
    }
} // EvaluatorASTVisitor
