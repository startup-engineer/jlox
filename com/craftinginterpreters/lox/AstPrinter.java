package com.craftinginterpreters.lox;

class AstPrinter implements Expr.Visitor<String> {

    public String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal literal) {
        if (literal.value == null) {
            return "nil";
        }
        return literal.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary unary) {
        return parenthesize(unary.operator.lexeme, unary.right);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary binary) {
        return parenthesize(binary.operator.lexeme, binary.left, binary.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping grouping) {
        return parenthesize("group", grouping.expression);
    }

    public String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(print(expr));
        }
        builder.append(")");

        return builder.toString();
    }
}
