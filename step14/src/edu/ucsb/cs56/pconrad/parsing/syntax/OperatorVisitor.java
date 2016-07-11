package edu.ucsb.cs56.pconrad.parsing.syntax;

public interface OperatorVisitor<A, T extends Throwable> {
    public A visitPlus() throws T;
    public A visitMinus() throws T;
    public A visitTimes() throws T;
    public A visitDiv() throws T;
}
