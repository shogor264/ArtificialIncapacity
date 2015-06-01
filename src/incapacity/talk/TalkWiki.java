package incapacity.talk;

import incapacity.Data;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import util.WikipediaAPIUtil;

public class TalkWiki extends Talk {
	public TalkWiki(String talkFilePath, String talkName) throws IOException {
		super(talkFilePath, talkName);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected Data createData(List<String> list) {
		return null;
	}

	@Override
	public boolean isTalk(String input) throws InterruptedException {
		Map<String, String> map = WikipediaAPIUtil.getMap(input);
		if (!map.isEmpty() && map.containsKey("body")) {
			String str = map.get("body").replaceAll("\\<br/\\>", "\r\n");
			//			String[] ary = map.get("body").split("<br/>");
//			setTalkContent("「" + input + "」について調べてみたよ。\r\n" + StringUtil.abbreviate(str, 20) + "\r\n");
			setTalkContent("「" + input + "」について調べてみたよ。\r\n" + str + "\r\n");
			return true;
		}
		return false;
	}
	//
	//	public static void printPattern(String str) {
	//		System.out.println("pattern>" + str);
	//	}
	//
	//	public static void printRandom(String str) {
	//		System.out.println("random>" + str);
	//	}
	//
	//	public static void printSame(String str) {
	//		System.out.println("same>" + str);
	//	}
}
