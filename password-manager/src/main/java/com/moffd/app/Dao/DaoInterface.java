package com.moffd.app.Dao;

import java.sql.ResultSet;
import java.util.List;

public interface DaoInterface<T> {
    public T findById(int id);

    public List<T> findAll();

    public T update(T dto);

    public T create(T dto);

    public void delete(int id);

    /**
     * Creates a Java DTO from the given result set
     * @param rs A result set object containing the info needed to create the DTO
     * @return The DTO associated with the result set
     */
    public T recreateDTO(ResultSet rs);
}
