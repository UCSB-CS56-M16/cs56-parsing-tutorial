package edu.ucsb.cs56.pconrad.parsing.parser;

import edu.ucsb.cs56.pconrad.parsing.tokenizer.*;
import edu.ucsb.cs56.pconrad.parsing.syntax.*;

import static edu.ucsb.cs56.pconrad.parsing.DefaultInterpreterInterface.DEFAULT;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.io.IOException;

/**
 Tests going from a string to an AST
 */ 

public class TestParser {
    public static AST parse(final String input)
	throws TokenizerException, ParserException {
	return DEFAULT.parse(DEFAULT.tokenize(input));
    }

    public static AST parseNoException(final String input) {
        AST retval = null;

        try {
            retval = parse(input);
	} catch (TokenizerException e) {
	    fail("Unexpected tokenizer exception: " + e.toString());
        } catch (ParserException e) {
            fail("Unexpected parse exception: " + e.toString());
        }

        assert(retval != null);
        return retval;
    }
    
    @Test
    public void testParseNum() {
        assertEquals(new Literal(42),
                     parseNoException("42"));
    }

    @Test
    public void testParseAdd() {
        assertEquals(new Binop(new Literal(1),
                               Plus.PLUS,
                               new Literal(2)),
                     parseNoException("1 + 2"));
    }

    @Test
    public void testParseParensLiteral() {
        assertEquals(new Literal(1),
                     parseNoException("(1)"));
    }
    
    @Test
    public void testParseParensBinop() {
        assertEquals(new Binop(new Literal(1),
                               Plus.PLUS,
                               new Literal(2)),
                     parseNoException("(1 + 2)"));
    }

    @Test
    public void testPrecedenceHigherFirst() {
        assertEquals(new Binop(new Binop(new Literal(1),
                                         Times.TIMES,
                                         new Literal(2)),
                               Plus.PLUS,
                               new Literal(3)),
                     parseNoException("1 * 2 + 3"));
    }

    @Test
    public void testPrecedenceParensApplyRight() {
        assertEquals(new Binop(new Literal(1),
                               Times.TIMES,
                               new Binop(new Literal(2),
                                         Plus.PLUS,
                                         new Literal(3))),
                     parseNoException("1 * (2 + 3)"));
    }

    @Test
    public void testUnaryMinusLiteral() {
	assertEquals(new UnaryMinus(new Literal(42)),
		     parseNoException("-42"));
    }

    @Test
    public void testUnaryMinusNonMinusBinop() {
	assertEquals(new Binop(new Literal(2),
			       Plus.PLUS,
			       new UnaryMinus(new Literal(5))),
		     parseNoException("2 + -5"));
    }

    // BEGIN TESTS FOR INVALID INPUTS
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public AST parseExpectFailure(String input)
        throws TokenizerException, ParserException {
        thrown.expect(ParserException.class);
        return parse(input);
    }

    @Test
    public void testInvalidDoubleNumber()
        throws TokenizerException, ParserException {
        parseExpectFailure("5 6");
    }

    @Test
    public void testMissingSecondOperand() 
        throws TokenizerException, ParserException {
        parseExpectFailure("5 +");
    }
    
    @Test
    public void testMissingSecondOperandInParens() 
        throws TokenizerException, ParserException {
        parseExpectFailure("(5 +)");
    }
} // TestParser

