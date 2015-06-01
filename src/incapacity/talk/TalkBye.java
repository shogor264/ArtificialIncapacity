package incapacity.talk;

import incapacity.Data;

import java.io.IOException;
import java.util.List;

public class TalkBye extends Talk {

	public static final String END = "またね";
	public TalkBye(String talkFilePath, String talkName) throws IOException {
		super(talkFilePath, talkName);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected Data createData(List<String> list) {
		Data data = new Data();
		int i = 0;
		for (String line : list) {
			String key = String.valueOf(i);
			String value = line;
			data.put(key, value);
		}
		return data;
	}

	@Override
	public boolean isTalk(String input) {
		Data data = getData();
		if (END.equals(input)) {
			setTalkContent(data.get("0"));
			return true;
		}
		return false;
	}
}
