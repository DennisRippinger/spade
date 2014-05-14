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
package info.interactivesystems.spade.util;

import info.interactivesystems.spade.dto.Nilsimsa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import lombok.extern.slf4j.Slf4j;

/**
 * Loading the entire set of unique reviews into a collection is way to huge. Even when the retrieved fields are limited to id
 * and Nilsimsa hash. It appears that Spring Data aggregates a lot more space than are required in the end.
 * 
 * Therefore a first step via CSV import is done.
 * 
 * @author Dennis Rippinger
 * 
 */
@Slf4j
public class NilsimsaCsvImport {

    public List<Nilsimsa> getReviewsFromCSV(String csvFile) {
        final List<Nilsimsa> result = new ArrayList<Nilsimsa>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");

                Nilsimsa nilsimsa = new Nilsimsa();
                nilsimsa.setId(split[0].replace("\"", ""));
                nilsimsa.setNilsimsa(split[1].replace("\"", ""));

                result.add(nilsimsa);

                // Reduce output noise
                Integer rand = ThreadLocalRandom.current().nextInt(1, 20000);
                if (rand == 300) {
                    log.info("Current Item ID '{}'", nilsimsa.getId());
                }
            }
        } catch (IOException e) {
            log.error("Could not load CSV file");
        }

        return result;

    }

}
