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

import info.interactivesystems.spade.entities.ShadowReview;

/**
 * The Class ShadowReviewDao for the {@link ShadowReview} entities.
 * 
 * @author Dennis Rippinger
 */
public class ShadowReviewDao extends DaoHelper implements
		GenericDao<ShadowReview> {

	@Override
	public void delete(ShadowReview obj) {
		helperDeletion(obj);
	}

	@Override
	public ShadowReview find(String id) {
		return helperFind(id, ShadowReview.class);
	}

	@Override
	public void save(ShadowReview t) {
		helperSave(t);
	}

}
