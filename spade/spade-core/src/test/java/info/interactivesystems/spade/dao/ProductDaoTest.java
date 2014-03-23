package info.interactivesystems.spade.dao;

import static org.assertj.core.api.Assertions.assertThat;
import info.interactivesystems.spade.entities.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = { "classpath:beans.xml" })
public class ProductDaoTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ProductDao productDao;

	@Test
	public void find() {
		assertThat(productDao).isNotNull();

		Product result = productDao.find("1934148644");
		assertThat(result.getName())
				.isEqualTo(
						"Nikon D90 inBrief laminated reference card by Blue Crane Digital (Apr 15, 2009)");
	}
}
