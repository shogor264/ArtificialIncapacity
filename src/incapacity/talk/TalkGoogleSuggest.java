package incapacity.talk;

import incapacity.Data;

import java.io.IOException;
import java.util.List;

import util.GoogleSuggestAPIUtil;

public class TalkGoogleSuggest extends Talk {
	public TalkGoogleSuggest(String talkFilePath, String talkName) throws IOException {
		super(talkFilePath, talkName);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected Data createData(List<String> list) {
		return null;
	}

	@Override
	public boolean isTalk(String input) throws InterruptedException {
		List<String> list = GoogleSuggestAPIUtil.getList(input);
		if (list.isEmpty()) {
			return false;
		}
		StringBuilder sb = new StringBuilder();
		String comma = "";
		for (String suggest : list) {
			sb.append(comma);
			comma = ", ";
			sb.append(suggest);
		}
		String str = sb.toString();
		setTalkContent ("「" + input + "」の関連キーワードだよ。\r\n" + str + "\r\n");
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
