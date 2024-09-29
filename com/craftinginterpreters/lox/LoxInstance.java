package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

class LoxInstance {
    private LoxClass klass;
    private final Map<String, Object> fields = new HashMap<>();

    LoxInstance(LoxClass klass) {
        this.klass = klass;
        addMethods();
    }

    LoxInstance() {
        if (this instanceof LoxClass) this.klass = (LoxClass)this;
        addMethods();
    }

    Object get(Token name) {
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }

        LoxFunction method = klass.findMethod(name.lexeme);
        if (method != null) return method.bind(this);

        throw new RuntimeError(name, "Undefined property '" + name.lexeme + "'.");
    }

    void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }

    void addMethods() {
        this.fields.put("klass", new LoxCallable() {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return LoxInstance.this.klass;
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        });
    }

    @Override
    public String toString() {
        return klass.name + " instance";
    }
}
