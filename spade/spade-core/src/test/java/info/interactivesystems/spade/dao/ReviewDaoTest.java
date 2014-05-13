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
package info.interactivesystems.spade.dao;

import static org.assertj.core.api.Assertions.assertThat;
import info.interactivesystems.spade.entities.Review;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Test for the ReviewDao.
 * 
 * @author Dennis Rippinger
 * 
 */
@Test(groups = { "functionTest" })
@ContextConfiguration(locations = { "classpath:beans.xml" })
public class ReviewDaoTest extends AbstractTestNGSpringContextTests {

    @Resource
    private ReviewDao reviewDao;

    private Review demoValue;

    @BeforeTest
    private void getDemoReview() {
        Review result = new Review();
        result.setAuthorId("12345ABC");
        result.setContent("Text String");
        result.setHelpfulVotes(5);
        result.setId("AED12345");
        result.setTitle("Test Title");

        demoValue = result;
    }

    @Test(groups = { "functionTest" })
    public void delete() {
        reviewDao.save(demoValue);
        Review found = reviewDao.find(demoValue.getId());

        assertThat(found).isNotNull();
        assertThat(found).isEqualsToByComparingFields(demoValue);

        reviewDao.delete(demoValue);

        found = reviewDao.find(found.getId());
        assertThat(found).isNull();

    }

    @Test
    public void find() {
        reviewDao.save(demoValue);
        Review found = reviewDao.find(demoValue.getId());

        assertThat(found).isNotNull();
        assertThat(found).isEqualsToByComparingFields(demoValue);

        reviewDao.delete(demoValue);

        found = reviewDao.find(found.getId());
        assertThat(found).isNull();
    }

    @Test
    public void save() {
        reviewDao.save(demoValue);
        Review found = reviewDao.find(demoValue.getId());

        assertThat(found).isNotNull();
        assertThat(found).isEqualsToByComparingFields(demoValue);

        reviewDao.delete(demoValue);

        found = reviewDao.find(found.getId());
        assertThat(found).isNull();
    }

}
