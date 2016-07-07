package edu.ucsb.cs56.pconrad.parsing;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestParser {
    public boolean parses(final String input) {
	return new Parser(input).syntacticallyValid();
    }
    
    @Test
    public void testParseNum() {
	assertTrue(parses("42"));
    }

    @Test
    public void testParseAdd() {
	assertTrue(parses("1 + 2"));
    }

    @Test
    public void testParseParensLiteral() {
	assertTrue(parses("(1)"));
    }
    
    @Test
    public void testParseParensBinop() {
	assertTrue(parses("(1 + 2)"));
    }

    @Test
    public void testPrecedenceHigherFirst() {
	assertTrue(parses("1 * 2 + 3"));
    }

    @Test
    public void testPrecedenceParensApplyRight() {
	assertTrue(parses("1 * (2 + 3)"));
    }

    @Test
    public void testUnaryMinusLiteral() {
	assertTrue(parses("-42"));
    }

    @Test
    public void testUnaryMinusNonMinusBinop() {
	assertTrue(parses("2 + -5"));
    }

    @Test
    public void testInvalidDoubleNumber() {
	assertTrue(!parses("5 6"));
    }

    @Test
    public void testMissingSecondOperand() {
	assertTrue(!parses("5 +"));
    }

    @Test
    public void testMissingSecondOperandInParens() {
	assertTrue(!parses("(5 +)"));
    }
} // TestParser
