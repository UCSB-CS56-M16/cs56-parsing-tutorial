package edu.ucsb.cs56.pconrad.parsing.tokenizer;

public interface Token {
    public boolean equals(Object other);
    public int hashCode();
    public String toString();

    public <A, T extends Throwable> A accept(TokenVisitor<A, T> visitor) throws T;
}
