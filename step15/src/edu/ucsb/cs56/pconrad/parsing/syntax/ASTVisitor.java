package edu.ucsb.cs56.pconrad.parsing.syntax;

public interface ASTVisitor<A, T extends Throwable> {
    public A visitLiteral(int value) throws T;
    public A visitBinop(AST left, Operator op, AST right) throws T;
    public A visitUnaryMinus(AST nested) throws T;
}
