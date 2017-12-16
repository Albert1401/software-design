package ru.akirakozov.sd.refactoring.functional;

@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;
}
