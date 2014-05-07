//package info.interactivesystems.spade.dao;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import info.interactivesystems.spade.entities.SentenceSimilarity;
//
//import javax.annotation.Resource;
//
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Test;
//
//@Test(groups = { "functionTest" })
//@ContextConfiguration(locations = { "classpath:beans.xml" })
//public class SentenceSimilarityDaoTest extends AbstractTestNGSpringContextTests {
//
//    @Resource
//    private SentenceSimilarityDao sentenceSimilarityDao;
//
//    private SentenceSimilarity demoValue;
//
//    @BeforeTest
//    private void getDemoSentenceSimilarity() {
//        SentenceSimilarity result = new SentenceSimilarity();
//        result.setId(1l);
//        result.setReferenceReviewId("1234");
//        result.setTargetReviewId("5678");
//        result.setReferenceSentence("testSentence one");
//        result.setTargetSentence("target sentence");
//        result.setSimilarity(0.45);
//        result.setWeightendSimilarity(2.3);
//
//        demoValue = result;
//    }
//
//    @Test
//    public void delete() {
//        sentenceSimilarityDao.save(demoValue);
//        SentenceSimilarity found = sentenceSimilarityDao.find(demoValue.getId());
//
//        assertThat(found).isNotNull();
//        assertThat(found).isEqualsToByComparingFields(demoValue);
//
//        sentenceSimilarityDao.delete(demoValue);
//
//        found = sentenceSimilarityDao.find(found.getId());
//        assertThat(found).isNull();
//    }
//
//    @Test
//    public void find() {
//        sentenceSimilarityDao.save(demoValue);
//        SentenceSimilarity found = sentenceSimilarityDao.find(demoValue.getId());
//
//        assertThat(found).isNotNull();
//        assertThat(found).isEqualsToByComparingFields(demoValue);
//
//        sentenceSimilarityDao.delete(demoValue);
//
//        found = sentenceSimilarityDao.find(found.getId());
//        assertThat(found).isNull();
//    }
//
//    @Test
//    public void save() {
//        sentenceSimilarityDao.save(demoValue);
//        SentenceSimilarity found = sentenceSimilarityDao.find(demoValue.getId());
//
//        assertThat(found).isNotNull();
//        assertThat(found).isEqualsToByComparingFields(demoValue);
//
//        sentenceSimilarityDao.delete(demoValue);
//
//        found = sentenceSimilarityDao.find(found.getId());
//        assertThat(found).isNull();
//    }
//
//}
