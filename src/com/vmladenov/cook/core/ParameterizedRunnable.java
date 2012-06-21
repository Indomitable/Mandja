package com.vmladenov.cook.core;

public abstract class ParameterizedRunnable<T> implements Runnable {
    private final T parameter;

    public ParameterizedRunnable(T parameter) {
        this.parameter = parameter;
    }

    @Override
    public void run() {
        ParameterizedRun(parameter);
    }

    public abstract void ParameterizedRun(T parameter);
}
