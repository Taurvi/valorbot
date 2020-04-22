package com.github.taurvi.valor.repositories;

import java.util.List;

public interface ValorRepository<I> {
    void create(I entity);

    I read(String index);

    void update(I entity);

    I delete(I entity);

    List<I> listAll();
}
