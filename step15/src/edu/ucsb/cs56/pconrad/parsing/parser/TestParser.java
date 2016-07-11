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
    // begin instance variables
    private final ASTFactory af;
    // end instance variables

    public TestParser() {
        af = DefaultASTFactory.DEFAULT;
    }
    
    public static AST parse(final String input)
	throws TokenizerException, ParserException {
	return DEFAULT.tokenizeAndParse(input);
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
        assertEquals(af.makeLiteral(42),
                     parseNoException("42"));
    }

    @Test
    public void testParseAdd() {
        assertEquals(af.makePlusNode(af.makeLiteral(1),
                                     af.makeLiteral(2)),
                     parseNoException("1 + 2"));
    }

    @Test
    public void testParseParensLiteral() {
        assertEquals(af.makeLiteral(1),
                     parseNoException("(1)"));
    }
    
    @Test
    public void testParseParensBinop() {
        assertEquals(af.makePlusNode(af.makeLiteral(1),
                                     af.makeLiteral(2)),
                     parseNoException("(1 + 2)"));
    }

    @Test
    public void testPrecedenceHigherFirst() {
        assertEquals(af.makePlusNode(af.makeTimesNode(af.makeLiteral(1),
                                                      af.makeLiteral(2)),
                                     af.makeLiteral(3)),
                     parseNoException("1 * 2 + 3"));
    }

    @Test
    public void testPrecedenceParensApplyRight() {
        assertEquals(af.makeTimesNode(af.makeLiteral(1),
                                      af.makePlusNode(af.makeLiteral(2),
                                                      af.makeLiteral(3))),
                     parseNoException("1 * (2 + 3)"));
    }

    @Test
    public void testUnaryMinusLiteral() {
	assertEquals(af.makeUnaryMinusNode(af.makeLiteral(42)),
		     parseNoException("-42"));
    }

    @Test
    public void testUnaryMinusNonMinusBinop() {
	assertEquals(af.makePlusNode(af.makeLiteral(2),
                                     af.makeUnaryMinusNode(af.makeLiteral(5))),
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

