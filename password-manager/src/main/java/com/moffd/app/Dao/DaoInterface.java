package com.moffd.app.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DaoInterface<T> {
    public T findById(int id);

    public List<T> findAll();

    public T update(T dto);

    public T create(T dto);

    public void delete(int id);
}
