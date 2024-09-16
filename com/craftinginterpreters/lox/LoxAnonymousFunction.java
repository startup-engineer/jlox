package com.craftinginterpreters.lox;

import java.util.List;

class LoxAnonymousFunction implements LoxCallable {
    private final List<Token> params;
    private final List<Stmt> body;
    private final Environment closure;

    public LoxAnonymousFunction(List<Token> params, List<Stmt> body, Environment closure) {
        this.params = params;
        this.closure = closure;
        this.body = body;
    }

    @Override
    public int arity() {
        return params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);

        for (int i = 0; i < params.size(); i++) {
            environment.define(params.get(i).lexeme, arguments.get(i));
        }

        try {
            interpreter.executeBlock(body, environment);
        } catch (Return returnValue) {
            return returnValue.value;
        }
        return null;
    }

    @Override
    public String toString() {
        return "<fn anonymous>";
    }
}
