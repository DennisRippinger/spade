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
package info.interactivesystems.spade.sentence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

/**
 * Calculates the information density of a review. The information density is defined by the relation of the size of the GZIP
 * compressed review to its original length.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class InfoDensityIndex {

	/**
	 * Calculates the information density with the GZIP algorithm.
	 *
	 * @param review a review as text.
	 * @return the density of the text.
	 */
	public Double getInformationDensity(String review) {

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			gzip.write(review.getBytes(StandardCharsets.UTF_8));
			gzip.close();

			Double compressed = out.size() * 1.0;
			Double uncompressed = review.getBytes().length * 1.0;

			Double result = compressed / uncompressed;

			if (result > 1.0) {
				result = 1.0;
			}
			return result;

		} catch (IOException e) {
			log.warn("Could not Compress the Input String", e);
		}
		return 0.0;

	}

}
