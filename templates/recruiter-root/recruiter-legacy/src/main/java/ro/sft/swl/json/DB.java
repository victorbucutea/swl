package ro.sft.swl.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class DB {

	public static String[] PHONES = null;

	static {

		InputStream phonesJson = DB.class.getClassLoader().getResourceAsStream("phones.json");

		try {
			String phones = IOUtils.toString(phonesJson);
			PHONES = phones.split("\\},");
			//PHONES = stripAll(PHONES);

			List<String> phonesList = new ArrayList<String>();

			for (String s : PHONES) {
				String phone = s;
				//if (indexOf(PHONES, s) != (PHONES.length - 1)) {
				phone = s.concat("\r\n}");
				//}
				phonesList.add(phone);
			}

			PHONES = phonesList.toArray(PHONES);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String asJson() {
		StringBuffer responseStr = new StringBuffer("[");

		for (String s : PHONES) {
			responseStr.append(s + ",");
		}

		responseStr.delete(responseStr.lastIndexOf(","), responseStr.length());

		responseStr.append("]");
		return responseStr.toString();
	}
}
