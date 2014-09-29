package info.interactivesystems.spade;

import org.junit.Test;

public class scriptGenerator {

	private String query = "UPDATE stylometry AS s "
			+ "JOIN "
			+ "reviews AS r ON s.id = r.stylometry_id "
			+ "SET  "
			+ "s.densityFunction = density * ln(length(content)) "
			+ "WHERE "
			+ "s.id >= %s AND s.id <= %s;";

	private String avgQuery = "UPDATE products "
			+ "SET "
			+ "products.rating = (SELECT "
			+ "AVG(reviews.rating) "
			+ "FROM "
			+ "reviews "
			+ "WHERE "
			+ "reviews.Product_fk = products.id) "
			+ "WHERE "
			+ "products.randomID >= %s "
			+ "AND products.randomID <= %s;";

	private String varianceQuery = "UPDATE reviews as r "
			+ "JOIN "
			+ "products as p ON r.Product_fk = p.id "
			+ "SET "
			+ "r.variance = ABS(r.rating - p.rating) "
			+ "WHERE "
			+ "p.randomID >= %s AND p.randomID <= %s; ";

	private String stdQuery = "UPDATE products AS p "
			+ "SET "
			+ "p.ratingDeviation = (SELECT STD(r.rating) FROM reviews AS r WHERE r.Product_fk = p.id and r.uniquee = true) "
			+ "WHERE p.randomID >= %s AND p.randomID <= %s;";

	private String densityQuery = "UPDATE spade.reviews " +
			"        JOIN " +
			"    stylometry ON reviews.stylometry_id = stylometry.id  " +
			"SET  " +
			"    reviews.density = stylometry.densityFunction " +
			"WHERE stylometry.id >= %s AND stylometry.id < %s;";

	@Test
	public void test() {
		int oldCounter;
		for (int counter = 500000; counter < 16264025; ) {
			oldCounter = counter;
			counter += 100000;
			System.out.println(String.format(densityQuery, oldCounter, counter));
		}
	}

}
