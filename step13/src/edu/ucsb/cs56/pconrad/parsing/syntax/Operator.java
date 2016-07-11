package edu.ucsb.cs56.pconrad.parsing.syntax;

public interface Operator {
    public boolean equals(Object other);
    public int hashCode();
    public String toString();

    public <A, T extends Throwable> A accept(OperatorVisitor<A, T> visitor) throws T;
}
