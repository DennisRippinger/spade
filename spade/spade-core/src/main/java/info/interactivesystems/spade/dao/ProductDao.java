/**
 * Copyright 2014 Dennis Rippinger
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.interactivesystems.spade.dao;

import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.util.Authority;
import info.interactivesystems.spade.util.ProductCategory;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class ProductDao for the {@link Product} entities.
 * 
 * @author Dennis Rippinger
 */
@Repository
@Transactional
public class ProductDao implements GenericDao<Product> {

    @Resource
    private SessionFactory sessionFactory;

    @Override
    public void delete(Product obj) {
        sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public Product find(String id) {
        return (Product) sessionFactory.getCurrentSession().get(Product.class, id);

    }

    @Override
    public void save(Product t) {
        sessionFactory.getCurrentSession().saveOrUpdate(t);
    }

    public Product getProductByURL(String url) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("from Product WHERE source= :url");
        query.setParameter("url", url);

        Product result = (Product) query.uniqueResult();

        return result;
    }

    public Boolean checkIfAlreadyExists(String id) {
        Product find = find(id);
        if (find == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @deprecated temp method to get test related values
     * @return
     */
    @Deprecated
    public List<Product> getYelpVenues() {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("from Product WHERE authority = :authority AND rating > 0 ");
        query.setParameter("authority", Authority.YELP);

        @SuppressWarnings("unchecked")
        List<Product> productList = query.list();

        return productList;
    }

    public Product getRandomProduct(ProductCategory category) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session
            .createSQLQuery("SELECT id FROM Spade.Products WHERE concurrentBit = 0 AND type = :type ORDER BY RAND()");
        query.setParameter("type", category.getId());
        query.setMaxResults(1);

        String id = (String) query.uniqueResult();
        if (id != null) {
            Product randomProduct = find(id);

            return randomProduct;
        }
        return null;

    }

}
