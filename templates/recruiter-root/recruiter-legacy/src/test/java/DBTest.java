import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Ignore;
import org.junit.Test;

import ro.sft.swl.json.DB;

@Ignore
public class DBTest {

	@Test
	public void numberOfPhones() {

		assertEquals(20, DB.PHONES.length);
	}

	@Test
	public void correctlyRead() {
		assertEquals(
				DB.PHONES[15],
				"{\r\n        \"age\": 15, \r\n        \"carrier\": \"US Cellular\", \r\n        \"id\": \"samsung-mesmerize-a-galaxy-s-phone\", \r\n"
						+ "        \"imageUrl\": \"img/phones/samsung-mesmerize-a-galaxy-s-phone.0.jpg\", \r\n"
						+ "        \"name\": \"Samsung Mesmerize\\u2122 a Galaxy S\\u2122 phone\", \r\n"
						+ "        \"snippet\": \"The Samsung Mesmerize\\u2122 delivers a cinema quality experience like you\\u2019ve never seen before. "
						+ "Its innovative 4\\u201d touch display technology provides rich picture brilliance,even outdoors\"\r\n}");

	}

	@Test
	public void cleanEnding() {
		assertFalse(DB.PHONES[19].endsWith("\r\n}"));
	}
}
