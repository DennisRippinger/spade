///**
// * Copyright 2014 Dennis Rippinger
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *    http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package info.interactivesystems.spade.calculation;
//
//import info.interactivesystems.spade.dao.ReviewDao;
//import info.interactivesystems.spade.dao.SentenceFrequencyDao;
//import info.interactivesystems.spade.entities.Review;
//import info.interactivesystems.spade.entities.SentenceFrequency;
//import info.interactivesystems.spade.nlp.SentenceDetector;
//import info.interactivesystems.spade.util.HashUtil;
//import info.interactivesystems.spade.util.ProductCategory;
//
//import java.io.UnsupportedEncodingException;
//import java.security.NoSuchAlgorithmException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import javax.annotation.Resource;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.stereotype.Service;
//
///**
// * The Class SentenceFrequencyAggregator.
// * 
// * @author Dennis Rippinger
// */
//@Slf4j
//@Service
//public class SentenceFrequencyAggregator {
//
//    @Resource
//    private ReviewDao reviewDao;
//
//    @Resource
//    private SentenceFrequencyDao frequencyDao;
//
//    @Resource
//    private SentenceDetector sentenceDetector;
//
//    public void aggregateSentenceFrequency(ProductCategory productCategory) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        List<Review> reviews = reviewDao.getReviewsOfCategory(productCategory);
//
//        Map<String, Integer> frequencyTable = new HashMap<String, Integer>();
//        Integer sentenceCounter = 0;
//
//        for (Review review : reviews) {
//
//            List<String> sentences = sentenceDetector.detectSentencesFromCorpus(review.getContent());
//            for (String sentence : sentences) {
//                if (frequencyTable.containsKey(sentence)) {
//                    Integer counter = frequencyTable.get(sentence);
//                    counter++;
//                    frequencyTable.put(sentence, counter);
//                } else {
//                    frequencyTable.put(sentence, 1);
//                }
//                sentenceCounter++;
//            }
//        }
//
//        for (Entry<String, Integer> wordFrequency : frequencyTable.entrySet()) {
//            Integer count = wordFrequency.getValue();
//            Double frequency = (count * 1.0) / (sentenceCounter * 1.0);
//
//            SentenceFrequency frequencyObject = new SentenceFrequency();
//            String hash = HashUtil.sha1(wordFrequency.getKey());
//            frequencyObject.setId(hash);
//            frequencyObject.setCategory(productCategory.getId());
//            frequencyObject.setFrequency(frequency);
//            frequencyObject.setSentence(wordFrequency.getKey());
//            frequencyObject.setCount(count);
//
//            frequencyDao.save(frequencyObject);
//        }
//
//        log.info("Found '{}' distinct sentences", sentenceCounter);
//    }
//}
