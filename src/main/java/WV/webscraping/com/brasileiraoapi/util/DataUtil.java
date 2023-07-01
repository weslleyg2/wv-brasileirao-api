package wv.webscraping.com.brasileiraoapi.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {
	public static String formataDateEmString(Date data, String mask) {
		DateFormat fomratter = new SimpleDateFormat(mask);
		return fomratter.format(data);
	}
}
