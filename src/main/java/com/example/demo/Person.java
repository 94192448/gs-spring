package com.example.demo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author yangzq80@gmail.com
 * @date 2020-06-12
 */
@UniqueLogin
public class Person {
    int id;
    String name;

    @NotNull
    @Min(18)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
