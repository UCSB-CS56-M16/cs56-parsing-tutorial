package edu.ucsb.cs56.pconrad.parsing.tokenizer;

// Instead of passing along the tokens themselves, we just
// pass along what the tokens held directly.
public interface TokenVisitor<A, T extends Throwable> {
    public A visitIntToken(int value) throws T;
    public A visitCharToken(char value) throws T;
}
