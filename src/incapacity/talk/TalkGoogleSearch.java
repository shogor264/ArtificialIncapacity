package incapacity.talk;

import incapacity.Data;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import util.GoogleSearchUtil;

public class TalkGoogleSearch extends Talk {
	public TalkGoogleSearch(String talkFilePath, String talkName) throws IOException {
		super(talkFilePath, talkName);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected Data createData(List<String> list) {
		return null;
	}

	@Override
	public boolean isTalk(String input) throws InterruptedException {
		Map<String, String> map = GoogleSearchUtil.getTitleDescriptionMap(input, 3);
		if (map.isEmpty()) {
			return false;
		}
		StringBuilder sb = new StringBuilder();
		int cnt = 0;
		for (Entry<String, String> entry : map.entrySet()) {
			sb.append((char) ('①' + cnt++));
			sb.append("リンク名：");
			sb.append(entry.getKey());
			sb.append("\r\n　内容：");
			sb.append(entry.getValue());
			sb.append("\r\n");
		}
		String str = sb.toString();
		//		setTalkContent("「" + input + "」に関する情報だよ。\r\n" + StringUtil.abbreviate(str, 50) + "\r\n");
		setTalkContent("「" + input + "」に関する情報だよ。\r\n" + str + "\r\n");
		return true;
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
