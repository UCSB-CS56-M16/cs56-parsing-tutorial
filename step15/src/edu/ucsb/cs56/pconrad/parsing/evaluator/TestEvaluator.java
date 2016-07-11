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
    // begin instance variables
    private final ASTFactory af;
    // end instance variables

    public TestEvaluator() {
        af = DefaultASTFactory.DEFAULT;
    }
    
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
		     evaluateNoException(af.makeLiteral(42)));
    }

    @Test
    public void testPlus() {
	assertEquals(8,
		     evaluateNoException(af.makePlusNode(af.makeLiteral(6),
                                                         af.makeLiteral(2))));
    }

    @Test
    public void testDivNonZero() {
	assertEquals(10,
		     evaluateNoException(af.makeDivNode(af.makeLiteral(30),
                                                        af.makeLiteral(3))));
    }

    @Test
    public void testUnaryMinusLiteral() {
	assertEquals(-10,
		     evaluateNoException(af.makeUnaryMinusNode(af.makeLiteral(10))));
    }
    
    // BEGIN TESTS INVOLVING DIVISION BY ZERO
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testDivDirectZero() throws EvaluatorException {
	thrown.expect(EvaluatorException.class);
	evaluate(af.makeDivNode(af.makeLiteral(14),
                                af.makeLiteral(0)));
    }
} // TestEvaluator

