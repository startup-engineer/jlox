package com.craftinginterpreters.lox;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

class LoxClass extends LoxInstance implements LoxCallable {
    final String name;
    private final Map<String, LoxFunction> methods;

    LoxClass(String name, Map<String, LoxFunction> methods) {
        super(new LoxClass(name + "Metaclass", new HashMap<String, LoxFunction>(), Interpreter.metaclass));
        this.name = name;
        this.methods = methods;
    }

    LoxClass(String name, Map<String, LoxFunction> methods, LoxClass klass) {
        super(klass);
        this.name = name;
        this.methods = methods;
    }

    LoxClass() {
        this.name = "Class";
        this.methods = new HashMap<String, LoxFunction>();
    }

    LoxFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }

        return null;
    }

    public void addMethod(String name, LoxFunction function) {
        methods.put(name, function);
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        LoxInstance instance = new LoxInstance(this);
        LoxFunction initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }

        return instance;
    }

    @Override
    public int arity() {
        LoxFunction initializer = findMethod("init");
        if (initializer == null) return 0;
        return initializer.arity();
    }

    @Override
    public String toString() {
        return name;
    }
}
