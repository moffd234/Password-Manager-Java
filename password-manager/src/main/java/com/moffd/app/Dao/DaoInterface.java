package com.moffd.app.Dao;

import java.sql.SQLException;
import java.util.List;

public interface DaoInterface<T> {
    public T findById(int id);

    public List<T> findAll();

    public T update(T dto);

    public T create(T dto) throws SQLException;

    public void delete(int id);
}
