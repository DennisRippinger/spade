package info.interactivesystems.spade;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class OrderTest {

	@Test
	public void test() {

		String[] tmp = {"A", "B", "C", "D", "E", "F", "G", "H"};

		List<String> reviews = Arrays.asList(tmp);
		Integer outerCounter = 0;
		Integer innerCounter = 1;
		Integer extraCounter = 2;
		for (; outerCounter <= reviews.size() - 1; outerCounter++) {

			for (; innerCounter < reviews.size(); innerCounter++) {
				log.info("{}-{}", reviews.get(outerCounter), reviews.get(innerCounter));
			}

			innerCounter = extraCounter++;
		}
	}
}
