package io.loli.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileNameGenerator {
	private static SimpleDateFormat format;

	public static String generate() {
		if (format == null) {
			format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss的屏幕截图");
		}
		return format.format(new Date());
	}
}
