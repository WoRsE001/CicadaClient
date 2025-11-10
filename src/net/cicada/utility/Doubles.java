package net.cicada.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class Doubles<T, E>{
    T t;
    E e;

    public void setValues(T t, E e) {
        this.t = t;
        this.e = e;
    }
}
