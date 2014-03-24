package info.interactivesystems.spade.similarity;

import info.interactivesystems.spade.entities.SimilartyMesurement;

import org.springframework.stereotype.Service;

/**
 * Calculates the Levenshtein Distance for two Strings. This measurement is suitable for small changes in individual sentences,
 * i.e. typing errors.
 * 
 * @author Dennis Rippinger
 * @see http://en.wikipedia.org/wiki/Levenshtein_distance
 */
@Service
public class LevenshteinDistance implements SimilarityCalculator {

    /**
     * Calculates the Levenshtein distance for two given Strings. The shorter the distance, the more similar the sentences. This
     * is useful for small changes in individual sentences, i.e. typing errors.
     * 
     * Code from: http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/ Levenshtein_distance#Java
     */
    @Override
    public SimilartyMesurement calculateSimilarity(String corpusFirst,
        String corpusSecond) {

        Integer len0 = corpusFirst.length() + 1;
        Integer len1 = corpusSecond.length() + 1;

        // the array of distances
        Integer[] cost = new Integer[len0];
        Integer[] newcost = new Integer[len0];

        // initial cost of skipping prefix in String corpusFirst
        for (Integer i = 0; i < len0; i++)
            cost[i] = i;

        for (Integer j = 1; j < len1; j++) {

            // initial cost of skipping prefix in String corpusSecond
            newcost[0] = j - 1;

            // transformation cost for each letter in corpusFirst
            for (Integer i = 1; i < len0; i++) {

                // matching current letters in both strings
                Integer match = (corpusFirst.charAt(i - 1) == corpusSecond
                    .charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                Integer cost_replace = cost[i - 1] + match;
                Integer cost_insert = cost[i] + 1;
                Integer cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete),
                    cost_replace);
            }

            // swap cost/newcost arrays
            Integer[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        Double value = cost[len0 - 1] * 1.0;

        SimilartyMesurement result = new SimilartyMesurement();

        result.setSimilarty(value);

        return result;
    }

}
