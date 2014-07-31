package info.interactivesystems.spade.similarity;

import info.interactivesystems.spade.dao.NilsimsaSimilarityDao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class MissingCategoriesResolverTest extends AbstractTestNGSpringContextTests {

    @Resource
    private MissingCategoriesResolver resolver;

    private List<String> suspiciousUsers;

    @Resource
    private NilsimsaSimilarityDao similartyDao;

    @Test
    public void resolveMissingCategory() {
        Map<String, Boolean> jobs = new LinkedHashMap<>();
        jobs.put("Books", false);
        jobs.put("Movies & TV", false);
        jobs.put("Music", false);
        jobs.put("Electronics", false);
        jobs.put("Home & Kitchen", false);
        jobs.put("Toys & Games", false);
        jobs.put("Health & Personal Care", true);
        jobs.put("Amazon Instant Video", true);
        jobs.put("Sports & Outdoors", true);
        jobs.put("Video Games", true);
        jobs.put("Tools & Home Improvement", true);
        jobs.put("Beauty", true);
        jobs.put("Pet Supplies", true);
        jobs.put("Baby", true);
        jobs.put("Patio ", true);
        jobs.put("Automotive", true);

        suspiciousUsers = similartyDao.listSuspicioussUser();

        for (Entry<String, Boolean> job : jobs.entrySet()) {
            resolver.resolveMissingCategory(job.getKey(), suspiciousUsers, job.getValue());
        }

    }
}
