package info.interactivesystems.spade.dao;

import info.interactivesystems.spade.entities.Product;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * @author Dennis Rippinger
 * 
 */
@Repository
public class ProductDao extends DaoHelper implements GenericDao<Product> {

	@Override
	public void delete(Product obj) {
		helperDeletion(obj);
	}

	@Override
	public Product find(String id) {
		return helperFind(id, Product.class);
	}

	@Override
	public void save(Product t) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
