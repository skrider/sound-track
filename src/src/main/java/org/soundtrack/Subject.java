package org.soundtrack;

public interface Subject<T> {
    void check(T t);
    void addObserver(Observer<T> o);
}
