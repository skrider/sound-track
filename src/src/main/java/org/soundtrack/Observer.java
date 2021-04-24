package org.soundtrack;

public interface Observer<T> {
    void update(T t);
}
