package util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.lang.StringEscapeUtils;

import debug.Debug;

public class StringUtil {
	public static boolean isNullOrEmpty(String str) {
		return str == null || "".equals(str);
	}

	public static boolean isSpace(String targetStr) {
		if (isNullOrEmpty(targetStr)) {
			return true;
		}
		String str = targetStr;
		str = str.replaceAll("\r", "");
		str = str.replaceAll("\n", "");
		str = str.replaceAll("\t", "");
		str = str.trim();
		return isNullOrEmpty(str);
	}

	public static boolean isUTF8(String srcStr)
	{
		try {
			byte[] src = srcStr.getBytes();
			byte[] tmp = new String(src, "UTF8").getBytes("UTF8");
			return Arrays.equals(tmp, src);
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

	public static String decodeHtml(String str) {
		//		str = str.replaceAll("&amp;", "&");
		//		str = str.replaceAll("&quot;", "\"");
		//		str = str.replaceAll("&gt;", ">");
		//		str = str.replaceAll("&lt;", "<");
		//		str = str.replaceAll("&nbsp;", " ");
		str = StringEscapeUtils.unescapeHtml(str);
		str = str.replaceAll("<br>", "");
		return str;
	}

	private static final String[] ABBREVIATION_STR_ARY = { "。", "、", "．", ".", "，", "," };

	/**
	 * 文章をいい感じに省略
	 * @param str
	 * @return
	 */
	public static String abbreviate(String str, int firstIndex) {

		int index = -1;
		for (String abbreviationStr : ABBREVIATION_STR_ARY) {
			index = str.indexOf(abbreviationStr, firstIndex);
			if (index != -1 && index < 200) {
				break;
			}
		}
		if (index == -1 || index > 200) {
			index = 200;
		}
		try {
			str = str.substring(0, index + 1) + "...";
		} catch (StringIndexOutOfBoundsException e) {
			// 範囲外なら
			//			e.printStackTrace();
		}
		return str;
	}

	public static String removeReplyStr(String str) {
		int startIndex = 0;
		while (startIndex < str.length()) {
			if (!(str.charAt(startIndex) == '@')) {
				startIndex++;
				continue;
			}
			int endIndex = str.indexOf(" ", startIndex);
			if (endIndex == -1) {
				endIndex = str.length();
			} else {
				endIndex++;
			}
			Debug.log(str);
			Debug.log(startIndex + "," + endIndex);
			str = str.substring(0, startIndex) + str.substring(endIndex);
		}
		return str;
	}

	public static String addSepOr(String str) {
		if (str.length() < 2) {
			return str;
		}
		int length = str.length();
		String before = str.substring(0, length / 2);
		String after = str.substring(length / 2 + 1);
		return before + " or " + after;

	}

	public static void main(String[] args) {
		System.out.println(removeReplyStr("@aaa @bcde bcd"));
		System.out.println(removeReplyStr("@aaa bcd @bcde"));
	}
}
