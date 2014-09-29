package info.interactivesystems.spade.dao;

import info.interactivesystems.spade.entities.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(locations = {"classpath:beans.xml"})
public class UserDaoTest extends AbstractTestNGSpringContextTests {

	@Resource
	private UserDao userDao;

	private User demoUser;

	@Test(groups = {"functionTest"})
	public void delete() {
		userDao.save(demoUser);
		User found = userDao.find(demoUser.getId());

		assertThat(found).isNotNull();
		assertThat(found).isEqualsToByComparingFields(demoUser);

		userDao.delete(demoUser);

		found = userDao.find(found.getId());
		assertThat(found).isNull();

	}

	@Test
	public void find() {
		userDao.save(demoUser);
		User found = userDao.find(demoUser.getId());

		assertThat(found).isNotNull();
		assertThat(found).isEqualsToByComparingFields(demoUser);

		userDao.delete(demoUser);

		found = userDao.find(found.getId());
		assertThat(found).isNull();
	}

	@Test
	public void save() {
		userDao.save(demoUser);
		User found = userDao.find(demoUser.getId());

		assertThat(found).isNotNull();
		assertThat(found).isEqualsToByComparingFields(demoUser);

		userDao.delete(demoUser);

		found = userDao.find(found.getId());
		assertThat(found).isNull();
	}

	@BeforeTest
	private void getDemoUser() {
		User result = new User();
		result.setId("123");
		result.setName("Test User");
		result.setNumberOfReviews(5);

		demoUser = result;
	}
}
