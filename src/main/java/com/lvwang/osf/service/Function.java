package com.lvwang.osf.service;

public interface Function<T, E> {

    T callBack(E e);
}