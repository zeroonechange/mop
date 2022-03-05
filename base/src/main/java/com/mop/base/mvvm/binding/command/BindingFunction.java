package com.mop.base.mvvm.binding.command;

public interface BindingFunction<T, R> {

    R apply(T t);
}