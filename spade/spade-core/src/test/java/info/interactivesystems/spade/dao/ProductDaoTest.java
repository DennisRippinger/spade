package info.interactivesystems.spade.dao;

import info.interactivesystems.spade.entities.Product;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

// @Test(groups = { "FunctionTest" })
@ContextConfiguration(locations = {"classpath:beans.xml"})
public class ProductDaoTest extends AbstractTestNGSpringContextTests {

	@Resource
	private ProductDao productDao;

	private Product demoValue;

	@Test
	public void delete() {
		productDao.save(demoValue);
		Product found = productDao.find(demoValue.getId());

		assertThat(found).isNotNull();
		assertThat(found).isEqualsToByComparingFields(demoValue);

		productDao.delete(demoValue);

		found = productDao.find(found.getId());
		assertThat(found).isNull();
	}

	@Test
	public void find() {
		productDao.save(demoValue);
		Product found = productDao.find(demoValue.getId());

		assertThat(found).isNotNull();
		assertThat(found).isEqualsToByComparingFields(demoValue);

		productDao.delete(demoValue);

		found = productDao.find(found.getId());
		assertThat(found).isNull();
	}

	@Test
	public void save() {
		productDao.save(demoValue);
		Product found = productDao.find(demoValue.getId());

		assertThat(found).isNotNull();
		assertThat(found).isEqualsToByComparingFields(demoValue);

		productDao.delete(demoValue);

		found = productDao.find(found.getId());
		assertThat(found).isNull();
	}

	@BeforeTest
	private void getDemoProduct() {
		Product result = new Product();
		result.setId("AED12345");
		result.setName("Demo Product");
		result.setRating(4.5);

		demoValue = result;
	}

}