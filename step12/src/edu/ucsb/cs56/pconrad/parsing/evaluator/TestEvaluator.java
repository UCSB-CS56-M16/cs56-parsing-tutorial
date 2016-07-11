package edu.ucsb.cs56.pconrad.parsing.evaluator;

import edu.ucsb.cs56.pconrad.parsing.DefaultInterpreterInterface;
import edu.ucsb.cs56.pconrad.parsing.syntax.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import org.junit.Test;

/**
   Test going from an AST to a int result (by evaluating it)
 */ 


public class TestEvaluator {
    public int evaluate(final AST ast) throws EvaluatorException {
	return DefaultInterpreterInterface.DEFAULT.evaluate(ast);
    }
    
    public int evaluateNoException(final AST ast) {
	int retval = 0;
	boolean retvalSet = false;
	
	try {
	    retval = evaluate(ast);
	    retvalSet = true;
	} catch (EvaluatorException e) {
	    fail("Unexpected evaluator exception: " + e.toString());
	}

	assert(retvalSet);
	return retval;
    }
    
    @Test
    public void testLiteral() {
	assertEquals(42,
		     evaluateNoException(new Literal(42)));
    }

    @Test
    public void testPlus() {
	assertEquals(8,
		     evaluateNoException(new Binop(new Literal(6),
						   Plus.PLUS,
						   new Literal(2))));
    }

    @Test
    public void testDivNonZero() {
	assertEquals(10,
		     evaluateNoException(new Binop(new Literal(30),
						   Div.DIV,
						   new Literal(3))));
    }

    @Test
    public void testUnaryMinusLiteral() {
	assertEquals(-10,
		     evaluateNoException(new UnaryMinus(new Literal(10))));
    }
    
    // BEGIN TESTS INVOLVING DIVISION BY ZERO
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testDivDirectZero() throws EvaluatorException {
	thrown.expect(EvaluatorException.class);
	evaluate(new Binop(new Literal(14),
			   Div.DIV,
			   new Literal(0)));
    }
} // TestAST

