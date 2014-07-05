/**
 * 
 */
package info.interactivesystems.spade.importer;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Product;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * @author Dennis Rippinger
 * 
 */
@Slf4j
@Component
public class GenericAvgRatingExport {

    private final Integer MAXSIZE = 2441053;

    private final File ratingFrequencies = new File("ratingFrequencies.csv");

    private final DecimalFormat df = new DecimalFormat("#.#");

    private Map<String, Integer> ratings = new HashMap<String, Integer>();

    @Resource
    private ReviewContentService service;

    public void exportAvgRating() {

        for (Long count = 1l; count <= MAXSIZE; count++) {
            Product currentProduct = service.findByID(count);
            String rating = df.format(currentProduct.getRating());
            incrementRatingCount(rating);

            Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
            if (rand == 500) {
                log.info("Current Item ID '{}'", count);
            }
        }

        generateCsvFile();
    }

    private void generateCsvFile() {
        try (FileWriter writer = new FileWriter(ratingFrequencies)) {
            for (Entry<String, Integer> rating : ratings.entrySet()) {
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

    private void incrementRatingCount(String rating) {
        Integer frequency = ratings.get(rating);
        if (frequency != null) {
            frequency++;
            ratings.put(rating, frequency);

            return;
        }
        ratings.put(rating, 1);

    }
}
