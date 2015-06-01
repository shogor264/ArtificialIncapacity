package util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GoogleSuggestAPIUtil
{
	private static final String CLASS_NAME = Thread.currentThread().getStackTrace()[1].getClassName();
	public static void main(String[] args) throws InterruptedException {

		System.out.println(getList("アニメ"));
	}


	public static final String URL = "http://www.google.co.jp/complete/search?hl=lang_ja&lr=ja&ie=utf_8&oe=utf_8&output=xml&q=";

	static {
		ProxyUtil.setProxy();

	}

	private static InputStream getInputStream(String keyword) throws IOException, InterruptedException {
		// 検索の時は、URLエンコードが必要
		keyword = URLEncoder.encode(keyword, "UTF8");
		String urlStr = URL + keyword;
		URL url = new URL(urlStr);
//		System.out.println(url);
		ThreadUtil.checkInterrupt(CLASS_NAME);
		ThreadUtil.waitExe(CLASS_NAME);
		ThreadUtil.checkInterrupt(CLASS_NAME);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		InputStream is = con.getInputStream();
		ThreadUtil.checkInterrupt(CLASS_NAME);
		return is;
	}

	public static List<String> getList(String keyword) throws InterruptedException {
		 List<String> list = new ArrayList<>();
		InputStream is = null;
		try {
			is = getInputStream(keyword);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(is);

			NodeList suggestionNodeList = document.getElementsByTagName("suggestion");

			for (int i = 0; i < suggestionNodeList.getLength(); i++) {
				Node suggestionNode = suggestionNodeList.item(i);
				NamedNodeMap  attr = suggestionNode.getAttributes();
				Node dataNode = attr.getNamedItem("data");
				String text = dataNode.getTextContent();
				list.add(text);
			}

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (SAXException e) {
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
		return list;
	}
}