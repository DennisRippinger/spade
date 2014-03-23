package info.interactivesystems.spade.dao;

import java.util.List;

public interface GenericDao<T> {

	void delete(T obj);

	T find(String id);

	void save(T t);

	List<T> findAll();

	Integer countAll();

}