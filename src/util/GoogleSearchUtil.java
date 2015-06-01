package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Google検索をして、Webページデータからリンク情報をぶっこ抜く
 * <pre>
 * ※GoogleCustomSearchAPIがあるが、無料は100件なので、正直使えないからこの方法にした。
 * ※強引に取ってきてるので、HTMLの構成が少しでも変わると取れない。
 * ※ TODO まだバグあり。10件にしても10件取れない。
 * </pre>
 * @author maruki.shogo
 *
 */
public class GoogleSearchUtil
{
	private static final String CLASS_NAME = Thread.currentThread().getStackTrace()[1].getClassName();
	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		//		String
		Map<String, String> map = getTitleDescriptionMap("YouTube", 1);
		System.out.println(map.size());
		System.out.println(map);
	}

	private static String TITLE_START_STR = "\">";
	private static String TITLE_END_STR = "</a></h3>";
	private static String DESCRIPTION_START_STR = "<span class=\"st\">";
	private static String DESCRIPTION_END_STR = "</span>";
	private static String[] REMOVE_STR_ARY = { "<b>", "</b>", "\r", "\n" };

	public static final String BASE_URL = "https://www.google.co.jp/search?hl=lang_ja&lr=ja&safe=active&num=";
	public static final String QUERY_URL = "&q=";

	static {
		ProxyUtil.setProxy();

	}

	private static InputStream getInputStream(String keyword, int cnt) throws IOException, InterruptedException {
		// 検索の時は、URLエンコードが必要
		keyword = URLEncoder.encode(keyword, "UTF8");
		URL url = new URL(BASE_URL + cnt + QUERY_URL + keyword);
		ThreadUtil.checkInterrupt(CLASS_NAME);
		ThreadUtil.waitExe(CLASS_NAME);
		ThreadUtil.checkInterrupt(CLASS_NAME);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);//POST可能にする
		con.setRequestProperty("User-Agent", "Mozilla/5.0");// ヘッダを設定
		con.setRequestProperty("Accept-Language", "ja");// ヘッダを設定

		InputStream is = con.getInputStream();
		ThreadUtil.checkInterrupt(CLASS_NAME);
		return is;
	}

	//	@SuppressWarnings("deprecation")
	private static String getHtmlCode(String keyword, int cnt) throws InterruptedException {
		String code = null;
		InputStream is = null;
		try {
			is = getInputStream(keyword, cnt);
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF8"));

			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\r\n"); // ラインセパレータは決めうち
			}
			code = sb.toString();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return code;
	}

	public static Map<String, String> getTitleDescriptionMap(String keyword, int cnt) throws InterruptedException {
		Map<String, String> map = new HashMap<>();
		String code = getHtmlCode(keyword, cnt);
		//				System.out.println("■code：" + code);
		int beforeIndex = 1;
		try {
			while (true) {
				// タイトル
				int endIndex = code.indexOf(TITLE_END_STR, beforeIndex + 1);// 終了の位置を探す
				int beginIndex = 0;
				for (int i = 1; i < 100; i++) {
					beginIndex = code.indexOf(TITLE_START_STR, endIndex - i); // 少しさかのぼって開始の位置を探す
					if (beginIndex < endIndex) {
						break;
					}
				}
				beforeIndex = endIndex;
				if (beginIndex == -1 || endIndex == -1 || beginIndex >= endIndex) {
					break;
				}
				beginIndex += TITLE_START_STR.length();
				String title = code.substring(beginIndex, endIndex);
				for (String removeStr : REMOVE_STR_ARY) {
					title = title.replaceAll(removeStr, "");
				}
				title = StringUtil.decodeHtml(title);
				//				System.out.println("■title：" + title);

				// 内容
				beginIndex = code.indexOf(DESCRIPTION_START_STR, beforeIndex);
				endIndex = code.indexOf(DESCRIPTION_END_STR, beginIndex + 1);
				beforeIndex = endIndex;
				if (beginIndex == -1 || endIndex == -1) {
					break;
				}
				beginIndex += DESCRIPTION_START_STR.length();
				String description = code.substring(beginIndex, endIndex);
				for (String removeStr : REMOVE_STR_ARY) {
					description = description.replaceAll(removeStr, "");
				}
				description = StringUtil.decodeHtml(description);
				//				System.out.println("■description：" + description);
				map.put(title, description);
			}
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		return map;
	}

}