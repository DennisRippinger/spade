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
package info.interactivesystems.spade.ui.action;

import info.interactivesystems.spade.dao.NilsimsaSimilarityDao;
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.exception.SpadeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.hibernate.loader.hql.QueryLoader;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class SimilarityBean.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Named
public class SimilarityBean {

	@Resource
	private NilsimsaSimilarityDao similarityDao;

	private List<NilsimsaSimilarity> similarities;

	private List<String> blacklist;

	private Integer internalWindow = 1;

	/**
	 * Init the similarity bean.
	 *
	 * @throws SpadeException if the bean can't be initialized.
	 */
	@PostConstruct
	public void init() throws SpadeException {

		log.info("Init Similarity Bean");

		try {
			initializeBlacklist();

			similarities = new LinkedList<>();

			queryPagination();

		} catch (Exception e) {
			log.error("Could not initialize similarity List");
			throw new SpadeException("Could not initialize Similarity Bean", e);
		}

	}

	/**
	 * Gets the similarity or adds new items to the collection.
	 *
	 * @param id the id
	 * @return the similarity
	 */
	public NilsimsaSimilarity getSimilarity(Integer id) {
		if (id >= similarities.size()) {
			internalWindow++;
			queryPagination();
		}

		return similarities.get(id);
	}

	private void queryPagination() {
		List<NilsimsaSimilarity> tempResult = similarityDao.find(0.9, false, internalWindow);
		Integer logCounter = 0;

		for (NilsimsaSimilarity similarity : tempResult) {
			if (!blacklist.contains(similarity.getReviewA().getId())) {
				similarities.add(similarity);
				logCounter++;
			}
		}

		log.info("Added '{}' new items to collection", logCounter);
	}

	private void initializeBlacklist() throws Exception {
		URL urlResource = QueryLoader.class.getResource("/Blacklist.txt");

		FileSystemManager fsManager = VFS.getManager();
		FileObject file = fsManager.resolveFile(urlResource.toURI().toString());
		InputStream inputStream = file.getContent().getInputStream();

		blacklist = IOUtils.readLines(inputStream);
	}

}
