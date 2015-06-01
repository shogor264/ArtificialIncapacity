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

import org.codehaus.jackson.map.ObjectMapper;

public class WikipediaAPIUtil
{
	private static final String CLASS_NAME = Thread.currentThread().getStackTrace()[1].getClassName();
	public static void main(String[] args) throws InterruptedException {

		System.out.println(getMap("アニメ"));
	}

	public static final String URL = "http://wikipedia.simpleapi.net/api?output=json&keyword=";

	static {
		ProxyUtil.setProxy();

	}

	private static InputStream getInputStream(String keyword) throws IOException, InterruptedException {
		// 検索の時は、URLエンコードが必要
		keyword = URLEncoder.encode(keyword, "UTF8");
		String urlStr = URL + keyword;
		URL url = new URL(urlStr);
		ThreadUtil.checkInterrupt(CLASS_NAME);
		ThreadUtil.waitExe(CLASS_NAME);
		ThreadUtil.checkInterrupt(CLASS_NAME);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		InputStream is = con.getInputStream();
		ThreadUtil.checkInterrupt(CLASS_NAME);
		return is;
	}

	public static Map<String, String> getMap(String keyword) throws InterruptedException {
		Map<String, String> map = new HashMap<>();
		InputStream is = null;
		try {
			is = getInputStream(keyword);
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF8"));

			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\r\n"); // ラインセパレータは決めうち
			}
			// JSON -> Map　※1個目以外つぶします
			String jsonStr = sb.toString();
			if ('[' == jsonStr.charAt(0)) {
				jsonStr = jsonStr.replaceAll("\\[", "");
				jsonStr = jsonStr.replaceAll("\\]", "");
			}
			ObjectMapper mapper = new ObjectMapper();
			map = mapper.readValue(jsonStr, HashMap.class);
			if (map == null) {
				map = new HashMap<>();
			}
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
		return map;
	}
}