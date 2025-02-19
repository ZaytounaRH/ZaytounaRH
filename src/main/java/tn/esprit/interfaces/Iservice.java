package tn.esprit.interfaces;

import java.util.List;

public interface Iservice<T> {
    void add(T t);
    List<T> getAll();
   void update(T t);
   void remove(int id);
}

