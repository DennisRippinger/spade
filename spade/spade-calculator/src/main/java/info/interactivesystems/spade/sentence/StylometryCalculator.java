package info.interactivesystems.spade.sentence;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.Stylometry;
import info.interactivesystems.spade.nlp.SentenceDetector;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class StylometryCalculator {

    @Resource
    private ReviewContentService service;

    @Resource
    private SentenceDetector sentenceDetector;

    public void calculateStylometryValues(Review review) {
        Stylometry stylometry = new Stylometry();

        // Restructure
//        stylometry.setAri(review.getAri());
//        stylometry.setGfi(review.getGfi());
//        stylometry.setDensity(review.getDensity());

        List<String> sentences = getSentences(review.getContent());
        List<String> words = getWords(sentences);

        stylometry.setWordLevel(getWordLevel(words));

        stylometry.setDigits(getDigits(review.getContent(), 1));
        stylometry.setDigitBigrams(getDigits(review.getContent(), 2));
        stylometry.setDigitTrigrams(getDigits(review.getContent(), 3));
        stylometry.setDollarOccurence(getSpecialCharacter('@', review.getContent()));
        stylometry.setExclamationMarkOccurence(getSpecialCharacter('!', review.getContent()));
        stylometry.setQuestionmarkOccurence(getSpecialCharacter('?', review.getContent()));
        stylometry.setCommaOccurence(getSpecialCharacter(',', review.getContent()));

        review.setStylometry(stylometry);

        service.saveReview(review);
    }

    private Double getSpecialCharacter(char character, String review) {
        String patter = String.format("[%s]{1}", character);
        Pattern charPattern = Pattern.compile(patter);

        Double result = 0.0;
        Matcher matcher = charPattern.matcher(review);
        while (matcher.find()) {
            result++;
        }

        result /= review.length();

        return result;
    }

    private Double getDigits(String review, Integer times) {
        String patter = String.format("[ ][0-9]{%s}[ ]", times);
        Pattern digitPattern = Pattern.compile(patter);

        Double result = 0.0;
        Matcher matcher = digitPattern.matcher(review);
        while (matcher.find()) {
            result++;
        }

        result /= review.length();

        return result;
    }

    private Double getWordLevel(List<String> words) {
        Double result = 0.0;
        for (String word : words) {
            result += word.length();
        }
        result /= words.size();

        return result;
    }

    private List<String> getSentences(String review) {
        return sentenceDetector.detectSentencesFromCorpus(review);
    }

    private List<String> getWords(List<String> sentences) {
        List<String> result = new LinkedList<>();

        for (String sentence : sentences) {
            String[] split = sentence.split(" ");
            result.addAll(Arrays.asList(split));
        }

        return result;
    }
}
