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

/**
 * The Interface GenericDao. Offers generic methods for all entities.
 * 
 * @param <T> the desired type.
 * @author Dennis Rippinger
 */
public interface GenericDao<T> {

    /**
     * Delete an Object.
     * 
     * @param obj the obj
     */
    void delete(T obj);

    /**
     * Find and object.
     * 
     * @param id the id
     * @return the t
     */
    T find(String id);

    /**
     * Save an object.
     * 
     * @param t the t
     */
    void save(T t);

}