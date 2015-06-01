package incapacity.talk;

import incapacity.Data;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

public class TalkPattern extends Talk {

	public TalkPattern(String talkFilePath, String talkName) throws IOException {
		super(talkFilePath, talkName);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected Data createData(List<String> list) {
		Data data = new Data();

		for (String line : list) {
			String[] keyValue = line.split(",");
			String key = keyValue[0];
			String value = keyValue[1];
			data.put(key, value);
		}
		return data;
	}

	@Override
	public boolean isTalk(String input) {
		Data data = getData();
		// 完全一致
		if (data.containsKey(input)) {
			setTalkContent(data.get(input));
			return true;
		}
		// 部分一致
		for (Entry<String, String> entry : data.entrySet()) {
			String key = entry.getKey();
			if (input.matches(".*" + key + ".*")) {
				String value = entry.getValue();
				setTalkContent(value);
				return true;
			}
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
