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

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Product;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * The Class AmazonCategoryImport.
 * 
 * @author Dennis Rippinger
 */
@Slf4j
@Service
public class AmazonCategoryImport {

    private final File categoryFrequencies = new File("categoryFrequencies.csv");

    @Resource
    private ReviewContentService contentService;

    private Map<String, Integer> categories = new HashMap<String, Integer>();

    private Long logCounter = 0l;

    public void importAmazonCategorySet(File amazondataset) {
        Boolean idGathered = false;

        try (BufferedReader br = new BufferedReader(new FileReader(amazondataset))) {
            Product product = null;
            for (String line; (line = br.readLine()) != null;) {
                if (logCounter <= 4810641) {
                    logCounter++;
                    continue;
                }

                if (line.startsWith("  ") && idGathered) {

                    String category = extractProductCategory(line);

                    incrementCategoryCount(category);

                    if (product != null) {
                        product.setCategory(category);
                        contentService.saveProduct(product);
                    }

                    idGathered = false;
                } else if (!line.startsWith("  ") && !idGathered) {
                    product = contentService.findProduct(line);
                    idGathered = true;
                }

                // Reduce output noise
                logCounter++;
                Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
                if (rand == 500) {
                    log.info("Log Counter ID '{}'", logCounter);
                }
            }

        } catch (FileNotFoundException e) {
            log.error("Could not find file", e);
        } catch (IOException e) {
            log.error("Error reading input file", e);
        }

        generateCsvFile();
    }

    private String extractProductCategory(String line) {
        String category = line.trim();
        Integer index = category.indexOf(',');
        index = index.equals(-1) ? category.length() : index;

        category = category.substring(0, index);

        return category;
    }

    private void generateCsvFile() {
        try (FileWriter writer = new FileWriter(categoryFrequencies)) {
            for (Entry<String, Integer> rating : categories.entrySet()) {
                writer.append(rating.getKey());
                writer.append('\t');
                writer.append(rating.getValue().toString());
                writer.append('\n');
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error("Could not write CSV File", e);
        }
    }

    private void incrementCategoryCount(String category) {
        Integer frequency = categories.get(category);
        if (frequency != null) {
            frequency++;
            categories.put(category, frequency);

            return;
        }
        categories.put(category, 1);
    }

}
