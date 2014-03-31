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

import javax.annotation.Resource;

import info.interactivesystems.spade.entities.Product;

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

}
