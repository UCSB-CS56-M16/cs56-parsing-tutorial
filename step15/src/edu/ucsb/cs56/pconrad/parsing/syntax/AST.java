package edu.ucsb.cs56.pconrad.parsing.syntax;

public interface AST {
    public boolean equals(Object other);
    public int hashCode();
    public String toString();

    public <A, T extends Throwable> A accept(ASTVisitor<A, T> visitor) throws T;
}
