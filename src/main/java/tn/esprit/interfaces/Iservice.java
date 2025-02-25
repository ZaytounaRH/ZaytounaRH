package tn.esprit.interfaces;

import java.util.List;

public interface Iservice<T> {
    void add(T t);
    List<T> getAll();

}

