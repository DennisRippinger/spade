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
package info.interactivesystems.spade.importer;

import info.interactivesystems.spade.dto.CombinedData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.*;

/**
 * The Class AmazonImport.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Service
public class AmazonImport {

	@Resource
	private ImportDataProducer producer;

	@Resource
	private ImportDataConsumer consumer;

	public void importAmazonDataset(File amazondataset) throws InterruptedException, ExecutionException {

		LinkedBlockingQueue<CombinedData> queue = new LinkedBlockingQueue<>(500);

		Integer reviewCounter = 1;
		Boolean hasMore = true;


		producer.setAmazonDataset(amazondataset);
		ImportDataProducer.setQueue(queue);
		producer.setReviewCounter(reviewCounter);
		producer.setHasMore(hasMore);

		consumer.setReviewCounter(reviewCounter);
		ImportDataConsumer.setQueue(queue);
		producer.setHasMore(hasMore);

		log.info("Starting Consumer und Producer Jobs");
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(consumer);
		Future<Boolean> submit = executor.submit(producer);

		submit.get();

	}

}
